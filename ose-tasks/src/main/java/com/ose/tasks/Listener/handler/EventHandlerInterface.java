package com.ose.tasks.Listener.handler;

import com.ose.dto.EventModel;


public interface EventHandlerInterface {

    void doHandler(EventModel model);

    String getSupportEventType();
}
