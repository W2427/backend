package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DrawingRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author: DaiZeFeng
 * @date: 2023/2/20
 */
@Transactional
public interface DrawingRecordRepository extends PagingAndSortingWithCrudRepository<DrawingRecord, Long>, DrawingRecordRepositoryCustom {

    List<DrawingRecord> findAllByOrgIdAndProjectIdAndDrawingIdAndDeletedIsFalse(Long orgId, Long projectId, Long DrawingId);

    List<DrawingRecord> findAllByEngineerIdAndWorkHourDateAndDeletedIsFalse(Long engineerId,String workHourDate);
    List<DrawingRecord> findAllByDeletedIsFalse();

    List<DrawingRecord> findAllByEngineerIdAndDrawingIdAndDeletedIsFalse(Long engineerId,Long drawingId);

    DrawingRecord findByEngineerIdAndWorkHourDateAndTaskAndDeletedIsFalse(Long engineerId, String workHourDate, String task);

    DrawingRecord findByEngineerIdAndWorkHourDateAndProjectNameAndTaskAndDeletedIsFalse(
        Long engineerId,
        String workHourDate,
        String projectName,
        String task);

    @Query(nativeQuery = true,
        value = "SELECT " +
            "dr.* , " +
            "u.username, " +
            "u.company, " +
            "u.division, " +
            "u.department " +
            "FROM " +
            "saint_whale_tasks.drawing_record dr " +
            "LEFT JOIN saint_whale_auth.users u ON u.id = dr.engineer_id " +
            "WHERE " +
            "dr.deleted is FALSE " +
            "AND dr.work_hour_date IN :workHourDates")
    List<Map<String, Object>> drawingRecordsByWorkHourDates(
        @Param("workHourDates") List<String> workHourDates
    );

    @Query(nativeQuery = true,
        value = "SELECT " +
            "u.username, " +
            "u.name, " +
            "dr.work_hour_date, " +
            "dr.work_hour, " +
            "dr.project_name, " +
            "dr.task " +
            "FROM " +
            "saint_whale_tasks.drawing_record dr " +
            "LEFT JOIN saint_whale_auth.users u ON u.id = dr.engineer_id " +
            "WHERE " +
            "dr.deleted is FALSE " +
            "AND dr.project_name = 'Leave' " +
            "AND dr.task != 'Holiday' " +
            "AND dr.work_hour_date between :startDate and :endDate " +
            "AND u.username =:jobNumber")
    List<Map<String, Object>> drawingRecordsByLeave(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("jobNumber") String jobNumber
    );

    @Query(nativeQuery = true,
        value = "select SUM( CASE WHEN ( dr.project_name != 'Leave' ) THEN IFNULL( dr.work_hour, 0 ) ELSE 0 END ) AS totalNormalManHour " +
            " FROM " +
            " saint_whale_tasks.drawing_record dr " +
            " WHERE " +
            " dr.work_hour_date between :startDate and :endDate " +
            " AND dr.deleted IS FALSE " +
            " AND dr.engineer_id =:engineerId ")
    Double searchWorkHoursByJobNumberAndDateRange(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("engineerId") String engineerId
    );

    @Query(nativeQuery = true,
        value = "SELECT " +
            " u.username AS username, " +
            " u.company AS company, " +
            " u.division AS division, " +
            " u.department AS department, " +
            " 'Labor hours not filled in' AS work_hour_date  " +
            "FROM " +
            " saint_whale_auth.users u  " +
            "WHERE " +
            " u.deleted IS FALSE  " +
            " AND u.disabled IS FALSE  " +
            " AND u.id NOT IN " +
            "( SELECT dr.engineer_id FROM saint_whale_tasks.drawing_record dr WHERE dr.deleted IS FALSE AND dr.work_hour_date IN :workHourDates ) " +
            " and u.username not in ('NT_101','SG_001','SG_002','super','testvp','admin','testnew')")
    List<Map<String, Object>> drawingRecordsByUnHourUsers(
        @Param("workHourDates") List<String> workHourDates
    );

    @Query(nativeQuery = true,
        value = "select dr.* from drawing_record dr where " +
            " dr.engineer_id=:engineerId and dr.work_hour_date=:workHourDate and dr.deleted = 0")
    List<Map<String, Object>> drawingRecords(
        @Param("engineerId") Long engineerId,
        @Param("workHourDate") String workHourDate
    );

    @Query(nativeQuery = true,
        value = "select dr.* from drawing_record dr where " +
            " dr.engineer_id=:engineerId and dr.work_hour_date=:workHourDate and dr.discipline =:discipline and dr.deleted = 0")
    List<Map<String, Object>> drawingRecordsByDiscipline(
        @Param("engineerId") Long engineerId,
        @Param("workHourDate") String workHourDate,
        @Param("discipline") String discipline
    );

    @Query(nativeQuery = true,
        value = "select dr.* from drawing_record dr where " +
            " dr.engineer_id=:engineerId and dr.work_hour_date IN:workHourDates  and dr.deleted = 0")
    List<Map<String, Object>> drawingRecordsForDownload(
        @Param("engineerId") Long engineerId,
        @Param("workHourDates") List<String> workHourDates
    );
    @Query(nativeQuery = true,
        value = "SELECT DISTINCT LEFT(weekly,4) AS year " +
            "FROM drawing_record WHERE weekly IS NOT NULL ORDER BY year")
    List<String> findYears();
    @Query(nativeQuery = true,
        value = "SELECT DISTINCT RIGHT(weekly,2) AS week " +
            "FROM drawing_record WHERE weekly IS NOT NULL ORDER BY week")
    List<String> findWeeks();

