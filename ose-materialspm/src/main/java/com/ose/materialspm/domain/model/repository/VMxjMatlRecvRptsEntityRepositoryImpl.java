package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.dto.ReceiveReceiptDTO;
import com.ose.materialspm.dto.ReceiveReceiptListResultsDTO;
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
public class VMxjMatlRecvRptsEntityRepositoryImpl extends BaseRepository implements VMxjMatlRecvRptsEntityRepositoryCustom {

    @Override
    public Page<ReceiveReceiptListResultsDTO> getReceiveReceiptList(ReceiveReceiptDTO receiveReceiptDTO) {
        String poNumber = receiveReceiptDTO.getPoNumber();
        String relnNumber = receiveReceiptDTO.getRelnNumber();
        String spmProjId = receiveReceiptDTO.getSpmProjId();
        int pageno = receiveReceiptDTO.getPage().getNo();
        int pagesize = receiveReceiptDTO.getPage().getSize();

        EntityManager entityManager = getEntityManager();

        String sqlForCnt = new StringBuffer()
            .append(" SELECT COUNT(MRR_ID) ")
            .append("   FROM V_MXJ_MATL_RECV_RPTS ")
            .append("  WHERE PROJ_ID = :projId ")
            .append("    AND (RELN_NUMBER = :relnNumber OR PO_NUMBER = :poNumber) ")
            .toString();

        Query queryForCnt = entityManager.createNativeQuery(sqlForCnt);
        queryForCnt.setParameter("projId", spmProjId);
        queryForCnt.setParameter("relnNumber", relnNumber);
        queryForCnt.setParameter("poNumber", poNumber);
        BigDecimal size = (BigDecimal) queryForCnt.getSingleResult();

        String sql = new StringBuffer()
            .append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM ( ")
            .append(" SELECT PROJ_ID, MRR_ID, MRR_NUMBER, REVISION_ID, RECV_TYPE, SHORT_DESC, DP_CODE, ")
            .append("        WH_CODE, LOC_CODE, POH_ID, PO_NUMBER, PO_SUPP, RELN_NUMBER ")
            .append("   FROM V_MXJ_MATL_RECV_RPTS ")
            .append("  WHERE PROJ_ID = :projId ")
            .append("    AND (RELN_NUMBER = :relnNumber OR PO_NUMBER = :poNumber) ")
            .append(") A WHERE ROWNUM <= :end) WHERE RN > :start ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projId", spmProjId);
        query.setParameter("relnNumber", relnNumber);
        query.setParameter("poNumber", poNumber);
        query.setParameter("start", (pageno - 1) * pagesize);
        query.setParameter("end", (pageno) * pagesize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        ReceiveReceiptListResultsDTO rs = null;
        List<ReceiveReceiptListResultsDTO> rsList = new ArrayList<ReceiveReceiptListResultsDTO>();
        for (Map<String, Object> m : list) {
            rs = new ReceiveReceiptListResultsDTO();
            rs.setMrrId(m.get("MRR_ID"));
            rs.setMrrNumber(m.get("MRR_NUMBER"));
            rs.setRevisionId(m.get("REVISION_ID"));
            rs.setPohId(m.get("POH_ID"));
            rs.setPoNumber(m.get("PO_NUMBER"));
            rs.setPoSupp(m.get("PO_SUPP"));
            rs.setRelnNumber(m.get("RELN_NUMBER"));
            rs.setWhCode(m.get("WH_CODE"));
            rs.setLocCode(m.get("LOC_CODE"));
            rs.setRecvType(m.get("RECV_TYPE"));
            rs.setProjId(m.get("PROJ_ID"));
            rs.setDpCode(m.get("DP_CODE"));
            rs.setShortDesc(m.get("SHORT_DESC"));

            rsList.add(rs);
        }

        return new PageImpl<ReceiveReceiptListResultsDTO>(rsList, receiveReceiptDTO.toPageable(), size.longValue());
    }

}
