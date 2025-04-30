package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBlob;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface BpmActivityInstanceBlobRepository extends PagingAndSortingWithCrudRepository<BpmActivityInstanceBlob, Long> {

    BpmActivityInstanceBlob findByProjectIdAndBaiId(Long projectId, Long id);

    @Query("SELECT t FROM BpmActivityInstanceBlob  t WHERE t.baiId = :baiId")
    BpmActivityInstanceBlob findByBaiId(@Param("baiId") Long baiId);
}

