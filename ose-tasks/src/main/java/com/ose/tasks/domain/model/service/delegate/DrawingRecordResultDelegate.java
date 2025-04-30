package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingRepository;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.drawing.Drawing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 最终结果 节点 代理
 */
@Component
public class DrawingRecordResultDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final DrawingRepository drawingRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final DrawingBaseInterface drawingBaseService;

    private final EntitySubTypeInterface entitySubTypeService;

    @Autowired
    public DrawingRecordResultDelegate(
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmRuTaskRepository ruTaskRepository,
        DrawingRepository drawingRepository,
        SubDrawingRepository subDrawingRepository,
        DrawingBaseInterface drawingBaseService,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        StringRedisTemplate stringRedisTemplate, EntitySubTypeInterface entitySubTypeService) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.drawingRepository = drawingRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.drawingBaseService = drawingBaseService;
        this.entitySubTypeService = entitySubTypeService;
    }


    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {
        Map<String, Object> variables = execResult.getVariables();
        OperatorDTO operatorDTO = (OperatorDTO) data.get("operator");
        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");

        Drawing drawing = drawingRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, execResult.getActInst().getEntityId());
        if (drawing != null) {
            if (execResult.getTodoTaskDTO() != null && execResult.getTodoTaskDTO().getRecordResult() != null
                && execResult.getTodoTaskDTO().getRecordResult().equals("OK")) {
                drawing.setReviewResult("OK");
                drawing.setLastModifiedBy(operatorDTO.getId());
                drawing.setLastModifiedAt(new Date());
                drawingRepository.save(drawing);
            } else {
                drawing.setReviewResult("NG");
                drawing.setLastModifiedBy(operatorDTO.getId());
                drawing.setLastModifiedAt(new Date());
                drawingRepository.save(drawing);
                if(execResult.getVariables() == null) {
                    execResult.setVariables(new HashMap<>());
                }
                execResult.getVariables().put("forceStart", true);

            }
        }


        return execResult;
    }
}
