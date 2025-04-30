package com.ose.tasks.domain.model.repository.performance;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.performance.PerformanceAppraisalList;

public interface PerformanceAppraisalListRepository extends PagingAndSortingWithCrudRepository<PerformanceAppraisalList, Long>, PerformanceAppraisalListRepositoryCustom {

}
