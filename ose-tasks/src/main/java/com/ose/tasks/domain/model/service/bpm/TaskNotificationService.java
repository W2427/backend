package com.ose.tasks.domain.model.service.bpm;

import com.ose.dto.jpql.TaskProcQLDTO;
import com.ose.notifications.api.NotificationFeignAPI;
import com.ose.notifications.dto.NotificationPostDTO;
import com.ose.notifications.dto.receiver.TeamReceiverDTO;
import com.ose.notifications.vo.NotificationType;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.qc.ExInspActInstRelationRepository;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务通知服务。
 */
@Component
public class TaskNotificationService implements TaskNotificationInterface {


    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmHiTaskinstRepository hiTaskinstRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final BpmExInspScheduleRepository externalInspectionScheduleRepository;

    private final NotificationFeignAPI notificationFeignAPI;

    private final QCReportRepository qcReportRepository;

    private final ExInspActInstRelationRepository exInspActInstRelationRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;


    /**
     * 构造方法。
     */
    @Autowired
    public TaskNotificationService(
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmHiTaskinstRepository hiTaskinstRepository,
        BpmRuTaskRepository ruTaskRepository,
        BpmExInspScheduleRepository externalInspectionScheduleRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") NotificationFeignAPI notificationFeignAPI,
        QCReportRepository qcReportRepository,
        ExInspActInstRelationRepository exInspActInstRelationRepository, BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository) {
        this.bpmActInstRepository = bpmActInstRepository;
        this.hiTaskinstRepository = hiTaskinstRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.externalInspectionScheduleRepository = externalInspectionScheduleRepository;
        this.notificationFeignAPI = notificationFeignAPI;

        this.qcReportRepository = qcReportRepository;
        this.exInspActInstRelationRepository = exInspActInstRelationRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
    }

    @Override
    public void sendNotification(Long orgId, Long projectId, List<Long> taskIds) {

        boolean send = false;

        Map<String, String> parameters = new HashMap<>();
        NotificationType type = null;

        Long actTaskId = taskIds.get(0);
        BpmHiTaskinst hiTask = hiTaskinstRepository.findByTaskId(actTaskId);
        if (hiTask != null) {
            Long actInstId = hiTask.getActInstId();
            Optional<BpmActivityInstanceBase> opAct = bpmActInstRepository.findById(actInstId);
            if (opAct.isPresent()) {
                send = true;
                BpmActivityInstanceBase actInst = opAct.get();

                String entityNo = actInst.getEntityNo();

                if (actInst.getProcess().equals(BpmCode.ENGINEERING)) {
                    parameters.put("drawingNo", entityNo);
                    type = NotificationType.DRAWING_REVIEW_WORKFLOW_STATE_CHANGE;
                } else if (actInst.getProcess().equals(BpmCode.DRAWING_PARTIAL_UPDATE) || actInst.getProcess().equals(BpmCode.DRAWING_INTEGRAL_UPDATE) ) {
                    parameters.put("drawingNo", entityNo);
                    type = NotificationType.DRAWING_MODIFYING_WORKFLOW_STATE_CHANGE;
                }/* else if(actInst.getProcess().equals(ENGINEERING_MTO)) {
                    parameters.put("drawingNo", entityNo);
                    type = NotificationType.DRAWING_MODIFYING_WORKFLOW_STATE_CHANGE;
                } */ else {
                    send = false;
                }

                List<BpmRuTask> ruTaskList = ruTaskRepository.findByActInstId(actInstId);
                if (!ruTaskList.isEmpty()) {
                    String currentTask = "";
                    for (BpmRuTask ruTask : ruTaskList) {
                        currentTask += ruTask.getName();
                    }
                    parameters.put("currentTask", currentTask);
                } else {
                    parameters.put("currentTask", BpmCode.END);
                }
            }
        }

        if (send) {
            this.send(orgId, projectId, parameters, type);
        }
    }

    private void send(Long orgId, Long projectId, Map<String, String> parameters, NotificationType type) {
        NotificationPostDTO notificationDTO = new NotificationPostDTO();
        notificationDTO.setParameters(parameters);
        Set<TeamReceiverDTO> teamDTO = new HashSet<>();
        TeamReceiverDTO team = new TeamReceiverDTO();
        team.setTeamId(orgId);
        teamDTO.add(team);
        notificationDTO.setTeams(teamDTO);
        notificationFeignAPI.send(orgId, projectId, type, notificationDTO);
    }

