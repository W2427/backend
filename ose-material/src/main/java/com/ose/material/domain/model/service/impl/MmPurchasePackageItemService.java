package com.ose.material.domain.model.service.impl;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.material.domain.model.repository.*;
import com.ose.material.domain.model.service.MmPurchasePackageItemInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.*;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.material.vo.QrCodeType;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
public class MmPurchasePackageItemService implements MmPurchasePackageItemInterface {

    @Value("${application.files.temporary}")
    private String temporaryDir;
    /**
     * 采购包明细  操作仓库。
     */
    private final MmPurchasePackageItemRepository mmPurchasePackageItemRepository;
    private final MmImportBatchTaskRepository mmImportBatchTaskRepository;
    private final MmPurchasePackageRepository mmPurchasePackageRepository;
    private final MmMaterialCodeRepository mmMaterialCodeRepository;
    private final HeatBatchNoRepository heatBatchNoRepository;
    private final MmWareHouseLocationRepository mmWareHouseLocationRepository;

    /**
     * 构造方法。
     *
     * @param mmPurchasePackageItemRepository
     */
    @Autowired
    public MmPurchasePackageItemService(
        MmPurchasePackageItemRepository mmPurchasePackageItemRepository,
        MmImportBatchTaskRepository mmImportBatchTaskRepository,
        MmPurchasePackageRepository mmPurchasePackageRepository,
        MmMaterialCodeRepository mmMaterialCodeRepository,
        HeatBatchNoRepository heatBatchNoRepository,
        MmWareHouseLocationRepository mmWareHouseLocationRepository
    ) {
        this.mmPurchasePackageItemRepository = mmPurchasePackageItemRepository;
        this.mmImportBatchTaskRepository = mmImportBatchTaskRepository;
        this.mmPurchasePackageRepository = mmPurchasePackageRepository;
        this.mmMaterialCodeRepository = mmMaterialCodeRepository;
        this.heatBatchNoRepository = heatBatchNoRepository;
        this.mmWareHouseLocationRepository = mmWareHouseLocationRepository;
    }

    /**
     * 查询采购单明细列表。
     *
     * @param orgId
     * @param projectId
     * @param mmPurchasePackageItemSearchDTO
     * @return
     */
    @Override
    public Page<MmPurchasePackageItemEntity> search(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageItemSearchDTO mmPurchasePackageItemSearchDTO
    ) {
        if (mmPurchasePackageItemSearchDTO.getKeyword() != null) {
            return null;
        } else {
            return mmPurchasePackageItemRepository.findByOrgIdAndProjectIdAndPurchasePackageIdAndStatus(
                orgId,
                projectId,
                purchasePackageId,
                EntityStatus.ACTIVE,
                mmPurchasePackageItemSearchDTO.toPageable()
            );
        }
    }

    /**
     * TODO 创建采购单明细。
     *
     * @param orgId
     * @param projectId
     * @param mmPurchasePackageItemCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmPurchasePackageItemEntity create(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageItemCreateDTO mmPurchasePackageItemCreateDTO,
        ContextDTO contextDTO
    ) {

        MmPurchasePackageItemEntity mmPurchasePackageItemEntity = new MmPurchasePackageItemEntity();

        BeanUtils.copyProperties(mmPurchasePackageItemCreateDTO, mmPurchasePackageItemEntity);
        mmPurchasePackageItemEntity.setOrgId(orgId);
        mmPurchasePackageItemEntity.setProjectId(projectId);
        mmPurchasePackageItemEntity.setCreatedAt(new Date());
        mmPurchasePackageItemEntity.setCreatedBy(contextDTO.getOperator().getId());
        mmPurchasePackageItemEntity.setLastModifiedAt(new Date());
        mmPurchasePackageItemEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmPurchasePackageItemEntity.setStatus(EntityStatus.ACTIVE);

        return mmPurchasePackageItemRepository.save(mmPurchasePackageItemEntity);
    }

    /**
     * TODO 更新采购单明细。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     * @param mmPurchasePackageItemCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmPurchasePackageItemEntity update(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        Long purchasePackageDetailId,
        MmPurchasePackageItemCreateDTO mmPurchasePackageItemCreateDTO,
        ContextDTO contextDTO
    ) {
        MmPurchasePackageItemEntity oldMmPurchasePackageItemEntity = mmPurchasePackageItemRepository.findByOrgIdAndProjectIdAndPurchasePackageIdAndIdAndStatus(
            orgId,
            projectId,
            purchasePackageId,
            purchasePackageDetailId,
            EntityStatus.ACTIVE
        );
        if (oldMmPurchasePackageItemEntity == null) {
            throw new BusinessError("Purchase package item info does not exist!采购单明细信息不存在！");
        }

        BeanUtils.copyProperties(mmPurchasePackageItemCreateDTO, oldMmPurchasePackageItemEntity);
        oldMmPurchasePackageItemEntity.setOrgId(orgId);
        oldMmPurchasePackageItemEntity.setProjectId(projectId);
        oldMmPurchasePackageItemEntity.setLastModifiedAt(new Date());
        oldMmPurchasePackageItemEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        oldMmPurchasePackageItemEntity.setStatus(EntityStatus.ACTIVE);

        return mmPurchasePackageItemRepository.save(oldMmPurchasePackageItemEntity);
    }

    /**
     * 采购单明细详情。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     * @return
     */
    @Override
    public MmPurchasePackageItemEntity detail(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        Long purchasePackageDetailId
    ) {
        MmPurchasePackageItemEntity mmPurchasePackageItemEntity = mmPurchasePackageItemRepository.findByOrgIdAndProjectIdAndPurchasePackageIdAndIdAndStatus(
            orgId,
            projectId,
            purchasePackageId,
            purchasePackageDetailId,
            EntityStatus.ACTIVE
        );
        if (mmPurchasePackageItemEntity == null) {
            throw new BusinessError("Purchase package item info does not exist!采购单明细信息不存在！");
        }
        return mmPurchasePackageItemEntity;
    }

