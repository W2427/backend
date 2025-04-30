package com.ose.tasks.domain.model.repository.drawing;

import com.ose.tasks.dto.EmployeeDataDTO;
import com.ose.tasks.dto.EmployeeDataTestDTO;
import com.ose.tasks.dto.ReviewCriteriaDTO;

import java.util.List;

/**
 * @author: DaiZeFeng
 * @date: 2023/2/20
 */
public interface EmployeeDataTestRepositoryCustom {

List<EmployeeDataTestDTO> searchReview(ReviewCriteriaDTO criteriaDTO);


}
