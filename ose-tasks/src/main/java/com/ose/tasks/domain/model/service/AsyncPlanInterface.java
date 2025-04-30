package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.tasks.dto.wbs.WBSEntryPlainStartDTO;

public interface AsyncPlanInterface {

    boolean callAsyncPlanExecuteFinish(ContextDTO context, Long version, Long orgId, Long projectId, boolean isPartOut);

    boolean callAsyncPlanByProcessExecuteFinish(ContextDTO context, Long orgId, Long projectId, WBSEntryPlainStartDTO wBSEntryPlainStartDTO, boolean isPartOut);
}
