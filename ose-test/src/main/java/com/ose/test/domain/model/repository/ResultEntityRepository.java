package com.ose.test.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.test.entity.ResultEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 业务代码数据仓库。
 */
public interface ResultEntityRepository extends PagingAndSortingWithCrudRepository<ResultEntity, Long> {

    List<ResultEntity> findByDbAndTbAndColumn(String db, String tb, String cl);


    List<ResultEntity> findByExecResultIsNull();
}
