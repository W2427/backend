package com.ose.material.domain.model.service.impl;

import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.material.domain.model.repository.*;
import com.ose.material.domain.model.service.MmMaterialCodeInterface;
import com.ose.material.dto.MmMaterialCodeCreateDTO;
import com.ose.material.dto.MmMaterialCodeSearchDTO;
import com.ose.material.dto.SeqNumberDTO;
import com.ose.material.entity.*;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.material.vo.QrCodeType;
import com.ose.vo.EntityStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class MmMaterialCodeService implements MmMaterialCodeInterface {

    /**
     * 材料编码  操作仓库。
     */
    private final MmMaterialCodeRepository mmMaterialCodeRepository;
    private final MmMaterialCodeTypeProjectRepository mmMaterialCodeTypeProjectRepository;
    private final MmPurchasePackageItemRepository mmPurchasePackageItemRepository;
    private final MmReleaseReceiveDetailRepository mmReleaseReceiveDetailRepository;
    private final MmShippingDetailRepository mmShippingDetailRepository;

    /**
     * 构造方法。
     *
     * @param mmMaterialCodeRepository
     */
    @Autowired
    public MmMaterialCodeService(
        MmMaterialCodeRepository mmMaterialCodeRepository,
        MmMaterialCodeTypeProjectRepository mmMaterialCodeTypeProjectRepository,
            MmPurchasePackageItemRepository mmPurchasePackageItemRepository,
        MmReleaseReceiveDetailRepository mmReleaseReceiveDetailRepository,
        MmShippingDetailRepository mmShippingDetailRepository

    ) {
        this.mmMaterialCodeRepository = mmMaterialCodeRepository;
        this.mmMaterialCodeTypeProjectRepository = mmMaterialCodeTypeProjectRepository;
        this.mmPurchasePackageItemRepository = mmPurchasePackageItemRepository;
        this.mmReleaseReceiveDetailRepository = mmReleaseReceiveDetailRepository;
        this.mmShippingDetailRepository = mmShippingDetailRepository;
    }

    /**
     * 查询材料编码列表。
     *
     * @param orgId
     * @param projectId
     * @param mmMaterialCodeSearchDTO
     * @return
     */
    @Override
    public Page<MmMaterialCodeEntity> search(
        Long orgId,
        Long projectId,
        MmMaterialCodeSearchDTO mmMaterialCodeSearchDTO
    ) {
        if (mmMaterialCodeSearchDTO.getKeyword() != null) {
            Page<MmMaterialCodeEntity> MmMaterialCodeEntityNoList = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndNoLikeAndMaterialOrganizationTypeAndStatusOrderByIdentCode(
                orgId,
                projectId,
                "%" + mmMaterialCodeSearchDTO.getKeyword() + "%",
                MaterialOrganizationType.PROJECT,
                EntityStatus.ACTIVE,
                mmMaterialCodeSearchDTO.toPageable()
            );
            Page<MmMaterialCodeEntity> MmMaterialCodeEntityDescriptionList = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndDescriptionLikeAndMaterialOrganizationTypeAndStatusOrderByIdentCode(
                orgId,
                projectId,
                "%" + mmMaterialCodeSearchDTO.getKeyword() + "%",
                MaterialOrganizationType.PROJECT,
                EntityStatus.ACTIVE,
                mmMaterialCodeSearchDTO.toPageable()
            );
            if (MmMaterialCodeEntityNoList.getContent().size() > 0) {
                return MmMaterialCodeEntityNoList;
            } else if (MmMaterialCodeEntityDescriptionList.getContent().size() > 0) {
                return MmMaterialCodeEntityDescriptionList;
            } else {
                return null;
            }
        } else {
            return mmMaterialCodeRepository.findByOrgIdAndProjectIdAndMaterialOrganizationTypeAndStatusOrderByIdentCode(
                orgId,
                projectId,
                MaterialOrganizationType.PROJECT,
                EntityStatus.ACTIVE,
                mmMaterialCodeSearchDTO.toPageable()
            );
        }

    }

    /**
     * 创建材料编码。
     *
     * @param orgId
     * @param projectId
     * @param mmMaterialCodeCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmMaterialCodeEntity create(
        Long orgId,
        Long projectId,
        MmMaterialCodeCreateDTO mmMaterialCodeCreateDTO,
        ContextDTO contextDTO
    ) {

        if (mmMaterialCodeCreateDTO.getEntities().size() > 0) {
            List<MmMaterialCodeEntity> mmMaterialCodeEntities = new ArrayList<>();
            for (MmMaterialCodeEntity mmMaterialCodeEntityItem : mmMaterialCodeCreateDTO.getEntities()) {

                // 查询公司材料代码分类是否存在
                Optional<MmMaterialCodeTypeEntity> mmMaterialCodeTypeEntityOptional = mmMaterialCodeTypeProjectRepository.findById(mmMaterialCodeEntityItem.getMmMaterialCodeTypeId());
                if (mmMaterialCodeTypeEntityOptional.isPresent()) {
                    // 查询项目材料编码分类是否存在
                    MmMaterialCodeTypeEntity projectMmMaterialCodeTypeEntity = mmMaterialCodeTypeProjectRepository.findByOrgIdAndProjectIdAndNameAndStatus(orgId, projectId, mmMaterialCodeTypeEntityOptional.get().getName(), EntityStatus.ACTIVE);
                    Long projectMaterialCodeTypeId = 0L;
                    String projectMaterialCodeTypeName = "";
                    if (projectMmMaterialCodeTypeEntity == null) {
                        MmMaterialCodeTypeEntity mmMaterialCodeTypeEntity = new MmMaterialCodeTypeEntity();
                        mmMaterialCodeTypeEntity.setOrgId(orgId);
                        mmMaterialCodeTypeEntity.setProjectId(projectId);
                        mmMaterialCodeTypeEntity.setCreatedAt(new Date());
                        mmMaterialCodeTypeEntity.setCreatedBy(contextDTO.getOperator().getId());
                        mmMaterialCodeTypeEntity.setLastModifiedAt(new Date());
                        mmMaterialCodeTypeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                        mmMaterialCodeTypeEntity.setStatus(EntityStatus.ACTIVE);
                        mmMaterialCodeTypeEntity.setCompanyId(mmMaterialCodeTypeEntityOptional.get().getCompanyId());
                        mmMaterialCodeTypeEntity.setName(mmMaterialCodeTypeEntityOptional.get().getName());
                        mmMaterialCodeTypeEntity.setWareHouseType(MaterialOrganizationType.PROJECT);
                        SeqNumberDTO dto = mmMaterialCodeTypeProjectRepository.getMaxSeqNumber(orgId, projectId, MaterialOrganizationType.PROJECT).orElse(new SeqNumberDTO(0));
                        int number = dto.getNewSeqNumber();
                        mmMaterialCodeTypeEntity.setSeqNumber(number);
                        mmMaterialCodeTypeProjectRepository.save(mmMaterialCodeTypeEntity);
                        projectMaterialCodeTypeId = mmMaterialCodeTypeEntity.getId();
                        projectMaterialCodeTypeName = mmMaterialCodeTypeEntity.getName();
                    } else {
                        projectMaterialCodeTypeId = projectMmMaterialCodeTypeEntity.getId();
                        projectMaterialCodeTypeName = projectMmMaterialCodeTypeEntity.getName();
                    }
                    MmMaterialCodeEntity oldMmMaterialCodeEntity = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndNoAndMaterialOrganizationTypeAndStatus(
                        orgId,
                        projectId,
                        mmMaterialCodeEntityItem.getNo(),
                        MaterialOrganizationType.PROJECT,
                        EntityStatus.ACTIVE
                    );

                    if (oldMmMaterialCodeEntity != null) {
                        throw new BusinessError("Material Code name already exists!材料编码名称已经存在！");
                    }

                    MmMaterialCodeEntity mmMaterialCodeEntity = new MmMaterialCodeEntity();
                    mmMaterialCodeEntity.setCompanyId(mmMaterialCodeEntityItem.getCompanyId());
                    mmMaterialCodeEntity.setDescription(mmMaterialCodeEntityItem.getDescription());
                    mmMaterialCodeEntity.setNo(mmMaterialCodeEntityItem.getNo());
                    mmMaterialCodeEntity.setQrCodeType(mmMaterialCodeEntityItem.getRecommendedQrCodeType());
                    mmMaterialCodeEntity.setRecommendedQrCodeType(mmMaterialCodeEntityItem.getRecommendedQrCodeType());
                    mmMaterialCodeEntity.setUnit(mmMaterialCodeEntityItem.getUnit());
                    mmMaterialCodeEntity.setMaterialOrganizationType(MaterialOrganizationType.PROJECT);
                    mmMaterialCodeEntity.setMmMaterialCodeTypeId(projectMaterialCodeTypeId);
                    mmMaterialCodeEntity.setMmMaterialCodeTypeName(projectMaterialCodeTypeName);

                    mmMaterialCodeEntity.setMaterialSpec(mmMaterialCodeEntityItem.getMaterialSpec());
                    mmMaterialCodeEntity.setMaterialQuality(mmMaterialCodeEntityItem.getMaterialQuality());

                    mmMaterialCodeEntity.setOrgId(orgId);
                    mmMaterialCodeEntity.setProjectId(projectId);
                    mmMaterialCodeEntity.setCreatedAt(new Date());
                    mmMaterialCodeEntity.setCreatedBy(contextDTO.getOperator().getId());
                    mmMaterialCodeEntity.setLastModifiedAt(new Date());
                    mmMaterialCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmMaterialCodeEntity.setStatus(EntityStatus.ACTIVE);

                    SeqNumberDTO seqNumberDTO = mmMaterialCodeRepository.getMaxSeqNumber(orgId, projectId, MaterialOrganizationType.PROJECT).orElse(new SeqNumberDTO(0));
                    int seqNumber = seqNumberDTO.getNewSeqNumber();
                    mmMaterialCodeEntity.setSeqNumber(seqNumber);
                    mmMaterialCodeEntity.setIdentCode(String.format("%06d", seqNumber));
                    mmMaterialCodeEntities.add(mmMaterialCodeEntity);
                    mmMaterialCodeRepository.save(mmMaterialCodeEntity);
                }
            }
        } else {
            throw new BusinessError("The field entities in the payload should not be empty.");
        }

        return null;

    }

    /**
     * 更新材料编码。
     *
     * @param orgId
     * @param projectId
     * @param materialCodeId
     * @param mmMaterialCodeCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmMaterialCodeEntity update(
        Long orgId,
        Long projectId,
        Long materialCodeId,
        MmMaterialCodeCreateDTO mmMaterialCodeCreateDTO,
        ContextDTO contextDTO
    ) {
        MmMaterialCodeEntity oldMmMaterialCodeEntity = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialCodeId,
            EntityStatus.ACTIVE
        );
        if (oldMmMaterialCodeEntity == null) {
            throw new BusinessError("材料编码信息不存在");
        }

        MmMaterialCodeEntity mmMaterialCodeEntity = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndNoAndMaterialOrganizationTypeAndStatus(
            orgId,
            projectId,
            mmMaterialCodeCreateDTO.getNo(),
            MaterialOrganizationType.PROJECT,
            EntityStatus.ACTIVE
        );

        if (mmMaterialCodeEntity != null && !mmMaterialCodeEntity.getId().equals(oldMmMaterialCodeEntity.getId())) {
            throw new BusinessError("材料编码名称已经存在");
        }

        if (null != mmMaterialCodeCreateDTO.getQrCodeType()) {
            oldMmMaterialCodeEntity.setQrCodeType(mmMaterialCodeCreateDTO.getQrCodeType());
        }
        if (null != mmMaterialCodeCreateDTO.getNeedSpec()) {
            oldMmMaterialCodeEntity.setNeedSpec(mmMaterialCodeCreateDTO.getNeedSpec());
        }

        // TODO 如果材料编码已经被使用，则不能更新
       List<MmPurchasePackageItemEntity> mmPurchasePackageItemEntities =this.mmPurchasePackageItemRepository.findByOrgIdAndProjectIdAndMmMaterialCodeNoAndStatus(
            orgId,
            projectId,
            oldMmMaterialCodeEntity.getNo(),
            EntityStatus.ACTIVE
        );
       List<MmReleaseReceiveDetailEntity> mmReleaseReceiveDetailEntities = this.mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndMmMaterialCodeNoAndStatus(
            orgId,
            projectId,
            oldMmMaterialCodeEntity.getNo(),
            EntityStatus.ACTIVE
        );
        List<MmShippingDetailEntity> mmShippingDetailEntities =this.mmShippingDetailRepository.findByOrgIdAndProjectIdAndMmMaterialCodeNoAndStatus(
            orgId,
            projectId,
            oldMmMaterialCodeEntity.getNo(),
            EntityStatus.ACTIVE
        );
        if(!mmPurchasePackageItemEntities.isEmpty() || !mmReleaseReceiveDetailEntities.isEmpty() || !mmShippingDetailEntities.isEmpty() ){
            throw new BusinessError("材料编码已被项目使用，不能进行修改");
        }

        oldMmMaterialCodeEntity.setOrgId(orgId);
        oldMmMaterialCodeEntity.setProjectId(projectId);
        oldMmMaterialCodeEntity.setLastModifiedAt(new Date());
        oldMmMaterialCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        oldMmMaterialCodeEntity.setStatus(EntityStatus.ACTIVE);

        return mmMaterialCodeRepository.save(oldMmMaterialCodeEntity);
    }

    /**
     * 材料编码详情。
     *
     * @param orgId
     * @param projectId
     * @param materialCodeId
     * @return
     */
    @Override
    public MmMaterialCodeEntity detail(
        Long orgId,
        Long projectId,
        Long materialCodeId
    ) {
        MmMaterialCodeEntity mmMaterialCodeEntity = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialCodeId,
            EntityStatus.ACTIVE
        );
        if (mmMaterialCodeEntity == null) {
            throw new BusinessError("材料编码不存在");
        }
        return mmMaterialCodeEntity;
    }

    /**
     * 删除材料编码。
     *
     * @param orgId
     * @param projectId
     * @param materialCodeId
     * @param contextDTO
     */
    @Override
    public void delete(
        Long orgId,
        Long projectId,
        Long materialCodeId,
        ContextDTO contextDTO
    ) {
        MmMaterialCodeEntity mmMaterialCodeEntity = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialCodeId,
            EntityStatus.ACTIVE
        );
        if (mmMaterialCodeEntity == null) {
            throw new BusinessError("材料编码不存在");
        }

        // TODO 删除项目材料编码check

