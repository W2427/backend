package com.ose.tasks.domain.model.repository.performance;

import com.ose.tasks.dto.ReviewCriteriaDTO;
import com.ose.tasks.dto.performance.PerformanceAppraisalListDTO;


import java.util.List;

public interface PerformanceAppraisalListRepositoryCustom {
    List<PerformanceAppraisalListDTO> searchReview(ReviewCriteriaDTO criteriaDTO);
}
