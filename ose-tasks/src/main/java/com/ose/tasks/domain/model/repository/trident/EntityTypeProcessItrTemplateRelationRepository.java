package com.ose.tasks.domain.model.repository.trident;

import com.ose.tasks.entity.trident.EntityTypeProcessItrTemplateRelation;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * EntityTypeProcessItrTemplateRelation CRUD 操作接口。
 */
public interface EntityTypeProcessItrTemplateRelationRepository extends EntityTypeProcessItrTemplateRelationRepositoryCustom,
    PagingAndSortingRepository<EntityTypeProcessItrTemplateRelation, Long> {


    List<EntityTypeProcessItrTemplateRelation> findByProjectId(Long projectId);

    EntityTypeProcessItrTemplateRelation findByIdAndStatus(Long entityTypeProcessItrTemplateRelationId, EntityStatus status);


    EntityTypeProcessItrTemplateRelation findByProjectIdAndEntitySubTypeProcessRelationIdAndItrTemplateIdAndStatus(Long projectId, Long id, Long itrId, EntityStatus active);

    List<EntityTypeProcessItrTemplateRelation> findByProjectIdAndEntitySubTypeIdAndProcessIdAndStatus(Long projectId, Long entitySubTypeId, Long processId, EntityStatus status);

    EntityTypeProcessItrTemplateRelation findByProjectIdAndEntitySubTypeIdAndItrTemplateIdAndStatus(Long projectId, Long entitySubTypeId, Long itrTemplateId, EntityStatus status);

    List<EntityTypeProcessItrTemplateRelation> findByProjectIdAndEntitySubTypeIdAndStatusOrderBySeq(Long projectId, Long entitySubTypeId, EntityStatus active);

    EntityTypeProcessItrTemplateRelation findByProjectIdAndEntitySubTypeIdAndProcessIdAndItrTemplateIdAndStatus(
        Long projectId, Long entitySubTypeId, Long processId, Long itrTemplateId, EntityStatus active);

//    EntityTypeProcessItrTemplateRelation findByProjectIdAndEntitySubTypeIdAndProcessIdAndItrTemplateIdAndStatus
}

