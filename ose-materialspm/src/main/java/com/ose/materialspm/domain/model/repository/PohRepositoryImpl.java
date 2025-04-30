package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.dto.PohDTO;
import com.ose.materialspm.entity.PoDetail;
import com.ose.materialspm.entity.ViewMxjValidPohEntity;
import com.ose.repository.BaseRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * 合同查询。
 */
public class PohRepositoryImpl extends BaseRepository implements PohRepositoryCustom {

    /**
     * 查询合同。
     *
     * @param criteriaDTO 查询条件
     * @return 合同查询结果分页数据
     */
    @Override
    public Page<ViewMxjValidPohEntity> getPohs(PohDTO pohDTO) {

        SQLQueryBuilder<ViewMxjValidPohEntity> sqlQueryBuilder = getSQLQueryBuilder(ViewMxjValidPohEntity.class)
            .is("projId", pohDTO.getSpmProjId())
            .like("poNumber", pohDTO.getPoNumber())
            .is("buyer", pohDTO.getBuyer())
            .desc("id");

        sqlQueryBuilder = sqlQueryBuilder
            .paginate(pohDTO.toPageable())
            .exec();

        return sqlQueryBuilder.page();
    }

    @Override
    public Page<PoDetail> getDetail(PohDTO pohDTO) {

        String pohId = pohDTO.getPohId();
        String poNumber = pohDTO.getPoNumber();
        int pageno = pohDTO.getPage().getNo();
        int pagesize = pohDTO.getPage().getSize();

        EntityManager entityManager = getEntityManager();

        String sqlForCnt = new StringBuffer()
            .append("SELECT count(pli.ident) ")
            .append(" FROM m_po_line_items pli, mv_mxj_commodity_desc cc_desc, (select poh_id, order_type,inq_id from m_po_headers where po_number = :poNumber) poh ")
            .append(" WHERE pli.poh_id = poh.poh_id ")
            .append(" AND pli.ident=cc_desc.ident ")
            .append(" AND m_pck_mscm.get_poli_ind(poli_id, parent_poli_id, :pohId) = 'Y'")
            .toString();

        System.out.println(sqlForCnt);

        Query queryForCnt = entityManager.createNativeQuery(sqlForCnt);
        queryForCnt.setParameter("poNumber", poNumber);
        queryForCnt.setParameter("pohId", pohId);
        BigDecimal size = (BigDecimal) queryForCnt.getSingleResult();

        System.out.println("size=" + size);

        String sql = new StringBuffer()
            .append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM (")
            .append(" SELECT pli.ident, cc_desc.commodity_code, cc_desc.short_desc,  poli_qty, pli.poh_id, order_type,  pli.poli_unit_price,  ")
            .append(" decode(poli_qty,0,0 ,m_pck_po.GET_POLI_OTHER_COST(pli.poli_id,  pli.currency_id, poh.inq_id) /poli_qty)  tax_cost,")
            .append(" ( select unit_code from  m_units where unit_id = pli.qty_unit_id) unit_code,")
            .append(" (select r.r_code||'('||r.r_supp||')' from m_reqs r,m_req_line_items ri where r.r_id = ri.r_id and ri.rli_id=pli.rli_id) req_code ,")
            .append(" (select sum(get_mrr_qty(ish.poli_id, ish.item_ship_pos, ish.item_ship_sub_pos)) from m_item_ships ish where ish.poli_id =pli.poli_id group by ish.poli_id ) inv_qty ")
            .append(" FROM m_po_line_items pli, mv_mxj_commodity_desc cc_desc, (select poh_id, order_type,inq_id from m_po_headers where po_number = :poNumber) poh ")
            .append(" WHERE pli.poh_id = poh.poh_id ")
            .append(" AND pli.ident=cc_desc.ident ")
            .append(" AND m_pck_mscm.get_poli_ind(poli_id, parent_poli_id, :pohId) = 'Y'")
            .append(" ) A where ROWNUM <= :end) WHERE RN > :start ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("poNumber", poNumber);
        query.setParameter("pohId", pohId);
        query.setParameter("start", (pageno - 1) * pagesize);
        query.setParameter("end", (pageno) * pagesize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        PoDetail po = null;
        List<PoDetail> rsList = new ArrayList<PoDetail>();
        for (Map<String, Object> m : list) {
            po = new PoDetail();
            po.setIdent(m.get("IDENT"));
            po.setCommodityCode(m.get("COMMODITY_CODE"));
            po.setShortDesc(m.get("SHORT_DESC"));
            po.setPoliQty(m.get("POLI_QTY"));
            po.setPohId(m.get("POH_ID"));
            po.setOrderType(m.get("ORDER_TYPE"));
            // po.setPoliUnitPrice(m.get("POLI_UNIT_PRICE"));
            // po.setTaxCost(m.get("TAX_COST"));
            po.setUnitCode(m.get("UNIT_CODE"));
            po.setReqCode(m.get("REQ_CODE"));
            po.setInvQty(m.get("INV_QTY"));
            rsList.add(po);
        }

        Page<PoDetail> result = new PageImpl<PoDetail>(rsList, pohDTO.toPageable(), size.longValue());

        return result;
    }

    @Override
    public List<PoDetail> getDetailNoPage(PohDTO pohDTO) {

        String pohId = pohDTO.getPohId();
        String poNumber = pohDTO.getPoNumber();

        EntityManager entityManager = getEntityManager();

        String sql = new StringBuffer()
            .append(" SELECT pli.ident, cc_desc.commodity_code, cc_desc.short_desc,  poli_qty, pli.poh_id, order_type,  pli.poli_unit_price,  ")
            .append(" decode(poli_qty,0,0 ,m_pck_po.GET_POLI_OTHER_COST(pli.poli_id,  pli.currency_id, poh.inq_id) /poli_qty)  tax_cost,")
            .append(" ( select unit_code from  m_units where unit_id = pli.qty_unit_id) unit_code,")
            .append(" (select r.r_code||'('||r.r_supp||')' from m_reqs r,m_req_line_items ri where r.r_id = ri.r_id and ri.rli_id=pli.rli_id) req_code ,")
            .append(" (select sum(get_mrr_qty(ish.poli_id, ish.item_ship_pos, ish.item_ship_sub_pos)) from m_item_ships ish where ish.poli_id =pli.poli_id group by ish.poli_id ) inv_qty ")
            .append(" FROM m_po_line_items pli, mv_mxj_commodity_desc cc_desc, (select poh_id, order_type,inq_id from m_po_headers where po_number = :poNumber) poh ")
            .append(" WHERE pli.poh_id = poh.poh_id ")
            .append(" AND pli.ident=cc_desc.ident ")
            .append(" AND m_pck_mscm.get_poli_ind(poli_id, parent_poli_id, :pohId) = 'Y'")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("poNumber", poNumber);
        query.setParameter("pohId", pohId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        PoDetail po = null;
        List<PoDetail> rsList = new ArrayList<PoDetail>();
        for (Map<String, Object> m : list) {
            po = new PoDetail();
            po.setIdent(m.get("IDENT"));
            po.setCommodityCode(m.get("COMMODITY_CODE"));
            po.setShortDesc(m.get("SHORT_DESC"));
            po.setPoliQty(m.get("POLI_QTY"));
            po.setPohId(m.get("POH_ID"));
            po.setOrderType(m.get("ORDER_TYPE"));
            // po.setPoliUnitPrice(m.get("POLI_UNIT_PRICE"));
            // po.setTaxCost(m.get("TAX_COST"));
            po.setUnitCode(m.get("UNIT_CODE"));
            po.setReqCode(m.get("REQ_CODE"));
            po.setInvQty(m.get("INV_QTY"));
            rsList.add(po);
        }

        return rsList;
    }
}
