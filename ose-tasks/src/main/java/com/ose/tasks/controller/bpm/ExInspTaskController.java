package com.ose.tasks.controller.bpm;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.feign.RequestWrapper;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.bpm.ExtenalInspectionTaskAPI;
import com.ose.tasks.domain.model.repository.bpm.BpmExInspScheduleRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.bpm.ExInspActInstHandleHistoryRepository;
import com.ose.tasks.domain.model.repository.bpm.QCReportRepository;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.domain.model.service.bpm.externalInspection.*;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmExInspApply;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.entity.report.ExInspActInstHandleHistory;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.bpm.ExInspApplyHandleType;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.BpmTaskType;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.util.PdfUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "外检流程任务管理接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class ExInspTaskController extends BaseController implements ExtenalInspectionTaskAPI {

    private final static Logger logger = LoggerFactory.getLogger(ExInspTaskController.class);


    @Value("${application.files.temporary}")
    private String temporaryDir;

    /**
     * 任务管理服务
     */

    private final ExInspTaskInterface externalInspectionTaskService;

    private final ExInspTaskBaseInterface externalInspectionTaskBaseService;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final ExInspMailInterface externalInspectionMailService;

    private final ExInspReportInterface externalInspectionReportService;

    private final ExInspApplyInterface externalInspectionApplyService;

    private final ExInspScheduleInterface externalInspectionScheduleService;

    private final ExInspUploadReportInterface externalInspectionUploadReportService;

    private final BpmExInspScheduleRepository bpmExInspScheduleRepository;

    private final BatchTaskInterface batchTaskService;
    private final ProjectInterface projectService;

    private final QCReportRepository qcReportRepository;

    private final BpmRuTaskRepository bpmRuTaskRepository;

    private final ExInspActInstHandleHistoryRepository exInspActInstHandleHistoryRepository;

    /**
     * 构造方法
     * 任务管理服务
     */
    @Autowired
    public ExInspTaskController(
        ExInspTaskInterface externalInspectionTaskService,
        ExInspTaskBaseInterface externalInspectionTaskBaseService,
        TodoTaskDispatchInterface todoTaskDispatchService,
        TodoTaskBaseInterface todoTaskBaseService,
        ExInspMailInterface externalInspectionMailService,
        ExInspReportInterface externalInspectionReportService,
        ExInspApplyInterface externalInspectionApplyService,
        ExInspScheduleInterface externalInspectionScheduleService,
        ExInspUploadReportInterface externalInspectionUploadReportService,
        BpmExInspScheduleRepository bpmExInspScheduleRepository,
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService,
        QCReportRepository qcReportRepository,
        BpmRuTaskRepository bpmRuTaskRepository,
        ExInspActInstHandleHistoryRepository exInspActInstHandleHistoryRepository) {
        this.externalInspectionTaskService = externalInspectionTaskService;
        this.externalInspectionMailService = externalInspectionMailService;
        this.externalInspectionTaskBaseService = externalInspectionTaskBaseService;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.todoTaskBaseService = todoTaskBaseService;
        this.externalInspectionReportService = externalInspectionReportService;
        this.externalInspectionApplyService = externalInspectionApplyService;
        this.externalInspectionScheduleService = externalInspectionScheduleService;
        this.externalInspectionUploadReportService = externalInspectionUploadReportService;
        this.bpmExInspScheduleRepository = bpmExInspScheduleRepository;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
        this.qcReportRepository = qcReportRepository;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
        this.exInspActInstHandleHistoryRepository = exInspActInstHandleHistoryRepository;
    }

