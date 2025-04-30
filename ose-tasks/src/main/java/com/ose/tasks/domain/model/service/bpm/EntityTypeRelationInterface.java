package com.ose.tasks.domain.model.service.bpm;

import com.ose.tasks.dto.bpm.EntityTypeRelationDTO;
import com.ose.tasks.entity.bpm.BpmEntityTypeRelation;

import java.util.List;
import java.util.Map;

/**
 * service接口
 */
public interface EntityTypeRelationInterface {


    BpmEntityTypeRelation create(Long orgId, Long projectId, Long entityTypeId, EntityTypeRelationDTO entityTypeRelationDTO, Long operatorId);

    List<BpmEntityTypeRelation> getList(Long projectId, Long orgId, Long entityTypeId);

    boolean delete(Long id, Long projectId, Long orgId, Long entityTypeId);

    BpmEntityTypeRelation modify(Long id, EntityTypeRelationDTO entityTypeRelationDTO, Long projectId, Long orgId, Long entityTypeId);

    Map<String, String> getEntityTypeRelation(Long projectId, String entityType);

    void setEntityTypeRelations(List<BpmEntityTypeRelation> bpmEntityTypeRelations);
}
