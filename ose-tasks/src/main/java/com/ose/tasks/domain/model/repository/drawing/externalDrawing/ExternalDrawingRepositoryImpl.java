package com.ose.tasks.domain.model.repository.drawing.externalDrawing;


import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.drawing.DrawingCriteriaDTO;
import com.ose.tasks.dto.drawing.ProofreadDrawingListCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.drawing.DrawingUploadZipFileHistory;
import com.ose.tasks.entity.drawing.DrawingUploadZipFileHistoryDetail;
import com.ose.tasks.entity.drawing.externalDrawing.ExternalDrawing;
import com.ose.tasks.vo.bpm.BpmsProcessNameEnum;
import com.ose.util.BeanUtils;
import com.ose.vo.BpmTaskDefKey;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 主图纸
 */
public class ExternalDrawingRepositoryImpl extends BaseRepository implements ExternalDrawingRepositoryCustom {

    private static final String SD_DWG_BOM_FORECAST = "SD_DWG_BOM_FORECAST";//生产设计预估材料表
    private static final List<String> ENTITY_CATEGORY = new ArrayList<>(
        Arrays.asList(SD_DWG_BOM_FORECAST));

    @Override
    public Page<ExternalDrawing> getList(Long orgId, Long projectId, PageDTO page,
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
//        sql.append("d.rev as rev, ");
        sql.append("d.sequence_no as sequenceNo, ");
        sql.append("d.file_id as fileId, ");
        sql.append("d.file_last_version as fileLastVersion, ");
        sql.append("d.file_name as fileName, ");
        sql.append("d.file_page_count as filePageCount, ");
        sql.append("d.file_path as filePath, ");
        sql.append("d.qr_code as qrCode, ");
//        sql.append("d.drawing_category_id as drawingCategoryId, ");
        sql.append("d.operator as operator, ");
        sql.append("d.locked as locked, ");
        sql.append("d.cover_id as coverId, ");
        sql.append("d.cover_path as coverPath, ");
        sql.append("d.engineering_finish_date as engineeringFinishDate, ");
        sql.append("d.engineering_start_date as engineeringStartDate, ");
        sql.append("d.estimated_man_hours as estimatedManHours, ");
        sql.append("d.drawing_delivery_file_name as drawingDeliveryFileName, ");
        sql.append("d.drawing_delivery_file_path as drawingDeliveryFilePath, ");
        sql.append("d.drawing_delivery_file_id as drawingDeliveryFileId, ");
//        sql.append("d.approved_user_id as approvedUserId, ");
        sql.append("d.approved_username as approvedUsername, ");
//        sql.append("d.check_user_id as checkUserId, ");
        sql.append("d.check_username as checkUsername, ");
//        sql.append("d.draw_user_id as drawUserId, ");
        sql.append("d.draw_username as drawUsername, ");
        sql.append("d.is_sub_drawing as isSubDrawing, ");
        sql.append("d.area_name as areaName, ");
        sql.append("d.module_name as moduleName, ");
        sql.append("d.is_info_put as isInfoPut, ");
        sql.append("d.block as block, ");
        sql.append("d.design_discipline as designDiscipline, ");
        sql.append("d.installation_drawing_no as installationDrawingNo, ");
        sql.append("d.section as section, ");
        sql.append("d.small_area as smallArea, ");
        sql.append("d.work_net as workNet, ");
//        sql.append("d.normal_hours as normalHours, ");
//        sql.append("d.plan_issue_date as planIssueDate, ");
//        sql.append("d.plan_start_date as planStartDate, ");

        sql.append("d.drawer as drawer, ");
        sql.append("d.drawer_id as drawerId ");
        sql.append("FROM external_drawing AS d ");
        sql.append("left join bpm_entity_sub_type AS c ON d.`drawing_category_id` = c.`id` ");
        sql.append("WHERE 1 = 1 ");

        whereSql.append("AND d.project_id = :projectId ");
        whereSql.append("AND d.status = :status ");

//        if (criteriaDTO.getDrawingCategoryId() != null) {
//            whereSql.append("AND d.drawing_category_id = :drawingCategoryId ");
//        }

        if (criteriaDTO.getInfoPut() != null) {
            whereSql.append("AND d.is_info_put = :isInfoPut ");
        }

        if (criteriaDTO.getAreaName() != null) {
            whereSql.append("AND d.area_name = :areaName ");
        }

        if (criteriaDTO.getModuleName() != null) {
            whereSql.append("AND d.module_name = :moduleName ");
        }

        if (criteriaDTO.getWorkNet() != null) {
            whereSql.append("AND d.work_net = :workNet ");
        }

        if (criteriaDTO.getSection() != null) {
            whereSql.append("AND d.section = :section ");
        }

        if (criteriaDTO.getBlock() != null) {
            whereSql.append("AND d.block = :block ");
        }

        if (criteriaDTO.getSmallArea() != null) {
            whereSql.append("AND d.small_area = :smallArea ");
        }

        if (criteriaDTO.getDesignDiscipline() != null) {
            whereSql.append("AND d.design_discipline = :designDiscipline ");
        }

        if (criteriaDTO.getLatestRev() != null) {
            whereSql.append("AND d.latest_rev = :latestRev ");
        }

        if (criteriaDTO.getKeyword() != null) {
            whereSql.append("AND (d.dwg_no like :dwgNo or d.drawing_title like :drawingTitle)");
        }

        if (criteriaDTO.getBomFlag() != null
            && criteriaDTO.getBomFlag()) {
            whereSql.append("AND c.name_en in (:entityCategories)");
            sql.append(whereSql);
        } else if (criteriaDTO.getBomFlag() != null
            && !criteriaDTO.getBomFlag()) {
            StringBuilder whereSql2 = new StringBuilder(whereSql);
            whereSql.append("AND c.name_en not in (:entityCategories)");
//            whereSql2.append("AND d.drawing_category_id is null");
            sql.append("AND ( 1 = 1 ");
            sql.append(whereSql);
            sql.append(") OR ( 1 = 1 ");
            sql.append(whereSql2);
            sql.append(")");
        } else {
            sql.append(whereSql);
        }

        sql.append(" ORDER BY d.created_at desc ");

        if (!page.getFetchAll()) {
            sql.append("LIMIT :start , :offset ");
        }

        // 查询结果
        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);