    @Override
    public void sendDrawingVersionNotification(Long orgId, Long projectId, String drawingNo, String version) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("drawingNo", drawingNo);
            parameters.put("currentVersion", version);

            NotificationType type = NotificationType.DRAWING_CONFIGURATION_DETAILS;
            this.send(orgId, projectId, parameters, type);
        } catch (Error e) {
            e.printStackTrace(System.out);
        }
    }

    @Override
    public void sendExternalInspectionNotification(Long orgId, Long projectId,
                                                   List<Long> externalInspectionApplyScheduleIds) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = sdf.format(date);

        for (Long scheduleId : externalInspectionApplyScheduleIds) {
            Optional<BpmExInspSchedule> op = externalInspectionScheduleRepository.findById(scheduleId);
            if (op.isPresent()) {
                BpmExInspSchedule schedule = op.get();
                List<String> scheduleSeriesNo = new ArrayList<>();
                List<String> reportSeries = new ArrayList<>();

                List<TaskProcQLDTO> taskProcQLDTOs = exInspActInstRelationRepository.findTaskIdByOrgIdAndProjectIdAndExInspScheduleId(
                    orgId, projectId, scheduleId
                );

                List<Long> tIds = taskProcQLDTOs.stream().map(TaskProcQLDTO::getTaskId).collect(Collectors.toList());

                List<Long> taskIds = new ArrayList<>(tIds);
                List<QCReport> qcReports = qcReportRepository.findByScheduleIdAndReportStatusNot(scheduleId,ReportStatus.CANCEL);
                qcReports.forEach(tmpReport -> {
                    reportSeries.add(tmpReport.getSeriesNo());
                });
                scheduleSeriesNo.add(schedule.getName() + "-" + String.join(":", reportSeries));

                Set<Long> actInstIds = new HashSet<>();
                for (Long taskId : taskIds) {
                    BpmHiTaskinst hiTask = hiTaskinstRepository.findByTaskId(taskId);
                    if (hiTask != null) {
                        actInstIds.add(hiTask.getActInstId());
                    }
                }

                String entityNos = "";
                String process = "";
                String processStage = "";
                String workSiteName = "";
                Iterator<Long> iterator = actInstIds.iterator();
                while (iterator.hasNext()) {
                    Long actInstId = iterator.next();
                    Optional<BpmActivityInstanceBase> opAct = bpmActInstRepository.findByProjectIdAndId(projectId, actInstId);
                    if (opAct.isPresent()) {
                        BpmActivityInstanceBase actInst = opAct.get();
                        BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByBaiId(actInst.getId());
                        entityNos += ", " + actInst.getEntityNo();
                        if (StringUtils.isBlank(process)) {
                            process = actInst.getProcess();
                        }
                        if (StringUtils.isBlank(processStage)) {
                            processStage = actInst.getProcessStage();
                        }
                        if (StringUtils.isBlank(workSiteName) && actInstState != null) {
                            workSiteName = actInstState.getWorkSiteName();
                        }
                    }
                }
                if (!entityNos.equals("")) {
                    entityNos = entityNos.substring(2);
                    entityNos = processStage + "-" + process + ":" + entityNos;
                }

                Map<String, String> parameters = new HashMap<>();
                parameters.put("date", dateStr);
                parameters.put("entityNo", entityNos);
                parameters.put("location", workSiteName);

                this.send(orgId, projectId, parameters, NotificationType.CONSTRUCTION_EXTERNAL_INSPECTION);
            }
        }
    }

    @Override
    public void sendInternalInspectionNotification(Long orgId, Long projectId, BpmActivityInstanceBase actInst) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = sdf.format(date);

        String entityNo = actInst.getProcessStage() + "-" + actInst.getProcess() + ":" + actInst.getEntityNo();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("date", dateStr);
        parameters.put("entityNo", entityNo);
        BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByBaiId(actInst.getId());
        if(actInstState != null)
            parameters.put("location", actInstState.getWorkSiteName());


        this.send(orgId, projectId, parameters, NotificationType.CONSTRUCTION_INTERNAL_INSPECTION);
    }

}
