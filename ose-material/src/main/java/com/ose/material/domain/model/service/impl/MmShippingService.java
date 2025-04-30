package com.ose.material.domain.model.service.impl;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.material.domain.model.repository.*;
import com.ose.material.domain.model.service.MmReleaseReceiveInterface;
import com.ose.material.domain.model.service.MmShippingInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.*;
import com.ose.material.vo.MaterialPrefixType;
import com.ose.material.vo.MmReleaseReceiveType;
import com.ose.material.vo.QrCodeType;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.response.JsonObjectResponseBody;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.QrcodePrefixType;
import com.ose.vo.ShippingStatus;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 发货单。
 */
@Component
public class MmShippingService implements MmShippingInterface {

    @Value("${application.files.temporary}")
    private String temporaryDir;

    /**
     * 采购合同  操作仓库。
     */
    private final MmReleaseReceiveRepository mmReleaseReceiveRepository;
    private final UploadFeignAPI uploadFeignAPI;
    private final MmShippingRepository mmShippingRepository;
    private final MmShippingDetailRepository mmShippingDetailRepository;
    private final MmShippingDetailRelationRepository mmShippingDetailRelationRepository;
    private final MmReleaseReceiveInterface mmReleaseReceiveService;
    private final MmMaterialCodeRepository mmMaterialCodeRepository;
    private final MmWareHouseLocationRepository mmWareHouseLocationRepository;
    private final MmPurchasePackageRepository mmPurchasePackageRepository;
    private final MmPurchasePackageItemRepository mmPurchasePackageItemRepository;
    private final MmImportBatchTaskRepository mmImportBatchTaskRepository;
    private final MmMaterialInStockDetailQrCodeRepository mmMaterialInStockDetailQrCodeRepository;
    private final MmReleaseReceiveDetailRepository mmReleaseReceiveDetailRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public MmShippingService(
        MmReleaseReceiveRepository mmReleaseReceiveRepository,
        UploadFeignAPI uploadFeignAPI,
        MmShippingRepository mmShippingRepository,
        MmShippingDetailRepository mmShippingDetailRepository,
        MmShippingDetailRelationRepository mmShippingDetailRelationRepository,
        MmReleaseReceiveInterface mmReleaseReceiveService,
        MmMaterialCodeRepository mmMaterialCodeRepository,
        MmWareHouseLocationRepository mmWareHouseLocationRepository,
        MmPurchasePackageRepository mmPurchasePackageRepository,
        MmPurchasePackageItemRepository mmPurchasePackageItemRepository,
        MmImportBatchTaskRepository mmImportBatchTaskRepository,
        MmMaterialInStockDetailQrCodeRepository mmMaterialInStockDetailQrCodeRepository,
        MmReleaseReceiveDetailRepository mmReleaseReceiveDetailRepository
    ) {

        this.mmReleaseReceiveRepository = mmReleaseReceiveRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.mmShippingRepository = mmShippingRepository;
        this.mmShippingDetailRepository = mmShippingDetailRepository;
        this.mmShippingDetailRelationRepository = mmShippingDetailRelationRepository;
        this.mmReleaseReceiveService = mmReleaseReceiveService;
        this.mmMaterialCodeRepository = mmMaterialCodeRepository;
        this.mmWareHouseLocationRepository = mmWareHouseLocationRepository;
        this.mmPurchasePackageRepository = mmPurchasePackageRepository;
        this.mmPurchasePackageItemRepository = mmPurchasePackageItemRepository;
        this.mmImportBatchTaskRepository = mmImportBatchTaskRepository;
        this.mmMaterialInStockDetailQrCodeRepository = mmMaterialInStockDetailQrCodeRepository;
        this.mmReleaseReceiveDetailRepository = mmReleaseReceiveDetailRepository;
    }

    /**
     * 查询列表。
     *
     * @param orgId
     * @param projectId
     * @param mmShippingSearchDTO
     * @return
     */
    @Override
    public Page<MmShippingEntity> search(Long orgId, Long projectId, MmShippingSearchDTO mmShippingSearchDTO) {
        return mmShippingRepository.search(orgId, projectId, mmShippingSearchDTO);
    }

    /**
     * 创建。
     *
     * @param orgId
     * @param projectId
     * @param mmShippingCreateDTO
     * @param contextDTO
     */
    @Override
    public void create(Long orgId, Long projectId, MmShippingCreateDTO mmShippingCreateDTO, ContextDTO contextDTO) {
        MmShippingEntity shippingEntityFind = mmShippingRepository.findByOrgIdAndProjectIdAndNameAndStatus(orgId, projectId, mmShippingCreateDTO.getName(), EntityStatus.ACTIVE);

        if (shippingEntityFind != null) {
            throw new BusinessError("发货单已经存在！");
        }

        if (null == mmShippingCreateDTO.getMmPurchasePackageId()) {
            throw new BusinessError("请关联请购包");
        }

        MmPurchasePackageEntity mmPurchasePackageEntity = mmPurchasePackageRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, mmShippingCreateDTO.getMmPurchasePackageId(), EntityStatus.ACTIVE);
        if (null == mmPurchasePackageEntity) {
            throw new BusinessError("请购包无效");
        }

        MmShippingEntity mmShippingEntity = new MmShippingEntity();
        BeanUtils.copyProperties(mmShippingCreateDTO, mmShippingEntity);

        SeqNumberDTO seqNumberDTO = mmShippingRepository.getMaxSeqNumber(orgId, projectId).orElse(new SeqNumberDTO(0));
        int seqNumber = seqNumberDTO.getNewSeqNumber();

        mmShippingEntity.setSeqNumber(seqNumber);

        mmShippingEntity.setNo(String.format("%s-%04d", MaterialPrefixType.MATERIAL_SHIPPING.getCode(), seqNumber));

        mmShippingEntity.setOrgId(orgId);
        mmShippingEntity.setMmPurchasePackageId(mmPurchasePackageEntity.getId());
        mmShippingEntity.setMmPurchasePackageNo(mmPurchasePackageEntity.getPwpsNumber());
        mmShippingEntity.setProjectId(projectId);
        mmShippingEntity.setCreatedAt(new Date());
        mmShippingEntity.setCreatedBy(contextDTO.getOperator().getId());
        mmShippingEntity.setLastModifiedAt(new Date());
        mmShippingEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmShippingEntity.setStatus(EntityStatus.ACTIVE);
        mmShippingEntity.setShippingStatus(ShippingStatus.UN_ISSUED);

