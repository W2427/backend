package com.ose.material.domain.model.service.impl;

import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.material.domain.model.repository.MmMaterialCodeRepository;
import com.ose.material.domain.model.repository.MmMaterialCodeTypeProjectRepository;
import com.ose.material.domain.model.service.MmMaterialCodeTypeCompanyInterface;
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
public class MmMaterialCodeTypeCompanyService implements MmMaterialCodeTypeCompanyInterface {

    /**
     * 公司仓库  操作仓库。
     */
    private final MmMaterialCodeTypeProjectRepository mmMaterialCodeTypeProjectRepository;

    private final MmMaterialCodeRepository mmMaterialCodeRepository;

    /**
     * 构造方法。
     *
     * @param mmMaterialCodeTypeProjectRepository
     */
    @Autowired
    public MmMaterialCodeTypeCompanyService(
        MmMaterialCodeTypeProjectRepository mmMaterialCodeTypeProjectRepository,
        MmMaterialCodeRepository mmMaterialCodeRepository
    ) {
        this.mmMaterialCodeTypeProjectRepository = mmMaterialCodeTypeProjectRepository;
        this.mmMaterialCodeRepository = mmMaterialCodeRepository;
    }

    /**
     * 查询公司仓库列表。
     *
     * @param companyId
     * @param mmMaterialCodeTypeSearchDTO
     * @return
     */
    @Override
    public Page<MmMaterialCodeTypeEntity> search(
        Long companyId,
        MmMaterialCodeTypeSearchDTO mmMaterialCodeTypeSearchDTO
    ) {
        if (mmMaterialCodeTypeSearchDTO.getKeyword() != null) {
            return mmMaterialCodeTypeProjectRepository.findByCompanyIdAndNameLikeAndMaterialOrganizationTypeAndStatus(
                companyId,
                "%" + mmMaterialCodeTypeSearchDTO.getKeyword() + "%",
                MaterialOrganizationType.COMPANY,
                EntityStatus.ACTIVE,
                mmMaterialCodeTypeSearchDTO.toPageable()
            );
        } else {
            return mmMaterialCodeTypeProjectRepository.findByCompanyIdAndMaterialOrganizationTypeAndStatus(
                companyId,
                MaterialOrganizationType.COMPANY,
                EntityStatus.ACTIVE,
                mmMaterialCodeTypeSearchDTO.toPageable()
            );
        }
    }

