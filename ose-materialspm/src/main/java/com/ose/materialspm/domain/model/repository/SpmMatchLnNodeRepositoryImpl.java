package com.ose.materialspm.domain.model.repository;

//import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.materialspm.dto.SpmMatchLnCriteriaDTO;
import com.ose.materialspm.dto.SpmMatchLnNodeDTO;
import com.ose.materialspm.entity.SpmListPosDTO;
import com.ose.materialspm.entity.SpmMatchLnNode;
import com.ose.materialspm.entity.SpmMatchLns;
import com.ose.repository.BaseRepository;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 请购单列表查询。
 */
public class SpmMatchLnNodeRepositoryImpl extends BaseRepository implements SpmMatchLnNodeRepositoryCustom {

    /**
     * 查询BOM NODE MATCH 总数。
     *
     * @param spmMatchLnNodeDTO
     * @param orgId             组织ID
     * @return BOM NODE MATCH 总数
     * @projId SpmProjectId
     */
    @Override
    public Long getMatchLnNodeCount(SpmMatchLnNodeDTO spmMatchLnNodeDTO) {

        String projId = spmMatchLnNodeDTO.getSpmProjId();
        Date lMod = spmMatchLnNodeDTO.getlMod();
        EntityManager entityManager = getEntityManager();


//(LMOD-to_date('01-JAN-1970','DD-MON-YYYY')) * 86400 - 8*3600 Unix 时间戳
//		 having max(lp.lmod) > to_date('2018-12-23','yyyy-mm-dd')

        StringBuffer sqlBuffer = new StringBuffer()
            .append("SELECT count(1) from ( ")
            .append("SELECT COUNT(1) FROM M_LIST_NODES ln join ")
            .append("M_LIST_POS lp on lp.LN_ID = ln.LN_ID ")
            .append(" where ln.PROJ_ID =:projId ")
            .append(" and (ln.DP_ID = 5000 or ln.DP_ID=5027) ")
            .append(" group by ln.LN_ID ");
        if (lMod != null) {
            sqlBuffer.append(" having （(max(lp.LMOD)-to_date('01-JAN-1970','DD-MON-YYYY')) * 86400 - 8*3600） > :lMod ");
        }
        sqlBuffer.append(")");
        String sqlForCnt = sqlBuffer.toString();

        Query queryForCnt = entityManager.createNativeQuery(sqlForCnt);
        queryForCnt.setParameter("projId", projId);
        if (lMod != null) {
            queryForCnt.setParameter("lMod", lMod.getTime() / 1000);
        }
        BigDecimal size = (BigDecimal) queryForCnt.getSingleResult();


        return size.longValue();
    }


