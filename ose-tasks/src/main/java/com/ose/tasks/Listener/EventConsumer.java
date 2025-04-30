package com.ose.tasks.Listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.dto.EventModel;
import com.ose.service.StringRedisService;
import com.ose.tasks.Listener.handler.EventHandlerInterface;
import com.ose.tasks.domain.model.repository.BatchTaskRepository;
import com.ose.tasks.domain.model.repository.ProjectRepository;
import com.ose.tasks.entity.Project;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.util.StringUtils;
import com.ose.vo.RedisKey;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class EventConsumer extends StringRedisService implements InitializingBean,ApplicationContextAware {

    private ApplicationContext context;

    private Map<String, EventHandlerInterface> config = new HashMap<>();

    private final ProjectRepository projectRepository;

    private final BatchTaskRepository batchTaskRepository;

    @Autowired
    public EventConsumer(StringRedisTemplate stringRedisTemplate, ProjectRepository projectRepository, BatchTaskRepository batchTaskRepository) {
        super(stringRedisTemplate);
        this.projectRepository = projectRepository;
        this.batchTaskRepository = batchTaskRepository;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.context = applicationContext;
    }

    public void afterPropertiesSet(){
        String key = RedisKey.BPM_ERROR.getDisplayName();

        List<Project> projects = projectRepository.findByFinishedAtIsNullAndDeletedIsFalse();
        projects.forEach(project -> {
            sadd(RedisKey.PROJECTS.getDisplayName(), String.format(RedisKey.PLAN_QUEUE_STATUS.getDisplayName(), project.getId().toString()));
            Boolean existPlan = batchTaskRepository.existsByProjectIdAndCodeAndRunning(project.getId(), BatchTaskCode.ENTITY_PROCESS_WBS_GENERATE, true);

            if(existPlan) {
                String statusKey = String.format(RedisKey.PLAN_QUEUE_STATUS.getDisplayName(), project.getId().toString());
                setRedisKey(statusKey, "HOT");
            } else {
                String statusKey = String.format(RedisKey.PLAN_QUEUE_STATUS.getDisplayName(), project.getId().toString());
                setRedisKey(statusKey, "COLD");
            }
        });



        Map<String, EventHandlerInterface> beans = context.getBeansOfType(EventHandlerInterface.class);


        if (!CollectionUtils.isEmpty(beans)) {
            for (Map.Entry<String, EventHandlerInterface> entry : beans.entrySet()) {


                String eventType = entry.getValue().getSupportEventType();



                if (!config.containsKey(eventType)) {
                    config.put(eventType, entry.getValue());
                }
            }
        }






        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                while (true) {
                    Set<String> planQueueStatusKeys = smembers(RedisKey.PROJECTS.getDisplayName());

                    for (String planQueueStatusKey : planQueueStatusKeys) {
                        String planQueueStatus = getRedisKey(planQueueStatusKey);
                        if ("COLD".equalsIgnoreCase(planQueueStatus)) {
                            String projectIdStr = planQueueStatusKey.replaceAll("PLAN_QUEUE_STATUS:", "");
                            String planMsg = rpop(String.format(RedisKey.PLAN_QUEUE.getDisplayName(), projectIdStr));
                            if (planMsg != null) {
                                EventModel planEventModel = StringUtils.decode(planMsg, new TypeReference<EventModel>() {
                                });
                                if (planEventModel == null || !config.containsKey(planEventModel.getType())) {
                                    System.out.println("不能识别的事件");
                                    continue;
                                }

                                EventHandlerInterface planHandler = config.get(planEventModel.getType());
                                try {
                                    planHandler.doHandler(planEventModel);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }


                    String msg = brpop(key,60, TimeUnit.SECONDS);
                    if(msg == null) {
                        continue;

                    }

                    EventModel eventModel = StringUtils.decode(msg, new TypeReference<EventModel>(){});
                    if (eventModel == null || !config.containsKey(eventModel.getType())) {
                        System.out.println("不能识别的事件");
                        continue;
                    }




                    EventHandlerInterface handler = config.get(eventModel.getType());
                    try {
                        handler.doHandler(eventModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        thread.start();

    }
}
