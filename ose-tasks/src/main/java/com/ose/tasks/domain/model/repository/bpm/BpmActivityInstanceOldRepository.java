package com.ose.tasks.domain.model.repository.bpm;

import com.ose.tasks.entity.bpm.BpmActivityInstanceOld;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BpmActivityInstanceOldRepository extends PagingAndSortingRepository<BpmActivityInstanceOld, Long> {
    List<BpmActivityInstanceOld> findByOrgIdAndProjectId(Long orgId, Long projectId);
}

