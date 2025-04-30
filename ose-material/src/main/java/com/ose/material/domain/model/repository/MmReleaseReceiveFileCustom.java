package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmReleaseReceiveFileSearchDTO;
import com.ose.material.entity.MmReleaseReceiveFileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 入库单文件数据仓库。
 */
public interface MmReleaseReceiveFileCustom {

    /**
     * 查询入库单文件。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param releaseReceiveId   入库单 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 任务包分页数据
     */
    Page<MmReleaseReceiveFileEntity> search(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
        MmReleaseReceiveFileSearchDTO criteriaDTO,
        Pageable pageable
    );

}
