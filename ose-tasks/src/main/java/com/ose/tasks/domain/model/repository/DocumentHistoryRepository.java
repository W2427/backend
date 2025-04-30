package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.DocumentHistory;
import com.ose.tasks.entity.SacsUploadHistory;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface DocumentHistoryRepository extends PagingAndSortingWithCrudRepository<DocumentHistory, Long>, DocumentHistoryRepositoryCustom {

    List<SacsUploadHistory> findByIdIn(Long[] ids);
}
