package com.ose.test.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.test.entity.ColumnNewEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

/**
 * 业务代码数据仓库。
 */
public interface ColumnNewEntityRepository extends PagingAndSortingWithCrudRepository<ColumnNewEntity, Long> {



    Optional<ColumnNewEntity> findByDbAndTbAndColumn(String db, String tb, String column);




    List<ColumnNewEntity> findByMarkIsNull();
}
