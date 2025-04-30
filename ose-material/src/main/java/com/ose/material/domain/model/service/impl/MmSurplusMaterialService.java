package com.ose.material.domain.model.service.impl;

import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.material.domain.model.repository.*;
import com.ose.material.domain.model.service.MmSurplusMaterialInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.*;
import com.ose.vo.EntityStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class MmSurplusMaterialService implements MmSurplusMaterialInterface {

    @Value("${application.files.temporary}")
    private String temporaryDir;

    /**
     * 余料库 操作仓库。
     */
    private final MmSurplusMaterialRepository mmSurplusMaterialRepository;

    /**
     * 构造方法。
     *
     * @param mmSurplusMaterialRepository 余料库 操作仓库
     */
    @Autowired
    public MmSurplusMaterialService(
        MmSurplusMaterialRepository mmSurplusMaterialRepository
    ) {
        this.mmSurplusMaterialRepository = mmSurplusMaterialRepository;

    }

    /**
     * 查找余料。
     *
     * @param orgId
     * @param projectId
     * @param mmSurplusMaterialSearchDTO
     * @return
     */
    @Override
    public Page<MmSurplusMaterialEntity> search(
        Long orgId,
        Long projectId,
        MmSurplusMaterialSearchDTO mmSurplusMaterialSearchDTO
    ) {
        return mmSurplusMaterialRepository.search(
            orgId,
            projectId,
            mmSurplusMaterialSearchDTO
        );
    }

    /**
     * 创建余料。
     *
     * @param orgId
     * @param projectId
     * @param mmSurplusMaterialCreateDTO
     * @return
     */
    @Override
    public MmSurplusMaterialEntity create(
        Long orgId,
        Long projectId,
        MmSurplusMaterialCreateDTO mmSurplusMaterialCreateDTO,
        ContextDTO contextDTO
    ) {
        MmSurplusMaterialEntity mmSurplusMaterialEntity = mmSurplusMaterialRepository.findByOrgIdAndProjectIdAndNameAndStatus(
            orgId,
            projectId,
            mmSurplusMaterialCreateDTO.getName(),
            EntityStatus.ACTIVE
        );

        if (null != mmSurplusMaterialEntity) {
            throw new BusinessError("余料名已经存在");
        }

        MmSurplusMaterialEntity newMmSurplusMaterialEntity = new MmSurplusMaterialEntity();

        BeanUtils.copyProperties(
            mmSurplusMaterialCreateDTO,
            newMmSurplusMaterialEntity,
            "issuedFlag","description"
        );
        newMmSurplusMaterialEntity.setProjectId(projectId);
        newMmSurplusMaterialEntity.setOrgId(orgId);
        newMmSurplusMaterialEntity.setCreatedAt(new Date());
        if (null != contextDTO.getOperator()) {
            newMmSurplusMaterialEntity.setCreatedBy(contextDTO.getOperator().getId());
            newMmSurplusMaterialEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        } else {
            newMmSurplusMaterialEntity.setCreatedBy(mmSurplusMaterialCreateDTO.getCreatedBy());
            newMmSurplusMaterialEntity.setLastModifiedBy(mmSurplusMaterialCreateDTO.getCreatedBy());
        }

        newMmSurplusMaterialEntity.setLastModifiedAt(new Date());
        newMmSurplusMaterialEntity.setStatus(EntityStatus.ACTIVE);
        return mmSurplusMaterialRepository.save(newMmSurplusMaterialEntity);
    }


    /**
     * 更新余料。
     *
     * @param orgId
     * @param projectId
     * @param mmSurplusMaterialCreateDTO
     * @return
     */
    @Override
    public MmSurplusMaterialEntity update(
        Long orgId,
        Long projectId,
        Long surplusMaterialId,
        MmSurplusMaterialCreateDTO mmSurplusMaterialCreateDTO,
        ContextDTO contextDTO
    ) {
        MmSurplusMaterialEntity mmSurplusMaterialEntity = mmSurplusMaterialRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            surplusMaterialId,
            EntityStatus.ACTIVE
        );

        if (null == mmSurplusMaterialEntity) {
            throw new BusinessError("余料ID不正确");
        }

        if(null!=mmSurplusMaterialCreateDTO.getIssuedFlag()){
            mmSurplusMaterialEntity.setIssuedFlag(mmSurplusMaterialCreateDTO.getIssuedFlag());
        }
        if(null!=mmSurplusMaterialCreateDTO.getDescription()){
            mmSurplusMaterialEntity.setDescription(mmSurplusMaterialCreateDTO.getDescription());
        }

        mmSurplusMaterialEntity.setLastModifiedAt(new Date());
        return mmSurplusMaterialRepository.save(mmSurplusMaterialEntity);
    }


    /**
     * task服务调用，查找余料信息。
     *
     * @param orgId
     * @param projectId
     * @param mmSurplusMaterialSearchDTO
     * @return
     */
    @Override
    public MmSurplusMaterialEntity searchSurplusMaterial(
        Long orgId,
        Long projectId,
        MmSurplusMaterialSearchDTO mmSurplusMaterialSearchDTO
    ) {

        return mmSurplusMaterialRepository.findByOrgIdAndProjectIdAndNameAndStatus(
            orgId,
            projectId,
            mmSurplusMaterialSearchDTO.getKeyword(),
            EntityStatus.ACTIVE
        );
    }
}
