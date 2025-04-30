package com.ose.tasks.domain.model.repository.drawing;


import com.ose.tasks.dto.drawing.DrawingWorkHourDTO;
import com.ose.tasks.dto.timesheet.SelectDataDTO;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.vo.bpm.BpmsProcessNameEnum;
import com.ose.vo.BpmTaskDefKey;
import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.drawing.DrawingCriteriaDTO;
import com.ose.tasks.dto.drawing.ProofreadDrawingListCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingUploadZipFileHistory;
import com.ose.tasks.entity.drawing.DrawingUploadZipFileHistoryDetail;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.*;

/**
 * 主图纸
 */
public class DrawingRepositoryImpl extends BaseRepository implements DrawingRepositoryCustom {

    private static final String SD_DWG_BOM_FORECAST = "SD_DWG_BOM_FORECAST";
    private static final List<String> ENTITY_CATEGORY = new ArrayList<>(
        Arrays.asList(SD_DWG_BOM_FORECAST));

    @Override
    public List<DrawingCriteriaDTO> getParamList(Long orgId, Long projectId) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("d.latest_approved_rev as latestApprovedRev ");
        sql.append("FROM drawing AS d ");
//        sql.append("left join bpm_entity_sub_type AS c ON d.`drawing_category_id` = c.`id` ");
        sql.append("WHERE  d.project_id = :projectId ");
        sql.append("AND d.status = :status ");
        sql.append("GROUP BY latest_approved_rev");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("projectId", projectId);
        query.setParameter("status", EntityStatus.ACTIVE.name());
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        List<DrawingCriteriaDTO> dwgList = new ArrayList<DrawingCriteriaDTO>();


