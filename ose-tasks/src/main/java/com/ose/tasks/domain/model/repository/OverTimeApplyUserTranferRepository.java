package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.OverTimeAppyFormUserTransferHistory;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface OverTimeApplyUserTranferRepository extends PagingAndSortingWithCrudRepository<OverTimeAppyFormUserTransferHistory, Long>, OverTimeApplyUserTranferRepositoryCustom {

}
