package com.ose.auth.domain.model.repository;


import com.ose.auth.entity.Company;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
public interface CompanyRepository extends PagingAndSortingWithCrudRepository<Company, Long>,CompanyRepositoryCustom {
    List<Company> findByDeletedIsFalse();

    Company findByNameAndCountryAndDeletedIsFalse(String name ,String country);
}
