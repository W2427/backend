package com.ose.test.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.test.entity.DuplicatedKeyEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 业务代码数据仓库。
 */
public interface DuplicatedKeyRepository extends PagingAndSortingWithCrudRepository<DuplicatedKeyEntity, Long> {


    DuplicatedKeyEntity findByOldValue(String id);

    List<DuplicatedKeyEntity> findByDbAndTbAndColumnAndExecResultIsNull(String db, String tb, String column);

}
