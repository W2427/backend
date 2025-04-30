package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmIssueSearchDTO;
import com.ose.material.entity.MmIssueEntity;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 入库材料二维码库。
 */
@Transactional
public interface MmIssueRepositoryCustom {

    Page<MmIssueEntity> search(Long orgId,
                               Long projectId,
                               MmIssueSearchDTO mmIssueSearchDTO);

}
