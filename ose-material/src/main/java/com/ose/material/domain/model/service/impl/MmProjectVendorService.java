package com.ose.material.domain.model.service.impl;

import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.material.domain.model.repository.MmVendorRepository;
import com.ose.material.domain.model.service.MmProjectVendorInterface;
import com.ose.material.dto.MmProjectVendorCreateDTO;
import com.ose.material.dto.MmVendorSearchDTO;
import com.ose.material.dto.SeqNumberDTO;
import com.ose.material.entity.MmVendorEntity;
import com.ose.vo.EntityStatus;
import com.ose.vo.SupplierLevel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MmProjectVendorService implements MmProjectVendorInterface {

    /**
     * 项目级供货商  操作仓库。
     */
    private final MmVendorRepository mmVendorRepository;

    /**
     * 构造方法。
     *
     * @param mmVendorRepository
     */
    @Autowired
    public MmProjectVendorService(
        MmVendorRepository mmVendorRepository
    ) {
        this.mmVendorRepository = mmVendorRepository;
    }

    /**
     * 查询项目级供货商列表。
     *
     * @param orgId
     * @param projectId
     * @param mmVendorSearchDTO
     * @return
     */
    @Override
    public Page<MmVendorEntity> search(
        Long orgId,
        Long projectId,
        MmVendorSearchDTO mmVendorSearchDTO
    ) {
        if (mmVendorSearchDTO.getKeyword() != null) {
            return mmVendorRepository.findByOrgIdAndProjectIdAndSupplierCodeLikeAndStatus(
                orgId,
                projectId,
                "%" + mmVendorSearchDTO.getKeyword() + "%",
                EntityStatus.ACTIVE,
                mmVendorSearchDTO.toPageable()
            );
        } else {
            return mmVendorRepository.findByOrgIdAndProjectIdAndStatus(
                orgId,
                projectId,
                EntityStatus.ACTIVE,
                mmVendorSearchDTO.toPageable()
            );
        }
    }

    /**
     * 创建供货商。
     *
     * @param orgId
     * @param projectId
     * @param mmProjectVendorCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public void create(
        Long orgId,
        Long projectId,
        MmProjectVendorCreateDTO mmProjectVendorCreateDTO,
        ContextDTO contextDTO
    ) {

        if (null == mmProjectVendorCreateDTO.getVendorIds()) {
            throw new BusinessError("没有选择公司级供应商");
        }

        if (null == mmProjectVendorCreateDTO.getCompanyId()) {
            throw new BusinessError("没有传输公司ID");
        }

        for (Long vendorId : mmProjectVendorCreateDTO.getVendorIds()) {
            MmVendorEntity companyVendor = mmVendorRepository.findByCompanyIdAndIdAndStatus(mmProjectVendorCreateDTO.getCompanyId(), vendorId, EntityStatus.ACTIVE);
            if (null == companyVendor) {
                throw new BusinessError("公司级供应商不存在");
            }
            // 查找是否存在于当前项目
            MmVendorEntity mmFindProjectVendorEntity = mmVendorRepository.findByOrgIdAndProjectIdAndSupplierCodeAndStatus(orgId, projectId, companyVendor.getSupplierCode(), EntityStatus.ACTIVE);
            if (null != mmFindProjectVendorEntity) {
                continue;
            }

            MmVendorEntity mmVendorEntity = new MmVendorEntity();
            BeanUtils.copyProperties(companyVendor, mmVendorEntity, "id", "companyId");
            mmVendorEntity.setCompanyVendorId(companyVendor.getId());
            mmVendorEntity.setOrgId(orgId);
            mmVendorEntity.setProjectId(projectId);
            mmVendorEntity.setCreatedAt(new Date());
            mmVendorEntity.setCreatedBy(contextDTO.getOperator().getId());
            mmVendorEntity.setLastModifiedAt(new Date());
            mmVendorEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            mmVendorEntity.setSupplierLevel(SupplierLevel.PROJECT);
            mmVendorEntity.setStatus(EntityStatus.ACTIVE);
            SeqNumberDTO seqNumberDTO = mmVendorRepository.getMaxSeqNumber(orgId, projectId).orElse(new SeqNumberDTO(0));
            int seqNumber = seqNumberDTO.getNewSeqNumber();
            mmVendorEntity.setSeqNumber(seqNumber);
            mmVendorRepository.save(mmVendorEntity);
        }
    }

    /**
     * 供货商详情。
     *
     * @param orgId
     * @param projectId
     * @param vendorId
     * @return
     */
    @Override
    public MmVendorEntity detail(
        Long orgId,
        Long projectId,
        Long vendorId
    ) {
        MmVendorEntity mmVendorEntity = mmVendorRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            vendorId,
            EntityStatus.ACTIVE
        );
        if (mmVendorEntity == null) {
            throw new BusinessError("供货商不存在");
        }
        return mmVendorEntity;
    }

    /**
     * 删除供货商。
     *
     * @param orgId
     * @param projectId
     * @param vendorId
     * @param contextDTO
     */
    @Override
    public void delete(
        Long orgId,
        Long projectId,
        Long vendorId,
        ContextDTO contextDTO
    ) {
        MmVendorEntity mmVendorEntity = mmVendorRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            vendorId,
            EntityStatus.ACTIVE
        );
        if (mmVendorEntity == null) {
            throw new BusinessError("供货商不存在");
        }

        mmVendorEntity.setLastModifiedAt(new Date());
        mmVendorEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmVendorEntity.setStatus(EntityStatus.DELETED);
        mmVendorEntity.setDeleted(true);
        mmVendorEntity.setDeletedAt(new Date());
        mmVendorEntity.setDeletedBy(contextDTO.getOperator().getId());
        mmVendorRepository.save(mmVendorEntity);
    }
}
