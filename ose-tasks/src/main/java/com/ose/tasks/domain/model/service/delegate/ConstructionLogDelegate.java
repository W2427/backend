package com.ose.tasks.domain.model.service.delegate;

import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.service.AsyncPlanService;
import com.ose.tasks.domain.model.service.bpm.TodoIndividualTaskInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseService;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 建造 工序节点生成LOG 的代理服务。
 */
@Component
public class ConstructionLogDelegate extends ConstructionDelegate implements BaseBpmTaskInterfaceDelegate {

    /**
     * 构造方法。
     */
    @Autowired
    public ConstructionLogDelegate(
        TodoTaskBaseService todoTaskBaseService,
        TaskRuleCheckService taskRuleCheckService,
        BpmActivityInstanceRepository bpmActInstRepository,
        AsyncPlanService asyncPlanService,
        TodoIndividualTaskInterface todoIndividualTaskService,
        ApplicationEventPublisher applicationEventPublisher,
        StringRedisTemplate stringRedisTemplate,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        BpmRuTaskRepository ruTaskRepository) {
        super(
            todoTaskBaseService,
            taskRuleCheckService,
            bpmActInstRepository,
            asyncPlanService,
            todoIndividualTaskService,
            applicationEventPublisher,
            stringRedisTemplate,
            bpmActivityInstanceStateRepository,
            ruTaskRepository);
    }


    @Override
    public CreateResultDTO postCreateActInst(CreateResultDTO createResult) {
        super.postCreateActInst(createResult);
        return createResult;
    }


    @Override
    public ExecResultDTO completeExecute(ExecResultDTO execResult) {

        super.completeExecute(execResult);

        return execResult;
    }

}
