package com.ose.tasks.domain.model.repository.holiday;

import com.ose.tasks.dto.holiday.HolidaySearchDTO;
import com.ose.tasks.entity.holiday.HolidayData;
import org.springframework.data.domain.Page;

public interface HolidayDateRepositoryCustom {

    Page<HolidayData> search(
        HolidaySearchDTO holidaySearchDTO
    );
}
