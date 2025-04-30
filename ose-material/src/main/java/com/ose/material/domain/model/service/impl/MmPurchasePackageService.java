package com.ose.material.domain.model.service.impl;

import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.material.domain.model.repository.MmPurchasePackageRepository;
import com.ose.material.domain.model.repository.MmPurchasePackageVendorRelationRepository;
import com.ose.material.domain.model.repository.MmVendorRepository;
import com.ose.material.domain.model.service.MmPurchasePackageInterface;
import com.ose.material.dto.*;
import com.ose.material.dto.SeqNumberDTO;
import com.ose.material.entity.MmPurchasePackageEntity;
import com.ose.material.entity.MmPurchasePackageVendorRelationEntity;
import com.ose.material.entity.MmVendorEntity;
import com.ose.vo.EntityStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class MmPurchasePackageService implements MmPurchasePackageInterface {

    /**
     * 采购包  操作仓库。
     */
    private final MmPurchasePackageRepository mmPurchasePackageRepository;

    private final MmPurchasePackageVendorRelationRepository mmPurchasePackageVendorRelationRepository;
    private final MmVendorRepository mmVendorRepository;

    /**
     * 构造方法。
     *
     * @param mmPurchasePackageRepository
     */
    @Autowired
    public MmPurchasePackageService(
        MmPurchasePackageRepository mmPurchasePackageRepository,
        MmPurchasePackageVendorRelationRepository mmPurchasePackageVendorRelationRepository,
        MmVendorRepository mmVendorRepository
    ) {
        this.mmPurchasePackageRepository = mmPurchasePackageRepository;
        this.mmPurchasePackageVendorRelationRepository = mmPurchasePackageVendorRelationRepository;
        this.mmVendorRepository = mmVendorRepository;
    }

    /**
     * 查询采购包列表。
     *
     * @param orgId
     * @param projectId
     * @param mmPurchasePackageSearchDTO
     * @return
     */
    @Override
    public Page<MmPurchasePackageEntity> search(
        Long orgId,
        Long projectId,
        MmPurchasePackageSearchDTO mmPurchasePackageSearchDTO
    ) {
        return mmPurchasePackageRepository.search(
            orgId,
            projectId,
            mmPurchasePackageSearchDTO
        );
    }

    /**
     * 创建采购包。
     *
     * @param orgId
     * @param projectId
     * @param mmPurchasePackageCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmPurchasePackageEntity create(
        Long orgId,
        Long projectId,
        MmPurchasePackageCreateDTO mmPurchasePackageCreateDTO,
        ContextDTO contextDTO
    ) {

        MmPurchasePackageEntity oldMmPurchasePackageEntity = mmPurchasePackageRepository.findByOrgIdAndProjectIdAndPwpsNumberAndStatus(
            orgId,
            projectId,
            mmPurchasePackageCreateDTO.getPwpsNumber(),
            EntityStatus.ACTIVE
        );

        if (oldMmPurchasePackageEntity != null) {
            throw new BusinessError("Purchase package name already exist!采购包名称已经存在！");
        }

        MmPurchasePackageEntity MmPurchasePackageEntity = new MmPurchasePackageEntity();

        BeanUtils.copyProperties(mmPurchasePackageCreateDTO, MmPurchasePackageEntity);
        MmPurchasePackageEntity.setOrgId(orgId);
        MmPurchasePackageEntity.setProjectId(projectId);
        MmPurchasePackageEntity.setCreatedAt(new Date());
        MmPurchasePackageEntity.setCreatedBy(contextDTO.getOperator().getId());
        MmPurchasePackageEntity.setLastModifiedAt(new Date());
        MmPurchasePackageEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        MmPurchasePackageEntity.setStatus(EntityStatus.ACTIVE);

        SeqNumberDTO seqNumberDTO = mmPurchasePackageRepository.getMaxSeqNumber(orgId, projectId).orElse(new SeqNumberDTO(0));
        int seqNumber = seqNumberDTO.getNewSeqNumber();
        MmPurchasePackageEntity.setSeqNumber(seqNumber);
        return mmPurchasePackageRepository.save(MmPurchasePackageEntity);
    }

    /**
     * 更新采购包。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     * @param mmPurchasePackageCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmPurchasePackageEntity update(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageCreateDTO mmPurchasePackageCreateDTO,
        ContextDTO contextDTO
    ) {
        MmPurchasePackageEntity oldMmPurchasePackageEntity = mmPurchasePackageRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            purchasePackageId,
            EntityStatus.ACTIVE
        );
        if (oldMmPurchasePackageEntity == null) {
            throw new BusinessError("Purchase package info does not exist!采购包信息不存在！");
        }

        // 判断是否只更新状态
        if(null!=mmPurchasePackageCreateDTO.getLocked()){
            oldMmPurchasePackageEntity.setLastModifiedAt(new Date());
            oldMmPurchasePackageEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            oldMmPurchasePackageEntity.setLocked(mmPurchasePackageCreateDTO.getLocked());
        }else{

            MmPurchasePackageEntity mmPurchasePackageEntity = mmPurchasePackageRepository.findByOrgIdAndProjectIdAndPwpsNumberAndStatus(
                orgId,
                projectId,
                mmPurchasePackageCreateDTO.getPwpsNumber(),
                EntityStatus.ACTIVE
            );

            if (mmPurchasePackageEntity != null && !mmPurchasePackageEntity.getId().equals(oldMmPurchasePackageEntity.getId())) {
                throw new BusinessError("Purchase package name already exist!采购包名称已经存在！");
            }

            BeanUtils.copyProperties(mmPurchasePackageCreateDTO, oldMmPurchasePackageEntity);
            oldMmPurchasePackageEntity.setOrgId(orgId);
            oldMmPurchasePackageEntity.setProjectId(projectId);
            oldMmPurchasePackageEntity.setLastModifiedAt(new Date());
            oldMmPurchasePackageEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            oldMmPurchasePackageEntity.setStatus(EntityStatus.ACTIVE);
        }
        return mmPurchasePackageRepository.save(oldMmPurchasePackageEntity);
    }

    /**
     * 采购包详情。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     * @return
     */
    @Override
    public MmPurchasePackageEntity detail(
        Long orgId,
        Long projectId,
        Long purchasePackageId
    ) {
        MmPurchasePackageEntity MmPurchasePackageEntity = mmPurchasePackageRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            purchasePackageId,
            EntityStatus.ACTIVE
        );
        if (MmPurchasePackageEntity == null) {
            throw new BusinessError("Purchase package does not exist!采购包不存在！");
        }
        return MmPurchasePackageEntity;
    }

    /**
     * 删除采购包。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     * @param contextDTO
     */
    @Override
    public void delete(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        ContextDTO contextDTO
    ) {
        MmPurchasePackageEntity MmPurchasePackageEntity = mmPurchasePackageRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            purchasePackageId,
            EntityStatus.ACTIVE
        );
        if (MmPurchasePackageEntity == null) {
            throw new BusinessError("Purchase package does not exist!采购包不存在！");
        }

        MmPurchasePackageEntity.setLastModifiedAt(new Date());
        MmPurchasePackageEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        MmPurchasePackageEntity.setStatus(EntityStatus.DELETED);
        MmPurchasePackageEntity.setDeleted(true);
        MmPurchasePackageEntity.setDeletedAt(new Date());
        MmPurchasePackageEntity.setDeletedBy(contextDTO.getOperator().getId());
        mmPurchasePackageRepository.save(MmPurchasePackageEntity);
    }

    /**
     * 添加供货商。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId,
     * @param mmPurchasePackageVendorAddDTO
     * @return
     */
    @Override
    public void addVendor(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageVendorAddDTO mmPurchasePackageVendorAddDTO,
        ContextDTO contextDTO
    ) {

        MmPurchasePackageEntity mmPurchasePackageEntity = mmPurchasePackageRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, purchasePackageId, EntityStatus.ACTIVE);
        if (mmPurchasePackageEntity == null) {
            throw new BusinessError("Purchase package does not exist!采购包不存在！");
        }

        if (null != mmPurchasePackageVendorAddDTO.getVendorIds()) {
            for (Long vendorId : mmPurchasePackageVendorAddDTO.getVendorIds()) {
                MmVendorEntity mmVendorEntity = mmVendorRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, vendorId, EntityStatus.ACTIVE);
                if (null != mmVendorEntity) {
                    // 如果存在历史关系表中，则更新操作
                    MmPurchasePackageVendorRelationEntity findMmPurchasePackageVendorRelationEntity = mmPurchasePackageVendorRelationRepository.findByOrgIdAndProjectIdAndPurchasePackageIdAndIdAndStatus(
                        orgId,
                        projectId,
                        purchasePackageId,
                        vendorId,
                        EntityStatus.ACTIVE
                    );
                    if (null == findMmPurchasePackageVendorRelationEntity) {
                        MmPurchasePackageVendorRelationEntity mmPurchasePackageVendorRelationEntity = new MmPurchasePackageVendorRelationEntity();
                        mmPurchasePackageVendorRelationEntity.setPurchasePackageId(purchasePackageId);
                        mmPurchasePackageVendorRelationEntity.setVendorId(vendorId);
                        mmPurchasePackageVendorRelationEntity.setOrgId(orgId);
                        mmPurchasePackageVendorRelationEntity.setProjectId(projectId);
                        mmPurchasePackageVendorRelationEntity.setCreatedAt(new Date());
                        mmPurchasePackageVendorRelationEntity.setCreatedBy(contextDTO.getOperator().getId());
                        mmPurchasePackageVendorRelationEntity.setLastModifiedAt(new Date());
                        mmPurchasePackageVendorRelationEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                        mmPurchasePackageVendorRelationEntity.setStatus(EntityStatus.ACTIVE);
                        mmPurchasePackageVendorRelationRepository.save(mmPurchasePackageVendorRelationEntity);
                    }
                }
            }
        } else {
            throw new BusinessError("No supplier selected!没有选择供货商！");
        }
    }


    /**
     * 查找采购包已添加供货商。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId,
     * @param mmPurchasePackageVendorSearchDTO
     * @return
     */
    @Override
    public Page<MmPurchasePackageVendorReturnDTO> searchVendor(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageVendorSearchDTO mmPurchasePackageVendorSearchDTO
    ) {
        return mmPurchasePackageVendorRelationRepository.search(
            orgId,
            projectId,
            purchasePackageId,
            mmPurchasePackageVendorSearchDTO
        );
    }

    /**
     * 修改供货商。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId,
     * @param mmPurchasePackageVendorAddDTO
     * @return
     */
    @Override
    public void updateVendor(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        Long purchasePackageVendorRelationId,
        MmPurchasePackageVendorAddDTO mmPurchasePackageVendorAddDTO,
        ContextDTO contextDTO
    ) {

        MmPurchasePackageEntity mmPurchasePackageEntity = mmPurchasePackageRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, purchasePackageId, EntityStatus.ACTIVE);
        if (mmPurchasePackageEntity == null) {
            throw new BusinessError("Purchase package does not exist!采购包不存在！");
        }
        MmPurchasePackageVendorRelationEntity entity = mmPurchasePackageVendorRelationRepository.findByOrgIdAndProjectIdAndPurchasePackageIdAndIdAndStatus(
            orgId,
            projectId,
            purchasePackageId,
            purchasePackageVendorRelationId,
            EntityStatus.ACTIVE
        );
        if (entity != null) {
            if (mmPurchasePackageVendorAddDTO.getConfirmStatus() != null) {
                entity.setConfirmStatus(EntityStatus.valueOf(mmPurchasePackageVendorAddDTO.getConfirmStatus()));
                entity.setRemark(mmPurchasePackageVendorAddDTO.getRemark());
            }
            if (mmPurchasePackageVendorAddDTO.getBidStatus() != null) {
                entity.setBidStatus(mmPurchasePackageVendorAddDTO.getBidStatus());
            }
            if (mmPurchasePackageVendorAddDTO.getScore() != null) {
                entity.setScore(mmPurchasePackageVendorAddDTO.getScore());
            }
            if (mmPurchasePackageVendorAddDTO.getFeedBack() != null) {
                entity.setFeedBack(mmPurchasePackageVendorAddDTO.getFeedBack());
            }
            if (mmPurchasePackageVendorAddDTO.getReview() != null) {
                entity.setReview(mmPurchasePackageVendorAddDTO.getReview());
            }
            entity.setLastModifiedBy(contextDTO.getOperator().getId());
            entity.setLastModifiedAt();
            mmPurchasePackageVendorRelationRepository.save(entity);
        }
    }

    /**
     * 删除采购包中的供货商。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     */
    public void deleteVendor(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        Long purchasePackageVendorRelationId,
        ContextDTO contextDTO
    ) {
        MmPurchasePackageEntity MmPurchasePackageEntity = mmPurchasePackageRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            purchasePackageId,
            EntityStatus.ACTIVE
        );
        if (MmPurchasePackageEntity == null) {
            throw new BusinessError("Purchase package does not exist!采购包不存在！");
        }
        // 如果存在历史关系表中，则更新操作
        MmPurchasePackageVendorRelationEntity findMmPurchasePackageVendorRelationEntity = mmPurchasePackageVendorRelationRepository.findByOrgIdAndProjectIdAndPurchasePackageIdAndIdAndStatus(
            orgId,
            projectId,
            purchasePackageId,
            purchasePackageVendorRelationId,
            EntityStatus.ACTIVE
        );
        if (null != findMmPurchasePackageVendorRelationEntity) {
            findMmPurchasePackageVendorRelationEntity.setLastModifiedAt(new Date());
            findMmPurchasePackageVendorRelationEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            findMmPurchasePackageVendorRelationEntity.setStatus(EntityStatus.DELETED);
            mmPurchasePackageVendorRelationRepository.save(findMmPurchasePackageVendorRelationEntity);
        } else {
            throw new BusinessError("The supplier does not exist in the current purchase package!供货商不存在于当前采购包中！");
        }
    }

    /**
     * 查询采购包相关的实体子类列表。
     *
     * @param orgId
     * @param projectId
     * @param entityType
     * @return
     */
    @Override
    public List<MmPurchasePackageEntitySubTypeDTO> searchEntitySubType(
        Long orgId,
        Long projectId,
        String entityType
    ) {
        List<Map<String, Object>> entitySubTypeList = mmPurchasePackageRepository.findEntitySubTypeByEntityTypeAndProjectId(orgId, projectId, entityType);
        List<MmPurchasePackageEntitySubTypeDTO> dtos = new ArrayList<>();
        if (entitySubTypeList.size() > 0) {
            for (Map<String, Object> map : entitySubTypeList) {
                MmPurchasePackageEntitySubTypeDTO dto = new MmPurchasePackageEntitySubTypeDTO();
                dto.setId(Long.valueOf(map.get("id").toString()));
                dto.setNameCn(map.get("nameCn").toString());
                dto.setNameEn(map.get("nameEn").toString());
                dtos.add(dto);
            }
        }
        return dtos;
    }
}
