package com.ose.material.domain.model.service.impl;

import com.ose.exception.BusinessError;
import com.ose.material.domain.model.repository.*;
import com.ose.material.domain.model.service.MmMaterialInStockInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.*;
import com.ose.vo.EntityStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class MmMaterialInStockService implements MmMaterialInStockInterface {

    @Value("${application.files.temporary}")
    private String temporaryDir;

    /**
     * 查找在库材料列表 操作仓库。
     */
    private final MmMaterialInStockRepository mmMaterialInStockRepository;
    private final MmMaterialInStockDetailRepository mmMaterialInStockDetailRepository;
    private final MmMaterialInStockDetailQrCodeRepository mmMaterialInStockDetailQrCodeRepository;
    private final MmReleaseReceiveDetailRepository mmReleaseReceiveDetailRepository;
    private final MmReleaseReceiveDetailQrCodeRepository mmReleaseReceiveDetailQrCodeRepository;
    private final MmIssueDetailQrCodeRepository mmIssueDetailQrCodeRepository;
    private final MmIssueDetailRepository mmIssueDetailRepository;

    private final HeatBatchNoRepository heatBatchNoRepository;

    /**
     * 构造方法。
     *
     * @param mmMaterialInStockRepository 查找在库材料列表 操作仓库
     */
    @Autowired
    public MmMaterialInStockService(
        MmMaterialInStockRepository mmMaterialInStockRepository,
        MmMaterialInStockDetailRepository mmMaterialInStockDetailRepository,
        MmMaterialInStockDetailQrCodeRepository mmMaterialInStockDetailQrCodeRepository,
        HeatBatchNoRepository heatBatchNoRepository,
        MmReleaseReceiveDetailQrCodeRepository mmReleaseReceiveDetailQrCodeRepository,
        MmIssueDetailQrCodeRepository mmIssueDetailQrCodeRepository,
        MmReleaseReceiveDetailRepository mmReleaseReceiveDetailRepository,
        MmIssueDetailRepository mmIssueDetailRepository
    ) {
        this.mmMaterialInStockRepository = mmMaterialInStockRepository;
        this.mmMaterialInStockDetailRepository = mmMaterialInStockDetailRepository;
        this.mmMaterialInStockDetailQrCodeRepository = mmMaterialInStockDetailQrCodeRepository;
        this.heatBatchNoRepository = heatBatchNoRepository;
        this.mmReleaseReceiveDetailQrCodeRepository = mmReleaseReceiveDetailQrCodeRepository;
        this.mmIssueDetailQrCodeRepository = mmIssueDetailQrCodeRepository;
        this.mmReleaseReceiveDetailRepository = mmReleaseReceiveDetailRepository;
        this.mmIssueDetailRepository = mmIssueDetailRepository;
    }

    /**
     * 查找在库材料列表。
     *
     * @param orgId
     * @param projectId
     * @param mmMaterialInStockSearchDTO
     * @return
     */
    @Override
    public Page<MmMaterialInStockEntity> search(
        Long orgId,
        Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    ) {
        return mmMaterialInStockRepository.search(
            orgId,
            projectId,
            mmMaterialInStockSearchDTO
        );
    }

    /**
     * 查找在库材料列表。
     *
     * @param orgId
     * @param projectId
     * @param mmMaterialInStockSearchDTO
     * @return
     */
    @Override
    public MmMaterialInStockEntity detail(
        Long orgId,
        Long projectId,
        Long MaterialStockId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    ) {
        return mmMaterialInStockRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(
            orgId,
            projectId,
            MaterialStockId
        );
    }

    /**
     * 查找在库材料列表。
     *
     * @param orgId
     * @param projectId
     * @param mmMaterialInStockSearchDTO
     * @return
     */
    @Override
    public Page<MmMaterialInStockDetailEntity> searchDetail(
        Long orgId,
        Long projectId,
        Long materialStockEntityId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    ) {
        return mmMaterialInStockDetailRepository.findByOrgIdAndProjectIdAndMmMaterialInStockIdAndStatus(
            orgId,
            projectId,
            materialStockEntityId,
            EntityStatus.ACTIVE,
            mmMaterialInStockSearchDTO.toPageable()
        );
    }

    /**
     * 查找在库材料明细。
     *
     * @param orgId
     * @param projectId
     * @param mmMaterialInStockSearchDTO
     * @return
     */
    @Override
    public Page<MmMaterialInStockDetailQrCodeEntity> searchDetailQrCode(
        Long orgId,
        Long projectId,
        Long materialStockEntityId,
        Long materialStockDetailEntityId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    ) {
        return mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndMmMaterialInStockDetailIdAndStatus(
            orgId,
            projectId,
            materialStockDetailEntityId,
            EntityStatus.ACTIVE,
            mmMaterialInStockSearchDTO.toPageable()
        );
    }

    @Override
    public MmReleaseReceiveQrCodeResultDTO searchQrCode(
        Long orgId,
        Long projectId,
        String qrCode
    ) {

        MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndQrCodeAndStatus(
            orgId,
            projectId,
            qrCode,
            EntityStatus.ACTIVE
        );
        if (null == mmMaterialInStockDetailQrCodeEntity) {
            mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndPieceTagNoAndStatus(
                orgId,
                projectId,
                qrCode,
                EntityStatus.ACTIVE
            );
        }

        if (mmMaterialInStockDetailQrCodeEntity == null) {
            throw new BusinessError("在库材料二维码信息不存在");
        }

        MmMaterialInStockDetailEntity mmMaterialInStockDetailEntity = mmMaterialInStockDetailRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            mmMaterialInStockDetailQrCodeEntity.getMmMaterialInStockDetailId(),
            EntityStatus.ACTIVE
        );

        if (mmMaterialInStockDetailEntity == null) {
            throw new BusinessError("在库材料详情信息不存在");
        }

        MmReleaseReceiveQrCodeResultDTO mmReleaseReceiveQrCodeResultDTO = new MmReleaseReceiveQrCodeResultDTO();

        BeanUtils.copyProperties(
            mmMaterialInStockDetailEntity,
            mmReleaseReceiveQrCodeResultDTO
        );
        mmReleaseReceiveQrCodeResultDTO.setQrCode(mmMaterialInStockDetailQrCodeEntity.getQrCode());
        mmReleaseReceiveQrCodeResultDTO.setPieceTagNo(mmMaterialInStockDetailQrCodeEntity.getPieceTagNo());
//        mmReleaseReceiveQrCodeResultDTO.setRunningStatus(mmMaterialInStockDetailQrCodeEntity.getRunningStatus());
        return mmReleaseReceiveQrCodeResultDTO;
    }

    /**
     * 查找在库材料详情列表。
     */
    @Override
    public Page<MmMaterialInStockDetailEntity> searchMaterialDetail(
        Long orgId,
        Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    ) {
        return mmMaterialInStockDetailRepository.search(
            orgId,
            projectId,
            EntityStatus.ACTIVE,
            mmMaterialInStockSearchDTO
        );
    }

    @Override
    public MmQrCodeResultDTO qrCode(
        Long orgId,
        Long projectId,
        String qrCode
    ) {
        MmQrCodeResultDTO mmQrCodeResultDTO = new MmQrCodeResultDTO();

        // TODO 查找入库单
        List<MmReleaseReceiveDetailQrCodeEntity> mmReleaseReceiveDetailQrCodeEntities = mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndQrCodeAndStatus(
            orgId,
            projectId,
            qrCode,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveDetailQrCodeEntities == null || mmReleaseReceiveDetailQrCodeEntities.size() == 0) {
            throw new BusinessError("二维码信息不存在于当前项目");
        }

        MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            mmReleaseReceiveDetailQrCodeEntities.get(0).getReleaseReceiveDetailId(),
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveDetailEntity == null) {
            throw new BusinessError("当前二维对应材料信息不存在");
        }

        BeanUtils.copyProperties(
            mmReleaseReceiveDetailEntity,
            mmQrCodeResultDTO
        );
        mmQrCodeResultDTO.setMmMaterialCodeDescription(mmReleaseReceiveDetailEntity.getMmMaterialCodeDescription());
        mmQrCodeResultDTO.setQrCodeType(mmReleaseReceiveDetailEntity.getQrCodeType().getDisplayName());
        // TODO 先找在库材料
        MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndQrCodeAndStatus(
            orgId,
            projectId,
            qrCode,
            EntityStatus.ACTIVE
        );
        if (mmMaterialInStockDetailQrCodeEntity != null) {

            MmMaterialInStockDetailEntity mmMaterialInStockDetailEntity = mmMaterialInStockDetailRepository.findByOrgIdAndProjectIdAndIdAndStatus(
                orgId,
                projectId,
                mmMaterialInStockDetailQrCodeEntity.getMmMaterialInStockDetailId(),
                EntityStatus.ACTIVE
            );
//            if (mmMaterialInStockDetailEntity != null) {
//                mmQrCodeResultDTO.setInStockQty(mmMaterialInStockDetailEntity.getInStockQty());
//                mmQrCodeResultDTO.setIssuedQty(mmMaterialInStockDetailEntity.getIssuedQty());
//            }
        }

        return mmQrCodeResultDTO;

    }

    @Override
    public Page<MmReleaseReceiveQrCodeResultDTO> searchQrCodes(
        Long orgId,
        Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    ) {
        return mmMaterialInStockDetailRepository.searchQrCodes(orgId, projectId, mmMaterialInStockSearchDTO);
    }

    /**
     * 查找在库材料详情列表。
     */
    @Override
    public MmMaterialInStockDetailQrCodeEntity searchMaterialInformation(
        Long orgId,
        Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    ) {
        if (null == mmMaterialInStockSearchDTO.getQrCode() && null == mmMaterialInStockSearchDTO.getPieceTagNo()) {
            throw new BusinessError("二维码和件号不能同时为空");
        }

        if (null != mmMaterialInStockSearchDTO.getQrCode()) {
            return mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndQrCodeAndStatus(
                orgId,
                projectId,
                mmMaterialInStockSearchDTO.getQrCode(),
                EntityStatus.ACTIVE
            );
        } else if (null != mmMaterialInStockSearchDTO.getPieceTagNo()) {
            return mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndPieceTagNoAndStatus(
                orgId,
                projectId,
                mmMaterialInStockSearchDTO.getPieceTagNo(),
                EntityStatus.ACTIVE
            );
        } else {
            return null;
        }
    }
}
