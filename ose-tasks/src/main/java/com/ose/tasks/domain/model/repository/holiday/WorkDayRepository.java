package com.ose.tasks.domain.model.repository.holiday;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.holiday.WorkDayData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface WorkDayRepository extends PagingAndSortingWithCrudRepository<WorkDayData, Long>, WorkDayRepositoryCustom {

//    List<HolidayData> findByHolidayDateAndDeletedIsFalse(String holidayDate);
//
    WorkDayData findByWorkDateAndCountryAndDeletedIsFalse(String date, String country);
//
//    Boolean existsByHolidayDateAndDeletedIsFalse(String date);
//
    @Query(value = " SELECT DISTINCT work_date FROM work_day_data WHERE status = 'ACTIVE' AND work_date BETWEEN :startDate AND :endDate ", nativeQuery = true)
    List<String> findAllHolidayDate(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
        );
//
//    @Query(value = " SELECT DISTINCT holiday_date FROM holiday_data WHERE status = 'ACTIVE' and country = :country AND holiday_date BETWEEN :startDate AND :endDate ", nativeQuery = true)
//    List<String> findAllHolidayDateByCountry(
//        @Param("startDate") String startDate,
//        @Param("endDate") String endDate,
//        @Param("country") String country
//    );

}
