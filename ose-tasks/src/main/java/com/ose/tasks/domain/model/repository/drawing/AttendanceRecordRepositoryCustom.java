package com.ose.tasks.domain.model.repository.drawing;

import com.ose.tasks.dto.ManHourCriteriaDTO;
import com.ose.tasks.entity.drawing.AttendanceRecord;
import org.springframework.data.domain.Page;

public interface AttendanceRecordRepositoryCustom {

    Page<AttendanceRecord> getList(ManHourCriteriaDTO dto);
}
