package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.dto.ReqDetailDTO;
import com.ose.materialspm.entity.ReqDetail;
import com.ose.repository.BaseRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * 请购单列表查询。
 */
public class ReqDetailRepositoryImpl extends BaseRepository implements ReqDetailRepositoryCustom {

    /**
     * 查询请购单详情。
     *
     * @param reqId 请购单id
     * @return 请购单详情分页数据
     */
    @SuppressWarnings("unchecked")
    @Override
    public Page<ReqDetail> getDetail(ReqDetailDTO reqDetailDTO) {

        String reqId = reqDetailDTO.getReqId();
        int pageno = reqDetailDTO.getPage().getNo();
        int pagesize = reqDetailDTO.getPage().getSize();

        EntityManager entityManager = getEntityManager();

        String sqlForCnt = new StringBuffer()
            .append("SELECT count(cc_code.ident_code) ")
            .append(" FROM M_REQ_LINE_ITEMS rli, M_UNITS, mv_mxj_commodity_desc cc_code")
            .append(" WHERE rli.r_id = :reqId")
            .append(" AND rli.IDENT = cc_code.IDENT")
            .append(" AND rli.release_QTY_UNIT_ID = M_UNITS.UNIT_ID")
            .append(" AND (rli.TOTAL_RELEASE_QTY - rli.LAST_TOTAL_RELEASE_QTY) <> 0")
            .toString();

        Query queryForCnt = entityManager.createNativeQuery(sqlForCnt);
        queryForCnt.setParameter("reqId", reqId);
        BigDecimal size = (BigDecimal) queryForCnt.getSingleResult();

        String sql = new StringBuffer()
            .append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM ( ")
            .append(" SELECT cc_code.ident_code as identCode ")
            .append(" ,cc_code.commodity_code as commodityCode, rli.total_release_qty as totalReleaseQty, cc_code.short_desc as shortDesc,")
            .append(" ((rli.TOTAL_RELEASE_QTY) - (rli.LAST_TOTAL_RELEASE_QTY)) as increasePty,")
            .append(" M_UNITS.unit_code as unitCode, rli.rli_pos as rliPos")
            .append(" FROM M_REQ_LINE_ITEMS rli, M_UNITS, mv_mxj_commodity_desc cc_code")
            .append(" WHERE rli.r_id = :reqId")
            .append(" AND rli.IDENT = cc_code.IDENT")
            .append(" AND rli.release_QTY_UNIT_ID = M_UNITS.UNIT_ID")
            .append(" AND (rli.TOTAL_RELEASE_QTY - rli.LAST_TOTAL_RELEASE_QTY) <> 0")
            .append(" ) A where ROWNUM <= :end) WHERE RN > :start ")
            .toString();

        Query query = entityManager.createNativeQuery(sql, "ReqDetailSqlResultMapping");
        query.setParameter("reqId", reqId);
        query.setParameter("start", (pageno - 1) * pagesize);
        query.setParameter("end", (pageno) * pagesize);

        List<ReqDetail> rsList = query.getResultList();
        Page<ReqDetail> result = new PageImpl<ReqDetail>(rsList, reqDetailDTO.toPageable(), size.longValue());

        return result;
    }

    @Override
    public List<ReqDetail> getDetailNoPage(ReqDetailDTO reqDetailDTO) {

        String reqId = reqDetailDTO.getReqId();

        EntityManager entityManager = getEntityManager();

        String sql = new StringBuffer()
            .append(" SELECT cc_code.ident_code as identCode ")
            .append(" ,cc_code.commodity_code as commodityCode, rli.total_release_qty as totalReleaseQty, cc_code.short_desc as shortDesc,")
            .append(" ((rli.TOTAL_RELEASE_QTY) - (rli.LAST_TOTAL_RELEASE_QTY)) as increasePty,")
            .append(" M_UNITS.unit_code as unitCode, rli.rli_pos as rliPos")
            .append(" FROM M_REQ_LINE_ITEMS rli, M_UNITS, mv_mxj_commodity_desc cc_code")
            .append(" WHERE rli.r_id = :reqId")
            .append(" AND rli.IDENT = cc_code.IDENT")
            .append(" AND rli.release_QTY_UNIT_ID = M_UNITS.UNIT_ID")
            .append(" AND (rli.TOTAL_RELEASE_QTY - rli.LAST_TOTAL_RELEASE_QTY) <> 0")
            .toString();

        Query query = entityManager.createNativeQuery(sql, "ReqDetailSqlResultMapping");
        query.setParameter("reqId", reqId);

        List<ReqDetail> rsList = query.getResultList();

        return rsList;
    }
}
