package com.ose.tasks.controller.bpm.drawing;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.drawing.WorkHourAPI;
import com.ose.tasks.domain.model.service.drawing.DrawingWorkHourInterface;
import com.ose.tasks.dto.*;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.dto.timesheet.SelectDataDTO;
import com.ose.tasks.entity.drawing.AttendanceRecord;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingRecord;
import com.ose.tasks.entity.drawing.DrawingWorkHour;
import com.ose.tasks.entity.holiday.HolidayData;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "图纸校审接口")
@RestController
public class WorkHourController extends BaseController implements WorkHourAPI {

    private final ConstructionChangeController constructionChangeController;
    private DrawingWorkHourInterface drawingWorkHourService;

    /**
     * 构造方法
     */
    @Autowired
    public WorkHourController(
        DrawingWorkHourInterface drawingWorkHourService,
        ConstructionChangeController constructionChangeController) {
        this.drawingWorkHourService = drawingWorkHourService;
        this.constructionChangeController = constructionChangeController;
    }


    @Override
    @Operation(description = "创建图纸工时实体")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-work-hour/create",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<DrawingWorkHour> create(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody DrawingWorkHourDTO dto
    ) {
        return new JsonObjectResponseBody<>(
            getContext(),
            drawingWorkHourService.create(orgId, projectId, dto)
        );

    }


    @Override
    @Operation(
        summary = "查询检查单列表",
        description = "查询检查单列表，或使用过滤条件（名称/编号）进行查询。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-work-hour"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<DrawingWorkHour> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        DrawingWorkHourDTO dto,
        PageDTO page) {

        return (new JsonListResponseBody<>(
            getContext(),
            drawingWorkHourService.search(orgId, projectId, dto, page)
        ));
    }

    @Override
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-examine/adopt/{drawingExamineId}"
    )
    @WithPrivilege
    public JsonResponseBody adopt(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "图纸记录ID") Long drawingExamineId) {

        drawingWorkHourService.adopt(orgId, projectId, drawingExamineId);

        return new JsonResponseBody();
    }


    @Override
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-examine/reject/{drawingExamineId}"
    )
    @WithPrivilege
    public JsonResponseBody reject(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "图纸记录ID") Long drawingExamineId) {

        drawingWorkHourService.reject(orgId, projectId, drawingExamineId);

        return new JsonResponseBody();
    }

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-record-list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(description = "图纸填报历史记录查询")
    public JsonListResponseBody<DrawingRecord> getList(
        @PathVariable("orgId") @Parameter(description = "组织Id") Long orgId,
        @PathVariable("projectId") @Parameter(description = "项目Id") Long projectId,
        @RequestBody @Parameter(description = "查询条件") DrawingRecordCriteriaDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>(
            drawingWorkHourService.getList(orgId, projectId, criteriaDTO)
        );
    }

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawings/{drawingId}/drawing-update",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updateDrawing(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
        @RequestBody DrawingCriteriaDTO criteriaDTO
    ) {
        ContextDTO contextDTO = getContext();
        drawingWorkHourService.updateDrawing(orgId, projectId, drawingId, criteriaDTO, contextDTO);
        return new JsonResponseBody();
    }

    @WithPrivilege
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-create",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody createDrawingRecord(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingRecordCriteriaDTO criteriaDTO
    ) {
        drawingWorkHourService.create(orgId, projectId, criteriaDTO, getContext());
        return new JsonResponseBody();
    }

    @WithPrivilege
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-update/{drawingRecordId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonResponseBody updateDrawingRecord(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingRecordId") Long drawingRecordId,
        @RequestBody DrawingRecordCriteriaDTO criteriaDTO
    ) {
        drawingWorkHourService.update(orgId, projectId, drawingRecordId, criteriaDTO, getContext());
        return new JsonResponseBody();
    }

    @WithPrivilege
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-record-delete/{drawingRecordId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonResponseBody deleteDrawingRecord(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingRecordId") Long drawingRecordId
    ) {
        drawingWorkHourService.delete(orgId, projectId, drawingRecordId, getContext());
        return new JsonResponseBody();
    }

    @RequestMapping(
        method = POST,
        value = "/users/{userId}/global-manHour",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(description = "查询全局的工时填报列表")
    public JsonObjectResponseBody<ManHourDTO> getGlobalManHour(
        @PathVariable("userId") Long userId,
        @RequestBody @Parameter(description = "查询条件") ManHourCriteriaDTO criteriaDTO
    ) {
        return new JsonObjectResponseBody<>(
            drawingWorkHourService.getGlobalManHour(userId, criteriaDTO)
        );
    }

    @RequestMapping(
        method = POST,
        value = "/users/{userId}/global-manHour/detail",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(description = "查询工时填报详情")
    public JsonListResponseBody<DrawingRecord> getGlobalManHourDetail(
        @PathVariable("userId") Long userId,
        @RequestBody @Parameter(description = "查询条件") ManHourDetailDTO manHourDetailDTO
    ) {
        return new JsonListResponseBody<>(
            getContext(),
            drawingWorkHourService.getGlobalManHourDetail(userId, manHourDetailDTO)
        );
    }

    @RequestMapping(
        method = GET,
        value = "/users/{userId}/drawing-record-list/{workHourDate}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(description = "查询全局的工时填报列表")
    @Override
    public JsonListResponseBody<DrawingRecord> getDrawingRecordByUserAndDate(
        @PathVariable("userId") Long userId,
        @PathVariable("workHourDate") String workHourDate
    ) {
        return new JsonListResponseBody<>(
            drawingWorkHourService.getDrawingRecordListByUserAndDate(userId, workHourDate)
        );
    }

    @RequestMapping(
        method = GET,
        value = "/drawingRecordId/{drawingRecordId}/drawing-record",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(description = "根据id查询图纸记录")
    @Override
    public JsonObjectResponseBody<DrawingRecord> getDrawingRecordById(
        @PathVariable("drawingRecordId") Long drawingRecordId
    ) {
        return new JsonObjectResponseBody<>(
            drawingWorkHourService.getDrawingRecordById(drawingRecordId)
        );
    }

    @RequestMapping(
        method = GET,
        value = "/users/{userId}/project-list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonListResponseBody<ManHourDTO> getProjectListByUser(
        @PathVariable("userId") Long userId
    ) {
        return new JsonListResponseBody<>(
            drawingWorkHourService.getProjectList(userId)
        );
    }

    @RequestMapping(
        method = GET,
        value = "/projects/{projectId}/project-drawing-data/{userId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonListResponseBody<Drawing> getProjectDrawingData(
        @PathVariable("projectId") Long projectId,
        @PathVariable("userId") Long userId
    ) {
        return new JsonListResponseBody<>(
            drawingWorkHourService.getProjectDrawingData(projectId, userId)
        );
    }

    @RequestMapping(
        method = GET,
        value = "/drawings/{drawingId}/project-stage-data/{userId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonListResponseBody<SelectDataDTO> getProjectStageData(
        @PathVariable("drawingId") Long drawingId,
        @PathVariable("userId") Long userId
    ) {
        return new JsonListResponseBody<>(
            drawingWorkHourService.getProjectStageData(drawingId, userId)
        );
    }

    @RequestMapping(
        method = GET,
        value = "/drawings/{drawingId}/processes/{processId}/{userId}/version-list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonListResponseBody<SelectDataDTO> getVersionDataList(
        @PathVariable("drawingId") Long drawingId,
        @PathVariable("processId") Long processId,
        @PathVariable("userId") Long userId
    ) {
        return new JsonListResponseBody<>(
            drawingWorkHourService.getVersionList(drawingId, processId, userId)
        );
    }

    @RequestMapping(
        method = GET,
        value = "/drawings/{drawingId}/version/{version}/{userId}/task-node-list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonListResponseBody<SelectDataDTO> getTaskNodes(
        @PathVariable("drawingId") Long drawingId,
        @PathVariable("version") String version,
        @PathVariable("userId") Long userId
    ) {
        return new JsonListResponseBody<>(
            drawingWorkHourService.getTaskNodes(drawingId, version, userId)
        );
    }

    @RequestMapping(
        method = GET,
        value = "/global-manHour/download/getYears",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonObjectResponseBody<ManHourDTO> getValuableYears(
    ) {
        return new JsonObjectResponseBody<>(
            drawingWorkHourService.getValuableYears()
        );
    }

    @RequestMapping(
        method = GET,
        value = "/global-manHour/download/getWeeks",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonObjectResponseBody<ManHourDTO> getValuableWeeks(
    ) {
        return new JsonObjectResponseBody<>(
            drawingWorkHourService.getValuableWeeks()
        );
    }

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-stages",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonObjectResponseBody<ManHourDTO> getStageList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    ) {
        return new JsonObjectResponseBody<>(
            drawingWorkHourService.getStageList(orgId, projectId)
        );
    }

    /**
     * @param userId
     * @param criteriaDTO
     * @throws IOException
     */
    @RequestMapping(
        method = GET,
        value = "/userId/{userId}/global-manHour/download"
    )
    @Operation(description = "按条件下载")
    @WithPrivilege
    @Override
    public void download(
        @PathVariable("userId") Long userId,
        ManHourCriteriaDTO criteriaDTO
    ) throws IOException {
        final OperatorDTO operator = getContext().getOperator();
        File excel = drawingWorkHourService.saveDownloadFile(userId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-global-manHour.xlsx\""
            );

            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("下载文件出错");
        }

        response.flushBuffer();
    }


    @RequestMapping(
        method = POST,
        value = "/attendance-manHour",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(description = "查询全局的考勤工时")
    public JsonListResponseBody<AttendanceDataDTO> getAttendanceHour(
        @RequestBody @Parameter(description = "查询条件") ManHourCriteriaDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>(
            drawingWorkHourService.getAttendanceHour(criteriaDTO)
        );
    }

    @RequestMapping(
        method = POST,
        value = "/attendance-manHour/weekly",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonObjectResponseBody<AttendanceFilterDTO> filterWeekly(
        @RequestBody @Parameter(description = "查询条件") ManHourCriteriaDTO criteriaDTO
    ) {
        return new JsonObjectResponseBody<>(
            drawingWorkHourService.filterWeekly(criteriaDTO)
        );
    }

    /**
     * @param criteriaDTO
     * @throws IOException
     */
    @RequestMapping(
        method = GET,
        value = "/attendance-manHour/download"
    )
    @Operation(description = "按条件下载")
    @WithPrivilege
    @Override
    public void attendanceDownload(
        ManHourExportCriteriaDTO criteriaDTO
    ) throws IOException, ParseException {
        final OperatorDTO operator = getContext().getOperator();
        File excel = drawingWorkHourService.saveAttendanceDownloadFile(criteriaDTO, operator.getId());
        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-attendance-manHour.xlsx\""
            );

            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("下载文件出错");
        }

        response.flushBuffer();
    }

    @RequestMapping(
        method = GET,
        value = "/checkData-manHour/download"
    )
    @Operation(description = "按条件下载")
    @WithPrivilege
    @Override
    public void checkDataDownload(
        ReviewCriteriaDTO criteriaDTO
    ) throws Exception {
        final OperatorDTO operator = getContext().getOperator();
        File excel = drawingWorkHourService.saveCheckDataDownloadFile(criteriaDTO, operator.getId());
        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-checkData-manHour.xlsx\""
            );

            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("下载文件出错");
        }

        response.flushBuffer();
    }

    @RequestMapping(
        method = GET,
        value = "/weekly-manHour/download"
    )
    @Operation(description = "按条件下载")
    @WithPrivilege
    @Override
    public void weeklyDataDownload(
        ReviewCriteriaDTO criteriaDTO
    ) throws Exception {
        final OperatorDTO operator = getContext().getOperator();
        File excel = drawingWorkHourService.saveWeeklyDataDownloadFile(criteriaDTO, operator.getId());
        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-weekly-manHour.xlsx\""
            );

            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("下载文件出错");
        }

        response.flushBuffer();
    }


    @RequestMapping(
        method = GET,
        value = "/employeeData-manHour/list",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(description = "查询员工的考勤数据")
    public JsonListResponseBody<EmployeeDataDTO> getEmployeeDataList(
        ReviewCriteriaDTO criteriaDTO
    ) throws Exception {
        System.out.println("接口被调用");
        return new JsonListResponseBody<>(
            drawingWorkHourService.getEmployeeDataList(criteriaDTO)
        );
    }


    @RequestMapping(
        method = GET,
        value = "/employeeData-manHour/detail",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<EmployeeDataDTO> getEmployeeDataDetail(
        @Parameter(description = "用户id") String id ) throws Exception {
        // 根据 id 查询单个 EmployeeDataDTO 对象
        EmployeeDataDTO employeeDataDTO = drawingWorkHourService.getEmployeeDataDetail(id);
        if (employeeDataDTO == null) {
            throw new ResourceNotFoundException("No data found for the given user id: " + id);
        }
        return new JsonObjectResponseBody<>(getContext(), employeeDataDTO);
    }

    @RequestMapping(
        method = GET,
        value = "/attendance-manHour/filter",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonObjectResponseBody<AttendanceFilterDTO> filter() {
        return (new JsonObjectResponseBody<>(
            getContext(),
            drawingWorkHourService.filter()
        ));
    }

    @WithPrivilege
    @RequestMapping(
        method = POST,
        value = "/attendance-manHour/locked",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(description = "查询全局的考勤工时")
    public JsonResponseBody lockedAttendanceHour(
        @RequestBody ManHourCriteriaDTO criteriaDTO
    ) {
        ContextDTO contextDTO = getContext();
        drawingWorkHourService.locked(criteriaDTO, getContext());
        return new JsonResponseBody();
    }


    @Override
    @RequestMapping(
        method = GET,
        value = "/attendance-manHour/locked-list",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(description = "查询全局的考勤工时")
    public JsonListResponseBody<AttendanceRecord> getAttendanceLockedList(
        ManHourCriteriaDTO criteriaDTO
    ) {

        return new JsonListResponseBody<>(
            drawingWorkHourService.getAttendanceLockedList(criteriaDTO)
        );
    }

    @WithPrivilege
    @RequestMapping(
        method = DELETE,
        value = "/attendance-manHour/locked-list/delete/{lockedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonResponseBody deleteLocked(
        @PathVariable("lockedId") Long lockedId
    ) {
        drawingWorkHourService.deleteLocked(lockedId, getContext());
        return new JsonResponseBody();
    }

    @WithPrivilege
    @RequestMapping(
        method = DELETE,
        value = "/attendance-manHour/locked-list/batch-delete/{ids}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonResponseBody batchDeleteLocked(
        @PathVariable("ids") List<Long> ids
    ) {
        for (Long id : ids) {
            drawingWorkHourService.deleteLocked(id, getContext());
        }
        return new JsonResponseBody();
    }

    @RequestMapping(
        method = POST,
        value = "/user/{userId}/review/list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonListResponseBody<ReviewDataDTO> getList(
        @PathVariable("userId") Long userId,
        @RequestBody ReviewCriteriaDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>(
            drawingWorkHourService.searchReview(criteriaDTO, userId)
        );
    }

    @RequestMapping(
        method = POST,
        value = "/review/list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonListResponseBody<ReviewDataDTO> getTimesheetList(
        @RequestBody ReviewCriteriaDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>(
            drawingWorkHourService.searchTimesheetList(criteriaDTO, getContext())
        );
    }


    @RequestMapping(
        method = POST,
        value = "/hour/dashboard",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonListResponseBody<ReviewDataDTO> getDashboardData(
        @RequestBody HourDashboardCriteriaDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>(
            drawingWorkHourService.searchHour(criteriaDTO)
        );
    }


}
