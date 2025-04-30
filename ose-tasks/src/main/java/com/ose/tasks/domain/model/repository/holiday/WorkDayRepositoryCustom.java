package com.ose.tasks.domain.model.repository.holiday;

import com.ose.tasks.dto.holiday.WorkDaySearchDTO;
import com.ose.tasks.entity.holiday.WorkDayData;
import org.springframework.data.domain.Page;

public interface WorkDayRepositoryCustom {

    Page<WorkDayData> search(
        WorkDaySearchDTO dto
    );
}