    /**
     * 查询BOM NODE MATCH 清单。
     *
     * @param spmMatchLnNodeDTO
     * @return BOM NODE MATCH 清单
     */
    @Override
    public List<SpmMatchLnNode> getList(SpmMatchLnNodeDTO spmMatchLnNodeDTO) {
        String projId = spmMatchLnNodeDTO.getSpmProjId();
        int pageno = spmMatchLnNodeDTO.getPageNo();
        int pagesize = spmMatchLnNodeDTO.getPageSize();
        String lnCode = spmMatchLnNodeDTO.getLnCode();
        Date lMod = spmMatchLnNodeDTO.getlMod();
//		Long size = spmMatchLnNodeDTO.getSize();

        EntityManager entityManager = getEntityManager();

        StringBuffer sqlBuffer = new StringBuffer()
            .append("select * from ( select rownum rn, abc.* from (")
            .append("Select ln.LN_ID lnId, ln.LN_CODE as lnCode, max(lp.lmod) as lastModified,")
            .append(" round(decode(sum(lp.quantity),0,0,sum(lp.resv_qty+lp.issue_qty)/sum(lp.quantity)),2) as matchPercent")
            .append(" from M_LIST_NODES ln join M_LIST_POS lp on lp.LN_ID = ln.LN_ID")
            .append(" where ln.PROJ_ID =:projId and (ln.DP_ID = 5000 or ln.DP_ID=5027) ");
        if (!StringUtils.isEmpty(lnCode)) { //根据ln_code节点名称查找
            sqlBuffer.append(" and ln.LN_CODE = :lnCode ");
        }
        sqlBuffer = sqlBuffer.append(" group by ln.LN_ID,ln.ln_code");
        if (lMod != null) {
            sqlBuffer.append(" having（(max(lp.LMOD)-to_date('01-JAN-1970','DD-MON-YYYY')) * 86400 - 8*3600） > :lMod ");
        }
        String sql = sqlBuffer.append(" ) abc) where rn > :start and rn <=:end")
            .toString();

        Query query = entityManager.createNativeQuery(sql, "SpmMatchLnNodeSqlResultMapping");
        query.setParameter("projId", projId);
        query.setParameter("start", (pageno - 1) * pagesize);
        query.setParameter("end", (pageno) * pagesize);
        if (!StringUtils.isEmpty(lnCode)) { //根据ln_code节点名称查找
            query.setParameter("lnCode", lnCode);//.append(" and ln.LN_CODE = :lnCode ");
        }
        if (lMod != null) {
            query.setParameter("lMod", lMod.getTime() / 1000);
        }

        List<SpmMatchLnNode> mlnList = query.getResultList();
//		Page<SpmMatchLnNode> result = new PageImpl<SpmMatchLnNode>(mlnList, spmMatchLnNodeDTO.toPageable(), size.longValue());

        return mlnList;
    }

    /**
     * 查询BOM NODE MATCH 聚合 清单。
     *
     * @param spmMatchLnCriteriaDTO
     * @return BOM NODE MATCH 聚合 清单
     */
    @Override
    public Page<SpmMatchLns> getMatchList(SpmMatchLnCriteriaDTO spmMatchLnCriteriaDTO) {
        String projId = spmMatchLnCriteriaDTO.getSpmProjId();
        int pageno = spmMatchLnCriteriaDTO.getPage().getNo();
        int pagesize = spmMatchLnCriteriaDTO.getPage().getSize();
        List<BigDecimal> lnIds = StringUtils.decode(spmMatchLnCriteriaDTO.getLnIdsString(), new TypeReference<List<BigDecimal>>(){});
//            JSONObject.parseArray(spmMatchLnCriteriaDTO.getLnIdsString(), BigDecimal.class);

//		Set<BigDecimal> lnIds = spmMatchLnCriteriaDTO.getLnIds();

        EntityManager entityManager = getEntityManager();

        StringBuffer sqlBuffer = new StringBuffer()
            .append("select * from (")
            .append("select rownum rn, abc.* from (")
            .append(" SELECT")
            .append(" lp.tag_number material_code,")
            .append(" sum(lp.quantity) bom_quantity,")
            .append(" sum(lp.resv_qty)+sum(lp.issue_qty) matched_quantity,")
            .append(" (case sum(lp.quantity) when 0 then 0 else (sum(lp.resv_qty)+sum(lp.issue_qty))/sum(lp.quantity) end ) match_percent,")
            .append(" mccn.short_desc material_desc")
            .append(" from ")
            .append(" M_LIST_NODES ln join M_LIST_POS lp ON lp.LN_ID = ln.LN_ID ")
            .append(" join M_COMMODITY_CODE_NLS mccn on mccn.commodity_id = lp.commodity_id ")
            .append(" where ln.proj_id = :projId ")
            .append(" and ln.ln_id in (:lnIds)")
            .append(" group by lp.TAG_NUMBER, ")
            .append(" lp.COMMODITY_ID, ")
            .append(" mccn.short_desc ")
            .append(" order by lp.tag_number ")
            .append(") abc ) a where rn > :start and rn <=:end");

        StringBuffer sqlCountBuffer = new StringBuffer()
            .append(" select count(0) from ( ")
            .append(" SELECT ")
            .append(" lp.tag_number material_code, ")
            .append(" sum(lp.quantity) bom_quantity, ")
            .append(" sum(lp.resv_qty)+sum(lp.issue_qty) matched_quantity, ")
            .append(" (case sum(lp.quantity) when 0 then 0 else (sum(lp.resv_qty)+sum(lp.issue_qty))/sum(lp.quantity) end ) match_percent,")
            .append(" mccn.short_desc material_desc")
            .append(" from ")
            .append(" M_LIST_NODES ln join M_LIST_POS lp ON lp.LN_ID = ln.LN_ID ")
            .append(" join M_COMMODITY_CODE_NLS mccn on mccn.commodity_id = lp.commodity_id ")
            .append(" where ln.proj_id = :projId ")
            .append(" and ln.ln_id in (:lnIds)")
            .append(" group by lp.TAG_NUMBER, ")
            .append(" lp.COMMODITY_ID, ")
            .append(" mccn.short_desc ")
            .append(" order by lp.tag_number ")
            .append(") abc");

        String sql = sqlBuffer.toString();

        String sqlCount = sqlCountBuffer.toString();

        Query query = entityManager.createNativeQuery(sql, "SpmMatchLnsSqlResultMapping");

        Query countQuery = entityManager.createNativeQuery(sqlCount);
        query.setParameter("projId", projId);
        countQuery.setParameter("projId", projId);
        query.setParameter("start", (pageno - 1) * pagesize);
        query.setParameter("end", (pageno) * pagesize);
        if (!CollectionUtils.isEmpty(lnIds)) { //根据ln_code节点名称查找
            query.setParameter("lnIds", lnIds);//.append(" and ln.LN_CODE = :lnCode ");
            countQuery.setParameter("lnIds", lnIds);
        }

        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        List<SpmMatchLns> mlns = query.getResultList();

        return new PageImpl<>(mlns, spmMatchLnCriteriaDTO.toPageable(), count.longValue());
    }

