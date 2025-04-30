package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.WRtiEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 退库单
 */
public interface WRtiRepository extends PagingAndSortingWithCrudRepository<WRtiEntity, String> {

    void deleteByProjIdAndRtiNumber(String projId, String rtiNumber);

}
