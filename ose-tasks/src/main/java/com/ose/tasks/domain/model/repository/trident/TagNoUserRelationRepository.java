package com.ose.tasks.domain.model.repository.trident;

import com.ose.tasks.entity.trident.TagNoUserRelation;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TagNoUserRelationRepository extends PagingAndSortingRepository<TagNoUserRelation, Long> {
    List<TagNoUserRelation> findByProjectIdAndEntityId(Long projectId, Long entityId);

    TagNoUserRelation findByProjectIdAndEntityIdAndUserId(Long projectId, Long entityId, Long userId);
}
