package com.ose.auth.domain.model.repository;


import com.ose.auth.entity.Department;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
public interface DepartmentRepository extends PagingAndSortingWithCrudRepository<Department, Long>,DepartmentRepositoryCustom {
    List<Department> findByDeletedIsFalse();

    Department findByNameAndDeletedIsFalse(String name);
}
