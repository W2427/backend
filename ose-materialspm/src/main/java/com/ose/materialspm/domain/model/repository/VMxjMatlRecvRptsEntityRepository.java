package com.ose.materialspm.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ose.materialspm.entity.VMxjMatlRecvRptsEntity;


/**
 * 请购单查询接口。
 */
@Transactional
public interface VMxjMatlRecvRptsEntityRepository extends PagingAndSortingWithCrudRepository<VMxjMatlRecvRptsEntity, String>, VMxjMatlRecvRptsEntityRepositoryCustom {

}
