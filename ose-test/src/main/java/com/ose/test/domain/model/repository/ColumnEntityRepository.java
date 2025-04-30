package com.ose.test.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.test.entity.ColumnEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


/**
 * 业务代码数据仓库。
 */
public interface ColumnEntityRepository extends PagingAndSortingWithCrudRepository<ColumnEntity, Long>, ColumnEntityRepositoryCustom {


    List<ColumnEntity> findByMark(String mark);

    List<ColumnEntity> findByMarkAndIsHandledIsNull(String mark);



    @Query(value = "UPDATE columns set action = null, is_handled = null",
    nativeQuery = true)
    @Transactional
    @Modifying
    void updateColumnEntityNull();

    List<ColumnEntity> findByColumnAndMark(String id, String mark);


    List<ColumnEntity> findByDbAndColumnIn(String db, Collection columns);

    @Query(value = "SELECT id FROM columns WHERE TABLE_NAME =:tb", nativeQuery = true)
    List<String> getIds(@Param("tb") String tb);

}
