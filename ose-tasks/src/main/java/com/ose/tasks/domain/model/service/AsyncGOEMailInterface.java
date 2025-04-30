package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;

public interface AsyncGOEMailInterface {

    boolean callAsyncGOEMailSend(ContextDTO context, Long orgId, Long projectId);

}
