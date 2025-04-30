package com.ose.tasks.domain.model.service.material;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.material.api.MmMaterialInStockFeignAPI;
import com.ose.material.api.MmSurplusMaterialFeignAPI;
import com.ose.material.dto.MmMaterialInStockSearchDTO;
import com.ose.material.dto.MmSurplusMaterialCreateDTO;
import com.ose.material.dto.MmSurplusMaterialSearchDTO;
import com.ose.material.entity.MmMaterialInStockDetailQrCodeEntity;
import com.ose.material.entity.MmSurplusMaterialEntity;
import com.ose.tasks.domain.model.repository.BatchTaskRepository;
import com.ose.tasks.domain.model.repository.ProjectRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.material.*;
import com.ose.tasks.domain.model.repository.wbs.structure.Wp05EntityRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.bpm.ActivityInstanceDTO;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import com.ose.tasks.dto.material.*;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.material.*;
import com.ose.tasks.entity.wbs.structureEntity.Wp05Entity;
import com.ose.tasks.vo.material.MaterialPrefixType;
import com.ose.tasks.vo.setting.BatchTaskStatus;
import com.ose.util.*;
import com.ose.vo.EntityStatus;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class FMaterialStructureNestService implements FMaterialStructureNestInterface {
    private final static Logger logger = LoggerFactory.getLogger(FMaterialStructureNestService.class);

    @Value("${application.files.temporary}")
    private String temporaryDir;

    private static final String STYLES_SHEET_NAME = "styles";
    private static final String STR_NESTING_PROGRAM = "STR_NESTING_PROGRAM";
    private static final String STR_NESTING_DRAWING = "STR_NESTING_DRAWING";

    private final FMaterialStructureNestRepository fMaterialStructureNestRepository;
    private final ProjectRepository projectRepository;
    private final FMaterialStructureNestDrawingRepository fMaterialStructureNestDrawingRepository;
    private final FMaterialStructureNestProgramRepository fMaterialStructureNestProgramRepository;
    private final MmMaterialInStockFeignAPI mmMaterialInStockFeignAPI;
    private final MmSurplusMaterialFeignAPI mmSurplusMaterialFeignAPI;
    private final Wp05EntityRepository wp05EntityRepository;
    private BpmProcessStageRepository bpmProcessStageRepository;
    private BpmProcessRepository bpmProcessRepository;
    private BpmEntitySubTypeRepository bpmEntityCategoryRepository;
    private BpmEntityTypeRepository bpmEntityCategoryTypeRepository;
    private final TodoTaskDispatchInterface todoTaskDispatchService;
    private final ProjectInterface projectInterface;
    private final StructureEntityQrCodeRepository structureEntityQrCodeRepository;
    private final BatchTaskRepository batchTaskRepository;

    /**
     * 构造方法
     */
    @Autowired
    public FMaterialStructureNestService(
        FMaterialStructureNestRepository fMaterialStructureNestRepository,
        ProjectRepository projectRepository,
        FMaterialStructureNestDrawingRepository fMaterialStructureNestDrawingRepository,
        FMaterialStructureNestProgramRepository fMaterialStructureNestProgramRepository,
        Wp05EntityRepository wp05EntityRepository,
        BpmProcessStageRepository bpmProcessStageRepository,
        BpmProcessRepository bpmProcessRepository,
        BpmEntitySubTypeRepository bpmEntityCategoryRepository,
        BpmEntityTypeRepository bpmEntityCategoryTypeRepository,
        TodoTaskDispatchInterface todoTaskDispatchService,
        ProjectInterface projectInterface,
        MmMaterialInStockFeignAPI mmMaterialInStockFeignAPI,
        MmSurplusMaterialFeignAPI mmSurplusMaterialFeignAPI,
        StructureEntityQrCodeRepository structureEntityQrCodeRepository,
        BatchTaskRepository batchTaskRepository
    ) {
        this.fMaterialStructureNestRepository = fMaterialStructureNestRepository;
        this.projectRepository = projectRepository;
        this.fMaterialStructureNestDrawingRepository = fMaterialStructureNestDrawingRepository;
        this.fMaterialStructureNestProgramRepository = fMaterialStructureNestProgramRepository;
        this.batchTaskRepository = batchTaskRepository;
        this.wp05EntityRepository = wp05EntityRepository;
        this.bpmProcessStageRepository = bpmProcessStageRepository;
        this.bpmProcessRepository = bpmProcessRepository;
        this.bpmEntityCategoryRepository = bpmEntityCategoryRepository;
        this.bpmEntityCategoryTypeRepository = bpmEntityCategoryTypeRepository;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.projectInterface = projectInterface;
        this.mmMaterialInStockFeignAPI = mmMaterialInStockFeignAPI;
        this.mmSurplusMaterialFeignAPI = mmSurplusMaterialFeignAPI;
        this.structureEntityQrCodeRepository = structureEntityQrCodeRepository;
    }


    /**
     * 创建结构套料方案。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param fMaterialStructureNestDTO 创建套料结构对象
     * @param context                   创建者
     */
    @Override
    public void create(
        Long orgId,
        Long projectId,
        FMaterialStructureNestDTO fMaterialStructureNestDTO,
        ContextDTO context,
        OperatorDTO operatorDTO
    ) {


        if (fMaterialStructureNestDTO.getName() == null) {
            throw new BusinessError("Structure Nest name is required");
        }

        if (fMaterialStructureNestDTO.getType() == null) {
            throw new BusinessError("Structure Nest type is required");
        }


        FMaterialStructureNest fMaterialStructureNestFind = fMaterialStructureNestRepository.findByOrgIdAndProjectIdAndNameAndDeletedIsFalse(orgId, projectId, fMaterialStructureNestDTO.getName());

        if (fMaterialStructureNestFind != null) {
            throw new BusinessError("Structure Nest name is existed");
        }

        FMaterialStructureNest fMaterialStructureNest = new FMaterialStructureNest();

        BeanUtils.copyProperties(fMaterialStructureNestDTO, fMaterialStructureNest);

        Date now = new Date();

        SeqNumberDTO seqNumberDTO = fMaterialStructureNestRepository.getMaxSeqNumber(orgId, projectId).orElse(new SeqNumberDTO(0));
        int seqNumber = seqNumberDTO.getNewSeqNumber();
        fMaterialStructureNest.setNo(String.format("%s-%s-%04d", getProjectName(orgId, projectId), MaterialPrefixType.MATERIAL_NESTING_STRUCTURE.getCode(), seqNumber));
        fMaterialStructureNest.setSeqNumber(seqNumber);
        fMaterialStructureNest.setOrgId(orgId);
        fMaterialStructureNest.setProjectId(projectId);
        fMaterialStructureNest.setStatus(EntityStatus.ACTIVE);
        fMaterialStructureNest.setCreatedAt(now);
        fMaterialStructureNest.setCreatedBy(operatorDTO.getId());
        fMaterialStructureNest.setLastModifiedAt(now);
        fMaterialStructureNest.setLastModifiedBy(operatorDTO.getId());
        fMaterialStructureNest.setModifyState(true);
        fMaterialStructureNest.setActivityStartStatus(true);
        fMaterialStructureNest.setSaveActivityStatus(false);
        fMaterialStructureNestRepository.save(fMaterialStructureNest);

    }

    /**
     * 查询结构套料方案列表。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param pageDTO   分页参数
     * @return
     */
    @Override
    public Page<FMaterialStructureNest> search(
        Long orgId,
        Long projectId,
        PageDTO pageDTO
    ) {
        return fMaterialStructureNestRepository.list(orgId, projectId, pageDTO.toPageable());
    }

    /**
     * 查询结构方案详情。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料清单id
     */
    @Override
    public FMaterialStructureNest detail(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId
    ) {
        FMaterialStructureNest fMaterialStructureNest = fMaterialStructureNestRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, fMaterialStructureNestId);
        if (fMaterialStructureNest == null) {
            throw new BusinessError("StructureNest not found");
        }
        return fMaterialStructureNest;
    }

    /**
     * 更新结构套料方案。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param fMaterialStructureNestId  结构套料清单id
     * @param fMaterialStructureNestDTO 更新内容
     * @param contextDTO                操作者
     */
    @Override
    public void update(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        FMaterialStructureNestDTO fMaterialStructureNestDTO,
        ContextDTO contextDTO,
        OperatorDTO operatorDTO
    ) {

        String no;
        Integer seqNumber;

        if (fMaterialStructureNestDTO.getName() == null) {
            throw new BusinessError("Structure Nest and name is required");
        }

        if (fMaterialStructureNestDTO.getType() == null) {
            throw new BusinessError("Structure Nest type is required");
        }


        FMaterialStructureNest fMaterialStructureNest = fMaterialStructureNestRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, fMaterialStructureNestId);
        if (fMaterialStructureNest == null) {
            throw new BusinessError("StructureNest not found");
        } else {
            seqNumber = fMaterialStructureNest.getSeqNumber();
            no = fMaterialStructureNest.getNo();
        }

        if (fMaterialStructureNest.getModifyState() != null && !fMaterialStructureNest.getModifyState()) {
            throw new BusinessError("StructureNest Activity is exited");
        }

        FMaterialStructureNest fMaterialStructureNestFind = fMaterialStructureNestRepository.findByOrgIdAndProjectIdAndNameAndDeletedIsFalse(orgId, projectId, fMaterialStructureNestDTO.getName());


        if (fMaterialStructureNestFind != null && (!fMaterialStructureNestDTO.getName().equals(fMaterialStructureNest.getName()))) {
            throw new BusinessError("StructureNest is existed");
        }

        BeanUtils.copyProperties(fMaterialStructureNestDTO, fMaterialStructureNest);

        Date now = new Date();
        fMaterialStructureNest.setCreatedBy(operatorDTO.getId());
        fMaterialStructureNest.setSeqNumber(seqNumber);
        fMaterialStructureNest.setNo(no);
        fMaterialStructureNest.setCreatedAt(now);
        fMaterialStructureNest.setLastModifiedBy(operatorDTO.getId());
        fMaterialStructureNest.setLastModifiedAt(now);
        fMaterialStructureNest.setStatus(EntityStatus.ACTIVE);
        fMaterialStructureNest.setModifyState(true);
        fMaterialStructureNest.setActivityStartStatus(true);
        fMaterialStructureNest.setSaveActivityStatus(false);
        fMaterialStructureNestRepository.save(fMaterialStructureNest);

    }

    /**
     * 删除结构套料方案。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料清单id
     */
    @Override
    public void delete(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        OperatorDTO operatorDTO
    ) {

        FMaterialStructureNest fMaterialStructureNest = fMaterialStructureNestRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, fMaterialStructureNestId);
        if (fMaterialStructureNest == null) {
            throw new BusinessError("StructureNest not found");
        }

        if (fMaterialStructureNest.getModifyState() != null && !fMaterialStructureNest.getModifyState()) {
            throw new BusinessError("StructureNest Activity is exited");
        }

        Date now = new Date();
        fMaterialStructureNest.setStatus(EntityStatus.DELETED);
        fMaterialStructureNest.setLastModifiedBy(operatorDTO.getId());
        fMaterialStructureNest.setLastModifiedAt(now);
        fMaterialStructureNest.setDeletedBy(operatorDTO.getId());
        fMaterialStructureNest.setDeletedAt(now);
        fMaterialStructureNest.setDeleted(true);

        fMaterialStructureNestRepository.save(fMaterialStructureNest);

    }

    /**
     * 取得项目名称
     *
     * @param orgId
     * @param projectId
     * @return
     */
    private String getProjectName(Long orgId, Long projectId) {

        return projectInterface.get(orgId, projectId).getName();
    }


    /**
     * 导入结构套料。
     *
     * @param orgId                           组织id
     * @param projectId                       项目id
     * @param batchTask                       批处理任务信息
     * @param operator                        操作者信息
     * @param project                         项目信息
     * @param fMaterialStructureNestImportDTO 节点导入操作数据传输对象
     * @return 批处理执行结果
     */
    @Override
    public BatchResultDTO importStructureNest(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        BatchTask batchTask,
        OperatorDTO operator,
        Project project,
        FMaterialStructureNestImportDTO fMaterialStructureNestImportDTO
    ) {
        File excel;
        Workbook workbook;
        Sheet sheet1;
        Sheet sheet2;
        Sheet styles;
        final Date timestamp = new Date();
        Long operatorId = operator.getId();
        batchTask.setEntityId(fMaterialStructureNestId);
        batchTask.setStatus(BatchTaskStatus.RUNNING);
        batchTaskRepository.save(batchTask);
        BatchResultDTO batchResult = new BatchResultDTO(batchTask);
        FMaterialStructureNestImportSheetReturnDTO fMaterialStructureNestImportSheetReturnDTO1 = new FMaterialStructureNestImportSheetReturnDTO();
        FMaterialStructureNestImportSheetReturnDTO fMaterialStructureNestImportSheetReturnDTO2 = new FMaterialStructureNestImportSheetReturnDTO();
        try {
            excel = new File(temporaryDir, fMaterialStructureNestImportDTO.getFileName());
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            throw new BusinessError(e.getMessage());
        }
        if (workbook == null
            || (sheet1 = workbook.getSheet(STR_NESTING_PROGRAM)) == null
            || (sheet2 = workbook.getSheet(STR_NESTING_DRAWING)) == null
            || (styles = workbook.getSheet(STYLES_SHEET_NAME)) == null) {
            return null;
        }
        if (sheet2 != null) {
            fMaterialStructureNestImportSheetReturnDTO2 = importDrawing(
                orgId,
                projectId,
                fMaterialStructureNestId,
                sheet2,
                styles,
                operator,
                batchTask,
                batchResult
            );
        }
        if (sheet1 != null) {
            fMaterialStructureNestImportSheetReturnDTO1 = importProgram(
                orgId,
                projectId,
                fMaterialStructureNestId,
                sheet1,
                styles,
                operator,
                fMaterialStructureNestImportSheetReturnDTO2,
                batchTask,
                batchResult
            );
        }
        if (fMaterialStructureNestImportSheetReturnDTO1.getfMaterialStructureNestProgramFailCount() == 0 && fMaterialStructureNestImportSheetReturnDTO2.getfMaterialStructureNestDrawingFailCount() == 0) {
            fMaterialStructureNestProgramRepository.saveAll(fMaterialStructureNestImportSheetReturnDTO1.getfMaterialStructureNestPrograms());
            fMaterialStructureNestDrawingRepository.saveAll(fMaterialStructureNestImportSheetReturnDTO2.getfMaterialStructureNestDrawings());
            batchTask.setStatus(BatchTaskStatus.FINISHED);
            batchTask.setLastModifiedAt(new Date());
            batchTaskRepository.save(batchTask);
        } else {
            batchTask.setStatus(BatchTaskStatus.FAILED);
            batchTask.setLastModifiedAt(new Date());
            batchTaskRepository.save(batchTask);
        }

        batchResult.addLog(
            batchResult.getProcessedCount()
                + " "
                + workbook.getSheetAt(0).getSheetName()
                + " imported."
        );

        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }

        project.setLastModifiedAt(timestamp);
        project.setLastModifiedBy(operatorId);
        projectRepository.save(project);
        return batchResult;
    }

    /**
     * 导入结构套料排版。
     *
     * @param sheet1
     * @param styles
     */
    private FMaterialStructureNestImportSheetReturnDTO importProgram(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        Sheet sheet1,
        Sheet styles,
        OperatorDTO operator,
        FMaterialStructureNestImportSheetReturnDTO fMaterialStructureNestImportSheetReturnDTO2,
        BatchTask batchTask,
        BatchResultDTO batchResult
    ) {
        Row row;
        int failedCount = 0;
        List<FMaterialStructureNestProgram> fMaterialStructureNestPrograms = new ArrayList<>();
        FMaterialStructureNestImportSheetReturnDTO fMaterialStructureNestImportSheetReturnDTO = new FMaterialStructureNestImportSheetReturnDTO();
        CellStyle errorStyle = styles.getRow(0).getCell(0).getCellStyle();
        int errorColNo = 9;

        // 清理原有数据
        List<FMaterialStructureNestProgram> structureNestProgramDeleteList = fMaterialStructureNestProgramRepository.findByOrgIdAndProjectIdAndFMaterialStructureNestIdAndStatus(orgId, projectId, fMaterialStructureNestId, EntityStatus.ACTIVE);
        if (!structureNestProgramDeleteList.isEmpty()) {
            fMaterialStructureNestProgramRepository.deleteAll(structureNestProgramDeleteList);
        }

        // 套料图号map判断是否存在
        Map<String, Integer> nestDrawingNoMap = new HashMap<>();
        // 材料二维码map判断是否存在
        Map<String, Integer> qrCodeMap = new HashMap<>();
        // 件号map判断是否存在
        Map<String, Integer> pieceTagNoMap = new HashMap<>();
        // 产生余料号map，判断是否唯一
        Map<String, Integer> remainderCreatedMap = new HashMap<>();

        // 获取当前套料单中零件表中存在的map关系
        if (!fMaterialStructureNestImportSheetReturnDTO2.getfMaterialStructureNestDrawings().isEmpty()) {
            for (FMaterialStructureNestDrawing fMaterialStructureNestDrawing : fMaterialStructureNestImportSheetReturnDTO2.getfMaterialStructureNestDrawings()) {
                if (!StringUtils.isEmpty(fMaterialStructureNestDrawing.getMaterialQrCode())) {
                    if (!qrCodeMap.containsKey(fMaterialStructureNestDrawing.getMaterialQrCode())) {
                        qrCodeMap.put(fMaterialStructureNestDrawing.getMaterialQrCode(), 1);
                    } else {
                        qrCodeMap.put(fMaterialStructureNestDrawing.getMaterialQrCode(), qrCodeMap.get(fMaterialStructureNestDrawing.getMaterialQrCode()) + 1);
                    }
                }
                if (!StringUtils.isEmpty(fMaterialStructureNestDrawing.getPieceTagNo())) {
                    if (!pieceTagNoMap.containsKey(fMaterialStructureNestDrawing.getPieceTagNo())) {
                        pieceTagNoMap.put(fMaterialStructureNestDrawing.getPieceTagNo(), 1);
                    } else {
                        pieceTagNoMap.put(fMaterialStructureNestDrawing.getPieceTagNo(), pieceTagNoMap.get(fMaterialStructureNestDrawing.getPieceTagNo()) + 1);
                    }
                }
                if (!StringUtils.isEmpty(fMaterialStructureNestDrawing.getNestingProgramNo())) {
                    if (!nestDrawingNoMap.containsKey(fMaterialStructureNestDrawing.getNestingProgramNo())) {
                        nestDrawingNoMap.put(fMaterialStructureNestDrawing.getNestingProgramNo(), 1);
                    } else {
                        nestDrawingNoMap.put(fMaterialStructureNestDrawing.getNestingProgramNo(), nestDrawingNoMap.get(fMaterialStructureNestDrawing.getNestingProgramNo()) + 1);
                    }
                }
            }
        }
        for (int i = 2; i < sheet1.getPhysicalNumberOfRows(); i++) {
            row = sheet1.getRow(i);
            List<String> errors = new ArrayList<>();
            FMaterialStructureNestProgram fMaterialStructureNestProgramItem = new FMaterialStructureNestProgram();
            String no = WorkbookUtils.readAsString(row, 1);
            String rev = WorkbookUtils.readAsString(row, 2);
            String pieceTagNo = WorkbookUtils.readAsString(row, 3);
            String qrCode = WorkbookUtils.readAsString(row, 4);
            String remainderCreated = WorkbookUtils.readAsString(row, 5);
            String remainderUsed = WorkbookUtils.readAsString(row, 6);

            if (StringUtils.isEmpty(no)) {
                failedCount = failedCount + 1;
                errors.add("Material Structure Nest no should not be null and empty");
                batchResult.addErrorCount(1);
            }
            if (!StringUtils.isEmpty(no)) {
                // 判断套料图号是否存在于零件表中
                if (!nestDrawingNoMap.containsKey(no)) {
                    failedCount = failedCount + 1;
                    errors.add("套料图号： " + no + "不存在于当前导入的零件表中。");
                    batchResult.addErrorCount(1);
                }
                fMaterialStructureNestProgramItem.setNo(no);
            }
            // 判断是否使用余料
            if (!StringUtils.isEmpty(remainderUsed)) {
                // 使用余料
                if (!StringUtils.isEmpty(pieceTagNo)) {
                    failedCount = failedCount + 1;
                    errors.add("使用余料，不能填写件号");
                    batchResult.addErrorCount(1);
                }
                if (!StringUtils.isEmpty(qrCode)) {
                    failedCount = failedCount + 1;
                    errors.add("使用余料，不能填材料二维码");
                    batchResult.addErrorCount(1);
                }
                // 判断余料是否合法
                MmSurplusMaterialSearchDTO mmSurplusMaterialSearchDTO = new MmSurplusMaterialSearchDTO();
                mmSurplusMaterialSearchDTO.setKeyword(remainderUsed);
                MmSurplusMaterialEntity mmSurplusMaterialEntity = mmSurplusMaterialFeignAPI.searchSurplusMaterial(
                    orgId,
                    projectId,
                    mmSurplusMaterialSearchDTO
                ).getData();

                if (null == mmSurplusMaterialEntity) {
                    failedCount = failedCount + 1;
                    errors.add("余料号: " + remainderUsed + " 不存在于当前项目");
                    batchResult.addErrorCount(1);
                }else if(null!=mmSurplusMaterialEntity.getIssuedFlag() && mmSurplusMaterialEntity.getIssuedFlag().equals(true)){
                    failedCount = failedCount + 1;
                    errors.add("余料号: " + remainderUsed + " 已经被使用");
                    batchResult.addErrorCount(1);
                }
                fMaterialStructureNestProgramItem.setRemainderUsed(remainderUsed);
            } else {
                // 材料二维码和件号只能有一个
                if (StringUtils.isEmpty(qrCode) && StringUtils.isEmpty(pieceTagNo)) {
                    errors.add("二维码和件号不能都为空");
                    failedCount = failedCount + 1;
                    batchResult.addErrorCount(1);
                }
                if (!StringUtils.isEmpty(qrCode) && !StringUtils.isEmpty(pieceTagNo)) {
                    errors.add("二维码和件号不能同时存在");
                    failedCount = failedCount + 1;
                    batchResult.addErrorCount(1);
                }

                // 材料表中查找二维码和件号是否有效
                if (!StringUtils.isEmpty(qrCode)) {
                    if (!qrCodeMap.containsKey(qrCode)) {
                        failedCount = failedCount + 1;
                        errors.add("材料二维码： " + qrCode + "不存在于当前导入的零件表中。");
                        batchResult.addErrorCount(1);
                    }
                    MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO = new MmMaterialInStockSearchDTO();
                    mmMaterialInStockSearchDTO.setQrCode(qrCode);
                    MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockFeignAPI.searchMaterialInformation(
                        orgId,
                        projectId,
                        mmMaterialInStockSearchDTO
                    ).getData();
                    if (null == mmMaterialInStockDetailQrCodeEntity) {
                        errors.add("二维码无效");
                        failedCount = failedCount + 1;
                        batchResult.addErrorCount(1);
                    } else {
                        fMaterialStructureNestProgramItem.setMaterialQrCode(qrCode);
                    }
                } else {
                    if (!pieceTagNoMap.containsKey(pieceTagNo)) {
                        failedCount = failedCount + 1;
                        errors.add("件号： " + pieceTagNo + "不存在于当前导入的零件表中。");
                        batchResult.addErrorCount(1);
                    }
                    MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO = new MmMaterialInStockSearchDTO();
                    mmMaterialInStockSearchDTO.setPieceTagNo(pieceTagNo);
                    MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockFeignAPI.searchMaterialInformation(
                        orgId,
                        projectId,
                        mmMaterialInStockSearchDTO
                    ).getData();
                    if (null == mmMaterialInStockDetailQrCodeEntity) {
                        errors.add("件号无效");
                        batchResult.addErrorCount(1);
                        failedCount = failedCount + 1;
                    } else {
                        fMaterialStructureNestProgramItem.setPieceTagNo(pieceTagNo);
                    }
                }
            }
            if (!StringUtils.isEmpty(rev)) {
                fMaterialStructureNestProgramItem.setRev(rev);
            } else {
                failedCount = failedCount + 1;
                errors.add("版本不能为空");
                batchResult.addErrorCount(1);
            }
            // 产生余料号
            if (!StringUtils.isEmpty(remainderCreated)) {
                //  产生余料号要唯一
                if (!remainderCreatedMap.containsKey(remainderCreated)) {
                    remainderCreatedMap.put(remainderCreated, 1);
                } else {
                    failedCount = failedCount + 1;
                    errors.add("产生的余料号: " + remainderCreated + " 在当前表中重复");
                    batchResult.addErrorCount(1);
                }
                // 产生余料号不应该存在于当前项目
                MmSurplusMaterialSearchDTO mmSurplusMaterialSearchDTO = new MmSurplusMaterialSearchDTO();
                mmSurplusMaterialSearchDTO.setKeyword(remainderCreated);
                MmSurplusMaterialEntity mmSurplusMaterialEntities = mmSurplusMaterialFeignAPI.searchSurplusMaterial(
                    orgId,
                    projectId,
                    mmSurplusMaterialSearchDTO
                ).getData();
                if (null != mmSurplusMaterialEntities) {
                    failedCount = failedCount + 1;
                    errors.add("产生的余料号: " + remainderCreated + " 不应该存在于当前项目");
                    batchResult.addErrorCount(1);
                }

                fMaterialStructureNestProgramItem.setRemainderCreated(remainderCreated);
            }

            fMaterialStructureNestProgramItem.setOrgId(orgId);
            fMaterialStructureNestProgramItem.setProjectId(projectId);
            fMaterialStructureNestProgramItem.setfMaterialStructureNestId(fMaterialStructureNestId);
            fMaterialStructureNestProgramItem.setCreatedBy(operator.getId());
            fMaterialStructureNestProgramItem.setCreatedAt(new Date());
            fMaterialStructureNestProgramItem.setLastModifiedBy(operator.getId());
            fMaterialStructureNestProgramItem.setLastModifiedAt(new Date());
            fMaterialStructureNestProgramItem.setStatus(EntityStatus.ACTIVE);
            if (errors.isEmpty()) {
                fMaterialStructureNestPrograms.add(fMaterialStructureNestProgramItem);
                batchResult.addProcessedRelationCount(1);
            }
            if (!errors.isEmpty()) {
                WorkbookUtils
                    .setCellValue(sheet1, i, errorColNo, String.join(";", errors))
                    .setCellStyle(errorStyle);
                failedCount++;
            }
            batchTask.setResult(batchResult);
            batchTask.setLastModifiedAt();
            batchTaskRepository.save(batchTask);
        }
        fMaterialStructureNestImportSheetReturnDTO.setfMaterialStructureNestProgramFailCount(failedCount);
        fMaterialStructureNestImportSheetReturnDTO.setSheetItem(sheet1);
        fMaterialStructureNestImportSheetReturnDTO.setfMaterialStructureNestPrograms(fMaterialStructureNestPrograms);
        return fMaterialStructureNestImportSheetReturnDTO;
    }

    /**
     * 导入结构套料零件。
     *
     * @param sheet2
     * @param styles
     */
    private FMaterialStructureNestImportSheetReturnDTO importDrawing(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        Sheet sheet2,
        Sheet styles,
        OperatorDTO operator,
        BatchTask batchTask,
        BatchResultDTO batchResult
    ) {
        Row row;
        int failedCount = 0;
        List<FMaterialStructureNestDrawing> fMaterialStructureNestDrawings = new ArrayList<>();
        FMaterialStructureNestImportSheetReturnDTO fMaterialStructureNestImportSheetReturnDTO = new FMaterialStructureNestImportSheetReturnDTO();
        CellStyle errorStyle = styles.getRow(0).getCell(0).getCellStyle();
        int errorColNo = 9;
        Map<String, Integer> wp05MapList = new HashMap<>();
        List<FMaterialStructureNestDrawing> structureNestDrawingDeleteList = fMaterialStructureNestDrawingRepository.findByOrgIdAndProjectIdAndFMaterialStructureNestIdAndStatus(orgId, projectId, fMaterialStructureNestId, EntityStatus.ACTIVE);
        if (!structureNestDrawingDeleteList.isEmpty()) {
            fMaterialStructureNestDrawingRepository.deleteAll(structureNestDrawingDeleteList);
        }

        // 或取WP05编号mapping list
        for (int j = 2; j < sheet2.getPhysicalNumberOfRows(); j++) {
            Row row1 = sheet2.getRow(j);
            String wp05No = StringUtils.trim(WorkbookUtils.readAsString(row1, 1));
            if (!StringUtils.isEmpty(wp05No)) {
                if (wp05MapList.containsKey(wp05No)) {
                    wp05MapList.put(wp05No, wp05MapList.get(wp05No) + 1);
                } else {
                    wp05MapList.put(wp05No, 1);
                }
            }
        }
        // excel 内容解析
        for (int i = 2; i < sheet2.getPhysicalNumberOfRows(); i++) {
            row = sheet2.getRow(i);
            List<String> errors = new ArrayList<>();
            FMaterialStructureNestDrawing fMaterialStructureNestDrawingItem = new FMaterialStructureNestDrawing();
            String wp05No = StringUtils.trim(WorkbookUtils.readAsString(row, 1));
            String rev = WorkbookUtils.readAsString(row, 2);
            String pieceTagNo = WorkbookUtils.readAsString(row, 3);
            String qrCode = WorkbookUtils.readAsString(row, 4);
            String workingPeople = WorkbookUtils.readAsString(row, 5);
            String cuttingDate = WorkbookUtils.readAsString(row, 6);
            String nestingProgramNo = WorkbookUtils.readAsString(row, 7);
            if (StringUtils.isEmpty(wp05No)) {
                errors.add("wp05No is Empty");
                failedCount = failedCount + 1;
                batchResult.addErrorCount(1);
            }
            if (StringUtils.isEmpty(nestingProgramNo)) {
                errors.add("nestingProgramNo is Empty");
                failedCount = failedCount + 1;
                batchResult.addErrorCount(1);
            }
            if (StringUtils.isEmpty(rev)) {
                errors.add("version is Empty");
                failedCount = failedCount + 1;
                batchResult.addErrorCount(1);
            }

            // 材料二维码和件号只能有一个
            if (StringUtils.isEmpty(qrCode) && StringUtils.isEmpty(pieceTagNo)) {
                errors.add("二维码和件号不能都为空");
                failedCount = failedCount + 1;
                batchResult.addErrorCount(1);
            }
            if (!StringUtils.isEmpty(qrCode) && !StringUtils.isEmpty(pieceTagNo)) {
                errors.add("二维码和件号不能同时存在");
                failedCount = failedCount + 1;
                batchResult.addErrorCount(1);
            }

            // 材料表中查找二维码和件号是否有效
            if (!StringUtils.isEmpty(qrCode)) {
                MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO = new MmMaterialInStockSearchDTO();
                mmMaterialInStockSearchDTO.setQrCode(qrCode);
                MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockFeignAPI.searchMaterialInformation(
                    orgId,
                    projectId,
                    mmMaterialInStockSearchDTO
                ).getData();
                if (null == mmMaterialInStockDetailQrCodeEntity) {
                    errors.add("二维码无效");
                    failedCount = failedCount + 1;
                    batchResult.addErrorCount(1);
                }
            } else {
                MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO = new MmMaterialInStockSearchDTO();
                mmMaterialInStockSearchDTO.setPieceTagNo(pieceTagNo);
                MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockFeignAPI.searchMaterialInformation(
                    orgId,
                    projectId,
                    mmMaterialInStockSearchDTO
                ).getData();
                if (null == mmMaterialInStockDetailQrCodeEntity) {
                    errors.add("件号无效");
                    failedCount = failedCount + 1;
                    batchResult.addErrorCount(1);
                }
            }

            if (!wp05No.isEmpty()) {

                Wp05Entity wp05Entity = wp05EntityRepository.findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(
                    orgId,
                    projectId,
                    wp05No
                ).orElse(null);

                if (null == wp05Entity) {
                    errors.add("This Wp05: " + wp05No + " Entity does not exist in this project.");
                    failedCount = failedCount + 1;
                    batchResult.addErrorCount(1);
                }

                List<FMaterialStructureNestDrawing> nestDrawings = fMaterialStructureNestDrawingRepository.findByOrgIdAndProjectIdAndWp05NoAndRevAndFinishAndStatus(
                    orgId,
                    projectId,
                    wp05No,
                    rev,
                    true,
                    EntityStatus.ACTIVE
                );

                if (!nestDrawings.isEmpty()) {
                    errors.add("This Wp05: " + wp05No + " version: " + rev + " exist in other structure nesting.");
                    failedCount = failedCount + 1;
                    batchResult.addErrorCount(1);
                }
                if (!wp05MapList.isEmpty() && wp05MapList.get(wp05No) > 1) {
                    errors.add("Wp05:" + wp05No + " is repeat in this excel.");
                    failedCount = failedCount + 1;
                    batchResult.addErrorCount(1);
                }
                fMaterialStructureNestDrawingItem.setWp05No(wp05No);
                if (null != wp05Entity && null != wp05Entity.getId()) {
                    fMaterialStructureNestDrawingItem.setWp05Id(wp05Entity.getId());
                }
                if (!StringUtils.isEmpty(rev)) {
                    fMaterialStructureNestDrawingItem.setRev(rev);
                }
                if (!StringUtils.isEmpty(pieceTagNo)) {
                    fMaterialStructureNestDrawingItem.setPieceTagNo(pieceTagNo);
                }
                if (!StringUtils.isEmpty(qrCode)) {
                    fMaterialStructureNestDrawingItem.setMaterialQrCode(qrCode);
                }
                if (!StringUtils.isEmpty(workingPeople)) {
                    fMaterialStructureNestDrawingItem.setWorkingPeople(workingPeople);
                }
                if (!StringUtils.isEmpty(cuttingDate)) {
                    fMaterialStructureNestDrawingItem.setCuttingDate(cuttingDate);
                }
                if (!StringUtils.isEmpty(nestingProgramNo)) {
                    fMaterialStructureNestDrawingItem.setNestingProgramNo(nestingProgramNo);
                }

                fMaterialStructureNestDrawingItem.setOrgId(orgId);
                fMaterialStructureNestDrawingItem.setProjectId(projectId);
                fMaterialStructureNestDrawingItem.setfMaterialStructureNestId(fMaterialStructureNestId);

                Date now = new Date();
                fMaterialStructureNestDrawingItem.setCreatedBy(operator.getId());
                fMaterialStructureNestDrawingItem.setCreatedAt(now);
                fMaterialStructureNestDrawingItem.setLastModifiedBy(operator.getId());
                fMaterialStructureNestDrawingItem.setLastModifiedAt(now);
                fMaterialStructureNestDrawingItem.setStatus(EntityStatus.ACTIVE);
                if (errors.isEmpty()) {
                    fMaterialStructureNestDrawings.add(fMaterialStructureNestDrawingItem);
                    batchResult.addProcessedCount(1);
                }

            }
            if (!errors.isEmpty()) {
                WorkbookUtils
                    .setCellValue(sheet2, i, errorColNo, String.join(";", errors))
                    .setCellStyle(errorStyle);
            }
            batchTask.setResult(batchResult);
            batchTask.setLastModifiedAt();
            batchTaskRepository.save(batchTask);
        }
        fMaterialStructureNestImportSheetReturnDTO.setfMaterialStructureNestDrawingFailCount(failedCount);
        fMaterialStructureNestImportSheetReturnDTO.setSheetItem(sheet2);
        fMaterialStructureNestImportSheetReturnDTO.setfMaterialStructureNestDrawings(fMaterialStructureNestDrawings);
        return fMaterialStructureNestImportSheetReturnDTO;
    }

    /**
     * 启动结构套料。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 套料结构方案id
     * @param context
     * @param operatorDTO
     */
    @Override
    public void createActivity(Long orgId,
                               Long projectId,
                               Long fMaterialStructureNestId,
                               ContextDTO context,
                               OperatorDTO operatorDTO) {


        FMaterialStructureNest fMaterialStructureNest = fMaterialStructureNestRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, fMaterialStructureNestId);
        if (fMaterialStructureNest == null) {
            throw new BusinessError("StructureNest not found");
        }


        Optional<BpmProcessStage> bpmProcessStageOptional = bpmProcessStageRepository.findByOrgIdAndProjectIdAndNameEn(orgId, projectId, "STORAGE");
        if (!bpmProcessStageOptional.isPresent()) {
            throw new NotFoundError("please confirm process stage");
        }
        BpmProcessStage bpmProcessStage = bpmProcessStageOptional.get();


        Optional<BpmProcess> bpmProcessOptional = bpmProcessRepository.findByOrgIdAndProjectIdAndStageIdAndName(orgId, projectId, bpmProcessStage.getId(), "NESTED_MATERIAL_ISSUE");
        if (!bpmProcessOptional.isPresent()) {
            throw new NotFoundError("please confirm process");
        }
        BpmProcess bpmProcess = bpmProcessOptional.get();


        Optional<BpmEntitySubType> bpmEntityCategoryOptional = bpmEntityCategoryRepository.findByProjectIdAndNameEnAndStatus(projectId, "NESTING_PLAN_ST", EntityStatus.ACTIVE);
        if (!bpmEntityCategoryOptional.isPresent()) {
            throw new NotFoundError("please confirm EntityCategory");
        }
        BpmEntitySubType bpmEntityCategory = bpmEntityCategoryOptional.get();


        Optional<BpmEntityType> bpmEntityCategoryTypeOptional = bpmEntityCategoryTypeRepository.findByNameEnAndProjectIdAndOrgIdAndStatus("NESTING_PLAN", projectId, orgId, EntityStatus.ACTIVE);
        if (!bpmEntityCategoryTypeOptional.isPresent()) {
            throw new NotFoundError("please confirm EntityCategoryType");
        }
        BpmEntityType bpmEntityCategoryType = bpmEntityCategoryTypeOptional.get();


        ActivityInstanceDTO taskDTO = new ActivityInstanceDTO();
        taskDTO.setProcessId(bpmProcess.getId());
        taskDTO.setEntityId(fMaterialStructureNest.getId());
        taskDTO.setEntityNo(fMaterialStructureNest.getNo());
        taskDTO.setDrawingTitle(fMaterialStructureNest.getName());
        taskDTO.setProcessStageId(bpmProcessStage.getId());
        taskDTO.setEntitySubTypeId(bpmEntityCategory.getId());
        taskDTO.setAssignee(operatorDTO.getId());
        taskDTO.setAssigneeName(operatorDTO.getName());
        taskDTO.setEntityType("NESTING_PLAN_ST");
        taskDTO.setMemo(fMaterialStructureNest.getMemo());
        CreateResultDTO createResult = todoTaskDispatchService.create(context, orgId, projectId, operatorDTO, taskDTO);
        BpmActivityInstanceBase bpmActivityInstance = createResult.getActInst();


        fMaterialStructureNest.setStageId(bpmProcessStage.getId());
        fMaterialStructureNest.setProcessId(bpmProcess.getId());
        fMaterialStructureNest.setEntitySubTypeId(bpmEntityCategory.getId());
        fMaterialStructureNest.setEntityTypeId(bpmEntityCategoryType.getId());
        fMaterialStructureNest.setActInstId(bpmActivityInstance.getId());
        fMaterialStructureNest.setActivityStartStatus(false);
        fMaterialStructureNest.setModifyState(false);
        fMaterialStructureNest.setSaveActivityStatus(false);
        fMaterialStructureNestRepository.save(fMaterialStructureNest);

    }

    /**
     * 更改结构方案流程状态。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料方案id
     */
    @Override
    public BatchResultDTO changeActivityStatus(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        OperatorDTO operatorDTO,
        BatchTask batchTask
    ) {

        batchTask.setEntityId(fMaterialStructureNestId);
        batchTask.setStatus(BatchTaskStatus.RUNNING);
        batchTaskRepository.save(batchTask);
        BatchResultDTO batchResult = new BatchResultDTO(batchTask);
        FMaterialStructureNest fMaterialStructureNest = fMaterialStructureNestRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, fMaterialStructureNestId);
        if (fMaterialStructureNest == null) {
            batchTask.setStatus(BatchTaskStatus.FAILED);
            batchTaskRepository.save(batchTask);
            throw new BusinessError("StructureNest not found");
        }

        if (!fMaterialStructureNest.getModifyState()) {
            batchTask.setStatus(BatchTaskStatus.FAILED);
            batchTaskRepository.save(batchTask);
            throw new BusinessError("StructureNest 已经锁定，不能再次锁定");
        }

        fMaterialStructureNest.setActivityStartStatus(false);
        fMaterialStructureNest.setModifyState(false);
        fMaterialStructureNest.setSaveActivityStatus(false);
        Date now = new Date();
        fMaterialStructureNest.setCreatedBy(operatorDTO.getId());
        fMaterialStructureNest.setCreatedAt(now);
        fMaterialStructureNest.setLastModifiedBy(operatorDTO.getId());
        fMaterialStructureNest.setLastModifiedAt(now);
        fMaterialStructureNestRepository.save(fMaterialStructureNest);

        // 创建结构下料实体总表
        List<FMaterialStructureNestDrawing> materialStructureNestDrawings = fMaterialStructureNestDrawingRepository.findByOrgIdAndProjectIdAndFMaterialStructureNestIdAndStatus(
            orgId,
            projectId,
            fMaterialStructureNestId,
            EntityStatus.ACTIVE
        );
        if (!materialStructureNestDrawings.isEmpty()) {
            for (FMaterialStructureNestDrawing fMaterialStructureNestDrawing : materialStructureNestDrawings) {
                StructureEntityQrCode newStructureEntityQrCode = new StructureEntityQrCode();
                StructureEntityQrCode structureEntityQrCode = structureEntityQrCodeRepository.findByOrgIdAndProjectIdAndEntityIdAndDeletedIsFalse(
                    orgId,
                    projectId,
                    fMaterialStructureNestDrawing.getWp05Id()
                );
                if (structureEntityQrCode != null) {
                    newStructureEntityQrCode = structureEntityQrCode;
                }
                // 查找实体信息
                Wp05Entity wp05Entity = wp05EntityRepository.findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(
                    orgId,
                    projectId,
                    fMaterialStructureNestDrawing.getWp05No()
                ).orElse(null);

                if (null == wp05Entity) {
                    fMaterialStructureNestDrawing.setLastModifiedBy(operatorDTO.getId());
                    fMaterialStructureNestDrawing.setLastModifiedAt(new Date());
                    fMaterialStructureNestDrawing.setFinish(false);
                    fMaterialStructureNestDrawing.setDescription("下料时实体 " + fMaterialStructureNestDrawing.getWp05No() + "不存在。");
                    fMaterialStructureNestDrawingRepository.save(fMaterialStructureNestDrawing);

                    batchResult.addErrorCount(1);
                    batchTask.setResult(batchResult);
                    batchTaskRepository.save(batchTask);
                    continue;
                }

                // 查找材料信息
                MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO = new MmMaterialInStockSearchDTO();
                if (null != fMaterialStructureNestDrawing.getMaterialQrCode()) {
                    mmMaterialInStockSearchDTO.setQrCode(fMaterialStructureNestDrawing.getMaterialQrCode());
                }
                if (null != fMaterialStructureNestDrawing.getPieceTagNo()) {
                    mmMaterialInStockSearchDTO.setPieceTagNo(fMaterialStructureNestDrawing.getPieceTagNo());
                }
                MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockFeignAPI.searchMaterialInformation(
                    orgId,
                    projectId,
                    mmMaterialInStockSearchDTO
                ).getData();
                if (null == mmMaterialInStockDetailQrCodeEntity) {
                    fMaterialStructureNestDrawing.setLastModifiedBy(operatorDTO.getId());
                    fMaterialStructureNestDrawing.setLastModifiedAt(new Date());
                    fMaterialStructureNestDrawing.setFinish(false);
                    fMaterialStructureNestDrawing.setDescription("下料时材料二维码或者件号有误。");
                    fMaterialStructureNestDrawingRepository.save(fMaterialStructureNestDrawing);
                    batchResult.addErrorCount(1);
                    batchTask.setResult(batchResult);
                    batchTaskRepository.save(batchTask);
                    continue;
                }
                newStructureEntityQrCode.setOrgId(orgId);
                newStructureEntityQrCode.setProjectId(projectId);
                newStructureEntityQrCode.setEntityId(fMaterialStructureNestDrawing.getWp05Id());
                newStructureEntityQrCode.setEntityNo(fMaterialStructureNestDrawing.getWp05No());
                newStructureEntityQrCode.setEntityType(wp05Entity.getEntitySubType());
                newStructureEntityQrCode.setRevision(fMaterialStructureNestDrawing.getRev());
                newStructureEntityQrCode.setMaterial(mmMaterialInStockDetailQrCodeEntity.getMmMaterialCodeDescription());
                newStructureEntityQrCode.setLengthText(wp05Entity.getLengthText());
                newStructureEntityQrCode.setLengthUnit(wp05Entity.getLengthUnit());
                newStructureEntityQrCode.setLength(wp05Entity.getLength());
                newStructureEntityQrCode.setWidthText(wp05Entity.getWidthText());
                newStructureEntityQrCode.setWidthUnit(wp05Entity.getWidthUnit());
                newStructureEntityQrCode.setWidth(wp05Entity.getWidth());
                newStructureEntityQrCode.setHeightText(wp05Entity.getHeightText());
                newStructureEntityQrCode.setHeightUnit(wp05Entity.getHeightUnit());
                newStructureEntityQrCode.setHeight(wp05Entity.getHeight());
                newStructureEntityQrCode.setWeightText(wp05Entity.getWeightText());
                newStructureEntityQrCode.setWeightUnit(wp05Entity.getWeightUnit());
                newStructureEntityQrCode.setWeight(wp05Entity.getWeight());
                newStructureEntityQrCode.setPaintCode(wp05Entity.getPaintCode());
                newStructureEntityQrCode.setBusinessType(wp05Entity.getBusinessType());
                newStructureEntityQrCode.setBusinessTypeId(wp05Entity.getBusinessTypeId());
                newStructureEntityQrCode.setNew(wp05Entity.isNew());
                newStructureEntityQrCode.setQrCode(wp05Entity.getQrCode());
                newStructureEntityQrCode.setNestedFlag(wp05Entity.getNestedFlag());
                newStructureEntityQrCode.setHeatNo(mmMaterialInStockDetailQrCodeEntity.getHeatBatchNo());
                newStructureEntityQrCode.setIdent(mmMaterialInStockDetailQrCodeEntity.getIdentCode());
                newStructureEntityQrCode.setTagNumber(mmMaterialInStockDetailQrCodeEntity.getMmMaterialCodeNo());
                newStructureEntityQrCode.setProgramId(fMaterialStructureNestDrawing.getNestingProgramId());
                newStructureEntityQrCode.setProgramNo(fMaterialStructureNestDrawing.getNestingProgramNo());
                newStructureEntityQrCode.setPrintFlg(false);
                newStructureEntityQrCode.setManuallyCreated(false);
                newStructureEntityQrCode.setCreatedAt(new Date());
                newStructureEntityQrCode.setCreatedBy(operatorDTO.getId());
                newStructureEntityQrCode.setLastModifiedAt(new Date());
                newStructureEntityQrCode.setLastModifiedBy(operatorDTO.getId());
                newStructureEntityQrCode.setStatus(EntityStatus.ACTIVE);
                newStructureEntityQrCode.setMaterialQrCode(mmMaterialInStockDetailQrCodeEntity.getQrCode());
                newStructureEntityQrCode.setQrCode(wp05Entity.getQrCode());
                newStructureEntityQrCode.setCuttingId(fMaterialStructureNest.getId());
                newStructureEntityQrCode.setCuttingNo(fMaterialStructureNest.getNo());

                structureEntityQrCodeRepository.save(newStructureEntityQrCode);

                fMaterialStructureNestDrawing.setLastModifiedBy(operatorDTO.getId());
                fMaterialStructureNestDrawing.setLastModifiedAt(new Date());
                fMaterialStructureNestDrawing.setFinish(true);
                fMaterialStructureNestDrawing.setDescription("完成下料");
                fMaterialStructureNestDrawingRepository.save(fMaterialStructureNestDrawing);

                batchResult.addProcessedCount(1);
                batchTask.setResult(batchResult);
                batchTaskRepository.save(batchTask);
            }
        }

        List<FMaterialStructureNestProgram> fMaterialStructureNestPrograms = fMaterialStructureNestProgramRepository.findByOrgIdAndProjectIdAndFMaterialStructureNestIdAndStatus(
            orgId,
            projectId,
            fMaterialStructureNestId,
            EntityStatus.ACTIVE
        );
        if (!fMaterialStructureNestPrograms.isEmpty()) {
            for (FMaterialStructureNestProgram fMaterialStructureNestProgram : fMaterialStructureNestPrograms) {
                // 产生余料不能存在于当前项目
                MmSurplusMaterialSearchDTO mmSurplusMaterialSearchDTO = new MmSurplusMaterialSearchDTO();
                mmSurplusMaterialSearchDTO.setKeyword(fMaterialStructureNestProgram.getRemainderCreated());
                MmSurplusMaterialEntity mmSurplusMaterialEntities = mmSurplusMaterialFeignAPI.searchSurplusMaterial(
                    orgId,
                    projectId,
                    mmSurplusMaterialSearchDTO
                ).getData();
                if (null != mmSurplusMaterialEntities) {
                    fMaterialStructureNestProgram.setLastModifiedBy(operatorDTO.getId());
                    fMaterialStructureNestProgram.setLastModifiedAt(new Date());
                    fMaterialStructureNestProgram.setFinish(false);
                    fMaterialStructureNestProgram.setDescription("产生余料存在于当前项目");
                    fMaterialStructureNestProgramRepository.save(fMaterialStructureNestProgram);
                    batchResult.addErrorCount(1);
                    batchTask.setResult(batchResult);
                    batchTaskRepository.save(batchTask);
                    continue;
                }
                // 是否使用余料
                if (StringUtils.isEmpty(fMaterialStructureNestProgram.getRemainderUsed())) {
                    // 原材料下料
                    MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO = new MmMaterialInStockSearchDTO();
                    if (null != fMaterialStructureNestProgram.getMaterialQrCode()) {
                        mmMaterialInStockSearchDTO.setQrCode(fMaterialStructureNestProgram.getMaterialQrCode());
                    }
                    if (null != fMaterialStructureNestProgram.getPieceTagNo()) {
                        mmMaterialInStockSearchDTO.setPieceTagNo(fMaterialStructureNestProgram.getPieceTagNo());
                    }
                    MmMaterialInStockDetailQrCodeEntity mmMaterialInStockDetailQrCodeEntity = mmMaterialInStockFeignAPI.searchMaterialInformation(
                        orgId,
                        projectId,
                        mmMaterialInStockSearchDTO
                    ).getData();
                    if (null == mmMaterialInStockDetailQrCodeEntity) {
                        fMaterialStructureNestProgram.setLastModifiedBy(operatorDTO.getId());
                        fMaterialStructureNestProgram.setLastModifiedAt(new Date());
                        fMaterialStructureNestProgram.setFinish(false);
                        fMaterialStructureNestProgram.setDescription("使用的二维码或者件号不存在于当前项目。");
                        fMaterialStructureNestProgramRepository.save(fMaterialStructureNestProgram);
                        batchResult.addErrorCount(1);
                        batchTask.setResult(batchResult);
                        batchTaskRepository.save(batchTask);
                        continue;
                    }
                    if (!StringUtils.isEmpty(fMaterialStructureNestProgram.getRemainderCreated())) {
                        MmSurplusMaterialCreateDTO mmSurplusMaterialCreateDTO = new MmSurplusMaterialCreateDTO();
                        BeanUtils.copyProperties(
                            mmMaterialInStockDetailQrCodeEntity,
                            mmSurplusMaterialCreateDTO
                        );
                        mmSurplusMaterialCreateDTO.setCreatedBy(operatorDTO.getId());
                        mmSurplusMaterialCreateDTO.setName(fMaterialStructureNestProgram.getRemainderCreated());
                        mmSurplusMaterialCreateDTO.setNestDrawingNo(fMaterialStructureNestProgram.getNo());
                        mmSurplusMaterialCreateDTO.setNestNo(fMaterialStructureNest.getNo());

                        mmSurplusMaterialFeignAPI.create(
                            orgId,
                            projectId,
                            mmSurplusMaterialCreateDTO
                        );
                    }
                    fMaterialStructureNestProgram.setLastModifiedBy(operatorDTO.getId());
                    fMaterialStructureNestProgram.setLastModifiedAt(new Date());
                    fMaterialStructureNestProgram.setFinish(true);
                    fMaterialStructureNestProgram.setDescription("完成下料");
                    fMaterialStructureNestProgramRepository.save(fMaterialStructureNestProgram);

                    batchResult.addProcessedRelationCount(1);
                    batchTask.setResult(batchResult);
                    batchTaskRepository.save(batchTask);
                } else {

                    MmSurplusMaterialSearchDTO mmSurplusMaterialSearchDTO1 = new MmSurplusMaterialSearchDTO();
                    mmSurplusMaterialSearchDTO1.setKeyword(fMaterialStructureNestProgram.getRemainderUsed());
                    MmSurplusMaterialEntity mmSurplusMaterialEntity = mmSurplusMaterialFeignAPI.searchSurplusMaterial(
                        orgId,
                        projectId,
                        mmSurplusMaterialSearchDTO1
                    ).getData();
                    if (null == mmSurplusMaterialEntity) {
                        fMaterialStructureNestProgram.setLastModifiedBy(operatorDTO.getId());
                        fMaterialStructureNestProgram.setLastModifiedAt(new Date());
                        fMaterialStructureNestProgram.setFinish(false);
                        fMaterialStructureNestProgram.setDescription("使用余料不存在于当前项目");
                        fMaterialStructureNestProgramRepository.save(fMaterialStructureNestProgram);
                        batchResult.addErrorCount(1);
                        batchTask.setResult(batchResult);
                        batchTaskRepository.save(batchTask);
                        continue;
                    }
                    MmSurplusMaterialCreateDTO mmSurplusMaterialUpdateDTO = new MmSurplusMaterialCreateDTO();
                    mmSurplusMaterialUpdateDTO.setIssuedFlag(true);
                    mmSurplusMaterialUpdateDTO.setDescription("系统自动套料");

                    mmSurplusMaterialFeignAPI.update(
                        orgId,
                        projectId,
                        mmSurplusMaterialEntity.getId(),
                        mmSurplusMaterialUpdateDTO
                    );

                    if (!StringUtils.isEmpty(fMaterialStructureNestProgram.getRemainderCreated())) {
                        MmSurplusMaterialCreateDTO mmSurplusMaterialCreateDTO = new MmSurplusMaterialCreateDTO();
                        BeanUtils.copyProperties(
                            mmSurplusMaterialEntity,
                            mmSurplusMaterialCreateDTO
                        );
                        mmSurplusMaterialCreateDTO.setCreatedBy(operatorDTO.getId());
                        mmSurplusMaterialCreateDTO.setName(fMaterialStructureNestProgram.getRemainderCreated());
                        mmSurplusMaterialCreateDTO.setNestDrawingNo(fMaterialStructureNestProgram.getNo());
                        mmSurplusMaterialCreateDTO.setNestNo(fMaterialStructureNest.getNo());
                        mmSurplusMaterialCreateDTO.setOriginalSurplusId(mmSurplusMaterialEntity.getId());
                        mmSurplusMaterialCreateDTO.setOriginalSurplusName(mmSurplusMaterialEntity.getName());

                        mmSurplusMaterialFeignAPI.create(
                            orgId,
                            projectId,
                            mmSurplusMaterialCreateDTO
                        );
                    }
                    fMaterialStructureNestProgram.setLastModifiedBy(operatorDTO.getId());
                    fMaterialStructureNestProgram.setLastModifiedAt(new Date());
                    fMaterialStructureNestProgram.setFinish(true);
                    fMaterialStructureNestProgram.setDescription("完成下料");
                    fMaterialStructureNestProgramRepository.save(fMaterialStructureNestProgram);

                    batchResult.addProcessedRelationCount(1);
                    batchTask.setResult(batchResult);
                    batchTaskRepository.save(batchTask);
                }
            }
        }
        return batchResult;
    }

    /**
     * 关联领料单。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param fMaterialStructureNestId  套料方案id
     * @param fMaterialStructureNestDTO 套料单信息
     */
    @Override
    public void saveMaterialRequisitions(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        FMaterialStructureNestDTO fMaterialStructureNestDTO,
        ContextDTO contextDTO,
        OperatorDTO operatorDTO
    ) {

        FMaterialStructureNest fMaterialStructureNest = fMaterialStructureNestRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, fMaterialStructureNestId);
        if (fMaterialStructureNest == null) {
            throw new BusinessError("StructureNest not found");
        }

        Date now = new Date();
        fMaterialStructureNest.setCreatedBy(operatorDTO.getId());
        fMaterialStructureNest.setCreatedAt(now);
        fMaterialStructureNest.setLastModifiedBy(operatorDTO.getId());
        fMaterialStructureNest.setLastModifiedAt(now);
        fMaterialStructureNest.setStatus(EntityStatus.ACTIVE);


        if (fMaterialStructureNestDTO.getMaterialRequisitionsId() != null) {
            List<FMaterialStructureNest> searchFMaterialStructureNest = fMaterialStructureNestRepository.findByOrgIdAndProjectIdAndMaterialRequisitionsIdAndDeletedIsFalse(orgId, projectId, fMaterialStructureNestDTO.getMaterialRequisitionsId());

            if (searchFMaterialStructureNest.size() > 0) {
                if (searchFMaterialStructureNest.size() == 1) {
                    if (!searchFMaterialStructureNest.get(0).getId().equals(fMaterialStructureNest.getId())) {
                        throw new BusinessError("Material Requisition is related");
                    }
                } else {
                    throw new BusinessError("Material Requisition is related");
                }

            }

//            Optional<FMaterialPrepareEntity> fMaterialPrepareEntityOptionalForSpmFahIdExistCheck =
//                fMaterialPrepareRepository.findByOrgIdAndProjectIdAndSpmFahId(orgId, projectId, fMaterialStructureNestDTO.getMaterialRequisitionsId());
//            if (fMaterialPrepareEntityOptionalForSpmFahIdExistCheck.isPresent()) {
//                throw new BusinessError("SPM FA has been selected");
//            }
            fMaterialStructureNest.setMaterialRequisitionsId(fMaterialStructureNestDTO.getMaterialRequisitionsId());

        } else {
            fMaterialStructureNest.setMaterialRequisitionsId(null);
        }

        if (fMaterialStructureNestDTO.getMaterialRequisitionsCode() != null) {
            fMaterialStructureNest.setMaterialRequisitionsCode(fMaterialStructureNestDTO.getMaterialRequisitionsCode());
        } else {
            fMaterialStructureNest.setMaterialRequisitionsCode(null);
        }

        if (fMaterialStructureNest.getMaterialRequisitionsId() != null && fMaterialStructureNest.getMaterialRequisitionsCode() != null) {
            FMaterialRequisitionPostDTO fMaterialRequisitionPostDTO = new FMaterialRequisitionPostDTO();
            fMaterialRequisitionPostDTO.setSpmFahId(fMaterialStructureNest.getMaterialRequisitionsId());
//            FMaterialRequisitionEntity fMaterialRequisitionEntity = fMaterialRequisitionInterface.postFMReq(orgId, projectId, operatorDTO, fMaterialRequisitionPostDTO);
//            if (fMaterialRequisitionEntity != null) {
//                fMaterialRequisitionEntity.setActInstanceId(fMaterialStructureNest.getProcInstId());
//                fMaterialRequisitionEntity.setfMaterialStructureNestId(fMaterialStructureNest.getId());
//                fMaterialRequisitionRepository.save(fMaterialRequisitionEntity);
//            } else {
//                throw new BusinessError("Material Requisition not found");
//            }
        }
        fMaterialStructureNestRepository.save(fMaterialStructureNest);
    }

    /**
     * 通过实体id查找领料信息。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料id
     * @param contextDTO
     * @param operatorDTO
     * @return
     */
    @Override
    public FMaterialRequisitionEntity findMaterialRequisitionsByEntityId(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        ContextDTO contextDTO,
        OperatorDTO operatorDTO) {

        FMaterialStructureNest fMaterialStructureNest = fMaterialStructureNestRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, fMaterialStructureNestId);
        if (fMaterialStructureNest == null) {
            throw new BusinessError("StructureNest not found");
        }

