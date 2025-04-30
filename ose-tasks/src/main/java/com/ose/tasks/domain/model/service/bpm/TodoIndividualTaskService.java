package com.ose.tasks.domain.model.service.bpm;

import com.ose.dto.PageDTO;
import com.ose.report.dto.InspectionReportPostDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.report.vo.ReportExportType;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.drawing.*;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.domain.model.service.report.InspectionReportNoInterface;
import com.ose.tasks.dto.*;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.*;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceReport;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.drawing.*;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 用户服务。
 */
@Component
public class TodoIndividualTaskService implements TodoIndividualTaskInterface {
    private final static Logger logger = LoggerFactory.getLogger(TodoIndividualTaskService.class);


    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    // 上传文件的临时路径
    @Value("${application.files.protected}")
    private String protectedDir;

    @Value("${mail.server.host}")
    private String mailHost;

    @Value("${mail.server.port}")
    private String mailPort;

    @Value("${mail.server.username}")
    private String mailUsername;

    @Value("${mail.server.password}")
    private String mailPassword;

    @Value("${mail.server.fromAddress}")
    private String mailFromAddress;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final DrawingRepository drawingRepository;

    private final DesignChangeReviewRegisterRepository designChangeReviewRegisterRepository;

    private final ConstructionChangeRegisterRepository constructionChangeRegisterRepository;

    private final InspectionReportNoInterface inspectionReportNoService;

    private final BpmActivityInstanceReportRepository bpmActivityInstanceReportRepository;

    //流程执行服务
//    private TodoTaskInterface todoTaskService;
//    private final TodoTaskDispatchInterface todoTaskDispatchService;

    //流程执行 基础服务
    private TodoTaskBaseInterface todoTaskBaseService;

    private final TaskRuleCheckService taskRuleCheckService;

    /**
     * 构造方法。
     */
    @Autowired
    public TodoIndividualTaskService(BpmActivityInstanceRepository bpmActInstRepository,
                                     BpmRuTaskRepository ruTaskRepository,
                                     DrawingRepository drawingRepository,
                                     DesignChangeReviewRegisterRepository designChangeReviewRegisterRepository,
                                     ConstructionChangeRegisterRepository constructionChangeRegisterRepository,
                                     InspectionReportNoInterface inspectionReportNoService,
                                     BpmActivityInstanceReportRepository bpmActivityInstanceReportRepository,
                                     TodoTaskBaseInterface todoTaskBaseService,
                                     TaskRuleCheckService taskRuleCheckService) {
        this.bpmActInstRepository = bpmActInstRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.designChangeReviewRegisterRepository = designChangeReviewRegisterRepository;
        this.constructionChangeRegisterRepository = constructionChangeRegisterRepository;
        this.drawingRepository = drawingRepository;
        this.inspectionReportNoService = inspectionReportNoService;
        this.bpmActivityInstanceReportRepository = bpmActivityInstanceReportRepository;
        this.todoTaskBaseService = todoTaskBaseService;
        this.taskRuleCheckService = taskRuleCheckService;
    }


    @Override
    public void unlockDrawing(Long orgId, Long projectId, BpmActivityInstanceBase actInst) {
        Optional<Drawing> op = drawingRepository.findByOrgIdAndProjectIdAndDwgNoAndStatus(orgId,
            projectId, actInst.getEntityNo(), EntityStatus.ACTIVE);
        if (op.isPresent()) {
            Drawing drawing = op.get();
            drawing.setLocked(false);
            drawing.setLastModifiedAt();
            drawingRepository.save(drawing);
        }
    }



    @Override
    public List<BpmActivityInstanceBase> getSubActInstFromModificationReviewRegister(Long projectId, Long id) {
        Optional<DesignChangeReviewRegister> op = designChangeReviewRegisterRepository.findById(id);
        if (op.isPresent()) {
            DesignChangeReviewRegister mrr = op.get();
            List<Long> actInstIds = mrr.getJsonDwgActInstIdsReadOnly();
            if (actInstIds != null) {
                Long[] longs = new Long[actInstIds.size()];
                return bpmActInstRepository.findByProjectIdAndIdInAndStatus(projectId, actInstIds.toArray(longs), EntityStatus.ACTIVE);
            }
        }
        return null;
    }

