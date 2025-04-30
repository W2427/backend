package com.ose.tasks.domain.model.repository.performance;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.dto.performance.PerformanceAppraisalListDetailDTO;
import com.ose.tasks.entity.performance.PerformanceAppraisalListDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface PerformanceAppraisalListDetailRepository extends PagingAndSortingWithCrudRepository<PerformanceAppraisalListDetail, Long>, PerformanceAppraisalListDetailRepositoryCustom {
    @Query(nativeQuery = true,
        value = "SELECT " +
            "pald.* " +
            "FROM " +
            "saint_whale_tasks.performance_appraisal_list_detail pald " +
            "WHERE " +
            "pald.performance_appraisal_id =: id" )
    List<PerformanceAppraisalListDetailDTO> searchDetailsById(
        @Param("id") long id
    );

}
