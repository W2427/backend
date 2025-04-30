package com.ose.tasks.domain.model.repository.drawing;

import com.ose.tasks.dto.CheckOutHourDTO;
import com.ose.tasks.dto.ReviewCriteriaDTO;
import com.ose.tasks.entity.drawing.DingTalkEmployeeData;

import java.util.List;

/**
 * @author: DaiZeFeng
 * @date: 2023/2/20
 */
public interface DingTalkEmployeeDataRepositoryCustom {

    List<DingTalkEmployeeData> searchReview();


}
