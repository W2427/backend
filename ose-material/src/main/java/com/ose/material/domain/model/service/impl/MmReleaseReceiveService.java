package com.ose.material.domain.model.service.impl;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.material.domain.model.repository.*;
import com.ose.material.domain.model.service.MmImportBatchTaskInterface;
import com.ose.material.domain.model.service.MmReleaseReceiveInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.*;
import com.ose.material.vo.*;
import com.ose.util.FileUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.DisciplineCode;
import com.ose.vo.EntityStatus;
import com.ose.vo.QrcodePrefixType;
import org.apache.poi.ss.usermodel.*;
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

@Component
public class MmReleaseReceiveService implements MmReleaseReceiveInterface {

    @Value("${application.files.temporary}")
    private String temporaryDir;

    /**
     * 入库单  操作仓库。
     */
    private final MmReleaseReceiveRepository mmReleaseReceiveRepository;
    private final MmReleaseReceiveDetailRepository mmReleaseReceiveDetailRepository;
    private final HeatBatchNoRepository heatBatchNoRepository;
    private final MmReleaseReceiveDetailQrCodeRepository mmReleaseReceiveDetailQrCodeRepository;
    private final MmMaterialInStockDetailRepository mmMaterialInStockDetailRepository;
    private final MmMaterialInStockRepository mmMaterialInStockRepository;
    private final MmMaterialInStockDetailQrCodeRepository mmMaterialInStockDetailQrCodeRepository;
    private final MmMaterialCodeRepository mmMaterialCodeRepository;
    private final MmShippingRepository mmShippingRepository;
    private final MmShippingDetailRepository mmShippingDetailRepository;
    private final MmShippingDetailRelationRepository mmShippingDetailRelationRepository;
    private final MmWareHouseLocationRepository mmWareHouseLocationRepository;
    private final MmImportBatchTaskRepository mmImportBatchTaskRepository;
    private MmImportBatchTaskInterface materialbatchTaskService;

    // 导出Excel数据输出行
    private static final int DATA_START_ROW = 3;
    // 模版行数
    private static final int TEMPLATE_ROW_COUNT = 6;

    /**
     * 构造方法。
     *
     * @param mmReleaseReceiveRepository 入库单管理 操作仓库
     */
    @Autowired
    public MmReleaseReceiveService(
        MmReleaseReceiveRepository mmReleaseReceiveRepository,
        HeatBatchNoRepository heatBatchNoRepository,
        MmReleaseReceiveDetailRepository mmReleaseReceiveDetailRepository,
        MmReleaseReceiveDetailQrCodeRepository mmReleaseReceiveDetailQrCodeRepository,
        MmMaterialInStockDetailRepository mmMaterialInStockDetailRepository,
        MmMaterialInStockRepository mmMaterialInStockRepository,
        MmMaterialInStockDetailQrCodeRepository mmMaterialInStockDetailQrCodeRepository,
        MmMaterialCodeRepository mmMaterialCodeRepository,
        MmShippingRepository mmShippingRepository,
        MmShippingDetailRepository mmShippingDetailRepository,
        MmShippingDetailRelationRepository mmShippingDetailRelationRepository,
        MmWareHouseLocationRepository mmWareHouseLocationRepository,
        MmImportBatchTaskRepository mmImportBatchTaskRepository,
        MmImportBatchTaskInterface materialbatchTaskService
    ) {
        this.mmReleaseReceiveRepository = mmReleaseReceiveRepository;
        this.heatBatchNoRepository = heatBatchNoRepository;
        this.mmReleaseReceiveDetailRepository = mmReleaseReceiveDetailRepository;
        this.mmReleaseReceiveDetailQrCodeRepository = mmReleaseReceiveDetailQrCodeRepository;
        this.mmMaterialInStockDetailRepository = mmMaterialInStockDetailRepository;
        this.mmMaterialInStockRepository = mmMaterialInStockRepository;
        this.mmMaterialInStockDetailQrCodeRepository = mmMaterialInStockDetailQrCodeRepository;
        this.mmMaterialCodeRepository = mmMaterialCodeRepository;
        this.mmShippingRepository = mmShippingRepository;
        this.mmShippingDetailRepository = mmShippingDetailRepository;
        this.mmShippingDetailRelationRepository = mmShippingDetailRelationRepository;
        this.mmWareHouseLocationRepository = mmWareHouseLocationRepository;
        this.mmImportBatchTaskRepository = mmImportBatchTaskRepository;
        this.materialbatchTaskService = materialbatchTaskService;
    }

    /**
     * 查询入库单列表。
     *
     * @param orgId
     * @param projectId
     * @param mmReleaseReceiveSearchDTO
     * @return
     */
    @Override
    public Page<MmReleaseReceiveEntity> search(
        Long orgId,
        Long projectId,
        MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO
    ) {
        return mmReleaseReceiveRepository.search(
            orgId,
            projectId,
            mmReleaseReceiveSearchDTO
        );
    }

