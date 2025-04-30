package com.ose.tasks.api.drawing;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.*;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.dto.timesheet.SelectDataDTO;
import com.ose.tasks.entity.drawing.AttendanceRecord;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingRecord;
import com.ose.tasks.entity.drawing.DrawingWorkHour;
import com.ose.tasks.entity.holiday.HolidayData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

public interface WorkHourAPI {

    /*
     * 创建图纸
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-work-hour/create",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<DrawingWorkHour> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingWorkHourDTO dto
    );


    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-work-hour"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingWorkHour> search(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @RequestBody DrawingWorkHourDTO dto,
        PageDTO page
    );

    /**
     * 审核通过图纸
     *
     * @param orgId
     * @param projectId
     * @param drawingExamineId
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-examine/adopt/{drawingExamineId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonResponseBody adopt(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingExamineId") Long drawingExamineId
    );

    /**
     * 审核通过图纸
     *
     * @param orgId
     * @param projectId
     * @param drawingExamineId
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-examine/reject/{drawingExamineId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonResponseBody reject(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingExamineId") Long drawingExamineId
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-record-list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<DrawingRecord> getList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingRecordCriteriaDTO ConstructionChangeAPI
    );


    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawings/{drawingId}/drawing-update",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateDrawing(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
        @RequestBody DrawingCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-create",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody createDrawingRecord(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingRecordCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-update/{drawingRecordId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateDrawingRecord(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingRecordId") Long drawingRecordId,
        @RequestBody DrawingRecordCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-record-delete/{drawingRecordId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteDrawingRecord(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingRecordId") Long drawingRecordId
    );

    @RequestMapping(
        method = POST,
        value = "/users/{userId}/global-manHour",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ManHourDTO> getGlobalManHour(
        @PathVariable("userId") Long userId,
        @RequestBody ManHourCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = POST,
        value = "/users/{userId}/global-manHour/detail",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<DrawingRecord> getGlobalManHourDetail(
        @PathVariable("userId") Long userId,
        @RequestBody ManHourDetailDTO manHourDetailDTO
    );

    @RequestMapping(
        method = GET,
        value = "/users/{userId}/drawing-record-list/{workHourDate}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<DrawingRecord> getDrawingRecordByUserAndDate(
        @PathVariable("userId") Long userId,
        @PathVariable("workHourDate") String workHourDate
    );

    @RequestMapping(
        method = GET,
        value = "/drawingRecordId/{drawingRecordId}/drawing-record",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<DrawingRecord> getDrawingRecordById(
        @PathVariable("drawingRecordId") Long drawingRecordId
    );

    @RequestMapping(
        method = GET,
        value = "/users/{userId}/project-list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ManHourDTO> getProjectListByUser(
        @PathVariable("userId") Long userId
    );

    @RequestMapping(
        method = GET,
        value = "/projects/{projectId}/project-drawing-data/{userId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Drawing> getProjectDrawingData(
        @PathVariable("projectId") Long projectId,
        @PathVariable("userId") Long userId
    );

    @RequestMapping(
        method = GET,
        value = "/drawings/{drawingId}/project-stage-data/{userId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<SelectDataDTO> getProjectStageData(
        @PathVariable("drawingId") Long drawingId,
        @PathVariable("userId") Long userId
    );

    @RequestMapping(
        method = GET,
        value = "/drawings/{drawingId}/processes/{processId}/{userId}/version-list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<SelectDataDTO> getVersionDataList(
        @PathVariable("drawingId") Long drawingId,
        @PathVariable("processId") Long processId,
        @PathVariable("userId") Long userId
    );

    @RequestMapping(
        method = GET,
        value = "/drawings/{drawingId}/version/{version}/{userId}/task-node-list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<SelectDataDTO> getTaskNodes(
        @PathVariable("drawingId") Long drawingId,
        @PathVariable("version") String version,
        @PathVariable("userId") Long userId
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-stages",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ManHourDTO> getStageList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    @RequestMapping(
        method = GET,
        value = "/userId/{userId}/global-manHour/download"
    )
    void download(
        @PathVariable("userId") Long userId,
        ManHourCriteriaDTO criteriaDTO
    ) throws IOException;


    @RequestMapping(
        method = POST,
        value = "/user/{userId}/review/list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReviewDataDTO> getList(
        @PathVariable("userId") Long userId,
        @RequestBody ReviewCriteriaDTO criteriaDTO
    );


    @RequestMapping(
        method = POST,
        value = "/timesheet/list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReviewDataDTO> getTimesheetList(
        @RequestBody ReviewCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        value = "/checkData-manHour/download"
    )
    void checkDataDownload(
        ReviewCriteriaDTO criteriaDTO
    ) throws Exception;

    @RequestMapping(
        method = GET,
        value = "/weekly-manHour/download"
    )
    @Operation(description = "按条件下载")
    @WithPrivilege
    void weeklyDataDownload(
        ReviewCriteriaDTO criteriaDTO
    ) throws Exception;

    @RequestMapping(
        method = GET,
        value = "/employeeData-manHour/list",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<EmployeeDataDTO> getEmployeeDataList(
        ReviewCriteriaDTO criteriaDTO
    )throws Exception;

    @RequestMapping(
        method = GET,
        value = "/employeeData-manHour/detail",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<EmployeeDataDTO> getEmployeeDataDetail(
        @Parameter(description = "用户id") String id ) throws Exception;



    @RequestMapping(
        method = POST,
        value = "/hour/dashboard",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReviewDataDTO> getDashboardData(
        @RequestBody HourDashboardCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        value = "/global-manHour/download/getYears"
    )
    JsonObjectResponseBody<ManHourDTO> getValuableYears() throws IOException;

    @RequestMapping(
        method = GET,
        value = "/global-manHour/download/getWeeks"
    )
    JsonObjectResponseBody<ManHourDTO> getValuableWeeks() throws IOException;


    @RequestMapping(
        method = POST,
        value = "/attendance-manHour",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<AttendanceDataDTO> getAttendanceHour(
        @RequestBody ManHourCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        value = "/attendance-manHour/download"
    )
    void attendanceDownload(
        ManHourExportCriteriaDTO criteriaDTO
    ) throws IOException, ParseException;

    @RequestMapping(
        method = GET,
        value = "/attendance-manHour/filter",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<AttendanceFilterDTO> filter();

    @RequestMapping(
        method = POST,
        value = "/attendance-manHour/weekly",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<AttendanceFilterDTO> filterWeekly(
        @RequestBody ManHourCriteriaDTO criteriaDTO
    );


    @RequestMapping(
        method = POST,
        value = "/attendance-manHour/locked",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody lockedAttendanceHour(
        @RequestBody ManHourCriteriaDTO criteriaDTO
    );


    @RequestMapping(
        method = GET,
        value = "/attendance-manHour/locked-list",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<AttendanceRecord> getAttendanceLockedList(
        ManHourCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = POST,
        value = "/attendance-manHour/locked-list/delete/{lockedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteLocked(
        @PathVariable("lockedId") Long lockedId
    );

    @RequestMapping(
        method = POST,
        value = "/attendance-manHour/locked-list/batch-delete/{ids}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody batchDeleteLocked(
        @PathVariable("ids") List<Long> ids
    );
}