//    @Override
//    @Operation(summary = "上传文档报告PDF", description = "上传文档文件")
//    @RequestMapping(method = POST, value = "upload-report", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
//    @WithPrivilege
//    public JsonObjectResponseBody<BpmExInspConfirmResponseDTO> uploadReport(
//        @PathVariable @Parameter(description = "所属组织 ID") Long orgId, @PathVariable @Parameter(description = "项目 ID") Long projectId,
//        @RequestBody DrawingUploadDTO uploadDTO) {
//        logger.info("Async Report upload Start");
//        uploadDTO.setTaskType(BpmTaskType.EX_INSP_UPLOAD_REPORT.name());
//        externalInspectionUploadReportService.uploadReport(
//            orgId,
//            projectId,
//            uploadDTO,
//            getContext().getOperator(),
//            false,
//            getContext());
//        logger.info("Async Report upload Finished");
//        return new JsonObjectResponseBody<>(
//        );
//    }

    @Override
    @Operation(summary = "上传文档报告PDF(包括替换上传)", description = "上传文档文件(包括替换上传)")
    @RequestMapping(method = POST, value = "upload-report", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<BpmExInspConfirmResponseDTO> uploadReport(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody DrawingUploadDTO uploadDTO) {
        logger.info("Async Report upload Start");
        uploadDTO.setTaskType(BpmTaskType.EX_INSP_UPLOAD_REPORT.name());
        ReportStatus reportStatus = ReportStatus.CANCEL;
        if (uploadDTO.getSeriesNo() != null && !"".equals(uploadDTO.getSeriesNo())) {
            QCReport qcReport = qcReportRepository.findByProjectIdAndSeriesNo(projectId, uploadDTO.getSeriesNo());
            if (qcReport != null) {
                reportStatus = qcReport.getReportStatus();
                if (reportStatus.equals(ReportStatus.CANCEL)) {
                    throw new BusinessError("当前报告已被废止！");
                }
            }
        } else {
            throw new BusinessError("请输入流水号！");
        }
        List<ReportStatus> uploadStatusList = new ArrayList<>();
        uploadStatusList.add(ReportStatus.INIT);
        uploadStatusList.add(ReportStatus.EMAIL_SENT);
        uploadStatusList.add(ReportStatus.REHANDLE_INIT);
        uploadStatusList.add(ReportStatus.REHANDLE_MID);

        List<ReportStatus> secondUploadStatusList = new ArrayList<>();
        secondUploadStatusList.add(ReportStatus.DONE);
        secondUploadStatusList.add(ReportStatus.UPLOADED);
        secondUploadStatusList.add(ReportStatus.COMMENTS);
        secondUploadStatusList.add(ReportStatus.REHANDLE_UPLOADED);
        secondUploadStatusList.add(ReportStatus.REHANDLE_DONE);
        secondUploadStatusList.add(ReportStatus.PENDING);
        secondUploadStatusList.add(ReportStatus.SKIP_UPLOAD);
        secondUploadStatusList.add(ReportStatus.REHANDLE_SKIP_UPLOAD);

        Set<String> taskDefKeys = new HashSet<>();
        taskDefKeys.add("UT-UPLOAD_EXTERNAL_INSPECTION_REPORT");
        taskDefKeys.add("usertask-QC-REPORT-UPLOAD");
        taskDefKeys.add("usertask-PMI-REPORT-UPLOAD");
        taskDefKeys.add("usertask-PWHT-REPORT-UPLOAD");
        taskDefKeys.add("usertask-QC-REPORT-NG-UPLOAD");
        taskDefKeys.add("usertask-QC-REPORT-UPLOAD-PAUT");
        taskDefKeys.add("usertask-QC-REPORT-NG-UPLOAD-PAUT");
        taskDefKeys.add("UT-UPLOAD_RENEWED_REPORT");

        if (uploadStatusList.contains(reportStatus)) {
            BpmRuTask bpmRuTask = bpmRuTaskRepository.findByActInstIdAndTaskDefKey(LongUtils.parseLong(uploadDTO.getSeriesNo()), projectId.toString());
//            for (BpmRuTask bpmRuTask : bpmRuTasks) {
                if (!taskDefKeys.contains(bpmRuTask.getTaskDefKey())) {
                    throw new BusinessError("当前报告中有流程未流转至上传报告！");
                }
//            }

            Optional<ExInspActInstHandleHistory> historyOptional = exInspActInstHandleHistoryRepository.findByProjectIdAndSeriesNoAndTypeAndRunningStatus(
                projectId,
                uploadDTO.getSeriesNo(),
                ExInspApplyHandleType.UPLOAD_REPORT,
                EntityStatus.ACTIVE
            );
            if (historyOptional.isPresent()) {
                throw new BusinessError("当前流水号正在回传中！！！");
            }

            QCReport report = qcReportRepository.findByProjectIdAndSeriesNo(projectId, uploadDTO.getSeriesNo());
            if (report == null) {
                throw new BusinessError("报告不存在");
            } else {
                BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findByIdAndStatus(report.getScheduleId(), EntityStatus.ACTIVE);
                if (!report.getProcess().equals("UT_MT") && bpmExInspSchedule != null && bpmExInspSchedule.getRunningStatus() != null && bpmExInspSchedule.getRunningStatus().equals(EntityStatus.APPROVED)
                    && bpmExInspSchedule.getState() != null && !bpmExInspSchedule.getState().equals(ReportStatus.REHANDLE_INIT)) {
                    throw new BusinessError("当前报告已回传！");
                }
            }


            externalInspectionUploadReportService.uploadReport(
                orgId,
                projectId,
                uploadDTO,
                getContext().getOperator(),
                false,
                getContext());
            logger.info("Async Report upload Finished");
            return new JsonObjectResponseBody<>(
            );
        } else if (secondUploadStatusList.contains(reportStatus)) {
            externalInspectionUploadReportService.uploadReport(
                orgId,
                projectId,
                uploadDTO,
                getContext().getOperator(),
                true,
                getContext());
            logger.info("Async Report upload Finished");
            return new JsonObjectResponseBody<>(
            );
        }
        return null;
    }

    @Override
    @Operation(summary = "确认上传的报告文件", description = "确认上传的报告文件")
    @RequestMapping(method = POST, value = "upload-report-confirm", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<BpmExInspConfirmResponseDTO> confirmUploadReport(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody BpmExInspConfirmsDTO confirmDTO
    ) {

        logger.info("Async Report upload confirm Start");
        confirmDTO.setTaskType(BpmTaskType.EX_INSP_UPLOAD_REPORT.name());
        externalInspectionUploadReportService.confirmUploadedReport(
            getContext(), orgId, projectId, confirmDTO, getContext().getOperator()
        );
        logger.info("Async Report upload confirm Finished");

        return new JsonObjectResponseBody<>(
        );
    }

    @Override
    @Operation(summary = "上传文档报告PDF", description = "确认上传的报告文件")
    @RequestMapping(method = POST, value = "upload-ndt-report-confirm", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<BpmExInspConfirmResponseDTO> confirmNdtUploadReport(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody BpmExInspConfirmsDTO confirmDTO
    ) {
        ContextDTO context = getContext();
        confirmDTO.setTaskType(BpmTaskType.NDT_UPLOAD_REPORT.name());

        Map<String, Object> data = new HashMap<>();
        data.put("orgId", orgId);
        data.put("projectId", projectId);

        TodoBatchTaskDTO todoBatchTaskDTO = todoTaskDispatchService.batchExec(context, data, confirmDTO);

        Map<String, Object> metaData = todoBatchTaskDTO.getMetaData();
        BpmExInspConfirmResponseDTO responseDTO;
        if (metaData == null) {
            metaData = new HashMap<>();
            responseDTO = new BpmExInspConfirmResponseDTO();
            responseDTO.setSuccessful(false);
            metaData.put("responseDTO", responseDTO);
        } else if (metaData.get("lastConfirmReport") != null) {
            metaData = new HashMap<>();
            responseDTO = new BpmExInspConfirmResponseDTO();
            responseDTO.setSuccessful(true);
            metaData.put("responseDTO", responseDTO);

        } else {
            responseDTO = (BpmExInspConfirmResponseDTO) metaData.get("responseDTO");
        }

        return new JsonObjectResponseBody<>(
            responseDTO
        );
    }

    @Override
    @Operation(summary = "确认PMI上传的报告文件", description = "确认PMI上传的报告文件")
    @RequestMapping(method = POST, value = "upload-pmi-report-confirm", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<BpmExInspConfirmResponseDTO> confirmPmiUploadReport(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody BpmExInspConfirmsDTO confirmDTO
    ) {
        ContextDTO context = getContext();
        confirmDTO.setTaskType(BpmTaskType.PMI_UPLOAD_REPORT.name());

        Map<String, Object> data = new HashMap<>();
        data.put("orgId", orgId);
        data.put("projectId", projectId);

        TodoBatchTaskDTO todoBatchTaskDTO = todoTaskDispatchService.batchExec(context, data, confirmDTO);

        Map<String, Object> metaData = todoBatchTaskDTO.getMetaData();
        BpmExInspConfirmResponseDTO responseDTO;
        if (metaData == null) {
            metaData = new HashMap<>();
            responseDTO = new BpmExInspConfirmResponseDTO();
            responseDTO.setSuccessful(false);
            metaData.put("responseDTO", responseDTO);
        } else {
            responseDTO = (BpmExInspConfirmResponseDTO) metaData.get("responseDTO");
        }

        return new JsonObjectResponseBody<>(
            responseDTO
        );
    }

    @Override
    @Operation(summary = "跳过上传", description = "跳过上传")
    @RequestMapping(method = POST, value = "skip-upload-report", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody skipUpload(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody DrawingUploadDTO skipDTO
    ) {

        logger.info("Async Report skip Start");
        skipDTO.setTaskType(BpmTaskType.EX_INSP_UPLOAD_REPORT.name());
        skipDTO.setSkipUploadFlag(true);
        externalInspectionUploadReportService.skipUpload(
            orgId,
            projectId,
            skipDTO,
            getContext()
        );

        logger.info("Async Report skip Finished");

        return new JsonResponseBody();
    }

    @RequestMapping(method = GET, value = "external-inspection-upload-histories/{id}/confirm-details")
    @Operation(summary = "外检报告上传历史详情", description = "查询外检报告上传历史的详情记录")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmExInspConfirm> uploadFileConfirmDetails(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "historyid") Long id,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            externalInspectionReportService.uploadFileConfirmDetails(orgId, projectId, id, pageDTO)
        );
    }

    @RequestMapping(method = GET, value = "tasks/external-inspection-schedule")
    @Operation(summary = "查询待办任务外检安排列表", description = "根据条件查询待办任务中外检安排列表。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<TodoTaskDTO> searchTodoSchedule(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                                @PathVariable @Parameter(description = "项目id") Long projectId, PageDTO pageDTO) {

        Long assignee = getContext().getOperator().getId();

        List<Long> actInstIds = todoTaskBaseService.findActInstIdsInActivityInstance(orgId, projectId,
            new TodoTaskCriteriaDTO());

        if (actInstIds.isEmpty()) {
            actInstIds.add(0L);
        }

        Page<BpmRuTask> ruTasks = externalInspectionTaskBaseService.getExternalInspectionRuTaskList(orgId, projectId, assignee,
            actInstIds, pageDTO);

        List<TodoTaskDTO> resultList = new ArrayList<>();
        List<BpmRuTask> bpmRuTasks = ruTasks.getContent();
        for (BpmRuTask ruTask : bpmRuTasks) {
            BpmActivityInstanceBase actInst = todoTaskBaseService.findActInstByProjectIdAndActInstId(projectId, ruTask.getActInstId());
            if (actInst != null) {
                TodoTaskDTO dto = new TodoTaskDTO();
                dto.setActInstId(actInst.getId());
                dto.setActInstId(actInst.getId());
                dto.setTaskId(ruTask.getId());
                dto.setEntitySubType(actInst.getEntitySubType());
                dto.setEntityNo(actInst.getEntityNo());
                dto.setProcess(actInst.getProcess());
                dto.setProcessStage(actInst.getProcessStage());
                dto.setProcessCategory(actInst.getActCategory());









                dto.setSuspensionState(SuspensionState.ACTIVE);


                dto.setTaskCreatedTime(ruTask.getCreateTime());
                dto.setTaskNode(ruTask.getName());
                resultList.add(dto);
            }
        }
        Page<TodoTaskDTO> resultPage = new PageImpl<>(resultList, pageDTO.toPageable(), ruTasks.getTotalElements());

        return new JsonListResponseBody<>(resultPage);
    }

    @RequestMapping(method = GET, value = "tasks/external-inspection-apply")
    @Operation(summary = "查询待办任务外检申请列表", description = "根据条件查询待办任务中外检申请列表。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmExInspApply> searchTodoApply(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        ExInspApplyCriteriaDTO criteriaDTO) {
        Long userId = getContext().getOperator().getId();
        List<Long> assignees = new ArrayList<>();
        assignees.add(userId);

        return new JsonListResponseBody<>(
            externalInspectionApplyService.getExternalInspectionApplyList(orgId, projectId, assignees, criteriaDTO));
    }

    @RequestMapping(method = GET, value = "tasks/external-inspection-apply/filter-condition")
    @Operation(summary = "查询待办任务外检申请列表过滤条件", description = "查询待办任务中外检申请列表过滤条件。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<ExInspApplyFilterConditionDTO> searchTodoApplyFilterCondition(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId) {
        Long userId = getContext().getOperator().getId();
        List<Long> assignees = new ArrayList<>();
        assignees.add(userId);

        return new JsonObjectResponseBody<>(
            externalInspectionApplyService.getExternalInspectionApplyFilterCondition(orgId, projectId, assignees));
    }

    @RequestMapping(method = POST, value = "external-inspection-schedule")
    @Operation(summary = "创建报检申请记录", description = "创建报检申请记录。")
    @WithPrivilege
    @ResponseStatus(CREATED)
    @Override
    public JsonObjectResponseBody<BpmExInspSchedule> createExternalInspectionSchedule(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "报检信息") ExInspScheduleDTO scheduleDTO) {
        return new JsonObjectResponseBody<>(getContext(), externalInspectionScheduleService.createExternalInspectionSchedule(orgId,
            projectId, scheduleDTO, getContext().getOperator(), null, null));
    }

    @RequestMapping(method = GET, value = "external-inspection-schedule")
    @Operation(summary = "获取报检申请安排记录列表", description = "获取报检申请安排记录列表。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmExInspSchedule> getExternalInspectionScheduleList(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        ExInspScheduleCriteriaDTO criteriaDTO, PageDTO page) {
        criteriaDTO.setOperator(getContext().getOperator().getId());
        return new JsonListResponseBody<>(
            externalInspectionScheduleService.getExternalInspectionScheduleList(orgId, projectId, page, criteriaDTO));
    }

    @RequestMapping(method = GET, value = "external-inspection-schedule/{id}")
    @Operation(summary = "获取报检申请记录详情", description = "获取报检申请记录详情。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<BpmExInspSchedule> getExternalInspectionSchedule(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "id") Long id) {
        return new JsonObjectResponseBody<>(getContext(), externalInspectionScheduleService.getExternalInspectionSchedule(id));
    }

    @RequestMapping(method = GET, value = "external-inspection-schedule/{id}/details")
    @Operation(summary = "获取报检申请记录文件详情", description = "获取报检申请记录文件详情。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmExInspScheduleDetail> getExternalInspectionScheduleDetail(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "id") Long id) {
        return new JsonListResponseBody<>(externalInspectionScheduleService.getExternalInspectionScheduleDetails(id));
    }

    @RequestMapping(method = DELETE, value = "external-inspection-schedule/{id}")
    @Operation(summary = "删除报检申请记录", description = "删除报检申请记录。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody deleteExternalInspectionSchedule(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                             @PathVariable @Parameter(description = "项目id") Long projectId,
                                                             @PathVariable @Parameter(description = "scheduleId") Long id) {
        ContextDTO context = getContext();
        Map<String, Object> command = new HashMap<>();
        command.put(BpmCode.EXCLUSIVE_GATEWAY_RESULT, BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT);

        externalInspectionScheduleService.deleteExternalInspectionSchedule(context, orgId, projectId, id, command);
        return new JsonResponseBody();
    }

    @RequestMapping(method = POST, value = "external-inspection-schedule/{scheduleId}")
    @Operation(summary = "修改报检申请记录", description = "修改报检申请记录。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody modifyExternalInspectionSchedule(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                             @PathVariable @Parameter(description = "项目id") Long projectId, @PathVariable @Parameter(description = "scheduleId") Long scheduleId,
                                                             @RequestBody @Parameter(description = "报检信息") ExInspScheduleDTO scheduleDTO) {
        externalInspectionScheduleService.modifyExternalInspectionSchedule(orgId, projectId, scheduleId, scheduleDTO);
        return new JsonResponseBody();
    }

    @RequestMapping(method = POST, value = "external-inspection-apply")
    @Operation(summary = "外检报检申请", description = "外检报检申请。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody externalInspectionApply(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                    @PathVariable @Parameter(description = "项目id") Long projectId,
                                                    @RequestBody @Parameter(description = "报检申请信息") ExInspApplyDTO applyDTO) {
        logger.info("Async report apply Start");
        Set<String> types = new HashSet<>();
        types.add("PWHT");
        types.add("PMI");
        String process = applyDTO.getExternalInspectionApplyList().get(0).getProcessStageName();
        process = process.substring(process.indexOf("-") + 1);
        if (!types.contains(process) && applyDTO.getCoordinateCategory() == null) {
            logger.error("COORDINATE CATEGORY IS NULL");

        }
        applyDTO.setTaskType(BpmTaskType.EX_INSP_APPLY.name());
        if (!types.contains(process)) {
            Map<String, Object> command = applyDTO.getCommand();
            if (MapUtils.isEmpty(command)) {
                command = new HashMap<>();
                command.put("RESULT", applyDTO.getCoordinateCategory());
            }
            applyDTO.setCommand(command);
        }
        externalInspectionApplyService.externalInspectionApply(getContext(), orgId, projectId, getContext().getOperator(), applyDTO);
        logger.info("Async report apply End");
        return new JsonResponseBody();
    }

    @RequestMapping(method = POST, value = "external-inspection-mail-preview")
    @Operation(summary = "报检邮件预览申请", description = "报检申请邮件。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<ExInspMailApplyPreviewDTO> externalInspectionMailPreview(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "报检申请信息") ExInspMailApplyPreviewDTO mailApplyDTO) {
        Long operator = getContext().getOperator().getId();
        return new JsonObjectResponseBody<>(externalInspectionMailService
            .externalInspectionMailPreview(orgId, projectId, operator, mailApplyDTO));
    }

    @RequestMapping(method = POST, value = "external-inspection-mail-send")
    @Operation(summary = "报检邮件发送", description = "报检申请邮件发送。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<ExInspMailApplyPreviewDTO> externalInspectionMailSend(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "报检申请信息") ExInspMailApplyPreviewDTO mailApplyDTO) {
        Long operator = getContext().getOperator().getId();
        Map<String, Object> command = new HashMap<>();
        command.put(BpmCode.EXCLUSIVE_GATEWAY_RESULT, BpmCode.EXCLUSIVE_GATEWAY_RESULT_ACCEPT);
        mailApplyDTO.setTaskType(BpmTaskType.EX_INSP_APPLY_MAIL.name());
        ContextDTO contextDTO = getContext();


        if (!contextDTO.isContextSet()) {
            String authorization = contextDTO.getAuthorization();

            final RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(contextDTO.getRequest(), authorization),
                null
            );
            RequestContextHolder.setRequestAttributes(attributes, true);
            contextDTO.setContextSet(true);
        }

        List<Long> exInspScheduleIds = mailApplyDTO.getExternalInspectionApplyScheduleIds();


        bpmExInspScheduleRepository.setRunningByScheduleIds(ReportStatus.RUNNING, exInspScheduleIds);
        logger.info("SEND EX INSP EMAIL " + StringUtils.toJSON(mailApplyDTO.getExternalInspectionApplyScheduleIds()));



        Project project = projectService.get(orgId, projectId);
        batchTaskService.runConstructTaskExecutor(
            null,
            project,
            BatchTaskCode.EXTERNAL_INSPECTION_APPLY,
            false,
            contextDTO,
            batchTask -> {
                externalInspectionMailService.externalInspectionMailSend(orgId, projectId, operator, mailApplyDTO, contextDTO, command);
                return new BatchResultDTO();
            }
        );







        return new JsonObjectResponseBody<>();

    }

    @RequestMapping(method = GET, value = "external-inspection-upload-histories")
    @Operation(summary = "外检报告上传历史", description = "查询外检报告上传历史记录")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmExInspUploadHistory> externalInspectionUploadHistories(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        ExInspUploadHistorySearchDTO pageDTO) {
        Long operatorId = getContext().getOperator().getId();
        return new JsonListResponseBody<>(
            externalInspectionReportService.externalInspectionUploadHistories(orgId, projectId, pageDTO, operatorId));
    }

    @Override
    @Operation(summary = "外检文档报告结果确认", description = "上传文档文件结果确认")
    @RequestMapping(method = POST, value = "external-inspection-confirm", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<ExInspReportHandleSearchDTO> externalInspectionConfirm(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId, @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody ExInspReportHandleSearchDTO exInspReportUploadDTO) {
        logger.info("Async report inspection confirm Start");
        externalInspectionUploadReportService.externalInspectionConfirm(
            orgId,
            projectId,
            getContext(),
            exInspReportUploadDTO
        );
        logger.info("Async report inspection confirm End");
        return new JsonObjectResponseBody<>(
            new ExInspReportHandleSearchDTO());
    }

    @Override
    @Operation(description = "下载文件")
    @RequestMapping(method = POST, value = "tasks/external-inspection/tempImagePdf")
    @WithPrivilege
    public JsonObjectResponseBody<TaskTemporaryFileDTO> tempImagePdf(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                                                     @PathVariable @Parameter(description = "项目id") Long projectId,
                                                                     @RequestBody TodoTempPdfDTO todoTempPdfDTO) {
        try {
            List<String> tempFiles = todoTempPdfDTO.getTempFiles();
            List<String> targetFiles = new ArrayList<>();
            for (String tempFile : tempFiles) {
                targetFiles.add(temporaryDir + tempFile);
            }
            String file = PdfUtils.mergePdfToImage(targetFiles, temporaryDir);
            FileES fileEs = todoTaskBaseService.uploadReportTodocs(orgId, projectId, file);
            if (fileEs != null) {
                TaskTemporaryFileDTO tempFileDTO = new TaskTemporaryFileDTO();
                tempFileDTO.setName(fileEs.getId());
                return new JsonObjectResponseBody<>(tempFileDTO);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return new JsonObjectResponseBody<>(new TaskTemporaryFileDTO());
    }

    @RequestMapping(method = GET, value = "external-inspection-views")
    @Operation(summary = "查询有意见的外检列表", description = "查询有意见的外检列表")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<QCReport> externalInspectionViews(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        PageDTO pageDTO, ExInspViewCriteriaDTO criteriaDTO) {
        OperatorDTO operator = getContext().getOperator();
        criteriaDTO.setOperator(operator.getId());
        return new JsonListResponseBody<>(
            externalInspectionTaskService.externalInspectionViews(orgId, projectId, pageDTO, criteriaDTO));
    }

    @RequestMapping(method = GET, value = "external-inspection-views/{id}/detail")
    @Operation(summary = "查询有意见的外检列表", description = "查询有意见的外检列表")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<ExInspReportHandleDTO> externalInspectionViewsDetail(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "id") Long id) {
        return new JsonListResponseBody<>(
            externalInspectionTaskService.externalInspectionViewsDetail(orgId, projectId, id));
    }

    @RequestMapping(method = POST, value = "external-inspection-re-handle")
    @Operation(summary = "外检再处理", description = "外检再处理")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<ExInspReportHandleSearchDTO> externalInspectionReHandle(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody ExInspReportHandleSearchDTO uploadDTO) {
        logger.info("Async Report reHandle Start");
        externalInspectionUploadReportService.externalInspectionReHandle(
            orgId,
            projectId
            , getContext(),
            uploadDTO
        );
        logger.info("Async Report reHandle Finished");
        return new JsonObjectResponseBody<>(uploadDTO);
    }

    @RequestMapping(method = GET, value = "external-inspection-reports")
    @Operation(summary = "已上传的报检记录", description = "已上传的报检记录")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<ExInspReportsDTO> externalInspectionReports(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        PageDTO pageDTO, ExInspReportCriteriaDTO criteriaDTO) {
        OperatorDTO operator = getContext().getOperator();
        criteriaDTO.setOperator(operator.getId());
        return new JsonListResponseBody<>(externalInspectionReportService.externalInspectionReports(orgId,
            projectId, pageDTO, criteriaDTO));
    }

    @RequestMapping(method = GET, value = "tasks/external-inspection-detail/series-nos/{seriesNo}")
    @Operation(summary = "查询外检实体信息", description = "查询外检实体信息")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<ExInspEntityInfoDTO> getExternalInspectionEntityInfo(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "seriesNo") String seriesNo) {
        return new JsonListResponseBody<>(
            externalInspectionTaskBaseService.getExternalInspectionEntityInfo(
                orgId, projectId, seriesNo));
    }

    @RequestMapping(method = GET, value = "tasks/external-inspection-mail")
    @Operation(summary = "查询外检报验邮件历史记录", description = "查询外检报验邮件历史记录")
    @SetUserInfo
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmExInspMailHistory> getExternalInspectionEmail(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        ExInspTaskEmailDTO criteriaDTO) {
        return new JsonListResponseBody<>(
            externalInspectionMailService.getExternalInspectionEmail(orgId, projectId, criteriaDTO)
        );
    }

    @RequestMapping(method = GET, value = "tasks/external-inspection-mail/operator")
    @Operation(summary = "查询外检报验邮件记录操作人", description = "查询外检报验邮件记录操作人")
    @SetUserInfo
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<ExInspTaskEmailDTO> getEmailOperatorList(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId) {
        return new JsonListResponseBody<>(
            externalInspectionMailService.getEmailOperatorList(orgId, projectId)
        );
    }

    @RequestMapping(method = GET, value = "tasks/external-inspection-mail/{id}")
    @Operation(summary = "查询外检报验邮件历史记录详情", description = "查询外检报验邮件历史记录详情")
    @SetUserInfo
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<BpmExInspMailHistory> getExternalInspectionEmail(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "id") Long id) {
        return new JsonObjectResponseBody<>(
            externalInspectionMailService.getExternalInspectionEmail(orgId, projectId, id)
        );
    }

    @Override
    @Operation(summary = "二次上传文档报告PDF", description = "二次上传文档报告PDF")
    @RequestMapping(method = POST, value = "second-upload-report", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<BpmExInspConfirmResponseDTO> secondUploadReport(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody DrawingUploadDTO uploadDTO) {
        logger.info("Async Report second upload Start");
        externalInspectionUploadReportService.uploadReport(
            orgId,
            projectId,
            uploadDTO,
            getContext().getOperator(),
            true,
            getContext()
        );
        logger.info("Async Report second upload Finished");
        return new JsonObjectResponseBody<>(
        );
    }

    @RequestMapping(method = POST, value = "tasks/external-inspection-mail/{id}/send")
    @Operation(summary = "外检报验邮件发送", description = "外检报验邮件发送")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody sendExternalInspectionEmail(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "id") Long id
    ) {
        externalInspectionMailService.sendExternalInspectionMail(
            orgId,
            projectId,
            id
        );
        return new JsonResponseBody();
    }

    @RequestMapping(method = POST, value = "external-inspection-resign-handle")
    @Operation(summary = "重签处理", description = "重签处理")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<ExInspReportHandleSearchDTO> externalInspectionResignHandle(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody ExInspReportHandleSearchDTO uploadDTO) {


        uploadDTO.setTaskType(BpmTaskType.EX_INSP_REHANDLE_REPORT.name());
        Map<String, Object> data = new HashMap<>();
        data.put("orgId", orgId);
        data.put("projectId", projectId);
        ContextDTO contextDTO = getContext();
        todoTaskDispatchService.batchExec(contextDTO, data, uploadDTO);
        return new JsonObjectResponseBody<>(uploadDTO);
    }

    @RequestMapping(method = GET, value = "external-inspection-schedule/{scheduleId}/qc-reports")
    @Operation(summary = "获取报检申请记录文件详情", description = "获取报检申请记录文件详情。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<QCReport> getExternalInspectionScheduleReport(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "scheduleId") Long scheduleId) {
        return new JsonListResponseBody<>(
            externalInspectionScheduleService.getExternalInspectionScheduleReport(orgId, projectId, scheduleId)
        );
    }

    @RequestMapping(method = GET, value = "external-inspection-handle-history")
    @Operation(summary = "获取项目中所有外检处理", description = "获取项目中所有外检处理。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<ExInspActInstHandleHistory> getExInspActInstHandleHistory(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        ExInspActInstHandleHistoryDTO exInspActInstHandleHistoryDTO
    ) {
        return new JsonListResponseBody<>(
            externalInspectionScheduleService.getExInspActInstHandleHistory(
                orgId,
                projectId,
                exInspActInstHandleHistoryDTO)
        );
    }

}
