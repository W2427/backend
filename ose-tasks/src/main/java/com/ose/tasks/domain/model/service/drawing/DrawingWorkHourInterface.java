package com.ose.tasks.domain.model.service.drawing;

import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.*;
import com.ose.tasks.dto.drawing.DrawingCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingWorkHourDTO;
import com.ose.tasks.dto.timesheet.SelectDataDTO;
import com.ose.tasks.entity.drawing.AttendanceRecord;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingRecord;
import com.ose.tasks.entity.drawing.DrawingWorkHour;
import org.springframework.data.domain.Page;

import java.io.File;
import java.text.ParseException;
import java.util.List;

/**
 * service接口
 */
public interface DrawingWorkHourInterface {

    DrawingWorkHour create(
        Long orgId,
        Long projectId,
        DrawingWorkHourDTO dto
    );

    Page<DrawingWorkHour> search(
        Long orgId,
        Long projectId,
        DrawingWorkHourDTO dto,
        PageDTO page
    );

    void adopt(
        Long orgId,
        Long projectId,
        Long drawingExamineId
    );

    void reject(
        Long orgId,
        Long projectId,
        Long drawingExamineId
    );

    List<DrawingRecord> getList(Long orgId, Long projectId, DrawingRecordCriteriaDTO criteriaDTO);

    void updateDrawing(
        Long orgId,
        Long projectId,
        Long drawingId,
        DrawingCriteriaDTO criteriaDTO,
        ContextDTO contextDTO
    );

    void create(Long orgId, Long projectId, DrawingRecordCriteriaDTO criteriaDTO, ContextDTO context);

    void update(Long orgId, Long projectId, Long drawingRecordId, DrawingRecordCriteriaDTO criteriaDTO, ContextDTO context);

    void delete(Long orgId, Long projectId, Long drawingRecordId, ContextDTO context);

    ManHourDTO getGlobalManHour(Long userId, ManHourCriteriaDTO criteriaDTO);

    List<DrawingRecord> getDrawingRecordListByUserAndDate(Long userId, String workHourDate);

    DrawingRecord getDrawingRecordById(Long drawingRecordId);

    List<ManHourDTO> getProjectList(Long userId);

    List<Drawing> getProjectDrawingData(Long projectId, Long userId);

    List<SelectDataDTO> getProjectStageData(Long drawingId, Long userId);

    List<SelectDataDTO> getVersionList(Long drawingId, Long processId, Long userId);

    List<SelectDataDTO> getTaskNodes(Long drawingId, String version, Long userId);

    ManHourDTO getValuableYears();

    ManHourDTO getValuableWeeks();

    ManHourDTO getStageList(Long orgId, Long projectId);

    File saveDownloadFile(Long userId, ManHourCriteriaDTO criteriaDTO, Long operatorId);

    Page<DrawingRecord> getGlobalManHourDetail(Long userId, ManHourDetailDTO manHourDetailDTO);

    Page<AttendanceDataDTO> getAttendanceHour(ManHourCriteriaDTO criteriaDTO);

    File saveAttendanceDownloadFile(ManHourExportCriteriaDTO criteriaDTO, Long operatorId) throws ParseException;

    File saveCheckDataDownloadFile(ReviewCriteriaDTO criteriaDTO, Long operatorId) throws Exception;

    File saveWeeklyDataDownloadFile(ReviewCriteriaDTO criteriaDTO, Long operatorId) throws Exception;

    AttendanceFilterDTO filter();

    AttendanceFilterDTO filterWeekly(ManHourCriteriaDTO criteriaDTO);

    void locked(ManHourCriteriaDTO criteriaDTO, ContextDTO context);

    Page<AttendanceRecord> getAttendanceLockedList(ManHourCriteriaDTO criteriaDTO);

    void deleteLocked(Long lockedId, ContextDTO context);

    Page<ReviewDataDTO> searchReview(ReviewCriteriaDTO criteriaDTO, Long userId);

    Page<ReviewDataDTO> searchTimesheetList(ReviewCriteriaDTO criteriaDTO, ContextDTO context);

    Page<ReviewDataDTO> searchHour(HourDashboardCriteriaDTO criteriaDTO);

    List<EmployeeDataDTO> getEmployeeDataList(ReviewCriteriaDTO criteriaDTO) throws Exception;

    EmployeeDataDTO getEmployeeDataDetail(String id) throws Exception;
}
