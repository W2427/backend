package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.Suggestion;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface SuggestionRepository extends PagingAndSortingWithCrudRepository<Suggestion, Long>, SuggestionRepositoryCustom {

}
