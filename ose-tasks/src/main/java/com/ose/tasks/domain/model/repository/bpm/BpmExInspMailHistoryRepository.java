package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ose.tasks.entity.bpm.BpmExInspMailHistory;
import com.ose.tasks.vo.bpm.MailRunningStatus;


public interface BpmExInspMailHistoryRepository extends PagingAndSortingWithCrudRepository<BpmExInspMailHistory, Long> {

    List<BpmExInspMailHistory> findByOrgIdAndProjectIdAndSendStatusAndServerUrl(Long orgId, Long projectId,
                                                                                MailRunningStatus status, String url);

}
