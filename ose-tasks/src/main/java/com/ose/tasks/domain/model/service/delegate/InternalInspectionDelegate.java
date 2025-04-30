package com.ose.tasks.domain.model.service.delegate;

import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.dto.ContextDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.vo.bpm.BpmCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 用户服务。
 */
@Component
public class InternalInspectionDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;


    /**
     * 构造方法。
     */
    @Autowired
    public InternalInspectionDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                      StringRedisTemplate stringRedisTemplate,
                                      BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                      BpmRuTaskRepository ruTaskRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.bpmActInstRepository = bpmActInstRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
    }


    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

        Map<String, Object> command = (Map<String, Object>) data.get("command");

        String taskDefKey = execResult.getRuTask().getTaskDefKey();

        if (!command.containsKey(BpmCode.EXCLUSIVE_GATEWAY_RESULT)) {
            return execResult;
        }

        BpmActivityInstanceState actInstState = execResult.getActInstState();

        return execResult;
    }


}
