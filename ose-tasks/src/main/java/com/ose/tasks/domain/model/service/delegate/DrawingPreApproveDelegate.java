package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingRepository;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.drawing.DrawingReviewStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 审核 节点 代理
 */
@Component
public class DrawingPreApproveDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final DrawingRepository drawingRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final DrawingBaseInterface drawingBaseService;

    private final EntitySubTypeInterface entitySubTypeService;

    @Autowired
    public DrawingPreApproveDelegate(
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmRuTaskRepository ruTaskRepository,
        DrawingRepository drawingRepository,
        SubDrawingRepository subDrawingRepository,
        StringRedisTemplate stringRedisTemplate,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        DrawingBaseInterface drawingBaseService, EntitySubTypeInterface entitySubTypeService) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.drawingRepository = drawingRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.drawingBaseService = drawingBaseService;
        this.entitySubTypeService = entitySubTypeService;
    }


    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {
        Long projectId = execResult.getProjectId();
        Long orgId = (Long) data.get("orgId");
        Map<String, Object> variables = execResult.getVariables();
        Long actInstId = execResult.getRuTask().getActInstId();

        if(variables == null ) {
            execResult = drawingBaseService.getDrawingAndLatestRev(execResult);
            if(!execResult.isExecResult()) return execResult;
        }


        if (execResult.getTodoTaskDTO() != null && execResult.getTodoTaskDTO().getCommand() != null
            && BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT.equals(execResult.getTodoTaskDTO().getCommand().get(BpmCode.EXCLUSIVE_GATEWAY_RESULT))) {
            Drawing dwg = (Drawing) execResult.getVariables().get("drawing");
            String latestRev = (String) execResult.getVariables().get("latestRev");


            dwg.setLocked(false);
            drawingRepository.save(dwg);
            BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, dwg.getEntitySubType());
            if (best != null && best.isSubDrawingFlg()) {
                subDrawingRepository.updateReviewStatusByDrawingIdAndActInstIdAndDrawingVersion(orgId, projectId, DrawingReviewStatus.MODIFY, dwg.getId(), actInstId,latestRev);
            }
        }

        return execResult;
    }
}
