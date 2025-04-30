package com.ose.material.domain.model.service.impl;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.material.domain.model.repository.MmPurchasePackageFileRepository;
import com.ose.material.domain.model.service.MmPurchasePackageFileInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.MmPurchasePackageFileEntity;
import com.ose.response.JsonObjectResponseBody;
import com.ose.util.LongUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class MmPurchasePackageFileService implements MmPurchasePackageFileInterface {

    /**
     * 采购包文件  操作仓库。
     */
    private final MmPurchasePackageFileRepository mmPurchasePackageFileRepository;

    private final UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法。
     *
     * @param mmPurchasePackageFileRepository
     */
    @Autowired
    public MmPurchasePackageFileService(
        MmPurchasePackageFileRepository mmPurchasePackageFileRepository,
        UploadFeignAPI uploadFeignAPI
    ) {
        this.mmPurchasePackageFileRepository = mmPurchasePackageFileRepository;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    /**
     * 查询采购单文件列表。
     *
     * @param orgId
     * @param projectId
     * @param mmPurchasePackageFileSearchDTO
     * @return
     */
    @Override
    public Page<MmPurchasePackageFileEntity> search(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageFileSearchDTO mmPurchasePackageFileSearchDTO
    ) {
        return mmPurchasePackageFileRepository.search(orgId, projectId, purchasePackageId, mmPurchasePackageFileSearchDTO, mmPurchasePackageFileSearchDTO.toPageable());
    }

    /**
     * 创建采购单文件。
     *
     * @param orgId
     * @param projectId
     * @param mmPurchasePackageFileCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmPurchasePackageFileEntity create(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageFileCreateDTO mmPurchasePackageFileCreateDTO,
        ContextDTO contextDTO
    ) {

        MmPurchasePackageFileEntity mmPurchasePackageFileEntity = new MmPurchasePackageFileEntity();


        if (mmPurchasePackageFileCreateDTO.getFileName() != null) {
            JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(
                orgId.toString(),
                projectId.toString(),
                mmPurchasePackageFileCreateDTO.getFileName(),
                new FilePostDTO()
            );
            FileES fileES = responseBody.getData();
            mmPurchasePackageFileEntity.setFilePath(fileES.getPath());
            mmPurchasePackageFileEntity.setFileName(fileES.getName());
            mmPurchasePackageFileEntity.setFileId(LongUtils.parseLong(fileES.getId()));
        }

        mmPurchasePackageFileEntity.setOrgId(orgId);
        mmPurchasePackageFileEntity.setProjectId(projectId);
        mmPurchasePackageFileEntity.setPurchasePackageId(purchasePackageId);
        mmPurchasePackageFileEntity.setCreatedAt(new Date());
        mmPurchasePackageFileEntity.setCreatedBy(contextDTO.getOperator().getId());
        mmPurchasePackageFileEntity.setLastModifiedAt(new Date());
        mmPurchasePackageFileEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmPurchasePackageFileEntity.setStatus(EntityStatus.ACTIVE);

        return mmPurchasePackageFileRepository.save(mmPurchasePackageFileEntity);
    }

    /**
     * 采购单文件详情。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     * @param purchasePackageFileId
     * @return
     */
    @Override
    public MmPurchasePackageFileEntity detail(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        Long purchasePackageFileId
    ) {
        MmPurchasePackageFileEntity mmPurchasePackageFileEntity = mmPurchasePackageFileRepository.findByOrgIdAndProjectIdAndPurchasePackageIdAndIdAndStatus(
            orgId,
            projectId,
            purchasePackageId,
            purchasePackageFileId,
            EntityStatus.ACTIVE
        );
        if (mmPurchasePackageFileEntity == null) {
            throw new BusinessError("Purchase package file does not exist!采购包文件不存在！");
        }
        return mmPurchasePackageFileEntity;
    }

    /**
     * 删除采购包文件。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     * @param purchasePackageFileId
     * @param contextDTO
     */
    @Override
    public void delete(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        Long purchasePackageFileId,
        ContextDTO contextDTO
    ) {
        Optional<MmPurchasePackageFileEntity> mmPurchasePackageFileEntityOptional = mmPurchasePackageFileRepository.findById(purchasePackageFileId);
        if (mmPurchasePackageFileEntityOptional.isPresent()) {
            MmPurchasePackageFileEntity mmPurchasePackageFileEntity = mmPurchasePackageFileEntityOptional.get();
            mmPurchasePackageFileEntity.setLastModifiedAt(new Date());
            mmPurchasePackageFileEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            mmPurchasePackageFileEntity.setStatus(EntityStatus.DELETED);
            mmPurchasePackageFileEntity.setDeleted(true);
            mmPurchasePackageFileEntity.setDeletedAt(new Date());
            mmPurchasePackageFileEntity.setDeletedBy(contextDTO.getOperator().getId());
            mmPurchasePackageFileRepository.save(mmPurchasePackageFileEntity);
        } else {
            throw new BusinessError("Purchase package file does not exist!采购包文件不存在！");
        }
    }
}
