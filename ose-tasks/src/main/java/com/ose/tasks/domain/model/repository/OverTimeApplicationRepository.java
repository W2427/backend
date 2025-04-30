package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.OverTimeApplicationForm;
import com.ose.tasks.vo.ApplyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface OverTimeApplicationRepository extends PagingAndSortingWithCrudRepository<OverTimeApplicationForm, Long>, OverTimeApplicationRepositoryCustom {

    Page<OverTimeApplicationForm> findByEmployeeIdAndDeletedIsFalse(String name, Pageable pageable);

//    boolean existsByProjectIdAndTaskAndDeletedIsFalse(Long projectId, String task);

    Optional<OverTimeApplicationForm> findByIdAndDeletedIsFalse(Long id);

    List<OverTimeApplicationForm> findByUserIdAndProjectIdAndApplyStatusNotAndDeletedIsFalse(Long userId, Long projectId, ApplyStatus applyStatus);

    // 涉及到auth服务的组织架构
    @Query(value = "SELECT ob.id FROM saint_whale_auth.organizations ob WHERE ob.name = :name AND ob.depth = 1 AND ob.deleted = FALSE ",
        nativeQuery = true)
    Long findOrgIdByName(String name);


    @Query(value = "SELECT ob.id FROM saint_whale_auth.organizations ob LEFT JOIN saint_whale_auth.user_organization_relations uo ON uo.organization_id = ob.id " +
        "WHERE ob.path LIKE :orgId AND ob.type = 'PROJECT' AND uo.user_id = :userId AND uo.deleted = FALSE ORDER BY ob.depth DESC",
        nativeQuery = true)
    List<BigInteger> findByOrgIdAndUserId(String orgId, Long userId);


    @Query(value = "SELECT u.id FROM saint_whale_auth.users u WHERE u.company = :company AND u.review_role = true AND u.deleted = FALSE ",
        nativeQuery = true)
    List<Long> findUserIdByCompany(String company);


    @Query(value = "SELECT u.id FROM saint_whale_auth.users u WHERE u.review_other_company like :company AND u.review_role = true AND u.deleted = FALSE ",
        nativeQuery = true)
    List<Long> findUserIdByReviewCompany(String company);

    @Query(value = "SELECT u.id FROM saint_whale_auth.users u WHERE u.company IN :companys AND u.review_role = true AND u.deleted = FALSE ",
        nativeQuery = true)
    List<Long> findUserIdByCompanyIn(List<String> companys);

    @Query(value = "SELECT u.user_id FROM saint_whale_auth.user_organization_relations u " +
        "WHERE u.organization_id = :orgId AND u.apply_role = true AND u.user_id != :userId AND u.deleted = FALSE ",
        nativeQuery = true)
    Long findUserIdByOrgId(Long orgId, Long userId);

    @Query(value = "SELECT s.name FROM saint_whale_auth.user_organization_relations u " +
        "LEFT JOIN saint_whale_auth.users s ON u.user_id = s.id " +
        "WHERE u.organization_id = :orgId AND u.apply_role = true AND u.user_id != :userId AND u.deleted = FALSE AND s.deleted = FALSE ",
        nativeQuery = true)
    String findUserNameByOrgId(Long orgId, Long userId);

    @Query(nativeQuery = true,
        value = "select distinct o.company from overtime_apply_form o where " +
            " o.deleted is false and o.company is not null and o.company <> '' order by o.company")
    List<String> findByCompany();

    @Query(nativeQuery = true,
        value = "select distinct o.team from overtime_apply_form o where " +
            " o.deleted is false and o.team is not null and o.team <> '' order by o.team")
    List<String> findByTeam();

    @Query(nativeQuery = true,
        value = "select distinct o.department from overtime_apply_form o where " +
            " o.deleted is false and o.department is not null and o.department <> '' order by o.department")
    List<String> findByDepartment();

    @Query(nativeQuery = true,
        value = "select distinct o.division from overtime_apply_form o where " +
            " o.deleted is false and o.division is not null and o.division <> '' order by o.division")
    List<String> findByDivision();

    @Query(nativeQuery = true,
        value = "select distinct o.project_name from overtime_apply_form o where " +
            " o.deleted is false and o.project_name is not null and o.project_name <> '' order by o.project_name")
    List<String> findByProjectName();


    @Query(value = "SELECT u.id FROM saint_whale_auth.users u " +
        "WHERE u.deleted = FALSE AND u.team = :team AND u.team_leader is true AND u.id != :userId ",
        nativeQuery = true)
    List<Long> findUserIdByTeam(String team, Long userId);


    @Query(value = "SELECT t.parent_team_name FROM saint_whale_auth.team t " +
        "WHERE t.deleted = FALSE AND t.name = :team ",
        nativeQuery = true)
    String findParentTeamByTeam(String team);


    @Query(value = "SELECT u.name FROM saint_whale_auth.users u " +
        "WHERE u.deleted = FALSE AND u.team = :team AND u.team_leader is true AND u.id != :userId ",
        nativeQuery = true)
    List<String> findUserNameByTeam(String team, Long userId);

    @Query(value = "SELECT u.id FROM saint_whale_auth.users u " +
        "WHERE u.deleted = FALSE AND u.division = :division AND u.divisionvp is true AND u.id != :userId LIMIT 1 ",
        nativeQuery = true)
    Long findUserIdByDivision(String division, Long userId);

    @Query(value = "SELECT u.name FROM saint_whale_auth.users u " +
        "WHERE u.deleted = FALSE AND u.division = :division AND u.divisionvp is true AND u.id != :userId LIMIT 1 ",
        nativeQuery = true)
    String findUserNameByDivision(String division, Long userId);

    @Query(value = "SELECT u.id FROM saint_whale_auth.users u " +
        "WHERE u.deleted = FALSE AND u.company = :division AND u.companygm is true AND u.id != :userId LIMIT 1 ",
        nativeQuery = true)
    Long findUserIdByCompany(String division, Long userId);

    @Query(value = "SELECT u.name FROM saint_whale_auth.users u " +
        "WHERE u.deleted = FALSE AND u.company = :division AND u.companygm is true AND u.id != :userId LIMIT 1 ",
        nativeQuery = true)
    String findUserNameByCompany(String division, Long userId);


    @Query(value = "SELECT ot.man_hour FROM overtime_apply_form ot " +
        "WHERE ot.deleted = FALSE AND ot.user_id = :userId AND ot.project_name = :projectName AND ot.start_date between :startDate AND :endDate " +
        "AND ot.task like :task ",
        nativeQuery = true)
    Double findByUserIdAndProjectNameAndStartDateAndTask(
        Long userId,
        String projectName,
        Date startDate,
        Date endDate,
        String task
    );

    @Query(
        value = "SELECT " +
            "IFNULL(SUM( IFNULL(dr.work_hour,0) ) ,0)  " +
            "FROM " +
            "drawing_record dr " +
            "WHERE " +
            "dr.deleted IS FALSE  " +
            "AND dr.engineer_id = :engineerId " +
            "AND dr.work_hour_date BETWEEN :startDate  " +
            "AND :endDate  " +
            "AND dr.project_name = 'Leave' " +
            "AND dr.task = 'Holiday' ",
        nativeQuery = true
    )
    Double searchHolidayHour(
        @Param("engineerId") Long engineerId,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );

    @Query(
        value = "SELECT " +
            "IFNULL(SUM( IFNULL(dr.work_hour,0) ),0) " +
            "FROM " +
            "drawing_record dr " +
            "WHERE " +
            "dr.deleted IS FALSE  " +
            "AND dr.engineer_id = :engineerId " +
            "AND dr.work_hour_date BETWEEN :startDate  " +
            "AND :endDate  " +
            "AND dr.project_name = 'Leave' " +
            "AND dr.task != 'Holiday' ",
        nativeQuery = true
    )
    Double searchTotalLeave(
        @Param("engineerId") Long engineerId,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );

//    @Query(
//        value = "SELECT " +
//            "IFNULL(SUM( IFNULL(dr.work_hour,0) ),0) " +
//            "FROM " +
//            "drawing_record dr " +
//            "WHERE " +
//            "dr.deleted IS FALSE  " +
//            "AND dr.engineer_id = :engineerId " +
//            "AND dr.work_hour_date BETWEEN :startDate  " +
//            "AND :endDate  " +
//            "AND dr.project_name = 'Leave' ",
//        nativeQuery = true
//    )
//    Double searchTotalLeave(
//        @Param("engineerId") Long engineerId,
//        @Param("startDate") String startDate,
//        @Param("endDate") String endDate
//    );

    @Query(nativeQuery = true,
        value = "SELECT " +
            "oaf.company, " +
            "oaf.employee_id, " +
            "oaf.display_name, " +
            "oaf.company, " +
            "oaf.apply_status,  " +
            "oaf.start_date, " +
            "oaf.apply_role_name, " +
            "u.email  " +
            "FROM " +
            "saint_whale_tasks.overtime_apply_form oaf " +
            "LEFT JOIN " +
            "saint_whale_auth.users u ON oaf.apply_role_id = u.id " +
            "WHERE " +
            "oaf.deleted IS FALSE  " +
            "AND oaf.apply_status = 'UNAPPROVED' ")
    List<Map<String, Object>> findByApplyStatus();

}
