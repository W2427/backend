package com.ose.tasks.domain.model.repository;

import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.DocumentUploadHistorySearchDTO;
import com.ose.tasks.dto.bpm.ExInspReportCriteriaDTO;
import com.ose.tasks.dto.bpm.ExInspViewCriteriaDTO;
import com.ose.tasks.entity.DocumentHistory;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.vo.qc.InspectType;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.tasks.vo.qc.ReportSubType;
import com.ose.tasks.vo.qc.ReportType;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class DocumentHistoryRepositoryImpl extends BaseRepository implements DocumentHistoryRepositoryCustom {

    @Override
    public Page<DocumentHistory>uploadHistories(DocumentUploadHistorySearchDTO pageDTO, Long operatorId) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT ");
        sql.append("  his.`id`,");
        sql.append("  his.`created_at` AS createdAt,");
        sql.append("  his.`last_modified_at` AS lastModifiedAt,");
        sql.append("  his.`status`,");
        sql.append("  his.`file_id` AS fileId,");
        sql.append("  his.`file_path` AS filePath,");
        sql.append("  his.`operator` AS operatorId,");
        sql.append("  his.`operator_name` AS operatorName,");
        sql.append("  his.`file_name` AS fileName, ");
        sql.append("  his.`memo` as memo, ");
        sql.append("  his.`label` as label ");
        sql.append("FROM");
        sql.append("  `document_history` his ");
        sql.append("WHERE his.deleted is false ");

        if (pageDTO.getKeyword() != null
            && !"".equals(pageDTO.getKeyword())) {
            sql.append("AND his.file_name like :keyword ");
        }

        if (pageDTO.getLabel() != null && !"All".equals(pageDTO.getLabel())) {
            sql.append("AND his.label like :labels ");
        }

        sql.append(" ORDER BY his.created_at desc ");
        sql.append("LIMIT :start , :offset ");


        Query query = entityManager.createNativeQuery(sql.toString());


        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";

        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (pageDTO.getKeyword() != null
            && !"All".equals(pageDTO.getKeyword())) {
            query.setParameter("keyword", "%" + pageDTO.getKeyword() + "%");
            countQuery.setParameter("keyword", "%" + pageDTO.getKeyword() + "%");
        }

        if (pageDTO.getLabel() != null
            && !"".equals(pageDTO.getLabel())) {
            query.setParameter("labels", "%" + pageDTO.getLabel() + "%");
            countQuery.setParameter("labels", "%" + pageDTO.getLabel() + "%");
        }


        int pageNo = pageDTO.getPage().getNo();
        int pageSize = pageDTO.getPage().getSize();
        if (pageDTO.getFetchAll()) {
            pageSize = Integer.MAX_VALUE;
            pageDTO.getPage().setSize(pageSize);
        }
        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();

        Long count = (Long) countQuery.getSingleResult();


        List<DocumentHistory> resultList = new ArrayList<>();

        for (Map<String, Object> wpsMap : queryResultList) {
            DocumentHistory his = new DocumentHistory();
            if (wpsMap.get("status") != null) {
                his.setStatus(EntityStatus.valueOf((String) wpsMap.get("status")));
            }
            BeanUtils.copyProperties(wpsMap, his, "status");
            resultList.add(his);
        }

        return new PageImpl<>(resultList, pageDTO.toPageable(), count.longValue());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QCReport> externalInspectionViews(Long orgId, Long projectId, PageDTO pageDTO, ExInspViewCriteriaDTO criteriaDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("`qr`.`id` AS `id`,");
        sql.append("`qr`.`created_at` AS `created_at`,");
        sql.append("`qr`.`last_modified_at` AS `last_modified_at`,");
        sql.append("`qr`.`status` AS `status`,");
        sql.append("`qr`.`act_task_ids` AS `act_task_ids`,");
        sql.append("`qr`.`entity_nos` AS `entityNos`,");
        sql.append("`qr`.`excel_report_file_id` AS `excel_report_file_id`,");
        sql.append("`qr`.`inspect_parties` AS `inspect_parties`,");
        sql.append("`qr`.`nde_type` AS `nde_type`,");
        sql.append("`qr`.`operator` AS `operator`,");
        sql.append("`qr`.`org_id` AS `org_id`,");
        sql.append("`qr`.`pdf_report_file_id` AS `pdf_report_file_id`,");
        sql.append("`qr`.`pdf_report_page_count` AS `pdf_report_page_count`,");
        sql.append("`qr`.`proc_inst_ids` AS `proc_inst_ids`,");
        sql.append("`qr`.`process` AS `process`,");
        sql.append("`qr`.`process_stage` AS `processStage`,");
        sql.append("`qr`.`project_id` AS `project_id`,");
        sql.append("`qr`.`qrcode`,");
        sql.append("`qr`.`report_status` AS `report_status`,");
        sql.append("`qr`.`report_type` AS `reportType`,");
        sql.append("`qr`.`schedule_id` AS `schedule_id`,");
        sql.append("`qr`.`series_no` AS `seriesNo`,");
        sql.append("`qr`.`upload_file_id` AS `upload_file_id`,");
        sql.append("`qr`.`upload_report_page_count` AS `upload_report_page_count`,");
        sql.append("`qr`.`operator_name` AS `operator_name`,");
        sql.append("`qr`.`report_no` AS `reportNo`,");
        sql.append("`qr`.`second_upload_file_id` AS `second_upload_file_id`,");
        sql.append("`qr`.`memo` AS `memo`,");
        sql.append("`qr`.`series_num` AS `series_num`,");
        sql.append("`qr`.`inspect_result` AS `inspect_result`,");
        sql.append("`qr`.`inspect_type` AS `inspect_type`,");
        sql.append("`qr`.`is_cover` AS `is_cover`,");
        sql.append("`qr`.`is_one_off_report` AS `is_one_off_report`,");
        sql.append("`qr`.`is_second_upload` AS `is_second_upload`,");
        sql.append("`qr`.`parent_report_id` AS `parent_report_id`,");
        sql.append("`qr`.`report_sub_type` AS `report_sub_type`,");
        sql.append("`qr`.`is_report_batch_confirmed` AS `is_report_batch_confirmed`,");
        sql.append("`qr`.`module_name` AS `module_name`");

        sql.append("  FROM qc_report qr JOIN bpm_external_inspection_schedule beis ON beis.id = qr.schedule_id ");
        sql.append(" WHERE qr.org_id = :orgId ");
        sql.append(" AND qr.project_id = :projectId ");

        sql.append(" AND beis.state = 'COMMENTS' ");

        if (criteriaDTO.getKeyword() != null
            && !"".equals(criteriaDTO.getKeyword())) {

            sql.append(" AND qr.series_no = :keyword");
        }

        if (criteriaDTO.getOperator() != null) {
            sql.append(" AND qr.operator = :operatorId ");
        }

        List<EntityStatus> statusList = new ArrayList<>();
        statusList.add(EntityStatus.ACTIVE);
        statusList.add(EntityStatus.CLOSED);
        if (criteriaDTO.getState() != null && !criteriaDTO.getState().name().equalsIgnoreCase("all") ) {
            sql.append(" AND qr.status = :status");
        } else {
            sql.append(" AND qr.status IN :statusList");
        }

        sql.append(" ORDER BY qr.created_at desc ");
        sql.append("LIMIT :start , :offset ");


        Query query = entityManager.createNativeQuery(sql.toString());


        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";

        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (criteriaDTO.getKeyword() != null
            && !"".equals(criteriaDTO.getKeyword())) {
            query.setParameter("keyword", criteriaDTO.getKeyword());
            countQuery.setParameter("keyword", criteriaDTO.getKeyword());
        }

        if (criteriaDTO.getOperator() != null) {
            query.setParameter("operatorId", criteriaDTO.getOperator());
            countQuery.setParameter("operatorId", criteriaDTO.getOperator());
        }

        query.setParameter("orgId", orgId);
        countQuery.setParameter("orgId", orgId);

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        if (criteriaDTO.getState() != null && !criteriaDTO.getState().name().equalsIgnoreCase("all") ) {
            query.setParameter("status", criteriaDTO.getState().name());
            countQuery.setParameter("status", criteriaDTO.getState().name());
        } else {

            List<String> newStatusList = new ArrayList<>();
            newStatusList.add(EntityStatus.ACTIVE.name());
            newStatusList.add(EntityStatus.CLOSED.name());
            query.setParameter("statusList", newStatusList);
            countQuery.setParameter("statusList", newStatusList);
        }


        int pageNo = pageDTO.getPage().getNo();
        int pageSize = pageDTO.getPage().getSize();
        if (pageDTO.getFetchAll()) {
            pageSize = Integer.MAX_VALUE;
            pageDTO.getPage().setSize(pageSize);
        }
        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();

        Long count = (Long) countQuery.getSingleResult();


        List<QCReport> resultList = new ArrayList<>();

        for (Map<String, Object> map : queryResultList) {
            QCReport qr = new QCReport();
            if (map.get("status") != null) {
                qr.setStatus(EntityStatus.valueOf((String) map.get("status")));
            }
            if(map.get("report_status") != null) {
                qr.setReportStatus(ReportStatus.valueOf((String) map.get("report_status")));
            }
            if(map.get("reportType") != null) {
                qr.setReportType(ReportType.valueOf((String) map.get("reportType")));
            }
            if(map.get("report_sub_type") != null) {
                qr.setReportSubType(ReportSubType.valueOf((String) map.get("report_sub_type")));
            }
            if(map.get("inspect_type") != null) {
                qr.setInspectType(InspectType.valueOf((String) map.get("inspect_type")));
            }

            BeanUtils.copyProperties(map, qr, "status", "report_status", "reportType","report_sub_type","inspect_type");
            resultList.add(qr);
        }

        return new PageImpl<>(resultList, pageDTO.toPageable(), count.longValue());
    }

    @Override
    public Page<QCReport> externalInspectionReports(Long orgId, Long projectId, PageDTO pageDTO, ExInspReportCriteriaDTO criteriaDTO) {
        SQLQueryBuilder<QCReport> builder = getSQLQueryBuilder(QCReport.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("status", EntityStatus.ACTIVE);


        if (criteriaDTO.getOperator() != null) {
            builder.is("operator", criteriaDTO.getOperator());
        }

        if (criteriaDTO.getKeyword() != null
            && !"".equals(criteriaDTO.getKeyword())) {
            builder.like("seriesNo", criteriaDTO.getKeyword());
        }

        if (criteriaDTO.getState() != null) {
            if (criteriaDTO.getState().equals("UNDONE")) {
                builder.isNot("reportStatus", ReportStatus.DONE);
            } else if (criteriaDTO.getState().equals("DONE")) {
                builder.is("reportStatus", ReportStatus.DONE);
            }
        }

        builder.sort(new Sort.Order(Sort.Direction.DESC, "createdAt"));

        return builder.paginate(pageDTO.toPageable())
            .exec().page();
    }


}