    /**
     * 创建入库单。
     *
     * @param orgId
     * @param projectId
     * @param mmReleaseReceiveCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmReleaseReceiveEntity create(
        Long orgId,
        Long projectId,
        MmReleaseReceiveCreateDTO mmReleaseReceiveCreateDTO,
        ContextDTO contextDTO
    ) {

        MmReleaseReceiveEntity mmReleaseReceiveEntityFind = mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndNameAndStatus(
            orgId,
            projectId,
            mmReleaseReceiveCreateDTO.getName(),
            EntityStatus.ACTIVE
        );

        if (mmReleaseReceiveEntityFind != null) {
            throw new BusinessError("Release Receive name already exists!入库单名称已经存在！");
        }
        MmShippingEntity mmShippingEntity = mmShippingRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            mmReleaseReceiveCreateDTO.getMmShippingId(),
            EntityStatus.ACTIVE
        );

        MmReleaseReceiveEntity mmReleaseReceiveEntity = new MmReleaseReceiveEntity();

        BeanUtils.copyProperties(mmReleaseReceiveCreateDTO, mmReleaseReceiveEntity);

        SeqNumberDTO seqNumberDTO = mmReleaseReceiveRepository.getMaxSeqNumber(orgId, projectId).orElse(new SeqNumberDTO(0));
        int seqNumber = seqNumberDTO.getNewSeqNumber();

        mmReleaseReceiveEntity.setSeqNumber(seqNumber);
        mmReleaseReceiveEntity.setNo(String.format("%s-%04d", MaterialPrefixType.MATERIAL_RECEIVE.getCode(), seqNumber));

        mmReleaseReceiveEntity.setOrgId(orgId);
        mmReleaseReceiveEntity.setProjectId(projectId);
        mmReleaseReceiveEntity.setCompanyId(mmReleaseReceiveCreateDTO.getCompanyId());
        mmReleaseReceiveEntity.setMmShippingId(mmShippingEntity == null ? null : mmShippingEntity.getId());
        mmReleaseReceiveEntity.setMmShippingNumber(mmShippingEntity == null ? null : mmShippingEntity.getNo());
        mmReleaseReceiveEntity.setCreatedAt(new Date());
        mmReleaseReceiveEntity.setCreatedBy(contextDTO.getOperator().getId());
        mmReleaseReceiveEntity.setLastModifiedAt(new Date());
        mmReleaseReceiveEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmReleaseReceiveEntity.setStatus(EntityStatus.ACTIVE);
        mmReleaseReceiveEntity.setRunningStatus(EntityStatus.INIT);
        if (mmShippingEntity != null) {
            createDetail(
                orgId,
                projectId,
                mmShippingEntity.getId(),
                mmReleaseReceiveEntity.getId(),
                mmReleaseReceiveCreateDTO.getMmReleaseReceiveType(),
                contextDTO
            );
        }
        mmReleaseReceiveRepository.save(mmReleaseReceiveEntity);
        return mmReleaseReceiveEntity;
    }

    private void createDetail(
        Long orgId,
        Long projectId,
        Long mmShippingId,
        Long releaseReceiveId,
        MmReleaseReceiveType mmReleaseReceiveType,
        ContextDTO contextDTO
    ) {

        List<MmShippingDetailEntity> mmShippingDetailEntities = mmShippingDetailRepository.findByOrgIdAndProjectIdAndShippingIdAndStatusOrderByMmMaterialCodeNo(
            orgId,
            projectId,
            mmShippingId,
            EntityStatus.ACTIVE
        );
        if (mmShippingDetailEntities.isEmpty()) {
            throw new BusinessError("发货单详情不存在");
        }

        for (MmShippingDetailEntity mmShippingDetailEntity : mmShippingDetailEntities) {

            MmMaterialCodeEntity mmMaterialCodeEntity = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndNoAndStatus(
                orgId,
                projectId,
                mmShippingDetailEntity.getMmMaterialCodeNo(),
                EntityStatus.ACTIVE
            );

            MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity = new MmReleaseReceiveDetailEntity();
            BeanUtils.copyProperties(mmShippingDetailEntity, mmReleaseReceiveDetailEntity, "id");
            mmReleaseReceiveDetailEntity.setReleaseReceiveId(releaseReceiveId);
            mmReleaseReceiveDetailEntity.setReceivePieceQty(0);
            mmReleaseReceiveDetailEntity.setReceiveTotalQty(0.0);
//            if (null == mmReleaseReceiveType) {
//                mmReleaseReceiveDetailEntity.setReceivePieceQty(mmShippingDetailEntity.getShippedPieceQty());
//                mmReleaseReceiveDetailEntity.setReceiveTotalQty(mmShippingDetailEntity.getShippedTotalQty());
//            } else if (mmReleaseReceiveType.equals(MmReleaseReceiveType.ZERO)) {
//                mmReleaseReceiveDetailEntity.setReceivePieceQty(0);
//                mmReleaseReceiveDetailEntity.setReceiveTotalQty(0.0);
//            } else if (mmReleaseReceiveType.equals(MmReleaseReceiveType.REQUISITION)) {
//                mmReleaseReceiveDetailEntity.setReceivePieceQty(mmShippingDetailEntity.getPieceQty());
//                mmReleaseReceiveDetailEntity.setReceiveTotalQty(mmShippingDetailEntity.getTotalQty());
//            } else {
//                mmReleaseReceiveDetailEntity.setReceivePieceQty(mmShippingDetailEntity.getShippedPieceQty());
//                mmReleaseReceiveDetailEntity.setReceiveTotalQty(mmShippingDetailEntity.getShippedTotalQty());
//            }

            mmReleaseReceiveDetailEntity.setCreatedAt(new Date());
            mmReleaseReceiveDetailEntity.setCreatedBy(contextDTO.getOperator().getId());
            mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
            mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            mmReleaseReceiveDetailEntity.setStatus(EntityStatus.ACTIVE);
            mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);

            if (mmReleaseReceiveDetailEntity.getHeatBatchNo() != null && !"".equals(mmReleaseReceiveDetailEntity.getHeatBatchNo())) {
                String[] parts = mmReleaseReceiveDetailEntity.getHeatBatchNo().split("/");
                if (parts.length > 1) {
                    createHeatBatch(orgId, projectId, parts[0], parts[1], contextDTO.getOperator());
                } else {
                    createHeatBatch(orgId, projectId, parts[0], null, contextDTO.getOperator());
                }
            }
            createReceiveNoteQrCode(
                mmReleaseReceiveDetailEntity,
                mmMaterialCodeEntity.getNeedSpec(),
                mmShippingDetailEntity.getTotalQty(),
                mmShippingDetailEntity.getPieceQty()
            );
        }

    }

    /**
     * 更新入库单。
     *
     * @param orgId
     * @param projectId
     * @param materialReceiveNoteId
     * @param mmReleaseReceiveCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmReleaseReceiveEntity update(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveCreateDTO mmReleaseReceiveCreateDTO,
        ContextDTO contextDTO
    ) {
        MmReleaseReceiveEntity mmReleaseReceiveEntity = mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveEntity == null) {
            throw new BusinessError("Release Receive does not exist!入库单不存在！");
        }
        // 如果存在其余同名的入库单则不可以更改
        MmReleaseReceiveEntity entity = mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndNameAndStatus(
            orgId,
            projectId,
            mmReleaseReceiveCreateDTO.getName(),
            EntityStatus.ACTIVE
        );
        if (entity != null && !entity.getId().equals(materialReceiveNoteId)) {
            throw new BusinessError("Release Receive name already exists!入库单名称已经存在！");
        }


        mmReleaseReceiveEntity.setName(mmReleaseReceiveCreateDTO.getName());
        mmReleaseReceiveEntity.setRemarks(mmReleaseReceiveCreateDTO.getRemarks());
        mmReleaseReceiveEntity.setOrgId(orgId);
        mmReleaseReceiveEntity.setProjectId(projectId);
        mmReleaseReceiveEntity.setLastModifiedAt(new Date());
        mmReleaseReceiveEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmReleaseReceiveEntity.setStatus(EntityStatus.ACTIVE);

        return mmReleaseReceiveRepository.save(mmReleaseReceiveEntity);
    }

    /**
     * 更新入库单状态。
     *
     * @param orgId
     * @param projectId
     * @param materialReceiveNoteId
     * @param mmReleaseReceiveUpdateRunningStatusDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmReleaseReceiveEntity updateRunningStatus(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveUpdateRunningStatusDTO mmReleaseReceiveUpdateRunningStatusDTO,
        ContextDTO contextDTO
    ) {
        MmReleaseReceiveEntity mmReleaseReceiveEntity = mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveEntity == null) {
            throw new BusinessError("入库单不存在");
        }
        if (EntityStatus.APPROVED.equals(mmReleaseReceiveEntity.getRunningStatus())) {
            throw new BusinessError("入库单已经完成入库。");
        }

        // 完成入库
        if (null != mmReleaseReceiveUpdateRunningStatusDTO.getReceiveFinished() && mmReleaseReceiveUpdateRunningStatusDTO.getReceiveFinished()) {
            mmReleaseReceiveEntity.setRunningStatus(EntityStatus.APPROVED);
            mmReleaseReceiveEntity.setLastModifiedAt(new Date());
            mmReleaseReceiveEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            mmReleaseReceiveRepository.save(mmReleaseReceiveEntity);
            MmReleaseReceiveReceiveDTO mmReleaseReceiveReceiveDTO = new MmReleaseReceiveReceiveDTO();
            mmReleaseReceiveReceiveDTO.setReceived(true);
            startReceive(
                orgId,
                projectId,
                materialReceiveNoteId,
                mmReleaseReceiveEntity.getNo(),
                mmReleaseReceiveReceiveDTO,
                contextDTO,
                mmReleaseReceiveEntity.getInExternalQuality()
            );
            return mmReleaseReceiveEntity;

        } else {
            mmReleaseReceiveEntity.setRunningStatus(mmReleaseReceiveUpdateRunningStatusDTO.getRunningStatus());
            mmReleaseReceiveEntity.setLocked(true);
            mmReleaseReceiveEntity.setLastModifiedAt(new Date());
            mmReleaseReceiveEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            mmReleaseReceiveEntity.setStatus(EntityStatus.ACTIVE);
            return mmReleaseReceiveRepository.save(mmReleaseReceiveEntity);
        }

    }

    /**
     * 入库单详情。
     *
     * @param orgId
     * @param projectId
     * @param materialReceiveNoteId
     * @return
     */
    @Override
    public MmReleaseReceiveEntity detail(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId
    ) {
        MmReleaseReceiveEntity mmReleaseReceiveEntity = mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveEntity == null) {
            throw new BusinessError("入库单不存在");
        }
        return mmReleaseReceiveEntity;
    }

    /**
     * 删除入库单。
     *
     * @param orgId
     * @param projectId
     * @param materialReceiveNoteId
     * @param contextDTO
     */
    @Override
    public void delete(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        ContextDTO contextDTO
    ) {
        MmReleaseReceiveEntity mmReleaseReceiveEntity = mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveEntity == null) {
            throw new BusinessError("Release Receive does not exist!入库单不存在！");
        }

        List<MmReleaseReceiveDetailEntity> mmReleaseReceiveDetailEntities = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndReleaseReceiveIdAndStatus(
            orgId,
            projectId,
            mmReleaseReceiveEntity.getId(),
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveDetailEntities.size() > 0) {
            throw new BusinessError("There is already content in the incoming order, it cannot be deleted!入库单中已有内容，无法删除！");
        }

        mmReleaseReceiveEntity.setLastModifiedAt(new Date());
        mmReleaseReceiveEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmReleaseReceiveEntity.setStatus(EntityStatus.DELETED);
        mmReleaseReceiveRepository.save(mmReleaseReceiveEntity);

    }

    public MmImportBatchResultDTO importDetail(Long orgId,
                                               Long projectId,
                                               Long materialReceiveNoteId,
                                               OperatorDTO operator,
                                               MmImportBatchTask batchTask,
                                               MmImportBatchTaskImportDTO importDTO
    ) {


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

        sheetImportResult = importDetail(
            orgId,
            projectId,
            materialReceiveNoteId,
            operator,
            workbook.getSheetAt(0),
            batchResult,
            batchTask
        );

        batchResult.addLog(
            sheetImportResult.getProcessedCount()
                + " "
                + workbook.getSheetAt(0).getSheetName()
                + " imported."
        );


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
    private MmImportBatchResultDTO importDetail(Long orgId,
                                                Long projectId,
                                                Long materialReceiveNoteId,
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

        List<MmReleaseReceiveDetailEntity> mmReleaseReceiveDetailEntities = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndReleaseReceiveIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );
        if (!mmReleaseReceiveDetailEntities.isEmpty()) {
            mmReleaseReceiveDetailRepository.deleteAll(mmReleaseReceiveDetailEntities);
        }

        List<MmReleaseReceiveDetailQrCodeEntity> mmReleaseReceiveDetailQrCodeEntities = mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndReleaseReceiveIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );
        if (!mmReleaseReceiveDetailQrCodeEntities.isEmpty()) {
            mmReleaseReceiveDetailQrCodeRepository.deleteAll(mmReleaseReceiveDetailQrCodeEntities);
        }

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

                MmReleaseReceiveEntity mmReleaseReceiveEntityFind = mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndIdAndStatus(
                    orgId,
                    projectId,
                    materialReceiveNoteId,
                    EntityStatus.ACTIVE
                );

                if (mmReleaseReceiveEntityFind == null) {
                    throw new BusinessError("入库单信息不存在");
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

                MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity = findReceiveDetail(
                    orgId,
                    projectId,
                    materialReceiveNoteId,
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
                if (null != mmReleaseReceiveDetailEntity) {

                    if (null != mmMaterialCodeEntity.getNeedSpec() && mmMaterialCodeEntity.getNeedSpec()) {
                        mmReleaseReceiveDetailEntity.setTotalQty(mmReleaseReceiveDetailEntity.getTotalQty() + totalQty);
                        mmReleaseReceiveDetailEntity.setPieceQty(mmReleaseReceiveDetailEntity.getPieceQty() + pieceQty);
                    } else {
                        mmReleaseReceiveDetailEntity.setTotalQty(mmReleaseReceiveDetailEntity.getTotalQty() + totalQty);
                    }
                    mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailEntity.setLastModifiedBy(operator.getId());
                    mmReleaseReceiveDetailEntity.setStatus(EntityStatus.ACTIVE);
                    mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);
                    createReceiveNoteQrCode(mmReleaseReceiveDetailEntity, mmMaterialCodeEntity.getNeedSpec(), totalQty, pieceQty);
                } else {
                    mmReleaseReceiveDetailEntity = new MmReleaseReceiveDetailEntity();
                    mmReleaseReceiveDetailEntity.setOrgId(orgId);
                    mmReleaseReceiveDetailEntity.setProjectId(projectId);
                    mmReleaseReceiveDetailEntity.setReleaseReceiveId(materialReceiveNoteId);
                    mmReleaseReceiveDetailEntity.setIdentCode(mmMaterialCodeEntity.getIdentCode());
                    mmReleaseReceiveDetailEntity.setQrCodeType(mmMaterialCodeEntity.getQrCodeType());
                    mmReleaseReceiveDetailEntity.setMmMaterialCodeNo(mmMaterialCodeNo);
                    mmReleaseReceiveDetailEntity.setMmMaterialCodeDescription(mmMaterialCodeEntity.getDescription());
                    mmReleaseReceiveDetailEntity.setSpecDescription(specDescription);
                    mmReleaseReceiveDetailEntity.setPieceTagNo(pieceTagNo);
                    mmReleaseReceiveDetailEntity.setHeatBatchNo(heatBatchNo);
                    mmReleaseReceiveDetailEntity.setMaterialCertificate(materialCertificate);
                    mmReleaseReceiveDetailEntity.setTotalQty(totalQty);
                    mmReleaseReceiveDetailEntity.setDesignUnit(designUnit);
                    mmReleaseReceiveDetailEntity.setSpecValue(specValue);
                    mmReleaseReceiveDetailEntity.setPieceQty(pieceQty);
                    mmReleaseReceiveDetailEntity.setWareHouseLocationId(wareHouseLocationEntity.getId());
                    mmReleaseReceiveDetailEntity.setWareHouseLocationName(wareHouseLocationEntity.getName());
                    mmReleaseReceiveDetailEntity.setShippingNo(shippingNo);
                    mmReleaseReceiveDetailEntity.setRemarks(remarks);

                    mmReleaseReceiveDetailEntity.setCreatedAt(new Date());
                    mmReleaseReceiveDetailEntity.setCreatedBy(operator.getId());
                    mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailEntity.setLastModifiedBy(operator.getId());
                    mmReleaseReceiveDetailEntity.setStatus(EntityStatus.ACTIVE);
                    mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);
                    createReceiveNoteQrCode(mmReleaseReceiveDetailEntity, mmMaterialCodeEntity.getNeedSpec(), totalQty, pieceQty);
                }

                processedCount++;
                batchResult.addProcessedCount(1);

            } catch (Exception e) {
                e.printStackTrace(System.out);
                errorCount++;
                batchResult.addErrorCount(1);
                setImportDataErrorMessage(row, "" + colIndex + "th import error." + e.getMessage());
            }
            batchTask.setResult(batchResult);
            batchTask.setLastModifiedAt();
            mmImportBatchTaskRepository.save(batchTask);
        }

        return new MmImportBatchResultDTO(
            totalCount,
            processedCount,
            skippedCount,
            errorCount
        );
    }

    private MmReleaseReceiveDetailEntity findReceiveDetail(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
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
        return mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndReleaseReceiveIdAndMmMaterialCodeNoAndSpecDescriptionAndPieceTagNoAndHeatBatchNoAndMaterialCertificateAndWareHouseLocationIdAndShippingNoAndSpecValueAndStatus(
            orgId,
            projectId,
            releaseReceiveId,
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

    /**
     * 查找并创建炉批号信息。
     *
     * @param orgId
     * @param projectId
     * @param heatNo
     * @param batchNo
     * @param operator
     * @return
     */
    private MmHeatBatchNoEntity createHeatBatch(
        Long orgId,
        Long projectId,
        String heatNo,
        String batchNo,
        OperatorDTO operator
    ) {
        MmHeatBatchNoEntity mmHeatBatchNoEntity = heatBatchNoRepository.findByOrgIdAndProjectIdAndHeatNoCodeAndBatchNoCodeAndStatus(
            orgId,
            projectId,
            heatNo,
            batchNo,
            EntityStatus.ACTIVE
        );

        if (mmHeatBatchNoEntity == null) {
            mmHeatBatchNoEntity = new MmHeatBatchNoEntity();
            mmHeatBatchNoEntity.setOrgId(orgId);
            mmHeatBatchNoEntity.setProjectId(projectId);
            mmHeatBatchNoEntity.setHeatNoCode(heatNo);
            mmHeatBatchNoEntity.setBatchNoCode(batchNo);
            mmHeatBatchNoEntity.setDescs("");
            mmHeatBatchNoEntity.setCreatedAt(new Date());
            mmHeatBatchNoEntity.setCreatedBy(operator.getId());
            mmHeatBatchNoEntity.setLastModifiedAt(new Date());
            mmHeatBatchNoEntity.setLastModifiedBy(operator.getId());
            mmHeatBatchNoEntity.setStatus(EntityStatus.ACTIVE);
            heatBatchNoRepository.save(mmHeatBatchNoEntity);
        }
        return mmHeatBatchNoEntity;
    }

    private void createReceiveNoteQrCode(
        MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity,
        Boolean needSpec,
        Double totalQty,
        Integer pieceQty
    ) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<MmReleaseReceiveDetailQrCodeEntity> mmReleaseReceiveDetailQrCodeEntities = mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndReleaseReceiveDetailIdAndStatus(
            mmReleaseReceiveDetailEntity.getOrgId(),
            mmReleaseReceiveDetailEntity.getProjectId(),
            mmReleaseReceiveDetailEntity.getId(),
            EntityStatus.ACTIVE,
            pageDTO.toPageable()
        ).getContent();
        if (!mmReleaseReceiveDetailQrCodeEntities.isEmpty()) {
            mmReleaseReceiveDetailQrCodeRepository.deleteAll(mmReleaseReceiveDetailQrCodeEntities);
        }

        if (mmReleaseReceiveDetailEntity.getQrCodeType().equals(QrCodeType.GOODS)) {
            Integer count = 0;
            if (null != needSpec && needSpec) {
                count = pieceQty;
            } else {
                count = totalQty.intValue();
            }
            for (int i = 0; i < count; i++) {
                MmReleaseReceiveDetailQrCodeEntity mmReleaseReceiveDetailQrCodeEntity = new MmReleaseReceiveDetailQrCodeEntity();
                BeanUtils.copyProperties(
                    mmReleaseReceiveDetailEntity,
                    mmReleaseReceiveDetailQrCodeEntity, "id"
                );
                if (null != needSpec && needSpec) {
                    mmReleaseReceiveDetailQrCodeEntity.setPieceQty(1);
//                    mmReleaseReceiveDetailQrCodeEntity.setPieceInventoryQty(1);
//                    mmReleaseReceiveDetailQrCodeEntity.setTotalInventoryQty(mmReleaseReceiveDetailEntity.getSpecValue());
                    mmReleaseReceiveDetailQrCodeEntity.setTotalQty(mmReleaseReceiveDetailEntity.getSpecValue());
                } else {
//                    mmReleaseReceiveDetailQrCodeEntity.setTotalInventoryQty(1.0);
                    mmReleaseReceiveDetailQrCodeEntity.setTotalQty(1.0);
                }
                mmReleaseReceiveDetailQrCodeEntity.setQrCode(QrcodePrefixType.MATERTIAL.getCode() + StringUtils.generateShortUuid());

                mmReleaseReceiveDetailQrCodeEntity.setReleaseReceiveDetailId(mmReleaseReceiveDetailEntity.getId());
                mmReleaseReceiveDetailQrCodeEntity.setCreatedAt(new Date());
                mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
                mmReleaseReceiveDetailQrCodeEntity.setStatus(EntityStatus.ACTIVE);
                mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
            }

        } else {
            if (null != needSpec && needSpec) {
                MmReleaseReceiveDetailQrCodeEntity mmReleaseReceiveDetailQrCodeEntity = new MmReleaseReceiveDetailQrCodeEntity();
                BeanUtils.copyProperties(
                    mmReleaseReceiveDetailEntity,
                    mmReleaseReceiveDetailQrCodeEntity, "id"
                );
//                mmReleaseReceiveDetailQrCodeEntity.setTotalInventoryQty(totalQty);
                mmReleaseReceiveDetailQrCodeEntity.setTotalQty(totalQty);
//                mmReleaseReceiveDetailQrCodeEntity.setPieceInventoryQty(pieceQty);
                mmReleaseReceiveDetailQrCodeEntity.setPieceQty(pieceQty);
                mmReleaseReceiveDetailQrCodeEntity.setQrCode(QrcodePrefixType.MATERTIAL.getCode() + StringUtils.generateShortUuid());

                mmReleaseReceiveDetailQrCodeEntity.setCreatedAt(new Date());
                mmReleaseReceiveDetailQrCodeEntity.setCreatedBy(mmReleaseReceiveDetailEntity.getCreatedBy());
                mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
                mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(mmReleaseReceiveDetailEntity.getCreatedBy());
                mmReleaseReceiveDetailQrCodeEntity.setStatus(EntityStatus.ACTIVE);
                // TODO 查询在库材料是否已有此材料编码的二维码信息
                MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndMmMaterialCodeNoAndPieceTagNoAndHeatBatchNoAndMaterialCertificateAndSpecValueAndStatus(
                    mmReleaseReceiveDetailEntity.getOrgId(),
                    mmReleaseReceiveDetailEntity.getProjectId(),
                    mmReleaseReceiveDetailEntity.getMmMaterialCodeNo(),
                    mmReleaseReceiveDetailEntity.getPieceTagNo(),
                    mmReleaseReceiveDetailEntity.getHeatBatchNo(),
                    mmReleaseReceiveDetailEntity.getMaterialCertificate(),
                    mmReleaseReceiveDetailEntity.getSpecValue(),
                    EntityStatus.ACTIVE);
                if (mmMaterialInStockDetailQrCodeEntity != null) {
                    mmReleaseReceiveDetailQrCodeEntity.setQrCode(mmMaterialInStockDetailQrCodeEntity.getQrCode());
                } else {
                    mmReleaseReceiveDetailQrCodeEntity.setQrCode(QrcodePrefixType.MATERTIAL.getCode() + StringUtils.generateShortUuid());
                }

                mmReleaseReceiveDetailQrCodeEntity.setReleaseReceiveDetailId(mmReleaseReceiveDetailEntity.getId());
                mmReleaseReceiveDetailQrCodeEntity.setQrCodeType(QrCodeType.TYPE);
                mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
            } else {
                MmReleaseReceiveDetailQrCodeEntity mmReleaseReceiveDetailQrCodeEntity = new MmReleaseReceiveDetailQrCodeEntity();
                BeanUtils.copyProperties(
                    mmReleaseReceiveDetailEntity,
                    mmReleaseReceiveDetailQrCodeEntity,
                    "id"
                );
//                mmReleaseReceiveDetailQrCodeEntity.setTotalInventoryQty(totalQty);
                mmReleaseReceiveDetailQrCodeEntity.setTotalQty(totalQty);
                mmReleaseReceiveDetailQrCodeEntity.setQrCode(QrcodePrefixType.MATERTIAL.getCode() + StringUtils.generateShortUuid());

                mmReleaseReceiveDetailQrCodeEntity.setCreatedAt(new Date());
                mmReleaseReceiveDetailQrCodeEntity.setCreatedBy(mmReleaseReceiveDetailEntity.getCreatedBy());
                mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
                mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(mmReleaseReceiveDetailEntity.getCreatedBy());
                mmReleaseReceiveDetailQrCodeEntity.setStatus(EntityStatus.ACTIVE);
                // TODO 查询在库材料是否已有此材料编码的二维码信息
                MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndMmMaterialCodeNoAndPieceTagNoAndHeatBatchNoAndMaterialCertificateAndSpecValueAndStatus(
                    mmReleaseReceiveDetailEntity.getOrgId(),
                    mmReleaseReceiveDetailEntity.getProjectId(),
                    mmReleaseReceiveDetailEntity.getMmMaterialCodeNo(),
                    mmReleaseReceiveDetailEntity.getPieceTagNo(),
                    mmReleaseReceiveDetailEntity.getHeatBatchNo(),
                    mmReleaseReceiveDetailEntity.getMaterialCertificate(),
                    mmReleaseReceiveDetailEntity.getSpecValue(),
                    EntityStatus.ACTIVE);
                if (mmMaterialInStockDetailQrCodeEntity != null) {
                    mmReleaseReceiveDetailQrCodeEntity.setQrCode(mmMaterialInStockDetailQrCodeEntity.getQrCode());
                } else {
                    mmReleaseReceiveDetailQrCodeEntity.setQrCode(QrcodePrefixType.MATERTIAL.getCode() + StringUtils.generateShortUuid());
                }
                mmReleaseReceiveDetailQrCodeEntity.setReleaseReceiveDetailId(mmReleaseReceiveDetailEntity.getId());
                mmReleaseReceiveDetailQrCodeEntity.setQrCodeType(QrCodeType.TYPE);
                mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
            }
        }


    }

    public Page<MmReleaseReceiveDetailSearchDetailDTO> searchDetails(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveDetailSearchDTO mmReleaseReceiveDetailSearchDTO
    ) {
        MmReleaseReceiveEntity releaseReceiveEntityFind = mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );
        if (releaseReceiveEntityFind == null) {
            throw new BusinessError("入库单不存在！");
        }

        List<MmReleaseReceiveDetailSearchDetailDTO> mmReleaseReceiveDetailSearchDetailDTOS = new ArrayList<>();

        Page<MmReleaseReceiveDetailEntity> mmReleaseReceiveDetailEntities = mmReleaseReceiveDetailRepository.searchDetails(
            orgId,
            projectId,
            materialReceiveNoteId,
            mmReleaseReceiveDetailSearchDTO
        );
        for (MmReleaseReceiveDetailEntity entity : mmReleaseReceiveDetailEntities) {
            MmReleaseReceiveDetailSearchDetailDTO dto = new MmReleaseReceiveDetailSearchDetailDTO();
            BeanUtils.copyProperties(
                entity,
                dto
            );
            PageDTO pageDTO = new PageDTO();
            pageDTO.setFetchAll(true);
            List<MmReleaseReceiveDetailQrCodeEntity> entities = mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndReleaseReceiveDetailIdAndStatus(
                orgId,
                projectId,
                entity.getId(),
                EntityStatus.ACTIVE,
                pageDTO.toPageable()
            ).getContent();
            if (entities.size() > 0) {
                dto.setMmReleaseReceiveDetailQrCodeEntities(entities);
            }
            mmReleaseReceiveDetailSearchDetailDTOS.add(dto);
        }
        return new PageImpl<>(mmReleaseReceiveDetailSearchDetailDTOS, mmReleaseReceiveDetailSearchDTO.toPageable(), mmReleaseReceiveDetailEntities.getTotalElements());
    }

    @Override
    public MmReleaseReceiveInventoryStatusDTO searchInventoryStatus(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId
    ) {
        MmReleaseReceiveInventoryStatusDTO mmReleaseReceiveInventoryStatusDTO = new MmReleaseReceiveInventoryStatusDTO();
        MmReleaseReceiveDetailSearchDTO mmReleaseReceiveDetailSearchDTO = new MmReleaseReceiveDetailSearchDTO();
        mmReleaseReceiveDetailSearchDTO.setInventory(true);
        List<MmReleaseReceiveDetailEntity> mmReleaseReceiveDetailEntities = mmReleaseReceiveDetailRepository.searchDetails(
            orgId,
            projectId,
            materialReceiveNoteId,
            mmReleaseReceiveDetailSearchDTO
        ).getContent();
        if (mmReleaseReceiveDetailEntities.size() > 0) {
            mmReleaseReceiveInventoryStatusDTO.setInventory(false);
        } else {
            mmReleaseReceiveInventoryStatusDTO.setInventory(true);
        }
        return mmReleaseReceiveInventoryStatusDTO;
    }

    /**
     * 删除入库单明细。
     *
     * @param orgId
     * @param projectId
     * @param materialReceiveNoteDetailId
     * @param contextDTO
     */
    @Override
    public void deleteItem(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteDetailId,
        ContextDTO contextDTO
    ) {
        MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteDetailId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveDetailEntity == null) {
            throw new BusinessError("Release Receive item does not exist!入库单明细不存在！");
        }

        mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
        mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmReleaseReceiveDetailEntity.setStatus(EntityStatus.DELETED);
        mmReleaseReceiveDetailEntity.setDeleted(true);
        mmReleaseReceiveDetailEntity.setDeletedAt();
        mmReleaseReceiveDetailEntity.setDeletedBy(contextDTO.getOperator().getId());
        mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);

    }

    /**
     * 查询入库单明细二维码。
     */
    @Override
    public Page<MmReleaseReceiveDetailQrCodeEntity> searchDetailQrCodes(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        Long materialReceiveNoteDetailEntityDetailId,
        MmReleaseReceiveQrCodeSearchDTO mmReleaseReceiveQrCodeSearchDTO
    ) {

        MmReleaseReceiveEntity mmReleaseReceiveEntity = mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveEntity.getRunningStatus().equals(EntityStatus.APPROVED)) {
//            return mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndReleaseReceiveDetailIdAndInventoryQtyNotAndStatus(
//                orgId,
//                projectId,
//                materialReceiveNoteDetailEntityDetailId,
//                0,
//                EntityStatus.ACTIVE,
//                mmReleaseReceiveQrCodeSearchDTO.toPageable()
//            );
            return null;

        } else {
            return mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndReleaseReceiveDetailIdAndStatus(
                orgId,
                projectId,
                materialReceiveNoteDetailEntityDetailId,
                EntityStatus.ACTIVE,
                mmReleaseReceiveQrCodeSearchDTO.toPageable()
            );
        }

    }

    public void createDetailQrCodes(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        Long materialReceiveNoteDetailEntityDetailId,
        MmReleaseReceiveQrCodeCreateDTO mmReleaseReceiveQrCodeCreateDTO,
        ContextDTO contextDTO
    ) {
        if (mmReleaseReceiveQrCodeCreateDTO.getQty() == null) {
            throw new BusinessError("数量不为空");
        }

        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<MmReleaseReceiveDetailQrCodeEntity> mmReleaseReceiveDetailQrCodeEntities = mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndReleaseReceiveDetailIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteDetailEntityDetailId,
            EntityStatus.ACTIVE,
            pageDTO.toPageable()
        ).getContent();

        if (mmReleaseReceiveDetailQrCodeEntities.get(0).getQrCodeType().equals(QrCodeType.GOODS)) {
            for (Integer i = 0; i < mmReleaseReceiveQrCodeCreateDTO.getQty(); i++) {
                String qrCode = QrcodePrefixType.MATERTIAL_RECEIVE_GOODS.getCode() + StringUtils.generateShortUuid();
                MmReleaseReceiveDetailQrCodeEntity mmReleaseReceiveDetailQrCodeEntity = new MmReleaseReceiveDetailQrCodeEntity();
                mmReleaseReceiveDetailQrCodeEntity.setOrgId(orgId);
                mmReleaseReceiveDetailQrCodeEntity.setProjectId(projectId);
                mmReleaseReceiveDetailQrCodeEntity.setReleaseReceiveDetailId(materialReceiveNoteDetailEntityDetailId);
                mmReleaseReceiveDetailQrCodeEntity.setQrCode(qrCode);
//                mmReleaseReceiveDetailQrCodeEntity.setQty(1);
                mmReleaseReceiveDetailQrCodeEntity.setQrCodeType(mmReleaseReceiveDetailQrCodeEntities.get(0).getQrCodeType());
//                mmReleaseReceiveDetailQrCodeEntity.setWareHouseType(MaterialOrganizationType.PROJECT);
                mmReleaseReceiveDetailQrCodeEntity.setCreatedAt(new Date());
                mmReleaseReceiveDetailQrCodeEntity.setCreatedBy(contextDTO.getOperator().getId());
                mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
                mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                mmReleaseReceiveDetailQrCodeEntity.setStatus(EntityStatus.ACTIVE);
//                mmReleaseReceiveDetailQrCodeEntity.setRunningStatus(EntityStatus.ACTIVE);
                mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
            }
        } else {

//            mmReleaseReceiveDetailQrCodeEntities.get(0).setQty(mmReleaseReceiveDetailQrCodeEntities.get(0).getQty() + mmReleaseReceiveQrCodeCreateDTO.getQty());
            mmReleaseReceiveDetailQrCodeEntities.get(0).setLastModifiedAt(new Date());
            mmReleaseReceiveDetailQrCodeEntities.get(0).setLastModifiedBy(contextDTO.getOperator().getId());
            mmReleaseReceiveDetailQrCodeEntities.get(0).setStatus(EntityStatus.ACTIVE);
            mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntities.get(0));
        }
    }

    @Override
    public void inventoryQrCodes(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        Long materialReceiveNoteDetailId,
        MmReleaseReceiveInventoryQrCodeDTO mmReleaseReceiveInventoryQrCodeDTO,
        ContextDTO contextDTO
    ) {
        if (mmReleaseReceiveInventoryQrCodeDTO.getQrCode() == null) {
            throw new BusinessError("二维码 qrCode字段不存在");
        }

        MmReleaseReceiveEntity mmReleaseReceiveEntity = mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveEntity == null) {
            throw new BusinessError("入库单不存在");
        }

        if (null != mmReleaseReceiveInventoryQrCodeDTO.getInExternalQuality() && !mmReleaseReceiveInventoryQrCodeDTO.getInExternalQuality()) {
            mmReleaseReceiveEntity.setInExternalQuality(false);
        } else if (null != mmReleaseReceiveInventoryQrCodeDTO.getInExternalQuality() && mmReleaseReceiveInventoryQrCodeDTO.getInExternalQuality()) {
            mmReleaseReceiveEntity.setInExternalQuality(true);

        }
        mmReleaseReceiveEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmReleaseReceiveEntity.setLastModifiedAt(new Date());
        mmReleaseReceiveRepository.save(mmReleaseReceiveEntity);

        MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteDetailId,
            EntityStatus.ACTIVE
        );

        if (null == mmReleaseReceiveDetailEntity) {
            throw new BusinessError("入库详情ID不正确");
        }

        MmReleaseReceiveDetailQrCodeEntity mmReleaseReceiveDetailQrCodeEntity = mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndReleaseReceiveIdAndQrCodeAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            mmReleaseReceiveInventoryQrCodeDTO.getQrCode(),
            EntityStatus.ACTIVE
        );

        if (null == mmReleaseReceiveDetailQrCodeEntity) {
            throw new BusinessError("二维码信息不正确");
        }