//        List<MmMaterialCodeEntity> mmMaterialCodeEntityList = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndMmWareHouseIdAndStatus(
//            orgId,
//            projectId,
//            mmMaterialCodeEntity.getMmWareHouseId(),
//            EntityStatus.ACTIVE
//        );
//        if (mmMaterialCodeEntityList.size() == 1) {
//            MmMaterialCodeTypeEntity mmMaterialCodeTypeEntity = mmMaterialCodeTypeProjectRepository.findByOrgIdAndProjectIdAndNameAndStatus(
//                orgId,
//                projectId,
//                mmMaterialCodeEntity.getMmWareHouseName(),
//                EntityStatus.ACTIVE
//            );
//            if (mmMaterialCodeTypeEntity != null) {
//                mmMaterialCodeTypeEntity.setLastModifiedAt(new Date());
//                mmMaterialCodeTypeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
//                mmMaterialCodeTypeEntity.setStatus(EntityStatus.DELETED);
//                mmMaterialCodeTypeEntity.setDeleted(true);
//                mmMaterialCodeTypeEntity.setDeletedAt(new Date());
//                mmMaterialCodeTypeEntity.setDeletedBy(contextDTO.getOperator().getId());
//                mmMaterialCodeTypeProjectRepository.save(mmMaterialCodeTypeEntity);
//
//            }
//        }

        mmMaterialCodeEntity.setLastModifiedAt(new Date());
        mmMaterialCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmMaterialCodeEntity.setStatus(EntityStatus.DELETED);
        mmMaterialCodeEntity.setDeleted(true);
        mmMaterialCodeEntity.setDeletedAt(new Date());
        mmMaterialCodeEntity.setDeletedBy(contextDTO.getOperator().getId());
        mmMaterialCodeRepository.save(mmMaterialCodeEntity);
    }


    /**
     * 材料编码详情。
     *
     * @param orgId
     * @param projectId
     * @param tagNumberCompanySearchDTO
     * @return
     */
    @Override
    public MmMaterialCodeEntity searchDetail(
        Long orgId,
        Long projectId,
        MmMaterialCodeSearchDTO tagNumberCompanySearchDTO
    ) {
        MmMaterialCodeEntity mmMaterialCodeEntity = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndNoAndStatus(
            orgId,
            projectId,
            tagNumberCompanySearchDTO.getKeyword(),
            EntityStatus.ACTIVE
        );
        if (mmMaterialCodeEntity == null) {
            throw new BusinessError("材料编码不存在");
        }
        return mmMaterialCodeEntity;
    }


}
