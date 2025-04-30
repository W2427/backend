package com.ose.tasks.domain.model.service.delegate;

import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseService;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 材料服务。
 */
@Component
public class MaterialDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final TodoTaskBaseService todoTaskBaseService;


    /**
     * 构造方法。
     */
    @Autowired
    public MaterialDelegate(
        TodoTaskBaseService todoTaskBaseService,
        BpmActivityInstanceRepository bpmActInstRepository,
        StringRedisTemplate stringRedisTemplate,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        BpmRuTaskRepository ruTaskRepository
    ) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.todoTaskBaseService = todoTaskBaseService;
    }


    /**
     * 创建工作流
     *
     * @param createResult 环境上下文
     * @return BpmActivityInstance 工作流实例
     */
    @Override
    public CreateResultDTO preCreateActInst(CreateResultDTO createResult) {

        createResult = todoTaskBaseService.getMaterialActInstInfo(createResult);
        return createResult;
    }


}