        if (mmShippingCreateDTO.getFileName() != null) {
            JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), mmShippingCreateDTO.getFileName(), new FilePostDTO());
            FileES fileES = responseBody.getData();
            mmShippingEntity.setFileId(LongUtils.parseLong(fileES.getId()));
            mmShippingEntity.setFileName(fileES.getName());
            mmShippingEntity.setFilePath(fileES.getPath());
        }

        mmShippingRepository.save(mmShippingEntity);
    }

    /**
     * 更新。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param mmShippingCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmShippingEntity update(Long orgId, Long projectId, Long shippingId, MmShippingCreateDTO mmShippingCreateDTO, ContextDTO contextDTO) {
        MmShippingEntity shippingEntity = mmShippingRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, shippingId, EntityStatus.ACTIVE);
        if (shippingEntity == null) {
            throw new BusinessError("发货单信息不存在！");
        }
        MmShippingEntity shippingEntityFind = mmShippingRepository.findByOrgIdAndProjectIdAndNameAndStatus(orgId, projectId, mmShippingCreateDTO.getName(), EntityStatus.ACTIVE);
        if (shippingEntityFind != null && !shippingEntityFind.getId().equals(shippingId)) {
            throw new BusinessError("发货单名称已经存在！");
        }

        if (null == mmShippingCreateDTO.getMmPurchasePackageId()) {
            throw new BusinessError("请关联请购包");
        }

        MmPurchasePackageEntity mmPurchasePackageEntity = mmPurchasePackageRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, mmShippingCreateDTO.getMmPurchasePackageId(), EntityStatus.ACTIVE);
        if (null == mmPurchasePackageEntity) {
            throw new BusinessError("请购包无效");
        }

        shippingEntity.setName(mmShippingCreateDTO.getName());
        shippingEntity.setContractNo(mmShippingCreateDTO.getContractNo());
        shippingEntity.setRemarks(mmShippingCreateDTO.getRemarks());
        shippingEntity.setType(mmShippingCreateDTO.getType());
        shippingEntity.setMmPurchasePackageNo(mmPurchasePackageEntity.getPwpsNumber());
        shippingEntity.setMmPurchasePackageId(mmPurchasePackageEntity.getId());
        shippingEntity.setOrgId(orgId);
        shippingEntity.setProjectId(projectId);
        shippingEntity.setLastModifiedAt(new Date());
        shippingEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        shippingEntity.setStatus(EntityStatus.ACTIVE);

        if (mmShippingCreateDTO.getFileName() != null && mmShippingCreateDTO.getFileName().length() != 19) {
            JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), mmShippingCreateDTO.getFileName(), new FilePostDTO());
            FileES fileES = responseBody.getData();
            shippingEntity.setFileId(LongUtils.parseLong(fileES.getId()));
            shippingEntity.setFileName(fileES.getName());
            shippingEntity.setFilePath(fileES.getPath());
        }

        mmShippingRepository.save(shippingEntity);

        return shippingEntity;
    }

    /**
     * 详情。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @return
     */
    @Override
    public MmShippingEntity detail(Long orgId, Long projectId, Long shippingId) {
        MmShippingEntity shippingEntityFind = mmShippingRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, shippingId, EntityStatus.ACTIVE);
        if (shippingEntityFind == null) {
            throw new BusinessError("发货单不存在");
        }

        return shippingEntityFind;

    }

    /**
     * 更新。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param mmShippingUpdateStatusDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmShippingEntity updateStatus(Long orgId, Long projectId, Long shippingId, MmShippingUpdateStatusDTO mmShippingUpdateStatusDTO, ContextDTO contextDTO) {
        MmShippingEntity shippingEntityFind = mmShippingRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, shippingId, EntityStatus.ACTIVE);
        if (shippingEntityFind == null) {
            throw new BusinessError("发货单不存在");
        }
        shippingEntityFind.setShippingStatus(mmShippingUpdateStatusDTO.getShippingStatus());
        shippingEntityFind.setLastModifiedBy(contextDTO.getOperator().getId());
        shippingEntityFind.setLastModifiedAt(new Date());
        mmShippingRepository.save(shippingEntityFind);

        // 更新相关的数据，包括（发货单单详情、采购包详情）
        List<MmShippingDetailRelationEntity> mmShippingDetailRelationEntities = mmShippingDetailRelationRepository.findByOrgIdAndProjectIdAndShippingIdAndStatus(orgId, projectId, shippingId, EntityStatus.ACTIVE);
        if (mmShippingDetailRelationEntities.size() > 0) {
            for (int i = 0; i < mmShippingDetailRelationEntities.size(); i++) {
                MmShippingDetailRelationEntity mmShippingDetailRelationEntity = mmShippingDetailRelationEntities.get(i);
//                MmShippingDetailEntity mmShippingDetailEntity = mmShippingDetailRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, mmShippingDetailRelationEntity.getShippingDetailId(), EntityStatus.ACTIVE);
//                if (mmShippingDetailEntity != null) {
//                    if (null != mmShippingDetailEntity.getShippedPieceQty() && null != mmShippingDetailRelationEntity.getPieceInventoryQty()) {
//                        mmShippingDetailEntity.setShippedPieceQty(mmShippingDetailEntity.getShippedPieceQty() + mmShippingDetailRelationEntity.getPieceInventoryQty());
//                    }
//                    mmShippingDetailEntity.setShippedTotalQty(mmShippingDetailEntity.getShippedTotalQty() + mmShippingDetailRelationEntity.getTotalInventoryQty());
//                    mmShippingDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
//                    mmShippingDetailEntity.setLastModifiedAt(new Date());
//                    mmShippingDetailRepository.save(mmShippingDetailEntity);
//                }

                MmPurchasePackageItemEntity mmPurchasePackageItemEntity = mmPurchasePackageItemRepository.findByOrgIdAndProjectIdAndPurchasePackageIdAndMmMaterialCodeNoAndSpecDescriptionAndSpecValueAndStatus(
                    orgId,
                    projectId,
                    mmShippingDetailRelationEntity.getPurchasePackageId(),
                    mmShippingDetailRelationEntity.getMmMaterialCodeNo(),
                    mmShippingDetailRelationEntity.getSpecDescription(),
                    mmShippingDetailRelationEntity.getSpecValue(),
                    EntityStatus.ACTIVE
                );
                if (mmPurchasePackageItemEntity != null) {
                    if (null != mmPurchasePackageItemEntity.getBuyPieceQty() && null != mmShippingDetailRelationEntity.getPieceInventoryQty()) {
                        mmPurchasePackageItemEntity.setBuyPieceQty(mmPurchasePackageItemEntity.getBuyPieceQty() + mmShippingDetailRelationEntity.getPieceInventoryQty());
                    }
                    mmPurchasePackageItemEntity.setBuyTotalQty(mmPurchasePackageItemEntity.getBuyTotalQty() + mmShippingDetailRelationEntity.getTotalInventoryQty());
                    mmPurchasePackageItemEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmPurchasePackageItemEntity.setLastModifiedAt(new Date());
                    mmPurchasePackageItemRepository.save(mmPurchasePackageItemEntity);
                }
            }
        }

        // 创建相应的入库单信息，详情内容与发货单详情类似
        MmReleaseReceiveCreateDTO mmReleaseReceiveCreateDTO = new MmReleaseReceiveCreateDTO();
        mmReleaseReceiveCreateDTO.setName("RECEIVE FOR " + shippingEntityFind.getName());
        mmReleaseReceiveCreateDTO.setType(shippingEntityFind.getType());
        mmReleaseReceiveCreateDTO.setMmShippingId(shippingEntityFind.getId());
        mmReleaseReceiveCreateDTO.setMmReleaseReceiveType(MmReleaseReceiveType.REQUISITION);
        mmReleaseReceiveService.create(orgId, projectId, mmReleaseReceiveCreateDTO, contextDTO);

        return shippingEntityFind;
    }

    /**
     * 删除。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param contextDTO
     */
    @Override
    public void delete(Long orgId, Long projectId, Long shippingId, ContextDTO contextDTO) {
        MmShippingEntity shippingEntityFind = mmShippingRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, shippingId, EntityStatus.ACTIVE);
        if (shippingEntityFind == null) {
            throw new BusinessError("发货单不存在");
        }

        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<MmShippingDetailEntity> mmShippingDetailEntities = mmShippingDetailRepository.findByOrgIdAndProjectIdAndShippingIdAndStatusOrderByMmMaterialCodeNo(orgId, projectId, shippingId, EntityStatus.ACTIVE);

        if (mmShippingDetailEntities != null && mmShippingDetailEntities.size() > 0) {
            throw new BusinessError("发货单存在明细信息");
        }

        shippingEntityFind.setLastModifiedAt(new Date());
        shippingEntityFind.setLastModifiedBy(contextDTO.getOperator().getId());
        shippingEntityFind.setStatus(EntityStatus.DELETED);
        mmShippingRepository.save(shippingEntityFind);
    }

    /**
     * 删除发货单明细。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param shippingDetailId
     * @param contextDTO
     */
    @Override
    public void deleteItem(Long orgId, Long projectId, Long shippingId, Long shippingDetailId, ContextDTO contextDTO) {
        MmShippingEntity mmShippingEntityFind = mmShippingRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, shippingId, EntityStatus.ACTIVE);
        if (mmShippingEntityFind == null) {
            throw new BusinessError("发货单不存在");
        }


        MmShippingDetailEntity mmShippingDetailEntityFind = mmShippingDetailRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, shippingDetailId, EntityStatus.ACTIVE);
        if (mmShippingDetailEntityFind == null) {
            throw new BusinessError("发货单详情不存在");
        }

        List<MmShippingDetailRelationEntity> mmShippingDetailRelationEntities = mmShippingDetailRelationRepository.findByOrgIdAndProjectIdAndShippingDetailIdAndStatus(orgId, projectId, shippingDetailId, EntityStatus.ACTIVE);


        if (mmShippingDetailRelationEntities.size() > 0) {
            throw new BusinessError("发货单详情下已关联具体请购单信息");
        } else {
            mmShippingDetailEntityFind.setDeleted(true);
            mmShippingDetailEntityFind.setDeletedAt(new Date());
            mmShippingDetailEntityFind.setStatus(EntityStatus.DELETED);
            mmShippingDetailEntityFind.setLastModifiedAt(new Date());
            mmShippingDetailEntityFind.setLastModifiedBy(contextDTO.getOperator().getId());
            mmShippingDetailRepository.save(mmShippingDetailEntityFind);
        }

    }

    /**
     * 删除发货单明细关系。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param shippingDetailId
     * @param contextDTO
     */
    @Override
    public void deleteItemRelation(Long orgId, Long projectId, Long shippingId, Long shippingDetailId, Long shippingDetailRelationId, ContextDTO contextDTO) {
        MmShippingEntity mmShippingEntityFind = mmShippingRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, shippingId, EntityStatus.ACTIVE);
        if (mmShippingEntityFind == null) {
            throw new BusinessError("发货单不存在");
        }


        MmShippingDetailEntity mmShippingDetailEntityFind = mmShippingDetailRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, shippingDetailId, EntityStatus.ACTIVE);
        if (mmShippingDetailEntityFind == null) {
            throw new BusinessError("发货单详情不存在");
        }

        MmShippingDetailRelationEntity mmShippingDetailRelationEntity = mmShippingDetailRelationRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, shippingDetailRelationId, EntityStatus.ACTIVE);
        if (mmShippingDetailRelationEntity == null) {
            throw new BusinessError("发货单详情关系不存在");
        } else {
            mmShippingDetailRelationEntity.setDeleted(true);
            mmShippingDetailRelationEntity.setDeletedAt(new Date());
            mmShippingDetailRelationEntity.setStatus(EntityStatus.DELETED);
            mmShippingDetailRelationEntity.setLastModifiedAt(new Date());
            mmShippingDetailRelationEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            mmShippingDetailRelationRepository.save(mmShippingDetailRelationEntity);
        }

        // 查询发货单详情中的关系是否还存在，若没有则将详情删除，若还没则更新数量信息
        List<MmShippingDetailRelationEntity> mmShippingDetailRelationEntities = mmShippingDetailRelationRepository.findByOrgIdAndProjectIdAndShippingDetailIdAndStatus(orgId, projectId, shippingDetailId, EntityStatus.ACTIVE);
        if (mmShippingDetailRelationEntities.size() == 0) {
            mmShippingDetailEntityFind.setDeleted(true);
            mmShippingDetailEntityFind.setDeletedAt(new Date());
            mmShippingDetailEntityFind.setStatus(EntityStatus.DELETED);
            mmShippingDetailEntityFind.setLastModifiedAt(new Date());
            mmShippingDetailEntityFind.setLastModifiedBy(contextDTO.getOperator().getId());
            mmShippingDetailRepository.save(mmShippingDetailEntityFind);
        } else {
//            mmShippingDetailEntityFind.setDesignQty(mmShippingDetailEntityFind.getDesignQty() - mmShippingDetailRelationEntity.getDesignQty());
//            mmShippingDetailEntityFind.setShippedQty(mmShippingDetailEntityFind.getShippedQty() - mmShippingDetailRelationEntity.getShippedQty());
//            mmShippingDetailEntityFind.setInventoryQty(mmShippingDetailEntityFind.getInventoryQty() - mmShippingDetailRelationEntity.getInventoryQty());
            mmShippingDetailEntityFind.setLastModifiedAt(new Date());
            mmShippingDetailEntityFind.setLastModifiedBy(contextDTO.getOperator().getId());
            mmShippingDetailRepository.save(mmShippingDetailEntityFind);
        }
    }

    /**
     * 发货单明细列表。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param mmShippingSearchDTO
     * @return
     */
    @Override
    public Page<MmShippingDetailSearchDetailDTO> searchDetails(Long orgId, Long projectId, Long shippingId, MmShippingSearchDTO mmShippingSearchDTO) {
        MmShippingEntity shippingEntityFind = mmShippingRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, shippingId, EntityStatus.ACTIVE);
        if (shippingEntityFind == null) {
            throw new BusinessError("发货单不存在");
        }
        List<MmShippingDetailSearchDetailDTO> mmShippingDetailSearchDetailDTOS = new ArrayList<>();

        List<MmShippingDetailEntity> mmShippingDetailEntities = mmShippingDetailRepository.findByOrgIdAndProjectIdAndShippingIdAndStatusOrderByMmMaterialCodeNo(orgId, projectId, shippingId, EntityStatus.ACTIVE);

        if (mmShippingDetailEntities.size() > 0) {
            for (int i = 0; i < mmShippingDetailEntities.size(); i++) {
                MmShippingDetailSearchDetailDTO mmShippingDetailSearchDetailDTO = new MmShippingDetailSearchDetailDTO();
                MmShippingDetailEntity mmShippingDetailEntity = mmShippingDetailEntities.get(i);

                BeanUtils.copyProperties(mmShippingDetailEntity, mmShippingDetailSearchDetailDTO);

                List<MmShippingDetailRelationEntity> mmShippingDetailRelationEntities = mmShippingDetailRelationRepository.findByOrgIdAndProjectIdAndShippingDetailIdAndStatus(orgId, projectId, mmShippingDetailEntity.getId(), EntityStatus.ACTIVE);
                if (mmShippingDetailRelationEntities.size() > 0) {
                    mmShippingDetailSearchDetailDTO.setMmShippingDetailRelationEntities(mmShippingDetailRelationEntities);
                }
                mmShippingDetailSearchDetailDTOS.add(mmShippingDetailSearchDetailDTO);
            }
        }

        int pageNo = mmShippingSearchDTO.getPage().getNo();
        int pageSize = mmShippingSearchDTO.getPage().getSize();

        int start = (pageNo - 1) * pageSize;
        int end = start + pageSize;

        if (end > mmShippingDetailSearchDetailDTOS.size()) {
            end = mmShippingDetailSearchDetailDTOS.size();
        }
        return new PageImpl<>(mmShippingDetailSearchDetailDTOS.subList(start, end), mmShippingSearchDTO.toPageable(), mmShippingDetailSearchDetailDTOS.size());

    }

    @Override
    public Page<MmShippingDetailsDTO> searchRequisitionDetails(Long orgId, Long projectId, Long shippingId, MmShippingSearchDTO mmShippingSearchDTO) {
        return mmShippingDetailRepository.searchRequisitionDetail(orgId, projectId, shippingId, mmShippingSearchDTO);
    }

    /**
     * 发货单详情添加。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param mmShippingDetailAddDTO
     * @return
     */
    public void addDetails(
        Long orgId,
        Long projectId,
        Long shippingId,
        MmShippingDetailAddDTO mmShippingDetailAddDTO,
        ContextDTO contextDTO
    ) {
        if (mmShippingDetailAddDTO.getMmShippingDetailsDTOS() == null) {
            throw new BusinessError("未选择采购包详情");
        }

        MmShippingEntity mmShippingEntity = mmShippingRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            shippingId,
            EntityStatus.ACTIVE
        );

        if (null == mmShippingEntity) {
            throw new BusinessError("入库准备单不存在");
        }

        MmPurchasePackageEntity mmPurchasePackageEntity = mmPurchasePackageRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId, projectId,
            mmShippingEntity.getMmPurchasePackageId(),
            EntityStatus.ACTIVE
        );
        if (mmPurchasePackageEntity == null) {
            throw new BusinessError("采购包不存在");
        }

        // 创建发货单详情信息（以及发货单详情与请购单详情的关系）
        for (MmShippingDetailsDTO mmShippingDetailsDTO : mmShippingDetailAddDTO.getMmShippingDetailsDTOS()) {

            // 查询请购单详情是否存在
            MmPurchasePackageItemEntity mmPurchasePackageItemEntity = mmPurchasePackageItemRepository.findByOrgIdAndProjectIdAndPurchasePackageIdAndIdAndStatus(
                orgId,
                projectId,
                mmShippingEntity.getMmPurchasePackageId(),
                mmShippingDetailsDTO.getMmRequisitionDetailId(),
                EntityStatus.ACTIVE);
            if (null == mmPurchasePackageItemEntity) {
                continue;
            }
            // 根据传入的请购单详情信息创建发货单详情信息
            // 1、查询发货单详情是否存在（材料编号为主键）
//            MmShippingDetailEntity mmShippingDetailEntityOld = mmShippingDetailRepository.findByOrgIdAndProjectIdAndShippingIdAndMmMaterialCodeNoAndHeatBatchNoAndWareHouseLocationIdAndMaterialCertificateAndStatus(
//                orgId,
//                projectId,
//                shippingId,
//                mmPurchasePackageItemEntity.getMmMaterialCodeNo(),
////                mmPurchasePackageItemEntity.getHeatBatchNo(),
////                mmPurchasePackageItemEntity.getWareHouseLocationId(),
////                mmPurchasePackageItemEntity.getMaterialCertificate(),
//                EntityStatus.ACTIVE);
//            if (mmShippingDetailEntityOld == null) {
//                MmShippingDetailEntity mmShippingDetailEntityNew = new MmShippingDetailEntity();
//                mmShippingDetailEntityNew.setOrgId(orgId);
//                mmShippingDetailEntityNew.setProjectId(projectId);
//                mmShippingDetailEntityNew.setCompanyId(null);
//                mmShippingDetailEntityNew.setShippingId(shippingId);
//                mmShippingDetailEntityNew.setPurchasePackageId(mmPurchasePackageEntity.getId());
//                mmShippingDetailEntityNew.setPurchasePackageNo(mmPurchasePackageEntity.getPwpsNumber());
//                mmShippingDetailEntityNew.setMmMaterialCodeNo(mmPurchasePackageItemEntity.getMmMaterialCodeNo());
//                mmShippingDetailEntityNew.setIdentCode(mmPurchasePackageItemEntity.getIdentCode());
//                mmShippingDetailEntityNew.setSpecQuality(mmPurchasePackageItemEntity.getSpecQuality());
//                mmShippingDetailEntityNew.setHeatBatchNo(mmPurchasePackageItemEntity.getHeatBatchNo());
//                mmShippingDetailEntityNew.setWareHouseLocationId(mmPurchasePackageItemEntity.getWareHouseLocationId());
//                mmShippingDetailEntityNew.setMaterialCertificate(mmPurchasePackageItemEntity.getMaterialCertificate());
//
//                mmShippingDetailEntityNew.setSpecName(mmPurchasePackageItemEntity.getSpecName());
//                mmShippingDetailEntityNew.setDesignQty(mmPurchasePackageItemEntity.getDesignQty());
//                mmShippingDetailEntityNew.setShippedQty(mmShippingDetailsDTO.getShippedQty());
//                mmShippingDetailEntityNew.setDesignUnit(mmPurchasePackageItemEntity.getDesignUnit());
//
//                mmShippingDetailEntityNew.setUnShippedQty(mmPurchasePackageItemEntity.getDesignQty() - mmShippingDetailEntityNew.getShippedQty());
//                mmShippingDetailEntityNew.setInventoryQty(mmShippingDetailEntityNew.getShippedQty());
//                mmShippingDetailEntityNew.setUnInventoryQty(0);
//
//                mmShippingDetailEntityNew.setMmMaterialCodeDescription(mmPurchasePackageItemEntity.getMmMaterialCodeDescription());
//                mmShippingDetailEntityNew.setQrCodeType(mmPurchasePackageItemEntity.getQrCodeType());
//                mmShippingDetailEntityNew.setWareHouseType(MaterialOrganizationType.PROJECT);
//                mmShippingDetailEntityNew.setCreatedAt(new Date());
//                mmShippingDetailEntityNew.setCreatedBy(contextDTO.getOperator().getId());
//                mmShippingDetailEntityNew.setLastModifiedAt(new Date());
//                mmShippingDetailEntityNew.setLastModifiedBy(contextDTO.getOperator().getId());
//                mmShippingDetailEntityNew.setStatus(EntityStatus.ACTIVE);
//                mmShippingDetailRepository.save(mmShippingDetailEntityNew);
//                createShippingDetails(
//                    mmShippingDetailEntityNew,
//                    mmShippingDetailsDTO.getShippedQty()
//                );
//            } else {
//                mmShippingDetailEntityOld.setLastModifiedAt(new Date());
//                mmShippingDetailEntityOld.setLastModifiedBy(contextDTO.getOperator().getId());
//                mmShippingDetailEntityOld.setStatus(EntityStatus.ACTIVE);
//                mmShippingDetailEntityOld.setShippedQty(mmShippingDetailsDTO.getShippedQty());
//                mmShippingDetailEntityOld.setUnShippedQty(0);
//                mmShippingDetailEntityOld.setInventoryQty(0);
//                mmShippingDetailEntityOld.setUnInventoryQty(mmShippingDetailEntityOld.getShippedQty());
//                mmShippingDetailRepository.save(mmShippingDetailEntityOld);
//                createShippingDetails(
//                    mmShippingDetailEntityOld,
//                    mmShippingDetailsDTO.getShippedQty()
//                );
//            }
        }
    }


    @Override
    public Page<MmReleaseReceiveEntity> searchReleaseReceive(Long orgId, Long projectId, Long shippingId, MmShippingSearchDTO mmShippingSearchDTO) {
        return mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndMmShippingIdAndStatus(orgId, projectId, shippingId, EntityStatus.ACTIVE, mmShippingSearchDTO.toPageable());
    }

    /**
     * 修改发货量。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param shippingDetailId
     * @param shippingDetailRelationId
     * @param contextDTO
     * @return
     */
    @Override
    public void updateQty(Long orgId, Long projectId, Long shippingId, Long shippingDetailId, Long shippingDetailRelationId, MmShippingDetailUpdateQtyDTO mmShippingDetailUpdateQtyDTO, ContextDTO contextDTO) {
        MmShippingDetailRelationEntity mmShippingDetailRelationEntity = mmShippingDetailRelationRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, shippingDetailRelationId, EntityStatus.ACTIVE);
        if (mmShippingDetailRelationEntity == null) {
            throw new BusinessError("发货单详情关系不存在");
        }
        if (mmShippingDetailRelationEntity.getQrCodeType().equals(QrCodeType.GOODS) && mmShippingDetailUpdateQtyDTO.getQty() > 1) {
            throw new BusinessError("一物一码的材料发货数量只能为1");
        }
