package com.ose.tasks.domain.model.repository.drawing;

import com.ose.tasks.dto.*;
import com.ose.tasks.entity.drawing.DrawingRecord;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author: DaiZeFeng
 * @date: 2023/2/20
 */
public interface DrawingRecordRepositoryCustom {

    List<DrawingRecord> getListByCondition(Long orgId, Long projectId, DrawingRecordCriteriaDTO criteriaDTO);

    Page<DrawingRecord> searchDetail(Long userId,
                               ManHourDetailDTO manHourDetailDTO);

    Page<AttendanceDataDTO> searchAttendance(ManHourCriteriaDTO manHourCriteriaDTO);

    Page<ReviewDataDTO> searchReview(ReviewCriteriaDTO criteriaDTO);

    List<WeeklyManHourDTO> weeklyManHour(ReviewCriteriaDTO criteriaDTO);

    List<CheckStandardWorkHourDTO> checkStandardWorkHour(ReviewCriteriaDTO criteriaDTO);
}
