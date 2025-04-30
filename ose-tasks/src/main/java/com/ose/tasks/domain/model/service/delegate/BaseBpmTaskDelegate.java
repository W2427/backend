package com.ose.tasks.domain.model.service.delegate;

import com.ose.report.entity.ReportHistory;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.vo.bpm.BpmCode;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 工作流任务服务接口
 */
public class BaseBpmTaskDelegate extends StringRedisService {

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmRuTaskRepository ruTaskRepository;
    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;


    public BaseBpmTaskDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                               StringRedisTemplate stringRedisTemplate,
                               BpmRuTaskRepository ruTaskRepository,
                               BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository) {
        super(stringRedisTemplate);
        this.bpmActInstRepository = bpmActInstRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
    }

    boolean updateRunTaskReportInfo(BpmRuTaskRepository ruTaskRepository, ReportHistory reportHistory,
                                    Long taskId) {
        BpmRuTask bpmRuTask = ruTaskRepository.findById(taskId).orElse(null);

        Long reportFileId = reportHistory.getFileId();

        String reportQrCode = reportHistory.getReportQrCode();
        String reportSerial = reportHistory.getReportNo();

        if (bpmRuTask != null) {
            List<ActReportDTO> reports = new ArrayList<ActReportDTO>();
            ActReportDTO report = new ActReportDTO();
            report.setFileId(reportFileId);
            report.setReportQrCode(reportQrCode);
            report.setReportNo(reportSerial);
            reports.add(report);
            bpmRuTask.setJsonDocuments(reports);
            ruTaskRepository.save(bpmRuTask);
        }

        return true;
    }


    protected void setFpy(Long projectId, Long[] taskIds, Map<String, Object> command) {


        if (command.containsKey(BpmCode.EXCLUSIVE_GATEWAY_RESULT)) {
            for (int i = 0; i < taskIds.length; i++) {
                Long taskId = taskIds[i];
                BpmRuTask ruTask = ruTaskRepository.findById(taskId).orElse(null);
                if (ruTask != null) {
                    BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByProjectIdAndBaiId(projectId, ruTask.getActInstId());
                }
            }
        }

    }


}
