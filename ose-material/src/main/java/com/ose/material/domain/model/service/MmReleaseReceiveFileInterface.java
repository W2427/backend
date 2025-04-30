package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.material.dto.MmReleaseReceiveFileCreateDTO;
import com.ose.material.dto.MmReleaseReceiveFileSearchDTO;
import com.ose.material.entity.MmReleaseReceiveFileEntity;
import org.springframework.data.domain.Page;

/**
 * 入库单文件接口
 */
public interface MmReleaseReceiveFileInterface {

    /**
     * 获取入库单文件。
     */
    Page<MmReleaseReceiveFileEntity> search(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
        MmReleaseReceiveFileSearchDTO mmReleaseReceiveFileSearchDTO
    );

    /**
     * 创建入库单文件。
     *
     * @param orgId
     * @param projectId
     * @param mmReleaseReceiveFileCreateDTO
     * @return
     */
    MmReleaseReceiveFileEntity create(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
        MmReleaseReceiveFileCreateDTO mmReleaseReceiveFileCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 入库单文件详情。
     *
     * @param orgId
     * @param projectId
     * @param releaseReceiveId
     * @return
     */
    MmReleaseReceiveFileEntity detail(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
        Long releaseReceiveFileId
    );

    /**
     * 删除入库单文件。
     *
     * @param orgId
     * @param projectId
     * @param releaseReceiveId
     */
    void delete(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
        Long releaseReceiveFileId,
        ContextDTO contextDTO
    );
}
