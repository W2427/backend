package com.ose.tasks.domain.model.repository.bpm;

import com.ose.tasks.entity.bpm.GOEMailConfig;
import com.ose.tasks.vo.bpm.MailRunningStatus;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface GOEMailConfigRepository extends PagingAndSortingRepository<GOEMailConfig, Long> {

    List<GOEMailConfig> findByOrgIdAndProjectIdAndTaskDefKey(Long orgId, Long projectId, String taskDefKey);

}
