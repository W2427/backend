package com.ose.tasks.domain.model.repository.holiday;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.holiday.HolidayData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface HolidayDateRepository extends PagingAndSortingWithCrudRepository<HolidayData, Long>, HolidayDateRepositoryCustom {

    List<HolidayData> findByHolidayDateAndDeletedIsFalse(String holidayDate);

    HolidayData findByHolidayDateAndCountryAndDeletedIsFalse(String date, String country);

    Boolean existsByHolidayDateAndDeletedIsFalse(String date);

    @Query(value = " SELECT DISTINCT holiday_date FROM holiday_data WHERE status = 'ACTIVE' AND holiday_date BETWEEN :startDate AND :endDate ", nativeQuery = true)
    List<String> findAllHolidayDate(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
        );

    @Query(value = " SELECT DISTINCT holiday_date FROM holiday_data WHERE status = 'ACTIVE' and country = :country AND holiday_date BETWEEN :startDate AND :endDate ", nativeQuery = true)
    List<String> findAllHolidayDateByCountry(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("country") String country
    );

}
