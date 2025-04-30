package com.ose.auth.domain.model.repository;


import com.ose.auth.entity.Division;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
public interface DivisionRepository extends PagingAndSortingWithCrudRepository<Division, Long>,DivisionRepositoryCustom {
    List<Division> findByDeletedIsFalse();

    Division findByNameAndDeletedIsFalse(String name);
}
