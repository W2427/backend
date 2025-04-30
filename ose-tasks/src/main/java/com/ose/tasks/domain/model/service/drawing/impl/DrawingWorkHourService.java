package com.ose.tasks.domain.model.service.drawing.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.ose.auth.entity.Organization;
import com.ose.auth.entity.UserProfile;
import com.ose.auth.vo.OrgType;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.domain.model.repository.holiday.HolidayDateRepository;
import com.ose.tasks.domain.model.repository.holiday.WorkDayRepository;
import com.ose.util.BeanUtils;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.dto.AuthCheckDTO;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.notifications.api.NotificationFeignAPI;
import com.ose.notifications.dto.NotificationCreateDTO;
import com.ose.notifications.vo.NotificationType;
import com.ose.tasks.domain.model.repository.OverTimeApplicationRepository;
import com.ose.tasks.domain.model.repository.ProjectRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessStageRepository;
import com.ose.tasks.domain.model.repository.drawing.*;
import com.ose.tasks.domain.model.service.drawing.DrawingWorkHourInterface;
import com.ose.tasks.dto.*;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.dto.timesheet.SelectDataDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmProcessStage;
import com.ose.tasks.entity.drawing.*;
import com.ose.vo.DrawingRecordStatus;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component

public class DrawingWorkHourService implements DrawingWorkHourInterface {

    private final DrawingWorkHourRepository drawingWorkHourRepository;
    private final DrawingRecordRepository drawingRecordRepository;

    private final DrawingRepository drawingRepository;
    private final BpmProcessRepository bpmProcessRepository;
    @Value("${application.files.temporary}")
    private String temporaryDir;

    private final DrawingHistoryRepository drawingHistoryRepository;

    private final UserFeignAPI userFeignAPI;

    private final ProjectRepository projectRepository;

    private final BpmProcessStageRepository bpmProcessStageRepository;

    private final AttendanceRecordRepository attendanceRecordRepository;

    private final NotificationFeignAPI notificationFeignAPI;

    private final OverTimeApplicationRepository overTimeApplicationRepository;

    private final DingTalkWorkHourRepository dingTalkWorkHourRepository;

    private final DingTalkLeaveDataRepository dingTalkLeaveDataRepository;

    private final DingTalkEmployeeDataRepository dingTalkEmployeeDataRepository;

    private final EmployeeDataRepository employeeDataRepository;

    private static HolidayDateRepository holidayDateRepository = null;

    private static WorkDayRepository workDayRepository = null;

    /**
     * 构造方法
     */
    @Autowired
    public DrawingWorkHourService(
        DrawingWorkHourRepository drawingWorkHourRepository,
        DrawingRecordRepository drawingRecordRepository,
        DrawingRepository drawingRepository,
        DrawingHistoryRepository drawingHistoryRepository,
        UserFeignAPI userFeignAPI,
        ProjectRepository projectRepository,
        BpmProcessStageRepository bpmProcessStageRepository,
        AttendanceRecordRepository attendanceRecordRepository,
        NotificationFeignAPI notificationFeignAPI,
        OverTimeApplicationRepository overTimeApplicationRepository,
        BpmProcessRepository bpmProcessRepository,
        DingTalkWorkHourRepository dingTalkWorkHourRepository,
        DingTalkLeaveDataRepository dingTalkLeaveDataRepository,
        DingTalkEmployeeDataRepository dingTalkEmployeeDataRepository,
        EmployeeDataRepository employeeDataRepository,
        HolidayDateRepository holidayDateRepository,
        WorkDayRepository workDayRepository) {
        this.drawingWorkHourRepository = drawingWorkHourRepository;
        this.drawingRecordRepository = drawingRecordRepository;
        this.drawingRepository = drawingRepository;
        this.drawingHistoryRepository = drawingHistoryRepository;
        this.userFeignAPI = userFeignAPI;
        this.projectRepository = projectRepository;
        this.bpmProcessStageRepository = bpmProcessStageRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.notificationFeignAPI = notificationFeignAPI;
        this.overTimeApplicationRepository = overTimeApplicationRepository;
        this.bpmProcessRepository = bpmProcessRepository;
        this.dingTalkWorkHourRepository = dingTalkWorkHourRepository;
        this.dingTalkLeaveDataRepository = dingTalkLeaveDataRepository;
        this.dingTalkEmployeeDataRepository = dingTalkEmployeeDataRepository;
        this.employeeDataRepository = employeeDataRepository;
        this.holidayDateRepository = holidayDateRepository;
        this.workDayRepository = workDayRepository;
    }


    @Override
    public DrawingWorkHour create(Long orgId, Long projectId, DrawingWorkHourDTO dto) {
        DrawingWorkHour drawingWorkHour = BeanUtils.copyProperties(dto, new DrawingWorkHour());
        drawingWorkHour.setOrgId(orgId);
        drawingWorkHour.setProjectId(projectId);
        return drawingWorkHourRepository.save(drawingWorkHour);
    }

    @Override
    public Page<DrawingWorkHour> search(Long orgId, Long projectId, DrawingWorkHourDTO dto, PageDTO page) {

        return drawingWorkHourRepository.getList(orgId, projectId, dto, page);
    }

    @Override
    public void adopt(Long orgId, Long projectId, Long drawingExamineId) {
        DrawingRecord drawingRecordOptional = drawingRecordRepository.findById(drawingExamineId).orElse(null);
        if (drawingRecordOptional == null) {
            throw new BusinessError();
        }
        drawingRecordOptional.setDrawingRecordStatus(DrawingRecordStatus.FINISH);
        drawingRecordRepository.save(drawingRecordOptional);
    }

    @Override
    public void reject(Long orgId, Long projectId, Long drawingExamineId) {
        DrawingRecord drawingRecordOptional = drawingRecordRepository.findById(drawingExamineId).orElse(null);
        if (drawingRecordOptional == null) {
            throw new BusinessError();
        }
        drawingRecordOptional.setDrawingRecordStatus(DrawingRecordStatus.CANCEL);
        drawingRecordRepository.save(drawingRecordOptional);
    }

    public List<DrawingRecord> getList(Long orgId, Long projectId, DrawingRecordCriteriaDTO criteriaDTO) {
        return drawingRecordRepository.getListByCondition(orgId, projectId, criteriaDTO);
    }