    @Query(nativeQuery = true,
        value = "select sum(work_hour) from drawing_record dr where " +
            " dr.engineer_id=:engineerId and dr.weekly =:weekly  and dr.deleted = 0")
    Double findByEngineerIdAndWeeklyAndDeletedIsFalse(Long engineerId, Integer weekly);

    @Query(nativeQuery = true,
        value = "select sum(work_hour) from drawing_record dr where " +
            " dr.engineer_id=:engineerId and dr.work_hour_date =:workHourDate  and dr.deleted = 0")
    Double findSumWorkHourByEngineerIdAndWorkHourDateAndDeletedIsFalse(Long engineerId, String workHourDate);

    @Query(nativeQuery = true,
        value = "select sum(out_hour) from drawing_record dr where " +
            " dr.engineer_id=:engineerId and dr.work_hour_date =:workHourDate  and dr.deleted = 0")
    Double findSumOutHourByEngineerIdAndWorkHourDateAndDeletedIsFalse(Long engineerId, String workHourDate);

    @Query(nativeQuery = true,
        value = "select sum(work_hour) as work_hour, dr.work_hour_date from drawing_record dr where " +
            " dr.engineer_id=:engineerId and dr.weekly =:weekly  and dr.deleted = 0 " +
            " group by dr.work_hour_date")
    List<Map<String, Object>> findWorkHourDateByEngineerIdAndWeeklyAndDeletedIsFalse(Long engineerId, Integer weekly);


    List<DrawingRecord> findByOrgIdAndProjectIdAndDrawingNoAndDeletedIsFalse(Long orgId, Long projectId, String drawingNo);

//    @Query(nativeQuery = true,
//        value = "select nullif(sum(work_hour), 0) + nullif(sum(out_hour), 0) from drawing_record dr where " +
//            " dr.engineer =:engineer and dr.work_hour_date =:workHourDate  and dr.deleted = 0")
//    Integer findByEngineerAndWorkHourDate(String engineer, String workHourDate);

    @Query(nativeQuery = true,
        value = "select distinct u.company from saint_whale_auth.users u where " +
            " u.status = 'ACTIVE' and u.company is not null and u.company <> '' order by u.company")
    List<String> findByCompany();

    @Query(nativeQuery = true,
        value = "select distinct u.division from saint_whale_auth.users u where " +
            " u.status = 'ACTIVE' and u.division is not null and u.division <> '' order by u.division")
    List<String> findByDivision();

    @Query(nativeQuery = true,
        value = "select distinct u.department from saint_whale_auth.users u where " +
            " u.status = 'ACTIVE' and u.department is not null and u.department <> '' order by u.department")
    List<String> findByDepartment();

    @Query(nativeQuery = true,
        value = "select distinct u.team from saint_whale_auth.users u where " +
            " u.status = 'ACTIVE' and u.team is not null and u.team <> '' order by u.team")
    List<String> findByTeam();

    @Query(nativeQuery = true,
        value = "select distinct u.title from saint_whale_auth.users u where " +
            " u.status = 'ACTIVE' and u.title is not null and u.title <> '' order by u.title")
    List<String> findByTitle();

    @Query(nativeQuery = true,
        value = "select distinct u.weekly from drawing_record u where " +
            " u.status = 'ACTIVE' and u.weekly is not null and u.weekly <> '' and u.work_hour_date between :startDate and :endDate order by u.weekly")
    List<Integer> findByWeekly(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );



    @Query(nativeQuery = true,
        value = "select dr.* from drawing_record dr where " +
            " dr.project_id = :projectId and dr.engineer_id =:engineerId and " +
            " dr.work_hour_date =:workHourDate and dr.out_hour > 0 and dr.deleted = 0")
    List<DrawingRecord> findByProjectIdAndEngineerIdAndWorkHourDateAndOutHour(Long projectId, Long engineerId, String workHourDate);


    @Query(nativeQuery = true,
        value = "select sum(out_hour) from drawing_record dr where " +
            " dr.engineer_id=:engineerId and dr.work_hour_date =:workHourDate and dr.project_name = :projectName and dr.task = :task and dr.deleted = 0")
    Double findSumOverHourByEngineerIdAndWorkHourDateAndProjectNameAndTaskAndDeletedIsFalse(
        Long engineerId,
        String workHourDate,
        String projectName,
        String task);

    @Query(nativeQuery = true,
        value = "select DISTINCT org_id from drawing_record  where project_id = :projectId")
    Long findOrgIdByProjectId(Long projectId);

    @Query(nativeQuery = true,
        value = "select DISTINCT weekly from drawing_record  where work_hour_date = :date")
    Integer findWeeklyIdByDate(String date);


    @Query(nativeQuery = true,
        value = "select  id from saint_whale_auth.users  where username = :name and deleted is false")
    Long findEmployeeIdByName(String name);

    @Query(nativeQuery = true,
        value = "select  * from drawing_record  where work_hour_date BETWEEN '2023-10-01' AND '2023-10-27'")
    List<DrawingRecord> findNeedFixedRecord();

}
