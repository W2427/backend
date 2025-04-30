package com.ose.auth.domain.model.repository;


import com.ose.auth.entity.DCalendar;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
public interface CalendarRepository extends PagingAndSortingWithCrudRepository<DCalendar, Long>, CalendarRepositoryCustom {


    DCalendar findByProjectId(Long projectId);
    List<DCalendar> findAll();
//    List<DrawingRecord> findAllByOrgIdAndProjectIdAndDrawingIdAndDeletedIsFalse(Long orgId, Long projectId, Long DrawingId);
//
//    List<DrawingRecord> findAllByEngineerIdAndWorkHourDateAndDeletedIsFalse(Long engineerId,String workHourDate);
//    List<DrawingRecord> findAllByDeletedIsFalse();
//    @Query(nativeQuery = true,
//        value = "SELECT " +
//            "dr.* , " +
//            "u.username, " +
//            "u.company, " +
//            "u.division, " +
//            "u.department " +
//            "FROM " +
//            "saint_whale_tasks.drawing_record dr " +
//            "LEFT JOIN saint_whale_auth.users u ON u.id = dr.engineer_id " +
//            "WHERE " +
//            "dr.deleted is FALSE " +
//            "AND dr.work_hour_date IN :workHourDates")
//    List<Map<String, Object>> drawingRecordsByWorkHourDates(
//        @Param("workHourDates") List<String> workHourDates
//    );
//
//    @Query(nativeQuery = true,
//        value = "SELECT " +
//            " u.username AS username, " +
//            " u.company AS company, " +
//            " u.division AS division, " +
//            " u.department AS department, " +
//            " 'Labor hours not filled in' AS work_hour_date  " +
//            "FROM " +
//            " saint_whale_auth.users u  " +
//            "WHERE " +
//            " u.deleted IS FALSE  " +
//            " AND u.disabled IS FALSE  " +
//            " AND u.id NOT IN " +
//            "( SELECT dr.engineer_id FROM saint_whale_tasks.drawing_record dr WHERE dr.deleted IS FALSE AND dr.work_hour_date IN :workHourDates ) " +
//            " and u.username not in ('NT_101','SG_001','SG_002','super','testvp','admin','testnew')")
//    List<Map<String, Object>> drawingRecordsByUnHourUsers(
//        @Param("workHourDates") List<String> workHourDates
//    );
//
//    @Query(nativeQuery = true,
//        value = "select dr.* from drawing_record dr where " +
//            " dr.engineer_id=:engineerId and dr.work_hour_date=:workHourDate and dr.deleted = 0")
//    List<Map<String, Object>> drawingRecords(
//        @Param("engineerId") Long engineerId,
//        @Param("workHourDate") String workHourDate
//    );
//
//    @Query(nativeQuery = true,
//        value = "select dr.* from drawing_record dr where " +
//            " dr.engineer_id=:engineerId and dr.work_hour_date=:workHourDate and dr.discipline =:discipline and dr.deleted = 0")
//    List<Map<String, Object>> drawingRecordsByDiscipline(
//        @Param("engineerId") Long engineerId,
//        @Param("workHourDate") String workHourDate,
//        @Param("discipline") String discipline
//    );
//
//    @Query(nativeQuery = true,
//        value = "select dr.* from drawing_record dr where " +
//            " dr.engineer_id=:engineerId and dr.work_hour_date IN:workHourDates  and dr.deleted = 0")
//    List<Map<String, Object>> drawingRecordsForDownload(
//        @Param("engineerId") Long engineerId,
//        @Param("workHourDates") List<String> workHourDates
//    );
//    @Query(nativeQuery = true,
//        value = "SELECT DISTINCT LEFT(weekly,4) AS year " +
//            "FROM drawing_record WHERE weekly IS NOT NULL ORDER BY year")
//    List<String> findYears();
//    @Query(nativeQuery = true,
//        value = "SELECT DISTINCT RIGHT(weekly,2) AS week " +
//            "FROM drawing_record WHERE weekly IS NOT NULL ORDER BY week")
//    List<String> findWeeks();
//
//    @Query(nativeQuery = true,
//        value = "select sum(work_hour) from drawing_record dr where " +
//            " dr.engineer_id=:engineerId and dr.weekly =:weekly  and dr.deleted = 0")
//    Integer findByEngineerIdAndWeeklyAndDeletedIsFalse(Long engineerId, Integer weekly);
//
//
//
//    List<DrawingRecord> findByOrgIdAndProjectIdAndDrawingNoAndDeletedIsFalse(Long orgId, Long projectId, String drawingNo);
//
//    @Query(nativeQuery = true,
//        value = "select nullif(sum(work_hour), 0) + nullif(sum(out_hour), 0) from drawing_record dr where " +
//            " dr.engineer =:engineer and dr.work_hour_date =:workHourDate  and dr.deleted = 0")
//    Integer findByEngineerAndWorkHourDate(String engineer, String workHourDate);
//
//    @Query(nativeQuery = true,
//        value = "select distinct u.company from saint_whale_auth.users u where " +
//            " u.status = 'ACTIVE' and u.company is not null and u.company <> '' order by u.company")
//    List<String> findByCompany();
//
//    @Query(nativeQuery = true,
//        value = "select distinct u.division from saint_whale_auth.users u where " +
//            " u.status = 'ACTIVE' and u.division is not null and u.division <> '' order by u.division")
//    List<String> findByDivision();
//
//    @Query(nativeQuery = true,
//        value = "select distinct u.department from saint_whale_auth.users u where " +
//            " u.status = 'ACTIVE' and u.department is not null and u.department <> '' order by u.department")
//    List<String> findByDepartment();
//
//    @Query(nativeQuery = true,
//        value = "select distinct u.weekly from drawing_record u where " +
//            " u.status = 'ACTIVE' and u.weekly is not null and u.weekly <> '' and u.work_hour_date between :startDate and :endDate order by u.weekly")
//    List<Integer> findByWeekly(
//        @Param("startDate") String startDate,
//        @Param("endDate") String endDate
//    );


}
