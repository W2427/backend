package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmActivityInstanceShiftLog;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface BpmActivityInstanceShiftRepository extends PagingAndSortingWithCrudRepository<BpmActivityInstanceShiftLog, Long>, BpmActivityInstanceShiftRepositoryCustom {

    List<BpmActivityInstanceShiftLog> findAllByBaiId(Long baiId);
}