    @Override
    public void updateDrawing(Long orgId, Long projectId, Long drawingId, DrawingCriteriaDTO criteriaDTO, ContextDTO contextDTO) {
        Drawing drawing = drawingRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, drawingId);
        if (drawing == null) {
            throw new BusinessError();
        } else {
            drawing.setDrawingStatus(criteriaDTO.getDrawingStatus());
            drawing.setLastModifiedAt();
        }
        drawingRepository.save(drawing);
    }

    @Override
    public void create(Long orgId, Long projectId, DrawingRecordCriteriaDTO criteriaDTO, ContextDTO context) {
        // 判断当前日期是否已有法定节假日，工时是否已填写，如果填写判断工时相加是否超过8小时
        DrawingRecord record = drawingRecordRepository.findByEngineerIdAndWorkHourDateAndTaskAndDeletedIsFalse(
            context.getOperator().getId(),
            DateUtil.format(criteriaDTO.getDate(), "yyyy-MM-dd"),
            "Holiday");
        if (record != null && criteriaDTO.getWorkHour() != null && criteriaDTO.getWorkHour() > 0) {
            if (criteriaDTO.getWorkHour() + record.getWorkHour() > 8){
                throw new BusinessError("The day is a statutory holiday, and the working hours have been filled in!");
            }
        }

        //节假日自动填写是判断当前日期是否已填写
        DrawingRecord recordHoliday = drawingRecordRepository.findByEngineerIdAndWorkHourDateAndTaskAndDeletedIsFalse(
            context.getOperator().getAccessTokenId(),
            DateUtil.format(criteriaDTO.getDate(), "yyyy-MM-dd"),
            "Holiday");
        if (recordHoliday != null && criteriaDTO.getWorkHour() != null && criteriaDTO.getWorkHour() > 0) {
            throw new BusinessError("The day is a statutory holiday, and the working hours have been filled in!");
        }

        //自动填写时判断当前日期工时是否已填写
        if (criteriaDTO.getAuto() != null && criteriaDTO.getAuto()) {
            List<DrawingRecord> currentDateRecords = drawingRecordRepository.findAllByEngineerIdAndWorkHourDateAndDeletedIsFalse(
                context.getOperator().getAccessTokenId(),
                DateUtil.format(criteriaDTO.getDate(), "yyyy-MM-dd"));
            if (currentDateRecords != null && currentDateRecords.size() > 0 && criteriaDTO.getWorkHour() != null && criteriaDTO.getWorkHour() > 0) {
                throw new BusinessError("The working hours of the current date are filled in!");
            }
        }
        //判断是否有自动填报的数据
        DrawingRecord currentDateAutoRecord = drawingRecordRepository.findByEngineerIdAndWorkHourDateAndProjectNameAndTaskAndDeletedIsFalse(
            context.getOperator().getId(),
            DateUtil.format(criteriaDTO.getDate(), "yyyy-MM-dd"),
            "Overhead",
            "autofill"
        );
        //处理自动生成的任务
        if (currentDateAutoRecord != null) {
            // 1、如果新填报的工时与自动填报工时相等，则将自动填报工时删除
            if (currentDateAutoRecord.getWorkHour().compareTo(criteriaDTO.getWorkHour()) == 0) {
                currentDateAutoRecord.setDeleted(true);
                currentDateAutoRecord.setDeletedAt(new Date());
                currentDateAutoRecord.setStatus(EntityStatus.DELETED);
                currentDateAutoRecord.setDeletedBy(context.getOperator().getId());
                currentDateAutoRecord.setLastModifiedAt();
                currentDateAutoRecord.setLastModifiedBy(context.getOperator().getId());
                // 2、如果自动工时>待填报工时，则更新自动工时数
            } else if (currentDateAutoRecord.getWorkHour().compareTo(criteriaDTO.getWorkHour()) > 0) {
                currentDateAutoRecord.setWorkHour(currentDateAutoRecord.getWorkHour() - criteriaDTO.getWorkHour());
                currentDateAutoRecord.setLastModifiedAt();
                currentDateAutoRecord.setLastModifiedBy(context.getOperator().getId());
            }
            // 3、自动工时数<待填报工时，通过下列的"判断工时总和是否超过8"来check
            drawingRecordRepository.save(currentDateAutoRecord);
        }
        //判断工时总和是否超过8
        Double sumWorkHour = drawingRecordRepository.findSumWorkHourByEngineerIdAndWorkHourDateAndDeletedIsFalse(
            context.getOperator().getId(),
            DateUtil.format(criteriaDTO.getDate(), "yyyy-MM-dd"));
        if (sumWorkHour != null && (sumWorkHour + (criteriaDTO.getWorkHour() == null ? 0 : criteriaDTO.getWorkHour())) > 8) {
            throw new BusinessError("Normal working hours have exceeded 8 hours!");
        }

//        // 查询当前项目、任务、日期下的加班工时总数
//        Double sumOutHour = drawingRecordRepository.findSumOverHourByEngineerIdAndWorkHourDateAndProjectNameAndTaskAndDeletedIsFalse(
//            context.getOperator().getId(),
//            DateUtil.format(criteriaDTO.getDate(), "yyyy-MM-dd"),
//            criteriaDTO.getProjectName(),
//            criteriaDTO.getTask());
//
//        // 查询当前日期、项目、任务的加班允许工时数
//        Double manHour = overTimeApplicationRepository.findByUserIdAndProjectNameAndStartDateAndTask(
//            context.getOperator().getId(),
//            criteriaDTO.getProjectName(),
//            criteriaDTO.getDate(),
//            new Date(criteriaDTO.getDate().getTime() + (1000 * 60 * 60 * 24)),
//            "%" + criteriaDTO.getTask() + "%"
//        );
//        if (sumOutHour != null && manHour != null && manHour < (sumOutHour + (criteriaDTO.getOutHour() == null ? 0 : criteriaDTO.getOutHour()))) {
//            throw new BusinessError("The overtime work for tasks under this project on that day has exceeded!");
//        }


        // 判断date是否锁定
        boolean isLocked = attendanceRecordRepository.existsByLockedDateAndStatus(DateUtil.format(criteriaDTO.getDate(), "yyyy-MM-dd"), EntityStatus.ACTIVE);
        if (isLocked) {
            throw new BusinessError(DateUtil.format(criteriaDTO.getDate(), "yyyy-MM-dd") + " already locked");
        }

        DrawingRecord drawingRecord = new DrawingRecord();
        BeanUtils.copyProperties(criteriaDTO, drawingRecord);
        if (criteriaDTO.getOutHour() != null && criteriaDTO.getOutHour() == 0.0) {
            drawingRecord.setOutHour(null);
        }
        if (criteriaDTO.getWorkHour() != null && criteriaDTO.getWorkHour() == 0.0) {
            drawingRecord.setWorkHour(null);
        }
        drawingRecord.setEngineerId(context.getOperator().getId() == null ? context.getOperator().getAccessTokenId() : context.getOperator().getId());
        drawingRecord.setEngineer(context.getOperator().getName());
        drawingRecord.setOrgId(orgId);
        drawingRecord.setProjectId(projectId);
        drawingRecord.setStatus(EntityStatus.ACTIVE);
        drawingRecord.setDrawingRecordStatus(DrawingRecordStatus.FILL);
        drawingRecord.setLastModifiedAt();
        drawingRecord.setLastModifiedBy(context.getOperator().getId() == null ? context.getOperator().getAccessTokenId() : context.getOperator().getId());
        drawingRecord.setCreatedAt();
        drawingRecord.setCreatedBy(context.getOperator().getId() == null ? context.getOperator().getAccessTokenId() : context.getOperator().getId());
        drawingRecord.setWorkHourDate(DateUtil.format(criteriaDTO.getDate(), "yyyy-MM-dd"));
        if (criteriaDTO.getWxWorkHourDate() != null && !"".equals(criteriaDTO.getWxWorkHourDate())) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-M-d");
            LocalDateTime parse = LocalDate.parse(criteriaDTO.getWxWorkHourDate(), df).atStartOfDay();
            drawingRecord.setWorkHourDate(DateUtil.format(parse, "yyyy-MM-dd"));
        }
        //根据criteriaDTO 的 Date 获取月份信息
        drawingRecord.setMonthly(Integer.valueOf(new SimpleDateFormat("yyyyMM").format(criteriaDTO.getDate())));


        String documentTitle = criteriaDTO.getDocumentTitle();
        String drawingNo = criteriaDTO.getDrawingNo();
        drawingRepository.findAllByOrgIdAndProjectIdAndDocumentTitleAndDwgNoAndStatus(orgId, projectId, documentTitle, drawingNo, EntityStatus.ACTIVE).ifPresent(drawing -> drawingRecord.setDrawingId(drawing.getId()));

        if (criteriaDTO.getDrawingId() != null && !"".equals(criteriaDTO.getDrawingId())) {
            Drawing drawing = drawingRepository.findById(criteriaDTO.getDrawingId()).orElse(null);
            if (drawing == null) {
                throw new NotFoundError();
            }
            drawingRecord.setDocumentTitle(drawing.getDocumentTitle());
            drawingRecord.setDrawingNo(drawing.getDwgNo());
        }

        if (criteriaDTO.getRemark() != null && !"".equals(criteriaDTO.getRemark())) {
            drawingRecord.setRemark(criteriaDTO.getRemark());
        }

        drawingRecordRepository.save(drawingRecord);

//        List<DrawingRecord> drawingRecords = drawingRecordRepository.findAllByOrgIdAndProjectIdAndDrawingIdAndDeletedIsFalse(orgId, projectId, criteriaDTO.getDrawingId());
//        double total = 0.0;
//        for (DrawingRecord entity : drawingRecords) {
//            total = total + entity.getWorkHour() + entity.getOutHour();
//        }
//        d.setWorkHour(total);
//        drawingHistoryRepository.save(d);
    }

    @Override
    public void update(Long orgId, Long projectId, Long drawingRecordId, DrawingRecordCriteriaDTO criteriaDTO, ContextDTO context) {
        DrawingRecord drawingRecord = drawingRecordRepository.findById(drawingRecordId).orElse(null);
        if (drawingRecord == null) {
            throw new BusinessError();
        }
        drawingRecord.setWorkHour(criteriaDTO.getWorkHour());
        drawingRecord.setOutHour(criteriaDTO.getOutHour());
        drawingRecord.setStage(criteriaDTO.getStage());
        drawingRecord.setRev(criteriaDTO.getRev());
        drawingRecord.setTask(criteriaDTO.getTask());
        drawingRecord.setActivity(criteriaDTO.getActivity());
        drawingRecord.setWorkHourDate(DateUtil.format(criteriaDTO.getDate(), "yyyy-MM-dd"));
        if (criteriaDTO.getWxWorkHourDate() != null && !"".equals(criteriaDTO.getWxWorkHourDate())) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-M-d");
            LocalDateTime parse = LocalDate.parse(criteriaDTO.getWxWorkHourDate(), df).atStartOfDay();
            drawingRecord.setWorkHourDate(DateUtil.format(parse, "yyyy-MM-dd"));
        }
        drawingRecord.setLastModifiedBy(context.getOperator().getId());
        drawingRecord.setLastModifiedAt(new Date());


        //判断date是否锁定
        boolean isLocked = attendanceRecordRepository.existsByLockedDateAndStatus(drawingRecord.getWorkHourDate(), EntityStatus.ACTIVE);
        if (isLocked) {
            throw new BusinessError(drawingRecord.getWorkHourDate() + " already locked");
        }

        drawingRecordRepository.save(drawingRecord);

