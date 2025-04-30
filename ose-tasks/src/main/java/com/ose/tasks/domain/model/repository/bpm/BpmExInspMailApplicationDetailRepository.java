package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ose.tasks.entity.bpm.BpmExInspMailApplicationDetail;


public interface BpmExInspMailApplicationDetailRepository extends PagingAndSortingWithCrudRepository<BpmExInspMailApplicationDetail, Long> {

    List<BpmExInspMailApplicationDetail> findByExternallInspectionMailApplicationId(Long exInspMailApplicationId);


}
