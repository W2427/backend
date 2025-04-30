package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.report.api.PipelineFitUpReportFeignAPI;
import com.ose.report.dto.InspectionApplicationPostDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.service.ProjectService;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmExInspSchedule;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.Project;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户服务。
 */
@Component
public class FitupInspectionDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final BpmExInspScheduleRepository exInspScheduleRepository;

    private PipelineFitUpReportFeignAPI reportAPI;

    private ProjectService projectService;

    /**
     * 构造方法。
     */
    @Autowired
    public FitupInspectionDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                   BpmRuTaskRepository ruTaskRepository,
                                   @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") PipelineFitUpReportFeignAPI reportAPI,
                                   ProjectService projectService,
                                   StringRedisTemplate stringRedisTemplate,
                                   BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                   BpmExInspScheduleRepository exInspScheduleRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.bpmActInstRepository = bpmActInstRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.reportAPI = reportAPI;
        this.projectService = projectService;
        this.exInspScheduleRepository = exInspScheduleRepository;
    }


    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {
        Long[] taskIds = (Long[]) data.get("actTaskIds");

        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");




        OperatorDTO operatorDTO = (OperatorDTO) data.get("operator");
        String operator = operatorDTO.getName();

        final Project project = projectService.get(orgId, projectId);

        String inspectioncontents = "";

        String content = "";

        for (int i = 0; i < taskIds.length; i++) {
            BpmRuTask bpmRuTask = ruTaskRepository.findById(taskIds[i]).orElse(null);
            if (bpmRuTask != null) {
                Optional<BpmActivityInstanceBase> actInstOp = bpmActInstRepository
                    .findByProjectIdAndId(projectId, bpmRuTask.getActInstId());
                if (actInstOp.isPresent()) {
                    BpmActivityInstanceBase actInst = actInstOp.get();



                    if (inspectioncontents.equals("")) {
                        inspectioncontents += actInst.getEntitySubType() + "  " + actInst.getProcessStage() + "-"
                            + actInst.getProcess() + ":\r\n";
                    }
                    content += actInst.getEntityNo() + ", ";
                }
            }
        }

        if (!content.equals("")) {
            content = content.substring(0, content.lastIndexOf(","));
            inspectioncontents += content;
        }

        InspectionApplicationPostDTO dto = new InspectionApplicationPostDTO();

        dto.setProjectName(project.getName());
        dto.setApplicantName(operator);
        dto.setApplicantTel("15301076711");
        dto.setApplyingDate(new Date());
        dto.setInspectionContents(inspectioncontents);
        dto.setInspectionLocation("");
        dto.setInspectionDate(null);
        dto.setReportNo("P" + StringUtils.generateShortUuid());

        ReportHistory reportHistory = null;
        if (!inspectioncontents.equals("")) {
            JsonObjectResponseBody<ReportHistory> response = reportAPI.postApplicationForPipelineFitUpInspection(orgId,
                projectId, dto);
            reportHistory = response.getData();
        }

        for (int i = 0; i < taskIds.length; i++) {
            updateRunTaskReportInfo(ruTaskRepository, reportHistory, taskIds[i]);
        }

        if (reportHistory != null) {
            List<ActReportDTO> reports = new ArrayList<>();
            ActReportDTO reportDTO = new ActReportDTO();
            reportDTO.setFileId(reportHistory.getFileId());
            reportDTO.setFilePath(reportHistory.getFilePath());
            reportDTO.setReportNo(reportHistory.getReportNo());
            reportDTO.setReportQrCode(reportHistory.getReportQrCode());
            reports.add(reportDTO);
            BpmExInspSchedule exInspSchedule = new BpmExInspSchedule();
            exInspSchedule.setCreatedAt();
            exInspSchedule.setStatus(EntityStatus.ACTIVE);
            exInspScheduleRepository.save(exInspSchedule);
        }

        return execResult;
    }


}
