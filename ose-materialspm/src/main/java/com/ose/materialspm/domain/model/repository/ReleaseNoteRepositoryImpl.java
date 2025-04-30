package com.ose.materialspm.domain.model.repository;

import com.ose.dto.PageDTO;
import com.ose.materialspm.dto.ReleaseNoteListDTO;
import com.ose.materialspm.entity.ReleaseNote;
import com.ose.materialspm.entity.ReleaseNoteHead;
import com.ose.repository.BaseRepository;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 合同查询。
 */
public class ReleaseNoteRepositoryImpl extends BaseRepository implements ReleaseNoteRepositoryCustom {

    @Override
    public Page<ReleaseNoteHead> getReleaseNoteHeadList(String spmProjectId, ReleaseNoteListDTO releaseNoteListDTO) {

        String spmProjId = spmProjectId;
        String relnNumber = releaseNoteListDTO.getSpmRelnNumber();
        int pageno = releaseNoteListDTO.getPage().getNo();
        int pagesize = releaseNoteListDTO.getPage().getSize();

        EntityManager entityManager = getEntityManager();

        String sqlForCnt = new StringBuffer()
            .append(" SELECT count(MRN.RELN_ID) ")
            .append("   FROM M_RELEASE_NOTES MRN, ")
            .append("        V_MXJ_POH VMP")
            .append("  WHERE MRN.PROJ_ID = VMP.PROJ_ID ")
            .append("    AND MRN.POH_ID = VMP.POH_ID ")
            .append("    AND MRN.PROJ_ID = :projId ")
            .append("    AND MRN.RELN_NUMBER LIKE :relnNumber")
            .toString();

        Query queryForCnt = entityManager.createNativeQuery(sqlForCnt);
        queryForCnt.setParameter("projId", spmProjId);
        queryForCnt.setParameter("relnNumber", "%" + relnNumber + "%");
        BigDecimal size = (BigDecimal) queryForCnt.getSingleResult();

        String sql = new StringBuffer()
            .append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM ( ")
            .append(" SELECT VMP.POH_ID, VMP.PO_NUMBER, VMP.PO_SUPP, MRN.RELN_NUMBER, MRN.RELN_ID ")
            .append("   FROM M_RELEASE_NOTES MRN, ")
            .append("        V_MXJ_POH VMP")
            .append("  WHERE MRN.PROJ_ID = VMP.PROJ_ID ")
            .append("    AND MRN.POH_ID = VMP.POH_ID ")
            .append("    AND MRN.PROJ_ID = :projId ")
            .append("    AND MRN.RELN_NUMBER LIKE :relnNumber")
            .append("  ORDER BY MRN.RELN_DATE DESC, VMP.PO_NUMBER DESC, MRN.RELN_NUMBER ASC ")
            .append(" ) A where ROWNUM <= :end) WHERE RN > :start ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projId", spmProjId);
        query.setParameter("relnNumber", "%" + relnNumber + "%");
        query.setParameter("start", (pageno - 1) * pagesize);
        query.setParameter("end", (pageno) * pagesize);


        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        ReleaseNoteHead rs = null;
        List<ReleaseNoteHead> rsList = new ArrayList<ReleaseNoteHead>();
        for (Map<String, Object> m : list) {
            rs = new ReleaseNoteHead();
            rs.setPoNumber(m.get("PO_NUMBER"));
            rs.setPoSupp(m.get("PO_SUPP"));
            rs.setPohId(m.get("POH_ID"));
            rs.setRelnNumber(m.get("RELN_NUMBER"));
            rs.setRelnId(m.get("RELN_ID"));
            rsList.add(rs);
        }

        return new PageImpl<ReleaseNoteHead>(rsList, releaseNoteListDTO.toPageable(), size.longValue());
    }

    @Override
    public ReleaseNoteHead getReleaseNoteHead(String spmProjId, String relnNumber) {

        EntityManager entityManager = getEntityManager();

        String sql = new StringBuffer()
            .append(" SELECT VMP.POH_ID, VMP.PO_NUMBER, VMP.PO_SUPP, MRN.RELN_NUMBER, MRN.RELN_ID ")
            .append("   FROM M_RELEASE_NOTES MRN, ")
            .append("        V_MXJ_POH VMP")
            .append("  WHERE MRN.PROJ_ID = VMP.PROJ_ID ")
            .append("    AND MRN.POH_ID = VMP.POH_ID ")
            .append("    AND MRN.PROJ_ID = :projId ")
            .append("    AND MRN.RELN_NUMBER = :relnNumber")
            .append("  ORDER BY MRN.RELN_DATE DESC, VMP.PO_NUMBER DESC, MRN.RELN_NUMBER ASC ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projId", spmProjId);
        query.setParameter("relnNumber", relnNumber);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        ReleaseNoteHead rs = null;
        List<ReleaseNoteHead> rsList = new ArrayList<ReleaseNoteHead>();
        for (Map<String, Object> m : list) {
            rs = new ReleaseNoteHead();
            rs.setPoNumber(m.get("PO_NUMBER"));
            rs.setPoSupp(m.get("PO_SUPP"));
            rs.setPohId(m.get("POH_ID"));
            rs.setRelnNumber(m.get("RELN_NUMBER"));
            rs.setRelnId(m.get("RELN_ID"));
            rsList.add(rs);
        }

        return rsList.isEmpty() ? null : rsList.get(0);
    }

    @Override
    public Page<ReleaseNote> getReleaseNoteItemsByPage(String spmProjId, String relnNumber, PageDTO pageDTO) {

        int pageno = pageDTO.getPage().getNo();
        int pagesize = pageDTO.getPage().getSize();

        EntityManager entityManager = getEntityManager();

        String sqlForCnt = new StringBuffer()
            .append("SELECT count(MIS.POLI_ID) ")
            .append("   FROM M_ITEM_SHIPS MIS, ")
            .append("        M_PO_LINE_ITEMS MPLI, ")
            .append("        M_RELEASE_NOTES MRN, ")
            .append("        V_MXJ_POH VMP, ")
            .append("        MV_MXJ_COMMODITY_DESC MMCD, ")
            .append("        MVN_DISCIPLINES DP ")
            .append("  WHERE MIS.PROJ_ID = MPLI.PROJ_ID ")
            .append("    AND MIS.PROJ_ID = MRN.PROJ_ID ")
            .append("    AND MIS.PROJ_ID = VMP.PROJ_ID ")
            .append("    AND MIS.PROJ_ID = MMCD.PROJ_ID ")
            .append("    AND MIS.POLI_ID = MPLI.POLI_ID ")
            .append("    AND MIS.RELN_ID = MRN.RELN_ID ")
            .append("    AND MIS.DP_ID = DP.DP_ID ")
            .append("    AND MRN.POH_ID = VMP.POH_ID ")
            .append("    AND MIS.TAG_NUMBER = MMCD.COMMODITY_CODE ")
            .append("    AND DP.NLS_ID = 1 ")
            .append("    AND MRN.RELN_NUMBER = :relnNumber ")
            .append("    AND MIS.PROJ_ID = :projId ")
            .toString();

        Query queryForCnt = entityManager.createNativeQuery(sqlForCnt);
        queryForCnt.setParameter("projId", spmProjId);
        queryForCnt.setParameter("relnNumber", relnNumber);
        BigDecimal size = (BigDecimal) queryForCnt.getSingleResult();

        String sql = new StringBuffer()
            .append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM ( ")
            .append(" SELECT MPLI.IDENT, MIS.ITEM_SHIP_ID, MIS.POLI_ID, VMP.POH_ID, VMP.PO_NUMBER, VMP.PO_SUPP, MRN.RELN_NUMBER, MRN.RELN_ID, ")
            .append("        MIS.QTY_UNIT_ID, (SELECT UNIT_CODE FROM M_UNITS WHERE UNIT_ID = MIS.QTY_UNIT_ID) QTY_UNIT_CODE, ")
            .append("        MPLI.POLI_QTY, MIS.RELN_QTY, MIS.RELN_WEIGHT, MIS.RECV_ON_SITE_QTY, ")
            .append("        MIS.TAG_NUMBER, MMCD.COMMODITY_ID, MMCD.SHORT_DESC, ")
            .append("        DP.DP_ID, DP.DP_CODE, VMP.COMPANY_NAME ")
            .append("   FROM M_ITEM_SHIPS MIS, ")
            .append("        M_PO_LINE_ITEMS MPLI, ")
            .append("        M_RELEASE_NOTES MRN, ")
            .append("        V_MXJ_POH VMP, ")
            .append("        MV_MXJ_COMMODITY_DESC MMCD, ")
            .append("        MVN_DISCIPLINES DP ")
            .append("  WHERE MIS.PROJ_ID = MPLI.PROJ_ID ")
            .append("    AND MIS.PROJ_ID = MRN.PROJ_ID ")
            .append("    AND MIS.PROJ_ID = VMP.PROJ_ID ")
            .append("    AND MIS.PROJ_ID = MMCD.PROJ_ID ")
            .append("    AND MIS.POLI_ID = MPLI.POLI_ID ")
            .append("    AND MIS.RELN_ID = MRN.RELN_ID ")
            .append("    AND MIS.DP_ID = DP.DP_ID ")
            .append("    AND MRN.POH_ID = VMP.POH_ID ")
            .append("    AND MIS.TAG_NUMBER = MMCD.COMMODITY_CODE ")
            .append("    AND DP.NLS_ID = 1 ")
            .append("    AND MRN.RELN_NUMBER = :relnNumber ")
            .append("    AND MIS.PROJ_ID = :projId ORDER BY MPLI.IDENT ASC")
            .append(" ) A where ROWNUM <= :end) WHERE RN > :start ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projId", spmProjId);
        query.setParameter("relnNumber", relnNumber);
        query.setParameter("start", (pageno - 1) * pagesize);
        query.setParameter("end", (pageno) * pagesize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        ReleaseNote rs = null;
        List<ReleaseNote> rsList = new ArrayList<ReleaseNote>();
        for (Map<String, Object> m : list) {
            rs = new ReleaseNote();
            rs.setIdent(m.get("IDENT"));
            rs.setPoNumber(m.get("PO_NUMBER"));
            rs.setPoSupp(m.get("PO_SUPP"));
            rs.setPohId(m.get("POH_ID"));
            rs.setRelnNumber(m.get("RELN_NUMBER"));
            rs.setRelnId(m.get("RELN_ID"));

            rs.setItemShipID(m.get("ITEM_SHIP_ID"));
            rs.setPoliId(m.get("POLI_ID"));
            rs.setQtyUnitId(m.get("QTY_UNIT_ID"));
            rs.setQtyUnitCode(m.get("QTY_UNIT_CODE"));

            rs.setPoliQty((BigDecimal) m.get("POLI_QTY"));
            rs.setRelnQty((BigDecimal) m.get("RELN_QTY"));
            rs.setRelnWeight((BigDecimal) m.get("RELN_WEIGHT"));
            rs.setRecvOnSiteQty((BigDecimal) m.get("RECV_ON_SITE_QTY"));

            rs.setDpId(m.get("DP_ID"));
            rs.setDpCode(m.get("DP_CODE"));

            rs.setTagNumber(m.get("TAG_NUMBER"));
            rs.setCommodityId(m.get("COMMODITY_ID"));
            rs.setShortDesc(m.get("SHORT_DESC"));
            rs.setCompany(m.get("COMPANY_NAME"));
            rsList.add(rs);
        }

        return new PageImpl<>(rsList, pageDTO.toPageable(), size.longValue());
    }

    @Override
    public List<ReleaseNote> getReleaseNoteItems(String spmProjId, String relnNumber) {

        EntityManager entityManager = getEntityManager();

        String sql = new StringBuffer()
            .append(" SELECT MPLI.IDENT, MIS.ITEM_SHIP_ID, MIS.POLI_ID, VMP.POH_ID, VMP.PO_NUMBER, VMP.PO_SUPP, MRN.RELN_NUMBER, MRN.RELN_ID, ")
            .append("        MIS.QTY_UNIT_ID, (SELECT UNIT_CODE FROM M_UNITS WHERE UNIT_ID = MIS.QTY_UNIT_ID) QTY_UNIT_CODE, ")
            .append("        MPLI.POLI_QTY, MIS.RELN_QTY, MIS.RELN_WEIGHT, MIS.RECV_ON_SITE_QTY, ")
            .append("        MIS.TAG_NUMBER, MMCD.COMMODITY_ID, MMCD.SHORT_DESC, ")
            .append("        DP.DP_ID, DP.DP_CODE ")
            .append("   FROM M_ITEM_SHIPS MIS, ")
            .append("        M_PO_LINE_ITEMS MPLI, ")
            .append("        M_RELEASE_NOTES MRN, ")
            .append("        V_MXJ_POH VMP, ")
            .append("        MV_MXJ_COMMODITY_DESC MMCD, ")
            .append("        MVN_DISCIPLINES DP ")
            .append("  WHERE MIS.PROJ_ID = MPLI.PROJ_ID ")
            .append("    AND MIS.PROJ_ID = MRN.PROJ_ID ")
            .append("    AND MIS.PROJ_ID = VMP.PROJ_ID ")
            .append("    AND MIS.PROJ_ID = MMCD.PROJ_ID ")
            .append("    AND MIS.POLI_ID = MPLI.POLI_ID ")
            .append("    AND MIS.RELN_ID = MRN.RELN_ID ")
            .append("    AND MIS.DP_ID = DP.DP_ID ")
            .append("    AND MRN.POH_ID = VMP.POH_ID ")
            .append("    AND MIS.TAG_NUMBER = MMCD.COMMODITY_CODE ")
            .append("    AND DP.NLS_ID = 1 ")
            .append("    AND MRN.RELN_NUMBER = :relnNumber ")
            .append("    AND MIS.PROJ_ID = :projId ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projId", spmProjId);
        query.setParameter("relnNumber", relnNumber);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        ReleaseNote rs = null;
        List<ReleaseNote> rsList = new ArrayList<ReleaseNote>();
        for (Map<String, Object> m : list) {
            rs = new ReleaseNote();
            rs.setIdent(m.get("IDENT"));
            rs.setPoNumber(m.get("PO_NUMBER"));
            rs.setPoSupp(m.get("PO_SUPP"));
            rs.setPohId(m.get("POH_ID"));
            rs.setRelnNumber(m.get("RELN_NUMBER"));
            rs.setRelnId(m.get("RELN_ID"));

            rs.setItemShipID(m.get("ITEM_SHIP_ID"));
            rs.setPoliId(m.get("POLI_ID"));
            rs.setQtyUnitId(m.get("QTY_UNIT_ID"));
            rs.setQtyUnitCode(m.get("QTY_UNIT_CODE"));

            rs.setPoliQty((BigDecimal) m.get("POLI_QTY"));
            rs.setRelnQty((BigDecimal) m.get("RELN_QTY"));
            rs.setRelnWeight((BigDecimal) m.get("RELN_WEIGHT"));
            rs.setRecvOnSiteQty((BigDecimal) m.get("RECV_ON_SITE_QTY"));

            rs.setDpId(m.get("DP_ID"));
            rs.setDpCode(m.get("DP_CODE"));

            rs.setTagNumber(m.get("TAG_NUMBER"));
            rs.setCommodityId(m.get("COMMODITY_ID"));
            rs.setShortDesc(m.get("SHORT_DESC"));
            rsList.add(rs);
        }

        return rsList;
    }
}
