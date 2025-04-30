package com.ose.tasks.domain.model.service.drawing.impl;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.HierarchyRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.drawing.DetailDesignDrawingDetailRepository;
import com.ose.tasks.domain.model.repository.drawing.DetailDesignDrawingRepository;
import com.ose.tasks.domain.model.service.HierarchyNodeRelationInterface;
import com.ose.tasks.domain.model.service.drawing.DetailDesignDrawingInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.drawing.DetailDesignDrawingCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingImportDTO;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.drawing.DetailDesignDrawing;
import com.ose.tasks.entity.drawing.DetailDesignDrawingDetail;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.util.BeanUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ose.vo.EntityStatus.ACTIVE;

@Component
public class DetailDesignDrawingService extends StringRedisService implements DetailDesignDrawingInterface {

    private class Coordinates {

        private int row;
        private int column;

        public Coordinates(int row, int column) {
            super();
            this.row = row;
            this.column = column;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }
    }


    @Value("${application.files.temporary}")
    private String temporaryDir;

    private DetailDesignDrawingRepository detailDesignDrawingRepository;

    private DetailDesignDrawingDetailRepository detailDesignDrawingDetailRepository;

    private final HierarchyRepository hierarchyRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private final HierarchyNodeRelationInterface hierarchyNodeRelationService;


    private static String SECTOR = "SECTOR";
    private static String FUNCTION = "FUNCTION";
    private static String TYPE = "TYPE";
    private static String SUB_TYPE = "SUB_TYPE";
    private static String CHAIN = "CHAIN";
    private static String FUNC_PART = "FUNC_PART";
    private static Set<String> columnKeys = new HashSet<String>(){{
        add(SECTOR);
        add(FUNCTION);
        add(TYPE);
        add(SUB_TYPE);
        add(CHAIN);
        add(FUNC_PART);
    }};
    private static Map<String, Integer> columnMap = new HashMap<>();

    /**
     * 构造方法。
     */
    @Autowired
    @Lazy
    public DetailDesignDrawingService(
        StringRedisTemplate stringRedisTemplate,
        DetailDesignDrawingRepository detailDesignDrawingRepository,
        DetailDesignDrawingDetailRepository detailDesignDrawingDetailRepository,
        HierarchyRepository hierarchyRepository, ProjectNodeRepository projectNodeRepository,
        HierarchyNodeRelationInterface hierarchyNodeRelationService) {
        super(stringRedisTemplate);
        this.detailDesignDrawingRepository = detailDesignDrawingRepository;
        this.detailDesignDrawingDetailRepository = detailDesignDrawingDetailRepository;
        this.hierarchyRepository = hierarchyRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.hierarchyNodeRelationService = hierarchyNodeRelationService;
    }

