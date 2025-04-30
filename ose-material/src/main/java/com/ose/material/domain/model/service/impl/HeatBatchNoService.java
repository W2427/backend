package com.ose.material.domain.model.service.impl;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.material.domain.model.repository.HeatBatchNoRepository;
import com.ose.material.domain.model.service.HeatBatchNoInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.MmHeatBatchNoEntity;
import com.ose.material.entity.MmImportBatchTask;
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

@Component
public class HeatBatchNoService implements HeatBatchNoInterface {

    @Value("${application.files.temporary}")
    private String temporaryDir;

    /**
     * 炉批号 操作仓库。
     */
    private final HeatBatchNoRepository heatBatchNoRepository;

    /**
     * 构造方法。
     *
     * @param heatBatchNoRepository FRQ设备物资单管理 操作仓库
     */
    @Autowired
    public HeatBatchNoService(
        HeatBatchNoRepository heatBatchNoRepository
    ) {
        this.heatBatchNoRepository = heatBatchNoRepository;
    }

    /**
     * 查询炉批号列表。
     *
     * @param orgId
     * @param projectId
     * @param heatBatchNoSearchDTO
     * @return
     */
    @Override
    public Page<MmHeatBatchNoEntity> search(
        Long orgId,
        Long projectId,
        HeatBatchNoSearchDTO heatBatchNoSearchDTO
    ) {
        return heatBatchNoRepository.search(orgId, projectId, heatBatchNoSearchDTO);
    }

    /**
     * 创建炉批号。
     *
     * @param orgId
     * @param projectId
     * @param heatBatchNoCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmHeatBatchNoEntity create(
        Long orgId,
        Long projectId,
        HeatBatchNoCreateDTO heatBatchNoCreateDTO,
        ContextDTO contextDTO
    ) {
        MmHeatBatchNoEntity mmHeatBatchNoEntity = new MmHeatBatchNoEntity();

        BeanUtils.copyProperties(heatBatchNoCreateDTO, mmHeatBatchNoEntity);
        mmHeatBatchNoEntity.setOrgId(orgId);
        mmHeatBatchNoEntity.setProjectId(projectId);
        mmHeatBatchNoEntity.setCreatedAt(new Date());
        mmHeatBatchNoEntity.setCreatedBy(contextDTO.getOperator().getId());
        mmHeatBatchNoEntity.setLastModifiedAt(new Date());
        mmHeatBatchNoEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmHeatBatchNoEntity.setStatus(EntityStatus.ACTIVE);

        return heatBatchNoRepository.save(mmHeatBatchNoEntity);
    }

    /**
     * 更新炉批号。
     *
     * @param orgId
     * @param projectId
     * @param heatBatchId
     * @param heatBatchNoCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public MmHeatBatchNoEntity update(
        Long orgId,
        Long projectId,
        Long heatBatchId,
        HeatBatchNoCreateDTO heatBatchNoCreateDTO,
        ContextDTO contextDTO
    ) {
        MmHeatBatchNoEntity mmHeatBatchNoEntity = heatBatchNoRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            heatBatchId,
            EntityStatus.ACTIVE
        );
        if (mmHeatBatchNoEntity == null) {
            throw new BusinessError("炉批号信息不存在");
        }
        BeanUtils.copyProperties(heatBatchNoCreateDTO, mmHeatBatchNoEntity);
        mmHeatBatchNoEntity.setOrgId(orgId);
        mmHeatBatchNoEntity.setProjectId(projectId);
        mmHeatBatchNoEntity.setLastModifiedAt(new Date());
        mmHeatBatchNoEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmHeatBatchNoEntity.setStatus(EntityStatus.ACTIVE);

        return heatBatchNoRepository.save(mmHeatBatchNoEntity);
    }

    /**
     * 炉批号详情。
     *
     * @param orgId
     * @param projectId
     * @param heatBatchId
     * @return
     */
    @Override
    public MmHeatBatchNoEntity detail(
        Long orgId,
        Long projectId,
        Long heatBatchId
    ) {
        MmHeatBatchNoEntity mmHeatBatchNoEntity = heatBatchNoRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            heatBatchId,
            EntityStatus.ACTIVE
        );
        if (mmHeatBatchNoEntity == null) {
            throw new BusinessError("炉批号信息不存在");
        }
        return mmHeatBatchNoEntity;
    }

    /**
     * 删除物料单。
     *
     * @param orgId
     * @param projectId
     * @param heatBatchId
     * @param contextDTO
     */
    @Override
    public void delete(
        Long orgId,
        Long projectId,
        Long heatBatchId,
        ContextDTO contextDTO
    ) {
        MmHeatBatchNoEntity mmHeatBatchNoEntity = heatBatchNoRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            heatBatchId,
            EntityStatus.ACTIVE
        );
        if (mmHeatBatchNoEntity == null) {
            throw new BusinessError("炉批号信息不存在");
        }

        mmHeatBatchNoEntity.setLastModifiedAt(new Date());
        mmHeatBatchNoEntity.setLastModifiedBy(contextDTO.getOperator().getId());
        mmHeatBatchNoEntity.setStatus(EntityStatus.DELETED);
        heatBatchNoRepository.save(mmHeatBatchNoEntity);

    }

    public MmImportBatchResultDTO importHeatBatch(Long orgId,
                                                  Long projectId,
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

        sheetImportResult = importHeatBatch(
            orgId,
            projectId,
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
    private MmImportBatchResultDTO importHeatBatch(Long orgId,
                                                   Long projectId,
                                                   OperatorDTO operator,
                                                   Sheet sheet,
                                                   MmImportBatchResultDTO batchResult
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

                String heatNo = WorkbookUtils.readAsString(row, colIndex++);
                String batchNo = WorkbookUtils.readAsString(row, colIndex++);

                if (heatNo == null || "".equals(heatNo)) {
                    throw new BusinessError("炉号不能为空");
                }

                if (batchNo == null || "".equals(batchNo)) {
                    throw new BusinessError("批号不能为空");
                }

                String desc = WorkbookUtils.readAsString(row, colIndex++);
                MmHeatBatchNoEntity mmHeatBatchNoEntityFind =   heatBatchNoRepository.findByOrgIdAndProjectIdAndHeatNoCodeAndBatchNoCodeAndStatus(
                   orgId,
                   projectId,
                   heatNo,
                   batchNo,
                   EntityStatus.ACTIVE
                );
                if(mmHeatBatchNoEntityFind ==null){
                    mmHeatBatchNoEntityFind = new MmHeatBatchNoEntity();
                }

                mmHeatBatchNoEntityFind.setBatchNoCode(batchNo);
                mmHeatBatchNoEntityFind.setHeatNoCode(heatNo);
                mmHeatBatchNoEntityFind.setDescs(desc);
                mmHeatBatchNoEntityFind.setOrgId(orgId);
                mmHeatBatchNoEntityFind.setProjectId(projectId);
                mmHeatBatchNoEntityFind.setCreatedAt(new Date());
                mmHeatBatchNoEntityFind.setCreatedBy(operator.getId());
                mmHeatBatchNoEntityFind.setLastModifiedAt(new Date());
                mmHeatBatchNoEntityFind.setLastModifiedBy(operator.getId());
                mmHeatBatchNoEntityFind.setStatus(EntityStatus.ACTIVE);
                heatBatchNoRepository.save(mmHeatBatchNoEntityFind);

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

}