        for (Map<String, Object> m : queryResultList) {
            DrawingCriteriaDTO dwg = new DrawingCriteriaDTO();
            if (m.get("latestApprovedRev") == null) {
                dwg.setLatestApprovedRev("空");
            } else {
                dwg.setLatestApprovedRev(m.get("latestApprovedRev").toString());
            }
            dwgList.add(dwg);
        }
        return dwgList;
    }

    @Override
    public Page<DrawingWorkHourDTO> getList(Long orgId, Long projectId, PageDTO page,
                                            DrawingCriteriaDTO criteriaDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        StringBuilder whereSql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("d.id as id, ");
        sql.append("d.created_at as createdAt, ");
        sql.append("d.last_modified_at as lastModifiedAt, ");
        sql.append("d.status as status, ");
        sql.append("d.actural_drawing_issue_date as acturalDrawingIssueDate, ");
        sql.append("d.audit_no as auditNo, ");
        sql.append("d.batch_task_id as batchTaskId, ");
        sql.append("d.change_notice_no as changeNoticeNo, ");
        sql.append("d.delivery_date as deliveryDate, ");
        sql.append("d.design_change_review_form as designChangeReviewForm, ");
        sql.append("d.drawing_title as drawingTitle, ");
        sql.append("d.dwg_no as dwgNo, ");
        sql.append("d.import_file_id as importFileId, ");
        sql.append("d.issue_card_no as issueCardNo, ");
        sql.append("d.latest_rev as latestRev, ");
        sql.append("d.latest_approved_rev as latestApprovedRev, ");
        sql.append("d.org_id as orgId, ");
        sql.append("d.paper_amount as paperAmount, ");
        sql.append("d.paper_use as paperUse, ");
        sql.append("d.printing as printing, ");
        sql.append("d.production_receiving_date as productionReceivingDate, ");
        sql.append("d.project_id as projectId, ");
        sql.append("d.quantity as quantity, ");
        sql.append("d.return_record as returnRecord, ");
        sql.append("d.display_name as displayName, ");
        sql.append("d.sequence_no as sequenceNo, ");
        sql.append("d.file_id as fileId, ");
        sql.append("d.file_name as fileName, ");
        sql.append("d.file_path as filePath, ");
        sql.append("d.qr_code as qrCode, ");
        sql.append("d.locked as locked, ");
        sql.append("d.cover_id as coverId, ");
        sql.append("d.cover_path as coverPath, ");
        sql.append("d.engineering_finish_date as engineeringFinishDate, ");
        sql.append("d.engineering_start_date as engineeringStartDate, ");
        sql.append("d.sdrl_code as sdrlCode, ");
        sql.append("d.package_no as packageNo, ");
        sql.append("d.package_name as packageName, ");
        sql.append("d.originator_name as originatorName, ");
        sql.append("d.project_No as projectNo, ");
        sql.append("d.org_code as orgCode, ");
        sql.append("d.system_code as systemCode, ");
        sql.append("d.disc_code as discCode, ");
        sql.append("d.doc_type as docType, ");
        sql.append("d.short_code as shortCode, ");
        sql.append("d.sheet_no as sheetNo, ");
        sql.append("d.document_title as documentTitle, ");
        sql.append("d.document_chain as documentChain, ");
        sql.append("d.chain_code as chainCode, ");
        sql.append("d.progress_stage as progressStage, ");
        sql.append("d.rev_no as revNo, ");
        sql.append("d.upload_date as uploadDate, ");
        sql.append("d.outgoing_transmittal as outgoingTransmittal, ");
        sql.append("d.incoming_transmittal as incomingTransmittal, ");
        sql.append("d.reply_date as replyDate, ");
        sql.append("d.reply_status as replyStatus, ");
        sql.append("d.client_doc_rev as clientDocRev, ");
        sql.append("d.client_doc_no as clientDocNo, ");
        sql.append("d.owner_doc_no as ownerDocNo, ");
        sql.append("d.validity_status as validityStatus, ");
        sql.append("d.surveillance_type as surveillanceType, ");
        sql.append("d.discipline as discipline, ");
        sql.append("d.drawing_type as drawingType, ");
        sql.append("d.drawing_status as drawingStatus, ");
//        sql.append("draw.name  AS drawUsername, ");
//        sql.append("checked.name  AS checkUsername, ");
//        sql.append("approve.name AS approvedUsername ");
        sql.append("MAX(CASE WHEN d.privilege = 'DESIGN_ENGINEER_EXECUTE' THEN d.user_name END) AS drawUsername, ");
        sql.append("MAX(CASE WHEN d.privilege = 'DRAWING_REVIEW_EXECUTE' THEN d.user_name END) AS checkUsername, ");
        sql.append("MAX(CASE WHEN d.privilege = 'DRAWING_APPROVE_EXECUTE' THEN d.user_name END) AS approvedUsername, ");
        sql.append("MAX(CASE WHEN d.privilege = 'DESIGN_ENGINEER_EXECUTE' THEN d.user_id END) AS drawUserId, ");
        sql.append("MAX(CASE WHEN d.privilege = 'DRAWING_REVIEW_EXECUTE' THEN d.user_id END) AS checkUserId, ");
        sql.append("MAX(CASE WHEN d.privilege = 'DRAWING_APPROVE_EXECUTE' THEN d.user_id END) AS approvedUserId ");


        sql.append(" FROM ( " +
            " SELECT " +
            "  od.*, " +
            "  ded.privilege AS privilege, " +
            "  u.name AS user_name, " +
            "  u.id AS user_id " +
            " FROM " +
            "  drawing AS od " +
            " LEFT JOIN drawing_entry_delegate ded  " +
            "        ON " +
            "  od.id = ded.drawing_id " +
            "  AND ded.privilege IN ('DESIGN_ENGINEER_EXECUTE', 'DRAWING_REVIEW_EXECUTE', 'DRAWING_APPROVE_EXECUTE') " +
            " LEFT JOIN saint_whale_auth.users u  " +
            "        ON " +
            "  ded.user_id = u.id WHERE 1=1 ");
        if (projectId != null) {
            sql.append(" AND od.project_id = :projectId ");
        }
        if (criteriaDTO.getAssigneeId() != null) {
            sql.append(" AND u.id = :userId ");
        }
        sql.append(") AS d ");
        sql.append("WHERE 1 = 1 ");

        if (criteriaDTO.getStatus() != null) {
            if (!criteriaDTO.getStatus().equalsIgnoreCase("ALL")) {
                whereSql.append("AND d.status = :status ");
            }
        } else {
            whereSql.append("AND d.status in ('ACTIVE','DELETED') ");
        }

        if (criteriaDTO.getActInst() != null) {
            whereSql.append("AND d.locked = :locked ");
        }

        if (criteriaDTO.getLocked() != null) {
            whereSql.append("AND d.drawing_status = :locked ");
        }
        //2023-07-19新增搜索条件
        if (criteriaDTO.getSystemCode() != null) {
            whereSql.append("AND d.system_code like :systemCode ");
        }

        if (criteriaDTO.getDiscCode() != null) {
            whereSql.append("AND (d.disc_code like :discCode OR d.org_code like :discCode) ");
        }

        if (criteriaDTO.getDocType() != null) {
            whereSql.append("AND d.doc_type like :docType ");
        }

        if (criteriaDTO.getDiscipline() != null) {
            whereSql.append("AND d.discipline like :discipline ");
        }

        if (criteriaDTO.getDrawingType() != null) {
            whereSql.append("AND d.drawing_type like :drawingType ");
        }

        if (criteriaDTO.getSdrlCode() != null) {
            whereSql.append("AND d.sdrl_code like :sdrlCode ");
        }

        if (criteriaDTO.getPackageNo() != null) {
            whereSql.append("AND (d.package_no like :packetNumber OR d.package_name like :packetNumber) ");
        }

        if (criteriaDTO.getOriginatorName() != null) {
            whereSql.append("AND d.originator_name like :originatorName ");
        }

        if (criteriaDTO.getEntityType() != null) {
            whereSql.append("AND d.entity_type = :entityType ");
        } else {
            whereSql.append("AND d.entity_type != 'PMT' ");
        }

        if (criteriaDTO.getApprovedUserId() != null) {
            whereSql.append("AND d.user_id = :approvedUserId AND d.privilege = 'DRAWING_APPROVE_EXECUTE' ");
        }
        if (criteriaDTO.getCheckUserId() != null) {
            whereSql.append("AND d.user_id = :checkUserId AND d.privilege = 'DRAWING_REVIEW_EXECUTE' ");
        }
        if (criteriaDTO.getDrawUserId() != null) {
            whereSql.append("AND d.user_id = :drawUserId AND d.privilege = 'DESIGN_ENGINEER_EXECUTE' ");
        }

//        if (criteriaDTO.getStartTime() != null) {
//            whereSql.append("AND d. >= :startTime ");
//        }

        //--------

        if ("null".equals(criteriaDTO.getLatestApprovedRev())) {
            whereSql.append("AND d.latest_approved_rev is null ");
        } else if (criteriaDTO.getLatestApprovedRev() != null) {
            whereSql.append("AND d.latest_approved_rev = :latestApprovedRev ");
        }

        if (criteriaDTO.getKeyword() != null) {
            whereSql.append("AND (( d.document_title like :documentTitle) OR ( d.display_name like :documentTitle) OR ( d.client_doc_no like :documentTitle) OR ( d.owner_doc_no like :documentTitle)) ");
        }

        if (criteriaDTO.getBomFlag() != null
            && criteriaDTO.getBomFlag()) {
            sql.append(whereSql);
        } else if (criteriaDTO.getBomFlag() != null
            && !criteriaDTO.getBomFlag()) {
            StringBuilder whereSql2 = new StringBuilder(whereSql);
            sql.append("AND ( 1 = 1 ");
            sql.append(whereSql);
            sql.append(") OR ( 1 = 1 ");
            sql.append(whereSql2);
            sql.append(")");
        } else {
            sql.append(whereSql);
        }

        sql.append("GROUP BY d.id ");

        if (!page.getFetchAll()) {
            sql.append("LIMIT :start , :offset ");
        }


        Query query = entityManager.createNativeQuery(sql.toString());


        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";

        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (criteriaDTO.getAssigneeId() != null) {
            query.setParameter("userId", criteriaDTO.getAssigneeId());
            countQuery.setParameter("userId", criteriaDTO.getAssigneeId());
        }
        if (criteriaDTO.getApprovedUserId() != null) {
            query.setParameter("approvedUserId", criteriaDTO.getApprovedUserId());
            countQuery.setParameter("approvedUserId", criteriaDTO.getApprovedUserId());
        }
        if (criteriaDTO.getCheckUserId() != null) {
            query.setParameter("checkUserId", criteriaDTO.getCheckUserId());
            countQuery.setParameter("checkUserId", criteriaDTO.getCheckUserId());
        }
        if (criteriaDTO.getDrawUserId() != null) {
            query.setParameter("drawUserId", criteriaDTO.getDrawUserId());
            countQuery.setParameter("drawUserId", criteriaDTO.getDrawUserId());
        }

        if (criteriaDTO.getLatestApprovedRev() != null && !"null".equals(criteriaDTO.getLatestApprovedRev())) {
            query.setParameter("latestApprovedRev", criteriaDTO.getLatestApprovedRev());
            countQuery.setParameter("latestApprovedRev", criteriaDTO.getLatestApprovedRev());
        }

        if (criteriaDTO.getLocked() != null) {
            query.setParameter("locked", criteriaDTO.getLocked());
            countQuery.setParameter("locked", criteriaDTO.getLocked());
        }

        if (criteriaDTO.getEntityType() != null) {
            query.setParameter("entityType", criteriaDTO.getEntityType());
            countQuery.setParameter("entityType", criteriaDTO.getEntityType());
        }
        //2023-7-19新增搜索条件
        if (criteriaDTO.getSystemCode() != null) {
            query.setParameter("systemCode", "%" + criteriaDTO.getSystemCode() + "%");
            countQuery.setParameter("systemCode", "%" + criteriaDTO.getSystemCode() + "%");
        }

        if (criteriaDTO.getDiscCode() != null) {
            query.setParameter("discCode", "%" + criteriaDTO.getDiscCode() + "%");
            countQuery.setParameter("discCode", "%" + criteriaDTO.getDiscCode() + "%");
        }

        if (criteriaDTO.getDocType() != null) {
            query.setParameter("docType", "%" + criteriaDTO.getDocType() + "%");
            countQuery.setParameter("docType", "%" + criteriaDTO.getDocType() + "%");
        }

        if (criteriaDTO.getDiscipline() != null) {
            query.setParameter("discipline", "%" + criteriaDTO.getDiscipline() + "%");
            countQuery.setParameter("discipline", "%" + criteriaDTO.getDiscipline() + "%");
        }

        if (criteriaDTO.getDrawingType() != null) {
            query.setParameter("drawingType", "%" + criteriaDTO.getDrawingType() + "%");
            countQuery.setParameter("drawingType", "%" + criteriaDTO.getDrawingType() + "%");
        }

        if (criteriaDTO.getSdrlCode() != null) {
            query.setParameter("sdrlCode", "%" + criteriaDTO.getSdrlCode() + "%");
            countQuery.setParameter("sdrlCode", "%" + criteriaDTO.getSdrlCode() + "%");
        }

        if (criteriaDTO.getPackageNo() != null) {
            query.setParameter("packetNumber", "%" + criteriaDTO.getPackageNo() + "%");
            countQuery.setParameter("packetNumber", "%" + criteriaDTO.getPackageNo() + "%");
        }

        if (criteriaDTO.getOriginatorName() != null) {
            query.setParameter("originatorName", "%" + criteriaDTO.getOriginatorName() + "%");
            countQuery.setParameter("originatorName", "%" + criteriaDTO.getOriginatorName() + "%");
        }

        //---------------

        if (criteriaDTO.getKeyword() != null) {
            query.setParameter("documentTitle", "%" + criteriaDTO.getKeyword() + "%");
            countQuery.setParameter("documentTitle", "%" + criteriaDTO.getKeyword() + "%");
        }

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        if (criteriaDTO.getStatus() != null && !criteriaDTO.getStatus().equalsIgnoreCase("ALL")) {
            query.setParameter("status", criteriaDTO.getStatus());
            countQuery.setParameter("status", criteriaDTO.getStatus());
        }

        if (!page.getFetchAll()) {

            int pageNo = page.getPage().getNo();
            int pageSize = page.getPage().getSize();
            query.setParameter("start", (pageNo - 1) * pageSize);
            query.setParameter("offset", pageSize);
        }
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();

        Long count = (Long) countQuery.getSingleResult();


        List<DrawingWorkHourDTO> dwgList = new ArrayList<DrawingWorkHourDTO>();


        for (Map<String, Object> wpsMap : queryResultList) {
            DrawingWorkHourDTO dwg = new DrawingWorkHourDTO();
            dwg.setStatus(EntityStatus.valueOf(wpsMap.get("status") == null ? "" : wpsMap.get("status").toString()));
            BeanUtils.copyProperties(wpsMap, dwg, "status");
            dwgList.add(dwg);
        }


        if (page.getFetchAll()) {
            return new PageImpl<>(
                dwgList
            );
        }

        return new PageImpl<>(dwgList, page.toPageable(), count.longValue());
    }

    @Override
    public Page<DrawingUploadZipFileHistory> zipFileHistory(Long orgId, Long projectId, Long drawingId, PageDTO page) {
        SQLQueryBuilder<DrawingUploadZipFileHistory> builder = getSQLQueryBuilder(DrawingUploadZipFileHistory.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("drawingId", drawingId)
            .is("status", EntityStatus.ACTIVE);

        return builder.paginate(page.toPageable())
            .exec()
            .page();
    }

    @Override
    public Page<DrawingUploadZipFileHistoryDetail> zipFileHistoryDetail(Long orgId, Long projectId,
                                                                        Long drawingId, Long id, PageDTO page) {
        SQLQueryBuilder<DrawingUploadZipFileHistoryDetail> builder = getSQLQueryBuilder(DrawingUploadZipFileHistoryDetail.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("drawingUploadZipFileHistoryId", id)
            .is("status", EntityStatus.ACTIVE);

        return builder.paginate(page.toPageable())
            .exec()
            .page();
    }

    @Override
    public Page<Drawing> findByOrgIdAndProjectIdAndDrawingCategoryIsNotNullAndIdIn(Long orgId, Long projectId,
                                                                                   List<Long> entityIds, Pageable pageable, String keyword) {
        SQLQueryBuilder<Drawing> builder = getSQLQueryBuilder(Drawing.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .like("dwgNo", keyword)
            .isNotNull("drawingCategory")
            .notIn("id", entityIds)
            .is("status", EntityStatus.ACTIVE);

        return builder.paginate(pageable)
            .exec()
            .page();

    }

    @Override
    public List<Map<String, Object>> getXlsList(Long orgId, Long projectId) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("select " +
            "c.id as id, " +
            "        e.created_at as createdAt, " +
            "        e.last_modified_at as lastModifiedAt, " +
            "        e.status as status, " +
            "        e.actural_drawing_issue_date as acturalDrawingIssueDate, " +
            "        e.audit_no as auditNo, " +
            "        c.batch_task_id as batchTaskId, " +
            "        e.change_notice_no as changeNoticeNo, " +
            "        e.delivery_date as deliveryDate, " +
            "        e.design_change_review_form as designChangeReviewForm, " +
            "        c.drawing_title as drawingTitle, " +
            "        c.dwg_no as dwgNo, " +
            "        c.import_file_id as importFileId, " +
            "        e.issue_card_no as issueCardNo, " +
            "        c.latest_rev as latestRev, " +
            "        c.org_id as org_id, " +
            "        e.paper_amount as paperAmount, " +
            "        ifnull(e.paper_use,0) as paperUse, " +
            "        e.printing as printing, " +
            "        e.production_receiving_date as productionReceivingDate, " +
            "        c.project_id as projectId, " +
            "        e.quantity as quantity, " +
            "        e.return_record as returnRecord, " +
            "        e.rev as rev, " +
            "        c.sequence_no as sequenceNo, " +
            "        e.file_id as file_id, " +
//            "        c.file_last_version as fileLastVersion, " +
            "        e.file_name as fileName, " +
            "        c.file_page_count as filePageCount, " +
            "        e.file_path as filePath, " +
            "        e.qr_code as qrCode, " +
            "        c.drawing_category_id as drawingCategoryId, " +
            "        c.operator as operator, " +
            "        c.locked as locked, " +
            "        c.cover_id as coverId, " +
            "        c.cover_path as coverPath, " +
            "        c.cover_name as coverName, " +
            "        c.drawer as drawer, " +
            "        c.drawer_id as drawerId, " +
            "        c.engineering_finish_date as engineeringFinishDate, " +
            "        c.engineering_start_date as engineeringStartDate " +
            "from drawing c " +
            "left join drawing_detail e on e.drawing_id = c.id " +
            "where c.project_id = :projectId and c.org_id = :orgId and c.status = 'ACTIVE' " +
            " order by c.dwg_no");


        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("projectId", projectId);
        query.setParameter("orgId", orgId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();


        return queryResultList;
    }

    @Override
    public Page<DrawingDetail> getProofreadDrawingList(
        Long orgId,
        Long projectId,
        Long userId,
        PageDTO page,
        ProofreadDrawingListCriteriaDTO criteriaDTO
    ) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        StringBuilder whereSql = new StringBuilder();
        sql.append("SELECT DISTINCT ");
        sql.append("dd.id as id, ");
        sql.append("dd.created_at as createdAt, ");
        sql.append("dd.last_modified_at as lastModifiedAt, ");
        sql.append("dd.status as status, ");
        sql.append("d.actural_drawing_issue_date as acturalDrawingIssueDate, ");
        sql.append("d.audit_no as auditNo, ");
        sql.append("d.batch_task_id as batchTaskId, ");
        sql.append("d.change_notice_no as changeNoticeNo, ");
        sql.append("d.delivery_date as deliveryDate, ");
        sql.append("d.design_change_review_form as designChangeReviewForm, ");
        sql.append("d.drawing_title as drawingTitle, ");
        sql.append("d.dwg_no as dwgNo, ");
        sql.append("dd.rev_no as revNo, ");
        sql.append("d.import_file_id as importFileId, ");
        sql.append("d.issue_card_no as issueCardNo, ");
        sql.append("d.latest_rev as latestRev, ");
        sql.append("d.org_id as orgId, ");
        sql.append("d.paper_amount as paperAmount, ");
        sql.append("d.paper_use as paperUse, ");
        sql.append("d.printing as printing, ");
        sql.append("d.production_receiving_date as productionReceivingDate, ");
        sql.append("d.project_id as projectId, ");
        sql.append("d.quantity as quantity, ");
        sql.append("d.return_record as returnRecord, ");
        sql.append("r.id as taskId, ");
        sql.append("r.assignee_ as assignee, ");

        sql.append("d.sequence_no as sequenceNo, ");
        sql.append("d.file_id as fileId, ");
//        sql.append("d.file_last_version as fileLastVersion, ");
        sql.append("d.file_name as fileName, ");
//        sql.append("d.file_page_count as filePageCount, ");
        sql.append("d.file_path as filePath, ");
        sql.append("d.qr_code as qrCode, ");
//        sql.append("d.drawing_category_id as drawingCategoryId, ");
//        sql.append("d.operator as operator, ");
        sql.append("d.locked as locked, ");
        sql.append("d.cover_id as coverId, ");
        sql.append("d.cover_path as coverPath, ");
        sql.append("d.engineering_finish_date as engineeringFinishDate, ");
        sql.append("d.engineering_start_date as engineeringStartDate, ");
//        sql.append("d.estimated_man_hours as estimatedManHours, ");
        sql.append("c.name_en as entitySubType ");

//        sql.append("d.approved_username as approvedUsername, ");

//        sql.append("d.check_username as checkUsername, ");

//        sql.append("d.draw_username as drawUsername, ");
//        sql.append("d.drawing_delivery_file_name as drawingDeliveryFileName, ");
//        sql.append("d.drawing_delivery_file_path as drawingDeliveryFilePath, ");
//        sql.append("d.drawing_delivery_file_id as drawingDeliveryFileId, ");


//        sql.append("d.drawer as drawer, ");
//        sql.append("d.drawer_id as drawerId ");


        sql.append(" FROM ");
        sql.append("  drawing d ");
        sql.append("  INNER JOIN drawing_detail dd  ");
        sql.append("  ON d.`id` = dd.`drawing_id` AND dd.status = 'PENDING' ");
        sql.append("  INNER JOIN bpm_activity_instance_base a  ");
        sql.append("  ON a.`entity_id` = d.`id` ");
        sql.append("  INNER JOIN bpm_activity_instance_state  `as` ON a.project_id = as.project_id AND a.id = as.bai_id  ");
        sql.append("  INNER JOIN bpm_ru_task r ");
        sql.append("  ON a.`id` = r.`act_inst_id` ");
        sql.append("  INNER JOIN bpm_hi_taskinst h ");
        sql.append("  ON a.`id` = h.`act_inst_id` ");
        sql.append("  INNER JOIN bpm_entity_sub_type c ");
        sql.append("  ON a.`entity_sub_type_id` = c.id ");


        sql.append(" WHERE 1 = 1 ");

        whereSql.append(" AND d.project_id = :projectId ");
        whereSql.append(" AND d.status = :status ");
        whereSql.append(" AND `as`.`suspension_state` = 'ACTIVE' ");
        whereSql.append("  AND `a`.`process_name` IN ( '" + BpmsProcessNameEnum.ENGINEERING.getType());
        whereSql.append("' ,'" + BpmsProcessNameEnum.DRAWING_INTEGRAL_UPDATE.getType());
        whereSql.append("' ,'" + BpmsProcessNameEnum.DRAWING_PARTIAL_UPDATE.getType());
        whereSql.append("' ,'" + "IFD");
        whereSql.append("' ,'" + "IFC");
        whereSql.append("' ,'" + "IFD_U");
        whereSql.append("' ,'" + "IFI");
        whereSql.append("' ,'" + "IAB");
        whereSql.append("' ,'" + "IFC_U");
        whereSql.append("' ,'" + "IFI");
        whereSql.append("' ,'" + BpmsProcessNameEnum.DRAWING_REDMARK.getType());
        whereSql.append("'  ) ");
        if (criteriaDTO.getType().name().equals("MODIFY") || criteriaDTO.getType().name().equals("CHECK") || criteriaDTO.getType().name().equals("REVIEW")) {
            whereSql.append("  AND h.`task_def_key_` IN ( '" + BpmTaskDefKey.USERTASK_REDMARK_CHECK.getType() + "' , '" + BpmTaskDefKey.USERTASK_DRAWING_CHECK.getType() + "' , '" + BpmTaskDefKey.USERTASK_REDMARK_MODIFY.getType() + "' , '" + BpmTaskDefKey.USERTASK_DRAWING_REVIEW.getType() + " ' ) ");
        }
        if (criteriaDTO.isSubDrawingFlg()) {
            whereSql.append("  AND c.`sub_drawing_flg` = TRUE ");
        }

        whereSql.append(" AND (r.`assignee_` =:assignee OR h.`assignee_` = :assignee) ");


        if (criteriaDTO.getKeyword() != null) {
            whereSql.append(" AND (d.dwg_no like :dwgNo or d.drawing_title like :drawingTitle) ");
        }

        sql.append(whereSql);


        sql.append(" ORDER BY dd.created_at desc ");

        if (!page.getFetchAll()) {
            sql.append("LIMIT :start , :offset ");
        }


        Query query = entityManager.createNativeQuery(sql.toString());


        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";

        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (criteriaDTO.getKeyword() != null) {
            query.setParameter("dwgNo", "%" + criteriaDTO.getKeyword() + "%");
            countQuery.setParameter("dwgNo", "%" + criteriaDTO.getKeyword() + "%");
            query.setParameter("drawingTitle", "%" + criteriaDTO.getKeyword() + "%");
            countQuery.setParameter("drawingTitle", "%" + criteriaDTO.getKeyword() + "%");
        }
        if (criteriaDTO.isSubDrawingFlg()) {
            query.setParameter("subDrawingFlg", true);
            countQuery.setParameter("subDrawingFlg", true);
        }

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        query.setParameter("status", EntityStatus.ACTIVE.name());
        countQuery.setParameter("status", EntityStatus.ACTIVE.name());

        query.setParameter("assignee", userId);
        countQuery.setParameter("assignee", userId);


        if (!page.getFetchAll()) {
            int pageNo = page.getPage().getNo();
            int pageSize = page.getPage().getSize();
            query.setParameter("start", (pageNo - 1) * pageSize);
            query.setParameter("offset", pageSize);
        }
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();

        Long count = (Long) countQuery.getSingleResult();


        List<DrawingDetail> dwgDetailList = new ArrayList<>();


        for (Map<String, Object> wpsMap : queryResultList) {
            DrawingDetail dwgDetail = new DrawingDetail();
            dwgDetail.setStatus(EntityStatus.ACTIVE);
            if (wpsMap.get("drawingCategoryId") != null) {
                SQLQueryBuilder<BpmEntitySubType> builder = getSQLQueryBuilder(BpmEntitySubType.class)
                    .is("id", wpsMap.get("drawingCategoryId").toString());
                List<BpmEntitySubType> category = builder.exec().list();
                if (!category.isEmpty()) {
                    dwgDetail.setEntitySubType(category.get(0).getNameEn());
                }
            }
            BeanUtils.copyProperties(wpsMap, dwgDetail, "status");
            dwgDetailList.add(dwgDetail);
        }

        return new PageImpl<>(dwgDetailList, page.toPageable(), count.longValue());
    }

    @Override
    public List<SelectDataDTO> getVersionList(Long drawingId, Long processId, Long userId) {
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT " +
            "  bai.version AS version " +
            "FROM " +
            "  bpm_activity_instance_base bai " +
            "  LEFT JOIN bpm_activity_instance_state bais ON bais.bai_id = bai.id " +
            "  LEFT JOIN bpm_act_task_assignee bata ON bata.act_inst_id = bai.id  " +
            "  LEFT JOIN drawing dr ON dr.id = bai.entity_id " +
            "  LEFT JOIN bpm_process_stage bps ON bps.id = bai.process_stage_id " +
            "  LEFT JOIN bpm_process bp ON bp.id = bai.process_id " +
            "WHERE " +
            "  bai.entity_id = :drawingId " +
            "  AND bai.process_id = :processId " +
            "  AND bai.act_category = 'Design'  " +
            "  AND bais.finish_state IS NOT NULL  " +
            "  AND bata.assignee = :userId " +
            "GROUP BY " +
            "  bai.version ");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("drawingId", drawingId);
        query.setParameter("processId", processId);
        query.setParameter("userId", userId);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        List<SelectDataDTO> result = new ArrayList<SelectDataDTO>();


        for (Map<String, Object> map : queryResultList) {
            SelectDataDTO data = new SelectDataDTO();
            BeanUtils.copyProperties(map, data);
            result.add(data);
        }
        return result;
    }

    @Override
    public List<SelectDataDTO> getTaskNodes(Long drawingId, String version, Long userId) {
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT " +
            "  bata.task_name AS task " +
            "FROM " +
            "  bpm_activity_instance_base bai " +
            "  LEFT JOIN bpm_activity_instance_state bais ON bais.bai_id = bai.id " +
            "  LEFT JOIN bpm_act_task_assignee bata ON bata.act_inst_id = bai.id  " +
            "  LEFT JOIN bpm_process_stage bps ON bps.id = bai.process_stage_id " +
            "  LEFT JOIN bpm_process bp ON bp.id = bai.process_id " +
            "WHERE " +
            "  bai.entity_id = :drawingId " +
            "  AND bai.act_category = 'Design'  " +
            "  AND bai.version = :version " +
            "  AND bais.finish_state IS NOT NULL  " +
            "  AND bata.assignee = :userId " +
            "GROUP BY " +
            "  bata.task_name ");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("drawingId", drawingId);
        query.setParameter("version", version);
        query.setParameter("userId", userId);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        List<SelectDataDTO> result = new ArrayList<SelectDataDTO>();


        for (Map<String, Object> map : queryResultList) {
            SelectDataDTO data = new SelectDataDTO();
            BeanUtils.copyProperties(map, data);
            result.add(data);
        }
        return result;
    }


}
