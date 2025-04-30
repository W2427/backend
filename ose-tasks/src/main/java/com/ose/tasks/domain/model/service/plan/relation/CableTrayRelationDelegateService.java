package com.ose.tasks.domain.model.service.plan.relation;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CableTrayRelationDelegateService implements PlanRelationDelegateInterface {

    @Override
    public List<Long> getRelatedEntityIds(Long orgId, Long projectId, Long entityId, String wbsEntityType, String relatedEntityType) {
        return null;
    }
}
