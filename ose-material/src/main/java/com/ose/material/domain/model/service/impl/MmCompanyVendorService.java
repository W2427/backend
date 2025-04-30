package com.ose.material.domain.model.service.impl;

import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.material.domain.model.repository.MmVendorRepository;
import com.ose.material.domain.model.service.MmCompanyVendorInterface;
import com.ose.material.dto.MmCompanyVendorCreateDTO;
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
import java.util.List;

@Component
public class MmCompanyVendorService implements MmCompanyVendorInterface {

    /**
     * 公司级供货商  操作仓库。
     */
    private final MmVendorRepository mmVendorRepository;

    /**
     * 构造方法。
     *
     * @param mmVendorRepository
     */
    @Autowired
    public MmCompanyVendorService(
        MmVendorRepository mmVendorRepository
    ) {
        this.mmVendorRepository = mmVendorRepository;
    }

    /**
     * 查询公司级供货商列表。
     *
     * @param companyId
     * @param mmVendorSearchDTO
     * @return
     */
    @Override
    public Page<MmVendorEntity> search(
        Long companyId,
        MmVendorSearchDTO mmVendorSearchDTO
    ) {
        if (mmVendorSearchDTO.getKeyword() != null) {
            return mmVendorRepository.findByCompanyIdAndSupplierCodeLikeAndStatus(
                companyId,
                "%" + mmVendorSearchDTO.getKeyword() + "%",
                EntityStatus.ACTIVE,
                mmVendorSearchDTO.toPageable()
            );
        } else {
            return mmVendorRepository.findByCompanyIdAndStatus(
                companyId,
                EntityStatus.ACTIVE,
                mmVendorSearchDTO.toPageable()
            );
        }
    }

    /**
     * 创建公司级供货商。
     *
     * @param companyId
     * @param mmCompanyVendorCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmVendorEntity create(
        Long companyId,
        MmCompanyVendorCreateDTO mmCompanyVendorCreateDTO,
        ContextDTO contextDTO
    ) {

        MmVendorEntity oldMmVendorEntity = mmVendorRepository.findByCompanyIdAndSupplierCodeAndStatus(
            companyId,
            mmCompanyVendorCreateDTO.getSupplierCode(),
            EntityStatus.ACTIVE
        );

        if (oldMmVendorEntity != null) {
            throw new BusinessError("Supplier code already exists!" + "\n" + "供货商已经存在！");
        }

        MmVendorEntity mmVendorEntity = new MmVendorEntity();

        BeanUtils.copyProperties(mmCompanyVendorCreateDTO, mmVendorEntity);
        mmVendorEntity.setCompanyId(companyId);
        mmVendorEntity.setCreatedAt(new Date());
        mmVendorEntity.setCreatedBy(contextDTO.getOperator().getId());
        mmVendorEntity.setLastModifiedAt(new Date());
        mmVendorEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmVendorEntity.setSupplierLevel(SupplierLevel.COMPANY);
        mmVendorEntity.setStatus(EntityStatus.ACTIVE);

        SeqNumberDTO seqNumberDTO = mmVendorRepository.getMaxSeqNumber(companyId).orElse(new SeqNumberDTO(0));
        int seqNumber = seqNumberDTO.getNewSeqNumber();
        mmVendorEntity.setSeqNumber(seqNumber);
        return mmVendorRepository.save(mmVendorEntity);
    }

    /**
     * 更新公司级供货商。
     *
     * @param companyId
     * @param vendorId
     * @param mmCompanyVendorCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmVendorEntity update(
        Long companyId,
        Long vendorId,
        MmCompanyVendorCreateDTO mmCompanyVendorCreateDTO,
        ContextDTO contextDTO
    ) {
        MmVendorEntity oldMmVendorEntity = mmVendorRepository.findByCompanyIdAndIdAndStatus(
            companyId,
            vendorId,
            EntityStatus.ACTIVE
        );
        if (oldMmVendorEntity == null) {
            throw new BusinessError("Supplier information does not exist!" +  "\n" + "供货商信息不存在！");
        }

        MmVendorEntity mmVendorEntity = mmVendorRepository.findByCompanyIdAndSupplierCodeAndStatus(
            companyId,
            mmCompanyVendorCreateDTO.getSupplierCode(),
            EntityStatus.ACTIVE
        );

        if (mmVendorEntity != null && !mmVendorEntity.getId().equals(oldMmVendorEntity.getId())) {
            throw new BusinessError("Supplier code already exists!" + "\n" + "供货商已经存在！");
        }
        // 更新项目级的供货商信息
        List<MmVendorEntity> projectVendorEntities = mmVendorRepository.findByCompanyVendorIdAndStatus(vendorId, EntityStatus.ACTIVE);
        for (MmVendorEntity projectVendorEntity : projectVendorEntities) {
            BeanUtils.copyProperties(mmCompanyVendorCreateDTO, projectVendorEntity);
            projectVendorEntity.setCompanyId(null);
            projectVendorEntity.setLastModifiedAt(new Date());
            projectVendorEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            projectVendorEntity.setSupplierLevel(SupplierLevel.PROJECT);
            projectVendorEntity.setStatus(EntityStatus.ACTIVE);
            mmVendorRepository.save(projectVendorEntity);
        }

        // 更新公司级供货商信息
        BeanUtils.copyProperties(mmCompanyVendorCreateDTO, oldMmVendorEntity);
        oldMmVendorEntity.setCompanyId(companyId);
        oldMmVendorEntity.setLastModifiedAt(new Date());
        oldMmVendorEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        oldMmVendorEntity.setSupplierLevel(SupplierLevel.COMPANY);
        oldMmVendorEntity.setStatus(EntityStatus.ACTIVE);


        return mmVendorRepository.save(oldMmVendorEntity);
    }

    /**
     * 公司级供货商详情。
     *
     * @param companyId
     * @param vendorId
     * @return
     */
    @Override
    public MmVendorEntity detail(
        Long companyId,
        Long vendorId
    ) {
        MmVendorEntity mmVendorEntity = mmVendorRepository.findByCompanyIdAndIdAndStatus(
            companyId,
            vendorId,
            EntityStatus.ACTIVE
        );
        if (mmVendorEntity == null) {
            throw new BusinessError("Supplier information does not exist!" +  "\n" + "供货商信息不存在！");
        }
        return mmVendorEntity;
    }

    /**
     * 删除公司级供货商。
     *
     * @param companyId
     * @param vendorId
     * @param contextDTO
     */
    @Override
    public void delete(
        Long companyId,
        Long vendorId,
        ContextDTO contextDTO
    ) {
        MmVendorEntity mmVendorEntity = mmVendorRepository.findByCompanyIdAndIdAndStatus(
            companyId,
            vendorId,
            EntityStatus.ACTIVE
        );
        if (mmVendorEntity == null) {
            throw new BusinessError("Supplier information does not exist!" +  "\n" + "供货商信息不存在！");
        }

        // 更新项目级的供货商信息
        List<MmVendorEntity> projectVendorEntities = mmVendorRepository.findByCompanyVendorIdAndStatus(vendorId, EntityStatus.ACTIVE);
        for (MmVendorEntity projectVendorEntity : projectVendorEntities) {
            projectVendorEntity.setLastModifiedAt(new Date());
            projectVendorEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            projectVendorEntity.setStatus(EntityStatus.DELETED);
            projectVendorEntity.setDeleted(true);
            projectVendorEntity.setDeletedAt(new Date());
            projectVendorEntity.setDeletedBy(contextDTO.getOperator().getId());
            mmVendorRepository.save(projectVendorEntity);
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
