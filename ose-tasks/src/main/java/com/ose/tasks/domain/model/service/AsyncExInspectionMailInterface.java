package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;

public interface AsyncExInspectionMailInterface {

    boolean callAsyncExInspectionMailSend(ContextDTO context, Long orgId, Long projectId);

}
