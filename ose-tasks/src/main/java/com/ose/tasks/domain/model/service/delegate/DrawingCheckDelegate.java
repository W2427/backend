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

import jakarta.persistence.Tuple;
import java.math.BigDecimal;
import java.util.*;

/**
 * 检查 节点 代理。
 */
@Component
public class DrawingCheckDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final DrawingRepository drawingRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final DrawingBaseInterface drawingBaseService;

    private final EntitySubTypeInterface entitySubTypeService;
    /**
     * 构造方法。
     */
    @Autowired
    public DrawingCheckDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                DrawingRepository drawingRepository,
                                BpmRuTaskRepository ruTaskRepository,
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

    /**
     * 预处理。
     *
     * @param contextDTO
     * @param data
     * @param execResult
     * @return
     */
    @Override
    public ExecResultDTO preExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {



        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");


        if(execResult.getVariables() == null) {
            execResult = drawingBaseService.getDrawingAndLatestRev(execResult);
            if(!execResult.isExecResult()) return execResult;
        }


        Drawing dwg = (Drawing) execResult.getVariables().get("drawing");
        String latestRev = (String) execResult.getVariables().get("latestRev");
        if(dwg==null){
            execResult.setExecResult(false);
            execResult.setErrorDesc("图纸不存在");
            return execResult;
        }


        Tuple tuple = subDrawingRepository.
            findNodeCheckStatusCount(orgId, projectId, dwg.getId(), latestRev);
        if(tuple == null) {
            execResult.setExecResult(false);
            execResult.setErrorDesc("业务错误");
            return execResult;
        }

        BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, dwg.getEntitySubType());

        if(best!=null && best.isSubDrawingFlg()){

            Long totalCount = ((BigDecimal) tuple.get("totalCount")).longValue();

            Long nodeCheckCount = ((BigDecimal) tuple.get("checkCount")).longValue();

            if(totalCount == 0L) {
                execResult.setExecResult(false);
                execResult.setErrorDesc("不存在校对图纸");
                return execResult;
            } else if(nodeCheckCount > 0L) {

                if(execResult.getTodoTaskDTO().getCommand() == null) {
                    execResult.setExecResult(false);
                    execResult.setErrorDesc("Not Found Gateway");
                    return execResult;
                }

                Map<String, Object> command = execResult.getTodoTaskDTO().getCommand();
                if(command.values().contains(BpmCode.EXCLUSIVE_GATEWAY_RESULT_ACCEPT) ||
                    command.values().contains(BpmCode.EXCLUSIVE_GATEWAY_RESULT_ACCEPT_SIGN)) {
                    execResult.setExecResult(false);
                    execResult.setErrorDesc("Some sub drawing not DONE");
                    return execResult;
                }

            }
        }

        return execResult;
    }

    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

        Map<String, Object> variables = execResult.getVariables();

        if(execResult.getVariables() == null) {
            execResult = drawingBaseService.getDrawingAndLatestRev(execResult);
            if(!execResult.isExecResult()) return execResult;
        }


        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");
        Drawing dwg = (Drawing) variables.get("drawing");
        String latestRev = (String) variables.get("latestRev");

        BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, dwg.getEntitySubType());


        if (best != null && best.isSubDrawingFlg()) {
            if (execResult.getTodoTaskDTO() != null && execResult.getTodoTaskDTO().getCommand() != null
                && BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT.equals(execResult.getTodoTaskDTO().getCommand().get(BpmCode.EXCLUSIVE_GATEWAY_RESULT))) {

                dwg.setLocked(false);
                drawingRepository.save(dwg);
            } else {

                Set<DrawingReviewStatus> inStatus = new HashSet<>();
                inStatus.add(DrawingReviewStatus.INIT);
                inStatus.add(DrawingReviewStatus.CHECK);
                inStatus.add(DrawingReviewStatus.CHECK_DONE);
                inStatus.add(DrawingReviewStatus.MODIFY);
                subDrawingRepository.updateReviewStatusByDrawingIdAndDrawingVersion(orgId, projectId, DrawingReviewStatus.REVIEW, dwg.getId(), latestRev, inStatus);
            }
        }
        return execResult;
    }

}
