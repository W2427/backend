package com.ose.materialspm.domain.model.repository;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ose.materialspm.entity.Demo;


/**
 * demo CRUD 操作接口。
 */
@Transactional
public interface DemoRepository extends PagingAndSortingWithCrudRepository<Demo, Long>, DemoRepositoryCustom {


    List<Demo> findByName(String name);

}
