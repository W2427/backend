package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.SacsUploadHistory;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface SacsUploadHistoryRepository extends PagingAndSortingWithCrudRepository<SacsUploadHistory, Long>, SacsUploadHistoryRepositoryCustom {

    List<SacsUploadHistory> findByIdIn(Long[] ids);
}
