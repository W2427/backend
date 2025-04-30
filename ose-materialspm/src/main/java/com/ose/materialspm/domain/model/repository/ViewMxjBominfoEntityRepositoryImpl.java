package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.dto.*;
import com.ose.materialspm.entity.ViewMxjBominfoEntity;
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
public class ViewMxjBominfoEntityRepositoryImpl extends BaseRepository implements ViewMxjBominfoEntityRepositoryCustom {

    @Override
    public Page<ViewMxjBominfoEntity> search(BominfoListSimpleDTO bominfoListSimpleDTO) {

        SQLQueryBuilder<ViewMxjBominfoEntity> sqlQueryBuilder = getSQLQueryBuilder(ViewMxjBominfoEntity.class)
            .like("tagNumber", bominfoListSimpleDTO.getTagNumber())
            .like("shortDesc", bominfoListSimpleDTO.getShortDesc())
            .like("bompath", bominfoListSimpleDTO.getBomPath())
            .is("projId", bominfoListSimpleDTO.getSpmProjId());

        sqlQueryBuilder = sqlQueryBuilder
            .paginate(bominfoListSimpleDTO.toPageable())
            .exec();

        return sqlQueryBuilder.page();
    }

    @Override
    public Page<FaListResultsDTO> getFahList(FaListDTO faListDTO) {
        String faCode = faListDTO.getFaCode();
        String spmProjId = faListDTO.getSpmProjId();
        int pageno = faListDTO.getPage().getNo();
        int pagesize = faListDTO.getPage().getSize();

        EntityManager entityManager = getEntityManager();

        String sqlForCnt = new StringBuffer()
            .append(" SELECT COUNT(H.FAH_ID) ")
            .append("   FROM M_FA_HEADERS H ")
            .append("  WHERE PROJ_ID = :projId ")
            .append("    AND FAH_CODE LIKE :faCode")
            .toString();

        Query queryForCnt = entityManager.createNativeQuery(sqlForCnt);
        queryForCnt.setParameter("projId", spmProjId);
        queryForCnt.setParameter("faCode", "%" + faCode + "%");
        BigDecimal size = (BigDecimal) queryForCnt.getSingleResult();

        String sql = new StringBuffer()
            .append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM ( ")
            .append("  SELECT * ")
            .append("    FROM M_FA_HEADERS H ")
            .append("   WHERE PROJ_ID= :projId ")
            .append("     AND FAH_CODE LIKE :faCode ")
            .append("   ORDER BY FAH_ID DESC ")
            .append(") A WHERE ROWNUM <= :end) WHERE RN > :start ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projId", spmProjId);
        query.setParameter("faCode", "%" + faCode + "%");
        query.setParameter("start", (pageno - 1) * pagesize);
        query.setParameter("end", (pageno) * pagesize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        FaListResultsDTO rs = null;
        List<FaListResultsDTO> rsList = new ArrayList<FaListResultsDTO>();
        for (Map<String, Object> m : list) {
            rs = new FaListResultsDTO();
            rs.setFahId(m.get("FAH_ID"));
            rs.setProjId(m.get("PROJ_ID"));
            rs.setFahCode(m.get("FAH_CODE"));
            rs.setRunNumber(m.get("RUN_NUMBER"));
            rs.setUserId(m.get("USER_ID"));
            rs.setDpId(m.get("DP_ID"));
            rs.setLstId(m.get("LST_ID"));
            rs.setFahType(m.get("FAH_TYPE"));
            rs.setJobStatus(m.get("JOB_STATUS"));
            rsList.add(rs);
        }

        return new PageImpl<FaListResultsDTO>(rsList, faListDTO.toPageable(), size.longValue());
    }

    @Override
    public FaListResultsDTO getFah(FadListDTO fadListDTO) {
        String fahId = fadListDTO.getFahId();
        String spmProjId = fadListDTO.getSpmProjId();

        EntityManager entityManager = getEntityManager();

        String sql = new StringBuffer()
            .append("  SELECT H.* ")
            .append("    FROM M_FA_HEADERS H ")
            .append("   WHERE H.PROJ_ID= :projId ")
            .append("     AND H.FAH_ID = :fahId ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projId", spmProjId);
        query.setParameter("fahId", fahId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        FaListResultsDTO rs = new FaListResultsDTO();
        List<FaListResultsDTO> rsList = new ArrayList<FaListResultsDTO>();
        for (Map<String, Object> m : list) {
            rs.setFahId(m.get("FAH_ID"));
            rs.setProjId(m.get("PROJ_ID"));
            rs.setFahCode(m.get("FAH_CODE"));
            rs.setRunNumber(m.get("RUN_NUMBER"));
            rs.setUserId(m.get("USR_ID"));
            rs.setDpId(m.get("DP_ID"));
            rs.setLstId(m.get("LST_ID"));
            rs.setFahType(m.get("FAH_TYPE"));
            rs.setJobStatus(m.get("JOB_STATUS"));
            rsList.add(rs);
        }

        return rsList.get(0);
    }

    @Override
    public Page<FadListResultsDTO> getFadList(FadListDTO fadListDTO) {
        String fahId = fadListDTO.getFahId();
        String spmProjId = fadListDTO.getSpmProjId();
        int pageno = fadListDTO.getPage().getNo();
        int pagesize = fadListDTO.getPage().getSize();

        EntityManager entityManager = getEntityManager();

        String sqlForCnt = new StringBuffer()
//            .append(" SELECT COUNT(H.FAH_ID) ")
//            .append("   FROM WV_FAH_ISSUE_ITEMS H ")
//            .append("  WHERE H.PROJ_ID = :projId ")
//            .append("    AND H.FAH_ID = :fahId ")

            .append(" SELECT COUNT(IPR.FAH_ID) ")
            .append(" FROM M_INV_POS_RES IPR, ")
                .append(" M_INV_ITEMS IVI, ")
                .append(" M_IDENTS I, ")
                .append(" M_UNITS U, ")
                .append(" M_SITE_MATL_STATUS SMST, ")
                .append(" M_WAREHOUSES WH, ")
                .append(" M_LOCATIONS L, ")
                .append(" M_HEATS H ")
            .append(" WHERE IPR.IVI_ID = IVI.IVI_ID ")
                .append(" AND IVI.WH_ID = WH.WH_ID ")
                .append(" AND IVI.IDENT = I.IDENT ")
                .append(" AND IVI.UNIT_ID = U.UNIT_ID ")
                .append(" AND IVI.LOC_ID = L.LOC_ID ")
                .append(" AND IVI.SMST_ID = SMST.SMST_ID ")
                .append(" AND IVI.HEAT_ID = H.HEAT_ID(+) ")
                .append(" AND IPR.RESV_QTY > 0 ")
                .append(" AND IPR.PROJ_ID = :projId ")
                .append(" AND IPR.FAH_ID = :fahId ")
            .toString();

        Query queryForCnt = entityManager.createNativeQuery(sqlForCnt);
        queryForCnt.setParameter("projId", spmProjId);
        queryForCnt.setParameter("fahId", fahId);
        BigDecimal size = (BigDecimal) queryForCnt.getSingleResult();

        String sql = new StringBuffer()
            .append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM ( ")
//            .append(" SELECT * ")
//            .append("   FROM WV_FAH_ISSUE_ITEMS H ")
//            .append("  WHERE H.PROJ_ID = :projId ")
//            .append("    AND H.FAH_ID = :fahId ")
            .append(" SELECT 'N' ESI_STATUS, ")
            .append(" IVI.IVI_ID, ")
            .append(" '' MIR_ID, ")
            .append(" LP_ID, ")
            .append(" IPR.IVPR_ID, ")
            .append(" IPR.RESV_QTY RESV_QTY, ")
            .append(" '' ISSUE_QTY, ")
            .append(" '' ISSUE_DATE, ")
            .append(" I.IDENT, ")
            .append(" IVI.WH_ID, ")
            .append(" IVI.LOC_ID, ")
            .append(" IVI.SMST_ID, ")
            .append(" IVI.UNIT_ID, ")
            .append(" IVI.TAG_NUMBER, ")
            .append(" IVI.HEAT_ID, ")
            .append(" '' PLATE_ID, ")
            .append(" '' IDENT_DEVIATION, ")
            .append(" '' SAS_ID, ")
            .append(" 'N' SITE_STAT_IND, ")
            .append(" '' MIR_NUMBER, ")
            .append(" I.IDENT_CODE, ")
            .append(" U.UNIT_CODE, ")
            .append(" IVI.PROJ_ID, ")
            .append(" H.HEAT_NUMBER, ")
            .append(" WH.WH_CODE, ")
            .append(" L.LOC_CODE, ")
            .append(" SMST.SMST_CODE, ")
            .append(" FAH_ID ")

            .append(" FROM M_INV_POS_RES IPR, ")
            .append(" M_INV_ITEMS IVI, ")
            .append(" M_IDENTS I, ")
            .append(" M_UNITS U, ")
            .append(" M_SITE_MATL_STATUS SMST, ")
            .append(" M_WAREHOUSES WH, ")
            .append(" M_LOCATIONS L, ")
            .append(" M_HEATS H ")
            .append(" WHERE IPR.IVI_ID = IVI.IVI_ID ")
            .append(" AND IVI.WH_ID = WH.WH_ID ")
            .append(" AND IVI.IDENT = I.IDENT ")
            .append(" AND IVI.UNIT_ID = U.UNIT_ID ")
            .append(" AND IVI.LOC_ID = L.LOC_ID ")
            .append(" AND IVI.SMST_ID = SMST.SMST_ID ")
            .append(" AND IVI.HEAT_ID = H.HEAT_ID(+) ")
//            .append("   FROM WV_FAH_ISSUE_ITEMS H ")
//            .append("  WHERE H.PROJ_ID = :projId ")

            .append(" AND IPR.RESV_QTY > 0 ")
            .append(" AND IPR.PROJ_ID = :projId ")
            .append(" AND IPR.FAH_ID = :fahId ")
            .append("  ORDER BY I.IDENT ASC, IPR.IVPR_ID ASC ")
            .append(") A WHERE ROWNUM <= :end) WHERE RN > :start ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projId", spmProjId);
        query.setParameter("fahId", fahId);
        query.setParameter("start", (pageno - 1) * pagesize);
        query.setParameter("end", (pageno) * pagesize);


        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        FadListResultsDTO rs = null;
        List<FadListResultsDTO> rsList = new ArrayList<FadListResultsDTO>();
        for (Map<String, Object> m : list) {
            rs = new FadListResultsDTO();
            rs.setEsiStatus(m.get("ESI_STATUS"));
            rs.setIviId(m.get("IVI_ID"));
            rs.setMirId(m.get("MIR_ID"));
            rs.setLpId(m.get("LP_ID"));
            rs.setIvprId(m.get("IVPR_ID"));
            rs.setResvQty(m.get("RESV_QTY"));
            rs.setIssueQty(m.get("ISSUE_QTY"));
            rs.setIssueDate(m.get("ISSUE_DATE"));
            rs.setIdent(m.get("IDENT"));
            rs.setWhId(m.get("WH_ID"));
            rs.setLocId(m.get("LOC_ID"));
            rs.setSmstId(m.get("SMST_ID"));
            rs.setUnitId(m.get("UNIT_ID"));
            rs.setTagNumber(m.get("TAG_NUMBER"));
            rs.setHeatId(m.get("HEAT_ID"));
            rs.setPlateId(m.get("PLATE_ID"));
            rs.setIdentDeviation(m.get("IDENT_DEVIATION"));
            rs.setSasId(m.get("SAS_ID"));
            rs.setSiteStatInd(m.get("SITE_STAT_IND"));
            rs.setMirNumber(m.get("MIR_NUMBER"));
            rs.setProjId(m.get("PROJ_ID"));
            rs.setHeatNumber(m.get("HEAT_NUMBER"));
            rs.setWhCode(m.get("WH_CODE"));
            rs.setLocCode(m.get("LOC_CODE"));
            rs.setSmstCode(m.get("SMST_CODE"));
            rs.setUnitCode(m.get("UNIT_CODE"));
            rs.setFahId(m.get("FAH_ID"));
            rsList.add(rs);
        }

        return new PageImpl<FadListResultsDTO>(rsList, fadListDTO.toPageable(), size.longValue());
    }

    @Override
    public List<FadListResultsDTO> getFadListNoPage(FadListDTO fadListDTO) {
        String fahId = fadListDTO.getFahId();
        String spmProjId = fadListDTO.getSpmProjId();

        EntityManager entityManager = getEntityManager();

        /*
            SELECT 'N' ESI_STATUS,
          IVI.IVI_ID,
          '' MIR_ID,
          LP_ID,
          IPR.IVPR_ID,
          IPR.RESV_QTY RESV_QTY,
          '' ISSUE_QTY,
          '' ISSUE_DATE,
          I.IDENT,
          IVI.WH_ID,
          IVI.LOC_ID,
          IVI.SMST_ID,
          IVI.UNIT_ID,
          IVI.TAG_NUMBER,
          IVI.HEAT_ID,
          '' PLATE_ID,
          '' IDENT_DEVIATION,
          '' SAS_ID,
          'N' SITE_STAT_IND,
          '' MIR_NUMBER,
          I.IDENT_CODE,
          U.UNIT_CODE,
          IVI.PROJ_ID,
          H.HEAT_NUMBER,
          WH.WH_CODE,
          L.LOC_CODE,
          SMST.SMST_CODE,
          FAH_ID
     FROM M_INV_POS_RES IPR,
          M_INV_ITEMS IVI,
          M_IDENTS I,
          M_UNITS U,
          M_SITE_MATL_STATUS SMST,
          M_WAREHOUSES WH,
          M_LOCATIONS L,
          M_HEATS H
    WHERE     IPR.IVI_ID = IVI.IVI_ID
          AND IVI.WH_ID = WH.WH_ID
          AND IVI.IDENT = I.IDENT
          AND IVI.UNIT_ID = U.UNIT_ID
          AND IVI.LOC_ID = L.LOC_ID
          AND IVI.SMST_ID = SMST.SMST_ID
          AND IVI.HEAT_ID = H.HEAT_ID



         */

        String sql = new StringBuffer()
//            .append(" SELECT * ")
//        .append(" SELECT * ")
        .append(" SELECT 'N' ESI_STATUS, ")
            .append(" IVI.IVI_ID, ")
            .append(" '' MIR_ID, ")
            .append(" LP_ID, ")
            .append(" IPR.IVPR_ID, ")
            .append(" IPR.RESV_QTY RESV_QTY, ")
            .append(" '' ISSUE_QTY, ")
            .append(" '' ISSUE_DATE, ")
            .append(" I.IDENT, ")
            .append(" IVI.WH_ID, ")
            .append(" IVI.LOC_ID, ")
            .append(" IVI.SMST_ID, ")
            .append(" IVI.UNIT_ID, ")
            .append(" IVI.TAG_NUMBER, ")
            .append(" IVI.HEAT_ID, ")
            .append(" '' PLATE_ID, ")
            .append(" '' IDENT_DEVIATION, ")
            .append(" '' SAS_ID, ")
            .append(" 'N' SITE_STAT_IND, ")
            .append(" '' MIR_NUMBER, ")
            .append(" I.IDENT_CODE, ")
            .append(" U.UNIT_CODE, ")
            .append(" IVI.PROJ_ID, ")
            .append(" H.HEAT_NUMBER, ")
            .append(" WH.WH_CODE, ")
            .append(" L.LOC_CODE, ")
            .append(" SMST.SMST_CODE, ")
            .append(" FAH_ID ")

        .append(" FROM M_INV_POS_RES IPR, ")
            .append(" M_INV_ITEMS IVI, ")
            .append(" M_IDENTS I, ")
            .append(" M_UNITS U, ")
            .append(" M_SITE_MATL_STATUS SMST, ")
            .append(" M_WAREHOUSES WH, ")
            .append(" M_LOCATIONS L, ")
            .append(" M_HEATS H ")
        .append(" WHERE IPR.IVI_ID = IVI.IVI_ID ")
            .append(" AND IVI.WH_ID = WH.WH_ID ")
            .append(" AND IVI.IDENT = I.IDENT ")
            .append(" AND IVI.UNIT_ID = U.UNIT_ID ")
            .append(" AND IVI.LOC_ID = L.LOC_ID ")
            .append(" AND IVI.SMST_ID = SMST.SMST_ID ")
            .append(" AND IVI.HEAT_ID = H.HEAT_ID(+) ")
            .append(" AND IPR.RESV_QTY > 0 ")

//            .append("   FROM WV_FAH_ISSUE_ITEMS H ")
//            .append("  WHERE H.PROJ_ID = :projId ")
            .append(" AND IPR.PROJ_ID = :projId ")
            .append(" AND IPR.FAH_ID = :fahId ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projId", spmProjId);
        query.setParameter("fahId", fahId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        FadListResultsDTO rs = null;
        List<FadListResultsDTO> rsList = new ArrayList<FadListResultsDTO>();
        for (Map<String, Object> m : list) {
            rs = new FadListResultsDTO();
            rs.setEsiStatus(m.get("ESI_STATUS"));
            rs.setIviId(m.get("IVI_ID"));
            rs.setMirId(m.get("MIR_ID"));
            rs.setLpId(m.get("LP_ID"));
            rs.setIvprId(m.get("IVPR_ID"));
            rs.setResvQty(m.get("RESV_QTY"));
            rs.setIssueQty(m.get("ISSUE_QTY"));
            rs.setIssueDate(m.get("ISSUE_DATE"));
            rs.setIdent(m.get("IDENT"));
            rs.setWhId(m.get("WH_ID"));
            rs.setLocId(m.get("LOC_ID"));
            rs.setSmstId(m.get("SMST_ID"));
            rs.setUnitId(m.get("UNIT_ID"));
            rs.setTagNumber(m.get("TAG_NUMBER"));
            rs.setHeatId(m.get("HEAT_ID"));
            rs.setPlateId(m.get("PLATE_ID"));
            rs.setIdentDeviation(m.get("IDENT_DEVIATION"));
            rs.setSasId(m.get("SAS_ID"));
            rs.setSiteStatInd(m.get("SITE_STAT_IND"));
            rs.setMirNumber(m.get("MIR_NUMBER"));
            rs.setProjId(m.get("PROJ_ID"));
            rs.setHeatNumber(m.get("HEAT_NUMBER"));
            rs.setWhCode(m.get("WH_CODE"));
            rs.setLocCode(m.get("LOC_CODE"));
            rs.setSmstCode(m.get("SMST_CODE"));
            rs.setUnitCode(m.get("UNIT_CODE"));
            rs.setFahId(m.get("FAH_ID"));
            rsList.add(rs);
        }

        return rsList;
    }
}
