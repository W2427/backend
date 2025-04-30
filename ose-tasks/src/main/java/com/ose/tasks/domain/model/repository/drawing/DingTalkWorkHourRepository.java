package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.dto.CheckOutHourDTO;
import com.ose.tasks.entity.drawing.DingTalkWorkHour;
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
public interface DingTalkWorkHourRepository extends PagingAndSortingWithCrudRepository<DingTalkWorkHour, Long>, DingTalkWorkHourRepositoryCustom {

    DingTalkWorkHour findByJobNumberAndWorkHourDate(String jobNumber, String workHourDate);

//    List<Map<String, Object>> findByJobNumber(String jobNumber);

    @Query(nativeQuery = true,
        value = "SELECT " +
            "dtw.*, " +
            "SUM( dr.out_hour ) AS outHour " +
            "FROM " +
            "saint_whale_tasks.drawing_record dr " +
            "LEFT JOIN saint_whale_auth.users u ON u.id = dr.engineer_id " +
            "LEFT JOIN saint_whale_tasks.dingtalk_workhour dtw ON ( u.username = dtw.job_number AND dr.work_hour_date = dtw.work_hour_date ) " +
            "WHERE " +
            "u.username =:jobNumber " +
            "AND dr.deleted = 0 " +
            "AND dr.out_hour IS NOT NULL " +
            "AND dr.work_hour_date BETWEEN :startWorkHourDate AND :endWorkHourDate " +
            "GROUP BY " +
            "dr.work_hour_date, " +
            "dr.engineer_id, " +
            "u.username; ")
    List<Map<String, Object>> findOutHourByJobNumberAndWorkHourDate(
        @Param("jobNumber") String jobNumber,
        @Param("startWorkHourDate") String startWorkHourDate,
        @Param("endWorkHourDate") String endWorkHourDate
    );

}
