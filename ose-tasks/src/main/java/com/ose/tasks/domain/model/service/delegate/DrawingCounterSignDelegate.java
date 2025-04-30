package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.service.bpm.taskexec.DrawingExecInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 会签节点代理。
 */
@Component
public class DrawingCounterSignDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final DrawingExecInterface drawingExecService;

    private final DrawingBaseInterface drawingBaseService;


    /**
     * 构造方法。
     */
    @Autowired
    public DrawingCounterSignDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                      BpmRuTaskRepository ruTaskRepository,
                                      DrawingExecInterface drawingExecService,
                                      StringRedisTemplate stringRedisTemplate,
                                      BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                      DrawingBaseInterface drawingBaseService) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.drawingExecService = drawingExecService;
        this.drawingBaseService = drawingBaseService;
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



        execResult = drawingBaseService.checkRuTask(execResult);

        if (execResult.getVariables() != null) {
            execResult.getVariables().put("setComment", execResult.getTodoTaskDTO().getComment());
        }

        return execResult;
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
        Map<String, Object> variables = execResult.getVariables();


        if (variables == null || variables.get("drawing") == null) {
            execResult.setExecResult(false);
            return execResult;
        }


        boolean signFinish = drawingExecService.signAssign(
            execResult.getRuTask().getId(),
            execResult.getContext().getOperator().getAccessTokenId(),
            execResult.getTodoTaskDTO(),
            execResult.getActInst()
        );
        if (!signFinish) {
            execResult.setExecResult(false);
            return execResult;
        }


        return execResult;
    }


}
