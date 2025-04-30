package com.ose.tasks.domain.model.service.delegate;

import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.vo.BpmTaskDefKey;
import com.ose.dto.ContextDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.vo.BpmTaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 确定 任务节点 是否自启动 的代理服务。
 */
@Component
public class NextTaskDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {



    private static final Set<String> AUTO_START_TASKS = new HashSet<>(
        Arrays.asList(
            BpmTaskDefKey.USERTASK_NDT_REPAIR_CONFIRM.getType(),
            BpmTaskDefKey.USERTASK_NDT_CHECK.getType(),
            BpmTaskDefKey.USERTASK_PMI_CHECK.getType()
        )
    );

    /**
     * 构造方法。
     */
    @Autowired
    public NextTaskDelegate(
        BpmActivityInstanceRepository bpmActInstRepository,
        StringRedisTemplate stringRedisTemplate,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        BpmRuTaskRepository ruTaskRepository
    ) {

        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
    }


    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {


        for (int i = 0; i < execResult.getNextTasks().size(); i++) {
            String taskTypeStr = execResult.getNextTasks().get(i).getRuTask().getTaskType();
            BpmTaskType taskType;
            try {
                taskType = BpmTaskType.valueOf(taskTypeStr);
            } catch (Exception e) {
                continue;
            }
            if (taskType.isAutoStart()) {
                execResult.getNextTasks().get(i).setAutoStart(true);
            }
        }
        return execResult;
    }


}
