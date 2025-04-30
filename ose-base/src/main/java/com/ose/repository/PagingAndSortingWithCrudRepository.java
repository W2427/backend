package com.ose.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PagingAndSortingWithCrudRepository<T, ID> extends PagingAndSortingRepository<T, ID>, CrudRepository<T, ID> {
}
