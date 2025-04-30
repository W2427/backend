package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 外检服务。
 */
@Component
public class ExInspDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {


    /**
     * 构造方法。
     */
    @Autowired
    public ExInspDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                          StringRedisTemplate stringRedisTemplate,
                          BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                          BpmRuTaskRepository ruTaskRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
    }


    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {
        Long projectId = (Long) data.get("projectId");

        Long[] taskIds = (Long[]) data.get("taskIds");

        Map<String, Object> command = (Map<String, Object>) data.get("command");

        setFpy(projectId, taskIds, command);

        return execResult;
    }

}
