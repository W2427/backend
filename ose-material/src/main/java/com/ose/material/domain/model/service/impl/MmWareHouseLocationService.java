package com.ose.material.domain.model.service.impl;

import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.material.domain.model.repository.MmWareHouseLocationRepository;
import com.ose.material.domain.model.service.MmWareHouseLocationInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.MmWareHouseLocationEntity;
import com.ose.material.vo.WareHouseLocationType;
import com.ose.vo.EntityStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MmWareHouseLocationService implements MmWareHouseLocationInterface {

    /**
     * 仓库货架  操作仓库。
     */
    private final MmWareHouseLocationRepository mmWareHouseLocationRepository;

    /**
     * 构造方法。
     *
     * @param mmWareHouseLocationRepository
     */
    @Autowired
    public MmWareHouseLocationService(
        MmWareHouseLocationRepository mmWareHouseLocationRepository
    ) {
        this.mmWareHouseLocationRepository = mmWareHouseLocationRepository;
    }

    /**
     * 查询仓库货架列表。
     *
     * @param orgId
     * @param projectId
     * @param mmWareHouseLocationSearchDTO
     * @return
     */
    @Override
    public Page<MmWareHouseLocationEntity> search(
        Long orgId,
        Long projectId,
        MmWareHouseLocationSearchDTO mmWareHouseLocationSearchDTO
    ) {
        return mmWareHouseLocationRepository.search(
            orgId,
            projectId,
            mmWareHouseLocationSearchDTO
        );
    }

    /**
     * 创建仓库货架。
     *
     * @param orgId
     * @param projectId
     * @param mmWareHouseLocationCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmWareHouseLocationEntity create(
        Long orgId,
        Long projectId,
        MmWareHouseLocationCreateDTO mmWareHouseLocationCreateDTO,
        ContextDTO contextDTO
    ) {

        MmWareHouseLocationEntity oldMmWareHouseLocationEntity = mmWareHouseLocationRepository.findByOrgIdAndProjectIdAndNameAndStatus(
            orgId,
            projectId,
            mmWareHouseLocationCreateDTO.getName(),
            EntityStatus.ACTIVE
        );

        if (oldMmWareHouseLocationEntity != null) {
            throw new BusinessError("WareHouse/Shelves already exists!仓库货架名称已经存在！");
        }

        MmWareHouseLocationEntity mmWareHouseLocationEntity = new MmWareHouseLocationEntity();

        BeanUtils.copyProperties(mmWareHouseLocationCreateDTO, mmWareHouseLocationEntity);
        mmWareHouseLocationEntity.setOrgId(orgId);
        mmWareHouseLocationEntity.setProjectId(projectId);
        mmWareHouseLocationEntity.setCreatedAt(new Date());
        mmWareHouseLocationEntity.setCreatedBy(contextDTO.getOperator().getId());
        mmWareHouseLocationEntity.setLastModifiedAt(new Date());
        mmWareHouseLocationEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmWareHouseLocationEntity.setStatus(EntityStatus.ACTIVE);

        SeqNumberDTO seqNumberDTO = mmWareHouseLocationRepository.getMaxSeqNumber(orgId, projectId, WareHouseLocationType.valueOf(mmWareHouseLocationCreateDTO.getType().toString())).orElse(new SeqNumberDTO(0));
        int seqNumber = seqNumberDTO.getNewSeqNumber();
        mmWareHouseLocationEntity.setSeqNumber(seqNumber);

        return mmWareHouseLocationRepository.save(mmWareHouseLocationEntity);
    }

    /**
     * 更新仓库货架。
     *
     * @param orgId
     * @param projectId
     * @param wareHouseId
     * @param mmWareHouseLocationCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmWareHouseLocationEntity update(
        Long orgId,
        Long projectId,
        Long wareHouseId,
        MmWareHouseLocationCreateDTO mmWareHouseLocationCreateDTO,
        ContextDTO contextDTO
    ) {
        MmWareHouseLocationEntity oldMmWareHouseLocationEntity = mmWareHouseLocationRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            wareHouseId,
            EntityStatus.ACTIVE
        );
        if (oldMmWareHouseLocationEntity == null) {
            throw new BusinessError("WareHouse/Shelves does not exists!仓库货架不存在！");
        }

        if (oldMmWareHouseLocationEntity.getType().equals(WareHouseLocationType.WAREHOUSE) &&
            mmWareHouseLocationCreateDTO.getType().equals(WareHouseLocationType.SHELVES)) {
            List<MmWareHouseLocationEntity> entities = mmWareHouseLocationRepository.findByOrgIdAndProjectIdAndParentWareHouseIdAndStatus(
                orgId,
                projectId,
                oldMmWareHouseLocationEntity.getId(),
                EntityStatus.ACTIVE
            );
            if (entities.size() > 0) {
                throw new BusinessError("WareHouse/Shelves type cannot be changed!仓库/货架不能变更类型！");
            }
        }

        MmWareHouseLocationEntity mmWareHouseLocationEntity = mmWareHouseLocationRepository.findByOrgIdAndProjectIdAndNameAndStatus(
            orgId,
            projectId,
            mmWareHouseLocationCreateDTO.getName(),
            EntityStatus.ACTIVE
        );

        if (mmWareHouseLocationEntity != null && !mmWareHouseLocationEntity.getId().equals(oldMmWareHouseLocationEntity.getId())) {
            throw new BusinessError("WareHouse/Shelves already exists!仓库货架名称已经存在！");
        }

        oldMmWareHouseLocationEntity.setParentWareHouseId(null);
        BeanUtils.copyProperties(mmWareHouseLocationCreateDTO, oldMmWareHouseLocationEntity);
        oldMmWareHouseLocationEntity.setOrgId(orgId);
        oldMmWareHouseLocationEntity.setProjectId(projectId);
        oldMmWareHouseLocationEntity.setLastModifiedAt(new Date());
        oldMmWareHouseLocationEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        oldMmWareHouseLocationEntity.setStatus(EntityStatus.ACTIVE);

        return mmWareHouseLocationRepository.save(oldMmWareHouseLocationEntity);
    }

    /**
     * 仓库货架详情。
     *
     * @param orgId
     * @param projectId
     * @param wareHouseId
     * @return
     */
    @Override
    public MmWareHouseLocationEntity detail(
        Long orgId,
        Long projectId,
        Long wareHouseId
    ) {
        MmWareHouseLocationEntity mmWareHouseLocationEntity = mmWareHouseLocationRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            wareHouseId,
            EntityStatus.ACTIVE
        );
        if (mmWareHouseLocationEntity == null) {
            throw new BusinessError("WareHouse/Shelves does not exists!仓库货架不存在！");
        }
        return mmWareHouseLocationEntity;
    }

    /**
     * 删除仓库货架。
     *
     * @param orgId
     * @param projectId
     * @param wareHouseId
     * @param contextDTO
     */
    @Override
    public void delete(
        Long orgId,
        Long projectId,
        Long wareHouseId,
        ContextDTO contextDTO
    ) {
        MmWareHouseLocationEntity mmWareHouseLocationEntity = mmWareHouseLocationRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            wareHouseId,
            EntityStatus.ACTIVE
        );
        if (mmWareHouseLocationEntity == null) {
            throw new BusinessError("WareHouse does not exist!仓库货架不存在！");
        }

        List<MmWareHouseLocationEntity> entities = mmWareHouseLocationRepository.findByOrgIdAndProjectIdAndParentWareHouseIdAndStatus(
            orgId,
            projectId,
            mmWareHouseLocationEntity.getId(),
            EntityStatus.ACTIVE
        );
        if (entities.size() > 0) {
            throw new BusinessError("WareHouse/Shelves cannot be deleted!仓库/货架不能删除！");
        }

        mmWareHouseLocationEntity.setLastModifiedAt(new Date());
        mmWareHouseLocationEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmWareHouseLocationEntity.setStatus(EntityStatus.DELETED);
        mmWareHouseLocationEntity.setDeleted(true);
        mmWareHouseLocationEntity.setDeletedAt(new Date());
        mmWareHouseLocationEntity.setDeletedBy(contextDTO.getOperator().getId());
        mmWareHouseLocationRepository.save(mmWareHouseLocationEntity);
    }

    /**
     * 查询某类仓库货架列表。
     *
     * @param orgId
     * @param projectId
     * @param wareHouseType
     * @return
     */
    @Override
    public List<MmWareHouseLocationEntity> searchType(
        Long orgId,
        Long projectId,
        String wareHouseType
    ) {
        if (wareHouseType != null) {
            List<MmWareHouseLocationEntity> mmWareHouseLocationEntities = mmWareHouseLocationRepository.findByOrgIdAndProjectIdAndTypeAndStatus(orgId, projectId, WareHouseLocationType.valueOf(wareHouseType), EntityStatus.ACTIVE);
            return mmWareHouseLocationEntities;
        }
        return null;
    }
}
