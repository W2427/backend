package com.ose.tasks.domain.model.repository.drawing;

import com.ose.auth.vo.OrgType;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.*;
import com.ose.util.BeanUtils;
import com.ose.vo.DrawingRecordStatus;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;

import com.ose.tasks.entity.drawing.DrawingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class DrawingRecordRepositoryImpl extends BaseRepository implements DrawingRecordRepositoryCustom {

    @Override
    public List<DrawingRecord> getListByCondition(
        Long orgId,
        Long projectId,
        DrawingRecordCriteriaDTO criteriaDTO
    ) {

        EntityManager entityManager = getEntityManager();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * from drawing_record a ");
        sql.append("WHERE a.org_id = :orgId ");
        sql.append("and a.project_id = :projectId ");
        sql.append("and a.deleted = 0 ");

        if (criteriaDTO.getDrawingId() != null) {
            sql.append("and a.drawing_id = :drawingId ");
        }
        if (criteriaDTO.getEngineerId() != null) {
            sql.append("and a.engineer_id = :engineerId ");
        }
        if (criteriaDTO.getEngineer() != null) {
            sql.append("and a.engineer = :engineer ");
        }
        if (criteriaDTO.getWorkHour() != null) {
            sql.append("and a.work_hour = :workHour ");
        }
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("orgId", orgId);
        query.setParameter("projectId", projectId);
        if (criteriaDTO.getDrawingId() != null) {
            query.setParameter("drawingId", criteriaDTO.getDrawingId());
        }
        if (criteriaDTO.getEngineerId() != null) {
            query.setParameter("engineerId", criteriaDTO.getEngineerId());
        }
        if (criteriaDTO.getEngineer() != null) {
            query.setParameter("engineer", criteriaDTO.getEngineer());
        }
        if (criteriaDTO.getWorkHour() != null) {
            query.setParameter("workHour", criteriaDTO.getWorkHour());
        }
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        List<DrawingRecord> results = new ArrayList<>();
        for (Map<String, Object> map : list) {
            DrawingRecord drawingRecord = new DrawingRecord();
            drawingRecord.setId(Long.valueOf((map.get("id") == null ? "" : map.get("id").toString())));
            drawingRecord.setOrgId(Long.valueOf((map.get("org_id") == null ? "" : map.get("org_id").toString())));
            drawingRecord.setProjectId(Long.valueOf((map.get("project_id") == null ? "" : map.get("project_id").toString())));
            drawingRecord.setDrawingId(Long.valueOf((map.get("drawing_id") == null ? "" : map.get("drawing_id").toString())));
            drawingRecord.setStage((map.get("stage") == null ? "" : map.get("stage").toString()));
            drawingRecord.setRev((map.get("rev") == null ? "" : map.get("rev").toString()));
            drawingRecord.setTask((map.get("task") == null ? "" : map.get("task").toString()));
            drawingRecord.setActivity((map.get("activity") == null ? "" : map.get("activity").toString()));
            drawingRecord.setEngineer((map.get("engineer") == null ? "" : map.get("engineer").toString()));
            drawingRecord.setEngineerId(Long.valueOf((map.get("engineer_id") == null ? "1" : map.get("engineer_id").toString())));
            drawingRecord.setWorkHour(Double.valueOf((map.get("work_hour") == null ? "0.0" : map.get("work_hour").toString())));
            drawingRecord.setWorkHourDate((map.get("work_hour_date") == null ? "" : map.get("work_hour_date").toString()));
            drawingRecord.setOutHour(Double.valueOf((map.get("out_hour") == null ? "0.0" : map.get("out_hour").toString())));
            results.add(drawingRecord);
        }
        return results;
    }

    @Override
    public Page<DrawingRecord> searchDetail(
        Long userId,
        ManHourDetailDTO manHourDetailDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" select dr.* ");
        sql.append(" from drawing_record dr ");
        sql.append(" where dr.engineer_id = :userId ");
        sql.append(" and dr.status = 'ACTIVE' ");

        if (manHourDetailDTO.getProjectName() != null && !"".equals(manHourDetailDTO.getProjectName())) {
            sql.append(" and dr.project_name = :projectName ");
        }
        if (manHourDetailDTO.getDrawingNo() != null && !"".equals(manHourDetailDTO.getDrawingNo())) {
            sql.append(" and dr.drawing_no = :drawingNo ");
        } else if ("".equals(manHourDetailDTO.getDrawingNo())) {
            sql.append(" and (dr.drawing_no is null or dr.drawing_no = '') ");
        }
        if (manHourDetailDTO.getDocumentTitle() != null && !"".equals(manHourDetailDTO.getDocumentTitle())) {
            sql.append(" and dr.document_title = :documentTitle ");
        } else if ("".equals(manHourDetailDTO.getDocumentTitle())) {
            sql.append(" and (dr.document_title is null or dr.document_title = '') ");
        }
        if (manHourDetailDTO.getStage() != null && !"".equals(manHourDetailDTO.getStage())) {
            sql.append(" and dr.stage = :stage ");
        } else if ("".equals(manHourDetailDTO.getStage())) {
            sql.append(" and (dr.stage is null or dr.stage = '') ");
        }
        if (manHourDetailDTO.getRev() != null && !"".equals(manHourDetailDTO.getRev())) {
            sql.append(" and dr.rev = :rev ");
        } else if ("".equals(manHourDetailDTO.getRev())) {
            sql.append(" and (dr.rev is null or dr.rev = '') ");
        }
        if (manHourDetailDTO.getTask() != null && !"".equals(manHourDetailDTO.getTask())) {
            sql.append(" and dr.task = :task ");
        } else if ("".equals(manHourDetailDTO.getTask())) {
            sql.append(" and (dr.task is null or dr.task = '') ");
        }
        if (manHourDetailDTO.getActivity() != null && !"".equals(manHourDetailDTO.getActivity())) {
            sql.append(" and dr.activity = :activity ");
        } else if ("".equals(manHourDetailDTO.getActivity())) {
            sql.append(" and (dr.activity is null or dr.activity = '') ");
        }
        if (manHourDetailDTO.getDiscipline() != null && !"".equals(manHourDetailDTO.getDiscipline())) {
            sql.append(" and dr.discipline = :discipline ");
        } else if ("".equals(manHourDetailDTO.getDiscipline())) {
            sql.append(" and (dr.discipline is null or dr.discipline = '') ");
        }
        if (manHourDetailDTO.getAssignedBy() != null && !"".equals(manHourDetailDTO.getAssignedBy())) {
            sql.append(" and dr.assigned_by = :assignedBy ");
        }

        if (manHourDetailDTO.getStartDate() != null && !"".equals(manHourDetailDTO.getStartDate())) {
            sql.append(" and dr.work_hour_date > :startDate ");
        }

        if (manHourDetailDTO.getEndDate() != null && !"".equals(manHourDetailDTO.getEndDate())) {
            sql.append(" and dr.work_hour_date < :endDate ");
        }
        sql.append(" order by dr.work_hour_date desc ");
        sql.append(" LIMIT :start , :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        query.setParameter("userId", userId);
        countQuery.setParameter("userId", userId);

        if (manHourDetailDTO.getProjectName() != null && !"".equals(manHourDetailDTO.getProjectName())) {
            query.setParameter("projectName", manHourDetailDTO.getProjectName());
            countQuery.setParameter("projectName", manHourDetailDTO.getProjectName());
        }

        if (manHourDetailDTO.getDrawingNo() != null && !"".equals(manHourDetailDTO.getDrawingNo())) {
            query.setParameter("drawingNo", manHourDetailDTO.getDrawingNo());
            countQuery.setParameter("drawingNo", manHourDetailDTO.getDrawingNo());
        }

        if (manHourDetailDTO.getDocumentTitle() != null && !"".equals(manHourDetailDTO.getDocumentTitle())) {
            query.setParameter("documentTitle", manHourDetailDTO.getDocumentTitle());
            countQuery.setParameter("documentTitle", manHourDetailDTO.getDocumentTitle());
        }

        if (manHourDetailDTO.getStage() != null && !"".equals(manHourDetailDTO.getStage())) {
            query.setParameter("stage", manHourDetailDTO.getStage());
            countQuery.setParameter("stage", manHourDetailDTO.getStage());
        }

        if (manHourDetailDTO.getRev() != null && !"".equals(manHourDetailDTO.getRev())) {
            query.setParameter("rev", manHourDetailDTO.getRev());
            countQuery.setParameter("rev", manHourDetailDTO.getRev());
        }

        if (manHourDetailDTO.getTask() != null && !"".equals(manHourDetailDTO.getTask())) {
            query.setParameter("task", manHourDetailDTO.getTask());
            countQuery.setParameter("task", manHourDetailDTO.getTask());
        }

        if (manHourDetailDTO.getActivity() != null && !"".equals(manHourDetailDTO.getActivity())) {
            query.setParameter("activity", manHourDetailDTO.getActivity());
            countQuery.setParameter("activity", manHourDetailDTO.getActivity());
        }

        if (manHourDetailDTO.getDiscipline() != null && !"".equals(manHourDetailDTO.getDiscipline())) {
            query.setParameter("discipline", manHourDetailDTO.getDiscipline());
            countQuery.setParameter("discipline", manHourDetailDTO.getDiscipline());
        }

        if (manHourDetailDTO.getAssignedBy() != null && !"".equals(manHourDetailDTO.getAssignedBy())) {
            query.setParameter("assignedBy", manHourDetailDTO.getAssignedBy());
            countQuery.setParameter("assignedBy", manHourDetailDTO.getAssignedBy());
        }

        if (manHourDetailDTO.getStartDate() != null && !"".equals(manHourDetailDTO.getStartDate())) {
            query.setParameter("startDate", manHourDetailDTO.getStartDate());
            countQuery.setParameter("startDate", manHourDetailDTO.getStartDate());
        }

        if (manHourDetailDTO.getEndDate() != null && !"".equals(manHourDetailDTO.getEndDate())) {
            query.setParameter("endDate", manHourDetailDTO.getEndDate());
            countQuery.setParameter("endDate", manHourDetailDTO.getEndDate());
        }

        sql.append("LIMIT :start , :offset ");

        // 查询结果
        int pageNo = manHourDetailDTO.getPage().getNo();
        int pageSize = manHourDetailDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<DrawingRecord> drawingRecords = new ArrayList<DrawingRecord>();
        for (Map<String, Object> resultMap : queryResultList) {
            DrawingRecord drawingRecord = new DrawingRecord();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

//            if (newMap.get("wareHouseType") != null) {
//                newMap.put("wareHouseType", WareHouseType.valueOf((String) newMap.get("wareHouseType")));
//            }
            if (newMap.get("status") != null) {
                newMap.put("status", EntityStatus.valueOf((String) newMap.get("status")));
            }
//            if (newMap.get("shippingStatus") != null) {
//                newMap.put("shippingStatus", ShippingStatus.valueOf((String) newMap.get("shippingStatus")));
//            }
            if (newMap.get("drawingRecordStatus") != null) {
                newMap.put("drawingRecordStatus", DrawingRecordStatus.valueOf((String) newMap.get("drawingRecordStatus")));
            }

            BeanUtils.copyProperties(newMap, drawingRecord);

            drawingRecords.add(drawingRecord);

        }
        return new PageImpl<>(drawingRecords, manHourDetailDTO.toPageable(), count.longValue());
    }


    @Override
    public Page<AttendanceDataDTO> searchAttendance(
        ManHourCriteriaDTO manHourCriteriaDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" u.username AS employeeId, ");
        sql.append(" u.name AS name, ");
        sql.append(" u.company AS company, ");
        sql.append(" u.division AS division, ");
        sql.append(" u.department AS department, ");
        sql.append(" dr.weekly, ");
        sql.append(" SUM(dr.work_hour) AS normalTime, ");
        sql.append(" SUM(dr.out_hour) AS overTime, ");
        sql.append(" SUM(dr.work_hour + dr.out_hour) AS totalHour, ");
        sql.append(" SUM(CASE WHEN WEEKDAY(dr.work_hour_date) = 0 THEN IFNULL(  dr.work_hour , 0 ) + IFNULL(  dr.out_hour , 0 ) ELSE 0 END) AS mondayHour, ");
        sql.append(" SUM(CASE WHEN WEEKDAY(dr.work_hour_date) = 1 THEN IFNULL(  dr.work_hour , 0 ) + IFNULL(  dr.out_hour , 0 ) ELSE 0 END) AS tuesdayHour, ");
        sql.append(" SUM(CASE WHEN WEEKDAY(dr.work_hour_date) = 2 THEN IFNULL(  dr.work_hour , 0 ) + IFNULL(  dr.out_hour , 0 ) ELSE 0 END) AS wednesdayHour, ");
        sql.append(" SUM(CASE WHEN WEEKDAY(dr.work_hour_date) = 3 THEN IFNULL(  dr.work_hour , 0 ) + IFNULL(  dr.out_hour , 0 ) ELSE 0 END) AS thursdayHour, ");
        sql.append(" SUM(CASE WHEN WEEKDAY(dr.work_hour_date) = 4 THEN IFNULL(  dr.work_hour , 0 ) + IFNULL(  dr.out_hour , 0 ) ELSE 0 END) AS fridayHour, ");
        sql.append(" SUM(CASE WHEN WEEKDAY(dr.work_hour_date) = 5 THEN IFNULL(  dr.work_hour , 0 ) + IFNULL(  dr.out_hour , 0 ) ELSE 0 END) AS saturdayHour, ");
        sql.append(" SUM(CASE WHEN WEEKDAY(dr.work_hour_date) = 6 THEN IFNULL(  dr.work_hour , 0 ) + IFNULL(  dr.out_hour , 0 ) ELSE 0 END) AS sundayHour ");
        sql.append(" FROM drawing_record dr ");
        sql.append(" INNER JOIN saint_whale_auth.users u ON dr.engineer_id = u.id  ");
        sql.append(" WHERE  ");
        sql.append(" u.username NOT IN ( 'testvp', 'admin', 'super', 'testnew', 'NT_101', 'SG_001', 'SG_002' ) ");
        sql.append(" AND dr.deleted IS FALSE ");
        sql.append(" AND u.STATUS = 'ACTIVE' ");

        if (manHourCriteriaDTO.getStartDate() != null && manHourCriteriaDTO.getEndDate() != null) {
            sql.append(" and dr.work_hour_date BETWEEN :startDate AND :endDate ");
        }

        if (manHourCriteriaDTO.getWeekly() != null) {
            sql.append(" and dr.weekly = :weekly ");
        }
        if (manHourCriteriaDTO.getCompany() != null) {
            sql.append(" and u.company = :company ");
        }
        if (manHourCriteriaDTO.getDivision() != null) {
            sql.append(" and u.division = :division ");
        }
        if (manHourCriteriaDTO.getDepartment() != null) {
            sql.append(" and u.department = :department ");
        }

        if (manHourCriteriaDTO.getKeyword() != null) {
            sql.append(" and u.name like :keyword ");
        }

        if (manHourCriteriaDTO.getStartDate() != null && manHourCriteriaDTO.getEndDate() != null) {
            sql.append(" GROUP BY u.username ");
        } else {
            sql.append(" GROUP BY dr.weekly,u.username ");
        }
        sql.append(" ORDER BY u.name  ");

        sql.append(" LIMIT :start , :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (manHourCriteriaDTO.getWeekly() != null) {
            query.setParameter("weekly", manHourCriteriaDTO.getWeekly());
            countQuery.setParameter("weekly", manHourCriteriaDTO.getWeekly());
        }

        if (manHourCriteriaDTO.getCompany() != null) {
            query.setParameter("company", manHourCriteriaDTO.getCompany());
            countQuery.setParameter("company", manHourCriteriaDTO.getCompany());
        }

        if (manHourCriteriaDTO.getDivision() != null) {
            query.setParameter("division", manHourCriteriaDTO.getDivision());
            countQuery.setParameter("division", manHourCriteriaDTO.getDivision());
        }

        if (manHourCriteriaDTO.getDepartment() != null) {
            query.setParameter("department", manHourCriteriaDTO.getDepartment());
            countQuery.setParameter("department", manHourCriteriaDTO.getDepartment());
        }


        if (manHourCriteriaDTO.getStartDate() != null && manHourCriteriaDTO.getEndDate() != null) {

            query.setParameter("startDate", manHourCriteriaDTO.getStartDate());
            countQuery.setParameter("startDate", manHourCriteriaDTO.getStartDate());

            query.setParameter("endDate", manHourCriteriaDTO.getEndDate());
            countQuery.setParameter("endDate", manHourCriteriaDTO.getEndDate());
        }


        if (manHourCriteriaDTO.getKeyword() != null) {
            query.setParameter("keyword", "%" + manHourCriteriaDTO.getKeyword() + "%");
            countQuery.setParameter("keyword", "%" + manHourCriteriaDTO.getKeyword() + "%");
        }

        sql.append("LIMIT :start , :offset ");

        // 查询结果
        int pageNo = manHourCriteriaDTO.getPage().getNo();
        int pageSize = manHourCriteriaDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<AttendanceDataDTO> attendanceDataDTOS = new ArrayList<AttendanceDataDTO>();
        for (Map<String, Object> resultMap : queryResultList) {
            AttendanceDataDTO attendanceDataDTO = new AttendanceDataDTO();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);
            BeanUtils.copyProperties(newMap, attendanceDataDTO);
            attendanceDataDTOS.add(attendanceDataDTO);

        }
        return new PageImpl<>(attendanceDataDTOS, manHourCriteriaDTO.toPageable(), count.longValue());
    }


    @Override
    public Page<ReviewDataDTO> searchReview(
        ReviewCriteriaDTO criteriaDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" dr.project_name AS projectName, ");
        sql.append(" dr.drawing_no AS drawingNo, ");
        sql.append(" dr.document_title AS documentTitle, ");
        sql.append(" dr.stage AS stage, ");
        sql.append(" dr.rev AS rev, ");
        sql.append(" dr.task AS task, ");
        sql.append(" dr.work_hour_date AS workHourDate, ");
        sql.append(" dr.activity AS activity, ");
        sql.append(" dr.discipline AS discipline, ");
        sql.append(" dr.remark AS remark, ");
        sql.append(" dr.assigned_by AS assignedBy, ");
        sql.append(" dr.engineer AS engineer, ");
        sql.append(" dr.work_hour AS workHour, ");
        sql.append(" dr.out_hour AS outHour, ");
        sql.append(" u.username AS username, ");
        sql.append(" u.company AS company, ");
        sql.append(" u.division AS division, ");
        sql.append(" u.department AS department ");
        sql.append(" FROM saint_whale_tasks.drawing_record dr ");
        sql.append(" LEFT JOIN saint_whale_auth.users u ON u.id = dr.engineer_id ");
        sql.append(" WHERE  ");
        sql.append(" dr.deleted IS FALSE ");
        if (criteriaDTO.getWeekly() != null) {
            sql.append(" and dr.weekly = :weekly ");
        }
        if (criteriaDTO.getDiscipline() != null) {
            sql.append(" and dr.discipline = :discipline ");
        }
        if (criteriaDTO.getCompany() != null) {
            sql.append(" and u.company = :company ");
        }
        if (criteriaDTO.getDivision() != null) {
            sql.append(" and u.division = :division ");
        }
        if (criteriaDTO.getDepartment() != null) {
            sql.append(" and u.department = :department ");
        }
        if (criteriaDTO.getUsername() != null) {
            sql.append(" and (u.username like :username or dr.engineer like :engineer ) ");
        }
        if (criteriaDTO.getProjectName() != null) {
            sql.append(" and dr.project_name like :projectName  ");
        }
        if (criteriaDTO.getStartDate() != null && criteriaDTO.getEndDate() != null) {
            sql.append(" and dr.work_hour_date between :startDate and :endDate  ");
        }


        if (criteriaDTO.getOrgType().equals(OrgType.COMPANY.toString())) {
            //判断帐号类型
            if (criteriaDTO.getAdmin() == null && criteriaDTO.getCompanyGM() != null && criteriaDTO.getCompanyGM()) {
                sql.append(" and u.company = :company2 ");
            } else if (criteriaDTO.getAdmin() == null && criteriaDTO.getDivisionVP() != null && criteriaDTO.getDivisionVP()) {
                sql.append(" and u.division = :division2 ");
            } else if (criteriaDTO.getAdmin() == null && criteriaDTO.getTeamLeader() != null && criteriaDTO.getTeamLeader()) {
                sql.append(" and u.team = :team ");
            } else if (criteriaDTO.getAdmin() == null) {
                sql.append(" and dr.engineer_id = :userId ");
            }
        } else {
            //判断是否为admin
            if (criteriaDTO.getAdmin() == null || !criteriaDTO.getAdmin()) {
                if (criteriaDTO.getUserIds() != null) {
                    sql.append(" and dr.engineer_id IN :userIds ");
                } else {
                    sql.append(" and dr.engineer_id = :userId ");
                }
                if (criteriaDTO.getOrgIds() != null) {
                    sql.append(" and dr.org_id IN :orgIds ");
                }
            }
        }


        sql.append(" LIMIT :start , :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (criteriaDTO.getWeekly() != null) {
            query.setParameter("weekly", criteriaDTO.getWeekly());
            countQuery.setParameter("weekly", criteriaDTO.getWeekly());
        }

        if (criteriaDTO.getDiscipline() != null) {
            query.setParameter("discipline", criteriaDTO.getDiscipline());
            countQuery.setParameter("discipline", criteriaDTO.getDiscipline());
        }

        if (criteriaDTO.getCompany() != null) {
            query.setParameter("company", criteriaDTO.getCompany());
            countQuery.setParameter("company", criteriaDTO.getCompany());
        }

        if (criteriaDTO.getStartDate() != null && criteriaDTO.getEndDate() != null) {
            query.setParameter("startDate", criteriaDTO.getStartDate());
            countQuery.setParameter("startDate", criteriaDTO.getStartDate());
            query.setParameter("endDate", criteriaDTO.getEndDate());
            countQuery.setParameter("endDate", criteriaDTO.getEndDate());
        }

        if (criteriaDTO.getUsername() != null) {
            query.setParameter("username", "%" + criteriaDTO.getUsername() + "%");
            countQuery.setParameter("username", "%" + criteriaDTO.getUsername() + "%");
            query.setParameter("engineer", "%" + criteriaDTO.getUsername() + "%");
            countQuery.setParameter("engineer", "%" + criteriaDTO.getUsername() + "%");
        }

        if (criteriaDTO.getProjectName() != null) {
            query.setParameter("projectName", "%" + criteriaDTO.getProjectName() + "%");
            countQuery.setParameter("projectName", "%" + criteriaDTO.getProjectName() + "%");
        }


        if (criteriaDTO.getDivision() != null) {
            query.setParameter("division", criteriaDTO.getDivision());
            countQuery.setParameter("division", criteriaDTO.getDivision());
        }


        if (criteriaDTO.getDepartment() != null) {
            query.setParameter("department", criteriaDTO.getDepartment());
            countQuery.setParameter("department", criteriaDTO.getDepartment());
        }

        if (criteriaDTO.getOrgType() == null || criteriaDTO.getOrgType().equals(OrgType.COMPANY.toString())) {
            if (criteriaDTO.getAdmin() == null && criteriaDTO.getCompanyGM() != null && criteriaDTO.getCompanyGM()) {
                query.setParameter("company2", criteriaDTO.getCompany2());
                countQuery.setParameter("company2", criteriaDTO.getCompany2());
            } else if (criteriaDTO.getAdmin() == null && criteriaDTO.getDivisionVP() != null && criteriaDTO.getDivisionVP()) {
                query.setParameter("division2", criteriaDTO.getDivision2());
                countQuery.setParameter("division2", criteriaDTO.getDivision2());
            } else if (criteriaDTO.getAdmin() == null && criteriaDTO.getTeamLeader() != null && criteriaDTO.getTeamLeader()) {
                query.setParameter("team", criteriaDTO.getTeam());
                countQuery.setParameter("team", criteriaDTO.getTeam());
            } else if (criteriaDTO.getAdmin() == null) {
                query.setParameter("userId", criteriaDTO.getUserId());
                countQuery.setParameter("userId", criteriaDTO.getUserId());
            }
        } else {
            //判断是否为admin
            if (criteriaDTO.getAdmin() == null || !criteriaDTO.getAdmin()) {
                if (criteriaDTO.getUserIds() != null) {
                    query.setParameter("userIds", criteriaDTO.getUserIds());
                    countQuery.setParameter("userIds", criteriaDTO.getUserIds());
                } else {
                    query.setParameter("userId", criteriaDTO.getUserId());
                    countQuery.setParameter("userId", criteriaDTO.getUserId());
                }
                if (criteriaDTO.getOrgIds() != null) {
                    query.setParameter("orgIds", criteriaDTO.getOrgIds());
                    countQuery.setParameter("orgIds", criteriaDTO.getOrgIds());
                }
            }
        }

        sql.append("LIMIT :start , :offset ");

        // 查询结果
        int pageNo = criteriaDTO.getPage().getNo();
        int pageSize = criteriaDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<ReviewDataDTO> reviewDataDTOs = new ArrayList<ReviewDataDTO>();
        for (Map<String, Object> resultMap : queryResultList) {
            ReviewDataDTO reviewDataDTO = new ReviewDataDTO();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);
            BeanUtils.copyProperties(newMap, reviewDataDTO);
            reviewDataDTOs.add(reviewDataDTO);

        }
        return new PageImpl<>(reviewDataDTOs, criteriaDTO.toPageable(), count.longValue());
    }

    @Override
    public List<WeeklyManHourDTO> weeklyManHour(
        ReviewCriteriaDTO criteriaDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();

        // 获取当前年份
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        sql.append(" SELECT ");
        sql.append(" u.username AS jobNumber, ");
        sql.append(" u.name AS employeeName, ");
        sql.append(" u.company, ");
        sql.append(" u.department, ");
        sql.append(" u.division, ");
        sql.append(" u.team, ");
        sql.append(" dr.project_name AS projectName, ");

        // 动态生成每周的列（假设一年最多53周）
        for (int week = 1; week <= 53; week++) {
            String weeklyCode = String.format("%d%d", currentYear, week); // 格式化为年份+周次
            sql.append(" SUM(CASE WHEN dr.weekly = ").append(weeklyCode)
                .append(" THEN IFNULL(dr.work_hour, 0) ELSE 0 END) AS week_").append(week).append(", ");
        }
        sql.delete(sql.length() - 2, sql.length()); // 删除最后一个多余的逗号
        sql.append(" FROM saint_whale_tasks.employee_information ei ");
        sql.append(" LEFT JOIN saint_whale_auth.users u ON u.username = ei.employee_id ");
        sql.append(" LEFT JOIN saint_whale_tasks.drawing_record dr ON dr.engineer_id = u.id ");
        sql.append(" WHERE  ");
        sql.append(" dr.deleted IS FALSE  ");
        sql.append(" AND dr.project_name != 'Leave'  ");
        if (criteriaDTO.getStartDate() != null && criteriaDTO.getEndDate()!=null) {
            sql.append(" AND dr.work_hour_date between :startDate and :endDate  ");
        }
        sql.append(" GROUP BY  ");
        sql.append(" dr.project_name,  ");
        sql.append(" u.username  ");
        sql.append(" ORDER BY  ");
        sql.append(" u.username  ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (criteriaDTO.getStartDate() != null && criteriaDTO.getEndDate() !=null) {
            query.setParameter("startDate", criteriaDTO.getStartDate());
            countQuery.setParameter("startDate", criteriaDTO.getStartDate());
            query.setParameter("endDate", criteriaDTO.getEndDate());
            countQuery.setParameter("endDate", criteriaDTO.getEndDate());
        }




        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

//        // 设置结果（结构为MAP）到WPS对象中
//        List<WeeklyManHourDTO> weeklyManHourDTOS = new ArrayList<WeeklyManHourDTO>();
//        for (Map<String, Object> resultMap : queryResultList) {
//            WeeklyManHourDTO weeklyManHourDTO = new WeeklyManHourDTO();
//            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);
//            BeanUtils.copyProperties(newMap, weeklyManHourDTO);
//            weeklyManHourDTOS.add(weeklyManHourDTO);
//
//        }
//        return weeklyManHourDTOS;

        // 将查询结果转换为 WeeklyManHourDTO 对象
        List<WeeklyManHourDTO> weeklyManHourDTOS = new ArrayList<>();
        for (Map<String, Object> resultMap : queryResultList) {
            WeeklyManHourDTO weeklyManHourDTO = new WeeklyManHourDTO();
            weeklyManHourDTO.setJobNumber((String) resultMap.get("jobNumber"));
            weeklyManHourDTO.setEmployeeName((String) resultMap.get("employeeName"));
            weeklyManHourDTO.setCompany((String) resultMap.get("company"));
            weeklyManHourDTO.setDepartment((String) resultMap.get("department"));
            weeklyManHourDTO.setDivision((String) resultMap.get("division"));
            weeklyManHourDTO.setTeam((String) resultMap.get("team"));
            weeklyManHourDTO.setProjectName((String) resultMap.get("projectName"));

            // 动态填充每周的工时数据
            for (int week = 1; week <= 53; week++) {
                String weekKey = "week_" + week; // 动态生成键名
                Double hours = resultMap.get(weekKey) != null ? (Double) resultMap.get(weekKey) : 0.0; // 防止 null
                weeklyManHourDTO.addWeeklyHour(weekKey, hours); // 添加到 Map
            }

            weeklyManHourDTOS.add(weeklyManHourDTO);
        }

        return weeklyManHourDTOS;

    }

    public List<CheckStandardWorkHourDTO> checkStandardWorkHour(
        ReviewCriteriaDTO criteriaDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" u.username AS jobNumber, ");
        sql.append(" u.NAME AS employeeName, ");
        sql.append(" u.company, ");
        sql.append(" u.department, ");
        sql.append(" u.division, ");
        sql.append(" u.team, ");
        sql.append(" SUM( CASE WHEN ( dr.project_name != 'Leave' ) THEN IFNULL( dr.work_hour, 0 ) ELSE 0 END ) AS totalNormalManHour, ");
        sql.append(" SUM( CASE WHEN ( dr.project_name = 'Leave' ) THEN IFNULL( dr.work_hour, 0 ) ELSE 0 END ) AS totalLeaveHour ");
        sql.append(" FROM saint_whale_tasks.employee_information ei ");
        sql.append(" LEFT JOIN saint_whale_auth.users u ON u.username = ei.employee_id ");
        sql.append(" LEFT JOIN saint_whale_tasks.drawing_record dr ON dr.engineer_id = u.id ");
        sql.append(" WHERE  ");
        if (criteriaDTO.getStartDate() != null && criteriaDTO.getEndDate()!=null) {
            sql.append(" dr.work_hour_date between :startDate and :endDate  ");
        }
        sql.append(" AND dr.task != 'Holiday' ");
        sql.append(" AND dr.deleted IS FALSE ");
        sql.append(" GROUP BY  ");
        sql.append(" u.username ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (criteriaDTO.getStartDate() != null && criteriaDTO.getEndDate() !=null) {
            query.setParameter("startDate", criteriaDTO.getStartDate());
            countQuery.setParameter("startDate", criteriaDTO.getStartDate());
            query.setParameter("endDate", criteriaDTO.getEndDate());
            countQuery.setParameter("endDate", criteriaDTO.getEndDate());
        }


        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<CheckStandardWorkHourDTO> checkStandardWorkHourDTOS = new ArrayList<CheckStandardWorkHourDTO>();
        for (Map<String, Object> resultMap : queryResultList) {
            CheckStandardWorkHourDTO checkStandardWorkHourDTO = new CheckStandardWorkHourDTO();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);
            BeanUtils.copyProperties(newMap, checkStandardWorkHourDTO);
            checkStandardWorkHourDTOS.add(checkStandardWorkHourDTO);

        }
        return checkStandardWorkHourDTOS;
    }
}