        if (criteriaDTO.getDrawingCategoryId() != null) {
            query.setParameter("drawingCategoryId", criteriaDTO.getDrawingCategoryId());
            countQuery.setParameter("drawingCategoryId", criteriaDTO.getDrawingCategoryId());
        }

        if (criteriaDTO.getKeyword() != null) {
            query.setParameter("dwgNo", "%" + criteriaDTO.getKeyword() + "%");
            countQuery.setParameter("dwgNo", "%" + criteriaDTO.getKeyword() + "%");
            query.setParameter("drawingTitle", "%" + criteriaDTO.getKeyword() + "%");
            countQuery.setParameter("drawingTitle", "%" + criteriaDTO.getKeyword() + "%");
        }

        if (criteriaDTO.getBomFlag() != null) {
            query.setParameter("entityCategories", ENTITY_CATEGORY);
            countQuery.setParameter("entityCategories", ENTITY_CATEGORY);
        }

        if (criteriaDTO.getAreaName() != null) {
            query.setParameter("areaName", criteriaDTO.getAreaName());
            countQuery.setParameter("areaName", criteriaDTO.getAreaName());
        }

        if (criteriaDTO.getModuleName() != null) {
            query.setParameter("moduleName", criteriaDTO.getModuleName());
            countQuery.setParameter("moduleName", criteriaDTO.getModuleName());
        }

        if (criteriaDTO.getWorkNet() != null) {
            query.setParameter("workNet", criteriaDTO.getWorkNet());
            countQuery.setParameter("workNet", criteriaDTO.getWorkNet());
        }

