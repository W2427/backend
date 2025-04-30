package com.ose.material.domain.model.service.impl;

import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.material.domain.model.repository.MmMaterialCodeRepository;
import com.ose.material.domain.model.service.MmMaterialCodeCompanyInterface;
import com.ose.material.dto.MmMaterialCodeCreateDTO;
import com.ose.material.dto.MmMaterialCodeSearchDTO;
import com.ose.material.dto.SeqNumberDTO;
import com.ose.material.entity.MmMaterialCodeEntity;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.vo.EntityStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MmMaterialCodeCompanyService implements MmMaterialCodeCompanyInterface {

    /**
     * 材料编码  操作仓库。
     */
    private final MmMaterialCodeRepository mmMaterialCodeRepository;


    /**
     * 构造方法。
     *
     * @param mmMaterialCodeRepository
     */
    @Autowired
    public MmMaterialCodeCompanyService(
        MmMaterialCodeRepository mmMaterialCodeRepository
    ) {
        this.mmMaterialCodeRepository = mmMaterialCodeRepository;
    }

    /**
     * 查询材料编码列表。
     *
     * @param companyId
     * @param mmMaterialCodeSearchDTO
     * @return
     */
    @Override
    public Page<MmMaterialCodeEntity> search(
        Long companyId,
        MmMaterialCodeSearchDTO mmMaterialCodeSearchDTO
    ) {
        if (mmMaterialCodeSearchDTO.getKeyword() != null) {
            return mmMaterialCodeRepository.findByCompanyIdAndNoLikeAndMaterialOrganizationTypeAndStatusOrderByNo(
                companyId,
                "%" + mmMaterialCodeSearchDTO.getKeyword() + "%",
                MaterialOrganizationType.COMPANY,
                EntityStatus.ACTIVE,
                mmMaterialCodeSearchDTO.toPageable()
            );
        } else if (mmMaterialCodeSearchDTO.getAddStatus() != null && !"全部".equals(mmMaterialCodeSearchDTO.getAddStatus())) {
            switch (mmMaterialCodeSearchDTO.getAddStatus()) {
                case "未添加":
                    return mmMaterialCodeRepository.getNoAdd(
                        companyId,
                        mmMaterialCodeSearchDTO.getOrgId(),
                        mmMaterialCodeSearchDTO.getProjectId(),
                        mmMaterialCodeSearchDTO.toPageable()
                    );
                case "已添加":
                    return mmMaterialCodeRepository.findByOrgIdAndProjectIdAndMaterialOrganizationTypeAndStatusOrderByIdentCode(
                        mmMaterialCodeSearchDTO.getOrgId(),
                        mmMaterialCodeSearchDTO.getProjectId(),
                        MaterialOrganizationType.PROJECT,
                        EntityStatus.ACTIVE,
                        mmMaterialCodeSearchDTO.toPageable()
                    );
            }
        } else {
            return mmMaterialCodeRepository.findByCompanyIdAndMaterialOrganizationTypeAndStatusOrderByNo(
                companyId,
                MaterialOrganizationType.COMPANY,
                EntityStatus.ACTIVE,
                mmMaterialCodeSearchDTO.toPageable()
            );
        }
        return null;
    }

    /**
     * 创建材料编码。
     *
     * @param companyId
     * @param mmMaterialCodeCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmMaterialCodeEntity create(
        Long companyId,
        MmMaterialCodeCreateDTO mmMaterialCodeCreateDTO,
        ContextDTO contextDTO
    ) {

        MmMaterialCodeEntity oldMmMaterialCodeEntity = mmMaterialCodeRepository.findByCompanyIdAndNoAndMaterialOrganizationTypeAndStatus(
            companyId,
            mmMaterialCodeCreateDTO.getNo(),
            mmMaterialCodeCreateDTO.getMaterialOrganizationType(),
            EntityStatus.ACTIVE
        );

        if (oldMmMaterialCodeEntity != null) {
            throw new BusinessError("Material Code name already exists!材料编码名称已经存在！");
        }

        MmMaterialCodeEntity mmMaterialCodeEntity = new MmMaterialCodeEntity();

        BeanUtils.copyProperties(mmMaterialCodeCreateDTO, mmMaterialCodeEntity,"orgId","projectId");
        mmMaterialCodeEntity.setCreatedAt(new Date());
        mmMaterialCodeEntity.setCreatedBy(contextDTO.getOperator().getId());
        mmMaterialCodeEntity.setLastModifiedAt(new Date());
        mmMaterialCodeEntity.setCompanyId(companyId);
        mmMaterialCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmMaterialCodeEntity.setStatus(EntityStatus.ACTIVE);

        SeqNumberDTO seqNumberDTO = mmMaterialCodeRepository.getMaxSeqNumber(companyId, MaterialOrganizationType.COMPANY).orElse(new SeqNumberDTO(0));
        int seqNumber = seqNumberDTO.getNewSeqNumber();
        mmMaterialCodeEntity.setSeqNumber(seqNumber);
//        mmMaterialCodeEntity.setNo(String.format("%s-%s-%04d", MaterialPrefixType.MATERIAL_TAG_NUMBER.getCode(), WareHouseType.COMPANY, seqNumber));

        return mmMaterialCodeRepository.save(mmMaterialCodeEntity);
    }

    /**
     * 更新材料编码。
     *
     * @param companyId
     * @param tagNumberId
     * @param mmMaterialCodeCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmMaterialCodeEntity update(
        Long companyId,
        Long tagNumberId,
        MmMaterialCodeCreateDTO mmMaterialCodeCreateDTO,
        ContextDTO contextDTO
    ) {
        // 找到公司级材料编码
        MmMaterialCodeEntity oldMmMaterialCodeEntity = mmMaterialCodeRepository.findByCompanyIdAndIdAndStatus(
            companyId,
            tagNumberId,
            EntityStatus.ACTIVE
        );
        if (oldMmMaterialCodeEntity == null) {
            throw new BusinessError("Material Code does not exist!材料编码信息不存在！");
        }

        // 查找是否存在此编码的材料编码
        MmMaterialCodeEntity mmMaterialCodeEntity = mmMaterialCodeRepository.findByCompanyIdAndNoAndMaterialOrganizationTypeAndStatus(
            companyId,
            mmMaterialCodeCreateDTO.getNo(),
            mmMaterialCodeCreateDTO.getMaterialOrganizationType(),
            EntityStatus.ACTIVE
        );

        if (mmMaterialCodeEntity != null && !mmMaterialCodeEntity.getId().equals(oldMmMaterialCodeEntity.getId())) {
            throw new BusinessError("Material Code name already exists!材料编码名称已经存在！");
        } else {
            // 修改项目中材料编码的名称
            MmMaterialCodeEntity mmProjectMaterialCodeEntity = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndNoAndMaterialOrganizationTypeAndStatus(
                mmMaterialCodeCreateDTO.orgId,
                mmMaterialCodeCreateDTO.projectId,
                oldMmMaterialCodeEntity.getNo(),
                MaterialOrganizationType.PROJECT,
                EntityStatus.ACTIVE
            );
            if (mmProjectMaterialCodeEntity != null) {
                BeanUtils.copyProperties(mmMaterialCodeCreateDTO, mmProjectMaterialCodeEntity,"orgId","projectId","companyId","needSpec","materialOrganizationType");
                mmProjectMaterialCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                mmProjectMaterialCodeEntity.setLastModifiedAt();
                mmMaterialCodeRepository.save(mmProjectMaterialCodeEntity);
            }
        }

        BeanUtils.copyProperties(mmMaterialCodeCreateDTO, oldMmMaterialCodeEntity,"orgId","projectId","companyId","needSpec","materialOrganizationType");
        oldMmMaterialCodeEntity.setLastModifiedAt(new Date());
        oldMmMaterialCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        oldMmMaterialCodeEntity.setStatus(EntityStatus.ACTIVE);

        return mmMaterialCodeRepository.save(oldMmMaterialCodeEntity);
    }

    /**
     * 材料编码详情。
     *
     * @param companyId
     * @param tagNumberId
     * @return
     */
    @Override
    public MmMaterialCodeEntity detail(
        Long companyId,
        Long tagNumberId
    ) {
        MmMaterialCodeEntity mmMaterialCodeEntity = mmMaterialCodeRepository.findByCompanyIdAndIdAndStatus(
            companyId,
            tagNumberId,
            EntityStatus.ACTIVE
        );
        if (mmMaterialCodeEntity == null) {
            throw new BusinessError("Material Code does not exist!材料编码信息不存在！");
        }
        return mmMaterialCodeEntity;
    }

    /**
     * 删除材料编码。
     *
     * @param companyId
     * @param tagNumberId
     * @param contextDTO
     */
    @Override
    public void delete(
        Long companyId,
        Long tagNumberId,
        MmMaterialCodeCreateDTO mmMaterialCodeCreateDTO,
        ContextDTO contextDTO
    ) {
        MmMaterialCodeEntity mmMaterialCodeEntity = mmMaterialCodeRepository.findByCompanyIdAndIdAndStatus(
            companyId,
            tagNumberId,
            EntityStatus.ACTIVE
        );
        if (mmMaterialCodeEntity == null) {
            throw new BusinessError("Material Code does not exist!材料编码信息不存在！");
        }

        List<MmMaterialCodeEntity> mmMaterialCodeEntityList = mmMaterialCodeRepository.findByProjectIdAndNoAndStatus(
            mmMaterialCodeCreateDTO.getProjectId(),
            mmMaterialCodeEntity.getNo(),
            EntityStatus.ACTIVE
        );

        if (mmMaterialCodeEntityList.size() > 0) {
            throw new BusinessError("The material code under this company has been linked to the material code information in the project!" +
                "该公司下的材料编码已关联项目中的材料编码信息！");
        }

        mmMaterialCodeEntity.setLastModifiedAt(new Date());
        mmMaterialCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmMaterialCodeEntity.setStatus(EntityStatus.DELETED);
        mmMaterialCodeEntity.setDeleted(true);
        mmMaterialCodeEntity.setDeletedAt(new Date());
        mmMaterialCodeEntity.setDeletedBy(contextDTO.getOperator().getId());
        mmMaterialCodeRepository.save(mmMaterialCodeEntity);
    }
}
