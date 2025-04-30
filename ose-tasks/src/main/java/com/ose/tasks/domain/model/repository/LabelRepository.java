package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface LabelRepository extends PagingAndSortingWithCrudRepository<Label, Long> {

    Page<Label> findByNameAndDeletedIsFalse(String name, Pageable pageable);

    Page<Label> findByDeletedIsFalse(Pageable pageable);

    Label findFirstByNameAndDeletedIsFalse(String name);
}