        if (criteriaDTO.getSection() != null) {
            query.setParameter("section", criteriaDTO.getSection());
            countQuery.setParameter("section", criteriaDTO.getSection());
        }

        if (criteriaDTO.getBlock() != null) {
            query.setParameter("block", criteriaDTO.getBlock());
            countQuery.setParameter("block", criteriaDTO.getBlock());
        }

        if (criteriaDTO.getSmallArea() != null) {
            query.setParameter("smallArea", criteriaDTO.getSmallArea());
            countQuery.setParameter("smallArea", criteriaDTO.getSmallArea());
        }

        if (criteriaDTO.getDesignDiscipline() != null) {
            query.setParameter("designDiscipline", criteriaDTO.getDesignDiscipline());
            countQuery.setParameter("designDiscipline", criteriaDTO.getDesignDiscipline());
        }

        if (criteriaDTO.getLatestRev() != null) {
            query.setParameter("latestRev", criteriaDTO.getLatestRev());
            countQuery.setParameter("latestRev", criteriaDTO.getLatestRev());
        }

        if (criteriaDTO.getInfoPut() != null) {
            if (criteriaDTO.getInfoPut().equals("true")) {
                query.setParameter("isInfoPut", 1);
                countQuery.setParameter("isInfoPut", 1);
            } else {
                query.setParameter("isInfoPut", 0);
                countQuery.setParameter("isInfoPut", 0);
            }

        }

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        query.setParameter("status", EntityStatus.ACTIVE.name());
        countQuery.setParameter("status", EntityStatus.ACTIVE.name());

        // 分页参数
        if (!page.getFetchAll()) {

            int pageNo = page.getPage().getNo();
            int pageSize = page.getPage().getSize();
            query.setParameter("start", (pageNo - 1) * pageSize);
            query.setParameter("offset", pageSize);
        }
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<ExternalDrawing> dwgList = new ArrayList<ExternalDrawing>();
//        Set<String> keys = new HashSet<String>();

