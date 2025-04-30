package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ose.tasks.entity.bpm.BpmActivityCategory;
import com.ose.vo.EntityStatus;


public interface BpmActivityCategoryRepository extends PagingAndSortingRepository<BpmActivityCategory, Long> {

    List<BpmActivityCategory> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus status);

}
