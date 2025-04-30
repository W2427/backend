package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ose.tasks.entity.bpm.BpmExInspMailApplication;


public interface BpmExInspMailApplicationRepository extends PagingAndSortingWithCrudRepository<BpmExInspMailApplication, Long> {

}