    /**
     * 删除采购包明细。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     * @param contextDTO
     */
    @Override
    public void delete(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        Long purchasePackageDetailId,
        ContextDTO contextDTO
    ) {
        MmPurchasePackageItemEntity mmPurchasePackageItemEntity = mmPurchasePackageItemRepository.findByOrgIdAndProjectIdAndPurchasePackageIdAndIdAndStatus(
            orgId,
            projectId,
            purchasePackageId,
            purchasePackageDetailId,
            EntityStatus.ACTIVE
        );
        if (mmPurchasePackageItemEntity == null) {
            throw new BusinessError("Purchase package item info does not exist!采购单明细信息不存在！");
        }

        mmPurchasePackageItemEntity.setLastModifiedAt(new Date());
        mmPurchasePackageItemEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmPurchasePackageItemEntity.setStatus(EntityStatus.DELETED);
        mmPurchasePackageItemEntity.setDeleted(true);
        mmPurchasePackageItemEntity.setDeletedAt(new Date());
        mmPurchasePackageItemEntity.setDeletedBy(contextDTO.getOperator().getId());
        mmPurchasePackageItemRepository.save(mmPurchasePackageItemEntity);
    }


    public MmImportBatchResultDTO importPurchasePackageItemBatch(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
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
        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);

        List<MmPurchasePackageItemEntity> mmPurchasePackageItemEntities = mmPurchasePackageItemRepository.findByOrgIdAndProjectIdAndPurchasePackageIdAndStatus(
            orgId,
            projectId,
            purchasePackageId,
            EntityStatus.ACTIVE,
            pageDTO.toPageable()
        ).getContent();
        mmPurchasePackageItemRepository.deleteAll(mmPurchasePackageItemEntities);