        for (Map<String, Object> wpsMap : queryResultList) {
            ExternalDrawing dwg = new ExternalDrawing();
            dwg.setStatus(EntityStatus.ACTIVE);
            if (wpsMap.get("drawingCategoryId") != null) {
                SQLQueryBuilder<BpmEntitySubType> builder = getSQLQueryBuilder(BpmEntitySubType.class)
                    .is("id",wpsMap.get("drawingCategoryId").toString());
                List<BpmEntitySubType> category = builder.exec().list();
                if (!category.isEmpty()) {
                    dwg.setEntitySubType(category.get(0).getNameEn());
                }
            }
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
    public Page<ExternalDrawing> findByOrgIdAndProjectIdAndDrawingCategoryIsNotNullAndIdIn(Long orgId, Long projectId,
                                                                                   List<Long> entityIds, Pageable pageable, String keyword) {
        SQLQueryBuilder<ExternalDrawing> builder = getSQLQueryBuilder(ExternalDrawing.class)
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
            "        c.file_last_version as fileLastVersion, " +
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
            "from external_drawing c " +
            "left join drawing_detail e on e.drawing_id = c.id " +
            "where c.project_id = :projectId and c.org_id = :orgId and c.status = 'ACTIVE' " +
            " order by c.dwg_no");

        // 查询结果
        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("projectId", projectId);
        query.setParameter("orgId", orgId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();


        return queryResultList;
    }

    @Override
    public Page<ExternalDrawing> getProofreadDrawingList(
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
        sql.append("d.org_id as orgId, ");
        sql.append("d.paper_amount as paperAmount, ");
        sql.append("d.paper_use as paperUse, ");
        sql.append("d.printing as printing, ");
        sql.append("d.production_receiving_date as productionReceivingDate, ");
        sql.append("d.project_id as projectId, ");
        sql.append("d.quantity as quantity, ");
        sql.append("d.return_record as returnRecord, ");
//        sql.append("d.rev as rev, ");
        sql.append("d.sequence_no as sequenceNo, ");
        sql.append("d.file_id as fileId, ");
        sql.append("d.file_last_version as fileLastVersion, ");
        sql.append("d.file_name as fileName, ");
        sql.append("d.file_page_count as filePageCount, ");
        sql.append("d.file_path as filePath, ");
        sql.append("d.qr_code as qrCode, ");
//        sql.append("d.drawing_category_id as drawingCategoryId, ");
        sql.append("d.operator as operator, ");
        sql.append("d.locked as locked, ");
        sql.append("d.cover_id as coverId, ");
        sql.append("d.cover_path as coverPath, ");
        sql.append("d.engineering_finish_date as engineeringFinishDate, ");
        sql.append("d.engineering_start_date as engineeringStartDate, ");
        sql.append("d.estimated_man_hours as estimatedManHours, ");

//        sql.append("d.approved_user as approvedUser, ");
        sql.append("d.approved_username as approvedUsername, ");
//        sql.append("d.check_user as checkUser, ");
        sql.append("d.check_username as checkUsername, ");
//        sql.append("d.draw_user as drawUser, ");
        sql.append("d.draw_username as drawUsername, ");
        sql.append("d.drawing_delivery_file_name as drawingDeliveryFileName, ");
        sql.append("d.drawing_delivery_file_path as drawingDeliveryFilePath, ");
        sql.append("d.drawing_delivery_file_id as drawingDeliveryFileId, ");
//        sql.append("d.normal_hours as normalHours, ");
//        sql.append("d.plan_issue_date as planIssueDate, ");
//        sql.append("d.plan_start_date as planStartDate, ");

        sql.append("d.drawer as drawer, ");
        sql.append("d.drawer_id as drawerId ");

//        sql.append("draw_assignee.`assignee` as drawUserId, ");
//        sql.append("check_assignee.`assignee` as checkUserId, ");
//        sql.append("review_assignee.`assignee` as approvedUserId ");

        sql.append(" FROM ");
        sql.append("  external_drawing d ");
        sql.append("  INNER JOIN bpm_activity_instance a  ");
        sql.append("  ON a.`entity_id` = d.`id` ");
        sql.append("  INNER JOIN bpm_ru_task r ");
        sql.append("  ON a.`act_inst_id` = r.`act_inst_id` ");
        sql.append("  INNER JOIN bpm_hi_taskinst h ");
        sql.append("  ON a.`act_inst_id` = h.`act_inst_id` ");
        sql.append("  INNER JOIN bpm_entity_sub_type c ");
        sql.append("  ON a.`entity_sub_type_id` = c.id ");
//        sql.append("  LEFT JOIN bpm_act_task_assignee draw_assignee ");
//        sql.append("    ON draw_assignee.`act_inst_id` = a.`act_inst_id` AND draw_assignee.`task_def_key` = '" + BpmTaskDefKey.USERTASK_DRAWING_MODIFY.getType() + "'  ");
//        sql.append("  LEFT JOIN bpm_act_task_assignee check_assignee ");
//        sql.append("    ON check_assignee.`act_inst_id` = a.`act_inst_id` AND check_assignee.`task_def_key` = '" + BpmTaskDefKey.USERTASK_DRAWING_CHECK.getType() + "'  ");
//        sql.append("  LEFT JOIN bpm_act_task_assignee review_assignee ");
//        sql.append("    ON review_assignee.`act_inst_id` = a.`act_inst_id` AND review_assignee.`task_def_key` = '" + BpmTaskDefKey.USERTASK_DRAWING_REVIEW.getType() + "'  ");
        sql.append(" WHERE 1 = 1 ");

        whereSql.append(" AND d.project_id = :projectId ");
        whereSql.append(" AND d.status = :status ");
        whereSql.append(" AND a.`suspension_state` = 'ACTIVE' ");
        whereSql.append("  AND a.`a.process_name` IN ( '" + BpmsProcessNameEnum.ENGINEERING.getType());
        whereSql.append("' ,'" + BpmsProcessNameEnum.DRAWING_INTEGRAL_UPDATE.getType());
        whereSql.append("' ,'" + BpmsProcessNameEnum.DRAWING_PARTIAL_UPDATE.getType());
        whereSql.append("' ,'" + BpmsProcessNameEnum.DRAWING_REDMARK.getType());
        whereSql.append("'  ) ");
        if(criteriaDTO.getType().name().equals("MODIFY")){
            whereSql.append("  AND h.`task_def_key_` IN ( '" + BpmTaskDefKey.USERTASK_DRAWING_MODIFY.getType()+" ' ) ");
        }
        if(criteriaDTO.getType().name().equals("CHECK")){
            whereSql.append("  AND h.`task_def_key_` IN ( '" + BpmTaskDefKey.USERTASK_REDMARK_CHECK.getType()+ "' , '"+BpmTaskDefKey.USERTASK_DRAWING_CHECK.getType() + "' ) ");
        }
        if(criteriaDTO.getType().name().equals("REVIEW")){
            whereSql.append("  AND h.`task_def_key_` IN ( '" + BpmTaskDefKey.USERTASK_DRAWING_REVIEW.getType()+ "' ) ");
        }
        whereSql.append("  AND c.`sub_drawing_flg` = TRUE ");

        whereSql.append(" AND (r.`assignee_` =:assignee OR h.`assignee_` = :assignee) ");


        if (criteriaDTO.getKeyword() != null) {
            whereSql.append(" AND (d.dwg_no like :dwgNo or d.drawing_title like :drawingTitle) ");
        }

        sql.append(whereSql);


        sql.append(" ORDER BY d.created_at desc ");

        if (!page.getFetchAll()) {
            sql.append("LIMIT :start , :offset ");
        }

        // 查询结果
        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (criteriaDTO.getKeyword() != null) {
            query.setParameter("dwgNo", "%" + criteriaDTO.getKeyword() + "%");
            countQuery.setParameter("dwgNo", "%" + criteriaDTO.getKeyword() + "%");
            query.setParameter("drawingTitle", "%" + criteriaDTO.getKeyword() + "%");
            countQuery.setParameter("drawingTitle", "%" + criteriaDTO.getKeyword() + "%");
        }

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        query.setParameter("status", EntityStatus.ACTIVE.name());
        countQuery.setParameter("status", EntityStatus.ACTIVE.name());

        query.setParameter("assignee", userId);
        countQuery.setParameter("assignee", userId);

        // 分页参数
        if (!page.getFetchAll()) {
            int pageNo = page.getPage().getNo();
            int pageSize = page.getPage().getSize();
            query.setParameter("start", (pageNo - 1) * pageSize);
            query.setParameter("offset", pageSize);
        }
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<ExternalDrawing> dwgList = new ArrayList<ExternalDrawing>();
//        Set<String> keys = new HashSet<String>();

        for (Map<String, Object> wpsMap : queryResultList) {
            ExternalDrawing dwg = new ExternalDrawing();
            dwg.setStatus(EntityStatus.ACTIVE);
            if (wpsMap.get("drawingCategoryId") != null) {
                SQLQueryBuilder<BpmEntitySubType> builder = getSQLQueryBuilder(BpmEntitySubType.class)
                    .is("id", wpsMap.get("drawingCategoryId").toString());
                List<BpmEntitySubType> category = builder.exec().list();
                if (!category.isEmpty()) {
                    dwg.setEntitySubType(category.get(0).getNameEn());
                }
            }
            BeanUtils.copyProperties(wpsMap, dwg, "status");
            dwgList.add(dwg);
        }

        return new PageImpl<>(dwgList, page.toPageable(), count.longValue());
    }


}
