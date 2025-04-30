package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmActivityInstanceLog;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface BpmActivityInstanceLogRepository extends PagingAndSortingWithCrudRepository<BpmActivityInstanceLog, Long> {

}

