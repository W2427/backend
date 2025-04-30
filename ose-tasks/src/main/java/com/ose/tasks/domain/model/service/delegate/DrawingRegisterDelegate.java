package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 图纸登记节点代理。
 */
@Component
public class DrawingRegisterDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final DrawingRepository drawingRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public DrawingRegisterDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                   DrawingRepository drawingRepository,
                                   StringRedisTemplate stringRedisTemplate,
                                   BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                   BpmRuTaskRepository ruTaskRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
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

        Drawing drawing = drawingRepository.findById(execResult.getActInst().getEntityId()).orElse(null);

        if(drawing == null) {
            execResult.setExecResult(false);
            execResult.setErrorDesc("图纸未找到");
            return execResult;
        }


        drawingVariableService(drawing, variables);

        return execResult;
    }


    /**
     * 图纸登记记录存储。
     *
     * @param dwg       图纸
     * @param variables 记录信息
     */
    private void drawingVariableService(Drawing dwg, Map<String, Object> variables) {
        if (dwg != null && variables != null) {
            if (variables.containsKey("deliveryDate")) {
                if(variables.get("deliveryDate")!=null && !variables.get("deliveryDate").equals("")){
                    String deliveryDate = (String) variables.get("deliveryDate");
                    dwg.setDeliveryDate(DateUtils.fromISOString(deliveryDate));
                }
            }
            if (variables.containsKey("acturalDrawingIssueDate")) {
                if(variables.get("acturalDrawingIssueDate")!=null && !variables.get("acturalDrawingIssueDate").equals("")) {
                    String acturalDrawingIssueDate = (String) variables.get("acturalDrawingIssueDate");
                    dwg.setActuralDrawingIssueDate(DateUtils.fromISOString(acturalDrawingIssueDate));
                }
            }
            if (variables.containsKey("productionReceivingDate")) {
                if(variables.get("productionReceivingDate")!=null && !variables.get("productionReceivingDate").equals("")) {
                    String productionReceivingDate = (String) variables.get("productionReceivingDate");
                    dwg.setProductionReceivingDate(DateUtils.fromISOString(productionReceivingDate));
                }
            }
            if (variables.containsKey("issueCardNo")) {
                if(variables.get("issueCardNo")!=null && !variables.get("issueCardNo").equals("")) {
                    String issueCardNo = (String) variables.get("issueCardNo");
                    dwg.setIssueCardNo(issueCardNo);
                }
            }
            if (variables.containsKey("returnRecord")) {
                if(variables.get("returnRecord")!=null && !variables.get("returnRecord").equals("")) {
                    String returnRecord = (String) variables.get("returnRecord");
                    dwg.setReturnRecord(returnRecord);
                }
            }
            if (variables.containsKey("designChangeReviewForm")) {
                if(variables.get("designChangeReviewForm")!=null && !variables.get("designChangeReviewForm").equals("")) {
                    String designChangeReviewForm = (String) variables.get("designChangeReviewForm");
                    dwg.setDesignChangeReviewForm(designChangeReviewForm);
                }
            }
            if (variables.containsKey("changeNoticeNo")) {
                if(variables.get("changeNoticeNo")!=null && !variables.get("changeNoticeNo").equals("")) {
                    String changeNoticeNo = (String) variables.get("changeNoticeNo");
                dwg.setChangeNoticeNo(changeNoticeNo);
                }
            }
            if (variables.containsKey("quantity")) {
                if(variables.get("quantity")!=null && !variables.get("quantity").equals("")) {
                    Integer quantity =(Integer)variables.get("quantity");
                dwg.setQuantity(quantity);
                }
            }
            if (variables.containsKey("printing")) {
                if(variables.get("printing")!=null && !variables.get("printing").equals("")) {
                    Integer printing =(Integer) variables.get("printing");
                dwg.setPrinting(printing);
                }
            }
            if (variables.containsKey("paperUse")) {
                if(variables.get("paperUse")!=null && !variables.get("paperUse").equals("")) {
                    Integer paperUse =(Integer)  variables.get("paperUse");
                dwg.setPaperUse(paperUse.floatValue());
                }
            }
            if (variables.containsKey("paperAmount")) {
                if(variables.get("paperAmount")!=null && !variables.get("paperAmount").equals("")) {
                    Integer paperAmount =(Integer)variables.get("paperAmount");
                dwg.setPaperAmount(paperAmount.floatValue());
                }
            }
            if (variables.containsKey("auditNo")) {
                if(variables.get("auditNo")!=null && !variables.get("auditNo").equals("")) {
                    String auditNo = (String) variables.get("auditNo");
                    dwg.setAuditNo(auditNo);
                }
            }
            drawingRepository.save(dwg);



        }
    }


}
