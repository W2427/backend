package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.service.bpm.TaskNotificationInterface;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmRuTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 用户服务。
 */
@Component
public class InternalInspectionNotifactionDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final TaskNotificationInterface taskNotificationService;

    /**
     * 构造方法。
     */
    @Autowired
    public InternalInspectionNotifactionDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                                 StringRedisTemplate stringRedisTemplate,
                                                 BpmRuTaskRepository ruTaskRepository,
                                                 BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                                 TaskNotificationInterface taskNotificationService
    ) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.bpmActInstRepository = bpmActInstRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.taskNotificationService = taskNotificationService;
    }


    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {



        Long orgId = (Long) data.get("orgId");

        Long projectId = (Long) data.get("projectId");

        Long[] taskIds = (Long[]) data.get("taskIds");







        for (int i = 0; i < taskIds.length; i++) {
            BpmRuTask ruTask = ruTaskRepository.findById(taskIds[0]).orElse(null);
            if (ruTask != null) {
                Long actInstId = ruTask.getActInstId();
                Optional<BpmActivityInstanceBase> op = bpmActInstRepository.findById(actInstId);
                if (op.isPresent()) {
                    BpmActivityInstanceBase actInst = op.get();

                    taskNotificationService.sendInternalInspectionNotification(orgId, projectId, actInst);
                }
            }

        }

        return execResult;
    }


}