//        if (fMaterialStructureNest.getMaterialRequisitionsId() != null && !"".equals(fMaterialStructureNest.getMaterialRequisitionsId())) {
//            FMaterialRequisitionPostDTO fMaterialRequisitionPostDTO = new FMaterialRequisitionPostDTO();
//            fMaterialRequisitionPostDTO.setSpmFahId(fMaterialStructureNest.getMaterialRequisitionsId());
//            FMaterialRequisitionEntity fMaterialRequisitionEntity = fMaterialRequisitionInterface.postFMReq(orgId, projectId, operatorDTO, fMaterialRequisitionPostDTO);
//            if (fMaterialRequisitionEntity != null) {
//                return fMaterialRequisitionEntity;
//            } else {
//                throw new BusinessError("Material Requisition not found");
//            }
//        } else {
//            throw new BusinessError("Material structure nest is not related Material Requisition");
//        }

        return new FMaterialRequisitionEntity();
    }

    /**
     * 设置分包商。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param fMaterialStructureNestId  结构套料清单id
     * @param fMaterialStructureNestDTO 更新内容
     * @param operatorDTO               操作者
     */
    @Override
    public FMaterialStructureNest setSubcontractor(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        FMaterialStructureNestDTO fMaterialStructureNestDTO,
        OperatorDTO operatorDTO
    ) {

        FMaterialStructureNest fMaterialStructureNest = fMaterialStructureNestRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, fMaterialStructureNestId);
        if (fMaterialStructureNest == null) {
            throw new BusinessError("StructureNest not found");
        } else {
            if (fMaterialStructureNestDTO.getCompanyCode() != null && fMaterialStructureNestDTO.getCompanyId() != null) {
                fMaterialStructureNest.setCompanyCode(fMaterialStructureNestDTO.getCompanyCode());
                fMaterialStructureNest.setCompanyId(fMaterialStructureNestDTO.getCompanyId());
            } else {
                fMaterialStructureNest.setCompanyCode(null);
                fMaterialStructureNest.setCompanyId(null);
            }
            Date now = new Date();
            fMaterialStructureNest.setLastModifiedBy(operatorDTO.getId());
            fMaterialStructureNest.setLastModifiedAt(now);
            fMaterialStructureNestRepository.save(fMaterialStructureNest);
            return fMaterialStructureNest;
        }

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
