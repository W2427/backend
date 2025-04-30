package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ose.tasks.entity.bpm.BpmProcessCheckList;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface BpmProcessCheckListRepository extends PagingAndSortingWithCrudRepository<BpmProcessCheckList, Long> {

    void deleteByProcessId(Long processId);

    List<BpmProcessCheckList> findByProcessId(Long processId);

    BpmProcessCheckList findByProcessIdAndFileId(Long processId, Long fileId);

}
