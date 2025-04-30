package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.AttendanceRecord;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AttendanceRecordRepository extends PagingAndSortingWithCrudRepository<AttendanceRecord, Long>, AttendanceRecordRepositoryCustom {

    @Query(nativeQuery = true,
        value = "SELECT locked_date FROM attendance_record WHERE locked_date IN :lockedDates AND status = 'ACTIVE' ")
    List<String> findExistedDate(
        @Param("lockedDates") List<String> lockedDates
    );

    boolean existsByLockedDateAndStatus(String lockedDate, EntityStatus status);

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


}
