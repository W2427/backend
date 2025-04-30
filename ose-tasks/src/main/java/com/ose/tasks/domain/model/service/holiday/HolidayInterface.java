package com.ose.tasks.domain.model.service.holiday;

import com.ose.dto.ContextDTO;
import com.ose.tasks.dto.holiday.HolidayCreateDTO;
import com.ose.tasks.dto.holiday.HolidaySearchDTO;
import com.ose.tasks.entity.holiday.HolidayData;
import org.springframework.data.domain.Page;

public interface HolidayInterface {

    Page<HolidayData> search(
        HolidaySearchDTO holidaySearchDTO
    );

    HolidayData create(
        HolidayCreateDTO holidayCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 更新节假日。
     *
     * @param id
     * @param holidayCreateDTO
     * @return
     */
    HolidayData update(
        Long id,
        HolidayCreateDTO holidayCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 删除节假日。
     *
     * @param id
     */
    void delete(
        Long id,
        ContextDTO contextDTO
    );

    /**
     * 节假日详情。
     *
     * @param id
     * @return
     */
    HolidayData detail(
        Long id
    );
}
