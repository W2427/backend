package com.ose.material.domain.model.service.impl;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.material.domain.model.repository.MmReleaseReceiveFileRepository;
import com.ose.material.domain.model.service.MmReleaseReceiveFileInterface;
import com.ose.material.dto.MmReleaseReceiveFileCreateDTO;
import com.ose.material.dto.MmReleaseReceiveFileSearchDTO;
import com.ose.material.entity.MmReleaseReceiveFileEntity;
import com.ose.response.JsonObjectResponseBody;
import com.ose.util.LongUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class MmReleaseReceiveFileService implements MmReleaseReceiveFileInterface {

    /**
     * 入库单文件  操作仓库。
     */
    private final MmReleaseReceiveFileRepository mmReleaseReceiveFileRepository;

    private final UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法。
     *
     * @param mmReleaseReceiveFileRepository
     */
    @Autowired
    public MmReleaseReceiveFileService(
        MmReleaseReceiveFileRepository mmReleaseReceiveFileRepository,
        UploadFeignAPI uploadFeignAPI
    ) {
        this.mmReleaseReceiveFileRepository = mmReleaseReceiveFileRepository;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    /**
     * 查询入库单文件列表。
     *
     * @param orgId
     * @param projectId
     * @param mmReleaseReceiveFileSearchDTO
     * @return
     */
    @Override
    public Page<MmReleaseReceiveFileEntity> search(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
        MmReleaseReceiveFileSearchDTO mmReleaseReceiveFileSearchDTO
    ) {
        return mmReleaseReceiveFileRepository.search(orgId, projectId, releaseReceiveId, mmReleaseReceiveFileSearchDTO, mmReleaseReceiveFileSearchDTO.toPageable());
    }

    /**
     * 创建入库单文件。
     *
     * @param orgId
     * @param projectId
     * @param mmReleaseReceiveFileCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmReleaseReceiveFileEntity create(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
        MmReleaseReceiveFileCreateDTO mmReleaseReceiveFileCreateDTO,
        ContextDTO contextDTO
    ) {

        MmReleaseReceiveFileEntity mmReleaseReceiveFileEntity = new MmReleaseReceiveFileEntity();


        if (mmReleaseReceiveFileCreateDTO.getFileName() != null) {
            JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(
                orgId.toString(),
                projectId.toString(),
                mmReleaseReceiveFileCreateDTO.getFileName(),
                new FilePostDTO()
            );
            FileES fileES = responseBody.getData();
            mmReleaseReceiveFileEntity.setFilePath(fileES.getPath());
            mmReleaseReceiveFileEntity.setFileName(fileES.getName());
            mmReleaseReceiveFileEntity.setFileId(LongUtils.parseLong(fileES.getId()));
        }

        mmReleaseReceiveFileEntity.setOrgId(orgId);
        mmReleaseReceiveFileEntity.setProjectId(projectId);
        mmReleaseReceiveFileEntity.setReleaseReceiveId(releaseReceiveId);
        mmReleaseReceiveFileEntity.setCreatedAt(new Date());
        mmReleaseReceiveFileEntity.setCreatedBy(contextDTO.getOperator().getId());
        mmReleaseReceiveFileEntity.setLastModifiedAt(new Date());
        mmReleaseReceiveFileEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmReleaseReceiveFileEntity.setStatus(EntityStatus.ACTIVE);

        return mmReleaseReceiveFileRepository.save(mmReleaseReceiveFileEntity);
    }

    /**
     * 入库单文件详情。
     *
     * @param orgId
     * @param projectId
     * @param releaseReceiveId
     * @param releaseReceiveFileId
     * @return
     */
    @Override
    public MmReleaseReceiveFileEntity detail(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
        Long releaseReceiveFileId
    ) {
        MmReleaseReceiveFileEntity mmReleaseReceiveFileEntity = mmReleaseReceiveFileRepository.findByOrgIdAndProjectIdAndReleaseReceiveIdAndIdAndStatus(
            orgId,
            projectId,
            releaseReceiveId,
            releaseReceiveFileId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveFileEntity == null) {
            throw new BusinessError("Release Receive file does not exist!入库单文件不存在！");
        }
        return mmReleaseReceiveFileEntity;
    }

    /**
     * 删除入库单文件。
     *
     * @param orgId
     * @param projectId
     * @param releaseReceiveId
     * @param releaseReceiveFileId
     * @param contextDTO
     */
    @Override
    public void delete(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
        Long releaseReceiveFileId,
        ContextDTO contextDTO
    ) {
        Optional<MmReleaseReceiveFileEntity> mmReleaseReceiveFileEntityOptional = mmReleaseReceiveFileRepository.findById(releaseReceiveFileId);
        if (mmReleaseReceiveFileEntityOptional.isPresent()) {
            MmReleaseReceiveFileEntity mmReleaseReceiveFileEntity = mmReleaseReceiveFileEntityOptional.get();
            mmReleaseReceiveFileEntity.setLastModifiedAt(new Date());
            mmReleaseReceiveFileEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            mmReleaseReceiveFileEntity.setStatus(EntityStatus.DELETED);
            mmReleaseReceiveFileEntity.setDeleted(true);
            mmReleaseReceiveFileEntity.setDeletedAt(new Date());
            mmReleaseReceiveFileEntity.setDeletedBy(contextDTO.getOperator().getId());
            mmReleaseReceiveFileRepository.save(mmReleaseReceiveFileEntity);
        } else {
            throw new BusinessError("Release Receive file does not exist!入库单文件不存在！");
        }
    }
}
