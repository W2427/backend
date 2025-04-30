package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.MProjSubcontrsEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;


/**
 * 查询接口。
 */
@Transactional
public interface MProjSubcontrsRepository extends PagingAndSortingRepository<MProjSubcontrsEntity, String> {

}
