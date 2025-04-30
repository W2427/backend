package com.ose.tasks.Listener;

import com.ose.tasks.Event.ConstructionLogEvent;
import com.ose.tasks.domain.model.service.constructlog.BaseConstructLog;
import com.ose.tasks.domain.model.service.constructlog.ConstructLogInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ApplicationListener是无序的
 */
@Component
public class ConstructionLogListener implements ApplicationListener<ConstructionLogEvent> {

    private final BaseConstructLog baseConstructLog;

    @Autowired
    public ConstructionLogListener(BaseConstructLog baseConstructLog) {
        this.baseConstructLog = baseConstructLog;
    }


    @Override
    @Async
    public void onApplicationEvent(ConstructionLogEvent event) {

        List<ConstructLogInterface> constructLogClazzs = baseConstructLog.getConstructLogClazzs((String) event.getSource());

        for (ConstructLogInterface constructLogClazz : constructLogClazzs) {
            constructLogClazz.createConstructLog(event.getOperator(),
                event.getEntityType(),
                event.getEntityId(),
                event.getProcessNameEn(),
                event.getProcessId(),
                event.getProcessStage(),
                event.getProcessTestResultDTO());
        }

    }
}


