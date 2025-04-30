package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.GOEMailHistory;
import com.ose.tasks.vo.bpm.MailRunningStatus;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface GOEMailHistoryRepository extends PagingAndSortingWithCrudRepository<GOEMailHistory, Long> {

    List<GOEMailHistory> findByOrgIdAndProjectIdAndSendStatusAndServerUrl(Long orgId, Long projectId,
                                                                          MailRunningStatus status, String url);

}
