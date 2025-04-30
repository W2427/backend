package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.WRtiItemEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


/**
 * 退库详情
 */
public interface WRtiItemRepository extends PagingAndSortingWithCrudRepository<WRtiItemEntity, String> {

    void deleteByProjIdAndRtiNumber(String projId, String rtiNumber);
}