//        if (null != mmReleaseReceiveDetailQrCodeEntity.getFirstExcess() && mmReleaseReceiveDetailQrCodeEntity.getFirstExcess()) {
//            throw new BusinessError("二维已经完成一次超额入库,不能再入库");
//        }

        MmMaterialCodeEntity mmMaterialCodeEntity = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndNoAndStatus(
            orgId,
            projectId,
            mmReleaseReceiveDetailQrCodeEntity.getMmMaterialCodeNo(),
            EntityStatus.ACTIVE
        );

        if (null == mmMaterialCodeEntity) {
            throw new BusinessError("材料编码无效");
        }

        if (null == mmReleaseReceiveInventoryQrCodeDTO.getInExternalQuality()) {
            // 盘点流程
            if (null != mmMaterialCodeEntity.getNeedSpec() && mmMaterialCodeEntity.getNeedSpec()) {
                // 盘点流程 -> 规格量管理
                if (null == mmReleaseReceiveInventoryQrCodeDTO.getPieceQty()) {
                    throw new BusinessError("请输入盘点件数");
                }
                if (mmReleaseReceiveDetailEntity.getQrCodeType().equals(QrCodeType.GOODS)) {
                    // 盘点流程 -> 规格量管理 -> 一物一码
                    if (!mmReleaseReceiveInventoryQrCodeDTO.getPieceQty().equals(1) && !mmReleaseReceiveInventoryQrCodeDTO.getPieceQty().equals(0)) {
                        throw new BusinessError("PieceQty 盘点件数应该是 1或者0");
                    }
                    // 盘点流程 -> 规格量管理 -> 一物一码 -> 更新入库详情
                    mmReleaseReceiveDetailEntity.setReceivePieceQty(mmReleaseReceiveDetailEntity.getReceivePieceQty() - mmReleaseReceiveDetailQrCodeEntity.getPieceInventoryQty() + mmReleaseReceiveInventoryQrCodeDTO.getPieceQty());
                    mmReleaseReceiveDetailEntity.setReceiveTotalQty(mmReleaseReceiveDetailEntity.getReceiveTotalQty() - mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty() + mmReleaseReceiveDetailEntity.getSpecValue() * mmReleaseReceiveInventoryQrCodeDTO.getPieceQty());
                    mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);

                    // 盘点流程 -> 规格量管理 -> 一物一码 -> 更新入库二维码信息
                    mmReleaseReceiveDetailQrCodeEntity.setTotalInventoryQty(mmReleaseReceiveInventoryQrCodeDTO.getPieceQty() * mmReleaseReceiveDetailQrCodeEntity.getSpecValue());
                    mmReleaseReceiveDetailQrCodeEntity.setPieceInventoryQty(mmReleaseReceiveInventoryQrCodeDTO.getPieceQty());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
                } else if (mmReleaseReceiveDetailEntity.getQrCodeType().equals(QrCodeType.TYPE)) {

                    if (mmReleaseReceiveInventoryQrCodeDTO.getPieceQty() < 0) {
                        throw new BusinessError("盘点件数不能小于0");
                    }
                    // 盘点流程 -> 规格量管理 -> 一类一码 -> 更新入库详情
                    mmReleaseReceiveDetailEntity.setReceiveTotalQty(mmReleaseReceiveDetailEntity.getReceiveTotalQty() - mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty() + mmReleaseReceiveInventoryQrCodeDTO.getPieceQty() * mmReleaseReceiveDetailEntity.getSpecValue());
                    mmReleaseReceiveDetailEntity.setReceivePieceQty(mmReleaseReceiveDetailEntity.getReceivePieceQty() - mmReleaseReceiveDetailQrCodeEntity.getPieceInventoryQty() + mmReleaseReceiveInventoryQrCodeDTO.getPieceQty());
                    mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);
                    // 盘点流程 -> 规格量管理 -> 一类一码 -> 更新入库二维码信息
                    mmReleaseReceiveDetailQrCodeEntity.setTotalInventoryQty(mmReleaseReceiveInventoryQrCodeDTO.getPieceQty() * mmReleaseReceiveDetailEntity.getSpecValue());
                    mmReleaseReceiveDetailQrCodeEntity.setPieceInventoryQty(mmReleaseReceiveInventoryQrCodeDTO.getPieceQty());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
                    if (mmReleaseReceiveDetailQrCodeEntity.getPieceInventoryQty() > mmReleaseReceiveDetailQrCodeEntity.getPieceQty()) {
                        mmReleaseReceiveDetailQrCodeEntity.setFirstExcess(true);
                    }
                    mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
                }
            } else {
                // 盘点流程 -> 不规格量管理
                if (null == mmReleaseReceiveInventoryQrCodeDTO.getTotalQty()) {
                    throw new BusinessError("请输入盘点总量");
                }
                if (mmReleaseReceiveDetailEntity.getQrCodeType().equals(QrCodeType.GOODS)) {
                    // 盘点流程 -> 不规格量管理 -> 一物一码
                    if (!mmReleaseReceiveInventoryQrCodeDTO.getTotalQty().equals(1.0) && !mmReleaseReceiveInventoryQrCodeDTO.getTotalQty().equals(0.0)) {
                        throw new BusinessError("TotalQty 盘点总量应该是 1 或者 0");
                    }
                    // 盘点流程 -> 不规格量管理 -> 一物一码 -> 更新入库详情
                    mmReleaseReceiveDetailEntity.setReceiveTotalQty(mmReleaseReceiveDetailEntity.getReceiveTotalQty() - mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty() + mmReleaseReceiveInventoryQrCodeDTO.getTotalQty());
                    mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);
                    // 盘点流程 -> 不规格量管理 -> 一物一码 ->  更新入库二维码信息
                    mmReleaseReceiveDetailQrCodeEntity.setTotalInventoryQty(mmReleaseReceiveInventoryQrCodeDTO.getTotalQty());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
                } else if (mmReleaseReceiveDetailEntity.getQrCodeType().equals(QrCodeType.TYPE)) {
                    // 盘点流程 -> 不规格量管理 -> 一类一码
                    if (mmReleaseReceiveInventoryQrCodeDTO.getTotalQty() < 0) {
                        throw new BusinessError("TotalQty 盘点总量应该大于或等于0");
                    }
                    // 盘点流程 -> 不规格量管理 -> 一类一码 -> 更新入库详情
                    mmReleaseReceiveDetailEntity.setReceiveTotalQty(mmReleaseReceiveDetailEntity.getReceiveTotalQty() - mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty() + mmReleaseReceiveInventoryQrCodeDTO.getTotalQty());
                    mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);
                    // 盘点流程 -> 不规格量管理 -> 一类一码 -> 更新入库二维码信息
                    mmReleaseReceiveDetailQrCodeEntity.setTotalInventoryQty(mmReleaseReceiveInventoryQrCodeDTO.getTotalQty());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());

                    // TODO
                    if (mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty() > mmReleaseReceiveDetailQrCodeEntity.getTotalQty()) {
                        mmReleaseReceiveDetailQrCodeEntity.setFirstExcess(true);
                    }
                    mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
                }
            }
        } else if (!mmReleaseReceiveInventoryQrCodeDTO.getInExternalQuality()) {
            // 内质检流程
            if (null != mmMaterialCodeEntity.getNeedSpec() && mmMaterialCodeEntity.getNeedSpec()) {
                // 内质检流程-> 规格量管理
                if (null == mmReleaseReceiveInventoryQrCodeDTO.getQualifiedPieceQty()) {
                    throw new BusinessError("请输入内检合格件数");
                }
                if (mmReleaseReceiveDetailEntity.getQrCodeType().equals(QrCodeType.GOODS)) {
                    // 内质检流程-> 规格量管理 -> 一物一码
                    if (!mmReleaseReceiveInventoryQrCodeDTO.getQualifiedPieceQty().equals(1) && !mmReleaseReceiveInventoryQrCodeDTO.getQualifiedPieceQty().equals(0)) {
                        throw new BusinessError("QualifiedPieceQty 内检合格件数应该是 1 或者 0");
                    }

                    if (mmReleaseReceiveInventoryQrCodeDTO.getQualifiedPieceQty() > mmReleaseReceiveDetailQrCodeEntity.getPieceInventoryQty()) {
                        throw new BusinessError("QualifiedPieceQty 内检合格件数应该小于盘点件数");
                    }

                    // 内质检流程-> 规格量管理 -> 一物一码 -> 更新入库详情
                    mmReleaseReceiveDetailEntity.setQualifiedPieceQty(mmReleaseReceiveDetailEntity.getQualifiedPieceQty() - mmReleaseReceiveDetailQrCodeEntity.getQualifiedPieceQty() + mmReleaseReceiveInventoryQrCodeDTO.getQualifiedPieceQty());
                    mmReleaseReceiveDetailEntity.setQualifiedTotalQty(mmReleaseReceiveDetailEntity.getQualifiedTotalQty() - mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty() + mmReleaseReceiveDetailEntity.getSpecValue() * mmReleaseReceiveInventoryQrCodeDTO.getQualifiedPieceQty());
                    mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);

                    // 内质检流程-> 规格量管理 -> 一物一码 -> 更新入库二维码信息
                    mmReleaseReceiveDetailQrCodeEntity.setQualifiedTotalQty(mmReleaseReceiveDetailQrCodeEntity.getSpecValue() * mmReleaseReceiveInventoryQrCodeDTO.getQualifiedPieceQty());
                    mmReleaseReceiveDetailQrCodeEntity.setQualifiedPieceQty(mmReleaseReceiveInventoryQrCodeDTO.getQualifiedPieceQty());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
                } else if (mmReleaseReceiveDetailEntity.getQrCodeType().equals(QrCodeType.TYPE)) {
                    // 内质检流程-> 规格量管理 -> 一类一码
                    if (mmReleaseReceiveInventoryQrCodeDTO.getQualifiedPieceQty() < 0 || mmReleaseReceiveInventoryQrCodeDTO.getQualifiedPieceQty() > mmReleaseReceiveDetailEntity.getReceivePieceQty()) {
                        throw new BusinessError("QualifiedPieceQty 内检件数应该大于或等于0 并且 小于盘点件数");
                    }
                    // 内质检流程-> 规格量管理 -> 一类一码 -> 更新入库详情
                    mmReleaseReceiveDetailEntity.setQualifiedTotalQty(mmReleaseReceiveDetailEntity.getQualifiedTotalQty() - mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty() + mmReleaseReceiveInventoryQrCodeDTO.getQualifiedPieceQty() * mmReleaseReceiveDetailEntity.getSpecValue());
                    mmReleaseReceiveDetailEntity.setQualifiedPieceQty(mmReleaseReceiveDetailEntity.getQualifiedPieceQty() - mmReleaseReceiveDetailQrCodeEntity.getQualifiedPieceQty() + mmReleaseReceiveInventoryQrCodeDTO.getQualifiedPieceQty());
                    mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);

                    // 内质检流程-> 规格量管理 -> 一类一码 -> 更新入库二维码信息
                    mmReleaseReceiveDetailQrCodeEntity.setQualifiedTotalQty(mmReleaseReceiveInventoryQrCodeDTO.getQualifiedPieceQty() * mmReleaseReceiveDetailEntity.getSpecValue());
                    mmReleaseReceiveDetailQrCodeEntity.setQualifiedPieceQty(mmReleaseReceiveInventoryQrCodeDTO.getQualifiedPieceQty());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
                }
            } else {
                // 内质检流程-> 不规格量管理
                if (null == mmReleaseReceiveInventoryQrCodeDTO.getQualifiedTotalQty()) {
                    throw new BusinessError("请输入内检合格总量");
                }
                if (mmReleaseReceiveDetailEntity.getQrCodeType().equals(QrCodeType.GOODS)) {
                    // 内质检流程-> 不规格量管理 -> 一物一码
                    if (!mmReleaseReceiveInventoryQrCodeDTO.getQualifiedTotalQty().equals(1.0) && !mmReleaseReceiveInventoryQrCodeDTO.getQualifiedTotalQty().equals(0.0)) {
                        throw new BusinessError("QualifiedTotalQt 内检合格总量应该是 1");
                    }
                    if (mmReleaseReceiveInventoryQrCodeDTO.getQualifiedTotalQty() > mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty()) {
                        throw new BusinessError("QualifiedTotalQt 内检合格总量应该小于盘点总量");
                    }

                    //  内质检流程-> 不规格量管理 -> 一物一码 -> 更新入库详情
                    mmReleaseReceiveDetailEntity.setQualifiedTotalQty(mmReleaseReceiveDetailEntity.getQualifiedTotalQty() - mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty() + mmReleaseReceiveInventoryQrCodeDTO.getQualifiedTotalQty());
                    mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);

                    // 内质检流程-> 不规格量管理 -> 一物一码 -> 更新入库二维码信息
                    mmReleaseReceiveDetailQrCodeEntity.setQualifiedTotalQty(mmReleaseReceiveInventoryQrCodeDTO.getQualifiedTotalQty());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
                } else if (mmReleaseReceiveDetailEntity.getQrCodeType().equals(QrCodeType.TYPE)) {
                    // 内质检流程-> 不规格量管理 -> 一类一码
                    if (mmReleaseReceiveInventoryQrCodeDTO.getQualifiedTotalQty() < 0 || mmReleaseReceiveInventoryQrCodeDTO.getQualifiedTotalQty() > mmReleaseReceiveDetailEntity.getReceiveTotalQty()) {
                        throw new BusinessError("QualifiedTotalQty 内检总数应该大于0或等于0 且小于盘点总量");
                    }
                    // 内质检流程-> 不规格量管理 -> 一类一码 -> 更新入库详情
                    mmReleaseReceiveDetailEntity.setQualifiedTotalQty(mmReleaseReceiveDetailEntity.getQualifiedTotalQty() - mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty() + mmReleaseReceiveInventoryQrCodeDTO.getQualifiedTotalQty());
                    mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);

                    // 内质检流程-> 不规格量管理 -> 一类一码 -> 更新入库二维码信息
                    mmReleaseReceiveDetailQrCodeEntity.setQualifiedTotalQty(mmReleaseReceiveInventoryQrCodeDTO.getQualifiedTotalQty());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
                }
            }
        } else {
            // 外检流程
            if (null != mmMaterialCodeEntity.getNeedSpec() && mmMaterialCodeEntity.getNeedSpec()) {
                // 外检流程-> 规格量管理
                if (null == mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedPieceQty()) {
                    throw new BusinessError("请输入外检合格件数");
                }
                if (mmReleaseReceiveDetailEntity.getQrCodeType().equals(QrCodeType.GOODS)) {
                    // 外检流程-> 规格量管理 -> 一物一码
                    if (!mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedPieceQty().equals(1) && !mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedPieceQty().equals(0)) {
                        throw new BusinessError("ExternalQualifiedPieceQty 外检合格件数应该是 1或者0");
                    }

                    if (mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedPieceQty() > mmReleaseReceiveDetailQrCodeEntity.getQualifiedPieceQty()) {
                        throw new BusinessError("ExternalQualifiedPieceQty 外检合格件数应该小于内检合格件数");
                    }
                    // 外检流程-> 规格量管理 -> 一物一码 -> 更新入库详情
                    mmReleaseReceiveDetailEntity.setExternalQualifiedPieceQty(mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty() - mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedPieceQty() + mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedPieceQty());
                    mmReleaseReceiveDetailEntity.setExternalQualifiedTotalQty(mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty() - mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedTotalQty() + mmReleaseReceiveDetailEntity.getSpecValue() * mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedPieceQty());
                    mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);

                    // 外检流程-> 规格量管理 -> 一物一码 -> 更新入库二维码信息
                    mmReleaseReceiveDetailQrCodeEntity.setExternalQualifiedTotalQty(mmReleaseReceiveDetailQrCodeEntity.getSpecValue() * mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedPieceQty());
                    mmReleaseReceiveDetailQrCodeEntity.setExternalQualifiedPieceQty(mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedPieceQty());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
                } else if (mmReleaseReceiveDetailEntity.getQrCodeType().equals(QrCodeType.TYPE)) {
                    // 外检流程-> 规格量管理 -> 一类一码
                    if (mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedPieceQty() < 0 || mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedPieceQty() > mmReleaseReceiveDetailEntity.getQualifiedPieceQty()) {
                        throw new BusinessError("ExternalQualifiedPieceQty 外检合格件数应该大于0 小于内检件数");
                    }
                    // 外检流程-> 规格量管理 -> 一类一码 -> 更新入库详情
                    mmReleaseReceiveDetailEntity.setExternalQualifiedTotalQty(mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty() - mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedTotalQty() + mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedPieceQty() * mmReleaseReceiveDetailEntity.getSpecValue());
                    mmReleaseReceiveDetailEntity.setExternalQualifiedPieceQty(mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty() - mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedPieceQty() + mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedPieceQty());
                    mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);

                    // 外检流程-> 规格量管理 -> 一类一码 -> 更新入库二维码信息
                    mmReleaseReceiveDetailQrCodeEntity.setExternalQualifiedTotalQty(mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedPieceQty() * mmReleaseReceiveDetailEntity.getSpecValue());
                    mmReleaseReceiveDetailQrCodeEntity.setExternalQualifiedPieceQty(mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedPieceQty());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
                }
            } else {
                // 外检流程-> 不规格量管理
                if (null == mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedTotalQty()) {
                    throw new BusinessError("请输入外检合格总量");
                }
                if (mmReleaseReceiveDetailEntity.getQrCodeType().equals(QrCodeType.GOODS)) {
                    // 外检流程-> 不规格量管理 -> 一物一码
                    if (!mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedTotalQty().equals(1.0) && !mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedTotalQty().equals(0.0)) {
                        throw new BusinessError("ExternalQualifiedTotalQty 外检合格总量应该是 1 或者 0");
                    }
                    if (mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedTotalQty() > mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty()) {
                        throw new BusinessError("ExternalQualifiedTotalQty 外检合格总量应该小于内检合格总量");
                    }
                    //  外检流程-> 不规格量管理 -> 一物一码 -> 更新入库详情
                    mmReleaseReceiveDetailEntity.setExternalQualifiedTotalQty(mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty() - mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedTotalQty() + mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedTotalQty());
                    mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);

                    // 外检流程-> 不规格量管理 -> 一物一码 -> 更新入库二维码信息
                    mmReleaseReceiveDetailQrCodeEntity.setExternalQualifiedTotalQty(mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedTotalQty());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
                } else if (mmReleaseReceiveDetailEntity.getQrCodeType().equals(QrCodeType.TYPE)) {
                    // 外检流程-> 不规格量管理 -> 一类一码
                    if (mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedTotalQty() < 0 || mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedTotalQty() > mmReleaseReceiveDetailEntity.getQualifiedTotalQty()) {
                        throw new BusinessError("ExternalQualifiedTotalQty 外检合格总量应该大于0 小于内检总量");
                    }
                    // 外检流程-> 不规格量管理 -> 一类一码 -> 更新入库详情
                    mmReleaseReceiveDetailEntity.setExternalQualifiedTotalQty(mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty() - mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedTotalQty() + mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedTotalQty());
                    mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);

                    // 外检流程-> 不规格量管理 -> 一类一码 -> 更新入库二维码信息
                    mmReleaseReceiveDetailQrCodeEntity.setExternalQualifiedTotalQty(mmReleaseReceiveInventoryQrCodeDTO.getExternalQualifiedTotalQty());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
                    mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
                }
            }
        }

    }

    public void batchInventoryQrCodes(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveInventoryQrCodeDTO mmReleaseReceiveInventoryQrCodeDTO,
        ContextDTO contextDTO
    ) {
        MmReleaseReceiveEntity mmReleaseReceiveEntity = mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveEntity == null) {
            throw new BusinessError("入库单不存在");
        }

        if (null != mmReleaseReceiveInventoryQrCodeDTO.getInExternalQuality() && !mmReleaseReceiveInventoryQrCodeDTO.getInExternalQuality()) {
            mmReleaseReceiveEntity.setInExternalQuality(false);
        } else if (null != mmReleaseReceiveInventoryQrCodeDTO.getInExternalQuality() && mmReleaseReceiveInventoryQrCodeDTO.getInExternalQuality()) {
            mmReleaseReceiveEntity.setInExternalQuality(true);

        }
        mmReleaseReceiveEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmReleaseReceiveEntity.setLastModifiedAt(new Date());
        mmReleaseReceiveRepository.save(mmReleaseReceiveEntity);

        List<MmReleaseReceiveDetailEntity> mmReleaseReceiveDetailEntities = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndReleaseReceiveIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );

        if (mmReleaseReceiveDetailEntities.isEmpty()) {
            throw new BusinessError("没有材料详情，请先导入材料信息");
        }

        for (MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity : mmReleaseReceiveDetailEntities) {
            if (null == mmReleaseReceiveInventoryQrCodeDTO.getInExternalQuality()) {
                // 盘点流程
                mmReleaseReceiveDetailEntity.setReceivePieceQty(mmReleaseReceiveDetailEntity.getPieceQty());
                mmReleaseReceiveDetailEntity.setReceiveTotalQty(mmReleaseReceiveDetailEntity.getTotalQty());
                mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
            } else if (!mmReleaseReceiveInventoryQrCodeDTO.getInExternalQuality()) {
                // 内质检流程
                mmReleaseReceiveDetailEntity.setQualifiedPieceQty(mmReleaseReceiveDetailEntity.getReceivePieceQty());
                mmReleaseReceiveDetailEntity.setQualifiedTotalQty(mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
            } else {
                // 外检流程
                mmReleaseReceiveDetailEntity.setExternalQualifiedPieceQty(mmReleaseReceiveDetailEntity.getQualifiedPieceQty());
                mmReleaseReceiveDetailEntity.setExternalQualifiedTotalQty(mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
            }
        }
        mmReleaseReceiveDetailRepository.saveAll(mmReleaseReceiveDetailEntities);

        List<MmReleaseReceiveDetailQrCodeEntity> mmReleaseReceiveDetailQrCodeEntities = mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndReleaseReceiveIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveDetailQrCodeEntities.isEmpty()) {
            throw new BusinessError("没有材料二维码信息，请先导入材料信息");
        }
        for (MmReleaseReceiveDetailQrCodeEntity mmReleaseReceiveDetailQrCodeEntity : mmReleaseReceiveDetailQrCodeEntities) {
            if (null == mmReleaseReceiveInventoryQrCodeDTO.getInExternalQuality()) {
                // 盘点流程
                mmReleaseReceiveDetailQrCodeEntity.setTotalInventoryQty(mmReleaseReceiveDetailQrCodeEntity.getTotalQty());
                mmReleaseReceiveDetailQrCodeEntity.setPieceInventoryQty(mmReleaseReceiveDetailQrCodeEntity.getPieceQty());
                mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
            } else if (!mmReleaseReceiveInventoryQrCodeDTO.getInExternalQuality()) {
                // 内质检流程
                mmReleaseReceiveDetailQrCodeEntity.setQualifiedTotalQty(mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty());
                mmReleaseReceiveDetailQrCodeEntity.setQualifiedPieceQty(mmReleaseReceiveDetailQrCodeEntity.getPieceInventoryQty());
                mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
            } else {
                // 外检流程
                mmReleaseReceiveDetailQrCodeEntity.setExternalQualifiedTotalQty(mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty());
                mmReleaseReceiveDetailQrCodeEntity.setExternalQualifiedPieceQty(mmReleaseReceiveDetailQrCodeEntity.getQualifiedPieceQty());
                mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
            }
        }
        mmReleaseReceiveDetailQrCodeRepository.saveAll(mmReleaseReceiveDetailQrCodeEntities);
    }

    @Override
    public void detailReceive(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        Long materialReceiveNoteDetailId,
        MmReleaseReceiveReceiveDTO mmReleaseReceiveReceiveDTO,
        ContextDTO contextDTO,
        Boolean inExternalQuality
    ) {
        // 入库单详情
        MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(
            orgId,
            projectId,
            materialReceiveNoteDetailId
        );

        // 如果质检合格数量等于0，则跳过
        if (mmReleaseReceiveDetailEntity.getQualifiedTotalQty().equals(0.0) && mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty().equals(0.0)) {
            return;
        }
        // 查找在库材料总记录信息
        MmMaterialInStockEntity mmMaterialInStockEntity = mmMaterialInStockRepository.findByOrgIdAndProjectIdAndMmMaterialCodeNoAndStatus(
            orgId,
            projectId,
            mmReleaseReceiveDetailEntity.getMmMaterialCodeNo(),
            EntityStatus.ACTIVE
        );
        if (mmMaterialInStockEntity == null) {
            mmMaterialInStockEntity = new MmMaterialInStockEntity();
            mmMaterialInStockEntity.setIdentCode(mmReleaseReceiveDetailEntity.getIdentCode());
            if (mmReleaseReceiveDetailEntity.getMmMaterialCodeNo() != null) {
                MmMaterialCodeEntity mmMaterialCodeEntity = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndNoAndMaterialOrganizationTypeAndStatus(
                    orgId,
                    projectId,
                    mmReleaseReceiveDetailEntity.getMmMaterialCodeNo(),
                    MaterialOrganizationType.PROJECT,
                    EntityStatus.ACTIVE
                );
                if (mmMaterialCodeEntity != null) {
                    mmMaterialInStockEntity.setSpecName(mmMaterialCodeEntity.getMaterialSpec());
                    mmMaterialInStockEntity.setSpecQuality(mmMaterialCodeEntity.getMaterialQuality());
                    mmMaterialInStockEntity.setMmMaterialCodeId(mmMaterialCodeEntity.getId());
                }
            }
            mmMaterialInStockEntity.setMmMaterialCodeNo(mmReleaseReceiveDetailEntity.getMmMaterialCodeNo());
            mmMaterialInStockEntity.setQrCodeType(mmReleaseReceiveDetailEntity.getQrCodeType());
            mmMaterialInStockEntity.setMmMaterialCodeDescription(mmReleaseReceiveDetailEntity.getMmMaterialCodeDescription());

            if (null == inExternalQuality) {
                mmMaterialInStockEntity.setReceivedTotalQty(mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                mmMaterialInStockEntity.setInStockTotalQty(mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                mmMaterialInStockEntity.setReceivedPieceQty(mmReleaseReceiveDetailEntity.getReceivePieceQty());
                mmMaterialInStockEntity.setInStockPieceQty(mmReleaseReceiveDetailEntity.getReceivePieceQty());
            } else if (inExternalQuality.equals(false)) {
                // 如果只有内检，则按照内检入库
                mmMaterialInStockEntity.setReceivedTotalQty(mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                mmMaterialInStockEntity.setInStockTotalQty(mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                mmMaterialInStockEntity.setReceivedPieceQty(mmReleaseReceiveDetailEntity.getQualifiedPieceQty());
                mmMaterialInStockEntity.setInStockPieceQty(mmReleaseReceiveDetailEntity.getQualifiedPieceQty());
            } else {
                // 如果有外检，则按照外检入库
                mmMaterialInStockEntity.setReceivedTotalQty(mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                mmMaterialInStockEntity.setInStockTotalQty(mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                mmMaterialInStockEntity.setReceivedPieceQty(mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
                mmMaterialInStockEntity.setInStockPieceQty(mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
            }

            mmMaterialInStockEntity.setIssuedTotalQty(0.0);
            mmMaterialInStockEntity.setDesignUnit(mmReleaseReceiveDetailEntity.getDesignUnit());
            mmMaterialInStockEntity.setIssuedPieceQty(0);
            mmMaterialInStockEntity.setCreatedAt(new Date());
            mmMaterialInStockEntity.setCreatedBy(contextDTO.getOperator().getId());
            mmMaterialInStockEntity.setOrgId(orgId);
            mmMaterialInStockEntity.setProjectId(projectId);
            mmMaterialInStockEntity.setCompanyId(mmReleaseReceiveReceiveDTO.getCompanyId());
            mmMaterialInStockEntity.setLastModifiedAt(new Date());
            mmMaterialInStockEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            mmMaterialInStockEntity.setStatus(EntityStatus.ACTIVE);
            mmMaterialInStockRepository.save(mmMaterialInStockEntity);
        } else {

            if (null == inExternalQuality) {
                mmMaterialInStockEntity.setReceivedTotalQty(mmMaterialInStockEntity.getReceivedTotalQty() + mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                mmMaterialInStockEntity.setInStockTotalQty(mmMaterialInStockEntity.getInStockTotalQty() + mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                mmMaterialInStockEntity.setReceivedPieceQty(mmMaterialInStockEntity.getReceivedPieceQty() + mmReleaseReceiveDetailEntity.getReceivePieceQty());
                mmMaterialInStockEntity.setInStockPieceQty(mmMaterialInStockEntity.getInStockPieceQty() + mmReleaseReceiveDetailEntity.getReceivePieceQty());

            } else if (inExternalQuality.equals(false)) {
                // 如果只有内检，则按照内检入库
                mmMaterialInStockEntity.setReceivedTotalQty(mmMaterialInStockEntity.getReceivedTotalQty() + mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                mmMaterialInStockEntity.setInStockTotalQty(mmMaterialInStockEntity.getInStockTotalQty() + mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                mmMaterialInStockEntity.setReceivedPieceQty(mmMaterialInStockEntity.getReceivedPieceQty() + mmReleaseReceiveDetailEntity.getQualifiedPieceQty());
                mmMaterialInStockEntity.setInStockPieceQty(mmMaterialInStockEntity.getInStockPieceQty() + mmReleaseReceiveDetailEntity.getQualifiedPieceQty());

            } else {
                // 如果有外检，则按照外检入库
                mmMaterialInStockEntity.setReceivedTotalQty(mmMaterialInStockEntity.getReceivedTotalQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                mmMaterialInStockEntity.setInStockTotalQty(mmMaterialInStockEntity.getInStockTotalQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                mmMaterialInStockEntity.setReceivedPieceQty(mmMaterialInStockEntity.getReceivedPieceQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
                mmMaterialInStockEntity.setInStockPieceQty(mmMaterialInStockEntity.getInStockPieceQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
            }

            mmMaterialInStockEntity.setLastModifiedAt(new Date());
            mmMaterialInStockEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            mmMaterialInStockRepository.save(mmMaterialInStockEntity);
        }

        // 查找并创建在库材料明细记录
        MmMaterialInStockDetailEntity mmMaterialInStockDetailEntity = mmMaterialInStockDetailRepository.findByOrgIdAndProjectIdAndMmMaterialInStockIdAndMmMaterialCodeNoAndSpecValueAndStatus(
            orgId,
            projectId,
            mmMaterialInStockEntity.getId(),
            mmReleaseReceiveDetailEntity.getMmMaterialCodeNo(),
            mmReleaseReceiveDetailEntity.getSpecValue(),
            EntityStatus.ACTIVE
        );

        if (mmMaterialInStockDetailEntity == null) {
            mmMaterialInStockDetailEntity = new MmMaterialInStockDetailEntity();
            mmMaterialInStockDetailEntity.setCreatedAt(new Date());
            mmMaterialInStockDetailEntity.setCreatedBy(contextDTO.getOperator().getId());
            mmMaterialInStockDetailEntity.setIdentCode(mmReleaseReceiveDetailEntity.getIdentCode());
            mmMaterialInStockDetailEntity.setMmMaterialCodeNo(mmReleaseReceiveDetailEntity.getMmMaterialCodeNo());
            mmMaterialInStockDetailEntity.setMmMaterialCodeDescription(mmReleaseReceiveDetailEntity.getMmMaterialCodeDescription());
            mmMaterialInStockDetailEntity.setSpecName(mmMaterialInStockEntity.getSpecName());
            mmMaterialInStockDetailEntity.setSpecQuality(mmMaterialInStockEntity.getSpecQuality());
            mmMaterialInStockDetailEntity.setDesignUnit(mmReleaseReceiveDetailEntity.getDesignUnit());

            if (null == inExternalQuality) {
                mmMaterialInStockDetailEntity.setInStockTotalQty(mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                mmMaterialInStockDetailEntity.setReceivedTotalQty(mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                mmMaterialInStockDetailEntity.setReceivedPieceQty(mmReleaseReceiveDetailEntity.getReceivePieceQty());
                mmMaterialInStockDetailEntity.setInStockPieceQty(mmReleaseReceiveDetailEntity.getReceivePieceQty());
            } else if (inExternalQuality.equals(false)) {
                mmMaterialInStockDetailEntity.setInStockTotalQty(mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                mmMaterialInStockDetailEntity.setReceivedTotalQty(mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                mmMaterialInStockDetailEntity.setReceivedPieceQty(mmReleaseReceiveDetailEntity.getQualifiedPieceQty());
                mmMaterialInStockDetailEntity.setInStockPieceQty(mmReleaseReceiveDetailEntity.getQualifiedPieceQty());
            } else {
                mmMaterialInStockDetailEntity.setInStockTotalQty(mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                mmMaterialInStockDetailEntity.setReceivedTotalQty(mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                mmMaterialInStockDetailEntity.setReceivedPieceQty(mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
                mmMaterialInStockDetailEntity.setInStockPieceQty(mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
            }
            mmMaterialInStockDetailEntity.setIssuedTotalQty(0.0);
            mmMaterialInStockDetailEntity.setIssuedPieceQty(0);
            mmMaterialInStockDetailEntity.setSpecValue(mmReleaseReceiveDetailEntity.getSpecValue());
            mmMaterialInStockDetailEntity.setQrCodeType(mmReleaseReceiveDetailEntity.getQrCodeType());
        } else {

            if (null == inExternalQuality) {
                mmMaterialInStockDetailEntity.setInStockTotalQty(mmMaterialInStockDetailEntity.getInStockTotalQty() + mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                mmMaterialInStockDetailEntity.setReceivedTotalQty(mmMaterialInStockDetailEntity.getReceivedTotalQty() + mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                mmMaterialInStockDetailEntity.setReceivedPieceQty(mmMaterialInStockDetailEntity.getReceivedPieceQty() + mmReleaseReceiveDetailEntity.getReceivePieceQty());
                mmMaterialInStockDetailEntity.setInStockPieceQty(mmMaterialInStockDetailEntity.getInStockPieceQty() + mmReleaseReceiveDetailEntity.getReceivePieceQty());
            } else if (inExternalQuality.equals(false)) {
                mmMaterialInStockDetailEntity.setInStockTotalQty(mmMaterialInStockDetailEntity.getInStockTotalQty() + mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                mmMaterialInStockDetailEntity.setReceivedTotalQty(mmMaterialInStockDetailEntity.getReceivedTotalQty() + mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                mmMaterialInStockDetailEntity.setReceivedPieceQty(mmMaterialInStockDetailEntity.getReceivedPieceQty() + mmReleaseReceiveDetailEntity.getQualifiedPieceQty());
                mmMaterialInStockDetailEntity.setInStockPieceQty(mmMaterialInStockDetailEntity.getInStockPieceQty() + mmReleaseReceiveDetailEntity.getQualifiedPieceQty());
            } else {
                mmMaterialInStockDetailEntity.setInStockTotalQty(mmMaterialInStockDetailEntity.getInStockTotalQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                mmMaterialInStockDetailEntity.setReceivedTotalQty(mmMaterialInStockDetailEntity.getReceivedTotalQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                mmMaterialInStockDetailEntity.setReceivedPieceQty(mmMaterialInStockDetailEntity.getReceivedPieceQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
                mmMaterialInStockDetailEntity.setInStockPieceQty(mmMaterialInStockDetailEntity.getInStockPieceQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
            }

        }
        mmMaterialInStockDetailEntity.setOrgId(orgId);
        mmMaterialInStockDetailEntity.setProjectId(projectId);
        mmMaterialInStockDetailEntity.setLastModifiedAt(new Date());
        mmMaterialInStockDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmMaterialInStockDetailEntity.setStatus(EntityStatus.ACTIVE);
        mmMaterialInStockDetailEntity.setMmMaterialInStockId(mmMaterialInStockEntity.getId());
        mmMaterialInStockDetailRepository.save(mmMaterialInStockDetailEntity);

        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<MmReleaseReceiveDetailQrCodeEntity> mmReleaseReceiveDetailQrCodeEntities = mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndReleaseReceiveDetailIdAndStatus(
            orgId, projectId,
            materialReceiveNoteDetailId,
            EntityStatus.ACTIVE,
            pageDTO.toPageable()
        ).getContent();

        if (mmReleaseReceiveDetailQrCodeEntities.isEmpty()) {
            return;
        }
        for (MmReleaseReceiveDetailQrCodeEntity mmReleaseReceiveDetailQrCodeEntity : mmReleaseReceiveDetailQrCodeEntities) {

            if (mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty().equals(0.0)) {
                continue;
            }

            if (null == inExternalQuality) {
                if (mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty().equals(0.0)) {
                    continue;
                }
            } else if (inExternalQuality.equals(false)) {
                if (mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty().equals(0.0)) {
                    continue;
                }
            } else {
                if (mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedTotalQty().equals(0.0)) {
                    continue;
                }
            }

            // 创建在库材料二维码信息
            MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndQrCodeAndStatus(
                orgId,
                projectId,
                mmReleaseReceiveDetailQrCodeEntity.getQrCode(),
                EntityStatus.ACTIVE
            );

            if (mmMaterialInStockDetailQrCodeEntity == null) {
                mmMaterialInStockDetailQrCodeEntity = new MmMaterialInStockDetailQrCodeEntity();
                BeanUtils.copyProperties(
                    mmReleaseReceiveDetailQrCodeEntity,
                    mmMaterialInStockDetailQrCodeEntity,
                    "id"
                );
                mmMaterialInStockDetailQrCodeEntity.setWareHouseLocationName(
                    mmReleaseReceiveDetailQrCodeEntity.getWareHouseLocationName()
                );

                mmMaterialInStockDetailQrCodeEntity.setOrgId(orgId);
                mmMaterialInStockDetailQrCodeEntity.setProjectId(projectId);
                mmMaterialInStockDetailQrCodeEntity.setLastModifiedAt(new Date());
                mmMaterialInStockDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                mmMaterialInStockDetailQrCodeEntity.setStatus(EntityStatus.ACTIVE);
                mmMaterialInStockDetailQrCodeEntity.setMmMaterialInStockDetailId(mmMaterialInStockDetailEntity.getId());
                mmMaterialInStockDetailQrCodeEntity.setCreatedAt(new Date());
                mmMaterialInStockDetailQrCodeEntity.setCreatedBy(contextDTO.getOperator().getId());

                if (null == inExternalQuality) {
                    mmMaterialInStockDetailQrCodeEntity.setInStockTotalQty(mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedTotalQty(mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedPieceQty(mmReleaseReceiveDetailQrCodeEntity.getPieceInventoryQty());
                    mmMaterialInStockDetailQrCodeEntity.setInStockPieceQty(mmReleaseReceiveDetailQrCodeEntity.getPieceInventoryQty());
                } else if (inExternalQuality.equals(false)) {
                    mmMaterialInStockDetailQrCodeEntity.setInStockTotalQty(mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedTotalQty(mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedPieceQty(mmReleaseReceiveDetailQrCodeEntity.getQualifiedPieceQty());
                    mmMaterialInStockDetailQrCodeEntity.setInStockPieceQty(mmReleaseReceiveDetailQrCodeEntity.getQualifiedPieceQty());
                } else {
                    mmMaterialInStockDetailQrCodeEntity.setInStockTotalQty(mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedTotalQty(mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedPieceQty(mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedPieceQty());
                    mmMaterialInStockDetailQrCodeEntity.setInStockPieceQty(mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedPieceQty());
                }
                mmMaterialInStockDetailQrCodeEntity.setIssuedTotalQty(0.0);
                mmMaterialInStockDetailQrCodeEntity.setIssuedPieceQty(0);
                mmMaterialInStockDetailQrCodeRepository.save(mmMaterialInStockDetailQrCodeEntity);
            } else {

                if (null == inExternalQuality) {
                    mmMaterialInStockDetailQrCodeEntity.setInStockTotalQty(mmMaterialInStockDetailQrCodeEntity.getInStockTotalQty() + mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedTotalQty(mmMaterialInStockDetailQrCodeEntity.getReceivedTotalQty() + mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedPieceQty(mmMaterialInStockDetailQrCodeEntity.getReceivedPieceQty() + mmReleaseReceiveDetailQrCodeEntity.getPieceInventoryQty());
                    mmMaterialInStockDetailQrCodeEntity.setInStockPieceQty(mmMaterialInStockDetailQrCodeEntity.getInStockPieceQty() + mmReleaseReceiveDetailQrCodeEntity.getPieceInventoryQty());
                } else if (inExternalQuality.equals(false)) {
                    mmMaterialInStockDetailQrCodeEntity.setInStockTotalQty(mmMaterialInStockDetailQrCodeEntity.getInStockTotalQty() + mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedTotalQty(mmMaterialInStockDetailQrCodeEntity.getReceivedTotalQty() + mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedPieceQty(mmMaterialInStockDetailQrCodeEntity.getReceivedPieceQty() + mmReleaseReceiveDetailQrCodeEntity.getQualifiedPieceQty());
                    mmMaterialInStockDetailQrCodeEntity.setInStockPieceQty(mmMaterialInStockDetailQrCodeEntity.getInStockPieceQty() + mmReleaseReceiveDetailQrCodeEntity.getQualifiedPieceQty());
                } else {
                    mmMaterialInStockDetailQrCodeEntity.setInStockTotalQty(mmMaterialInStockDetailQrCodeEntity.getInStockTotalQty() + mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedTotalQty(mmMaterialInStockDetailQrCodeEntity.getReceivedTotalQty() + mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedPieceQty(mmMaterialInStockDetailQrCodeEntity.getReceivedPieceQty() + mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedPieceQty());
                    mmMaterialInStockDetailQrCodeEntity.setInStockPieceQty(mmMaterialInStockDetailQrCodeEntity.getInStockPieceQty() + mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedPieceQty());
                }

                mmMaterialInStockDetailQrCodeEntity.setLastModifiedAt(new Date());
                mmMaterialInStockDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                mmMaterialInStockDetailQrCodeRepository.save(mmMaterialInStockDetailQrCodeEntity);
            }
        }
    }

    /**
     * TODO
     *
     * @param orgId
     * @param projectId
     * @param materialReceiveNoteId
     * @param mmReleaseReceiveInventoryQrCodeDTO
     * @param contextDTO
     */
    @Override
    public void cancelInventoryQrCodes(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveInventoryQrCodeDTO mmReleaseReceiveInventoryQrCodeDTO,
        ContextDTO contextDTO
    ) {
        if (mmReleaseReceiveInventoryQrCodeDTO.getQrCode() == null) {
            throw new BusinessError("二维码 qrCode字段不存在");
        }

//        if (mmReleaseReceiveInventoryQrCodeDTO.getQty() == null) {
//            throw new BusinessError("二维码 qty字段不存在");
//        }
//        MmReleaseReceiveDetailQrCodeEntity mmReleaseReceiveDetailQrCodeEntity = mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndReleaseReceiveIdAndQrCodeAndStatus(
//            orgId,
//            projectId,
//            materialReceiveNoteId,
//            mmReleaseReceiveInventoryQrCodeDTO.getQrCode(),
//            EntityStatus.ACTIVE
//        );
//        if (mmReleaseReceiveDetailQrCodeEntity == null) {
//            throw new BusinessError("二维码不存在当前项目");
//        }

        // 判断材料类型和数量关系是否正确
//        if (mmReleaseReceiveDetailQrCodeEntity.getType().equals(QrCodeType.GOODS)) {
//            if (mmReleaseReceiveInventoryQrCodeDTO.getQty() != 1) {
//                throw new BusinessError("当前材料为一物一码，取消盘点数量只能为1，请更改取消盘点数量");
//            }
//            if (!mmReleaseReceiveDetailQrCodeEntity.getStatus().equals(EntityStatus.APPROVED)) {
//                throw new BusinessError("当前材料为一物一码，取消盘点的二维码还未盘点");
//            }
//        }
//        if (mmReleaseReceiveDetailQrCodeEntity.getType().equals(QrCodeType.TYPE)) {
//            if (mmReleaseReceiveInventoryQrCodeDTO.getQty() <= 0) {
//                throw new BusinessError("当前材料为一类一码，取消盘点数量不能小于或等于0");
//            }
//            if (mmReleaseReceiveInventoryQrCodeDTO.getQty() > mmReleaseReceiveDetailQrCodeEntity.getQty()) {
//                throw new BusinessError("当前材料为一物一码，取消盘点的二维码的数量不能大于自身数量");
//            }
//        }

//        if (mmReleaseReceiveDetailQrCodeEntity.getType().equals(QrCodeType.GOODS)) {
//            mmReleaseReceiveDetailQrCodeEntity.setStatus(EntityStatus.ACTIVE);
//            mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(
//                contextDTO.getOperator().getId()
//            );
//            mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(
//                new Date()
//            );
//            mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
//
//            // 更新入库单盘点数量
//            MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(
//                orgId, projectId, mmReleaseReceiveDetailQrCodeEntity.getMaterialReceiveNoteDetailId()
//            );
//            mmReleaseReceiveDetailEntity.setInventoryQty(
//                mmReleaseReceiveDetailEntity.getInventoryQty() - mmReleaseReceiveInventoryQrCodeDTO.getQty()
//            );
//            mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
//            mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
//            mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);
//
//        }
//        if (mmReleaseReceiveDetailQrCodeEntity.getType().equals(QrCodeType.TYPE)) {
//
//            if (mmReleaseReceiveInventoryQrCodeDTO.getQty() >= mmReleaseReceiveDetailQrCodeEntity.getQty()) {
//                mmReleaseReceiveDetailQrCodeEntity.setStatus(EntityStatus.APPROVED);
//                mmReleaseReceiveDetailQrCodeEntity.setQty(0);
//            } else {
//                mmReleaseReceiveDetailQrCodeEntity.setQty(mmReleaseReceiveDetailQrCodeEntity.getQty() + mmReleaseReceiveInventoryQrCodeDTO.getQty());
//            }
//
//            mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(
//                contextDTO.getOperator().getId()
//            );
//            mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(
//                new Date()
//            );
//            mmReleaseReceiveDetailQrCodeEntity.setStatus(EntityStatus.APPROVED);
//            mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);
//
//            MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(
//                orgId, projectId, mmReleaseReceiveDetailQrCodeEntity.getMaterialReceiveNoteDetailId()
//            );
//            mmReleaseReceiveDetailEntity.setInventoryQty(
//                mmReleaseReceiveDetailEntity.getInventoryQty() - mmReleaseReceiveInventoryQrCodeDTO.getQty()
//            );
//            mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
//            mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
//            mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);
//        }
    }

    @Override
    public MmReleaseReceiveQrCodeResultDTO qrCodeSearch(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        String qrCode
    ) {

        MmReleaseReceiveDetailQrCodeEntity mmReleaseReceiveDetailQrCodeEntity = mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndReleaseReceiveIdAndQrCodeAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            qrCode,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveDetailQrCodeEntity == null) {
            throw new BusinessError("二维码不存在当前项目");
        }
        MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(
            orgId, projectId, mmReleaseReceiveDetailQrCodeEntity.getReleaseReceiveDetailId()
        );
//        MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndReleaseReceiveIdAndQrCodeAndStatus(
//            orgId,
//            projectId,
//            materialReceiveNoteId,
//            qrCode,
//            EntityStatus.ACTIVE
//        );
        if (mmReleaseReceiveDetailEntity == null) {
            throw new BusinessError("当前二维码无关联的入库单");
        }
        MmReleaseReceiveQrCodeResultDTO mmReleaseReceiveQrCodeResultDTO = new MmReleaseReceiveQrCodeResultDTO();

//        BeanUtils.copyProperties(
//            mmReleaseReceiveDetailEntity,
//            mmReleaseReceiveQrCodeResultDTO
//        );
        mmReleaseReceiveQrCodeResultDTO.setQrCode(qrCode);
        mmReleaseReceiveQrCodeResultDTO.setMmReleaseReceiveDetailId(mmReleaseReceiveDetailEntity.getId());
//        mmReleaseReceiveQrCodeResultDTO.setDesignQty(mmReleaseReceiveDetailEntity.getDesignQty());
//        mmReleaseReceiveQrCodeResultDTO.setShippedQty(mmReleaseReceiveDetailEntity.getShippedQty());
//        mmReleaseReceiveQrCodeResultDTO.setInventoryQty(mmReleaseReceiveDetailEntity.getInventoryQty());
//        mmReleaseReceiveQrCodeResultDTO.setRunningStatus(mmReleaseReceiveDetailEntity.getRunningStatus());

        return mmReleaseReceiveQrCodeResultDTO;
    }


    public void startReceive(
        Long orgId,
        Long projectId,
        Long mmReleaseReceiveId,
        String mmReleaseReceiveNo,
        MmReleaseReceiveReceiveDTO mmReleaseReceiveReceiveDTO,
        ContextDTO contextDTO,
        Boolean inExternalQuality
    ) {
        materialbatchTaskService.run(
            orgId,
            projectId,
            mmReleaseReceiveId,
            mmReleaseReceiveNo,
            contextDTO,
            MaterialImportType.RECEIVE_RELEASE_RECEIVE,
            DisciplineCode.MATERIAL,
            batchTask -> {
                MmImportBatchResultDTO batchResult = new MmImportBatchResultDTO(batchTask);
                MmImportBatchResultDTO result = receive(
                    orgId,
                    projectId,
                    mmReleaseReceiveId,
                    mmReleaseReceiveReceiveDTO,
                    contextDTO,
                    inExternalQuality,
                    batchResult,
                    batchTask
                );

                return result;
            }
        );
    }


    public MmImportBatchResultDTO receive(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveReceiveDTO mmReleaseReceiveReceiveDTO,
        ContextDTO contextDTO,
        Boolean inExternalQuality,
        MmImportBatchResultDTO batchResult,
        MmImportBatchTask batchTask
    ) {

        int totalCount = 0;
        int skippedCount = 0;
        int processedCount = 0;
        int errorCount = 0;
        List<String> errorLogs = new ArrayList<>();

        // 查找入库材料详情
        List<MmReleaseReceiveDetailEntity> materialReceiveNoteDetailEntities = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndReleaseReceiveIdAndStatus(
            orgId, projectId, materialReceiveNoteId, EntityStatus.ACTIVE
        );

        if (materialReceiveNoteDetailEntities.size() == 0) {
            errorLogs.add("没有可入库材料");
            batchTask.setErrorLog(
                errorLogs
            );
            batchTask.setResult(batchResult);
            batchTask.setLastModifiedAt();
            errorCount =errorCount+1;
            batchResult.addErrorCount(1);
            mmImportBatchTaskRepository.save(batchTask);

            errorCount =errorCount+1;
            return new MmImportBatchResultDTO(
                totalCount,
                processedCount,
                skippedCount,
                errorCount
            );
        }

        // 更新在库材料主表和详细表
        for (MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity : materialReceiveNoteDetailEntities) {

            // 如果质检合格数量等于0，则跳过
            if (mmReleaseReceiveDetailEntity.getQualifiedTotalQty().equals(0.0) && mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty().equals(0.0)) {
                continue;
            }

            // 查找在库材料总记录信息
            MmMaterialInStockEntity mmMaterialInStockEntity = mmMaterialInStockRepository.findByOrgIdAndProjectIdAndMmMaterialCodeNoAndStatus(
                orgId,
                projectId,
                mmReleaseReceiveDetailEntity.getMmMaterialCodeNo(),
                EntityStatus.ACTIVE
            );
            if (mmMaterialInStockEntity == null) {
                mmMaterialInStockEntity = new MmMaterialInStockEntity();
                mmMaterialInStockEntity.setIdentCode(mmReleaseReceiveDetailEntity.getIdentCode());
                if (mmReleaseReceiveDetailEntity.getMmMaterialCodeNo() != null) {
                    MmMaterialCodeEntity mmMaterialCodeEntity = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndNoAndMaterialOrganizationTypeAndStatus(
                        orgId,
                        projectId,
                        mmReleaseReceiveDetailEntity.getMmMaterialCodeNo(),
                        MaterialOrganizationType.PROJECT,
                        EntityStatus.ACTIVE
                    );
                    if (mmMaterialCodeEntity != null) {
                        mmMaterialInStockEntity.setSpecName(mmMaterialCodeEntity.getMaterialSpec());
                        mmMaterialInStockEntity.setSpecQuality(mmMaterialCodeEntity.getMaterialQuality());
                        mmMaterialInStockEntity.setMmMaterialCodeId(mmMaterialCodeEntity.getId());
                    }
                }
                mmMaterialInStockEntity.setMmMaterialCodeNo(mmReleaseReceiveDetailEntity.getMmMaterialCodeNo());
                mmMaterialInStockEntity.setQrCodeType(mmReleaseReceiveDetailEntity.getQrCodeType());
                mmMaterialInStockEntity.setMmMaterialCodeDescription(mmReleaseReceiveDetailEntity.getMmMaterialCodeDescription());

                if (null == inExternalQuality) {
                    mmMaterialInStockEntity.setReceivedTotalQty(mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                    mmMaterialInStockEntity.setInStockTotalQty(mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                    mmMaterialInStockEntity.setReceivedPieceQty(mmReleaseReceiveDetailEntity.getReceivePieceQty());
                    mmMaterialInStockEntity.setInStockPieceQty(mmReleaseReceiveDetailEntity.getReceivePieceQty());
                } else if (inExternalQuality.equals(false)) {
                    // 如果只有内检，则按照内检入库
                    mmMaterialInStockEntity.setReceivedTotalQty(mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                    mmMaterialInStockEntity.setInStockTotalQty(mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                    mmMaterialInStockEntity.setReceivedPieceQty(mmReleaseReceiveDetailEntity.getQualifiedPieceQty());
                    mmMaterialInStockEntity.setInStockPieceQty(mmReleaseReceiveDetailEntity.getQualifiedPieceQty());
                } else {
                    // 如果有外检，则按照外检入库
                    mmMaterialInStockEntity.setReceivedTotalQty(mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockEntity.setInStockTotalQty(mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockEntity.setReceivedPieceQty(mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
                    mmMaterialInStockEntity.setInStockPieceQty(mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
                }

                mmMaterialInStockEntity.setIssuedTotalQty(0.0);
                mmMaterialInStockEntity.setDesignUnit(mmReleaseReceiveDetailEntity.getDesignUnit());
                mmMaterialInStockEntity.setIssuedPieceQty(0);
                mmMaterialInStockEntity.setCreatedAt(new Date());
                mmMaterialInStockEntity.setCreatedBy(contextDTO.getOperator().getId());
                mmMaterialInStockEntity.setOrgId(orgId);
                mmMaterialInStockEntity.setProjectId(projectId);
                mmMaterialInStockEntity.setCompanyId(mmReleaseReceiveReceiveDTO.getCompanyId());
                mmMaterialInStockEntity.setLastModifiedAt(new Date());
                mmMaterialInStockEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                mmMaterialInStockEntity.setStatus(EntityStatus.ACTIVE);
                mmMaterialInStockRepository.save(mmMaterialInStockEntity);
            } else {

                if (null == inExternalQuality) {
                    mmMaterialInStockEntity.setReceivedTotalQty(mmMaterialInStockEntity.getReceivedTotalQty() + mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                    mmMaterialInStockEntity.setInStockTotalQty(mmMaterialInStockEntity.getInStockTotalQty() + mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                    mmMaterialInStockEntity.setReceivedPieceQty(mmMaterialInStockEntity.getReceivedPieceQty() + mmReleaseReceiveDetailEntity.getReceivePieceQty());
                    mmMaterialInStockEntity.setInStockPieceQty(mmMaterialInStockEntity.getInStockPieceQty() + mmReleaseReceiveDetailEntity.getReceivePieceQty());

                } else if (inExternalQuality.equals(false)) {
                    // 如果只有内检，则按照内检入库
                    mmMaterialInStockEntity.setReceivedTotalQty(mmMaterialInStockEntity.getReceivedTotalQty() + mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                    mmMaterialInStockEntity.setInStockTotalQty(mmMaterialInStockEntity.getInStockTotalQty() + mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                    mmMaterialInStockEntity.setReceivedPieceQty(mmMaterialInStockEntity.getReceivedPieceQty() + mmReleaseReceiveDetailEntity.getQualifiedPieceQty());
                    mmMaterialInStockEntity.setInStockPieceQty(mmMaterialInStockEntity.getInStockPieceQty() + mmReleaseReceiveDetailEntity.getQualifiedPieceQty());

                } else {
                    // 如果有外检，则按照外检入库
                    mmMaterialInStockEntity.setReceivedTotalQty(mmMaterialInStockEntity.getReceivedTotalQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockEntity.setInStockTotalQty(mmMaterialInStockEntity.getInStockTotalQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockEntity.setReceivedPieceQty(mmMaterialInStockEntity.getReceivedPieceQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
                    mmMaterialInStockEntity.setInStockPieceQty(mmMaterialInStockEntity.getInStockPieceQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
                }

                mmMaterialInStockEntity.setLastModifiedAt(new Date());
                mmMaterialInStockEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                mmMaterialInStockRepository.save(mmMaterialInStockEntity);
            }

            // 查找并创建在库材料明细记录
            MmMaterialInStockDetailEntity mmMaterialInStockDetailEntity = mmMaterialInStockDetailRepository.findByOrgIdAndProjectIdAndMmMaterialInStockIdAndMmMaterialCodeNoAndSpecValueAndStatus(
                orgId,
                projectId,
                mmMaterialInStockEntity.getId(),
                mmReleaseReceiveDetailEntity.getMmMaterialCodeNo(),
                mmReleaseReceiveDetailEntity.getSpecValue(),
                EntityStatus.ACTIVE
            );

            if (mmMaterialInStockDetailEntity == null) {
                mmMaterialInStockDetailEntity = new MmMaterialInStockDetailEntity();
                mmMaterialInStockDetailEntity.setCreatedAt(new Date());
                mmMaterialInStockDetailEntity.setCreatedBy(contextDTO.getOperator().getId());
                mmMaterialInStockDetailEntity.setIdentCode(mmReleaseReceiveDetailEntity.getIdentCode());
                mmMaterialInStockDetailEntity.setMmMaterialCodeNo(mmReleaseReceiveDetailEntity.getMmMaterialCodeNo());
                mmMaterialInStockDetailEntity.setMmMaterialCodeDescription(mmReleaseReceiveDetailEntity.getMmMaterialCodeDescription());
                mmMaterialInStockDetailEntity.setSpecName(mmMaterialInStockEntity.getSpecName());
                mmMaterialInStockDetailEntity.setSpecQuality(mmMaterialInStockEntity.getSpecQuality());
                mmMaterialInStockDetailEntity.setDesignUnit(mmReleaseReceiveDetailEntity.getDesignUnit());

                if (null == inExternalQuality) {
                    mmMaterialInStockDetailEntity.setInStockTotalQty(mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                    mmMaterialInStockDetailEntity.setReceivedTotalQty(mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                    mmMaterialInStockDetailEntity.setReceivedPieceQty(mmReleaseReceiveDetailEntity.getReceivePieceQty());
                    mmMaterialInStockDetailEntity.setInStockPieceQty(mmReleaseReceiveDetailEntity.getReceivePieceQty());
                } else if (inExternalQuality.equals(false)) {
                    mmMaterialInStockDetailEntity.setInStockTotalQty(mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                    mmMaterialInStockDetailEntity.setReceivedTotalQty(mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                    mmMaterialInStockDetailEntity.setReceivedPieceQty(mmReleaseReceiveDetailEntity.getQualifiedPieceQty());
                    mmMaterialInStockDetailEntity.setInStockPieceQty(mmReleaseReceiveDetailEntity.getQualifiedPieceQty());
                } else {
                    mmMaterialInStockDetailEntity.setInStockTotalQty(mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockDetailEntity.setReceivedTotalQty(mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockDetailEntity.setReceivedPieceQty(mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
                    mmMaterialInStockDetailEntity.setInStockPieceQty(mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
                }
                mmMaterialInStockDetailEntity.setIssuedTotalQty(0.0);
                mmMaterialInStockDetailEntity.setIssuedPieceQty(0);
                mmMaterialInStockDetailEntity.setSpecValue(mmReleaseReceiveDetailEntity.getSpecValue());
                mmMaterialInStockDetailEntity.setQrCodeType(mmReleaseReceiveDetailEntity.getQrCodeType());
            } else {

                if (null == inExternalQuality) {
                    mmMaterialInStockDetailEntity.setInStockTotalQty(mmMaterialInStockDetailEntity.getInStockTotalQty() + mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                    mmMaterialInStockDetailEntity.setReceivedTotalQty(mmMaterialInStockDetailEntity.getReceivedTotalQty() + mmReleaseReceiveDetailEntity.getReceiveTotalQty());
                    mmMaterialInStockDetailEntity.setReceivedPieceQty(mmMaterialInStockDetailEntity.getReceivedPieceQty() + mmReleaseReceiveDetailEntity.getReceivePieceQty());
                    mmMaterialInStockDetailEntity.setInStockPieceQty(mmMaterialInStockDetailEntity.getInStockPieceQty() + mmReleaseReceiveDetailEntity.getReceivePieceQty());
                } else if (inExternalQuality.equals(false)) {
                    mmMaterialInStockDetailEntity.setInStockTotalQty(mmMaterialInStockDetailEntity.getInStockTotalQty() + mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                    mmMaterialInStockDetailEntity.setReceivedTotalQty(mmMaterialInStockDetailEntity.getReceivedTotalQty() + mmReleaseReceiveDetailEntity.getQualifiedTotalQty());
                    mmMaterialInStockDetailEntity.setReceivedPieceQty(mmMaterialInStockDetailEntity.getReceivedPieceQty() + mmReleaseReceiveDetailEntity.getQualifiedPieceQty());
                    mmMaterialInStockDetailEntity.setInStockPieceQty(mmMaterialInStockDetailEntity.getInStockPieceQty() + mmReleaseReceiveDetailEntity.getQualifiedPieceQty());
                } else {
                    mmMaterialInStockDetailEntity.setInStockTotalQty(mmMaterialInStockDetailEntity.getInStockTotalQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockDetailEntity.setReceivedTotalQty(mmMaterialInStockDetailEntity.getReceivedTotalQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockDetailEntity.setReceivedPieceQty(mmMaterialInStockDetailEntity.getReceivedPieceQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
                    mmMaterialInStockDetailEntity.setInStockPieceQty(mmMaterialInStockDetailEntity.getInStockPieceQty() + mmReleaseReceiveDetailEntity.getExternalQualifiedPieceQty());
                }

            }
            mmMaterialInStockDetailEntity.setOrgId(orgId);
            mmMaterialInStockDetailEntity.setProjectId(projectId);
            mmMaterialInStockDetailEntity.setLastModifiedAt(new Date());
            mmMaterialInStockDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            mmMaterialInStockDetailEntity.setStatus(EntityStatus.ACTIVE);
            mmMaterialInStockDetailEntity.setMmMaterialInStockId(mmMaterialInStockEntity.getId());
            mmMaterialInStockDetailRepository.save(mmMaterialInStockDetailEntity);

        }

        List<MmReleaseReceiveDetailQrCodeEntity> mmReleaseReceiveDetailQrCodeEntities = mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndReleaseReceiveIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );

        if (mmReleaseReceiveDetailQrCodeEntities.isEmpty()) {
            errorLogs.add("没有可入库材料");
            batchTask.setErrorLog(
                errorLogs
            );
            batchTask.setResult(batchResult);
            batchTask.setLastModifiedAt();
            errorCount =errorCount+1;
            batchResult.addErrorCount(1);
            mmImportBatchTaskRepository.save(batchTask);

            return new MmImportBatchResultDTO(
                totalCount,
                processedCount,
                skippedCount,
                errorCount
            );
        }

        totalCount = materialReceiveNoteDetailEntities.size();
        batchTask.setTotalCount(totalCount);

        for (MmReleaseReceiveDetailQrCodeEntity mmReleaseReceiveDetailQrCodeEntity : mmReleaseReceiveDetailQrCodeEntities) {

            if (mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty().equals(0.0)) {
                skippedCount = skippedCount + 1;
                batchResult.addSkippedCount(1);
                continue;
            }

            if (null == inExternalQuality) {
                if (mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty().equals(0.0)) {
                    skippedCount = skippedCount + 1;
                    batchResult.addSkippedCount(1);
                    continue;
                }
            } else if (inExternalQuality.equals(false)) {
                if (mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty().equals(0.0)) {
                    skippedCount = skippedCount + 1;
                    batchResult.addSkippedCount(1);
                    continue;
                }
            } else {
                if (mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedTotalQty().equals(0.0)) {
                    skippedCount = skippedCount + 1;
                    batchResult.addSkippedCount(1);
                    continue;
                }
            }

            // 创建在库材料二维码信息
            MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndQrCodeAndStatus(
                orgId,
                projectId,
                mmReleaseReceiveDetailQrCodeEntity.getQrCode(),
                EntityStatus.ACTIVE
            );

            if (mmMaterialInStockDetailQrCodeEntity == null) {
                MmMaterialInStockDetailEntity mmMaterialInStockDetailEntity = mmMaterialInStockDetailRepository.findByOrgIdAndProjectIdAndMmMaterialCodeNoAndSpecValueAndStatus(
                    orgId,
                    projectId,
                    mmReleaseReceiveDetailQrCodeEntity.getMmMaterialCodeNo(),
                    mmReleaseReceiveDetailQrCodeEntity.getSpecValue(),
                    EntityStatus.ACTIVE
                );
                if (null == mmMaterialInStockDetailEntity) {
                    errorCount = errorCount + 1;
                    batchResult.addErrorCount(1);
                    continue;
                }

                mmMaterialInStockDetailQrCodeEntity = new MmMaterialInStockDetailQrCodeEntity();
                BeanUtils.copyProperties(
                    mmReleaseReceiveDetailQrCodeEntity,
                    mmMaterialInStockDetailQrCodeEntity,
                    "id"
                );
                mmMaterialInStockDetailQrCodeEntity.setWareHouseLocationName(
                    mmReleaseReceiveDetailQrCodeEntity.getWareHouseLocationName()
                );

                mmMaterialInStockDetailQrCodeEntity.setOrgId(orgId);
                mmMaterialInStockDetailQrCodeEntity.setProjectId(projectId);
                mmMaterialInStockDetailQrCodeEntity.setLastModifiedAt(new Date());
                mmMaterialInStockDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                mmMaterialInStockDetailQrCodeEntity.setStatus(EntityStatus.ACTIVE);
                mmMaterialInStockDetailQrCodeEntity.setMmMaterialInStockDetailId(mmMaterialInStockDetailEntity.getId());
                mmMaterialInStockDetailQrCodeEntity.setCreatedAt(new Date());
                mmMaterialInStockDetailQrCodeEntity.setCreatedBy(contextDTO.getOperator().getId());

                if (null == inExternalQuality) {
                    mmMaterialInStockDetailQrCodeEntity.setInStockTotalQty(mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedTotalQty(mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedPieceQty(mmReleaseReceiveDetailQrCodeEntity.getPieceInventoryQty());
                    mmMaterialInStockDetailQrCodeEntity.setInStockPieceQty(mmReleaseReceiveDetailQrCodeEntity.getPieceInventoryQty());
                } else if (inExternalQuality.equals(false)) {
                    mmMaterialInStockDetailQrCodeEntity.setInStockTotalQty(mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedTotalQty(mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedPieceQty(mmReleaseReceiveDetailQrCodeEntity.getQualifiedPieceQty());
                    mmMaterialInStockDetailQrCodeEntity.setInStockPieceQty(mmReleaseReceiveDetailQrCodeEntity.getQualifiedPieceQty());
                } else {
                    mmMaterialInStockDetailQrCodeEntity.setInStockTotalQty(mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedTotalQty(mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedPieceQty(mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedPieceQty());
                    mmMaterialInStockDetailQrCodeEntity.setInStockPieceQty(mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedPieceQty());
                }
                mmMaterialInStockDetailQrCodeEntity.setIssuedTotalQty(0.0);
                mmMaterialInStockDetailQrCodeEntity.setIssuedPieceQty(0);
                mmMaterialInStockDetailQrCodeRepository.save(mmMaterialInStockDetailQrCodeEntity);
                processedCount = processedCount + 1;
                batchResult.addProcessedCount(1);
            } else {

                if (null == inExternalQuality) {
                    mmMaterialInStockDetailQrCodeEntity.setInStockTotalQty(mmMaterialInStockDetailQrCodeEntity.getInStockTotalQty() + mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedTotalQty(mmMaterialInStockDetailQrCodeEntity.getReceivedTotalQty() + mmReleaseReceiveDetailQrCodeEntity.getTotalInventoryQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedPieceQty(mmMaterialInStockDetailQrCodeEntity.getReceivedPieceQty() + mmReleaseReceiveDetailQrCodeEntity.getPieceInventoryQty());
                    mmMaterialInStockDetailQrCodeEntity.setInStockPieceQty(mmMaterialInStockDetailQrCodeEntity.getInStockPieceQty() + mmReleaseReceiveDetailQrCodeEntity.getPieceInventoryQty());
                } else if (inExternalQuality.equals(false)) {
                    mmMaterialInStockDetailQrCodeEntity.setInStockTotalQty(mmMaterialInStockDetailQrCodeEntity.getInStockTotalQty() + mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedTotalQty(mmMaterialInStockDetailQrCodeEntity.getReceivedTotalQty() + mmReleaseReceiveDetailQrCodeEntity.getQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedPieceQty(mmMaterialInStockDetailQrCodeEntity.getReceivedPieceQty() + mmReleaseReceiveDetailQrCodeEntity.getQualifiedPieceQty());
                    mmMaterialInStockDetailQrCodeEntity.setInStockPieceQty(mmMaterialInStockDetailQrCodeEntity.getInStockPieceQty() + mmReleaseReceiveDetailQrCodeEntity.getQualifiedPieceQty());
                } else {
                    mmMaterialInStockDetailQrCodeEntity.setInStockTotalQty(mmMaterialInStockDetailQrCodeEntity.getInStockTotalQty() + mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedTotalQty(mmMaterialInStockDetailQrCodeEntity.getReceivedTotalQty() + mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedTotalQty());
                    mmMaterialInStockDetailQrCodeEntity.setReceivedPieceQty(mmMaterialInStockDetailQrCodeEntity.getReceivedPieceQty() + mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedPieceQty());
                    mmMaterialInStockDetailQrCodeEntity.setInStockPieceQty(mmMaterialInStockDetailQrCodeEntity.getInStockPieceQty() + mmReleaseReceiveDetailQrCodeEntity.getExternalQualifiedPieceQty());
                }

                mmMaterialInStockDetailQrCodeEntity.setLastModifiedAt(new Date());
                mmMaterialInStockDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                mmMaterialInStockDetailQrCodeRepository.save(mmMaterialInStockDetailQrCodeEntity);
                processedCount = processedCount + 1;
                batchResult.addProcessedCount(1);
            }
        }
        batchTask.setResult(batchResult);
        batchTask.setLastModifiedAt();
        mmImportBatchTaskRepository.save(batchTask);

        return new MmImportBatchResultDTO(
            totalCount,
            processedCount,
            skippedCount,
            errorCount
        );
    }

    @Override
    public void updateDetail(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        Long materialReceiveNoteDetailId,
        MmReleaseReceiveInventoryQrCodeDTO mmReleaseReceiveInventoryQrCodeDTO,
        ContextDTO contextDTO
    ) {
        MmReleaseReceiveEntity mmReleaseReceiveEntity = mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveEntity == null) {
            throw new BusinessError("入库单不存在");
        }

        if (!mmReleaseReceiveEntity.getRunningStatus().equals(EntityStatus.INIT)) {
            throw new BusinessError("修改放行量必须在入库单未开始时进行");
        }

        MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndReleaseReceiveIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            materialReceiveNoteDetailId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveDetailEntity == null) {
            throw new BusinessError("入库详情不存在");
        }

//        MmShippingDetailRelationEntity mmShippingDetailRelationEntity = mmShippingDetailRelationRepository.findByOrgIdAndProjectIdAndIdAndStatus(
//            orgId,
//            projectId,
//            mmReleaseReceiveDetailEntity.getMmShippingDetailRelationId(),
//            EntityStatus.ACTIVE
//        );

//        mmShippingDetailEntity.setNotReleasedQty(mmPoDetailEntity.getNotReleasedQty() + mmReleaseReceiveDetailEntity.getReleasedQty() - mmReleaseReceiveInventoryQrCodeDTO.getQty());
//        mmShippingDetailEntity.setReleasedQty(
//            mmPoDetailEntity.getReleasedQty() - mmReleaseReceiveDetailEntity.getReleasedQty() + mmReleaseReceiveInventoryQrCodeDTO.getQty()
//        );

//        mmReleaseReceiveDetailEntity.setReleasedQty(mmReleaseReceiveInventoryQrCodeDTO.getQty());
        mmReleaseReceiveDetailEntity.setLastModifiedAt(new Date());
        mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);


        List<Long> materialReceiveNoteDetailIds = new ArrayList<>();
        materialReceiveNoteDetailIds.add(materialReceiveNoteDetailId);
        List<MmReleaseReceiveDetailQrCodeEntity> materialReceiveNoteQrCodeEntities = mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndReleaseReceiveDetailIdInAndQrCodeType(
            orgId,
            projectId,
            materialReceiveNoteDetailIds,
            mmReleaseReceiveDetailEntity.getQrCodeType()
        );

        if (materialReceiveNoteQrCodeEntities.size() > 0) {
            mmReleaseReceiveDetailQrCodeRepository.deleteAll(materialReceiveNoteQrCodeEntities);
        }

        // 如果是一类一码则查找一类一码的二维码信息
//        List<MmReleaseReceiveDetailEntity> mmReleaseReceiveDetailEntities = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndMmMaterialCodeNoAndSpecNameAndStatus(
//            orgId,
//            projectId,
//            mmReleaseReceiveDetailEntity.getMmMaterialCodeNo(),
//            mmReleaseReceiveDetailEntity.getSpecName(),
//            EntityStatus.ACTIVE
//        );
        String qrCode = "";
//        if (mmReleaseReceiveDetailEntities.size() > 0) {
//            List<Long> releaseReceiveDetailIds = new ArrayList<>();
//            for (MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntityFind : mmReleaseReceiveDetailEntities) {
//                releaseReceiveDetailIds.add(mmReleaseReceiveDetailEntityFind.getId());
//            }
//
//            List<MmReleaseReceiveDetailQrCodeEntity> mmReleaseReceiveDetailQrCodeEntities = mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndReleaseReceiveDetailIdInAndQrCodeType(
//                orgId,
//                projectId,
//                releaseReceiveDetailIds,
//                mmReleaseReceiveDetailEntity.getQrCodeType()
//            );
//            if (mmReleaseReceiveDetailQrCodeEntities.size() > 0) {
//                qrCode = mmReleaseReceiveDetailQrCodeEntities.get(0).getQrCode();
//            }
//        }

//        createReceiveNoteQrCode(
//            orgId,
//            projectId,
//            materialReceiveNoteId,
//            mmReleaseReceiveDetailEntity.getId(),
//            mmReleaseReceiveDetailEntity.getQrCodeType(),
//            mmReleaseReceiveDetailEntity.getReleasedQty(),
//            qrCode,
//            contextDTO.getOperator()
//        );

    }

    @Override
    public List<MmReleaseNotePrintDTO> getQrCodeByRelnItemId(Long orgId, Long projectId, Long materialReceiveNoteId,
                                                             Long materialReceiveNoteDetailId) {
        MmReleaseReceiveEntity mmReleaseReceiveEntity = mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveEntity == null) {
            throw new BusinessError("入库单不存在");
        }
        if (mmReleaseReceiveEntity.getRunningStatus().equals(EntityStatus.APPROVED)) {
            return mmReleaseReceiveDetailQrCodeRepository.findByMaterialReceiveNoteIdAndMaterialReceiveNoteDetailId(
                orgId,
                projectId,
                materialReceiveNoteId,
                materialReceiveNoteDetailId,
                true
            );
        } else {
            return mmReleaseReceiveDetailQrCodeRepository.findByMaterialReceiveNoteIdAndMaterialReceiveNoteDetailId(
                orgId,
                projectId,
                materialReceiveNoteId,
                materialReceiveNoteDetailId,
                false
            );
        }

    }

    public void batchUpdateDetail(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveBatchUpdateDTO mmReleaseReceiveBatchUpdateDTO,
        ContextDTO contextDTO
    ) {
        MmReleaseReceiveEntity mmReleaseReceiveEntity = mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveEntity == null) {
            throw new BusinessError("入库单不存在");
        }

        if (!mmReleaseReceiveEntity.getRunningStatus().equals(EntityStatus.ACTIVE)) {
            throw new BusinessError("盘点和检验必须在入库单未开始时进行");
        }
        MmReleaseReceiveInventoryQrCodeDTO mmReleaseReceiveInventoryQrCodeDTO = new MmReleaseReceiveInventoryQrCodeDTO();

        if (null == mmReleaseReceiveBatchUpdateDTO.getInExternalQuality()) {
            // 盘点
            mmReleaseReceiveInventoryQrCodeDTO.setInExternalQuality(null);
        } else if (mmReleaseReceiveBatchUpdateDTO.getInExternalQuality()) {
            // 外检
            mmReleaseReceiveInventoryQrCodeDTO.setInExternalQuality(true);
        } else {
            // 内检
            mmReleaseReceiveInventoryQrCodeDTO.setInExternalQuality(false);
        }
        batchInventoryQrCodes(
            orgId, projectId, materialReceiveNoteId, mmReleaseReceiveInventoryQrCodeDTO, contextDTO
        );
    }

    /**
     * 保存实体下载临时文件。
     *
     * @param orgId                     组织 ID
     * @param projectId                 项目 ID
     * @param mmReleaseReceiveSearchDTO 查询条件
     * @param operatorId                项目ID
     * @return 实体下载临时文件
     */
    @Override
    public File download(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO,
        Long operatorId
    ) {

        // ① 获取模板文件 复制到 临时路径下
        String temporaryFileName = FileUtils.copy(
            this.getClass()
                .getClassLoader()
                .getResourceAsStream("templates/export-material-release.xlsx"),
            temporaryDir,
            operatorId.toString()
        );

        // ② 把临时路径下的模板文件读到workbook中，并填充数据
        File excel;
        Workbook workbook;

        try {
            excel = new File(temporaryDir, temporaryFileName);
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }

        Sheet sheet = workbook.getSheet("RELEASE_DETAIL");
        CellStyle cellStyle = sheet.getRow(3).getCell(6).getCellStyle();
        // 上部3行是固定列名，第4行开始是数据
        int rowNum = DATA_START_ROW;

        // 管线实体数据
        List<MmReleaseReceiveDetailEntity> mmReleaseReceiveDetailEntities = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndReleaseReceiveIdAndStatusOrderByIdentCode(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );

        for (MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity : mmReleaseReceiveDetailEntities) {
//            if (mmReleaseReceiveDetailEntity.getQualifiedQty() < mmReleaseReceiveDetailEntity.getInventoryQty()) {
//                Row row = WorkbookUtils.getRow(sheet, rowNum++);
//                // 材料编码
//                Cell cell = WorkbookUtils.getCell(row, 0);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue(mmReleaseReceiveDetailEntity.getMmMaterialCodeNo());
//                // 材料描述
//                Cell cell2 = WorkbookUtils.getCell(row, 1);
//                cell2.setCellStyle(cellStyle);
//                cell2.setCellValue(mmReleaseReceiveDetailEntity.getMmMaterialCodeDescription());
//
//                // 炉批号
//                Cell cell3 = WorkbookUtils.getCell(row, 2);
//                cell3.setCellStyle(cellStyle);
//                cell3.setCellValue(mmReleaseReceiveDetailEntity.getHeatBatchNo());
//
//                // 库位货位
//                MmWareHouseLocationEntity mmWareHouseLocationEntity = mmWareHouseLocationRepository.findByOrgIdAndProjectIdAndIdAndStatus(
//                    orgId,
//                    projectId,
//                    mmReleaseReceiveDetailEntity.getWareHouseLocationId(),
//                    EntityStatus.ACTIVE
//                );
//                Cell cell4 = WorkbookUtils.getCell(row, 3);
//                cell4.setCellStyle(cellStyle);
//
//                if (null != mmWareHouseLocationEntity) {
//                    cell4.setCellValue(mmWareHouseLocationEntity.getName());
//                } else {
//                    cell4.setCellValue("");
//                }
//
//
//                // 材料证书编码
//                Cell cell5 = WorkbookUtils.getCell(row, 4);
//                cell5.setCellStyle(cellStyle);
//                cell5.setCellValue(mmReleaseReceiveDetailEntity.getMaterialCertificate());
//
//                // 计量单位
//                Cell cell6 = WorkbookUtils.getCell(row, 5);
//                cell6.setCellStyle(cellStyle);
//                cell6.setCellValue(mmReleaseReceiveDetailEntity.getDesignUnit());
//
//                // 放行量
//                Cell cell7 = WorkbookUtils.getCell(row, 6);
//                cell7.setCellStyle(cellStyle);
////                if (null != mmReleaseReceiveDetailEntity.getQualifiedQty() && null != mmReleaseReceiveDetailEntity.getInventoryQty()) {
////                    cell7.setCellValue(mmReleaseReceiveDetailEntity.getInventoryQty() - mmReleaseReceiveDetailEntity.getQualifiedQty());
////                } else {
////                    cell7.setCellValue(0);
////                }
//            }
        }

        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
            return excel;
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }
    }

    /**
     * 获取入库单待办列表。
     *
     * @param orgId
     * @param projectId
     * @param mmReleaseReceiveSearchDTO
     * @return
     */
    @Override
    public Page<MmReleaseReceiveEntity> searchByAssignee(
        Long orgId,
        Long projectId,
        MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO,
        OperatorDTO operatorDTO
    ) {
        return mmReleaseReceiveRepository.searchByAssignee(
            orgId,
            projectId,
            mmReleaseReceiveSearchDTO,
            operatorDTO
        );
    }

    /**
     * 删除入库单明细。
     *
     * @param orgId
     * @param projectId
     * @param materialReceiveNoteDetailId
     * @param qrCodeId
     * @param contextDTO
     */
    @Override
    public void deleteQrCode(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteDetailId,
        Long qrCodeId,
        ContextDTO contextDTO
    ) {
        MmReleaseReceiveDetailQrCodeEntity mmReleaseReceiveDetailQrCodeEntity = mmReleaseReceiveDetailQrCodeRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            qrCodeId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveDetailQrCodeEntity == null) {
            throw new BusinessError("Qrcode does not exist!入库单明细二维码不存在！");
        }

        MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity = mmReleaseReceiveDetailRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteDetailId,
            EntityStatus.ACTIVE
        );
        if (mmReleaseReceiveDetailEntity != null) {
//            mmReleaseReceiveDetailEntity.setShippedQty(mmReleaseReceiveDetailEntity.getShippedQty() - mmReleaseReceiveDetailQrCodeEntity.getShippedQty());
//            mmReleaseReceiveDetailEntity.setInventoryQty(mmReleaseReceiveDetailEntity.getInventoryQty() - mmReleaseReceiveDetailQrCodeEntity.getInventoryQty());
//            mmReleaseReceiveDetailEntity.setQualifiedQty(mmReleaseReceiveDetailEntity.getQualifiedQty() - mmReleaseReceiveDetailQrCodeEntity.getQualifiedQty());
            mmReleaseReceiveDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            mmReleaseReceiveDetailEntity.setLastModifiedAt();
            mmReleaseReceiveDetailRepository.save(mmReleaseReceiveDetailEntity);
        }

        mmReleaseReceiveDetailQrCodeEntity.setLastModifiedAt(new Date());
        mmReleaseReceiveDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmReleaseReceiveDetailQrCodeEntity.setStatus(EntityStatus.DELETED);
        mmReleaseReceiveDetailQrCodeEntity.setDeleted(true);
        mmReleaseReceiveDetailQrCodeEntity.setDeletedAt();
        mmReleaseReceiveDetailQrCodeEntity.setDeletedBy(contextDTO.getOperator().getId());
        mmReleaseReceiveDetailQrCodeRepository.save(mmReleaseReceiveDetailQrCodeEntity);

    }

}
