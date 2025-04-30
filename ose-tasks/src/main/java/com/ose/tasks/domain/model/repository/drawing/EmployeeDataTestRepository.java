package com.ose.tasks.domain.model.repository.drawing;

import com.ose.tasks.entity.drawing.EmployeeData;
import com.ose.tasks.entity.drawing.EmployeeDataTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Transactional
public interface EmployeeDataTestRepository extends PagingAndSortingRepository<EmployeeDataTest, Long>, EmployeeDataTestRepositoryCustom {

//    EmployeeDataTest findByStaffId(String jobNumber);

    @Query(nativeQuery = true,
        value = "SELECT " +
            "* " +
            "FROM " +
            "saint_whale_tasks.employee_information_test eit " +
            "WHERE " +
            "eit.staff_id =:jobNumber")
    List<Map<String, Object>> searchDataByJobNumber(
        @Param("jobNumber") String jobNumber
    );


}
