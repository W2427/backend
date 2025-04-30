package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.util.DateUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 会签节点代理。
 */
@Component
public class DrawingDispatchDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final DrawingDetailRepository drawingDetailRepository;
    private final DrawingRepository drawingRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public DrawingDispatchDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                   DrawingDetailRepository drawingDetailRepository,
                                   BpmRuTaskRepository ruTaskRepository,
                                   StringRedisTemplate stringRedisTemplate,
                                   BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                   DrawingRepository drawingRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.drawingDetailRepository = drawingDetailRepository;
        this.drawingRepository = drawingRepository;
    }


    /**
     * 任务处理后。
     *
     * @param contextDTO
     * @param data
     * @param execResult
     * @return
     */
    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {
        Map<String, Object> variables = execResult.getTodoTaskDTO().getVariables();
        List<ActReportDTO> attachments = (List<ActReportDTO>) data.get("attachments");

        Drawing drawing = drawingRepository.findById(execResult.getActInst().getEntityId()).orElse(null);

        if (drawing == null) {
            execResult.setExecResult(false);
            execResult.setErrorDesc("图纸未找到");
            return execResult;
        }

        drawingIssue(drawing, variables);




//        if (attachments == null || attachments.size() == 0 || attachments.size() != 1) {
//            execResult.setExecResult(false);
//            execResult.setErrorDesc("附件必须上传且只能有一个");
//            return execResult;
//        }

        if (attachments.size() > 0) {
            if (attachments.size() != 1) {
                execResult.setExecResult(false);
                execResult.setErrorDesc("附件必须上传且只能有一个");
                return execResult;
            }
            ActReportDTO actReportDTO = attachments.get(0);
            String fileType = actReportDTO.getFilePath().substring(actReportDTO.getFilePath().length() - 4);
            if (fileType.equals("." + BpmCode.FILE_TYPE_PDF)) {
//                drawing.setDrawingDeliveryFileId(actReportDTO.getFileId());
//                drawing.setDrawingDeliveryFileName(actReportDTO.getReportNo());
//                drawing.setDrawingDeliveryFilePath(actReportDTO.getFilePath());
                drawingRepository.save(drawing);
            }
        }

        return execResult;
    }


    /**
     * 图纸分发。
     *
     * @param dwg 图纸信息
     */
    private void drawingIssue(Drawing dwg, Map<String, Object> variables) {
        if (dwg != null) {
            Optional<DrawingDetail> opDetail = drawingDetailRepository.findByDrawingIdAndRevAndStatus(dwg.getId(), dwg.getLatestRev(), EntityStatus.ACTIVE);
            if (opDetail.isPresent()) {
                DrawingDetail detail = opDetail.get();
                detail.setActuralDrawingIssueDate(dwg.getActuralDrawingIssueDate());
                detail.setAuditNo(dwg.getAuditNo());
                detail.setChangeNoticeNo(dwg.getChangeNoticeNo());
                detail.setDeliveryDate(dwg.getDeliveryDate());
                detail.setDesignChangeReviewForm(dwg.getDesignChangeReviewForm());
                detail.setDrawingId(dwg.getId());
                detail.setIssueCardNo(dwg.getIssueCardNo());
                detail.setLastModifiedAt();
                detail.setPaperAmount(dwg.getPaperAmount());
                detail.setPaperUse(dwg.getPaperUse());
                detail.setPrinting(dwg.getPrinting());

                if (variables != null && variables.containsKey("productionReceivingDate")) {
                    if(variables.get("productionReceivingDate")!=null && !variables.get("productionReceivingDate").equals("")) {
                        String productionReceivingDate = (String) variables.get("productionReceivingDate");
                        detail.setProductionReceivingDate(DateUtils.fromISOString(productionReceivingDate));
                    }
                }

                detail.setQuantity(dwg.getQuantity());
                detail.setReturnRecord(dwg.getReturnRecord());
                detail.setProjectId(dwg.getProjectId());
                detail.setOrgId(dwg.getOrgId());
                drawingDetailRepository.save(detail);
            }
        }
    }
}
