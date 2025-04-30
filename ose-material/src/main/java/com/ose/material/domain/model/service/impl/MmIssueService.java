package com.ose.material.domain.model.service.impl;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.entity.BaseEntity;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.material.domain.model.repository.*;
import com.ose.material.domain.model.service.MmIssueInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.*;
import com.ose.material.entity.MmIssueEntity;
import com.ose.material.vo.MaterialPrefixType;
import com.ose.material.vo.QrCodeType;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class MmIssueService implements MmIssueInterface {

    @Value("${application.files.temporary}")
    private String temporaryDir;

    /**
     * 出库单  操作仓库。
     */
    private final MmIssueRepository mmIssueRepository;

    private final MmIssueDetailRepository mmIssueDetailRepository;
    private final MmMaterialInStockDetailQrCodeRepository mmMaterialInStockDetailQrCodeRepository;
    private final MmMaterialInStockDetailRepository mmMaterialInStockDetailRepository;
    private final MmMaterialInStockRepository mmMaterialInStockRepository;
    private final MmIssueDetailQrCodeRepository mmIssueDetailQrCodeRepository;
    private final MmMaterialCodeRepository mmMaterialCodeRepository;

    /**
     * 构造方法。
     *
     * @param mmIssueRepository 操作仓库
     */
    @Autowired
    public MmIssueService(
        MmIssueRepository mmIssueRepository,
        MmIssueDetailRepository mmIssueDetailRepository,
        MmMaterialInStockDetailQrCodeRepository mmMaterialInStockDetailQrCodeRepository,
        MmMaterialInStockDetailRepository mmMaterialInStockDetailRepository,
        MmMaterialInStockRepository mmMaterialInStockRepository,
        MmIssueDetailQrCodeRepository mmIssueDetailQrCodeRepository,
        MmMaterialCodeRepository mmMaterialCodeRepository
    ) {
        this.mmIssueRepository = mmIssueRepository;
        this.mmIssueDetailRepository = mmIssueDetailRepository;
        this.mmMaterialInStockDetailQrCodeRepository = mmMaterialInStockDetailQrCodeRepository;
        this.mmMaterialInStockDetailRepository = mmMaterialInStockDetailRepository;
        this.mmMaterialInStockRepository = mmMaterialInStockRepository;
        this.mmIssueDetailQrCodeRepository = mmIssueDetailQrCodeRepository;
        this.mmMaterialCodeRepository = mmMaterialCodeRepository;
    }

    /**
     * 查询出库单列表。
     *
     * @param orgId
     * @param projectId
     * @param mmIssueSearchDTO
     * @return
     */
    @Override
    public Page<MmIssueEntity> search(
        Long orgId,
        Long projectId,
        MmIssueSearchDTO mmIssueSearchDTO
    ) {
        return mmIssueRepository.search(
            orgId,
            projectId,
            mmIssueSearchDTO
        );
    }

    /**
     * 创建出库单。
     *
     * @param orgId
     * @param projectId
     * @param mmIssueCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmIssueEntity create(
        Long orgId,
        Long projectId,
        MmIssueCreateDTO mmIssueCreateDTO,
        ContextDTO contextDTO
    ) {
        MmIssueEntity mmIssueEntity = new MmIssueEntity();

        BeanUtils.copyProperties(mmIssueCreateDTO, mmIssueEntity);

        SeqNumberDTO seqNumberDTO = mmIssueRepository.getMaxSeqNumber(orgId, projectId).orElse(new SeqNumberDTO(0));
        int seqNumber = seqNumberDTO.getNewSeqNumber();

        mmIssueEntity.setSeqNumber(seqNumber);

        mmIssueEntity.setNo(String.format("%s-%04d", MaterialPrefixType.MATERIAL_ISSUE.getCode(), seqNumber));

        mmIssueEntity.setOrgId(orgId);
        mmIssueEntity.setProjectId(projectId);
        mmIssueEntity.setCreatedAt(new Date());
        mmIssueEntity.setCreatedBy(contextDTO.getOperator().getId());
        mmIssueEntity.setLastModifiedAt(new Date());
        mmIssueEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmIssueEntity.setStatus(EntityStatus.ACTIVE);
        mmIssueEntity.setRunningStatus(EntityStatus.ACTIVE);

        return mmIssueRepository.save(mmIssueEntity);
    }

    /**
     * 更新出库单。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @param mmIssueCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmIssueEntity update(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        MmIssueCreateDTO mmIssueCreateDTO,
        ContextDTO contextDTO
    ) {
        MmIssueEntity mmIssueEntity = mmIssueRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialIssueEntityId,
            EntityStatus.ACTIVE
        );
        if (mmIssueEntity == null) {
            throw new BusinessError("设备物资单不存在");
        }
        mmIssueEntity.setName(mmIssueCreateDTO.getName());
        mmIssueEntity.setRemarks(mmIssueCreateDTO.getRemarks());
        mmIssueEntity.setOrgId(orgId);
        mmIssueEntity.setProjectId(projectId);
        mmIssueEntity.setLastModifiedAt(new Date());
        mmIssueEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmIssueEntity.setStatus(EntityStatus.ACTIVE);
        return mmIssueRepository.save(mmIssueEntity);
    }

    /**
     * 出库单详情。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @return
     */
    @Override
    public MmIssueEntity detail(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId
    ) {
        MmIssueEntity mmIssueEntity = mmIssueRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialIssueEntityId,
            EntityStatus.ACTIVE
        );
        if (mmIssueEntity == null) {
            throw new BusinessError("出库单不存在");
        }
        return mmIssueEntity;
    }

    /**
     * 删除出库单。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @param contextDTO
     */
    @Override
    public void delete(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        ContextDTO contextDTO
    ) {
        MmIssueEntity mmIssueEntity = mmIssueRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialIssueEntityId,
            EntityStatus.ACTIVE
        );
        if (mmIssueEntity == null) {
            throw new BusinessError("出库单不存在");
        }
        List<MmIssueDetailEntity> mmIssueDetailEntities = mmIssueDetailRepository.findByOrgIdAndProjectIdAndMmIssueIdAndStatus(
            orgId,
            projectId,
            materialIssueEntityId,
            EntityStatus.ACTIVE
        );

        mmIssueEntity.setLastModifiedAt(new Date());
        mmIssueEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmIssueEntity.setStatus(EntityStatus.DELETED);
        mmIssueRepository.save(mmIssueEntity);

    }

    public MmImportBatchResultDTO importDetail(Long orgId,
                                               Long projectId,
                                               Long materialIssueEntityId,
                                               OperatorDTO operator,
                                               MmImportBatchTask batchTask,
                                               MmImportBatchTaskImportDTO importDTO
    ) {
        MmIssueEntity mmIssueEntity = mmIssueRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialIssueEntityId,
            EntityStatus.ACTIVE
        );
        if (mmIssueEntity == null) {
            throw new ValidationError("出库单不存在");
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

        sheetImportResult = importDetail(
            orgId,
            projectId,
            materialIssueEntityId,
            operator,
            workbook.getSheetAt(0),
            batchResult
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
                                                Long materialIssueEntityId,
                                                OperatorDTO operator,
                                                Sheet sheet,
                                                MmImportBatchResultDTO batchResult
    ) {

        List<MmIssueDetailEntity> materialIssueDetailEntities = mmIssueDetailRepository.findByOrgIdAndProjectIdAndMmIssueIdAndStatus(
            orgId,
            projectId,
            materialIssueEntityId,
            EntityStatus.ACTIVE
        );
        if (materialIssueDetailEntities.size() > 0) {
            mmIssueDetailRepository.deleteAll(materialIssueDetailEntities);
        }

        Iterator<Row> rows = sheet.rowIterator();
        Row row;
        int totalCount = 0;
        int skippedCount = 0;
        int processedCount = 0;
        int errorCount = 0;

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
                String materialCodeNo = WorkbookUtils.readAsString(row, colIndex++);
                if (materialCodeNo == null || "".equals(materialCodeNo)) {
                    throw new BusinessError("材料编码不能为空");
                }
                String specName = WorkbookUtils.readAsString(row, colIndex++);
                if (specName == null || "".equals(specName)) {
                    throw new BusinessError("规格名称不能为空");
                }

                MmMaterialInStockDetailEntity mmMaterialInStockDetailEntity = mmMaterialInStockDetailRepository.findByOrgIdAndProjectIdAndMmMaterialInStockIdAndMmMaterialCodeNoAndSpecValueAndStatus(
                    orgId,
                    projectId,
                    materialIssueEntityId,
                    materialCodeNo,
                    0.0,
                    EntityStatus.ACTIVE
                );
                if (mmMaterialInStockDetailEntity == null) {
                    throw new BusinessError("当前项目中不存在导入的材料编码及规格名称");
                }

                String unit = WorkbookUtils.readAsString(row, colIndex++);
                if (unit == null || "".equals(unit)) {
                    throw new BusinessError("单位不能为空");
                }

                if (!mmMaterialInStockDetailEntity.getDesignUnit().equals(unit)) {
                    throw new BusinessError("单位不匹配");
                }

                String specQtyString = WorkbookUtils.readAsString(row, colIndex++);
                if (specQtyString == null || "".equals(specQtyString)) {
                    throw new BusinessError("规格量不能为空");
                }
                Integer specQty = 0;
                if (specQtyString != null && !"".equals(specQtyString)) {
                    specQty = Integer.valueOf(specQtyString);
                }

//                if (mmMaterialInStockDetailEntity.getSpecQty() != specQty) {
//                    throw new BusinessError("规格量不匹配");
//                }

                String planQtyString = WorkbookUtils.readAsString(row, colIndex++);
                if (planQtyString == null || "".equals(planQtyString)) {
                    throw new BusinessError("出库量不能为空");
                }
                Integer planQty = 0;
                if (planQtyString != null && !"".equals(planQtyString)) {
                    planQty = Integer.valueOf(planQtyString);
                }


                MmIssueDetailEntity mmIssueDetailEntity = mmIssueDetailRepository.findByOrgIdAndProjectIdAndMmIssueIdAndMmMaterialCodeNoAndSpecValueAndStatus(
                    orgId,
                    projectId,
                    materialIssueEntityId,
                    materialCodeNo,
                    0.0,
                    EntityStatus.ACTIVE
                );
                if (mmIssueDetailEntity == null) {
                    mmIssueDetailEntity = new MmIssueDetailEntity();
                    mmIssueDetailEntity.setCreatedAt(new Date());
                    mmIssueDetailEntity.setCreatedBy(operator.getId());
                }
                BeanUtils.copyProperties(
                    mmMaterialInStockDetailEntity,
                    mmIssueDetailEntity,
                    "id",
                    "createdBy",
                    "createdAt"
                );
                mmIssueDetailEntity.setMmIssueId(materialIssueEntityId);
//                mmIssueDetailEntity.setPlanIssuedQty(planQty);
                mmIssueDetailEntity.setLastModifiedAt(new Date());
                mmIssueDetailEntity.setLastModifiedBy(operator.getId());
                mmIssueDetailEntity.setStatus(EntityStatus.ACTIVE);
                mmIssueDetailRepository.save(mmIssueDetailEntity);

                processedCount++;
                batchResult.addProcessedCount(1);

            } catch (Exception e) {
                e.printStackTrace(System.out);
                errorCount++;
                batchResult.addErrorCount(1);
                setImportDataErrorMessage(row, "" + colIndex + "th import error." + e.getMessage());
            }

        }

        return new MmImportBatchResultDTO(
            totalCount,
            processedCount,
            skippedCount,
            errorCount
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
     * 出库单详情列表。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @return
     */
    @Override
    public Page<MmIssueDetailEntity> searchDetails(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        MmIssueSearchDTO mmIssueSearchDTO
    ) {
        return mmIssueDetailRepository.findByOrgIdAndProjectIdAndMmIssueIdAndStatusOrderByIdentCode(
            orgId,
            projectId,
            materialIssueEntityId,
            EntityStatus.ACTIVE,
            mmIssueSearchDTO.toPageable()
        );
    }

    /**
     * 出库单详情列表。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @return
     */
    @Override
    public Page<MmIssueDetailQrCodeEntity> searchQrCode(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        Long mmIssueDetailEntityId,
        MmIssueSearchDTO mmIssueSearchDTO
    ) {
        return mmIssueDetailQrCodeRepository.findByOrgIdAndProjectIdAndMmIssueDetailIdAndStatus(
            orgId,
            projectId,
            mmIssueDetailEntityId,
            EntityStatus.ACTIVE,
            mmIssueSearchDTO.toPageable()
        );
    }

    /**
     * 出库盘点。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @return
     */
    public void inventoryQrCodes(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        MmIssueInventoryQrCodeDTO mmIssueInventoryQrCodeDTO,
        ContextDTO contextDTO
    ) {

        if (null == mmIssueInventoryQrCodeDTO.getQrCode()) {
            throw new BusinessError("请输入件号或者二维码");
        }
        // 在库材料二维码信息
        MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndQrCodeAndStatus(
            orgId,
            projectId,
            mmIssueInventoryQrCodeDTO.getQrCode(),
            EntityStatus.ACTIVE
        );

        if (null == mmMaterialInStockDetailQrCodeEntity) {
            mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndPieceTagNoAndStatus(
                orgId,
                projectId,
                mmIssueInventoryQrCodeDTO.getQrCode(),
                EntityStatus.ACTIVE
            );
        }

        if (mmMaterialInStockDetailQrCodeEntity == null) {
            throw new BusinessError("二维码不存在于当前项目");
        }

        if (mmMaterialInStockDetailQrCodeEntity.getInStockTotalQty() <= 0) {
            throw new BusinessError("该材料已出库");
        }

        // 项目材料编码
        MmMaterialCodeEntity mmMaterialCodeEntity = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndNoAndStatus(
            orgId,
            projectId,
            mmMaterialInStockDetailQrCodeEntity.getMmMaterialCodeNo(),
            EntityStatus.ACTIVE
        );

        if (null == mmMaterialCodeEntity) {
            throw new BusinessError("材料编码不存在于当前项目");
        }

        // 检查是否必填及数量check
        if (mmMaterialInStockDetailQrCodeEntity.getQrCodeType().equals(QrCodeType.GOODS)) {
            // 出库数量为一物一码
            if (null != mmMaterialCodeEntity.getNeedSpec() && mmMaterialCodeEntity.getNeedSpec()) {
                if (null == mmIssueInventoryQrCodeDTO.getPieceQty()) {
                    throw new BusinessError("规格量管理的一物一码的材料 PieceQty 为必填");
                }
                if (1 != mmIssueInventoryQrCodeDTO.getPieceQty()) {
                    throw new BusinessError("一物一码的材料 PieceQty 必须为1");
                }
            } else {
                if (null == mmIssueInventoryQrCodeDTO.getTotalQty()) {
                    throw new BusinessError("非规格量管理的一物一码的材料 TotalQty 为必填");
                }
                if (1 != mmIssueInventoryQrCodeDTO.getTotalQty()) {
                    throw new BusinessError("一物一码的材料 TotalQty 必须为1");
                }
            }
        } else {
            // 出库数量为一类一码
            if (null != mmMaterialCodeEntity.getNeedSpec() && mmMaterialCodeEntity.getNeedSpec()) {
                if (null == mmIssueInventoryQrCodeDTO.getPieceQty()) {
                    throw new BusinessError("规格量管理的一类一码的材料 PieceQty 为必填");
                }
                if (mmIssueInventoryQrCodeDTO.getPieceQty() > mmMaterialInStockDetailQrCodeEntity.getInStockPieceQty()) {
                    throw new BusinessError("一类一码的材料 PieceQty 必须小于在库材料件数");
                }
            } else {
                if (null == mmIssueInventoryQrCodeDTO.getTotalQty()) {
                    throw new BusinessError("非规格量管理的一类一码的材料 TotalQty 为必填");
                }
                if (mmIssueInventoryQrCodeDTO.getTotalQty() > mmMaterialInStockDetailQrCodeEntity.getInStockTotalQty()) {
                    throw new BusinessError("一类一码的材料 TotalQty 必须小于在库材料总量");
                }
            }
        }

        // 在库材料详情
        MmMaterialInStockDetailEntity mmMaterialInStockDetailEntity = mmMaterialInStockDetailRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            mmMaterialInStockDetailQrCodeEntity.getMmMaterialInStockDetailId(),
            EntityStatus.ACTIVE
        );

        if (mmMaterialInStockDetailEntity == null) {
            throw new BusinessError("该材料不在库存中");
        }

        // 在库材料主表
        MmMaterialInStockEntity mmMaterialInStockEntity = mmMaterialInStockRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            mmMaterialInStockDetailEntity.getMmMaterialInStockId(),
            EntityStatus.ACTIVE
        );

        if (mmMaterialInStockEntity == null) {
            throw new BusinessError("该材料主表不存在");
        }

        // 查找出库详情
        MmIssueDetailEntity mmIssueDetailEntity = mmIssueDetailRepository.findByOrgIdAndProjectIdAndMmIssueIdAndMmMaterialCodeNoAndSpecValueAndStatus(
            orgId,
            projectId,
            materialIssueEntityId,
            mmMaterialInStockDetailEntity.getMmMaterialCodeNo(),
            mmMaterialInStockDetailEntity.getSpecValue(),
            EntityStatus.ACTIVE
        );

        if (mmIssueDetailEntity == null) {
            throw new BusinessError("出库盘点材料不存在于当前出库单中");
        }

        // 更新在库材料二维码信息
        if (null != mmIssueInventoryQrCodeDTO.getTotalQty()) {
            mmMaterialInStockDetailQrCodeEntity.setIssuedTotalQty(mmMaterialInStockDetailQrCodeEntity.getIssuedTotalQty() + mmIssueInventoryQrCodeDTO.getTotalQty());
            mmMaterialInStockDetailQrCodeEntity.setInStockTotalQty(mmMaterialInStockDetailQrCodeEntity.getInStockTotalQty() - mmIssueInventoryQrCodeDTO.getTotalQty());
        }
        if (null != mmIssueInventoryQrCodeDTO.getPieceQty()) {
            mmMaterialInStockDetailQrCodeEntity.setIssuedPieceQty(mmMaterialInStockDetailQrCodeEntity.getIssuedPieceQty() + mmIssueInventoryQrCodeDTO.getPieceQty());
            mmMaterialInStockDetailQrCodeEntity.setInStockPieceQty(mmMaterialInStockDetailQrCodeEntity.getInStockPieceQty() - mmIssueInventoryQrCodeDTO.getPieceQty());
            mmMaterialInStockDetailQrCodeEntity.setIssuedTotalQty(mmMaterialInStockDetailQrCodeEntity.getIssuedTotalQty() + mmIssueInventoryQrCodeDTO.getPieceQty() * mmMaterialInStockDetailQrCodeEntity.getSpecValue());
            mmMaterialInStockDetailQrCodeEntity.setInStockTotalQty(mmMaterialInStockDetailQrCodeEntity.getInStockTotalQty() - mmIssueInventoryQrCodeDTO.getPieceQty() * mmMaterialInStockDetailQrCodeEntity.getSpecValue());
        }
        mmMaterialInStockDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmMaterialInStockDetailQrCodeEntity.setLastModifiedAt(new Date());
        mmMaterialInStockDetailQrCodeRepository.save(mmMaterialInStockDetailQrCodeEntity);

        // 更新在库材料详情
        if (null != mmIssueInventoryQrCodeDTO.getTotalQty()) {
            mmMaterialInStockDetailEntity.setIssuedTotalQty(mmMaterialInStockDetailEntity.getIssuedTotalQty() + mmIssueInventoryQrCodeDTO.getTotalQty());
            mmMaterialInStockDetailEntity.setInStockTotalQty(mmMaterialInStockDetailEntity.getInStockTotalQty() - mmIssueInventoryQrCodeDTO.getTotalQty());
        }
        if (null != mmIssueInventoryQrCodeDTO.getPieceQty()) {
            mmMaterialInStockDetailEntity.setIssuedPieceQty(mmMaterialInStockDetailEntity.getIssuedPieceQty() + mmIssueInventoryQrCodeDTO.getPieceQty());
            mmMaterialInStockDetailEntity.setInStockPieceQty(mmMaterialInStockDetailEntity.getInStockPieceQty() - mmIssueInventoryQrCodeDTO.getPieceQty());
            mmMaterialInStockDetailEntity.setIssuedTotalQty(mmMaterialInStockDetailEntity.getIssuedTotalQty() + mmIssueInventoryQrCodeDTO.getPieceQty() * mmMaterialInStockDetailQrCodeEntity.getSpecValue());
            mmMaterialInStockDetailEntity.setInStockTotalQty(mmMaterialInStockDetailEntity.getInStockTotalQty() - mmIssueInventoryQrCodeDTO.getPieceQty() * mmMaterialInStockDetailQrCodeEntity.getSpecValue());
        }
        mmMaterialInStockDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmMaterialInStockDetailEntity.setLastModifiedAt(new Date());
        mmMaterialInStockDetailRepository.save(mmMaterialInStockDetailEntity);

        // 更新在库材料主表
        if (null != mmIssueInventoryQrCodeDTO.getTotalQty()) {
            mmMaterialInStockEntity.setIssuedTotalQty(mmMaterialInStockEntity.getIssuedTotalQty() + mmIssueInventoryQrCodeDTO.getTotalQty());
            mmMaterialInStockEntity.setInStockTotalQty(mmMaterialInStockEntity.getInStockTotalQty() - mmIssueInventoryQrCodeDTO.getTotalQty());
        }
        if (null != mmIssueInventoryQrCodeDTO.getPieceQty()) {
            mmMaterialInStockEntity.setIssuedPieceQty(mmMaterialInStockEntity.getIssuedPieceQty() + mmIssueInventoryQrCodeDTO.getPieceQty());
            mmMaterialInStockEntity.setInStockPieceQty(mmMaterialInStockEntity.getInStockPieceQty() - mmIssueInventoryQrCodeDTO.getPieceQty());
            mmMaterialInStockEntity.setIssuedTotalQty(mmMaterialInStockEntity.getIssuedTotalQty() + mmIssueInventoryQrCodeDTO.getPieceQty() * mmMaterialInStockDetailQrCodeEntity.getSpecValue());
            mmMaterialInStockEntity.setInStockTotalQty(mmMaterialInStockEntity.getInStockTotalQty() - mmIssueInventoryQrCodeDTO.getPieceQty() * mmMaterialInStockDetailQrCodeEntity.getSpecValue());
        }
        mmMaterialInStockEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmMaterialInStockEntity.setLastModifiedAt(new Date());
        mmMaterialInStockRepository.save(mmMaterialInStockEntity);

        // 更新创建出库二维码信息
        MmIssueDetailQrCodeEntity mmIssueDetailQrCodeEntity = mmIssueDetailQrCodeRepository.findByOrgIdAndProjectIdAndMmIssueDetailIdAndQrCodeAndStatus(
            orgId,
            projectId,
            mmIssueDetailEntity.getId(),
            mmIssueInventoryQrCodeDTO.getQrCode(),
            EntityStatus.ACTIVE
        );
        if (mmIssueDetailQrCodeEntity == null) {
            mmIssueDetailQrCodeEntity = new MmIssueDetailQrCodeEntity();
            BeanUtils.copyProperties(mmMaterialInStockDetailQrCodeEntity, mmIssueDetailQrCodeEntity);
            mmIssueDetailQrCodeEntity.setCreatedAt(new Date());
            mmIssueDetailQrCodeEntity.setCreatedBy(contextDTO.getOperator().getId());
            mmIssueDetailQrCodeEntity.setStatus(EntityStatus.ACTIVE);
            mmIssueDetailQrCodeEntity.setMmIssueDetailId(mmIssueDetailEntity.getId());
        }
        if (null != mmIssueInventoryQrCodeDTO.getTotalQty()) {
            mmIssueDetailQrCodeEntity.setInventoryTotalQty(mmIssueDetailQrCodeEntity.getInventoryTotalQty() + mmIssueInventoryQrCodeDTO.getTotalQty());
        }
        if (null != mmIssueInventoryQrCodeDTO.getPieceQty()) {
            mmIssueDetailQrCodeEntity.setInventoryPieceQty(mmIssueDetailQrCodeEntity.getInventoryPieceQty() + mmIssueInventoryQrCodeDTO.getPieceQty());
            mmIssueDetailQrCodeEntity.setInventoryTotalQty(mmIssueDetailQrCodeEntity.getInventoryTotalQty() + mmIssueInventoryQrCodeDTO.getPieceQty() * mmMaterialInStockDetailQrCodeEntity.getSpecValue());
        }
        mmIssueDetailQrCodeEntity.setLastModifiedAt(new Date());
        mmIssueDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmIssueDetailQrCodeRepository.save(mmIssueDetailQrCodeEntity);

        // 更新出库单详情
        mmIssueDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmIssueDetailEntity.setLastModifiedAt(new Date());
        if (null != mmIssueInventoryQrCodeDTO.getTotalQty()) {
            mmIssueDetailEntity.setIssuedTotalQty(mmIssueDetailEntity.getIssuedTotalQty() + mmIssueInventoryQrCodeDTO.getTotalQty());
        }
        if (null != mmIssueInventoryQrCodeDTO.getPieceQty()) {
            mmIssueDetailEntity.setIssuedPieceQty(mmIssueDetailEntity.getIssuedPieceQty() + mmIssueInventoryQrCodeDTO.getPieceQty());
            mmIssueDetailEntity.setIssuedTotalQty(mmIssueDetailEntity.getIssuedTotalQty() + mmIssueInventoryQrCodeDTO.getPieceQty() * mmMaterialInStockDetailQrCodeEntity.getSpecValue());
        }
        mmIssueDetailRepository.save(mmIssueDetailEntity);

    }

    /**
     * 出库确认。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @return
     */
    public void confirm(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        ContextDTO contextDTO
    ) {
        MmIssueEntity mmIssueEntity = mmIssueRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId, projectId, materialIssueEntityId,
            EntityStatus.ACTIVE
        );
        if (mmIssueEntity == null) {
            throw new BusinessError("出库单不存在");
        }

        List<MmIssueDetailEntity> mmIssueDetailEntities = mmIssueDetailRepository.findByOrgIdAndProjectIdAndMmIssueIdAndStatus(
            orgId,
            projectId,
            materialIssueEntityId,
            EntityStatus.ACTIVE
        );
        if (mmIssueDetailEntities == null || mmIssueDetailEntities.size() == 0) {
            throw new BusinessError("出库单内不存在明细");
        }
        for (MmIssueDetailEntity mmIssueDetailEntity : mmIssueDetailEntities) {
            PageDTO pageDTO = new PageDTO();
            pageDTO.setFetchAll(true);
            List<MmIssueDetailQrCodeEntity> mmIssueDetailQrCodeEntities = mmIssueDetailQrCodeRepository.findByOrgIdAndProjectIdAndMmIssueDetailIdAndStatus(
                orgId,
                projectId,
                mmIssueDetailEntity.getId(),
                EntityStatus.ACTIVE,
                pageDTO.toPageable()
            ).getContent();
            if (mmIssueDetailQrCodeEntities.size() > 0) {
                for (MmIssueDetailQrCodeEntity mmIssueDetailQrCodeEntity : mmIssueDetailQrCodeEntities) {

                    MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndQrCodeAndStatus(
                        orgId,
                        projectId,
                        mmIssueDetailQrCodeEntity.getQrCode(),
                        EntityStatus.ACTIVE
                    );

                    MmMaterialInStockDetailEntity mmMaterialInStockDetailEntity = mmMaterialInStockDetailRepository.findByOrgIdAndProjectIdAndIdAndStatus(
                        orgId,
                        projectId,
                        mmMaterialInStockDetailQrCodeEntity.getMmMaterialInStockDetailId(),
                        EntityStatus.ACTIVE
                    );

                    MmMaterialInStockEntity mmMaterialInStockEntity = mmMaterialInStockRepository.findByOrgIdAndProjectIdAndIdAndStatus(
                        orgId, projectId, mmMaterialInStockDetailEntity.getMmMaterialInStockId(), EntityStatus.ACTIVE
                    );

//                    mmMaterialInStockDetailQrCodeEntity.setLockingQty(mmMaterialInStockDetailQrCodeEntity.getLockingQty() - mmIssueDetailQrCodeEntity.getInventoryQty());
//                    mmMaterialInStockDetailQrCodeEntity.setInStockQty(mmMaterialInStockDetailQrCodeEntity.getInStockQty() - mmIssueDetailQrCodeEntity.getInventoryQty());
//                    if (mmMaterialInStockDetailQrCodeEntity.getInStockQty() == 0) {
//                        mmMaterialInStockDetailQrCodeEntity.setRunningStatus(EntityStatus.APPROVED);
//                    }
//                    mmMaterialInStockDetailQrCodeEntity.setIssuedQty(mmMaterialInStockDetailQrCodeEntity.getIssuedQty() + mmIssueDetailQrCodeEntity.getInventoryQty());
                    mmMaterialInStockDetailQrCodeRepository.save(mmMaterialInStockDetailQrCodeEntity);

//                    mmMaterialInStockDetailEntity.setInStockQty(mmMaterialInStockDetailEntity.getInStockQty() - mmIssueDetailQrCodeEntity.getInventoryQty());
//                    mmMaterialInStockDetailEntity.setIssuedQty(mmMaterialInStockDetailEntity.getIssuedQty() + mmIssueDetailQrCodeEntity.getInventoryQty());
                    mmMaterialInStockDetailEntity.setLastModifiedAt(new Date());
                    mmMaterialInStockDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmMaterialInStockDetailRepository.save(mmMaterialInStockDetailEntity);

//                    mmMaterialInStockEntity.setInStockQty(mmMaterialInStockEntity.getInStockQty() - mmIssueDetailQrCodeEntity.getInventoryQty());
//                    mmMaterialInStockEntity.setIssuedQty(mmMaterialInStockEntity.getIssuedQty() + mmIssueDetailQrCodeEntity.getInventoryQty());
                    mmMaterialInStockEntity.setLastModifiedAt(new Date());
                    mmMaterialInStockEntity.setLastModifiedBy(contextDTO.getOperator().getId());
                    mmMaterialInStockRepository.save(mmMaterialInStockEntity);
                }
            }

        }

        mmIssueEntity.setRunningStatus(EntityStatus.APPROVED);
        mmIssueEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmIssueEntity.setLastModifiedAt(new Date());
        mmIssueRepository.save(mmIssueEntity);
    }

    /**
     * 添加出库详情。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @return
     */
    @Override
    public void addDetail(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        MmIssueDetailCreateDTO mmIssueDetailCreateDTO,
        ContextDTO contextDTO
    ) {
        if (mmIssueDetailCreateDTO.getMmIssueDetailCreateItemDTOs() == null || mmIssueDetailCreateDTO.getMmIssueDetailCreateItemDTOs().size() == 0) {
            throw new BusinessError("详情创建信息不存在");
        }

//        List<MmIssueDetailEntity> mmIssueDetailEntities = mmIssueDetailRepository.findByOrgIdAndProjectIdAndMmIssueIdAndStatus(
//            orgId,
//            projectId,
//            materialIssueEntityId,
//            EntityStatus.ACTIVE
//        );
//
//        mmIssueDetailRepository.deleteAll(mmIssueDetailEntities);

        for (MmIssueDetailCreateItemDTO mmIssueDetailCreateItemDTO : mmIssueDetailCreateDTO.getMmIssueDetailCreateItemDTOs()) {

            MmIssueDetailEntity mmIssueDetailEntity = mmIssueDetailRepository.findByOrgIdAndProjectIdAndMmIssueIdAndMmMaterialCodeNoAndSpecValueAndStatus(
                orgId,
                projectId,
                materialIssueEntityId,
                mmIssueDetailCreateItemDTO.getMmMaterialCodeNo(),
                mmIssueDetailCreateItemDTO.getSpecValue(),
                EntityStatus.ACTIVE
            );
            if (mmIssueDetailEntity != null) {

            } else {
                mmIssueDetailEntity = new MmIssueDetailEntity();
            }
            BeanUtils.copyProperties(
                mmIssueDetailCreateItemDTO,
                mmIssueDetailEntity
            );

            mmIssueDetailEntity.setPlanIssuedTotalQty(0.0);
            mmIssueDetailEntity.setIssuedTotalQty(0.0);

            mmIssueDetailEntity.setPlanIssuedPieceQty(0);
            mmIssueDetailEntity.setIssuedPieceQty(0);

            mmIssueDetailEntity.setOrgId(orgId);
            mmIssueDetailEntity.setMmIssueId(materialIssueEntityId);
            mmIssueDetailEntity.setProjectId(projectId);
            mmIssueDetailEntity.setCreatedAt(new Date());
            mmIssueDetailEntity.setCreatedBy(contextDTO.getOperator().getId());
            mmIssueDetailEntity.setLastModifiedAt(new Date());
            mmIssueDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
            mmIssueDetailEntity.setStatus(EntityStatus.ACTIVE);
            mmIssueDetailRepository.save(mmIssueDetailEntity);
        }

    }

    /**
     * 删除出库单。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @param contextDTO
     */
    @Override
    public void deleteItem(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        Long mmIssueDetailEntityId,
        ContextDTO contextDTO
    ) {

        MmIssueEntity mmIssueEntity = mmIssueRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialIssueEntityId,
            EntityStatus.ACTIVE
        );
        if (mmIssueEntity == null) {
            throw new BusinessError("出库单不存在");
        }

        if (mmIssueEntity.getRunningStatus().equals(EntityStatus.APPROVED)) {
            throw new BusinessError("出库单已完成");
        }

        MmIssueDetailEntity mmIssueDetailEntity = mmIssueDetailRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            mmIssueDetailEntityId,
            EntityStatus.ACTIVE
        );
        if (mmIssueDetailEntity == null) {
            throw new BusinessError("出库单详情不存在");
        }

        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<MmIssueDetailQrCodeEntity> mmIssueDetailQrCodeEntities = mmIssueDetailQrCodeRepository.findByOrgIdAndProjectIdAndMmIssueDetailIdAndStatus(
            orgId,
            projectId,
            mmIssueDetailEntityId,
            EntityStatus.ACTIVE,
            pageDTO.toPageable()
        ).getContent();
        if (mmIssueDetailQrCodeEntities.size() > 0) {
            for (MmIssueDetailQrCodeEntity mmIssueDetailQrCodeEntity : mmIssueDetailQrCodeEntities) {
                deleteQrCode(
                    orgId,
                    projectId,
                    materialIssueEntityId,
                    mmIssueDetailEntityId,
                    mmIssueDetailQrCodeEntity.getId(),
                    contextDTO
                );
            }
        }

        mmIssueDetailEntity.setLastModifiedAt(new Date());
        mmIssueDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmIssueDetailEntity.setStatus(EntityStatus.DELETED);

        mmIssueDetailRepository.save(mmIssueDetailEntity);

    }

    /**
     * 删除出库单。
     *
     * @param orgId
     * @param projectId
     * @param materialIssueEntityId
     * @param contextDTO
     */
    @Override
    public void deleteQrCode(
        Long orgId,
        Long projectId,
        Long materialIssueEntityId,
        Long mmIssueDetailEntityId,
        Long mmIssueDetailQrCodeId,
        ContextDTO contextDTO
    ) {

        MmIssueEntity mmIssueEntity = mmIssueRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialIssueEntityId,
            EntityStatus.ACTIVE
        );
        if (mmIssueEntity == null) {
            throw new BusinessError("出库单不存在");
        }

        if (mmIssueEntity.getRunningStatus().equals(EntityStatus.APPROVED)) {
            throw new BusinessError("出库单已完成");
        }

        MmIssueDetailEntity mmIssueDetailEntity = mmIssueDetailRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            mmIssueDetailEntityId,
            EntityStatus.ACTIVE
        );
        if (mmIssueDetailEntity == null) {
            throw new BusinessError("出库单详情不存在");
        }

        MmIssueDetailQrCodeEntity mmIssueDetailQrCodeEntity = mmIssueDetailQrCodeRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            mmIssueDetailQrCodeId,
            EntityStatus.ACTIVE
        );
        if (mmIssueDetailQrCodeEntity == null) {
            throw new BusinessError("出库单详情二维码不存在");
        }

        MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockDetailQrCodeRepository.findByOrgIdAndProjectIdAndQrCodeAndStatus(
            orgId,
            projectId,
            mmIssueDetailQrCodeEntity.getQrCode(),
            EntityStatus.ACTIVE
        );

        if (mmMaterialInStockDetailQrCodeEntity == null) {
            throw new BusinessError("在库材料二维码信息不存在");
        }

        // 查找在库材料详情表
        MmMaterialInStockDetailEntity mmMaterialInStockDetailEntity = mmMaterialInStockDetailRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            mmMaterialInStockDetailQrCodeEntity.getMmMaterialInStockDetailId(),
            EntityStatus.ACTIVE
        );
        if (null == mmMaterialInStockDetailEntity) {
            throw new BusinessError("在库材料详情信息不存在");
        }
        // 查找在库材料主表
        MmMaterialInStockEntity mmMaterialInStockEntity = mmMaterialInStockRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            mmMaterialInStockDetailEntity.getMmMaterialInStockId(),
            EntityStatus.ACTIVE
        );
        if (null == mmMaterialInStockEntity) {
            throw new BusinessError("在库材料主表信息不存在");
        }

        // 删除出库单二维码信息
        mmIssueDetailQrCodeEntity.setLastModifiedAt(new Date());
        mmIssueDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmIssueDetailQrCodeEntity.setStatus(EntityStatus.DELETED);
        mmIssueDetailQrCodeRepository.save(mmIssueDetailQrCodeEntity);

        // TODO 更新出库单详情
        mmIssueDetailEntity.setIssuedPieceQty(mmIssueDetailEntity.getIssuedPieceQty() - mmIssueDetailQrCodeEntity.getInventoryPieceQty());
        mmIssueDetailEntity.setIssuedTotalQty(mmIssueDetailEntity.getIssuedTotalQty() - mmIssueDetailQrCodeEntity.getInventoryTotalQty());
        mmIssueDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmIssueDetailEntity.setLastModifiedAt(new Date());
        mmIssueDetailRepository.save(mmIssueDetailEntity);

        // TODO 更新在库材料二维码信息
        mmMaterialInStockDetailQrCodeEntity.setIssuedPieceQty(mmMaterialInStockDetailQrCodeEntity.getIssuedPieceQty() - mmIssueDetailQrCodeEntity.getInventoryPieceQty());
        mmMaterialInStockDetailQrCodeEntity.setInStockPieceQty(mmMaterialInStockDetailQrCodeEntity.getInStockPieceQty() + mmIssueDetailQrCodeEntity.getInventoryPieceQty());
        mmMaterialInStockDetailQrCodeEntity.setIssuedTotalQty(mmMaterialInStockDetailQrCodeEntity.getIssuedTotalQty() - mmIssueDetailQrCodeEntity.getInventoryTotalQty());
        mmMaterialInStockDetailQrCodeEntity.setInStockTotalQty(mmMaterialInStockDetailQrCodeEntity.getInStockTotalQty() + mmIssueDetailQrCodeEntity.getInventoryTotalQty());
        mmMaterialInStockDetailQrCodeEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmMaterialInStockDetailQrCodeEntity.setLastModifiedAt(new Date());
        mmMaterialInStockDetailQrCodeRepository.save(mmMaterialInStockDetailQrCodeEntity);
        // TODO 更新在库材料详情信息
        mmMaterialInStockDetailEntity.setIssuedPieceQty(mmMaterialInStockDetailEntity.getIssuedPieceQty() - mmIssueDetailQrCodeEntity.getInventoryPieceQty());
        mmMaterialInStockDetailEntity.setInStockPieceQty(mmMaterialInStockDetailEntity.getInStockPieceQty() + mmIssueDetailQrCodeEntity.getInventoryPieceQty());
        mmMaterialInStockDetailEntity.setIssuedTotalQty(mmMaterialInStockDetailEntity.getIssuedTotalQty() - mmIssueDetailQrCodeEntity.getInventoryTotalQty());
        mmMaterialInStockDetailEntity.setInStockTotalQty(mmMaterialInStockDetailEntity.getInStockTotalQty() + mmIssueDetailQrCodeEntity.getInventoryTotalQty());
        mmMaterialInStockDetailEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmMaterialInStockDetailEntity.setLastModifiedAt(new Date());
        mmMaterialInStockDetailRepository.save(mmMaterialInStockDetailEntity);

        // TODO 更新在库材料主表信息
        mmMaterialInStockEntity.setIssuedPieceQty(mmMaterialInStockEntity.getIssuedPieceQty() - mmIssueDetailQrCodeEntity.getInventoryPieceQty());
        mmMaterialInStockEntity.setInStockPieceQty(mmMaterialInStockEntity.getInStockPieceQty() + mmIssueDetailQrCodeEntity.getInventoryPieceQty());
        mmMaterialInStockEntity.setIssuedTotalQty(mmMaterialInStockEntity.getIssuedTotalQty() - mmIssueDetailQrCodeEntity.getInventoryTotalQty());
        mmMaterialInStockEntity.setInStockTotalQty(mmMaterialInStockEntity.getInStockTotalQty() + mmIssueDetailQrCodeEntity.getInventoryTotalQty());
        mmMaterialInStockEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmMaterialInStockEntity.setLastModifiedAt(new Date());
        mmMaterialInStockRepository.save(mmMaterialInStockEntity);

    }
}