    @Override
    public List<BpmActivityInstanceBase> getSubActInstFromConstructionChangeRegister(Long projectId, Long id) {
        Optional<ConstructionChangeRegister> op = constructionChangeRegisterRepository.findById(id);
        if (op.isPresent()) {
            ConstructionChangeRegister ccr = op.get();
            List<Long> actInstIds = ccr.getJsonDwgActInstIdsReadOnly();
            if (actInstIds != null) {
                Long[] longs = new Long[actInstIds.size()];
                return bpmActInstRepository.findByProjectIdAndIdInAndStatus(projectId, actInstIds.toArray(longs), EntityStatus.ACTIVE);
            }
        }
        return null;
    }




    @Override
    public ExportFileDTO preBuildReport(Long orgId, Long projectId, TodoTaskExecuteDTO toDoTaskDTO) {

        List<BpmActivityInstanceReport> actInstList = new ArrayList<>();
        String process = null;
        String ndeType = null;
        String moduleName = null;
        String taskDefKey = null;
        if (toDoTaskDTO.getId() != null) {
            todoTaskBaseService.saveVariableValue(orgId, projectId, toDoTaskDTO, toDoTaskDTO.getId());
            BpmRuTask ruTask = todoTaskBaseService.findBpmRuTaskById(toDoTaskDTO.getId());
            if (ruTask != null) {
                actInstList.add(bpmActivityInstanceReportRepository.findActInstByActInstId(projectId, ruTask.getActInstId()));
            }
            if (taskDefKey == null) {
                taskDefKey = ruTask.getTaskDefKey();
            }
        } else if (toDoTaskDTO.getIds() != null) {
            for (Long taskId : toDoTaskDTO.getIds()) {
                BpmRuTask ruTask = todoTaskBaseService.findBpmRuTaskByActTaskId(taskId);
                if (ruTask == null) continue;
                todoTaskBaseService.saveVariableValue(orgId, projectId, toDoTaskDTO, ruTask.getId());
                if (ruTask != null) {
                    actInstList.add(bpmActivityInstanceReportRepository.findActInstByActInstId(projectId, ruTask.getActInstId()));
                }
                if (taskDefKey == null) {
                    taskDefKey = ruTask.getTaskDefKey();
                }
            }
        }

        for (BpmActivityInstanceReport actInst : actInstList) {
            if (process == null) {
                process = actInst.getProcess();
                ndeType = actInst.getNdeType().getDisplayName();
                moduleName = actInst.getEntityModuleName();
            } else {
                continue;
            }
        }

        String reportNo = inspectionReportNoService.generateTempReportNo(orgId, projectId, process, ndeType, moduleName);
        InspectionReportPostDTO dtoReport = new InspectionReportPostDTO();
//        String reportId = QrcodePrefixType.EXTERNAL_CONTROL_REPORT.getCode() + StringUtils.generateShortUuid();
        dtoReport.setDate(new Date());
        dtoReport.setSerialNo("00000");
        dtoReport.setReportNo(reportNo);
        dtoReport.setReportName(reportNo);
        dtoReport.setExportType(ReportExportType.PDF);

        ReportHistory reportHistory = null;

        Map<String, Object> reportMetaData = new HashMap<>();
        reportMetaData.put("taskDefKey", taskDefKey);
//        reportHistory = ndtExInspReport.generateReport(orgId, projectId, null, actInstList, dtoReport, reportMetaData, null,null);

        ExportFileDTO file = new ExportFileDTO();
        if (reportHistory != null) {
            file.setFileId(reportHistory.getFileId());
        }
        return file;
    }


    @Override
    public Page<TodoTaskForemanDispatchDTO> searchForemanDispatchTodo(Long orgId, Long projectId, Long assignee,
                                                                      TodoTaskCriteriaDTO taskCriteria, PageDTO pageDTO) {

        String assigneeId = null;
        List<Long> entityIdList = todoTaskBaseService.findEntityIdListForRuTask(orgId, projectId, taskCriteria.getQrcode());

//        if (CollectionUtils.isEmpty(entityIdList)) {
//            new PageImpl<>(new ArrayList<>(), pageDTO.toPageable(), 0L);
//            return new Page;
//        }

        if (assignee != null) {
            assigneeId = assignee.toString();
        }
        Page<TodoTaskForemanDispatchDTO> page = ruTaskRepository.searchForemanDispatchTodo(orgId, projectId, assigneeId, taskCriteria, pageDTO, entityIdList);

        return page;
    }


}