        sheetImportResult = importPurchasePackageItem(
            orgId,
            projectId,
            purchasePackageId,
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
     * @param orgId             组织ID
     * @param projectId         项目ID
     * @param purchasePackageId 项目ID
     * @param operator
     * @param sheet
     * @param batchResult
     * @return
     */
    private MmImportBatchResultDTO importPurchasePackageItem(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
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

                // 查找采购包信息
                MmPurchasePackageEntity mmPurchasePackageEntityFind = mmPurchasePackageRepository.findByOrgIdAndProjectIdAndIdAndStatus(
                    orgId,
                    projectId,
                    purchasePackageId,
                    EntityStatus.ACTIVE
                );
                if (mmPurchasePackageEntityFind == null) {
                    throw new BusinessError("采购包信息不存在");
                }

                // 查找材料编码
                String materialCodeNo = WorkbookUtils.readAsString(row, colIndex++);
                if (materialCodeNo == null || "".equals(materialCodeNo)) {
                    throw new BusinessError("材料编码不能为空");
                }
                MmMaterialCodeEntity mmMaterialCodeEntity = mmMaterialCodeRepository.findByOrgIdAndProjectIdAndNoAndStatus(
                    orgId,
                    projectId,
                    materialCodeNo,
                    EntityStatus.ACTIVE
                );
                if (mmMaterialCodeEntity == null) {
                    throw new BusinessError("项目中不存在此材料编码");
                }

                // 查找规格描述
                String specDescription = WorkbookUtils.readAsString(row, colIndex++);

                // 查找采购总量
                String totalQtyString = WorkbookUtils.readAsString(row, colIndex++);
                if (totalQtyString == null || "".equals(totalQtyString)) {
                    throw new BusinessError("采购总量不能为空");
                }
                Double totalQty = Double.valueOf(totalQtyString);

                if (totalQty.compareTo(0.0) <= 0) {
                    throw new BusinessError("采购总量必须大于0");
                }
                // 查找计量单位
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

                String remarks = WorkbookUtils.readAsString(row, colIndex++);

                // 判断材料是否需要合并（材料编码，规格描述，件号，单件规格量）
                MmPurchasePackageItemEntity searchEntity = mmPurchasePackageItemRepository.findByOrgIdAndProjectIdAndPurchasePackageIdAndMmMaterialCodeNoAndSpecDescriptionAndSpecValueAndStatus(
                    orgId,
                    projectId,
                    purchasePackageId,
                    materialCodeNo,
                    specDescription,
                    specValue,
                    EntityStatus.ACTIVE
                );

                if (searchEntity != null) {
                    searchEntity.setTotalQty(searchEntity.getTotalQty() + totalQty);
                    searchEntity.setPieceQty(searchEntity.getPieceQty() + pieceQty);
                    searchEntity.setLastModifiedAt();
                    searchEntity.setLastModifiedBy(operator.getId());
                    mmPurchasePackageItemRepository.save(searchEntity);
                } else {
                    // 关联外键
                    MmPurchasePackageItemEntity newMmPurchasePackageItemEntity = new MmPurchasePackageItemEntity();
                    newMmPurchasePackageItemEntity.setPurchasePackageId(purchasePackageId);
                    newMmPurchasePackageItemEntity.setPurchasePackageNo(mmPurchasePackageEntityFind.getPwpsNumber());

                    // 关联材料编码信息
                    newMmPurchasePackageItemEntity.setIdentCode(mmMaterialCodeEntity.getIdentCode());
                    newMmPurchasePackageItemEntity.setQrCodeType(mmMaterialCodeEntity.getQrCodeType());
                    newMmPurchasePackageItemEntity.setMmMaterialCodeDescription(mmMaterialCodeEntity.getDescription());

                    // 导入信息
                    newMmPurchasePackageItemEntity.setMmMaterialCodeNo(materialCodeNo);
                    newMmPurchasePackageItemEntity.setSpecDescription(specDescription);
                    newMmPurchasePackageItemEntity.setTotalQty(totalQty);
                    newMmPurchasePackageItemEntity.setBuyTotalQty(0.0);
                    newMmPurchasePackageItemEntity.setDesignUnit(designUnit);
                    newMmPurchasePackageItemEntity.setSpecValue(specValue);
                    newMmPurchasePackageItemEntity.setPieceQty(pieceQty);
                    newMmPurchasePackageItemEntity.setBuyPieceQty(0);
                    newMmPurchasePackageItemEntity.setRemarks(remarks);

                    // 是体表基础信息
                    newMmPurchasePackageItemEntity.setStatus(EntityStatus.ACTIVE);
                    newMmPurchasePackageItemEntity.setCreatedBy(operator.getId());
                    newMmPurchasePackageItemEntity.setCreatedAt();
                    newMmPurchasePackageItemEntity.setLastModifiedBy(operator.getId());
                    newMmPurchasePackageItemEntity.setLastModifiedAt();
                    newMmPurchasePackageItemEntity.setDeleted(false);
                    newMmPurchasePackageItemEntity.setOrgId(orgId);
                    newMmPurchasePackageItemEntity.setProjectId(projectId);
                    newMmPurchasePackageItemEntity.setRemarks(remarks);
                    mmPurchasePackageItemRepository.save(newMmPurchasePackageItemEntity);
                }

                processedCount++;
                batchResult.addProcessedCount(1);

            } catch (Exception e) {
                e.printStackTrace(System.out);
                errorCount++;
                batchResult.addErrorCount(1);
                setImportDataErrorMessage(row, "" + colIndex + "th import error." + e.getMessage());
            }

            if (batchTask.checkRunningStatus()) {
                batchTask.setResult(batchResult);
                batchTask.setLastModifiedAt();
                mmImportBatchTaskRepository.save(batchTask);
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

    /**
     * 设置导入数据错误信息。
     *
     * @param row     错误所在行
     * @param message 错误消息
     */
    private void setImportDataErrorMessage(Row row, String message) {
        row.createCell(row.getLastCellNum()).setCellValue(message);
    }
}
