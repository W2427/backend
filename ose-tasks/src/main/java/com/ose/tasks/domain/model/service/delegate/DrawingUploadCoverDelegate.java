package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.vo.bpm.BpmCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.ose.tasks.vo.setting.BatchTaskCode.DRAWING_PACKAGE;

/**
 * ISSUE 上传封面 节点 代理
 */
@Component
public class DrawingUploadCoverDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final DrawingRepository drawingRepository;

    private final DrawingBaseInterface drawingBaseService;


    private final BatchTaskInterface batchTaskService;


    @Autowired
    public DrawingUploadCoverDelegate(
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmRuTaskRepository ruTaskRepository,
        DrawingRepository drawingRepository,
        DrawingBaseInterface drawingBaseService,
        BatchTaskInterface batchTaskService,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        StringRedisTemplate stringRedisTemplate) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.drawingRepository = drawingRepository;
        this.drawingBaseService = drawingBaseService;
        this.batchTaskService = batchTaskService;
    }


    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {
        Long projectId = execResult.getProjectId();
        OperatorDTO operatorDTO = (OperatorDTO) data.get("operator");
        Long orgId = (Long) data.get("orgId");
        Map<String, Object> variables = execResult.getVariables();
        List<ActReportDTO> attachments = (List<ActReportDTO>) data.get("attachments");
        Long actInstId = execResult.getRuTask().getActInstId();

        if (variables == null) {
            execResult = drawingBaseService.getDrawingAndLatestRev(execResult);
            if (!execResult.isExecResult()) {
                return execResult;
            }
        }
        Drawing dwg = (Drawing) execResult.getVariables().get("drawing");



        if (attachments == null || attachments.size() != 1) {
            execResult.setExecResult(false);
            execResult.setErrorDesc("封面必须上传且只能有一个");
            return execResult;
        }

        ActReportDTO actReportDTO = attachments.get(0);
        String fileType = actReportDTO.getFilePath().substring(actReportDTO.getFilePath().length() - 4);
        if (fileType.equals("." + BpmCode.FILE_TYPE_PDF) || fileType.equals("." + BpmCode.FILE_TYPE_PDF_EXTEND)) {
            dwg.setCoverId(actReportDTO.getFileId());
            dwg.setCoverName(actReportDTO.getReportNo());
            dwg.setCoverPath(actReportDTO.getFilePath());
            drawingRepository.save(dwg);
        }









































        return execResult;
    }

    /**
     * 新起线程打包图纸文件。
     */
    private void packSubFiles(
        Long orgId,
        Project project,
        Long actInstId,
        OperatorDTO operatorDTO,
        Drawing dwg,
        ExecResultDTO execResult,
        DrawingDetail drawingDetail,
        ContextDTO contextDTO
    ) {
        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());
        batchTaskService.run(
            contextDTO, project, DRAWING_PACKAGE,
            batchTask -> {
                drawingBaseService.packSubFiles(
                    orgId,
                    project,
                    actInstId,
                    operatorDTO,
                    true,
                    dwg,
                    execResult.getProcessId(),
                    drawingDetail);
                return new BatchResultDTO();
            }
        );
    }

}
