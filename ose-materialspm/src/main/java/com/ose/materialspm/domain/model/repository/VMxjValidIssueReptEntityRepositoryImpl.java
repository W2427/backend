package com.ose.materialspm.domain.model.repository;

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

import com.ose.materialspm.dto.IssueReceiptDTO;
import com.ose.materialspm.dto.IssueReceiptListResultsDTO;
import com.ose.repository.BaseRepository;

/**
 * 合同查询。
 */
public class VMxjValidIssueReptEntityRepositoryImpl extends BaseRepository implements VMxjValidIssueReptEntityRepositoryCustom {

    @Override
    public Page<IssueReceiptListResultsDTO> getIssueReceiptList(IssueReceiptDTO issueReceiptDTO) {
        String fahCode = issueReceiptDTO.getFahCode();
        String spmProjId = issueReceiptDTO.getSpmProjId();
        int pageno = issueReceiptDTO.getPage().getNo();
        int pagesize = issueReceiptDTO.getPage().getSize();

        EntityManager entityManager = getEntityManager();

        String sqlForCnt = new StringBuffer()
            .append(" SELECT COUNT(MIR_ID) ")
            .append("   FROM V_MXJ_VALID_ISSUE_REPT ")
            .append("  WHERE PROJ_ID = :projId ")
            .append("    AND FAH_CODE = :fahCode ")
            .toString();

        Query queryForCnt = entityManager.createNativeQuery(sqlForCnt);
        queryForCnt.setParameter("projId", spmProjId);
        queryForCnt.setParameter("fahCode", fahCode);
        BigDecimal size = (BigDecimal) queryForCnt.getSingleResult();

        String sql = new StringBuffer()
            .append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM ( ")
            .append(" SELECT PROJ_ID, MIR_ID, MIR_DESC, DP_CODE, MIR_NUMBER, REVISION_ID, MIR_TYPE, ")
            .append("        ISSUE_TYPE, WH_CODE, LOC_CODE, FAH_ID, FAH_CODE, RUN_NUMBER ")
            .append("   FROM V_MXJ_VALID_ISSUE_REPT ")
            .append("  WHERE PROJ_ID = :projId ")
            .append("    AND FAH_CODE = :fahCode ")
            .append(") A WHERE ROWNUM <= :end) WHERE RN > :start ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projId", spmProjId);
        query.setParameter("fahCode", fahCode);
        query.setParameter("start", (pageno - 1) * pagesize);
        query.setParameter("end", (pageno) * pagesize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        IssueReceiptListResultsDTO rs = null;
        List<IssueReceiptListResultsDTO> rsList = new ArrayList<IssueReceiptListResultsDTO>();
        for (Map<String, Object> m : list) {
            rs = new IssueReceiptListResultsDTO();
            rs.setMirId(m.get("MIR_ID"));
            rs.setMirNumber(m.get("MIR_NUMBER"));
            rs.setRevisionId(m.get("REVISION_ID"));
            rs.setMirDesc(m.get("MIR_DESC"));
            rs.setDpCode(m.get("DP_CODE"));
            rs.setFahId(m.get("FAH_ID"));
            rs.setFahCode(m.get("FAH_CODE"));
            rs.setRunNumber(m.get("RUN_NUMBER"));
            rs.setIssueType(m.get("ISSUE_TYPE"));
            rs.setMirType(m.get("MIR_TYPE"));
            rs.setLocCode(m.get("LOC_CODE"));
            rs.setWhCode(m.get("WH_CODE"));
            rs.setProjId(m.get("PROJ_ID"));

            rsList.add(rs);
        }

        return new PageImpl<IssueReceiptListResultsDTO>(rsList, issueReceiptDTO.toPageable(), size.longValue());
    }

}
