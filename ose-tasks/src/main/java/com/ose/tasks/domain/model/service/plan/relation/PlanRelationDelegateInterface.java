package com.ose.tasks.domain.model.service.plan.relation;

import java.util.List;

public interface PlanRelationDelegateInterface {

    List<Long> getRelatedEntityIds(Long orgId, Long projectId, Long entityId, String wbsEntityType, String relatedEntityType);


}