    /**
     * 取得 ln_ids 对应的 list_pos 的清单
     *
     * @param spmProjId
     * @param lnIds
     * @return
     */
    @Override
    public List<SpmListPosDTO> getListPos(String spmProjId, List<BigDecimal> lnIds) {
        EntityManager entityManager = getEntityManager();

        /**
         * LN_ID
         * LP_ID
         * DP_ID
         * PROJ_ID
         * LP_POS
         * QUANTITY
         * LMOD
         * COMMODITY_ID
         * IDENT
         * RESV_QTY
         * TAG_NUMBER
         * ISSUE_QTY
         * SHORT_DESC
         */

        String sql = new StringBuffer()
            .append(" SELECT")
            .append(" lp.tag_number tag_number,")
            .append(" lp.ln_id ln_id,")
            .append(" lp.lp_id lp_id,")
            .append(" lp.dp_id dp_id,")
            .append(" lp.proj_id proj_id,")
            .append(" lp.lp_pos lp_pos,")
            .append(" lp.quantity quantity,")
            .append(" lp.lmod lmod,")
            .append(" lp.commodity_id commodity_id,")
            .append(" lp.ident ident,")
            .append(" lp.resv_qty resv_qty,")
            .append(" lp.issue_qty issue_qty,")
            .append(" mccn.short_desc short_desc ")
            .append(" from ")
            .append(" M_LIST_POS lp  ")
            .append(" left join M_COMMODITY_CODE_NLS mccn on mccn.commodity_id = lp.commodity_id ")
            .append(" where lp.proj_id = :projId ")
            .append(" and lp.ln_id in (:lnIds)")
            .toString();

        Query query = entityManager.createNativeQuery(sql, "SpmListPosSqlResultMapping");

        query.setParameter("projId", spmProjId);
        query.setParameter("lnIds", lnIds);

        List<SpmListPosDTO> spmLisPoses = query.getResultList();

        return spmLisPoses;
    }

}