    /**
     * 获取合并单元格的值所在的坐标。
     *
     * @param sheet  表格
     * @param row    行
     * @param column 列
     * @return
     */
    private Coordinates getMergedRegionValue(Sheet sheet, Row row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row.getRowNum() >= firstRow && row.getRowNum() <= lastRow) {

                if (column >= firstColumn && column <= lastColumn) {
/*                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);    */
                    return new Coordinates(firstRow, firstColumn);
                }
            }
        }
        return null;
    }

    /**
     * 判断是否是单元格。
     *
     * @param sheet
     * @param row
     * @param colIndex
     * @return
     */
    private boolean isMergedRegion(Sheet sheet, Row row, int colIndex) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row.getRowNum() >= firstRow && row.getRowNum() <= lastRow) {
                if (colIndex >= firstColumn && colIndex <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
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
     * 根据批处理id更新导入文件id。
     *
     * @param batchTaskId 批处理任务id
     * @param fileId      文件id
     * @return
     */
    @Transactional
    @Override
    public boolean updateFileId(Long batchTaskId, Long fileId) {
        List<DetailDesignDrawing> list = detailDesignDrawingRepository.findByBatchTaskId(batchTaskId);
        detailDesignDrawingRepository.updateFileIdIn(fileId, list);
        return true;
    }

    /**
     * 导入详细设计图纸。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param operator  操作者
     * @param batchTask 批处理任务
     * @param importDTO 导入DTO
     * @return
     */
    @Override
    public BatchResultDTO importDetailDesign(Long orgId, Long projectId, OperatorDTO operator, BatchTask batchTask,
                                             DrawingImportDTO importDTO) {
        Workbook workbook;
        File excel;


        try {
            excel = new File(temporaryDir, importDTO.getFileName());
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            throw new NotFoundError();
        }


        BatchResultDTO batchResult = new BatchResultDTO(batchTask);

        BatchResultDTO sheetImportResult;

        int sheetNum = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetNum; i++) {

            if (workbook.getSheetAt(i).getSheetName().equalsIgnoreCase("SETTINGS")) {
                Sheet setting = workbook.getSheetAt(i);
                int rowCount = setting.getPhysicalNumberOfRows();

                for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {

                    Row row = setting.getRow(rowIndex);

                    String idxKey = StringUtils.trim(row.getCell(0).getStringCellValue());
                    Integer idxVal = Integer.parseInt(StringUtils.trim(row.getCell(0).getStringCellValue()));
                    if(!StringUtils.isEmpty(idxKey) && idxKey != null) {
                        columnMap.put(idxKey, idxVal);

                    }

                }
                if(!columnMap.keySet().containsAll(columnKeys)) {
                    throw new BusinessError("NO Proper setting");
                }

            } else if (!workbook.getSheetAt(i).getSheetName().equals(BpmCode.EMDR)) {
                continue;
            } else {

                sheetImportResult = importDetailDesign(
                    orgId,
                    projectId,
                    operator,
                    workbook.getSheetAt(i),
                    batchResult
                );


                batchResult.addLog(
                    sheetImportResult.getProcessedCount()
                        + " "
                        + workbook.getSheetAt(i).getSheetName()
                        + " imported."
                );
            }
        }



        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

        return batchResult;
    }

    /**
     * 导入详细设计图纸私有方法。
     *
     * @param orgId       组织id
     * @param projectId   项目id
     * @param operator    操作者
     * @param sheet       表格
     * @param batchResult 批处理结果
     * @return
     */
    private BatchResultDTO importDetailDesign(
        Long orgId,
        Long projectId,
        OperatorDTO operator,
        Sheet sheet,
        BatchResultDTO batchResult
    ) {
        final String progressKey = "BATCH_TASK:" + batchResult.getTaskId() + ":PROGRESS";
        Iterator<Row> rows = sheet.rowIterator();
        Row row;
        int totalCount = 0;
        int skippedCount = 0;
        int processedCount = 0;
        int errorCount = 0;
        Date date = new Date();
        Map<String, String> parentNodeNoMap = new HashMap<>();


        Map<Long, Map<String, String>> detailMap = new HashMap<>();
        DateFormat sdf = new SimpleDateFormat("dd-MMM-yy", java.util.Locale.US);

        while (rows.hasNext()) {

            row = rows.next();
            int colIndex = 0;

            if (row.getRowNum() < BpmCode.DD_HEADER) {
                continue;
            }
            try {

                batchResult.addTotalCount(1);
                totalCount++;

                String parentNode = row.getCell(columnMap.get(SECTOR)).getStringCellValue().trim() +"_"+ row.getCell(columnMap.get(FUNCTION)).getStringCellValue().trim() + "_" +
                    row.getCell(columnMap.get(TYPE)).getStringCellValue().trim() + "_" + row.getCell(columnMap.get(SUB_TYPE)).getStringCellValue().trim();
                parentNodeNoMap.put("ENGINEERING_COMMON", parentNode);
                if (MapUtils.isEmpty(parentNodeNoMap)) {
                    skippedCount++;
                    batchResult.addSkippedCount(1);
                    throw new ValidationError("parent node is empty");

                }

                DetailDesignDrawing list = new DetailDesignDrawing();
                list.setBatchTaskId(batchResult.getTaskId());

                list.setCompany(WorkbookUtils.readAsString(row, colIndex++));
                list.setEngineringCategory(WorkbookUtils.readAsString(row, colIndex++));
//                list.setDisciplineCode(WorkbookUtils.readAsString(row, colIndex++));
                list.setDocumentType(WorkbookUtils.readAsString(row, colIndex++));
                list.setDocumentNumber(WorkbookUtils.readAsString(row, colIndex++));

                String designChangeNumber = WorkbookUtils.readAsString(row, colIndex++);
                String readDate = WorkbookUtils.readAsString(row, colIndex++);
                String oldDocumentNumber = WorkbookUtils.readAsString(row, colIndex++);

                String documentTitle = WorkbookUtils.readAsString(row, colIndex++);
                String activeRevision = WorkbookUtils.readAsString(row, colIndex++);

                String revDateStr = null;
                list.setEntityType(row.getCell(columnMap.get(TYPE)).getStringCellValue().trim());
                list.setSubType(row.getCell(columnMap.get(SUB_TYPE)).getStringCellValue().trim());
//                list.setFunction(row.getCell(columnMap.get(FUNCTION)).getStringCellValue().trim());
                list.setSector(row.getCell(columnMap.get(SECTOR)).getStringCellValue().trim());
//                list.setFunction(row.getCell(columnMap.get(FUNC_PART)).getStringCellValue().trim());
                try {
                    Date revDate = WorkbookUtils.readAsDate(row, colIndex++);
                    revDateStr = sdf.format(revDate);
                } catch (Exception e) {
                    revDateStr = WorkbookUtils.readAsString(row, colIndex - 1);
                }

                String documentStatus = WorkbookUtils.readAsString(row, colIndex++);

                String planRequiredTimeStr = null;
                try {
                    Date planRequiredTime = WorkbookUtils.readAsDate(row, colIndex++);
                    planRequiredTimeStr = sdf.format(planRequiredTime);
                } catch (Exception e) {
                    planRequiredTimeStr = WorkbookUtils.readAsString(row, colIndex - 1);
                }

                String actualDrawingTimeStr = null;
                try {
                    Date actualDrawingTime = WorkbookUtils.readAsDate(row, colIndex++);
                    actualDrawingTimeStr = sdf.format(actualDrawingTime);
                } catch (Exception e) {
                    actualDrawingTimeStr = WorkbookUtils.readAsString(row, colIndex - 1);
                }


                if (!BpmCode.CURRENT.equals(documentStatus)) {
                    list.setDocumentTitle(null);
                    list.setActiveRevision(null);
                    list.setPlanRequiredTime(null);
                    list.setActualDrawingTime(null);
                } else {
                    list.setDocumentTitle(documentTitle);
                    list.setActiveRevision(activeRevision);
                    list.setPlanRequiredTime(planRequiredTimeStr);
                    list.setActualDrawingTime(actualDrawingTimeStr);
                }


                Optional<DetailDesignDrawing> op = detailDesignDrawingRepository.findByOrgIdAndProjectIdAndDocumentNumberAndStatus(orgId, projectId, list.getDocumentNumber(), ACTIVE);
                if (op.isPresent()) {
                    if (!BpmCode.CURRENT.equals(documentStatus)) {
                        list = op.get();
                    } else {
                        DetailDesignDrawing oldList = op.get();
                        list.setId(oldList.getId());
                        list.setOrgId(oldList.getOrgId());
                        list.setProjectId(oldList.getProjectId());
                        list.setCreatedAt(oldList.getCreatedAt());
                        list.setStatus(oldList.getStatus());
                        DetailDesignDrawing newList = BeanUtils.copyProperties(list, oldList);
                        newList.setLastModifiedAt();

                        list = detailDesignDrawingRepository.save(newList);
                    }
                } else {
                    list.setOrgId(orgId);
                    list.setProjectId(projectId);
                    list.setLastModifiedAt(date);
                    list.setCreatedAt(date);
                    list.setStatus(ACTIVE);
                    list = detailDesignDrawingRepository.save(list);
                }



                updateHierarchyNodesAndProjectNodes(list,
                    parentNodeNoMap,
                    projectId,
                    orgId,
                    operator.getId(),
                    batchResult);


                if (!detailMap.containsKey(list.getId())) {

                    List<DetailDesignDrawingDetail> detailList = detailDesignDrawingDetailRepository.findByDetailDesignListId(list.getId());
                    Map<String, String> m = new HashMap<>();
                    for (DetailDesignDrawingDetail d : detailList) {
                        m.put(d.getActiveRevision(), d.getStatus().toString());
                    }
                    detailMap.put(list.getId(), m);
                }


                DetailDesignDrawingDetail detail = new DetailDesignDrawingDetail();
                detail.setActiveRevision(activeRevision);
                detail.setActualDrawingTime(actualDrawingTimeStr);
                detail.setDate(readDate);
                detail.setDesignChangeNumber(designChangeNumber);
                detail.setDocumentStatus(documentStatus);
                detail.setDocumentTitle(documentTitle);
                detail.setOldDocumentNumber(oldDocumentNumber);
                detail.setPlanRequiredTime(planRequiredTimeStr);
                detail.setRevDate(revDateStr);

                Map<String, String> varsionStatus = detailMap.get(list.getId());


                if (varsionStatus.containsKey(activeRevision)) {

                    Optional<DetailDesignDrawingDetail> opDetail = detailDesignDrawingDetailRepository.
                        findByDetailDesignListIdAndActiveRevision(list.getId(), activeRevision);
                    DetailDesignDrawingDetail oldDetail = opDetail.get();
                    detail.setId(oldDetail.getId());
                    detail.setCreatedAt(oldDetail.getCreatedAt());
                    detail.setStatus(oldDetail.getStatus());
                    detail = BeanUtils.copyProperties(detail, oldDetail);
                    detail.setDetailDesignListId(list.getId());
                    detail.setLastModifiedAt();

                    if (varsionStatus.get(activeRevision).equals(EntityStatus.DELETED.toString())) {
                        detail.setStatus(ACTIVE);
                    }
                } else {

                    detail.setCreatedAt();
                    detail.setStatus(ACTIVE);
                    detail.setDetailDesignListId(list.getId());
                }
                detailDesignDrawingDetailRepository.save(detail);


                processedCount++;
                batchResult.addProcessedCount(1);


                setRedisKey(progressKey, "" + batchResult.getProgress(), 3000);

            } catch (Exception e) {
                e.printStackTrace(System.out);
                errorCount++;
                batchResult.addErrorCount(1);
                setImportDataErrorMessage(row, "" + colIndex + "th import error.");
            }

        }

        return new BatchResultDTO(
            totalCount,
            processedCount,
            skippedCount,
            errorCount
        );
    }

    private void updateHierarchyNodesAndProjectNodes(
        DetailDesignDrawing entity,
        Map<String, String> parentNodeNoMap,
        Long projectId,
        Long orgId,
        Long operatorId,
        BatchResultDTO batchResultDTO) {

        try {
            HierarchyNode lastSiblingNode;
            int sortWeight;
            HierarchyNode node;
            int i = -1;

            for (Map.Entry<String, String> parentNodeNo : parentNodeNoMap.entrySet()) {
                i++;
                String hierarchyType = parentNodeNo.getKey();
                String parentNoStr = parentNodeNo.getValue();


                HierarchyNode parentNode = null;
                parentNode = hierarchyRepository
                    .findByNoAndProjectIdAndHierarchyTypeAndDeletedIsFalse(
                        parentNoStr,
                        projectId,
                        hierarchyType
                    );


                if (parentNode == null) {

                    throw new ValidationError("parent '" + parentNodeNo + "' doesn't exit");
                }


                lastSiblingNode = hierarchyRepository
                    .findFirstByProjectIdAndParentIdAndDeletedIsFalseOrderBySortDesc(
                        projectId,
                        parentNode.getId()
                    )
                    .orElse(null);


                sortWeight = lastSiblingNode != null
                    ? lastSiblingNode.getSort()
                    : parentNode.getSort();


                node = getEntityNode(
                    projectId,
                    parentNode.getId(),
                    entity.getNo(),
                    parentNodeNo.getKey()
                );


                if (node == null) {
                    node = new HierarchyNode(parentNode);
                    node.setEntityType(entity.getEntityType());
                    node.setNode(
                        projectNodeRepository
                            .findByProjectIdAndNoAndDeletedIsFalse(
                                projectId,
                                entity.getNo()
                            )
                            .orElse(null)
                    );
                    node.setCreatedAt();
                    node.setCreatedBy(operatorId);
                    node.setNew(true);
                }


                node.setParentNode(parentNode);
                node.setNo(entity.getNo(), parentNode.getNo());

                node.setEntityType(entity.getEntityType());


                node.getNode().setDisplayName(entity.getDocumentNumber());
                node.setEntitySubType(entity.getEntitySubType());

                node.getNode().setEntityBusinessType(entity.getEntityBusinessType());
//                node.getNode().setWorkLoad(entity.getNps());
//                node.getNode().setDwgShtNo(entity.getSheetNo());


                node.setEntityId(entity.getId());
                node.setSort(sortWeight + 1);
                node.setLastModifiedAt();
                node.setLastModifiedBy(operatorId);
                node.setStatus(ACTIVE);

                node.setHierarchyType(hierarchyType);

                node.getNode().setDiscipline("PIPING");
                projectNodeRepository.save(node.getNode());
                hierarchyRepository.save(node);

                hierarchyNodeRelationService.saveHierarchyPath(orgId, projectId, node);

            }


        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw e;
        }
    }

    /**
     * 取得实体节点信息。
     *
     * @param projectId     项目 ID
     * @param parentId      上级节点 ID
     * @param nodeNo        节点编号
     * @param hierarchyType
     * @return 实体节点信息
     */
    private HierarchyNode getEntityNode(
        Long projectId,
        Long parentId,
        String nodeNo,
        String hierarchyType
    ) {

        HierarchyNode node = hierarchyRepository
            .findByNoAndProjectIdAndParentIdAndDeletedIsFalse(
                nodeNo,
                projectId,
                parentId,
                hierarchyType
            )
            .orElse(null);

        if (node == null) {
            return null;
        }

        return node;
    }

    /**
     * 查找详细图纸列表。
     *
     * @param orgId       组织id
     * @param projectId   项目id
     * @param page        分页参数
     * @param criteriaDTO 查询参数
     * @return
     */
    @Override
    public Page<DetailDesignDrawing> searchDetailDesignList(Long orgId, Long projectId, PageDTO page,
                                                            DetailDesignDrawingCriteriaDTO criteriaDTO) {
        Page<DetailDesignDrawing> result = detailDesignDrawingRepository.search(orgId, projectId, page.toPageable(), criteriaDTO);
        /*for(DetailDesignDrawing l:result.getContent()) {
            String docNo = l.getDocumentNumber();
            drawingListRepository.findByOrgIdAndProjectIdAndDwgNoAndStatus(orgId, projectId, docNo);
        }*/
        return result;
    }

    /**
     * 查询详细设计图纸详细。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param id        详细设计图纸id
     * @return
     */
    @Override
    public List<DetailDesignDrawingDetail> searchDetailDesignListDetail(Long orgId, Long projectId, Long id) {
        return detailDesignDrawingRepository.searchDetail(orgId, projectId, id);
    }
}