    /**
     * 创建公司仓库。
     *
     * @param companyId
     * @param mmMaterialCodeTypeCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmMaterialCodeTypeEntity create(
        Long companyId,
        MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO,
        ContextDTO contextDTO
    ) {

        MmMaterialCodeTypeEntity oldMmMaterialCodeTypeEntity = mmMaterialCodeTypeProjectRepository.findByCompanyIdAndNameAndMaterialOrganizationTypeAndStatus(
            companyId,
            mmMaterialCodeTypeCreateDTO.getName(),
            MaterialOrganizationType.COMPANY,
            EntityStatus.ACTIVE
        );

        if (oldMmMaterialCodeTypeEntity != null) {
            throw new BusinessError("WareHouse name already exists!仓库名称已经存在！");
        }

        MmMaterialCodeTypeEntity MMMaterialCodeTypeEntity = new MmMaterialCodeTypeEntity();

        BeanUtils.copyProperties(mmMaterialCodeTypeCreateDTO, MMMaterialCodeTypeEntity);
        MMMaterialCodeTypeEntity.setCreatedAt(new Date());
        MMMaterialCodeTypeEntity.setCreatedBy(contextDTO.getOperator().getId());
        MMMaterialCodeTypeEntity.setLastModifiedAt(new Date());
        MMMaterialCodeTypeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        MMMaterialCodeTypeEntity.setStatus(EntityStatus.ACTIVE);

        SeqNumberDTO seqNumberDTO = mmMaterialCodeTypeProjectRepository.getMaxSeqNumber(companyId, MaterialOrganizationType.COMPANY).orElse(new SeqNumberDTO(0));
        int seqNumber = seqNumberDTO.getNewSeqNumber();
        MMMaterialCodeTypeEntity.setSeqNumber(seqNumber);
//        MMWareHouseEntity.setNo(String.format("%s-%s-%04d", MaterialPrefixType.MATERIAL_WARE_HOUSE.getCode(), WareHouseType.COMPANY, seqNumber));

        return mmMaterialCodeTypeProjectRepository.save(MMMaterialCodeTypeEntity);
    }

    /**
     * 更新公司仓库。
     *
     * @param companyId
     * @param wareHouseId
     * @param mmMaterialCodeTypeCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmMaterialCodeTypeEntity update(
        Long companyId,
        Long wareHouseId,
        MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO,
        ContextDTO contextDTO
    ) {
        MmMaterialCodeTypeEntity oldMmMaterialCodeTypeEntity = mmMaterialCodeTypeProjectRepository.findByCompanyIdAndIdAndStatus(
            companyId,
            wareHouseId,
            EntityStatus.ACTIVE
        );
        if (oldMmMaterialCodeTypeEntity == null) {
            throw new BusinessError("WareHouse does not exist!仓库不存在！");
        }

        MmMaterialCodeTypeEntity MMMaterialCodeTypeEntity = mmMaterialCodeTypeProjectRepository.findByCompanyIdAndNameAndMaterialOrganizationTypeAndStatus(
            companyId,
            mmMaterialCodeTypeCreateDTO.getName(),
            MaterialOrganizationType.COMPANY,
            EntityStatus.ACTIVE
        );

        if (MMMaterialCodeTypeEntity != null && !MMMaterialCodeTypeEntity.getId().equals(oldMmMaterialCodeTypeEntity.getId())) {
            throw new BusinessError("WareHouse name already exists!仓库名称已经存在！");
        } else {
            // 同时要修改和公司仓库相关的项目仓库名称
            List<MmMaterialCodeTypeEntity> wareHouseEntities = mmMaterialCodeTypeProjectRepository.findByProjectIdAndMaterialOrganizationTypeAndNameAndStatus(
                mmMaterialCodeTypeCreateDTO.projectId,
                MaterialOrganizationType.PROJECT,
                oldMmMaterialCodeTypeEntity.getName(),
                EntityStatus.ACTIVE
            );
            if (wareHouseEntities.size() > 0) {
                for (MmMaterialCodeTypeEntity entity : wareHouseEntities) {
                    entity.setName(mmMaterialCodeTypeCreateDTO.getName());
                    entity.setLastModifiedBy(contextDTO.getOperator().getId());
                    entity.setLastModifiedAt();
                    mmMaterialCodeTypeProjectRepository.save(entity);
                }
            }
            // 同时修改材料编码中的材料编码分类名称
            List<MmMaterialCodeEntity> mmMaterialCodeEntityList = mmMaterialCodeRepository.findByMmMaterialCodeTypeNameAndStatus(oldMmMaterialCodeTypeEntity.getName(), EntityStatus.ACTIVE);
            if (mmMaterialCodeEntityList.size() > 0) {
                for (MmMaterialCodeEntity entity : mmMaterialCodeEntityList) {
                    entity.setMmMaterialCodeTypeName(mmMaterialCodeTypeCreateDTO.getName());
                    entity.setLastModifiedBy(contextDTO.getOperator().getId());
                    entity.setLastModifiedAt();
                    mmMaterialCodeRepository.save(entity);
                }
            }

            BeanUtils.copyProperties(mmMaterialCodeTypeCreateDTO, oldMmMaterialCodeTypeEntity);
            oldMmMaterialCodeTypeEntity.setLastModifiedAt(new Date());
            oldMmMaterialCodeTypeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            oldMmMaterialCodeTypeEntity.setStatus(EntityStatus.ACTIVE);
        }

        return mmMaterialCodeTypeProjectRepository.save(oldMmMaterialCodeTypeEntity);
    }

    /**
     * 公司仓库详情。
     *
     * @param companyId
     * @param wareHouseId
     * @return
     */
    @Override
    public MmMaterialCodeTypeEntity detail(
        Long companyId,
        Long wareHouseId
    ) {
        MmMaterialCodeTypeEntity MMMaterialCodeTypeEntity = mmMaterialCodeTypeProjectRepository.findByCompanyIdAndIdAndStatus(
            companyId,
            wareHouseId,
            EntityStatus.ACTIVE
        );
        if (MMMaterialCodeTypeEntity == null) {
            throw new BusinessError("WareHouse does not exist!仓库不存在！");
        }
        return MMMaterialCodeTypeEntity;
    }

    /**
     * 删除公司仓库。
     *
     * @param companyId
     * @param wareHouseId
     * @param mmMaterialCodeTypeCreateDTO
     * @param contextDTO
     */
    @Override
    public void delete(
        Long companyId,
        Long wareHouseId,
        MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO,
        ContextDTO contextDTO
    ) {
        MmMaterialCodeTypeEntity MMMaterialCodeTypeEntity = mmMaterialCodeTypeProjectRepository.findByCompanyIdAndIdAndStatus(
            companyId,
            wareHouseId,
            EntityStatus.ACTIVE
        );
        if (MMMaterialCodeTypeEntity == null) {
            throw new BusinessError("WareHouse does not exist!仓库不存在！");
        }

//        List<MmMaterialCodeEntity> mmMaterialCodeEntityList = mmMaterialCodeRepository.findByMmWareHouseIdAndStatus(wareHouseId, EntityStatus.ACTIVE);
//        if (mmMaterialCodeEntityList.size() > 0) {
//            throw new BusinessError("There is already a material code under this warehouse and it cannot be deleted!该仓库下已有材料编码，无法删除！");
//        }

//        List<MmWareHouseEntity> wareHouseEntities = mmWareHouseRepository.findByProjectIdAndWareHouseTypeAndNameAndStatus(
//            mmWareHouseCreateDTO.projectId,
//            WareHouseType.PROJECT,
//            MMWareHouseEntity.getName(),
//            EntityStatus.ACTIVE
//        );
//        if (wareHouseEntities.size() > 0) {
//            throw new BusinessError("WareHouse has been created in the project and cannot be deleted!仓库已在项目中创立，无法删除！");
//        }

        MMMaterialCodeTypeEntity.setLastModifiedAt(new Date());
        MMMaterialCodeTypeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        MMMaterialCodeTypeEntity.setStatus(EntityStatus.DELETED);
        MMMaterialCodeTypeEntity.setDeleted(true);
        MMMaterialCodeTypeEntity.setDeletedAt(new Date());
        MMMaterialCodeTypeEntity.setDeletedBy(contextDTO.getOperator().getId());
        mmMaterialCodeTypeProjectRepository.save(MMMaterialCodeTypeEntity);
    }
}
