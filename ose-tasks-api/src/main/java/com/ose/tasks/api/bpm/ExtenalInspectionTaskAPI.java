package com.ose.tasks.api.bpm;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.report.ExInspActInstHandleHistory;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.bpm.*;
import org.springframework.web.bind.annotation.*;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.ExInspApplyDTO;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.entity.bpm.BpmExInspMailHistory;
import com.ose.tasks.entity.bpm.BpmExInspApply;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * 任务管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface ExtenalInspectionTaskAPI {

    /**
     * 上传报告文件
     */
    @RequestMapping(method = POST, value = "upload-report")
    JsonObjectResponseBody<BpmExInspConfirmResponseDTO> uploadReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingUploadDTO uploadDTO
    );

    /**
     * 确认上传报告文件
     */
    @RequestMapping(method = POST, value = "upload-report-confirm")
    JsonObjectResponseBody<BpmExInspConfirmResponseDTO> confirmUploadReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody BpmExInspConfirmsDTO confirmDTO
    );

    /**
     * 确认上传的NDT报告文件
     *
     * @return
     */
    @RequestMapping(method = POST, value = "upload-ndt-report-confirm", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    JsonObjectResponseBody<BpmExInspConfirmResponseDTO> confirmNdtUploadReport(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody BpmExInspConfirmsDTO confirmDTO
    );

    /**
     * 确认上传的NDT报告文件
     *
     * @return
     */
    @RequestMapping(method = POST, value = "upload-pmi-report-confirm", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    JsonObjectResponseBody<BpmExInspConfirmResponseDTO> confirmPmiUploadReport(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody BpmExInspConfirmsDTO confirmDTO
    );

    @RequestMapping(method = POST, value = "skip-upload-report")
    JsonResponseBody skipUpload(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingUploadDTO skipDTO
    );

    /**
     * 上传文件确认信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    @RequestMapping(method = GET, value = "external-inspection-upload-histories/{id}/confirm-details")
    public JsonListResponseBody<BpmExInspConfirm> uploadFileConfirmDetails(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "historyId") Long id,
        PageDTO pageDTO
    );

    /**
     * 待办任务列表
     *
     * @return 实体信息
     */
    @RequestMapping(method = GET, value = "tasks/external-inspection-schedule")
    @ResponseStatus(OK)
    JsonListResponseBody<TodoTaskDTO> searchTodoSchedule(@PathVariable("orgId") Long orgId,
                                                         @PathVariable("projectId") Long projectId, PageDTO pageDTO);

    /**
     * 待办任务列表
     *
     * @return 实体信息
     */
    @RequestMapping(method = GET, value = "tasks/external-inspection-apply/filter-condition")
    @ResponseStatus(OK)
    JsonObjectResponseBody<ExInspApplyFilterConditionDTO> searchTodoApplyFilterCondition(
        @PathVariable("orgId") Long orgId, @PathVariable("projectId") Long projectId);

    /**
     * 待办任务列表
     *
     * @param orgId       待办任务列表查询数据类
     * @param projectId   分页
     * @param criteriaDTO
     * @return 实体信息
     */
    @RequestMapping(method = GET, value = "tasks/external-inspection-apply/")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmExInspApply> searchTodoApply(@PathVariable("orgId") Long orgId,
                                                         @PathVariable("projectId") Long projectId, ExInspApplyCriteriaDTO criteriaDTO);

    /**
     * 创建报检申请
     */
    @RequestMapping(method = POST, value = "external-inspection-schedule")
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<BpmExInspSchedule> createExternalInspectionSchedule(
        @PathVariable("orgId") Long orgId, @PathVariable("projectId") Long projectId,
        @RequestBody ExInspScheduleDTO scheduleDTO

    );

    /**
     * 获取报检申请列表
     */
    @RequestMapping(method = GET, value = "external-inspection-schedule")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmExInspSchedule> getExternalInspectionScheduleList(
        @PathVariable("orgId") Long orgId, @PathVariable("projectId") Long projectId,
        ExInspScheduleCriteriaDTO criteriaDTO, PageDTO page);

    /**
     * 获取报检申请详细信息
     *
     * @return 工序分类
     */
    @RequestMapping(method = GET, value = "external-inspection-schedule/{id}")
    @ResponseStatus(OK)
    JsonObjectResponseBody<BpmExInspSchedule> getExternalInspectionSchedule(
        @PathVariable("orgId") Long orgId, @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id);

    /**
     * 获取报检申请详细信息
     *
     * @return 工序分类
     */
    @RequestMapping(method = GET, value = "external-inspection-schedule/{id}/details")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmExInspScheduleDetail> getExternalInspectionScheduleDetail(
        @PathVariable("orgId") Long orgId, @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id);

    /**
     * 删除报检申请
     */
    @RequestMapping(method = DELETE, value = "external-inspection-schedule/{id}", produces = APPLICATION_JSON_VALUE)
    JsonResponseBody deleteExternalInspectionSchedule(@PathVariable("orgId") Long orgId,
                                                      @PathVariable("projectId") Long projectId, @PathVariable("id") Long id);

    /**
     * 编辑报检申请
     */
    @RequestMapping(method = POST, value = "external-inspection-schedule/{scheduleId}", produces = APPLICATION_JSON_VALUE)
    JsonResponseBody modifyExternalInspectionSchedule(@PathVariable("orgId") Long orgId,
                                                      @PathVariable("projectId") Long projectId, @PathVariable("scheduleId") Long scheduleId,
                                                      @RequestBody ExInspScheduleDTO scheduleDTO);

    /**
     * 报检申请
     */
    @RequestMapping(method = POST, value = "external-inspection-apply", produces = APPLICATION_JSON_VALUE)
    JsonResponseBody externalInspectionApply(@PathVariable("orgId") Long orgId,
                                             @PathVariable("projectId") Long projectId, @RequestBody ExInspApplyDTO applyDTO);

    /**
     * 报检申请邮件预览
     */
    @RequestMapping(method = POST, value = "external-inspection-mail-priview")
    public JsonResponseBody externalInspectionMailPreview(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                          @PathVariable @Parameter(description = "项目id") Long projectId,
                                                          @RequestBody @Parameter(description = "报检申请信息") ExInspMailApplyPreviewDTO mailApplyDTO);

    /**
     * 报检申请邮件发送
     */
    @RequestMapping(method = POST, value = "external-inspection-mail-send")
    public JsonResponseBody externalInspectionMailSend(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                       @PathVariable @Parameter(description = "项目id") Long projectId,
                                                       @RequestBody @Parameter(description = "报检申请信息") ExInspMailApplyPreviewDTO mailApplyDTO);

    /**
     * 外检上传历史记录
     */
    @RequestMapping(method = GET, value = "external-inspection-upload-histories")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmExInspUploadHistory> externalInspectionUploadHistories(
        @PathVariable("orgId") Long orgId, @PathVariable("projectId") Long projectId, ExInspUploadHistorySearchDTO pageDTO);

    /**
     * 外检报告上传后确认
     */
    @RequestMapping(method = POST, value = "external-inspection-confirm")
    JsonObjectResponseBody<ExInspReportHandleSearchDTO> externalInspectionConfirm(@PathVariable("orgId") Long orgId,
                                                                                  @PathVariable("projectId") Long projectId,
                                                                                  @RequestBody ExInspReportHandleSearchDTO uploadDTO);


    /**
     * pdf文件临时merge
     */
    @RequestMapping(method = POST, value = "tasks/external-inspection/tempImagePdf")
    @ResponseStatus(OK)
    JsonObjectResponseBody<TaskTemporaryFileDTO> tempImagePdf(
        @PathVariable("orgId") Long orgId, @PathVariable("projectId") Long projectId, @RequestBody TodoTempPdfDTO todoTempPdfDTO);

    @RequestMapping(method = GET, value = "external-inspection-views")
    JsonListResponseBody<QCReport> externalInspectionViews(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO pageDTO, ExInspViewCriteriaDTO criteriaDTO);

    @RequestMapping(method = POST, value = "external-inspection-re-handle")
    JsonObjectResponseBody<ExInspReportHandleSearchDTO> externalInspectionReHandle(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ExInspReportHandleSearchDTO uploadDTO);

    @RequestMapping(method = GET, value = "external-inspection-views/{id}/detail")
    JsonListResponseBody<ExInspReportHandleDTO> externalInspectionViewsDetail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id);

    @RequestMapping(method = GET, value = "external-inspection-reports")
    JsonListResponseBody<ExInspReportsDTO> externalInspectionReports(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO pageDTO, ExInspReportCriteriaDTO criteriaDTO);


    @RequestMapping(method = GET, value = "tasks/external-inspection-detail/series-nos/{seriesNo}")
    @ResponseStatus(OK)
    JsonListResponseBody<ExInspEntityInfoDTO> getExternalInspectionEntityInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("seriesNo") String seriesNo);

    @RequestMapping(method = GET, value = "tasks/external-inspection-mail/operator")
    @ResponseStatus(OK)
    JsonListResponseBody<ExInspTaskEmailDTO> getEmailOperatorList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    @RequestMapping(method = GET, value = "tasks/external-inspection-mail")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmExInspMailHistory> getExternalInspectionEmail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        ExInspTaskEmailDTO criteriaDTO
    );

    @RequestMapping(method = GET, value = "tasks/external-inspection-mail/{id}")
    @ResponseStatus(OK)
    JsonObjectResponseBody<BpmExInspMailHistory> getExternalInspectionEmail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    @RequestMapping(method = GET, value = "tasks/external-inspection-mail/{id}/send")
    @ResponseStatus(OK)
    JsonResponseBody sendExternalInspectionEmail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    @RequestMapping(method = POST, value = "second-upload-report")
    JsonObjectResponseBody<BpmExInspConfirmResponseDTO> secondUploadReport(@PathVariable("orgId") Long orgId,
                                                                           @PathVariable("projectId") Long projectId, @RequestBody DrawingUploadDTO uploadDTO);


    @RequestMapping(method = POST, value = "external-inspection-resign-handle")
    JsonObjectResponseBody<ExInspReportHandleSearchDTO> externalInspectionResignHandle(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ExInspReportHandleSearchDTO uploadDTO);

    @RequestMapping(method = GET, value = "external-inspection-schedule/{scheduleId}/qc-reports")
    @ResponseStatus(OK)
    JsonListResponseBody<QCReport> getExternalInspectionScheduleReport(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "scheduleId") Long scheduleId);

    @RequestMapping(method = GET, value = "external-inspection-handle-history")
    @ResponseStatus(OK)
    JsonListResponseBody<ExInspActInstHandleHistory> getExInspActInstHandleHistory(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        ExInspActInstHandleHistoryDTO exInspActInstHandleHistoryDTO);


}
