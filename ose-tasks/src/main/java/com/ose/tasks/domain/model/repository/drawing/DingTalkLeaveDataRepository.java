package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DingTalkLeaveData;
import com.ose.tasks.entity.drawing.DingTalkWorkHour;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Transactional
public interface DingTalkLeaveDataRepository extends PagingAndSortingWithCrudRepository<DingTalkLeaveData, Long>, DingTalkLeaveDataRepositoryCustom {

    DingTalkLeaveData findByJobNumberAndStartLeaveTime(String jobNumber, String startLeaveTime);

//    List<DingTalkLeaveData> searchLeaveByJobNumberAndDateRange(String jobNumber, String startLeaveTime, String endLeaveTime);

    @Query(nativeQuery = true,
//        value = "SELECT " +
//            "* " +
//            "FROM " +
//            "saint_whale_tasks.dingtalk_leavedata dtl " +
//            "WHERE " +
//            "dtl.start_leave_time BETWEEN :startDate and :endDate " +
//            "AND dtl.end_leave_time BETWEEN :startDate and :endDate " +
//            "AND dtl.job_number =:jobNumber"
        value = "SELECT " +
            "* " +
            "FROM " +
            "saint_whale_tasks.dingtalk_leavedata dtl " +
            "WHERE " +
            "dtl.start_leave_time <= CONCAT(:endDate, ' 23:59:59') " +
            "AND dtl.end_leave_time >= CONCAT(:startDate, ' 00:00:00') " +
            "AND dtl.job_number =:jobNumber")
    List<Map<String, Object>> searchLeaveByJobNumberAndDateRange(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("jobNumber") String jobNumber
    );

    DingTalkLeaveData findByJobNumberAndUserIdAndStartLeaveTimeAndEndLeaveTimeAndLeaveCodeAndDurationPercent(
        String jobNumber,
        String userId,
        String startLeaveTime,
        String endLeaveTime,
        String leaveCode,
        Double durationPercent
    );
}
