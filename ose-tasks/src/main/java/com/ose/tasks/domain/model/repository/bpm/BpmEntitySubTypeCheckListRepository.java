package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ose.tasks.entity.bpm.BpmEntitySubTypeCheckList;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface BpmEntitySubTypeCheckListRepository extends PagingAndSortingWithCrudRepository<BpmEntitySubTypeCheckList, Long> {

    void deleteByEntitySubTypeId(Long id);

    List<BpmEntitySubTypeCheckList> findByEntitySubTypeId(Long id);

}
