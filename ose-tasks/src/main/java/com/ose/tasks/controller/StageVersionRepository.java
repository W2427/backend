package com.ose.tasks.controller;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.StageVersion;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author: DaiZeFeng
 * @date: 2023/2/28
 */
public interface StageVersionRepository extends PagingAndSortingWithCrudRepository<StageVersion, Long> {

    Page<StageVersion> findAllByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus entityStatus, Pageable pageable);

    List<StageVersion> findAllByOrgIdAndProjectIdAndStage(Long orgId, Long projectId, String stage);

    StageVersion findAllByOrgIdAndProjectIdAndId(Long orgId, Long projectId, Long id);

    StageVersion findByOrgIdAndProjectIdAndStageAndVersionCodeAndStatus(Long orgId, Long projectId, String stage, String versionCode, EntityStatus entityStatus);
}
