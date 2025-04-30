package com.ose.material.domain.model.service.impl;

import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.material.domain.model.repository.MmMaterialCodeRepository;
import com.ose.material.domain.model.repository.MmMaterialCodeTypeProjectRepository;
import com.ose.material.domain.model.service.MmMaterialCodeTypeProjectInterface;
import com.ose.material.dto.MmMaterialCodeTypeCreateDTO;
import com.ose.material.dto.MmMaterialCodeTypeSearchDTO;
import com.ose.material.dto.SeqNumberDTO;
import com.ose.material.entity.MmMaterialCodeEntity;
import com.ose.material.entity.MmMaterialCodeTypeEntity;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.vo.EntityStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MmMaterialCodeTypeProjectService implements MmMaterialCodeTypeProjectInterface {

    /**
     * 材料编码类型。
     */
    private final MmMaterialCodeTypeProjectRepository mmMaterialCodeTypeProjectRepository;

    private final MmMaterialCodeRepository mmMaterialCodeRepository;

    /**
     * 构造方法。
     *
     * @param mmMaterialCodeTypeProjectRepository
     */
    @Autowired
    public MmMaterialCodeTypeProjectService(
        MmMaterialCodeTypeProjectRepository mmMaterialCodeTypeProjectRepository,
        MmMaterialCodeRepository mmMaterialCodeRepository
    ) {
        this.mmMaterialCodeTypeProjectRepository = mmMaterialCodeTypeProjectRepository;
        this.mmMaterialCodeRepository = mmMaterialCodeRepository;
    }

    /**
     * 查询材料编码类型列表。
     *
     * @param orgId
     * @param projectId
     * @param mmMaterialCodeTypeSearchDTO
     * @return
     */
    @Override
    public Page<MmMaterialCodeTypeEntity> search(
        Long orgId,
        Long projectId,
        MmMaterialCodeTypeSearchDTO mmMaterialCodeTypeSearchDTO
    ) {
        if (mmMaterialCodeTypeSearchDTO.getKeyword() != null) {
            return mmMaterialCodeTypeProjectRepository.findByOrgIdAndProjectIdAndNameLikeAndStatus(
                orgId,
                projectId,
                "%" + mmMaterialCodeTypeSearchDTO.getKeyword() + "%",
                EntityStatus.ACTIVE,
                mmMaterialCodeTypeSearchDTO.toPageable()
            );
        } else {
            return mmMaterialCodeTypeProjectRepository.findByOrgIdAndProjectIdAndStatus(
                orgId,
                projectId,
                EntityStatus.ACTIVE,
                mmMaterialCodeTypeSearchDTO.toPageable()
            );
        }
    }

    /**
     * 创建材料编码类型。
     *
     * @param orgId
     * @param projectId
     * @param mmMaterialCodeTypeCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmMaterialCodeTypeEntity create(
        Long orgId,
        Long projectId,
        MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO,
        ContextDTO contextDTO
    ) {

        MmMaterialCodeTypeEntity oldMmMaterialCodeTypeEntity = mmMaterialCodeTypeProjectRepository.findByOrgIdAndProjectIdAndNameAndStatus(
            orgId,
            projectId,
            mmMaterialCodeTypeCreateDTO.getName(),
            EntityStatus.ACTIVE
        );

        if (oldMmMaterialCodeTypeEntity != null) {
            throw new BusinessError("材料编码类型名称已经存在");
        }

        MmMaterialCodeTypeEntity MMMaterialCodeTypeEntity = new MmMaterialCodeTypeEntity();

        BeanUtils.copyProperties(mmMaterialCodeTypeCreateDTO, MMMaterialCodeTypeEntity);
        MMMaterialCodeTypeEntity.setOrgId(orgId);
        MMMaterialCodeTypeEntity.setProjectId(projectId);
        MMMaterialCodeTypeEntity.setCreatedAt(new Date());
        MMMaterialCodeTypeEntity.setCreatedBy(contextDTO.getOperator().getId());
        MMMaterialCodeTypeEntity.setLastModifiedAt(new Date());
        MMMaterialCodeTypeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        MMMaterialCodeTypeEntity.setStatus(EntityStatus.ACTIVE);

        SeqNumberDTO seqNumberDTO = mmMaterialCodeTypeProjectRepository.getMaxSeqNumber(orgId, projectId, MaterialOrganizationType.valueOf(mmMaterialCodeTypeCreateDTO.getWareHouseType().toString())).orElse(new SeqNumberDTO(0));
        int seqNumber = seqNumberDTO.getNewSeqNumber();
        MMMaterialCodeTypeEntity.setSeqNumber(seqNumber);
//        MMWareHouseEntity.setNo(String.format("%s-%s-%04d", MaterialPrefixType.MATERIAL_WARE_HOUSE.getCode(), mmWareHouseCreateDTO.getWareHouseType().toString(), seqNumber));

        return mmMaterialCodeTypeProjectRepository.save(MMMaterialCodeTypeEntity);
    }

    /**
     * 更新材料编码类型。
     *
     * @param orgId
     * @param projectId
     * @param wareHouseId
     * @param mmMaterialCodeTypeCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmMaterialCodeTypeEntity update(
        Long orgId,
        Long projectId,
        Long wareHouseId,
        MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO,
        ContextDTO contextDTO
    ) {
        MmMaterialCodeTypeEntity oldMmMaterialCodeTypeEntity = mmMaterialCodeTypeProjectRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            wareHouseId,
            EntityStatus.ACTIVE
        );
        if (oldMmMaterialCodeTypeEntity == null) {
            throw new BusinessError("材料编码类型信息不存在");
        }

        MmMaterialCodeTypeEntity MMMaterialCodeTypeEntity = mmMaterialCodeTypeProjectRepository.findByOrgIdAndProjectIdAndNameAndStatus(
            orgId,
            projectId,
            mmMaterialCodeTypeCreateDTO.getName(),
            EntityStatus.ACTIVE
        );

        if (MMMaterialCodeTypeEntity != null && !MMMaterialCodeTypeEntity.getId().equals(oldMmMaterialCodeTypeEntity.getId())) {
            throw new BusinessError("材料编码类型名称已经存在");
        }

        BeanUtils.copyProperties(mmMaterialCodeTypeCreateDTO, oldMmMaterialCodeTypeEntity);
        oldMmMaterialCodeTypeEntity.setOrgId(orgId);
        oldMmMaterialCodeTypeEntity.setProjectId(projectId);
        oldMmMaterialCodeTypeEntity.setLastModifiedAt(new Date());
        oldMmMaterialCodeTypeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        oldMmMaterialCodeTypeEntity.setStatus(EntityStatus.ACTIVE);

        return mmMaterialCodeTypeProjectRepository.save(oldMmMaterialCodeTypeEntity);
    }

    /**
     * 材料编码类型详情。
     *
     * @param orgId
     * @param projectId
     * @param wareHouseId
     * @return
     */
    @Override
    public MmMaterialCodeTypeEntity detail(
        Long orgId,
        Long projectId,
        Long wareHouseId
    ) {
        MmMaterialCodeTypeEntity MMMaterialCodeTypeEntity = mmMaterialCodeTypeProjectRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            wareHouseId,
            EntityStatus.ACTIVE
        );
        if (MMMaterialCodeTypeEntity == null) {
            throw new BusinessError("材料编码类型不存在");
        }
        return MMMaterialCodeTypeEntity;
    }

    /**
     * 删除材料编码类型。
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
        MmMaterialCodeTypeEntity MMMaterialCodeTypeEntity = mmMaterialCodeTypeProjectRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            wareHouseId,
            EntityStatus.ACTIVE
        );
        if (MMMaterialCodeTypeEntity == null) {
            throw new BusinessError("WareHouse does not exist!材料编码类型不存在！");
        }
        // TODO
//        List<MmMaterialCodeEntity> mmMaterialCodeEntityList = mmMaterialCodeRepository.findByMmWareHouseIdAndStatus(wareHouseId, EntityStatus.ACTIVE);
//        if (mmMaterialCodeEntityList.size() > 0) {
//            throw new BusinessError("There is already a material code under this warehouse and it cannot be deleted!该材料编码类型下已有材料编码，无法删除！");
//        }

        MMMaterialCodeTypeEntity.setLastModifiedAt(new Date());
        MMMaterialCodeTypeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        MMMaterialCodeTypeEntity.setStatus(EntityStatus.DELETED);
        MMMaterialCodeTypeEntity.setDeleted(true);
        MMMaterialCodeTypeEntity.setDeletedAt(new Date());
        MMMaterialCodeTypeEntity.setDeletedBy(contextDTO.getOperator().getId());
        mmMaterialCodeTypeProjectRepository.save(MMMaterialCodeTypeEntity);
    }

    /**
     * 查询某类材料编码类型列表。
     *
     * @param orgId
     * @param projectId
     * @param materialOrganizationType
     * @return
     */
    @Override
    public List<MmMaterialCodeTypeEntity> searchType(
        Long orgId,
        Long projectId,
        String materialOrganizationType
    ) {
        if (materialOrganizationType != null) {
            List<MmMaterialCodeTypeEntity> mmWareHouseEntities = mmMaterialCodeTypeProjectRepository.findByOrgIdAndProjectIdAndMaterialOrganizationTypeAndStatus(orgId, projectId, MaterialOrganizationType.valueOf(materialOrganizationType), EntityStatus.ACTIVE);
            return mmWareHouseEntities;
        }
        return null;
    }
}
