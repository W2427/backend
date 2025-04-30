package com.ose.materialspm.domain.model.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ose.materialspm.entity.ViewMxjBominfoEntity;


/**
 * 请购单查询接口。
 */
@Transactional
public interface ViewMxjBominfoEntityRepository extends PagingAndSortingRepository<ViewMxjBominfoEntity, String>, ViewMxjBominfoEntityRepositoryCustom {


}