//        mmShippingDetailRelationEntity.setInput(true);
//        mmShippingDetailRelationEntity.setShippedQty(mmShippingDetailUpdateQtyDTO.getQty());
        mmShippingDetailRelationEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmShippingDetailRelationEntity.setLastModifiedAt(new Date());
        mmShippingDetailRelationRepository.save(mmShippingDetailRelationEntity);
    }

    /**
     * 批量修改发货量。
     *
     * @param orgId
     * @param projectId
     * @param shippingId
     * @param shippingDetailId
     * @param contextDTO
     * @return
     */
    @Override
    public void batchUpdateQty(Long orgId, Long projectId, Long shippingId, Long shippingDetailId, MmShippingDetailUpdateQtyDTO mmShippingDetailUpdateQtyDTO, ContextDTO contextDTO) {
        List<MmShippingDetailRelationEntity> mmShippingDetailRelationEntities = mmShippingDetailRelationRepository.findByOrgIdAndProjectIdAndShippingDetailIdAndStatus(orgId, projectId, shippingDetailId, EntityStatus.ACTIVE);
        if (mmShippingDetailRelationEntities.size() <= 0) {
            return;
        }
        for (MmShippingDetailRelationEntity mmShippingDetailRelationEntity : mmShippingDetailRelationEntities) {
//            mmShippingDetailRelationEntity.setInput(true);
//            mmShippingDetailRelationEntity.setShippedQty(mmShippingDetailUpdateQtyDTO.getQty());
            mmShippingDetailRelationEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            mmShippingDetailRelationEntity.setLastModifiedAt(new Date());
            mmShippingDetailRelationRepository.save(mmShippingDetailRelationEntity);
        }
    }

    public MmImportBatchResultDTO importDetailItem(Long orgId, Long projectId, Long shippingId, OperatorDTO operator, MmImportBatchTask batchTask, MmImportBatchTaskImportDTO importDTO) {
        List<MmShippingDetailEntity> mmShippingDetailEntities = mmShippingDetailRepository.findByOrgIdAndProjectIdAndShippingIdAndStatusOrderByMmMaterialCodeNo(orgId, projectId, shippingId, EntityStatus.ACTIVE);
        if (!mmShippingDetailEntities.isEmpty()) {
            mmShippingDetailRepository.deleteAll(mmShippingDetailEntities);
        }

        List<MmShippingDetailRelationEntity> mmShippingDetailRelationEntities = mmShippingDetailRelationRepository.findByOrgIdAndProjectIdAndShippingIdAndStatus(
            orgId,
            projectId,
            shippingId,
            EntityStatus.ACTIVE
        );

        if (!mmShippingDetailRelationEntities.isEmpty()) {
            mmShippingDetailRelationRepository.deleteAll(mmShippingDetailRelationEntities);
        }

        Workbook workbook;
        File excel;
        // 读取已上传的导入文件
        try {
            excel = new File(temporaryDir, importDTO.getFileName());
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            throw new NotFoundError();
        }
        MmImportBatchResultDTO batchResult = new MmImportBatchResultDTO(batchTask);
        MmImportBatchResultDTO sheetImportResult;
        int sheetNum = workbook.getNumberOfSheets();
        if (sheetNum < 1) throw new BusinessError("there is no importSheet");
        sheetImportResult = importExcel(orgId, projectId, shippingId, operator, workbook.getSheetAt(0), batchResult, batchTask);

        batchResult.addLog(sheetImportResult.getProcessedCount() + " " + workbook.getSheetAt(0).getSheetName() + " imported.");
        // 保存工作簿
        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace(System.out); // TODO
        }
        return batchResult;
    }

    /**
     * 导入pi sheet
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param operator
     * @param sheet
     * @param batchResult
     * @return
     */
    private MmImportBatchResultDTO importExcel(
        Long orgId,
        Long projectId,
        Long shippingId,
        OperatorDTO operator,
        Sheet sheet,
        MmImportBatchResultDTO batchResult,
        MmImportBatchTask batchTask
    ) {

        Iterator<Row> rows = sheet.rowIterator();
        Row row;
        int totalCount = 0;
        int skippedCount = 0;
        int processedCount = 0;
        int errorCount = 0;
        // 清空当前发货二维码信息
        // 清空当前发货单详情信息
        List<MmShippingDetailEntity> mmShippingDetailEntities = mmShippingDetailRepository.findByOrgIdAndProjectIdAndShippingIdAndStatusOrderByMmMaterialCodeNo(orgId, projectId, shippingId, EntityStatus.ACTIVE);
        if (!mmShippingDetailEntities.isEmpty()) {
            for (MmShippingDetailEntity mmShippingDetailEntity : mmShippingDetailEntities) {
                List<MmShippingDetailRelationEntity> mmShippingDetailRelationEntities = mmShippingDetailRelationRepository.findByOrgIdAndProjectIdAndShippingDetailIdAndStatus(orgId, projectId, mmShippingDetailEntity.getId(), EntityStatus.ACTIVE);
                mmShippingDetailRelationRepository.deleteAll(mmShippingDetailRelationEntities);
            }
        }
        mmShippingDetailRepository.deleteAll(mmShippingDetailEntities);

        /*-------
        2.0 导入内容
        */
        while (rows.hasNext()) {

            row = rows.next();
            int colIndex = 0;
            if (row.getRowNum() < 3) {
                continue;
            }
            try {
                batchResult.addTotalCount(1);
                totalCount++;

                MmShippingEntity shippingEntityFind = mmShippingRepository.findByOrgIdAndProjectIdAndIdAndStatus(
                    orgId,
                    projectId,
                    shippingId,
                    EntityStatus.ACTIVE
                );
                if (null == shippingEntityFind) {
                    throw new BusinessError("发货单ID不正确");
                }

                MmPurchasePackageEntity mmPurchasePackageEntity = mmPurchasePackageRepository.findByOrgIdAndProjectIdAndIdAndStatus(
                    orgId,
                    projectId,
                    shippingEntityFind.getMmPurchasePackageId(),
                    EntityStatus.ACTIVE
                );
                if (null == mmPurchasePackageEntity) {
                    throw new BusinessError("发货包没有关联采购包");
                }

                String mmMaterialCodeNo = WorkbookUtils.readAsString(row, colIndex++);
                if (mmMaterialCodeNo == null || "".equals(mmMaterialCodeNo)) {
                    throw new BusinessError("材料编码不能为空");
                }
                MmMaterialCodeEntity mmMaterialCodeEntity = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndNoAndMaterialOrganizationTypeAndStatus(orgId, projectId, mmMaterialCodeNo, MaterialOrganizationType.PROJECT, EntityStatus.ACTIVE);
                if (mmMaterialCodeEntity == null) {
                    throw new BusinessError("材料编码不存在于当前项目");
                }
                String specDescription = WorkbookUtils.readAsString(row, colIndex++);

                String pieceTagNo = WorkbookUtils.readAsString(row, colIndex++);

                if (!StringUtils.isEmpty(pieceTagNo)) {
                    // 如果有件号，则判断项目中是否存在重复件号
                    MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndPieceTagNoAndStatus(
                        orgId,
                        projectId,
                        pieceTagNo,
                        EntityStatus.ACTIVE
                    );
                    if (null != mmReleaseReceiveDetailEntity) {
                        throw new BusinessError("件号: " + pieceTagNo + " 已经存在于当前项目中");
                    }

                    MmShippingDetailEntity mmShippingDetailEntity = mmShippingDetailRepository.findByOrgIdAndProjectIdAndPieceTagNoAndStatus(
                        orgId,
                        projectId,
                        pieceTagNo,
                        EntityStatus.ACTIVE
                    );
                    if (null != mmShippingDetailEntity) {
                        throw new BusinessError("件号: " + pieceTagNo + " 已经存在于当前项目中");
                    }
                }

                String heatBatchNo = WorkbookUtils.readAsString(row, colIndex++);
                if (heatBatchNo != null && !heatBatchNo.equals("")) {
                    String[] parts = heatBatchNo.split("/");
                    if (parts.length > 2) {
                        throw new BusinessError("炉批号格式不正确");
                    }
                }

                String materialCertificate = WorkbookUtils.readAsString(row, colIndex++);

                String totalQtyString = WorkbookUtils.readAsString(row, colIndex++);
                if (totalQtyString == null || "".equals(totalQtyString)) {
                    throw new BusinessError("采购总量不能为空");
                }
                Double totalQty = Double.valueOf(totalQtyString);

                if (totalQty.compareTo(0.0) <= 0) {
                    throw new BusinessError("采购总量必须大于0");
                }

                String designUnit = WorkbookUtils.readAsString(row, colIndex++);
                if (designUnit == null || "".equals(designUnit)) {
                    throw new BusinessError("计量单位不能为空");
                }
                if (!designUnit.equals(mmMaterialCodeEntity.getUnit())) {
                    throw new BusinessError("计量单位与材料编码对应单位不符");
                }

                // 查找单件规格量
                String specValueString = WorkbookUtils.readAsString(row, colIndex++);
                String pieceQtyString = WorkbookUtils.readAsString(row, colIndex++);
                Double specValue = 0.0;
                Integer pieceQty = 0;

                if (null != mmMaterialCodeEntity.getNeedSpec() && mmMaterialCodeEntity.getNeedSpec()) {
                    if (specValueString == null || "".equals(specValueString)) {
                        throw new BusinessError("放行数量不能为空");
                    }
                    if (pieceQtyString == null || "".equals(pieceQtyString)) {
                        throw new BusinessError("放行数量不能为空");
                    }
                    specValue = Double.valueOf(specValueString);
                    pieceQty = Integer.parseInt(pieceQtyString);
                    if (specValue.compareTo(0.0) <= 0) {
                        throw new BusinessError("规格量必须大于0");
                    }
                    if (pieceQty <= 0) {
                        throw new BusinessError("件数必须大于0");
                    }
                    // 判断计算总量和填写总量是否相等
                    Double computeQty = specValue * pieceQty;
                    if (Double.compare(computeQty, totalQty) != 0) {
                        throw new BusinessError("F和G列的乘积与D列不匹配");
                    }
                } else {
                    if (mmMaterialCodeEntity.getQrCodeType().equals(QrCodeType.GOODS) && !totalQtyString.matches("\\d+")) {
                        throw new BusinessError("一物一码的材料没有规格量管理时，总量的值应为正整数");
                    }
                }

                String wareHouse = WorkbookUtils.readAsString(row, colIndex++);
                if (wareHouse == null || "".equals(wareHouse)) {
                    throw new BusinessError("仓库/货架不能为空");
                }
                MmWareHouseLocationEntity wareHouseLocationEntity = mmWareHouseLocationRepository.findByOrgIdAndProjectIdAndNameAndStatus(orgId, projectId, wareHouse, EntityStatus.ACTIVE);
                if (wareHouseLocationEntity == null) {
                    throw new BusinessError("仓库/货架不存在于当前项目");
                }

                String shippingNo = WorkbookUtils.readAsString(row, colIndex++);

                String remarks = WorkbookUtils.readAsString(row, colIndex++);

                // 判断发货单的详细是否存在于采购包中及数据的有效性
                MmPurchasePackageItemEntity mmPurchasePackageItemEntity = isInPurchasePackage(
                    orgId,
                    projectId,
                    mmPurchasePackageEntity.getId(),
                    mmMaterialCodeNo,
                    specDescription,
                    specValue
                );
                if (null == mmPurchasePackageItemEntity) {
                    throw new BusinessError("导入数据和采购包数据不匹配，请重新关联采购包或确认数据的正确性");
                }

                // 查找是否存在相同数据在发货单详情中
                MmShippingDetailEntity findMmShippingDetailEntity = findShippingDetail(
                    orgId,
                    projectId,
                    shippingId,
                    mmMaterialCodeNo,
                    specDescription,
                    pieceTagNo,
                    heatBatchNo,
                    materialCertificate,
                    wareHouseLocationEntity.getId(),
                    shippingNo,
                    specValue,
                    mmMaterialCodeEntity.getNeedSpec()
                );
                if (null != findMmShippingDetailEntity) {

                    if (null != mmMaterialCodeEntity.getNeedSpec() && mmMaterialCodeEntity.getNeedSpec()) {
                        findMmShippingDetailEntity.setTotalQty(findMmShippingDetailEntity.getTotalQty() + totalQty);
                        findMmShippingDetailEntity.setPieceQty(findMmShippingDetailEntity.getPieceQty() + pieceQty);
                        findMmShippingDetailEntity.setShippedTotalQty(findMmShippingDetailEntity.getTotalQty() + totalQty);
                        findMmShippingDetailEntity.setShippedPieceQty(findMmShippingDetailEntity.getPieceQty() + pieceQty);

                    } else {
                        findMmShippingDetailEntity.setTotalQty(findMmShippingDetailEntity.getTotalQty() + totalQty);
                        findMmShippingDetailEntity.setShippedTotalQty(findMmShippingDetailEntity.getTotalQty() + totalQty);
                    }
                    findMmShippingDetailEntity.setLastModifiedAt(new Date());
                    findMmShippingDetailEntity.setLastModifiedBy(operator.getId());
                    findMmShippingDetailEntity.setStatus(EntityStatus.ACTIVE);
                    mmShippingDetailRepository.save(findMmShippingDetailEntity);
                    createShippingDetails(findMmShippingDetailEntity, mmMaterialCodeEntity.getNeedSpec(), totalQty, pieceQty);
                } else {
                    MmShippingDetailEntity mmShippingDetailEntity = new MmShippingDetailEntity();
                    mmShippingDetailEntity.setOrgId(orgId);
                    mmShippingDetailEntity.setProjectId(projectId);
                    mmShippingDetailEntity.setShippingId(shippingId);

                    mmShippingDetailEntity.setPurchasePackageNo(mmPurchasePackageEntity.getPwpsNumber());
                    mmShippingDetailEntity.setPurchasePackageId(mmPurchasePackageEntity.getId());
                    mmShippingDetailEntity.setIdentCode(mmMaterialCodeEntity.getIdentCode());
                    mmShippingDetailEntity.setQrCodeType(mmMaterialCodeEntity.getQrCodeType());
                    mmShippingDetailEntity.setMmMaterialCodeNo(mmMaterialCodeNo);
                    mmShippingDetailEntity.setMmMaterialCodeDescription(mmMaterialCodeEntity.getDescription());
                    mmShippingDetailEntity.setSpecDescription(specDescription);
                    mmShippingDetailEntity.setPieceTagNo(pieceTagNo);
                    mmShippingDetailEntity.setHeatBatchNo(heatBatchNo);
                    mmShippingDetailEntity.setMaterialCertificate(materialCertificate);
                    mmShippingDetailEntity.setTotalQty(totalQty);
                    mmShippingDetailEntity.setShippedTotalQty(totalQty);
                    mmShippingDetailEntity.setDesignUnit(designUnit);
                    mmShippingDetailEntity.setSpecValue(specValue);
                    mmShippingDetailEntity.setPieceQty(pieceQty);
                    mmShippingDetailEntity.setShippedPieceQty(pieceQty);
                    mmShippingDetailEntity.setWareHouseLocationId(wareHouseLocationEntity.getId());
                    mmShippingDetailEntity.setWareHouseLocationName(wareHouseLocationEntity.getName());
                    mmShippingDetailEntity.setShippingNo(shippingNo);
                    mmShippingDetailEntity.setRemarks(remarks);

                    mmShippingDetailEntity.setCreatedAt(new Date());
                    mmShippingDetailEntity.setCreatedBy(operator.getId());
                    mmShippingDetailEntity.setLastModifiedAt(new Date());
                    mmShippingDetailEntity.setLastModifiedBy(operator.getId());
                    mmShippingDetailEntity.setStatus(EntityStatus.ACTIVE);
                    mmShippingDetailRepository.save(mmShippingDetailEntity);
                    createShippingDetails(mmShippingDetailEntity, mmMaterialCodeEntity.getNeedSpec(), totalQty, pieceQty);
                }

                processedCount++;
                batchResult.addProcessedCount(1);

            } catch (Exception e) {
                e.printStackTrace(System.out);
                errorCount++;
                batchResult.addErrorCount(1);
                setImportDataErrorMessage(row, "" + colIndex + "th column import error." + e.getMessage());
            }
            batchTask.setResult(batchResult);
            batchTask.setLastModifiedAt();
            mmImportBatchTaskRepository.save(batchTask);
        }
        return new MmImportBatchResultDTO(totalCount, processedCount, skippedCount, errorCount);
    }

    /**
     * 设置导入数据错误信息。
     *
     * @param row     错误所在行
     * @param message 错误消息
     */
    private void setImportDataErrorMessage(Row row, String message) {
        row.createCell(row.getLastCellNum()).setCellValue(message);
    }


    private void createShippingDetails(
        MmShippingDetailEntity mmShippingDetailEntity,
        Boolean needSpec,
        Double totalQty,
        Integer pieceQty
    ) {

        List<MmShippingDetailRelationEntity> mmShippingDetailRelationEntities = mmShippingDetailRelationRepository.findByOrgIdAndProjectIdAndShippingDetailIdAndStatus(
            mmShippingDetailEntity.getOrgId(),
            mmShippingDetailEntity.getProjectId(),
            mmShippingDetailEntity.getId(),
            EntityStatus.ACTIVE);
        if (!mmShippingDetailRelationEntities.isEmpty()) {
            mmShippingDetailRelationRepository.deleteAll(mmShippingDetailRelationEntities);
        }

        if (mmShippingDetailEntity.getQrCodeType().equals(QrCodeType.GOODS)) {
            Integer count = 0;
            if (null != needSpec && needSpec) {
                count = pieceQty;
            } else {
                count = totalQty.intValue();
            }
            for (int i = 0; i < count; i++) {
                MmShippingDetailRelationEntity mmShippingDetailRelationEntity = new MmShippingDetailRelationEntity();
                BeanUtils.copyProperties(
                    mmShippingDetailEntity,
                    mmShippingDetailRelationEntity, "id"
                );
                if (null != needSpec && needSpec) {
                    mmShippingDetailRelationEntity.setPieceQty(1);
                    mmShippingDetailRelationEntity.setPieceInventoryQty(1);
                    mmShippingDetailRelationEntity.setTotalInventoryQty(mmShippingDetailEntity.getSpecValue());
                    mmShippingDetailRelationEntity.setTotalQty(mmShippingDetailEntity.getSpecValue());
                } else {
                    mmShippingDetailRelationEntity.setTotalInventoryQty(1.0);
                    mmShippingDetailRelationEntity.setTotalQty(1.0);
                }
                mmShippingDetailRelationEntity.setQrCode(QrcodePrefixType.MATERTIAL.getCode() + StringUtils.generateShortUuid());

                mmShippingDetailRelationEntity.setShippingDetailId(mmShippingDetailEntity.getId());
                mmShippingDetailRelationEntity.setCreatedAt(new Date());
                mmShippingDetailRelationEntity.setLastModifiedAt(new Date());
                mmShippingDetailRelationEntity.setStatus(EntityStatus.ACTIVE);
                mmShippingDetailRelationRepository.save(mmShippingDetailRelationEntity);
            }

        } else {
            if (null != needSpec && needSpec) {
                MmShippingDetailRelationEntity mmShippingDetailRelationEntity = new MmShippingDetailRelationEntity();
                BeanUtils.copyProperties(
                    mmShippingDetailEntity,
                    mmShippingDetailRelationEntity, "id"
                );
                mmShippingDetailRelationEntity.setTotalInventoryQty(totalQty);
                mmShippingDetailRelationEntity.setTotalQty(totalQty);
                mmShippingDetailRelationEntity.setPieceInventoryQty(pieceQty);
                mmShippingDetailRelationEntity.setPieceQty(pieceQty);
                mmShippingDetailRelationEntity.setQrCode(QrcodePrefixType.MATERTIAL.getCode() + StringUtils.generateShortUuid());

                mmShippingDetailRelationEntity.setCreatedAt(new Date());
                mmShippingDetailRelationEntity.setCreatedBy(mmShippingDetailEntity.getCreatedBy());
                mmShippingDetailRelationEntity.setLastModifiedAt(new Date());
                mmShippingDetailRelationEntity.setLastModifiedBy(mmShippingDetailEntity.getCreatedBy());
                mmShippingDetailRelationEntity.setStatus(EntityStatus.ACTIVE);
                // TODO 查询在库材料是否已有此材料编码的二维码信息
                MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndMmMaterialCodeNoAndPieceTagNoAndHeatBatchNoAndMaterialCertificateAndSpecValueAndStatus(
                    mmShippingDetailEntity.getOrgId(),
                    mmShippingDetailEntity.getProjectId(),
                    mmShippingDetailEntity.getMmMaterialCodeNo(),
                    mmShippingDetailEntity.getPieceTagNo(),
                    mmShippingDetailEntity.getHeatBatchNo(),
                    mmShippingDetailEntity.getMaterialCertificate(),
                    mmShippingDetailEntity.getSpecValue(),
                    EntityStatus.ACTIVE);
                if (mmMaterialInStockDetailQrCodeEntity != null) {
                    mmShippingDetailRelationEntity.setQrCode(mmMaterialInStockDetailQrCodeEntity.getQrCode());
                } else {
                    mmShippingDetailRelationEntity.setQrCode(QrcodePrefixType.MATERTIAL.getCode() + StringUtils.generateShortUuid());
                }
                mmShippingDetailRelationEntity.setShippingDetailId(mmShippingDetailEntity.getId());
                mmShippingDetailRelationEntity.setQrCodeType(QrCodeType.TYPE);
                mmShippingDetailRelationRepository.save(mmShippingDetailRelationEntity);
            } else {
                MmShippingDetailRelationEntity mmShippingDetailRelationEntity = new MmShippingDetailRelationEntity();
                BeanUtils.copyProperties(
                    mmShippingDetailEntity,
                    mmShippingDetailRelationEntity,
                    "id"
                );
                mmShippingDetailRelationEntity.setTotalInventoryQty(totalQty);
                mmShippingDetailRelationEntity.setTotalQty(totalQty);
                mmShippingDetailRelationEntity.setQrCode(QrcodePrefixType.MATERTIAL.getCode() + StringUtils.generateShortUuid());

                mmShippingDetailRelationEntity.setCreatedAt(new Date());
                mmShippingDetailRelationEntity.setCreatedBy(mmShippingDetailEntity.getCreatedBy());
                mmShippingDetailRelationEntity.setLastModifiedAt(new Date());
                mmShippingDetailRelationEntity.setLastModifiedBy(mmShippingDetailEntity.getCreatedBy());
                mmShippingDetailRelationEntity.setStatus(EntityStatus.ACTIVE);
                // TODO 查询在库材料是否已有此材料编码的二维码信息
                MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndMmMaterialCodeNoAndPieceTagNoAndHeatBatchNoAndMaterialCertificateAndSpecValueAndStatus(
                    mmShippingDetailEntity.getOrgId(),
                    mmShippingDetailEntity.getProjectId(),
                    mmShippingDetailEntity.getMmMaterialCodeNo(),
                    mmShippingDetailEntity.getPieceTagNo(),
                    mmShippingDetailEntity.getHeatBatchNo(),
                    mmShippingDetailEntity.getMaterialCertificate(),
                    mmShippingDetailEntity.getSpecValue(),
                    EntityStatus.ACTIVE);
                if (mmMaterialInStockDetailQrCodeEntity != null) {
                    mmShippingDetailRelationEntity.setQrCode(mmMaterialInStockDetailQrCodeEntity.getQrCode());
                } else {
                    mmShippingDetailRelationEntity.setQrCode(QrcodePrefixType.MATERTIAL.getCode() + StringUtils.generateShortUuid());
                }
                mmShippingDetailRelationEntity.setShippingDetailId(mmShippingDetailEntity.getId());
                mmShippingDetailRelationEntity.setQrCodeType(QrCodeType.TYPE);
                mmShippingDetailRelationRepository.save(mmShippingDetailRelationEntity);
            }
        }
    }

    private MmPurchasePackageItemEntity isInPurchasePackage(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        String materialCode,
        String specDescription,
        Double specValue
    ) {
        return mmPurchasePackageItemRepository.findByOrgIdAndProjectIdAndPurchasePackageIdAndMmMaterialCodeNoAndSpecDescriptionAndSpecValueAndStatus(
            orgId,
            projectId,
            purchasePackageId,
            materialCode,
            specDescription,
            specValue,
            EntityStatus.ACTIVE
        );
    }

    private MmShippingDetailEntity findShippingDetail(
        Long orgId,
        Long projectId,
        Long shippingId,
        String mmMaterialCodeNo,
        String specDescription,
        String pieceTagNo,
        String heatBatchNo,
        String materialCertificate,
        Long wareHouseLocationId,
        String shippingNo,
        Double specValue,
        Boolean needSpec
    ) {
        if (null != needSpec && needSpec) {
            return mmShippingDetailRepository.findByOrgIdAndProjectIdAndShippingIdAndMmMaterialCodeNoAndSpecDescriptionAndPieceTagNoAndHeatBatchNoAndMaterialCertificateAndWareHouseLocationIdAndShippingNoAndSpecValueAndStatus(
                orgId,
                projectId,
                shippingId,
                mmMaterialCodeNo,
                specDescription,
                pieceTagNo,
                heatBatchNo,
                materialCertificate,
                wareHouseLocationId,
                shippingNo,
                specValue,
                EntityStatus.ACTIVE
            );
        } else {
            return mmShippingDetailRepository.findByOrgIdAndProjectIdAndShippingIdAndMmMaterialCodeNoAndSpecDescriptionAndPieceTagNoAndHeatBatchNoAndMaterialCertificateAndWareHouseLocationIdAndShippingNoAndStatus(
                orgId,
                projectId,
                shippingId,
                mmMaterialCodeNo,
                specDescription,
                pieceTagNo,
                heatBatchNo,
                materialCertificate,
                wareHouseLocationId,
                shippingNo,
                EntityStatus.ACTIVE
            );
        }
    }
}
