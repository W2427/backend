package com.ose.tasks.domain.model.repository.performance;


import com.ose.tasks.dto.ReviewCriteriaDTO;
import com.ose.tasks.dto.performance.PerformanceAppraisalListDetailDTO;

import java.util.List;

public interface PerformanceAppraisalListDetailRepositoryCustom {
    List<PerformanceAppraisalListDetailDTO> searchReview(ReviewCriteriaDTO criteriaDTO);
}
