package com.ose.tasks.domain.model.repository.drawing;

import com.ose.tasks.dto.EmployeeDataDTO;
import com.ose.tasks.dto.ReviewCriteriaDTO;
import com.ose.tasks.entity.drawing.DingTalkEmployeeData;
import com.ose.tasks.entity.drawing.EmployeeData;

import java.util.List;

/**
 * @author: DaiZeFeng
 * @date: 2023/2/20
 */
public interface EmployeeDataRepositoryCustom {

    List<EmployeeDataDTO> searchReview(ReviewCriteriaDTO criteriaDTO);

    EmployeeDataDTO findById(String id);

}
