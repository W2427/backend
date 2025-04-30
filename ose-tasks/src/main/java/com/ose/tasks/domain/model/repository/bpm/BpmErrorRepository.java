package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmError;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BpmErrorRepository extends PagingAndSortingWithCrudRepository<BpmError, Long> {


}
