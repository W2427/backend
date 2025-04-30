package com.ose.tasks.domain.model.repository.drawing;

import com.ose.tasks.dto.*;
import com.ose.tasks.entity.drawing.DingTalkWorkHour;
import com.ose.tasks.entity.drawing.DrawingRecord;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author: DaiZeFeng
 * @date: 2023/2/20
 */
public interface DingTalkWorkHourRepositoryCustom {

List<CheckOutHourDTO> searchReview(ReviewCriteriaDTO criteriaDTO);


}
