package com.ose.tasks.domain.model.repository;

import com.ose.exception.BusinessError;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.OverTimeApplicationFormSearchDTO;
import com.ose.tasks.dto.PerformanceEvaluationDataDTO;
import com.ose.tasks.dto.PerformanceEvaluationSearchDTO;
import com.ose.tasks.entity.OverTimeApplicationForm;
import com.ose.tasks.vo.ApplyStatus;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class OverTimeApplicationRepositoryImpl extends BaseRepository implements OverTimeApplicationRepositoryCustom {


    @Override
    public Page<OverTimeApplicationForm> search(
        Long operatorId,
        OverTimeApplicationFormSearchDTO dto) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ha.* ");
        sql.append(" from overtime_apply_form ha ");
        sql.append(" where ha.deleted is false ");

        // 3、如果是普通人，则只展示自己的信息；管理员全部展示
        if (dto.getUserType() != null && !dto.getUserType().equals("administrator") &&
            dto.getReviewRole() == null && dto.getApplyRole() == null &&
            dto.getTeamLeader() == null && dto.getDivisionVP() == null && dto.getCompanyGM() == null) {
            sql.append(" and ha.user_id = :userId ");
        }

        // 1、当前帐号为行政审核
        if (dto.getUserType() != null && !dto.getUserType().equals("administrator") &&
            dto.getReviewRole() != null && dto.getReviewRole()) {
            sql.append(" and (ha.review_role_id like :reviewRoleId or ha.user_id like :reviewRoleId) ");
        }

        // 2、当前帐号为项目主管、teamLeader、divisionVP、companyGM
        if (dto.getUserType() != null && !dto.getUserType().equals("administrator") &&
            ((dto.getApplyRole() != null && dto.getApplyRole()) ||
                (dto.getTeamLeader() != null && dto.getTeamLeader()) ||
                (dto.getDivisionVP() != null && dto.getDivisionVP()) ||
                (dto.getCompanyGM() != null && dto.getCompanyGM()))) {
            sql.append(" and (ha.apply_role_id like :applyRoleId or ha.user_id = :userId) ");
        }

        if (dto.getKeyword() != null && !"".equals(dto.getKeyword())) {
            sql.append(" and (ha.employee_id like :employeeId or ha.display_name like :displayName ) ");
        }

        if (dto.getProjectName() != null && !"".equals(dto.getProjectName())) {
            sql.append(" and ha.project_name = :projectName ");
        }

        if (dto.getCompany() != null && !"".equals(dto.getCompany())) {
            sql.append(" and ha.company = :company ");
        }

        if (dto.getDepartment() != null && !"".equals(dto.getDepartment())) {
            sql.append(" and ha.department = :department ");
        }

        if (dto.getDivision() != null && !"".equals(dto.getDivision())) {
            sql.append(" and ha.division = :division ");
        }

        if (dto.getTeam() != null && !"".equals(dto.getTeam())) {
            sql.append(" and ha.team = :team ");
        }

        if (dto.getTask() != null && !"".equals(dto.getTask())) {
            sql.append(" and ha.task like :task ");
        }

        if (dto.getApplyStatus() != null) {
            sql.append(" and ha.apply_status = :applyStatus ");
        }

        if (dto.getReviewStatus() != null) {
            sql.append(" and ha.review_status = :reviewStatus ");
        }

        if (dto.getStartDateTime() != null) {
            sql.append(" and ha.start_date >= :startDateFrom ");
        }

        if (dto.getEndDateTime() != null) {
            sql.append(" and ha.start_date <= :endDateFrom ");
        }

        sql.append(" ORDER BY ha.created_at desc ");
        sql.append(" LIMIT :start , :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (dto.getUserType() != null && !dto.getUserType().equals("administrator") &&
            dto.getReviewRole() != null && dto.getReviewRole()) {
            query.setParameter("reviewRoleId", "%" + operatorId + "%");
            countQuery.setParameter("reviewRoleId", "%" + operatorId + "%");
        }

        if (dto.getUserType() != null && !dto.getUserType().equals("administrator") && (
            (dto.getApplyRole() != null && dto.getApplyRole()) ||
                (dto.getTeamLeader() != null && dto.getTeamLeader()) ||
                (dto.getDivisionVP() != null && dto.getDivisionVP()) ||
                (dto.getCompanyGM() != null && dto.getCompanyGM()))) {
            query.setParameter("applyRoleId", "%" + operatorId + "%");
            countQuery.setParameter("applyRoleId", "%" + operatorId + "%");
            query.setParameter("userId", operatorId);
            countQuery.setParameter("userId", operatorId);
        }

        if (dto.getUserType() != null && !dto.getUserType().equals("administrator") &&
            dto.getReviewRole() == null && dto.getApplyRole() == null &&
            dto.getTeamLeader() == null && dto.getDivisionVP() == null && dto.getCompanyGM() == null) {
            query.setParameter("userId", operatorId);
            countQuery.setParameter("userId", operatorId);
        }

        if (dto.getKeyword() != null && !"".equals(dto.getKeyword())) {
            query.setParameter("employeeId", "%" + dto.getKeyword() + "%");
            countQuery.setParameter("employeeId", "%" + dto.getKeyword() + "%");
            query.setParameter("displayName", "%" + dto.getKeyword() + "%");
            countQuery.setParameter("displayName", "%" + dto.getKeyword() + "%");
        }

        if (dto.getApplyStatus() != null && !"".equals(dto.getApplyStatus())) {
            query.setParameter("applyStatus", dto.getApplyStatus());
            countQuery.setParameter("applyStatus", dto.getApplyStatus());
        }

        if (dto.getReviewStatus() != null && !"".equals(dto.getReviewStatus())) {
            query.setParameter("reviewStatus", dto.getReviewStatus());
            countQuery.setParameter("reviewStatus", dto.getReviewStatus());
        }

        if (dto.getProjectName() != null && !"".equals(dto.getProjectName())) {
            query.setParameter("projectName", dto.getProjectName());
            countQuery.setParameter("projectName", dto.getProjectName());
        }

        if (dto.getCompany() != null && !"".equals(dto.getCompany())) {
            query.setParameter("company", dto.getCompany());
            countQuery.setParameter("company", dto.getCompany());
        }

        if (dto.getDepartment() != null && !"".equals(dto.getDepartment())) {
            query.setParameter("department", dto.getDepartment());
            countQuery.setParameter("department", dto.getDepartment());
        }

        if (dto.getDivision() != null && !"".equals(dto.getDivision())) {
            query.setParameter("division", dto.getDivision());
            countQuery.setParameter("division", dto.getDivision());
        }

        if (dto.getTeam() != null && !"".equals(dto.getTeam())) {
            query.setParameter("team", dto.getTeam());
            countQuery.setParameter("team", dto.getTeam());
        }

        if (dto.getTask() != null && !"".equals(dto.getTask())) {
            query.setParameter("task", "%" + dto.getTask() + "%");
            countQuery.setParameter("task", "%" + dto.getTask() + "%");
        }

        if (dto.getStartDateTime() != null) {
            GregorianCalendar dateFrom = new GregorianCalendar();
            dateFrom.setTime(dto.getStartDateTime());
            dateFrom.set(GregorianCalendar.HOUR_OF_DAY, 0);
            dateFrom.set(GregorianCalendar.MINUTE, 0);
            dateFrom.set(GregorianCalendar.SECOND, 0);
            query.setParameter("startDateFrom", dateFrom.getTime());
            countQuery.setParameter("startDateFrom", dateFrom.getTime());
        }

        if (dto.getEndDateTime() != null) {
            GregorianCalendar dateFrom = new GregorianCalendar();
            dateFrom.setTime(dto.getEndDateTime());
            dateFrom.set(GregorianCalendar.HOUR_OF_DAY, 0);
            dateFrom.set(GregorianCalendar.MINUTE, 0);
            dateFrom.set(GregorianCalendar.SECOND, 0);
            query.setParameter("endDateFrom", dateFrom.getTime());
            countQuery.setParameter("endDateFrom", dateFrom.getTime());
        }

        // 查询结果
        int pageNo = dto.getPage().getNo();
        int pageSize = dto.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<OverTimeApplicationForm> overTimeApplicationForms = new ArrayList<OverTimeApplicationForm>();
        for (Map<String, Object> resultMap : queryResultList) {
            OverTimeApplicationForm overTimeApplicationForm = new OverTimeApplicationForm();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

            if (newMap.get("status") != null) {
                newMap.put("status", EntityStatus.valueOf((String) newMap.get("status")));
            }
            if (newMap.get("applyStatus") != null) {
                newMap.put("applyStatus", ApplyStatus.valueOf((String) newMap.get("applyStatus")));
            }
            if (newMap.get("reviewStatus") != null) {
                newMap.put("reviewStatus", ApplyStatus.valueOf((String) newMap.get("reviewStatus")));
            }
            BeanUtils.copyProperties(newMap, overTimeApplicationForm);


            overTimeApplicationForms.add(overTimeApplicationForm);

        }
        return new PageImpl<>(overTimeApplicationForms, dto.toPageable(), count.longValue());
    }

    @Override
    public Page<OverTimeApplicationForm> searchAll(
        Long operatorId,
        OverTimeApplicationFormSearchDTO dto) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ha.* ");
        sql.append(" from overtime_apply_form ha ");
        sql.append(" where ha.deleted is false ");

        if (dto.getKeyword() != null && !"".equals(dto.getKeyword())) {
            sql.append(" and ha.employee_id like :employeeId ");
        }

        if (dto.getProjectName() != null && !"".equals(dto.getProjectName())) {
            sql.append(" and ha.project_name = :projectName ");
        }

        if (dto.getCompany() != null && !"".equals(dto.getCompany())) {
            sql.append(" and ha.company = :company ");
        }

        if (dto.getDepartment() != null && !"".equals(dto.getDepartment())) {
            sql.append(" and ha.department = :department ");
        }

        if (dto.getDivision() != null && !"".equals(dto.getDivision())) {
            sql.append(" and ha.division = :division ");
        }

        if (dto.getTeam() != null && !"".equals(dto.getTeam())) {
            sql.append(" and ha.team = :team ");
        }

        if (dto.getUserType() != null && !dto.getUserType().equals("administrator")) {
            if (dto.getCompany() != null && !"".equals(dto.getCompany())) {
                sql.append(" and ha.company = :company ");
            } else if (dto.getDivision() != null && !"".equals(dto.getDivision())) {
                sql.append(" and ha.division = :division ");
            } else if (dto.getTeam() != null && !"".equals(dto.getTeam())) {
                sql.append(" and ha.team = :team ");
            }
        }


        if (dto.getTask() != null && !"".equals(dto.getTask())) {
            sql.append(" and ha.task like :task ");
        }

        if (dto.getApplyStatus() != null) {
            sql.append(" and ha.apply_status = :applyStatus ");
        }

        if (dto.getReviewStatus() != null) {
            sql.append(" and ha.review_status = :reviewStatus ");
        }

        if (dto.getStartDateTime() != null) {
            sql.append(" and ha.start_date >= :startDateFrom ");
        }

        if (dto.getEndDateTime() != null) {
            sql.append(" and ha.start_date <= :endDateFrom ");
        }

        sql.append(" ORDER BY ha.created_at desc ");
        sql.append(" LIMIT :start , :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (dto.getKeyword() != null && !"".equals(dto.getKeyword())) {
            query.setParameter("employeeId", "%" + dto.getKeyword() + "%");
            countQuery.setParameter("employeeId", "%" + dto.getKeyword() + "%");
        }

        if (dto.getApplyStatus() != null && !"".equals(dto.getApplyStatus())) {
            query.setParameter("applyStatus", dto.getApplyStatus());
            countQuery.setParameter("applyStatus", dto.getApplyStatus());
        }

        if (dto.getReviewStatus() != null && !"".equals(dto.getReviewStatus())) {
            query.setParameter("reviewStatus", dto.getReviewStatus());
            countQuery.setParameter("reviewStatus", dto.getReviewStatus());
        }

        if (dto.getProjectName() != null && !"".equals(dto.getProjectName())) {
            query.setParameter("projectName", dto.getProjectName());
            countQuery.setParameter("projectName", dto.getProjectName());
        }

        if (dto.getCompany() != null && !"".equals(dto.getCompany())) {
            query.setParameter("company", dto.getCompany());
            countQuery.setParameter("company", dto.getCompany());
        }

        if (dto.getDepartment() != null && !"".equals(dto.getDepartment())) {
            query.setParameter("department", dto.getDepartment());
            countQuery.setParameter("department", dto.getDepartment());
        }

        if (dto.getDivision() != null && !"".equals(dto.getDivision())) {
            query.setParameter("division", dto.getDivision());
            countQuery.setParameter("division", dto.getDivision());
        }

        if (dto.getTeam() != null && !"".equals(dto.getTeam())) {
            query.setParameter("team", dto.getTeam());
            countQuery.setParameter("team", dto.getTeam());
        }

        if (dto.getUserType() != null && !dto.getUserType().equals("administrator")) {
            if (dto.getCompany() != null && !"".equals(dto.getCompany())) {
                query.setParameter("company", dto.getCompany());
                countQuery.setParameter("company", dto.getCompany());
            } else if (dto.getDivision() != null && !"".equals(dto.getDivision())) {
                query.setParameter("division", dto.getDivision());
                countQuery.setParameter("division", dto.getDivision());
            } else if (dto.getTeam() != null && !"".equals(dto.getTeam())) {
                query.setParameter("team", dto.getTeam());
                countQuery.setParameter("team", dto.getTeam());
            }
        }


        if (dto.getTask() != null && !"".equals(dto.getTask())) {
            query.setParameter("task", "%" + dto.getTask() + "%");
            countQuery.setParameter("task", "%" + dto.getTask() + "%");
        }

        if (dto.getStartDateTime() != null) {
            GregorianCalendar dateFrom = new GregorianCalendar();
            dateFrom.setTime(dto.getStartDateTime());
            dateFrom.set(GregorianCalendar.HOUR_OF_DAY, 0);
            dateFrom.set(GregorianCalendar.MINUTE, 0);
            dateFrom.set(GregorianCalendar.SECOND, 0);
            query.setParameter("startDateFrom", dateFrom.getTime());
            countQuery.setParameter("startDateFrom", dateFrom.getTime());
        }

        if (dto.getEndDateTime() != null) {
            GregorianCalendar dateFrom = new GregorianCalendar();
            dateFrom.setTime(dto.getEndDateTime());
            dateFrom.set(GregorianCalendar.HOUR_OF_DAY, 0);
            dateFrom.set(GregorianCalendar.MINUTE, 0);
            dateFrom.set(GregorianCalendar.SECOND, 0);
            query.setParameter("endDateFrom", dateFrom.getTime());
            countQuery.setParameter("endDateFrom", dateFrom.getTime());
        }

        // 查询结果
        int pageNo = dto.getPage().getNo();
        int pageSize = dto.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<OverTimeApplicationForm> overTimeApplicationForms = new ArrayList<OverTimeApplicationForm>();
        for (Map<String, Object> resultMap : queryResultList) {
            OverTimeApplicationForm overTimeApplicationForm = new OverTimeApplicationForm();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

            if (newMap.get("status") != null) {
                newMap.put("status", EntityStatus.valueOf((String) newMap.get("status")));
            }
            if (newMap.get("applyStatus") != null) {
                newMap.put("applyStatus", ApplyStatus.valueOf((String) newMap.get("applyStatus")));
            }
            if (newMap.get("reviewStatus") != null) {
                newMap.put("reviewStatus", ApplyStatus.valueOf((String) newMap.get("reviewStatus")));
            }
            BeanUtils.copyProperties(newMap, overTimeApplicationForm);


            overTimeApplicationForms.add(overTimeApplicationForm);

        }
        return new PageImpl<>(overTimeApplicationForms, dto.toPageable(), count.longValue());
    }

    @Override
    public Page<PerformanceEvaluationDataDTO> searchEvaluation(
        Long operatorId,
        PerformanceEvaluationSearchDTO dto,
        Map<String, Integer> companyToNormalDays,
        Map<String, List<String>> companyToHolidates) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT " +
            "u.id AS engineerId, " +
            "u.username AS employeeId, " +
            "u.NAME AS displayName, " +
            "u.company AS company, " +
            "u.division AS division, " +
            "u.team AS team, " +
            "u.title AS title, " +
            "COUNT(DISTINCT CASE WHEN dr.project_name NOT IN ( 'Overhead', 'Leave', 'IT', 'HR', 'General_Training', 'General_Study', 'General_Activity', 'Discipline', 'CPY_Database', 'Commercial', 'Admin' ) THEN " +
            "dr.project_name ELSE NULL END) AS projectQty, " +
            "GROUP_CONCAT(DISTINCT CAST(dr.project_name as CHAR) SEPARATOR ', ') AS projectList, " +
            "SUM( CASE WHEN (dr.project_name != 'Leave') THEN IFNULL(dr.work_hour,0) ELSE 0 END) AS totalNormalManHour, " +
            "SUM(IFNULL(dr.out_hour,0)) AS totalOvertime, " +
            "SUM( CASE WHEN " +
            "dr.project_name NOT IN('Overhead','Leave','IT','HR','General_Training','General_Study','General_Activity','Discipline','CPY_Database','Commercial','Admin') " +
            "THEN IFNULL(dr.work_hour,0) + IFNULL(dr.out_hour,0) ELSE 0 END ) AS projectManhour, " +
            "SUM( CASE WHEN " +
            "dr.project_name IN('Overhead','IT','HR','General_Training','General_Study','General_Activity','Discipline','CPY_Database','Commercial','Admin') " +
            "THEN IFNULL(dr.work_hour,0) + IFNULL(dr.out_hour,0) ELSE 0 END ) AS nonProjectHour, " +
            "SUM( CASE WHEN " +
            "(dr.project_name NOT IN('Overhead','Leave','IT','HR','General_Training','General_Study','General_Activity','Discipline','CPY_Database','Commercial','Admin') AND dr.task IN('Training','Study')) " +
            "THEN IFNULL(dr.work_hour,0) + IFNULL(dr.out_hour,0) ELSE 0 END) AS generalStudyHour, " +
            "SUM( CASE WHEN " +
            "dr.project_name IN('General_Training','General_Study') " +
            "THEN IFNULL(dr.work_hour,0) + IFNULL(dr.out_hour,0) ELSE 0 END) AS projectStudyHour " +
            "FROM " +
            "drawing_record dr " +
            "INNER JOIN saint_whale_auth.users u ON dr.engineer_id = u.id  " +
            "WHERE  " +
            "dr.deleted IS FALSE  " +
            "AND u.username NOT IN (  'admin', 'adminhr', 'super', 'testnew', 'NT_101', 'SG_001', 'SG_002', 'testvp' )  " +
            "AND u.STATUS in ('ACTIVE','DISABLED') " +
            "AND dr.work_hour_date BETWEEN :startDate  AND :endDate ");

        //如果当前用户是管理员则全显示，否则只显示对应权限下的内容
        //如果三个权限都具有
        if (dto.getTeamLeader() != null && !"".equals(dto.getTeamLeader())
            && dto.getDivisionVP() != null && !"".equals(dto.getDivisionVP())
            && dto.getCompanyGM() != null && !"".equals(dto.getCompanyGM())) {
            sql.append("AND (u.company = :companyGM or u.division = :divisionVP or u.team = :teamLeader )  ");
        } else if (
            dto.getDivisionVP() != null && !"".equals(dto.getDivisionVP())
                && dto.getCompanyGM() != null && !"".equals(dto.getCompanyGM())) {
            //如果只有companyGM和divisionVP
            sql.append("AND (u.company = :companyGM or u.division = :divisionVP  )  ");
        } else if (
            dto.getTeamLeader() != null && !"".equals(dto.getTeamLeader())
                && dto.getCompanyGM() != null && !"".equals(dto.getCompanyGM())) {
            //如果只有companyGM和teamLeader
            sql.append("AND (u.company = :companyGM or u.team = :teamLeader  )  ");
        } else if (
            dto.getTeamLeader() != null && !"".equals(dto.getTeamLeader())
                && dto.getDivisionVP() != null && !"".equals(dto.getDivisionVP())) {
            //如果只有divisionVP和teamLeader
            sql.append("AND (u.division = :divisionVP or u.team = :teamLeader  )  ");
        } else if (dto.getTeamLeader() != null && !"".equals(dto.getTeamLeader())) {
            sql.append("AND u.team = :teamLeader  ");
        } else if (dto.getDivisionVP() != null && !"".equals(dto.getDivisionVP())) {
            sql.append("AND u.division = :divisionVP  ");
        } else if (dto.getCompanyGM() != null && !"".equals(dto.getCompanyGM())) {
            sql.append("AND u.company = :companyGM   ");
        }

        if (dto.getKeyword() != null && !"".equals(dto.getKeyword())) {
            sql.append("AND (u.username like :employeeId or u.name like :displayName )  ");
        }

        if (dto.getProjectName() != null && !"".equals(dto.getProjectName())) {
            sql.append("AND dr.project_name like :projectName  ");
        }

        if (dto.getReviewCompany() != null && !"".equals(dto.getReviewCompany()) && dto.getReviewCompany2() == null) {
            sql.append("AND u.company = :reviewCompany  ");
        }

        if (dto.getReviewCompany2() != null && !"".equals(dto.getReviewCompany2()) && dto.getReviewCompany() != null && !"".equals(dto.getReviewCompany())) {
            sql.append("AND u.company in( :reviewCompany ,  :reviewCompany2 ) ");
        }

        if (dto.getCompany() != null && !"".equals(dto.getCompany())) {
            sql.append("AND u.company = :company  ");
        }

        if (dto.getDivision() != null && !"".equals(dto.getDivision())) {
            sql.append("AND u.division = :division  ");
        }

        if (dto.getTeam() != null && !"".equals(dto.getTeam())) {
            sql.append("AND u.team = :team  ");
        }

        if (dto.getTitle() != null && !"".equals(dto.getTitle())) {
            sql.append("AND u.title = :title  ");
        }


        sql.append(" GROUP BY u.id ");
        if (dto.getSort() != null) {
            sql.append(" ORDER BY ").append(dto.getSort()[0].replaceAll(":", " "));
        }
        sql.append(" LIMIT :start , :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);


        query.setParameter("startDate", dto.getStartDate());
        countQuery.setParameter("startDate", dto.getStartDate());

        query.setParameter("endDate", dto.getEndDate());
        countQuery.setParameter("endDate", dto.getEndDate());

        if (dto.getKeyword() != null && !"".equals(dto.getKeyword())) {
            query.setParameter("employeeId", "%" + dto.getKeyword() + "%");
            countQuery.setParameter("employeeId", "%" + dto.getKeyword() + "%");
            query.setParameter("displayName", "%" + dto.getKeyword() + "%");
            countQuery.setParameter("displayName", "%" + dto.getKeyword() + "%");
        }

        if (dto.getProjectName() != null && !"".equals(dto.getProjectName())) {
            query.setParameter("projectName", "%" + dto.getProjectName() + "%");
            countQuery.setParameter("projectName", "%" + dto.getProjectName() + "%");
        }

        if (dto.getReviewCompany() != null && !"".equals(dto.getReviewCompany()) && dto.getReviewCompany2() == null) {
            query.setParameter("reviewCompany", dto.getReviewCompany());
            countQuery.setParameter("reviewCompany", dto.getReviewCompany());
        }

        if (dto.getReviewCompany2() != null && !"".equals(dto.getReviewCompany2()) && dto.getReviewCompany() != null && !"".equals(dto.getReviewCompany())) {
            query.setParameter("reviewCompany", dto.getReviewCompany());
            countQuery.setParameter("reviewCompany", dto.getReviewCompany());
            query.setParameter("reviewCompany2", dto.getReviewCompany2());
            countQuery.setParameter("reviewCompany2", dto.getReviewCompany2());
        }

        if (dto.getCompany() != null && !"".equals(dto.getCompany())) {
            query.setParameter("company", dto.getCompany());
            countQuery.setParameter("company", dto.getCompany());
        }

        if (dto.getTitle() != null && !"".equals(dto.getTitle())) {
            query.setParameter("title", dto.getTitle());
            countQuery.setParameter("title", dto.getTitle());
        }

        if (dto.getDivision() != null && !"".equals(dto.getDivision())) {
            query.setParameter("division", dto.getDivision());
            countQuery.setParameter("division", dto.getDivision());
        }

        if (dto.getTeam() != null && !"".equals(dto.getTeam())) {
            query.setParameter("team", dto.getTeam());
            countQuery.setParameter("team", dto.getTeam());
        }

        //如果三个权限都具有
        if (dto.getTeamLeader() != null && !"".equals(dto.getTeamLeader())
            && dto.getDivisionVP() != null && !"".equals(dto.getDivisionVP())
            && dto.getCompanyGM() != null && !"".equals(dto.getCompanyGM())) {
            query.setParameter("companyGM", dto.getCompanyGM());
            countQuery.setParameter("companyGM", dto.getCompanyGM());
            query.setParameter("divisionVP", dto.getDivisionVP());
            countQuery.setParameter("divisionVP", dto.getDivisionVP());
            query.setParameter("teamLeader", dto.getTeamLeader());
            countQuery.setParameter("teamLeader", dto.getTeamLeader());
        } else if (
            dto.getDivisionVP() != null && !"".equals(dto.getDivisionVP())
                && dto.getCompanyGM() != null && !"".equals(dto.getCompanyGM())) {
            //如果只有companyGM和divisionVP
            query.setParameter("companyGM", dto.getCompanyGM());
            countQuery.setParameter("companyGM", dto.getCompanyGM());
            query.setParameter("divisionVP", dto.getDivisionVP());
            countQuery.setParameter("divisionVP", dto.getDivisionVP());
        } else if (
            dto.getTeamLeader() != null && !"".equals(dto.getTeamLeader())
                && dto.getCompanyGM() != null && !"".equals(dto.getCompanyGM())) {
            //如果只有companyGM和teamLeader
            query.setParameter("companyGM", dto.getCompanyGM());
            countQuery.setParameter("companyGM", dto.getCompanyGM());
            ;
            query.setParameter("teamLeader", dto.getTeamLeader());
            countQuery.setParameter("teamLeader", dto.getTeamLeader());
        } else if (
            dto.getTeamLeader() != null && !"".equals(dto.getTeamLeader())
                && dto.getDivisionVP() != null && !"".equals(dto.getDivisionVP())) {
            //如果只有divisionVP和teamLeader
            query.setParameter("divisionVP", dto.getDivisionVP());
            countQuery.setParameter("divisionVP", dto.getDivisionVP());
            query.setParameter("teamLeader", dto.getTeamLeader());
            countQuery.setParameter("teamLeader", dto.getTeamLeader());
        } else if (dto.getTeamLeader() != null && !"".equals(dto.getTeamLeader())) {
            query.setParameter("teamLeader", dto.getTeamLeader());
            countQuery.setParameter("teamLeader", dto.getTeamLeader());
            ;
        } else if (dto.getDivisionVP() != null && !"".equals(dto.getDivisionVP())) {
            query.setParameter("divisionVP", dto.getDivisionVP());
            countQuery.setParameter("divisionVP", dto.getDivisionVP());
        } else if (dto.getCompanyGM() != null && !"".equals(dto.getCompanyGM())) {
            query.setParameter("companyGM", dto.getCompanyGM());
            countQuery.setParameter("companyGM", dto.getCompanyGM());
        }


        // 查询结果
        int pageNo = dto.getPage().getNo();
        int pageSize = dto.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<PerformanceEvaluationDataDTO> performanceEvaluationDataDTOs = new ArrayList<PerformanceEvaluationDataDTO>();
        for (Map<String, Object> resultMap : queryResultList) {
            PerformanceEvaluationDataDTO performanceEvaluationDataDTO = new PerformanceEvaluationDataDTO();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);
            BeanUtils.copyProperties(newMap, performanceEvaluationDataDTO);
            Integer normalDays = companyToNormalDays.get(performanceEvaluationDataDTO.getCompany());
            if (normalDays != null) {
                performanceEvaluationDataDTO.setStandardHour(normalDays * 8.0);
            }else {
                throw new BusinessError(" employee "+ performanceEvaluationDataDTO.getEmployeeId() +" has set up an exception");
            }
            performanceEvaluationDataDTOs.add(performanceEvaluationDataDTO);
        }
        return new PageImpl<>(performanceEvaluationDataDTOs, dto.toPageable(), count.longValue());
    }

}
