package com.ose.tasks.domain.model.repository.drawing;


import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.drawing.DrawingWorkHourDTO;
import com.ose.tasks.entity.drawing.DrawingWorkHour;
import org.springframework.data.domain.Page;

/**
 * 工时管理
 */
public class DrawingWorkHourRepositoryImpl extends BaseRepository implements DrawingWorkHourRepositoryCustom {

    @Override
    public Page<DrawingWorkHour> getList(Long orgId, Long projectId, DrawingWorkHourDTO dto, PageDTO page) {
        SQLQueryBuilder<DrawingWorkHour> builder = getSQLQueryBuilder(DrawingWorkHour.class)
            .is("orgId", orgId)
            .is("projectId", projectId);

        if (dto.getSdrlCode() != null
            && !"".equals(dto.getProcess())) {
            builder.like("process", dto.getSdrlCode());
        }

//        if (criteriaDTO.getReportNo() != null
//            && !"".equals(criteriaDTO.getReportNo())) {
//            builder.like("reportNo", criteriaDTO.getReportNo());
//        }
//
//        if (criteriaDTO.getEntityNo() != null
//            && !"".equals(criteriaDTO.getEntityNo())) {
//            builder.like("entityNos", criteriaDTO.getEntityNo().replaceAll("\"", "\\\\\\\\\""));
//        }
//
//
//        if (criteriaDTO.getReportStatusItemList() != null) {
//            builder.in("reportStatus", criteriaDTO.getReportStatusItemList());
//        }
//
//        if (criteriaDTO.getSeriesNo() != null
//            && !"".equals(criteriaDTO.getSeriesNo())) {
//            try {
//                int seriesNo = Integer.parseInt(criteriaDTO.getSeriesNo());
//                builder.is("seriesNo", seriesNo);
//            } catch (Exception e) {
//                e.printStackTrace(System.out);
//            }
//
//        }
//
//        if (criteriaDTO.getOperatorName() != null
//            && !"".equals(criteriaDTO.getOperatorName())) {
//            builder.like("operatorName", criteriaDTO.getOperatorName());
//        }
//
//        if (criteriaDTO.getNdeType() != null
//            && !"".equals(criteriaDTO.getNdeType())) {
//            if ("--".equals(criteriaDTO.getNdeType())) {
//                builder.isNull("ndeType");
//            } else {
//                builder.is("ndeType", criteriaDTO.getNdeType());
//            }
//        }

        return builder.paginate(page.toPageable())
            .exec()
            .page();
    }

//    private static final String SD_DWG_BOM_FORECAST = "SD_DWG_BOM_FORECAST";
//    private static final List<String> ENTITY_CATEGORY = new ArrayList<>(
//        Arrays.asList(SD_DWG_BOM_FORECAST));
//
//    @Override
//    public List<DrawingCriteriaDTO> getParamList(Long orgId, Long projectId) {
//
//        EntityManager entityManager = getEntityManager();
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT ");
//        sql.append("d.latest_approved_rev as latestApprovedRev ");
//        sql.append("FROM drawing AS d ");
//        sql.append("left join bpm_entity_sub_type AS c ON d.`drawing_category_id` = c.`id` ");
//        sql.append("WHERE  d.project_id = :projectId ");
//        sql.append("AND d.status = :status ");
//        sql.append("GROUP BY latest_approved_rev");
//
//        Query query = entityManager.createNativeQuery(sql.toString());
//
//        query.setParameter("projectId", projectId);
//        query.setParameter("status", EntityStatus.ACTIVE.name());
//        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//
//        @SuppressWarnings("unchecked")
//        List<Map<String, Object>> queryResultList = query.getResultList();
//        List<DrawingCriteriaDTO> dwgList = new ArrayList<DrawingCriteriaDTO>();
//
//
//        for (Map<String, Object> m : queryResultList) {
//            DrawingCriteriaDTO dwg = new DrawingCriteriaDTO();
//            if (m.get("latestApprovedRev") == null) {
//                dwg.setLatestApprovedRev("空");
//            } else {
//                dwg.setLatestApprovedRev(m.get("latestApprovedRev").toString());
//            }
//            dwgList.add(dwg);
//        }
//        return dwgList;
//    }
//
//    @Override
//    public Page<Drawing> getList(Long orgId, Long projectId, PageDTO page,
//                                 DrawingCriteriaDTO criteriaDTO) {
//
//        EntityManager entityManager = getEntityManager();
//        StringBuilder sql = new StringBuilder();
//        StringBuilder whereSql = new StringBuilder();
//        sql.append("SELECT ");
//        sql.append("d.id as id, ");
//        sql.append("d.created_at as createdAt, ");
//        sql.append("d.last_modified_at as lastModifiedAt, ");
//        sql.append("d.status as status, ");
//        sql.append("d.actural_drawing_issue_date as acturalDrawingIssueDate, ");
//        sql.append("d.audit_no as auditNo, ");
//        sql.append("d.batch_task_id as batchTaskId, ");
//        sql.append("d.change_notice_no as changeNoticeNo, ");
//        sql.append("d.delivery_date as deliveryDate, ");
//        sql.append("d.design_change_review_form as designChangeReviewForm, ");
//        sql.append("d.drawing_title as drawingTitle, ");
//        sql.append("d.dwg_no as dwgNo, ");
//        sql.append("d.import_file_id as importFileId, ");
//        sql.append("d.issue_card_no as issueCardNo, ");
//        sql.append("d.latest_rev as latestRev, ");
//        sql.append("d.latest_approved_rev as latestApprovedRev, ");
//        sql.append("d.org_id as orgId, ");
//        sql.append("d.paper_amount as paperAmount, ");
//        sql.append("d.paper_use as paperUse, ");
//        sql.append("d.printing as printing, ");
//        sql.append("d.production_receiving_date as productionReceivingDate, ");
//        sql.append("d.project_id as projectId, ");
//        sql.append("d.quantity as quantity, ");
//        sql.append("d.return_record as returnRecord, ");
//
//        sql.append("d.sequence_no as sequenceNo, ");
//        sql.append("d.file_id as fileId, ");
//        sql.append("d.file_last_version as fileLastVersion, ");
//        sql.append("d.file_name as fileName, ");
//        sql.append("d.file_page_count as filePageCount, ");
//        sql.append("d.file_path as filePath, ");
//        sql.append("d.qr_code as qrCode, ");
//        sql.append("d.drawing_category_id as drawingCategoryId, ");
//        sql.append("d.operator as operator, ");
//        sql.append("d.locked as locked, ");
//        sql.append("d.cover_id as coverId, ");
//        sql.append("d.cover_path as coverPath, ");
//        sql.append("d.engineering_finish_date as engineeringFinishDate, ");
//        sql.append("d.engineering_start_date as engineeringStartDate, ");
//        sql.append("d.estimated_man_hours as estimatedManHours, ");
//        sql.append("d.drawing_delivery_file_name as drawingDeliveryFileName, ");
//        sql.append("d.drawing_delivery_file_path as drawingDeliveryFilePath, ");
//        sql.append("d.drawing_delivery_file_id as drawingDeliveryFileId, ");
//
//        sql.append("d.approved_username as approvedUsername, ");
//
//        sql.append("d.check_username as checkUsername, ");
//
//        sql.append("d.draw_username as drawUsername, ");
//
//
//
//
//        sql.append("d.drawer as drawer, ");
//        sql.append("d.drawer_id as drawerId ");
//        sql.append("FROM drawing AS d ");
//        sql.append("left join bpm_entity_sub_type AS c ON d.`drawing_category_id` = c.`id` ");
//        sql.append("WHERE 1 = 1 ");
//
//        whereSql.append("AND d.project_id = :projectId ");
//        whereSql.append("AND d.status = :status ");
//
//        if (criteriaDTO.getDrawingCategoryId() != null) {
//            whereSql.append("AND d.drawing_category_id = :drawingCategoryId ");
//        }
//
//        if (criteriaDTO.getActInst() != null) {
//            whereSql.append("AND d.locked = :locked ");
//        }
//
//        if (criteriaDTO.getLocked() != null) {
//            whereSql.append("AND d.locked = :locked ");
//        }
//
//        if ("null".equals(criteriaDTO.getLatestApprovedRev())) {
//            whereSql.append("AND d.latest_approved_rev is null ");
//        } else if (criteriaDTO.getLatestApprovedRev() != null) {
//            whereSql.append("AND d.latest_approved_rev = :latestApprovedRev ");
//        }
//
//        if (criteriaDTO.getKeyword() != null) {
//            whereSql.append("AND (d.dwg_no like :dwgNo or d.drawing_title like :drawingTitle)");
//        }
//
//        if (criteriaDTO.getBomFlag() != null
//            && criteriaDTO.getBomFlag()) {
//            whereSql.append("AND c.name_en in (:entityCategories)");
//            sql.append(whereSql);
//        } else if (criteriaDTO.getBomFlag() != null
//            && !criteriaDTO.getBomFlag()) {
//            StringBuilder whereSql2 = new StringBuilder(whereSql);
//            whereSql.append("AND c.name_en not in (:entityCategories)");
//            whereSql2.append("AND d.drawing_category_id is null");
//            sql.append("AND ( 1 = 1 ");
//            sql.append(whereSql);
//            sql.append(") OR ( 1 = 1 ");
//            sql.append(whereSql2);
//            sql.append(")");
//        } else {
//            sql.append(whereSql);
//        }
//
//        sql.append(" ORDER BY d.created_at desc ");
//
//        if (!page.getFetchAll()) {
//            sql.append("LIMIT :start , :offset ");
//        }
//
//
//        Query query = entityManager.createNativeQuery(sql.toString());
//
//
//        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
//
//        Query countQuery = entityManager.createNativeQuery(countSQL);
//
//
//        if (criteriaDTO.getDrawingCategoryId() != null) {
//            query.setParameter("drawingCategoryId", criteriaDTO.getDrawingCategoryId());
//            countQuery.setParameter("drawingCategoryId", criteriaDTO.getDrawingCategoryId());
//        }
//
//        if (criteriaDTO.getLatestApprovedRev() != null && !"null".equals(criteriaDTO.getLatestApprovedRev())) {
//            query.setParameter("latestApprovedRev", criteriaDTO.getLatestApprovedRev());
//            countQuery.setParameter("latestApprovedRev", criteriaDTO.getLatestApprovedRev());
//        }
//
//        if (criteriaDTO.getLocked() != null) {
//            query.setParameter("locked", criteriaDTO.getLocked());
//            countQuery.setParameter("locked", criteriaDTO.getLocked());
//        }
//
//        if (criteriaDTO.getKeyword() != null) {
//            query.setParameter("dwgNo", "%" + criteriaDTO.getKeyword() + "%");
//            countQuery.setParameter("dwgNo", "%" + criteriaDTO.getKeyword() + "%");
//            query.setParameter("drawingTitle", "%" + criteriaDTO.getKeyword() + "%");
//            countQuery.setParameter("drawingTitle", "%" + criteriaDTO.getKeyword() + "%");
//        }
//
//        if (criteriaDTO.getBomFlag() != null) {
//            query.setParameter("entityCategories", ENTITY_CATEGORY);
//            countQuery.setParameter("entityCategories", ENTITY_CATEGORY);
//        }
//
//        query.setParameter("projectId", projectId);
//        countQuery.setParameter("projectId", projectId);
//
//        query.setParameter("status", EntityStatus.ACTIVE.name());
//        countQuery.setParameter("status", EntityStatus.ACTIVE.name());
//
//
//        if (!page.getFetchAll()) {
//
//            int pageNo = page.getPage().getNo();
//            int pageSize = page.getPage().getSize();
//            query.setParameter("start", (pageNo - 1) * pageSize);
//            query.setParameter("offset", pageSize);
//        }
//        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//
//
//
//        @SuppressWarnings("unchecked")
//        List<Map<String, Object>> queryResultList = query.getResultList();
//
//        Long count = (Long) countQuery.getSingleResult();
//
//
//        List<Drawing> dwgList = new ArrayList<Drawing>();
//
//
//        for (Map<String, Object> wpsMap : queryResultList) {
//            Drawing dwg = new Drawing();
//            dwg.setStatus(EntityStatus.ACTIVE);
//            if (wpsMap.get("drawingCategoryId") != null) {
//                SQLQueryBuilder<BpmEntitySubType> builder = getSQLQueryBuilder(BpmEntitySubType.class)
//                    .is("id",wpsMap.get("drawingCategoryId").toString());
//                List<BpmEntitySubType> category = builder.exec().list();
//                if (!category.isEmpty()) {
//                    dwg.setDrawingCategory(category.get(0));
//                }
//            }
//            BeanUtils.copyProperties(wpsMap, dwg, "status");
//            dwgList.add(dwg);
//        }
//
//
//        if (page.getFetchAll()) {
//            return new PageImpl<>(
//                dwgList
//            );
//        }
//
//        return new PageImpl<>(dwgList, page.toPageable(), count.longValue());
//    }
//
//    @Override
//    public Page<DrawingUploadZipFileHistory> zipFileHistory(Long orgId, Long projectId, Long drawingId, PageDTO page) {
//        SQLQueryBuilder<DrawingUploadZipFileHistory> builder = getSQLQueryBuilder(DrawingUploadZipFileHistory.class)
//            .is("orgId", orgId)
//            .is("projectId", projectId)
//            .is("drawingId", drawingId)
//            .is("status", EntityStatus.ACTIVE);
//
//        return builder.paginate(page.toPageable())
//            .exec()
//            .page();
//    }
//
//    @Override
//    public Page<DrawingUploadZipFileHistoryDetail> zipFileHistoryDetail(Long orgId, Long projectId,
//                                                                        Long drawingId, Long id, PageDTO page) {
//        SQLQueryBuilder<DrawingUploadZipFileHistoryDetail> builder = getSQLQueryBuilder(DrawingUploadZipFileHistoryDetail.class)
//            .is("orgId", orgId)
//            .is("projectId", projectId)
//            .is("drawingUploadZipFileHistoryId", id)
//            .is("status", EntityStatus.ACTIVE);
//
//        return builder.paginate(page.toPageable())
//            .exec()
//            .page();
//    }
//
//    @Override
//    public Page<Drawing> findByOrgIdAndProjectIdAndDrawingCategoryIsNotNullAndIdIn(Long orgId, Long projectId,
//                                                                                   List<Long> entityIds, Pageable pageable, String keyword) {
//        SQLQueryBuilder<Drawing> builder = getSQLQueryBuilder(Drawing.class)
//            .is("orgId", orgId)
//            .is("projectId", projectId)
//            .like("dwgNo", keyword)
//            .isNotNull("drawingCategory")
//            .notIn("id", entityIds)
//            .is("status", EntityStatus.ACTIVE);
//
//        return builder.paginate(pageable)
//            .exec()
//            .page();
//
//    }
//
//    @Override
//    public List<Map<String, Object>> getXlsList(Long orgId, Long projectId) {
//
//        EntityManager entityManager = getEntityManager();
//        StringBuilder sql = new StringBuilder();
//        sql.append("select " +
//            "c.id as id, " +
//            "        e.created_at as createdAt, " +
//            "        e.last_modified_at as lastModifiedAt, " +
//            "        e.status as status, " +
//            "        e.actural_drawing_issue_date as acturalDrawingIssueDate, " +
//            "        e.audit_no as auditNo, " +
//            "        c.batch_task_id as batchTaskId, " +
//            "        e.change_notice_no as changeNoticeNo, " +
//            "        e.delivery_date as deliveryDate, " +
//            "        e.design_change_review_form as designChangeReviewForm, " +
//            "        c.drawing_title as drawingTitle, " +
//            "        c.dwg_no as dwgNo, " +
//            "        c.import_file_id as importFileId, " +
//            "        e.issue_card_no as issueCardNo, " +
//            "        c.latest_rev as latestRev, " +
//            "        c.org_id as org_id, " +
//            "        e.paper_amount as paperAmount, " +
//            "        ifnull(e.paper_use,0) as paperUse, " +
//            "        e.printing as printing, " +
//            "        e.production_receiving_date as productionReceivingDate, " +
//            "        c.project_id as projectId, " +
//            "        e.quantity as quantity, " +
//            "        e.return_record as returnRecord, " +
//            "        e.rev as rev, " +
//            "        c.sequence_no as sequenceNo, " +
//            "        e.file_id as file_id, " +
//            "        c.file_last_version as fileLastVersion, " +
//            "        e.file_name as fileName, " +
//            "        c.file_page_count as filePageCount, " +
//            "        e.file_path as filePath, " +
//            "        e.qr_code as qrCode, " +
//            "        c.drawing_category_id as drawingCategoryId, " +
//            "        c.operator as operator, " +
//            "        c.locked as locked, " +
//            "        c.cover_id as coverId, " +
//            "        c.cover_path as coverPath, " +
//            "        c.cover_name as coverName, " +
//            "        c.drawer as drawer, " +
//            "        c.drawer_id as drawerId, " +
//            "        c.engineering_finish_date as engineeringFinishDate, " +
//            "        c.engineering_start_date as engineeringStartDate " +
//            "from drawing c " +
//            "left join drawing_detail e on e.drawing_id = c.id " +
//            "where c.project_id = :projectId and c.org_id = :orgId and c.status = 'ACTIVE' " +
//            " order by c.dwg_no");
//
//
//        Query query = entityManager.createNativeQuery(sql.toString());
//
//        query.setParameter("projectId", projectId);
//        query.setParameter("orgId", orgId);
//
//        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//
//
//
//        @SuppressWarnings("unchecked")
//        List<Map<String, Object>> queryResultList = query.getResultList();
//
//
//        return queryResultList;
//    }
//
//    @Override
//    public Page<Drawing> getProofreadDrawingList(
//        Long orgId,
//        Long projectId,
//        Long userId,
//        PageDTO page,
//        ProofreadDrawingListCriteriaDTO criteriaDTO
//    ) {
//
//        EntityManager entityManager = getEntityManager();
//        StringBuilder sql = new StringBuilder();
//        StringBuilder whereSql = new StringBuilder();
//        sql.append("SELECT DISTINCT ");
//        sql.append("d.id as id, ");
//        sql.append("d.created_at as createdAt, ");
//        sql.append("d.last_modified_at as lastModifiedAt, ");
//        sql.append("d.status as status, ");
//        sql.append("d.actural_drawing_issue_date as acturalDrawingIssueDate, ");
//        sql.append("d.audit_no as auditNo, ");
//        sql.append("d.batch_task_id as batchTaskId, ");
//        sql.append("d.change_notice_no as changeNoticeNo, ");
//        sql.append("d.delivery_date as deliveryDate, ");
//        sql.append("d.design_change_review_form as designChangeReviewForm, ");
//        sql.append("d.drawing_title as drawingTitle, ");
//        sql.append("d.dwg_no as dwgNo, ");
//        sql.append("d.import_file_id as importFileId, ");
//        sql.append("d.issue_card_no as issueCardNo, ");
//        sql.append("d.latest_rev as latestRev, ");
//        sql.append("d.org_id as orgId, ");
//        sql.append("d.paper_amount as paperAmount, ");
//        sql.append("d.paper_use as paperUse, ");
//        sql.append("d.printing as printing, ");
//        sql.append("d.production_receiving_date as productionReceivingDate, ");
//        sql.append("d.project_id as projectId, ");
//        sql.append("d.quantity as quantity, ");
//        sql.append("d.return_record as returnRecord, ");
//
//        sql.append("d.sequence_no as sequenceNo, ");
//        sql.append("d.file_id as fileId, ");
//        sql.append("d.file_last_version as fileLastVersion, ");
//        sql.append("d.file_name as fileName, ");
//        sql.append("d.file_page_count as filePageCount, ");
//        sql.append("d.file_path as filePath, ");
//        sql.append("d.qr_code as qrCode, ");
//        sql.append("d.drawing_category_id as drawingCategoryId, ");
//        sql.append("d.operator as operator, ");
//        sql.append("d.locked as locked, ");
//        sql.append("d.cover_id as coverId, ");
//        sql.append("d.cover_path as coverPath, ");
//        sql.append("d.engineering_finish_date as engineeringFinishDate, ");
//        sql.append("d.engineering_start_date as engineeringStartDate, ");
//        sql.append("d.estimated_man_hours as estimatedManHours, ");
//
//
//        sql.append("d.approved_username as approvedUsername, ");
//
//        sql.append("d.check_username as checkUsername, ");
//
//        sql.append("d.draw_username as drawUsername, ");
//        sql.append("d.drawing_delivery_file_name as drawingDeliveryFileName, ");
//        sql.append("d.drawing_delivery_file_path as drawingDeliveryFilePath, ");
//        sql.append("d.drawing_delivery_file_id as drawingDeliveryFileId, ");
//
//
//
//
//        sql.append("d.drawer as drawer, ");
//        sql.append("d.drawer_id as drawerId ");
//
//
//
//
//
//        sql.append(" FROM ");
//        sql.append("  drawing d ");
//        sql.append("  INNER JOIN bpm_activity_instance_base a  ");
//        sql.append("  ON a.`entity_id` = d.`id` ");
//        sql.append("  INNER JOIN bpm_activity_instance_state  `as` ON a.project_id = as.project_id AND a.id = as.bai_id  ");
//        sql.append("  INNER JOIN bpm_ru_task r ");
//        sql.append("  ON a.`act_inst_id` = r.`act_inst_id` ");
//        sql.append("  INNER JOIN bpm_hi_taskinst h ");
//        sql.append("  ON a.`act_inst_id` = h.`act_inst_id` ");
//        sql.append("  INNER JOIN bpm_entity_sub_type c ");
//        sql.append("  ON a.`entity_sub_type_id` = c.id ");
//
//
//
//
//
//
//        sql.append(" WHERE 1 = 1 ");
//
//        whereSql.append(" AND d.project_id = :projectId ");
//        whereSql.append(" AND d.status = :status ");
//        whereSql.append(" AND `as`.`suspension_state` = 'ACTIVE' ");
//        whereSql.append("  AND a.`process_name` IN ( '" + BpmsProcessNameEnum.ENGINEERING.getType());
//        whereSql.append("' ,'" + BpmsProcessNameEnum.DRAWING_INTEGRAL_UPDATE.getType());
//        whereSql.append("' ,'" + BpmsProcessNameEnum.DRAWING_PARTIAL_UPDATE.getType());
//        whereSql.append("' ,'" + BpmsProcessNameEnum.DRAWING_REDMARK.getType());
//        whereSql.append("'  ) ");
//        if(criteriaDTO.getType().name().equals("MODIFY")){
//            whereSql.append("  AND h.`task_def_key_` IN ( '" + BpmTaskDefKey.USERTASK_DRAWING_MODIFY.getType()+" ' ) ");
//        }
//        if(criteriaDTO.getType().name().equals("CHECK")){
//            whereSql.append("  AND h.`task_def_key_` IN ( '" + BpmTaskDefKey.USERTASK_REDMARK_CHECK.getType()+ "' , '"+BpmTaskDefKey.USERTASK_DRAWING_CHECK.getType() + "' ) ");
//        }
//        if(criteriaDTO.getType().name().equals("REVIEW")){
//            whereSql.append("  AND h.`task_def_key_` IN ( '" + BpmTaskDefKey.USERTASK_DRAWING_REVIEW.getType()+ "' ) ");
//        }
//        whereSql.append("  AND c.`sub_drawing_flg` = TRUE ");
//
//        whereSql.append(" AND (r.`assignee_` =:assignee OR h.`assignee_` = :assignee) ");
//
//
//        if (criteriaDTO.getKeyword() != null) {
//            whereSql.append(" AND (d.dwg_no like :dwgNo or d.drawing_title like :drawingTitle) ");
//        }
//
//        sql.append(whereSql);
//
//
//        sql.append(" ORDER BY d.created_at desc ");
//
//        if (!page.getFetchAll()) {
//            sql.append("LIMIT :start , :offset ");
//        }
//
//
//        Query query = entityManager.createNativeQuery(sql.toString());
//
//
//        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
//
//        Query countQuery = entityManager.createNativeQuery(countSQL);
//
//        if (criteriaDTO.getKeyword() != null) {
//            query.setParameter("dwgNo", "%" + criteriaDTO.getKeyword() + "%");
//            countQuery.setParameter("dwgNo", "%" + criteriaDTO.getKeyword() + "%");
//            query.setParameter("drawingTitle", "%" + criteriaDTO.getKeyword() + "%");
//            countQuery.setParameter("drawingTitle", "%" + criteriaDTO.getKeyword() + "%");
//        }
//
//        query.setParameter("projectId", projectId);
//        countQuery.setParameter("projectId", projectId);
//
//        query.setParameter("status", EntityStatus.ACTIVE.name());
//        countQuery.setParameter("status", EntityStatus.ACTIVE.name());
//
//        query.setParameter("assignee", userId);
//        countQuery.setParameter("assignee", userId);
//
//
//        if (!page.getFetchAll()) {
//            int pageNo = page.getPage().getNo();
//            int pageSize = page.getPage().getSize();
//            query.setParameter("start", (pageNo - 1) * pageSize);
//            query.setParameter("offset", pageSize);
//        }
//        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//
//
//
//        @SuppressWarnings("unchecked")
//        List<Map<String, Object>> queryResultList = query.getResultList();
//
//        Long count = (Long) countQuery.getSingleResult();
//
//
//        List<Drawing> dwgList = new ArrayList<Drawing>();
//
//
//        for (Map<String, Object> wpsMap : queryResultList) {
//            Drawing dwg = new Drawing();
//            dwg.setStatus(EntityStatus.ACTIVE);
//            if (wpsMap.get("drawingCategoryId") != null) {
//                SQLQueryBuilder<BpmEntitySubType> builder = getSQLQueryBuilder(BpmEntitySubType.class)
//                    .is("id", wpsMap.get("drawingCategoryId").toString());
//                List<BpmEntitySubType> category = builder.exec().list();
//                if (!category.isEmpty()) {
//                    dwg.setDrawingCategory(category.get(0));
//                }
//            }
//            BeanUtils.copyProperties(wpsMap, dwg, "status");
//            dwgList.add(dwg);
//        }
//
//        return new PageImpl<>(dwgList, page.toPageable(), count.longValue());
//    }


}