//        List<DrawingRecord> drawingRecords = drawingRecordRepository.findAllByOrgIdAndProjectIdAndDrawingIdAndDeletedIsFalse(orgId, projectId, criteriaDTO.getDrawingId());
//        double total = 0.0;
//        for (DrawingRecord entity : drawingRecords) {
//            total = total + entity.getWorkHour() + entity.getOutHour();
//        }
//        d.setWorkHour(total);
//        drawingHistoryRepository.save(d);
    }

    @Override
    public void delete(Long orgId, Long projectId, Long drawingRecordId, ContextDTO context) {
        DrawingRecord drawingRecord = drawingRecordRepository.findById(drawingRecordId).orElse(null);
        if (drawingRecord == null) {
            throw new BusinessError();
        }
        drawingRecord.setDeleted(true);
        drawingRecord.setDeletedAt(new Date());
        drawingRecord.setStatus(EntityStatus.DELETED);
        drawingRecord.setDeletedBy(context.getOperator().getId());

        //判断date是否锁定
        boolean isLocked = attendanceRecordRepository.existsByLockedDateAndStatus(drawingRecord.getWorkHourDate(), EntityStatus.ACTIVE);
        if (isLocked) {
            throw new BusinessError(drawingRecord.getWorkHourDate() + " already locked");
        }

        drawingRecordRepository.save(drawingRecord);
    }

    @Override
    public ManHourDTO getGlobalManHour(Long userId, ManHourCriteriaDTO criteriaDTO) {

        ManHourDTO manHourDTO = new ManHourDTO();
        List<ManHourDTO> manHourDTOS = new ArrayList<>();
        List<Double> gatherManDatas = new ArrayList<>();
        List<Double> gatherOutDatas = new ArrayList<>();
        int year = criteriaDTO.getYear() == null ? DateUtil.thisYear() : criteriaDTO.getYear();
        int week = criteriaDTO.getWeek() == null ? DateUtil.thisWeekOfYear() - 1 : criteriaDTO.getWeek();
        List<String> daysOfWeek = getDaysOfWeek(year, week);

        Map<String, ManHourDTO> manHourDTOMap = new HashMap<>();
        for (String weekDate : daysOfWeek) {
            String weekStr = weekDate.split("_")[0];
            String dateStr = weekDate.split("_")[1];
            List<Map<String, Object>> maps = new ArrayList<>();
            if (criteriaDTO.getDiscipline() != null) {
                maps = drawingRecordRepository.drawingRecordsByDiscipline(userId, dateStr, criteriaDTO.getDiscipline());
            } else {
                maps = drawingRecordRepository.drawingRecords(userId, dateStr);
            }


            double gatherMan = 0.0;
            double gatherOut = 0.0;
            for (Map<String, Object> map : maps) {
                ManHourDTO entity;

                Long id = map.get("id") == null ? 0L : Long.parseLong(map.get("id").toString());
                String documentTitle = map.get("document_title") == null ? "" : map.get("document_title").toString();
                String drawingNo = map.get("drawing_no") == null ? "" : map.get("drawing_no").toString();
                String discipline = map.get("discipline") == null ? "" : map.get("discipline").toString();
                String rev = map.get("rev") == null ? "" : map.get("rev").toString();
                String remark = map.get("remark") == null ? "" : map.get("remark").toString();
                String task = map.get("task") == null ? "" : map.get("task").toString();
                String activity = map.get("activity") == null ? "" : map.get("activity").toString();
                String assignedBy = map.get("assigned_by") == null ? "" : map.get("assigned_by").toString();
                Double workHour = map.get("work_hour") == null ? 0.0 : (Double) map.get("work_hour");
                Double outHour = map.get("out_hour") == null ? 0.0 : (Double) map.get("out_hour");
                String projectName = map.get("project_name") == null ? "" : map.get("project_name").toString();
                String stage = map.get("stage") == null ? "" : map.get("stage").toString();
                Long stageId = map.get("stage_id") == null ? 0L : Long.parseLong(map.get("stage_id").toString());

                gatherMan += workHour;
                gatherOut += outHour;

//                String key = projectName + documentTitle + rev + remark + task + activity + stage;
                String key = projectName + drawingNo + documentTitle + rev + task + activity + stage + discipline;
                if (manHourDTOMap.get(key) == null) {
                    entity = new ManHourDTO();
                    List<Double> temp = new ArrayList<>();
                    temp.add(0.0);
                    temp.add(0.0);
                    entity.setMonDatas(temp);
                    entity.setTueDatas(temp);
                    entity.setWedDatas(temp);
                    entity.setThuDatas(temp);
                    entity.setFriDatas(temp);
                    entity.setSatDatas(temp);
                    entity.setSunDatas(temp);
                } else {
                    entity = manHourDTOMap.get(key);
                }

                entity.setId(id);
                entity.setProjectName(projectName);
                entity.setDocumentTitle(documentTitle);
                entity.setDrawingNo(drawingNo);
                entity.setDiscipline(discipline);
                entity.setRev(rev);
                entity.setRemark(remark);
                entity.setTask(task);
                entity.setActivity(activity);
                entity.setAssignedBy(assignedBy);
                entity.setStage(stage);
                entity.setStageId(stageId);

                double work;
                double out;
                List<Double> datas;
                switch (weekStr) {
                    case "周一":
                        datas = new ArrayList<>();
                        work = entity.getMonDatas() == null ? 0.0 : entity.getMonDatas().get(0) + workHour;
                        out = entity.getMonDatas() == null ? 0.0 : entity.getMonDatas().get(1) + outHour;
                        datas.add(work);
                        datas.add(out);
                        entity.setMonDatas(datas);
                        break;
                    case "周二":
                        datas = new ArrayList<>();
                        work = entity.getTueDatas() == null ? 0.0 : entity.getTueDatas().get(0) + workHour;
                        out = entity.getTueDatas() == null ? 0.0 : entity.getTueDatas().get(1) + outHour;
                        datas.add(work);
                        datas.add(out);
                        entity.setTueDatas(datas);
                        break;
                    case "周三":
                        datas = new ArrayList<>();
                        work = entity.getWedDatas() == null ? 0.0 : entity.getWedDatas().get(0) + workHour;
                        out = entity.getWedDatas() == null ? 0.0 : entity.getWedDatas().get(1) + outHour;
                        datas.add(work);
                        datas.add(out);
                        entity.setWedDatas(datas);
                        break;
                    case "周四":
                        datas = new ArrayList<>();
                        work = entity.getThuDatas() == null ? 0.0 : entity.getThuDatas().get(0) + workHour;
                        out = entity.getThuDatas() == null ? 0.0 : entity.getThuDatas().get(1) + outHour;
                        datas.add(work);
                        datas.add(out);
                        entity.setThuDatas(datas);
                        break;
                    case "周五":
                        datas = new ArrayList<>();
                        work = entity.getFriDatas() == null ? 0.0 : entity.getFriDatas().get(0) + workHour;
                        out = entity.getFriDatas() == null ? 0.0 : entity.getFriDatas().get(1) + outHour;
                        datas.add(work);
                        datas.add(out);
                        entity.setFriDatas(datas);
                        break;
                    case "周六":
                        datas = new ArrayList<>();
                        work = entity.getSatDatas() == null ? 0.0 : entity.getSatDatas().get(0) + workHour;
                        out = entity.getSatDatas() == null ? 0.0 : entity.getSatDatas().get(1) + outHour;
                        datas.add(work);
                        datas.add(out);
                        entity.setSatDatas(datas);
                        break;
                    case "周日":
                        datas = new ArrayList<>();
                        work = entity.getSunDatas() == null ? 0.0 : entity.getSunDatas().get(0) + workHour;
                        out = entity.getSunDatas() == null ? 0.0 : entity.getSunDatas().get(1) + outHour;
                        datas.add(work);
                        datas.add(out);
                        entity.setSunDatas(datas);
                        break;
                    default:
                        return null;
                }
                manHourDTOMap.put(key, entity);
            }
            gatherManDatas.add(gatherMan);
            gatherOutDatas.add(gatherOut);
        }

        for (String key : manHourDTOMap.keySet()) {
            ManHourDTO hourDTO = manHourDTOMap.get(key);
            double manHourSum = hourDTO.getMonDatas().get(0) + hourDTO.getTueDatas().get(0) + hourDTO.getWedDatas().get(0) + hourDTO.getThuDatas().get(0) + hourDTO.getFriDatas().get(0) + hourDTO.getSatDatas().get(0) + hourDTO.getSunDatas().get(0);
            double outHourSum = hourDTO.getMonDatas().get(1) + hourDTO.getTueDatas().get(1) + hourDTO.getWedDatas().get(1) + hourDTO.getThuDatas().get(1) + hourDTO.getFriDatas().get(1) + hourDTO.getSatDatas().get(1) + hourDTO.getSunDatas().get(1);

            List<Double> sumDouble = new ArrayList<>();
            sumDouble.add(manHourSum);
            sumDouble.add(outHourSum);

            hourDTO.setSumDatas(sumDouble);

            List<String> dates = new ArrayList<>();
            for (String s : daysOfWeek) {
                String dateStr = s.split("_")[1];
                dates.add(dateStr);
            }
            hourDTO.setManHourTitleList(dates);
            manHourDTOS.add(hourDTO);
        }

        manHourDTO.setDatas(manHourDTOS);
        double sum = gatherManDatas.stream().mapToDouble(Double::doubleValue).sum();
        gatherManDatas.add(sum);
        sum = gatherOutDatas.stream().mapToDouble(Double::doubleValue).sum();
        gatherOutDatas.add(sum);
        manHourDTO.setGatherManDatas(gatherManDatas);
        manHourDTO.setGatherOutDatas(gatherOutDatas);

        manHourDTO.setWeek(DateUtil.thisWeekOfYear() - 1);
        manHourDTO.setYear(DateUtil.thisYear());

        return manHourDTO;
    }

    public Page<DrawingRecord> getGlobalManHourDetail(
        Long userId,
        ManHourDetailDTO manHourDetailDTO
    ) {
        return drawingRecordRepository.searchDetail(userId, manHourDetailDTO);

    }

    @Override
    public List<DrawingRecord> getDrawingRecordListByUserAndDate(Long userId, String workHourDate) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-M-d");
        LocalDateTime parse = LocalDate.parse(workHourDate, df).atStartOfDay();
        String date = DateUtil.format(parse, "yyyy-MM-dd");
        return drawingRecordRepository.findAllByEngineerIdAndWorkHourDateAndDeletedIsFalse(userId, date);
    }

    @Override
    public DrawingRecord getDrawingRecordById(Long drawingRecordId) {
        DrawingRecord drawingRecord = drawingRecordRepository.findById(drawingRecordId).orElse(null);
        if (drawingRecord == null) {
            throw new BusinessError();
        }
        return drawingRecord;
    }

    @Override
    public List<ManHourDTO> getProjectList(Long userId) {
        List<ManHourDTO> manHourDTOS = new ArrayList<>();
        AuthCheckDTO data = userFeignAPI.getOrganizationByUserId(userId).getData();
        String jsonList = data.getUserAgent();
        JSONArray jsonArray = JSONUtil.parseArray(jsonList);

        List<Long> orgIds = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            Long orgId = Long.valueOf(jsonArray.getJSONObject(i).getJSONObject("organizationId").getStr("$ref"));
            orgIds.add(orgId);
        }


        for (int i = 0; i < jsonArray.size(); i++) {
            Long orgId = Long.valueOf(jsonArray.getJSONObject(i).getJSONObject("organizationId").getStr("$ref"));
            List<Project> list = projectRepository.findByOrgIdAndStatusAndHaveHour(orgId);
            for (Project project : list) {
                if (project.getName().contains("非项目") || project.getName().contains("投标")) {
                    continue;
                }
                ManHourDTO manHourDTO = new ManHourDTO();
                Long projectId = project.getId();
                manHourDTO.setOrgId(orgId);
                manHourDTO.setProjectId(projectId);
                manHourDTO.setProjectName(project.getName());
//                List<Drawing> drawings = drawingRepository.findAllByOrgIdAndProjectIdAndDesc(orgId, projectId,userId);
//                List<Drawing> drawings = drawingRepository.findByOrgIdAndProjectId(orgId, projectId);
//                List<BpmProcessStage> bpmProcessStages = bpmProcessStageRepository.findByOrgIdAndProjectIdAndStatus(orgId, projectId, EntityStatus.ACTIVE);
//
//                List<String> stages = new ArrayList<>();
//                for (BpmProcessStage stage : bpmProcessStages) {
//                    stages.add(stage.getNameEn());
//                }
//                manHourDTO.setTitles(drawings);
//                manHourDTO.setStages(stages);
                manHourDTOS.add(manHourDTO);
            }
        }
        return manHourDTOS;
    }

    @Override
    public List<SelectDataDTO> getProjectStageData(Long projectId, Long userId) {

//        return bpmProcessStageRepository.findByProjectIdAndStatus(projectId, EntityStatus.ACTIVE);
        return bpmProcessRepository.findByDrawingIdAndUserId(projectId, userId);
    }

    @Override
    public List<SelectDataDTO> getVersionList(Long projectId, Long processId, Long userId) {
        return drawingRepository.getVersionList(projectId, processId, userId);
    }

    @Override
    public List<SelectDataDTO> getTaskNodes(Long projectId, String version, Long userId) {
        return drawingRepository.getTaskNodes(projectId, version, userId);
    }

    @Override
    public List<Drawing> getProjectDrawingData(Long drawingId, Long userId) {
        return drawingRepository.findByProjectIdAndUserId(drawingId, userId);
    }

    @Override
    public ManHourDTO getValuableYears() {
        List<String> years = drawingRecordRepository.findYears();
        ManHourDTO result = new ManHourDTO();
        result.setYears(years);
        return result;
    }

    @Override
    public ManHourDTO getValuableWeeks() {
        List<String> weeks = drawingRecordRepository.findWeeks();
        ManHourDTO result = new ManHourDTO();
        result.setWeeks(weeks);
        return result;
    }

    @Override
    public ManHourDTO getStageList(Long orgId, Long projectId) {
        ManHourDTO manHourDTO = new ManHourDTO();
        List<BpmProcessStage> bpmProcessStages = bpmProcessStageRepository.findByOrgIdAndProjectIdAndStatus(orgId, projectId, EntityStatus.ACTIVE);
        List<String> stages = new ArrayList<>();
        for (BpmProcessStage stage : bpmProcessStages) {
            stages.add(stage.getNameEn());
        }
        manHourDTO.setStages(stages);
        return manHourDTO;
    }

    @Override
    public File saveDownloadFile(Long userId, ManHourCriteriaDTO criteriaDTO, Long operatorId) {

        String templateFilePath = "/var/www/saint-whale/backend/resources/templates/export-global-man-hour.xlsx";

        String temporaryFileName = System.currentTimeMillis() + ".xlsx";
        String filePath = "/var/www/saint-whale/backend/private/upload/" + temporaryFileName;

        File excel = new File(temporaryDir, temporaryFileName);

        List<Map<String, Object>> drawingRecords = new ArrayList<>();
        List<Map<String, Object>> unHourUserInfo = new ArrayList<>();
        if (criteriaDTO.getType() != null && "currentWeek".equals(criteriaDTO.getType())) {
            List<String> daysOfWeek = getDaysOfWeek(DateUtil.thisYear(), DateUtil.thisWeekOfYear() - 1);
            List<String> days = new ArrayList<>();
            for (String day : daysOfWeek) {
                String substring = day.substring(0, day.indexOf("_"));
                String res = day.substring(substring.length() + 1);
                days.add(res);
            }
            drawingRecords = drawingRecordRepository.drawingRecordsByWorkHourDates(days);
            unHourUserInfo = drawingRecordRepository.drawingRecordsByUnHourUsers(days);
            if (unHourUserInfo.size() > 0) {
                drawingRecords.addAll(unHourUserInfo);
            }
        } else if (criteriaDTO.getType() != null && "multipleWeeks".equals(criteriaDTO.getType())) {
            int year = criteriaDTO.getYear();
            List<Integer> weeks = criteriaDTO.getWeeks();
            List<String> days = new ArrayList<>();
            for (Integer week : weeks) {
                List<String> daysOfWeek = getDaysOfWeek(year, week);
                for (String day : daysOfWeek) {
                    String substring = day.substring(0, day.indexOf("_"));
                    String res = day.substring(substring.length() + 1);
                    days.add(res);
                }
            }
            drawingRecords = drawingRecordRepository.drawingRecordsByWorkHourDates(days);
            if (weeks.size() == 1) {
                unHourUserInfo = drawingRecordRepository.drawingRecordsByUnHourUsers(days);
                if (unHourUserInfo.size() > 0) {
                    drawingRecords.addAll(unHourUserInfo);
                }
            }
        }

        EasyExcel.write(filePath).withTemplate(templateFilePath).sheet("global_man_hour").doFill(drawingRecords);
        return excel;
    }

    public static List<String> getDaysOfWeek(int year, int week) {
        List<String> daysOfWeek = new ArrayList<>();
        TemporalField fieldISO = WeekFields.of(Locale.CHINESE).dayOfWeek();
        LocalDate date = LocalDate.ofYearDay(year, 1).with(fieldISO, 1);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        LocalDate firstMondayOfYear = date;
        switch (dayOfWeek) {
            case SUNDAY:
                firstMondayOfYear = LocalDate.ofYearDay(year, 1).with(fieldISO, 2);
                break;
            case MONDAY:
                break;
            default:
                firstMondayOfYear = LocalDate.ofYearDay(year, 1).with(fieldISO, 8 - dayOfWeek.getValue());
        }
        LocalDate firstMondayOfWeek = firstMondayOfYear.plusWeeks(week - 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 7; i++) {
            LocalDate dayOfWeekDate = firstMondayOfWeek.plusDays(i);
            LocalDateTime dateTime = LocalDateTime.of(dayOfWeekDate, LocalTime.MIN);
            String formattedDate = dateTime.format(formatter);
            String weekNum = "";
            switch (i) {
                case 1:
                    weekNum = "周二";
                    break;
                case 2:
                    weekNum = "周三";
                    break;
                case 3:
                    weekNum = "周四";
                    break;
                case 4:
                    weekNum = "周五";
                    break;
                case 5:
                    weekNum = "周六";
                    break;
                case 6:
                    weekNum = "周日";
                    break;
                default:
                    weekNum = "周一";
            }
            daysOfWeek.add(weekNum + "_" + formattedDate);
        }
        return daysOfWeek;
    }


    @Override
    public Page<AttendanceDataDTO> getAttendanceHour(ManHourCriteriaDTO criteriaDTO) {
        if (criteriaDTO.getStartDate() == null && criteriaDTO.getEndDate() == null) {
            Integer weekly = criteriaDTO.getYear() * 100 + criteriaDTO.getWeek();
            criteriaDTO.setWeekly(weekly);
        }

        Page<AttendanceDataDTO> attendanceDataDTOS = drawingRecordRepository.searchAttendance(criteriaDTO);

        return attendanceDataDTOS;
    }

    @Override
    public File saveAttendanceDownloadFile(ManHourExportCriteriaDTO criteriaDTO, Long operatorId) throws ParseException {


        String templateFilePath = null;
        if (criteriaDTO.getStartDate() != null && criteriaDTO.getEndDate() != null) {
            templateFilePath = "/var/www/saint-whale/backend/resources/templates/export-attendance-monthly-manHour.xlsx";
        } else {
            templateFilePath = "/var/www/saint-whale/backend/resources/templates/export-attendance-manHour.xlsx";
        }
        String temporaryFileName = System.currentTimeMillis() + ".xlsx";
        String filePath = "/var/www/saint-whale/backend/private/upload/" + temporaryFileName;

        File excel = new File(temporaryDir, temporaryFileName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ManHourCriteriaDTO manHourCriteriaDTO = new ManHourCriteriaDTO();
        manHourCriteriaDTO.getPage().setNo(1);
        manHourCriteriaDTO.getPage().setSize(36655);
        manHourCriteriaDTO.setKeyword(criteriaDTO.getKeyword());
        if (criteriaDTO.getYear() != null && criteriaDTO.getWeek() != null) {
            manHourCriteriaDTO.setYear(criteriaDTO.getYear());
            manHourCriteriaDTO.setWeek(criteriaDTO.getWeek());
        }
//        manHourCriteriaDTO.setMonday(sdf.parse(criteriaDTO.getMonday()));
//        manHourCriteriaDTO.setTuesday(sdf.parse(criteriaDTO.getTuesday()));
//        manHourCriteriaDTO.setWednesday(sdf.parse(criteriaDTO.getWednesday()));
//        manHourCriteriaDTO.setThursday(sdf.parse(criteriaDTO.getThursday()));
//        manHourCriteriaDTO.setFriday(sdf.parse(criteriaDTO.getFriday()));
//        manHourCriteriaDTO.setSaturday(sdf.parse(criteriaDTO.getSaturday()));
//        manHourCriteriaDTO.setSunday(sdf.parse(criteriaDTO.getSunday()));
        if (criteriaDTO.getStartDate() != null && criteriaDTO.getEndDate() != null) {
            manHourCriteriaDTO.setStartDate(criteriaDTO.getStartDate());
            manHourCriteriaDTO.setEndDate(criteriaDTO.getEndDate());
        }


        manHourCriteriaDTO.setCompany(criteriaDTO.getCompany());
        manHourCriteriaDTO.setDivision(criteriaDTO.getDivision());
        manHourCriteriaDTO.setDepartment(criteriaDTO.getDepartment());
        manHourCriteriaDTO.setWeekly(criteriaDTO.getWeekly());

        List<AttendanceDataDTO> attendanceDataDTOS = getAttendanceHour(manHourCriteriaDTO).getContent();

        EasyExcel.write(filePath).withTemplate(templateFilePath).sheet("attendance_man_hour").doFill(attendanceDataDTOS);
        return excel;
    }

    @Override
    public File saveCheckDataDownloadFile(ReviewCriteriaDTO criteriaDTO, Long operatorId) throws Exception {
        String templateFilePath = null;
        if (criteriaDTO.getStartDate() != null && criteriaDTO.getEndDate() != null) {
            templateFilePath = "/var/www/saint-whale/backend/resources/templates/OceanSTAR Elite Group_Check Out Hour.xlsx";
        } else {
            templateFilePath = "/var/www/saint-whale/backend/resources/templates/OceanSTAR Elite Group_Check Out Hour.xlsx";
        }
        String temporaryFileName = System.currentTimeMillis() + ".xlsx";
        String filePath = "/var/www/saint-whale/backend/private/upload/" + temporaryFileName;
        File excel = new File(filePath);

        // 工作时长处理
        List<CheckOutHourDTO> checkOutHourDTOS = dingTalkWorkHourRepository.searchReview(criteriaDTO);
        for (CheckOutHourDTO data:checkOutHourDTOS) {
            //workHourDate   是不是周末
            LocalDate localDate = LocalDate.parse(data.getWorkHourDate());
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            boolean isWeekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
            if (isWeekend){
                if (data.getWorkDuration() != null){
                    data.setDingTalkOutHour(data.getWorkDuration() / 3600);
                } else {
                    data.setDingTalkOutHour(0.0);
                }
            } else {
                if(data.getWorkDuration() != null){
                    data.setDingTalkOutHour(data.getWorkDuration() / 3600 - 9);
                } else {
                    data.setDingTalkOutHour(0.0);
                }
            }
            if (data.getOutHour() != null && data.getDingTalkOutHour() != null){
                    data.setCheckHour(data.getOutHour() - data.getDingTalkOutHour());
            }

            // 计算工作时长
            if (data.getWorkDuration() != null) {
                double tempWorkHour =  (data.getWorkDuration() / 3600) - 1;
                data.setWorkHour(tempWorkHour);
//                // 提取整数部分
//                double integerPart = Math.floor(tempWorkHour);
//                // 提取小数部分
//                double decimalPart = tempWorkHour - integerPart;
//
//                if (decimalPart > 0.5) {
//                    data.setWorkHour(integerPart + 0.5);
//                } else if (decimalPart >= 0.5) {
//                    data.setWorkHour(tempWorkHour);
//                } else {
//                    data.setWorkHour(integerPart);
//                }
            }
        }

        // 请假时长处理
        List<DingTalkLeaveData> dingTalkLeaveData = dingTalkLeaveDataRepository.searchReview(criteriaDTO);

        List<LeaveDataDTO> res = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 用于存储同一天请假时长的累加结果
        Map<String, Double> dailyLeaveDurationMap = new HashMap<>();

        // 指定的日期
        String specifiedDateStr = "2025-01-27";
        Date specifiedDate = sdf.parse(specifiedDateStr);
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 第一次遍历：累加同一天的请假时长
        for (DingTalkLeaveData data : dingTalkLeaveData) {
            try {
                Date startDateFormat = sdf.parse(data.getStartLeaveTime());
                Date endDateFormat = sdf.parse(data.getEndLeaveTime());
                String startDate = sdf.format(startDateFormat);
                String endDate = sdf.format(endDateFormat);

                // 如果开始日期和结束日期是同一天
                if (startDate.equals(endDate)) {
                    String key = data.getJobNumber() + "_" + startDate;
                    double duration = data.getDurationPercent();
                    dailyLeaveDurationMap.put(key, dailyLeaveDurationMap.getOrDefault(key, 0.0) + duration);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // 第二次遍历：处理请假数据
        for (DingTalkLeaveData data : dingTalkLeaveData) {
            LeaveDataDTO leaveDataDTO = new LeaveDataDTO();
            try {
                Date startDateFormat = sdf.parse(data.getStartLeaveTime());
                Date endDateFormat = sdf.parse(data.getEndLeaveTime());
                String startDate = sdf.format(startDateFormat);
                String endDate = sdf.format(endDateFormat);
                String jobNumber = data.getJobNumber();
                String name = data.getUserName();
                List<Map<String, Object>> drawingRecord = drawingRecordRepository.drawingRecordsByLeave(startDate, endDate, jobNumber);
//                if (drawingRecord.size() != 0){
                    double drHour = 0.0;
//                    String name = data.getName();
                    String task = null;
                    String workHourDate = null;
                    StringBuilder dateRangeString = new StringBuilder();
//                    boolean foundMatch = false;
                    for (Map<String, Object> reviewDataMap : drawingRecord) {
                        if (reviewDataMap.get("username") != null && jobNumber.equals(reviewDataMap.get("username"))){
                            Date reviewDate = sdf.parse((String) reviewDataMap.get("work_hour_date"));
                            //判断满足时间范围
                            if ((reviewDate.equals(startDateFormat) || reviewDate.after(startDateFormat)) &&
                                (reviewDate.equals(endDateFormat) || reviewDate.before(endDateFormat))) {
                                drHour = drHour + (Double) reviewDataMap.get("work_hour");
                                if (dateRangeString.length() > 0) {
                                    dateRangeString.append(", ");
                                }
                                dateRangeString.append((String) reviewDataMap.get("work_hour_date"));
                            }
                            workHourDate = dateRangeString.toString();
                            name = (String) reviewDataMap.get("name");
                            task = (String) reviewDataMap.get("task");
//                            foundMatch = true;
                        }
                    }
//                    if (foundMatch){
                        leaveDataDTO.setJobNumber(data.getJobNumber());
                        leaveDataDTO.setEngineer(name);
                        leaveDataDTO.setStartLeaveTime(data.getStartLeaveTime());
                        leaveDataDTO.setEndLeaveTime(data.getEndLeaveTime());
                        leaveDataDTO.setWorkHourDate(workHourDate);
                        leaveDataDTO.setLeaveCode(data.getLeaveCode());
                        leaveDataDTO.setTask(task);
                        leaveDataDTO.setUserCompany(data.getUserCompany());
                        // 判断是否在同一天内多次请假，并累加请假时长
                        String key = jobNumber + "_" + startDate;
                        double durationPercent = dailyLeaveDurationMap.getOrDefault(key, data.getDurationPercent());

                        // 检查请假时间范围内是否包含指定日期的下午
                        Date leaveStartDate = sdfDateTime.parse(data.getStartLeaveTime());
                        Date leaveEndDate = sdfDateTime.parse(data.getEndLeaveTime());
                        // 新增月份判断逻辑
                        Calendar leaveStartCalendar = Calendar.getInstance();
                        leaveStartCalendar.setTime(leaveStartDate);
                        Calendar leaveEndCalendar = Calendar.getInstance();
                        leaveEndCalendar.setTime(leaveEndDate);
                        Calendar specifiedCalendar = Calendar.getInstance();
                        specifiedCalendar.setTime(specifiedDate);

//                        // 判断请假记录是否属于指定月份（一月）
//                        boolean isInSpecifiedMonth = (leaveStartCalendar.get(Calendar.YEAR) == specifiedCalendar.get(Calendar.YEAR) &&
//                            leaveStartCalendar.get(Calendar.MONTH) == specifiedCalendar.get(Calendar.MONTH));
//
//                        // 仅在属于指定月份时才检查特定日期下午
//                        if (isInSpecifiedMonth && isAfternoonOnSpecifiedDate(leaveStartDate, leaveEndDate, specifiedDate)) {
//                            if (durationPercent >= 8) {
//                                durationPercent -= 4;
//                            } else {
//                                durationPercent -= 4.5;
//                            }
//                            durationPercent = Math.max(durationPercent, 0);
//                        }

                        leaveDataDTO.setDurationPercent(durationPercent);
                        leaveDataDTO.setWorkHour(drHour);
                        leaveDataDTO.setCheckHour(durationPercent - drHour);
                        res.add(leaveDataDTO);
//                    }
//                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // 年假处理
        List<EmployeeDataDTO> employeeDataDTOS = employeeDataRepository.searchReview(criteriaDTO);

        for (EmployeeDataDTO data : employeeDataDTOS) {
            String initialEmploymentDate = data.getInitialEmploymentDate();
            if (initialEmploymentDate!= null) {

                // 计算当月请假 （年假、事假） Personal Leave
                double usedAnnualLeaveThisMonth = calculateUsedAnnualLeaveThisMonth(data.getEmployeeId(), dingTalkLeaveDataRepository);
                data.setUsedAnnualLeaveThisMonth(usedAnnualLeaveThisMonth);

                // 计算当月请假 （病假）Sick Leave
                double usedSickLeaveThisMonth = calculateUsedSickLeaveThisMonth(data.getEmployeeId(), dingTalkLeaveDataRepository);
                data.setUsedSickLeaveThisMonth(usedSickLeaveThisMonth);

                // 计算当月请假 （除 年假、事假、病假 以外的所有假）Other Leave
                double usedOtherLeaveThisMonth = calculateUsedOtherLeaveThisMonth(data.getEmployeeId(), dingTalkLeaveDataRepository);
                data.setUsedOtherLeaveThisMonth(usedOtherLeaveThisMonth);


            }
        }

        // 人工时对比,需要加上请假时长。
        List<CheckStandardWorkHourDTO> checkStandardWorkHourDTOS = drawingRecordRepository.checkStandardWorkHour(criteriaDTO);
        for (CheckStandardWorkHourDTO data:checkStandardWorkHourDTOS) {
            double totalHours = data.getTotalNormalManHour() + data.getTotalLeaveHour();
            data.setCheckStandardWorkHour(totalHours);
        }


        //通过模板创建多个sheet
        ExcelWriter writer = EasyExcel
            .write(filePath)
            .withTemplate(templateFilePath)
            .build();
        WriteSheet firstSheet = EasyExcel.writerSheet(0).build();
        WriteSheet secondSheet = EasyExcel.writerSheet(1).build();
        WriteSheet thirdSheet = EasyExcel.writerSheet(2).build();
        WriteSheet fourthSheet = EasyExcel.writerSheet(3).build();
        WriteSheet fifthSheet = EasyExcel.writerSheet(4).build();
        FillConfig fillConfig = FillConfig.builder()
            .forceNewRow(true)
            .build();

        // 执行填充操作
        writer.fill(checkOutHourDTOS, fillConfig, firstSheet);
        writer.fill(res, fillConfig, secondSheet);
        writer.fill(checkStandardWorkHourDTOS, fillConfig, thirdSheet);
        writer.fill(employeeDataDTOS, fillConfig, fourthSheet);
        writer.fill(employeeDataDTOS, fillConfig, fifthSheet);
        writer.finish();
        return excel;
    }

    public List<EmployeeDataDTO> getEmployeeDataList(ReviewCriteriaDTO criteriaDTO) throws Exception {
        List<EmployeeDataDTO> employeeDataDTOS = employeeDataRepository.searchReview(criteriaDTO);

        for (EmployeeDataDTO data : employeeDataDTOS) {
            String initialEmploymentDate = data.getInitialEmploymentDate();
            if (initialEmploymentDate!= null) {

                // 计算当月人工时
                String id = data.getId();
                double totalWorkHours = calculateTotalWorkHoursThisMonth(id, drawingRecordRepository);
                data.setTotalNormalManHour(totalWorkHours);

                // 计算当月请假 （年假、事假） Personal Leave
                double usedAnnualLeaveThisMonth = calculateUsedAnnualLeaveThisMonth(data.getEmployeeId(), dingTalkLeaveDataRepository);
                data.setUsedAnnualLeaveThisMonth(usedAnnualLeaveThisMonth);

                // 计算当月请假 （病假）Sick Leave
                double usedSickLeaveThisMonth = calculateUsedSickLeaveThisMonth(data.getEmployeeId(), dingTalkLeaveDataRepository);
                data.setUsedSickLeaveThisMonth(usedSickLeaveThisMonth);

                // 计算当月请假 （除 年假、事假、病假 以外的所有假）Other Leave
                double usedOtherLeaveThisMonth = calculateUsedOtherLeaveThisMonth(data.getEmployeeId(), dingTalkLeaveDataRepository);
                data.setUsedOtherLeaveThisMonth(usedOtherLeaveThisMonth);
            }
        }
        return employeeDataDTOS;
    }

    public EmployeeDataDTO getEmployeeDataDetail(String id) throws Exception {
//        List<EmployeeDataDTO> employeeDataDTOS = employeeDataRepository.searchReview(criteriaDTO);
        // 根据 id 查询单个 EmployeeDataDTO 对象
        EmployeeDataDTO employeeDataDTO = employeeDataRepository.findById(id);
        if (employeeDataDTO == null) {
          System.out.println("No data found for the given id: " + id);
        }

        String initialEmploymentDate = employeeDataDTO.getInitialEmploymentDate();
        if (initialEmploymentDate != null) {

            // 计算当月人工时
            double totalWorkHours = calculateTotalWorkHoursThisMonth(id, drawingRecordRepository);
            employeeDataDTO.setTotalNormalManHour(totalWorkHours);

            // 计算当月请假 （年假、事假）Personal Leave
            double usedAnnualLeaveThisMonth = calculateUsedAnnualLeaveThisMonth(employeeDataDTO.getEmployeeId(), dingTalkLeaveDataRepository);
            employeeDataDTO.setUsedAnnualLeaveThisMonth(usedAnnualLeaveThisMonth);

            // 计算当月请假 （病假）Sick Leave
            double usedSickLeaveThisMonth = calculateUsedSickLeaveThisMonth(employeeDataDTO.getEmployeeId(), dingTalkLeaveDataRepository);
            employeeDataDTO.setUsedSickLeaveThisMonth(usedSickLeaveThisMonth);

            // 计算当月请假 （除 年假、事假、病假 以外的所有假）Other Leave
            double usedOtherLeaveThisMonth = calculateUsedOtherLeaveThisMonth(employeeDataDTO.getEmployeeId(), dingTalkLeaveDataRepository);
            employeeDataDTO.setUsedOtherLeaveThisMonth(usedOtherLeaveThisMonth);
        }

        return employeeDataDTO;
    }

//    public EmployeeDataDTO getEmployeeDataDetail(Long employeeId) throws Exception {
//        ReviewCriteriaDTO criteriaDTO = new ReviewCriteriaDTO();
//        criteriaDTO.setUserId(employeeId);
//        List<EmployeeDataDTO> result = employeeDataRepository.searchReview(criteriaDTO);
//        return result.isEmpty() ? null : result.get(0);
//    }

    @Override
    public File saveWeeklyDataDownloadFile(ReviewCriteriaDTO criteriaDTO, Long operatorId) throws Exception {
        String templateFilePath = "/var/www/saint-whale/backend/resources/templates/OceanSTAREliteGroup_WeeklyManHour.xlsx";
        String temporaryFileName = System.currentTimeMillis() + ".xlsx";
        String filePath = "/var/www/saint-whale/backend/private/upload/" + temporaryFileName;

        File excel = new File(filePath);

        List<WeeklyManHourDTO> weeklyManHourDTOS = drawingRecordRepository.weeklyManHour(criteriaDTO);

        // 动态生成每周的列名（week_1、week_2、...、week_53）
        List<String> weeklyColumns = new ArrayList<>();
        for (int week = 1; week <= 53; week++) {
            weeklyColumns.add("week_" + week);
        }

        // 定义完整的表头（固定列 + 动态列）
        List<List<String>> headers = new ArrayList<>(); // 每个字段单独作为一个 List<String>
        headers.add(Collections.singletonList("工号"));
        headers.add(Collections.singletonList("姓名"));
        headers.add(Collections.singletonList("所属公司"));
        headers.add(Collections.singletonList("所属部门"));
        headers.add(Collections.singletonList("所属事业部"));
        headers.add(Collections.singletonList("所属团队"));
        headers.add(Collections.singletonList("项目名称"));

        // 添加每周的列名
        for (String weekColumn : weeklyColumns) {
            headers.add(Collections.singletonList(weekColumn));
        }

        // 使用 EasyExcel 写入数据
        EasyExcel.write(filePath)
            .sheet("weekly_man_hour")
            .head(headers) // 使用 List<List<String>> 格式的表头
            .doWrite(weeklyManHourDTOS.stream().map(dto -> {
                List<Object> row = new ArrayList<>();
                row.add(dto.getJobNumber());
                row.add(dto.getEmployeeName());
                row.add(dto.getCompany());
                row.add(dto.getDepartment());
                row.add(dto.getDivision());
                row.add(dto.getTeam());
                row.add(dto.getProjectName());

                // 动态添加每周的工时数据
                for (int week = 1; week <= 53; week++) {
                    String weekKey = "week_" + week;
                    Double hours = dto.getWeeklyHours().getOrDefault(weekKey, 0.0);
                    row.add(hours);
                }

                return row;
            }).collect(Collectors.toList()));

        return excel;

    }


    // 计算员工当月的总工时
    public static double calculateTotalWorkHoursThisMonth(String id, DrawingRecordRepository drawingRecordRepository) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        // 将月份减 1，得到上一个月
        calendar.add(Calendar.MONTH, -1);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfLastMonth = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endOfLastMonth = calendar.getTime();
        String startDate = sdf.format(startOfLastMonth);
        String endDate = sdf.format(endOfLastMonth);

        // 查询上一个月的工时数据
        Double totalWorkHours  = drawingRecordRepository.searchWorkHoursByJobNumberAndDateRange(startDate, endDate, id);
        return totalWorkHours != null ? totalWorkHours : 0;
    }

    // 计算当月请年假（年假、事假）时长（剔除假期和周末）
    public static double calculateUsedAnnualLeaveThisMonth(String jobNumber, DingTalkLeaveDataRepository dingTalkLeaveDataRepository) throws Exception {
        double usedAnnualLeave = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        // 将月份减 1，得到上一个月
        calendar.add(Calendar.MONTH, -1);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfLastMonth = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endOfLastMonth = calendar.getTime();
        String startDate = sdf.format(startOfLastMonth);
        String endDate = sdf.format(endOfLastMonth);

        // 指定的日期
        String specifiedDateStr = "2025-01-27";
        Date specifiedDate = sdf.parse(specifiedDateStr);

        List<Map<String, Object>> leaveData = dingTalkLeaveDataRepository.searchLeaveByJobNumberAndDateRange(startDate, endDate, jobNumber);
        for (Map<String, Object> leave : leaveData) {
            if (leave.get("leave_code").equals("Annual 年假") || leave.get("leave_code").equals("Personal 事假")) {
                double duration = calculateLeaveDurationInLastMonth(leave, startDate, endDate);

//                // 检查请假时间范围内是否包含指定日期
//                SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date leaveStartDate = sdfDateTime.parse((String) leave.get("start_leave_time"));
//                Date leaveEndDate = sdfDateTime.parse((String) leave.get("end_leave_time"));
//
//                // 新增月份判断逻辑
//                Calendar leaveStartCalendar = Calendar.getInstance();
//                leaveStartCalendar.setTime(leaveStartDate);
//                Calendar leaveEndCalendar = Calendar.getInstance();
//                leaveEndCalendar.setTime(leaveEndDate);
//                Calendar specifiedCalendar = Calendar.getInstance();
//                specifiedCalendar.setTime(specifiedDate);
//
//                // 判断请假记录是否属于指定月份（一月）
//                boolean isInSpecifiedMonth = (leaveStartCalendar.get(Calendar.YEAR) == specifiedCalendar.get(Calendar.YEAR) &&
//                    leaveStartCalendar.get(Calendar.MONTH) == specifiedCalendar.get(Calendar.MONTH));
//
//                // 仅在属于指定月份时才检查特定日期下午
//                if (isInSpecifiedMonth && isAfternoonOnSpecifiedDate(leaveStartDate, leaveEndDate, specifiedDate)) {
//                    if (duration >= 8) {
//                        duration -= 4;
//                    } else {
//                        duration -= 4.5;
//                    }
//                    duration = Math.max(duration, 0);
//                }
                usedAnnualLeave += duration;
            }
        }
        return usedAnnualLeave;
    }

//    // 判断是否在指定日期的下午请假，并且仅对指定月份内的部分进行扣除
//    private static boolean isAfternoonOnSpecifiedDate(Date leaveStartDate, Date leaveEndDate, Date specifiedDate) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(specifiedDate);
//
//        // 设置指定日期的下午时间范围（12:00:00 至 23:59:59）
//        calendar.set(Calendar.HOUR_OF_DAY, 12);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        Date specifiedDateAfternoonStart = calendar.getTime();
//
//        calendar.set(Calendar.HOUR_OF_DAY, 23);
//        calendar.set(Calendar.MINUTE, 59);
//        calendar.set(Calendar.SECOND, 59);
//        Date specifiedDateAfternoonEnd = calendar.getTime();
//
//        // 判断请假时间范围是否与指定日期的下午时间范围有重叠
//        boolean overlapsAfternoon = (leaveStartDate.before(specifiedDateAfternoonEnd) && leaveEndDate.after(specifiedDateAfternoonStart));
//
//        // 确保请假日期范围属于指定日期所在的月份
//        Calendar leaveStartCalendar = Calendar.getInstance();
//        leaveStartCalendar.setTime(leaveStartDate);
//        Calendar leaveEndCalendar = Calendar.getInstance();
//        leaveEndCalendar.setTime(leaveEndDate);
//        Calendar specifiedCalendar = Calendar.getInstance();
//        specifiedCalendar.setTime(specifiedDate);
//
//        // 判断请假时间范围是否跨越了不同月份
//        boolean isSameMonth = (leaveStartCalendar.get(Calendar.YEAR) == specifiedCalendar.get(Calendar.YEAR) &&
//            leaveStartCalendar.get(Calendar.MONTH) == specifiedCalendar.get(Calendar.MONTH));
//
//        // 如果请假时间范围跨越了不同月份，仅对指定月份内的部分进行判断
//        if (!isSameMonth) {
//            // 如果请假开始日期在指定日期之前，且结束日期在指定日期之后
//            if (leaveStartDate.before(specifiedDate) && leaveEndDate.after(specifiedDate)) {
//                // 仅对指定日期当天进行判断
//                return overlapsAfternoon;
//            }
//            // 如果请假时间范围完全在指定日期之后，且不属于指定月份，则不扣除
//            return false;
//        }
//
//        // 如果请假时间范围完全在指定月份内，正常判断是否重叠
//        return overlapsAfternoon;
//    }

    // 计算当月请病假时长
    public static double calculateUsedSickLeaveThisMonth(String jobNumber, DingTalkLeaveDataRepository dingTalkLeaveDataRepository) throws Exception {
        double usedSickLeave = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        // 将月份减 1，得到上一个月
        calendar.add(Calendar.MONTH, -1);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfLastMonth = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endOfLastMonth = calendar.getTime();
        String startDate = sdf.format(startOfLastMonth);
        String endDate = sdf.format(endOfLastMonth);

        List<Map<String, Object>> leaveData = dingTalkLeaveDataRepository.searchLeaveByJobNumberAndDateRange(startDate, endDate, jobNumber);
        for (Map<String, Object> leave : leaveData) {
            if (leave.get("leave_code").equals("Sick 病假")) {
//                usedSickLeave += (Double) leave.get("duration_percent");
                double duration = calculateLeaveDurationInLastMonth(leave, startDate, endDate);
                usedSickLeave += duration;
            }
        }
        return usedSickLeave;
    }

    // 计算当月请假 除 年假、病假 时长
    public static double calculateUsedOtherLeaveThisMonth(String jobNumber, DingTalkLeaveDataRepository dingTalkLeaveDataRepository) throws Exception {
        double usedOtherLeave = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        // 将月份减 1，得到上一个月
        calendar.add(Calendar.MONTH, -1);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfLastMonth = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endOfLastMonth = calendar.getTime();
        String startDate = sdf.format(startOfLastMonth);
        String endDate = sdf.format(endOfLastMonth);

        List<Map<String, Object>> leaveData = dingTalkLeaveDataRepository.searchLeaveByJobNumberAndDateRange(startDate, endDate, jobNumber);
        for (Map<String, Object> leave : leaveData) {
            String leaveCode = (String) leave.get("leave_code");
            if (!leaveCode.equals("Annual 年假") && !leaveCode.equals("Personal 事假") && !leaveCode.equals("Sick 病假")) {
//                usedOtherLeave += (Double) leave.get("duration_percent");
                double duration = calculateLeaveDurationInLastMonth(leave, startDate, endDate);
                usedOtherLeave += duration;
            }
        }
        return usedOtherLeave;
    }

    // 计算周末日期
    private static List<String> findWeekends(String startDateStr, String endDateStr) {
        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<String> weekends = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                weekends.add(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            currentDate = currentDate.plusDays(1);
        }
        return weekends;
    }

    // 获取指定日期范围内的假期和周末列表
    private static List<String> getHolidayAndWeekendDates(String startDateStr, String endDateStr) {
        List<String> allHolidayDate = holidayDateRepository.findAllHolidayDateByCountry(startDateStr, endDateStr, "China");
        List<String> weekends = findWeekends(startDateStr, endDateStr);
        List<String> workDays = workDayRepository.findAllHolidayDate(startDateStr, endDateStr);
        if (workDays != null && workDays.size() > 0) {
            weekends = weekends.stream()
                .filter(date -> !workDays.contains(date))
                .collect(Collectors.toList());
        }
        return Stream.concat(allHolidayDate.stream(), weekends.stream())
            .distinct()
            .collect(Collectors.toList());
    }

    private static double calculateLeaveDurationInLastMonth(
        Map<String, Object> leave, String startDateStr, String endDateStr) throws ParseException {
        // 定义日期时间格式和日期格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 获取请假开始和结束日期时间
        String startLeaveTime = (String) leave.get("start_leave_time");
        String endLeaveTime = (String) leave.get("end_leave_time");

        // 解析包含时间的日期字符串为 LocalDateTime 对象
        LocalDateTime leaveStartDateTime = LocalDateTime.parse(startLeaveTime, dateTimeFormatter);
        LocalDateTime leaveEndDateTime = LocalDateTime.parse(endLeaveTime, dateTimeFormatter);

        // 从 LocalDateTime 对象中提取日期部分
        LocalDate leaveStartDate = leaveStartDateTime.toLocalDate();
        LocalDate leaveEndDate = leaveEndDateTime.toLocalDate();

        // 获取上个月的第一天和最后一天
        LocalDate startOfLastMonth = LocalDate.parse(startDateStr, dateFormatter);
        LocalDate endOfLastMonth = LocalDate.parse(endDateStr, dateFormatter);

        // 计算交集范围
        LocalDate effectiveStartDate = leaveStartDate.isBefore(startOfLastMonth) ? startOfLastMonth : leaveStartDate;
        LocalDate effectiveEndDate = leaveEndDate.isAfter(endOfLastMonth) ? endOfLastMonth : leaveEndDate;

        List<String> holidaysAndWeekends = getHolidayAndWeekendDates(effectiveStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            effectiveEndDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // 计算请假天数（剔除假期和周末）
        double duration = 0;
        for (LocalDate date = effectiveStartDate; !date.isAfter(effectiveEndDate); date = date.plusDays(1)) {
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!holidaysAndWeekends.contains(formattedDate)) {
                duration += 1; // 只计算工作日
            }
        }

        // 获取请假时长
        double totalDuration = (Double) leave.get("duration_percent");

        // 返回上个月内的请假时长（不超过实际请假时长）
        return Math.min(duration * 8, totalDuration);
    }



    @Override
    public AttendanceFilterDTO filter() {
        List<String> companyList = drawingRecordRepository.findByCompany();
        List<String> divisionList = drawingRecordRepository.findByDivision();
        List<String> departmentList = drawingRecordRepository.findByDepartment();
        List<String> teamList = drawingRecordRepository.findByTeam();
        List<String> titleList = drawingRecordRepository.findByTitle();

        AttendanceFilterDTO attendanceFilterDTO = new AttendanceFilterDTO();
        attendanceFilterDTO.setCompanyList(companyList);
        attendanceFilterDTO.setDivisionList(divisionList);
        attendanceFilterDTO.setDepartmentList(departmentList);
        attendanceFilterDTO.setTeamList(teamList);
        attendanceFilterDTO.setTitleList(titleList);

        return attendanceFilterDTO;
    }

    @Override
    public AttendanceFilterDTO filterWeekly(ManHourCriteriaDTO criteriaDTO) {
        List<Integer> weeklyList = drawingRecordRepository.findByWeekly(criteriaDTO.getStartDate(), criteriaDTO.getEndDate());

        AttendanceFilterDTO attendanceFilterDTO = new AttendanceFilterDTO();
        attendanceFilterDTO.setWeeklyList(weeklyList);

        return attendanceFilterDTO;
    }

    @Override
    public void locked(ManHourCriteriaDTO criteriaDTO, ContextDTO context) {
        List<String> dates = new ArrayList<>();
        if (criteriaDTO.getType().equals("monthly")) {
            dates = getDateListByRange(criteriaDTO.getStartDate(), criteriaDTO.getEndDate());
        } else {
            Integer year = criteriaDTO.getYear();
            List<Integer> weeks = criteriaDTO.getWeeks();
            dates = new ArrayList<>();
            for (Integer week : weeks) {
                dates.addAll(getDateListByYearAndWeek(year, week));
            }
        }

        //判断是否存在
        List<String> existedDate = attendanceRecordRepository.findExistedDate(dates);

        if (existedDate != null && existedDate.size() > 0) {
            String message = existedDate.toString();
            throw new BusinessError(message + " already locked");
        }

        for (String date : dates) {
            AttendanceRecord item = new AttendanceRecord();
            item.setLockedDate(date);
            item.setStatus(EntityStatus.ACTIVE);
            attendanceRecordRepository.save(item);
        }


        // 向notification服务添加消息
        NotificationCreateDTO notificationCreateDTO = new NotificationCreateDTO();
        if (dates.size() > 1) {
            notificationCreateDTO.setContent("staff： " + dates.get(0) + "~" + dates.get(dates.size() - 1) + " is already locked");
        } else {
            notificationCreateDTO.setContent("staff： " + dates.get(0) + " is already locked");
        }
        notificationCreateDTO.setCreatorName(context.getOperator().getName());
        notificationCreateDTO.setTitle("Attendance date lock");
        notificationCreateDTO.setType(NotificationType.ATTENDANCE_LOCKED);
//        notificationCreateDTO.setUserName(users.get(i).getUsername());
//        notificationCreateDTO.setName(users.get(i).getName());
        notificationFeignAPI.create(notificationCreateDTO);

    }

    @Override
    public Page<AttendanceRecord> getAttendanceLockedList(ManHourCriteriaDTO criteriaDTO) {
        return attendanceRecordRepository.getList(criteriaDTO);
    }

    @Override
    public void deleteLocked(Long lockedId, ContextDTO context) {
        AttendanceRecord attendanceRecord = attendanceRecordRepository.findById(lockedId).orElse(null);
        if (attendanceRecord == null) {
            throw new BusinessError();
        }
        attendanceRecord.setDeleted(true);
        attendanceRecord.setDeletedAt(new Date());
        attendanceRecord.setStatus(EntityStatus.DELETED);
        attendanceRecord.setDeletedBy(context.getOperator().getId());

        attendanceRecordRepository.save(attendanceRecord);
    }

    @Override
    public Page<ReviewDataDTO> searchReview(ReviewCriteriaDTO criteriaDTO, Long userId) {

        if (criteriaDTO.getOrgType().equals(OrgType.PROJECT.toString())){

            JsonListResponseBody<UserProfile> userProfileJsonListResponseBody = userFeignAPI.searchProjectOrgUserList(userId, criteriaDTO.getOrgType());
            List<UserProfile> userProfiles = userProfileJsonListResponseBody.getData();

            if (!userProfiles.isEmpty()){
                List<Long> ids = userProfiles.stream().map(UserProfile::getId).collect(Collectors.toList());
                criteriaDTO.setUserIds(ids);
            }

            JsonListResponseBody<Organization> rootOrgListByUserId = userFeignAPI.getRootOrgListByUserId(userId, criteriaDTO.getOrgType());
            List<Organization> organizations = rootOrgListByUserId.getData();
            if (!organizations.isEmpty()){
                List<Long> rootOrgIds = organizations.stream().map(Organization::getId).collect(Collectors.toList());
                criteriaDTO.setOrgIds(rootOrgIds);
            }
        }else if (criteriaDTO.getOrgType().equals(OrgType.DEPARTMENT.toString())){
            JsonListResponseBody<UserProfile> userProfileJsonListResponseBody = userFeignAPI.searchProjectOrgUserList(userId, criteriaDTO.getOrgType());
            List<UserProfile> userProfiles = userProfileJsonListResponseBody.getData();

            if (!userProfiles.isEmpty()){
                List<Long> ids = userProfiles.stream().map(UserProfile::getId).collect(Collectors.toList());
                criteriaDTO.setUserIds(ids);
            }
        }
        criteriaDTO.setUserId(userId);

        return drawingRecordRepository.searchReview(criteriaDTO);
    }

    @Override
    public Page<ReviewDataDTO> searchTimesheetList(ReviewCriteriaDTO criteriaDTO, ContextDTO context) {
        //1.根据传过来的OrgType查询相关的组织架构树
        Long userId = context.getOperator().getId();

        return null;
    }

    @Override
    public Page<ReviewDataDTO> searchHour(HourDashboardCriteriaDTO criteriaDTO) {
        return null;
    }


    private List<String> getDateListByYearAndWeek(int year, int week) {
        List<String> dateList = new ArrayList<>();

        // 设置年份和周次
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, week);

        // 将日期调整到指定周次的第一天
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        // 格式化日期为字符串并添加到列表中
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 7; i++) {
            dateList.add(formatter.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dateList;
    }

    private List<String> getDateListByRange(String startDateString, String endDateString) {
        List<String> dateList = new ArrayList<>();

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = formatter.parse(startDateString);
            Date endDate = formatter.parse(endDateString);

            // 设置起始日期
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            // 将日期依次添加到列表中
            while (!calendar.getTime().after(endDate)) {
                dateList.add(formatter.format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateList;
    }
}
