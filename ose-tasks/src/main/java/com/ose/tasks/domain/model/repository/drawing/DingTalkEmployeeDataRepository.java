package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DingTalkEmployeeData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Transactional
public interface DingTalkEmployeeDataRepository extends PagingAndSortingWithCrudRepository<DingTalkEmployeeData, Long>, DingTalkEmployeeDataRepositoryCustom {

    DingTalkEmployeeData findByJobNumberAndName(String jobNumber, String name);

    @Query(nativeQuery = true,
        value = "SELECT DISTINCT " +
            "dte.job_number, " +
            "dte.name, " +
            "dte.employee_status, " +
            "dte.confirm_join_time, " +
            "dte.regular_time, " +
            "ei.length_of_career,  " +
            "ei.initial_employment_date,  " +
            "ei.transfer_to_regular_date  " +
            "FROM " +
            "saint_whale_tasks.dingtalk_employeedata dte " +
            "LEFT JOIN saint_whale_tasks.employee_information ei ON dte.job_number = ei.employee_id")
    List<Map<String, Object>> searchUserInfo();
}
