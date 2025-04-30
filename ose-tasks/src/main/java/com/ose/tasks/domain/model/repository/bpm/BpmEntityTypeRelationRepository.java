package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmEntityTypeRelation;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 实体 CRUD 操作接口。
 */
@Transactional
public interface BpmEntityTypeRelationRepository extends PagingAndSortingWithCrudRepository<BpmEntityTypeRelation, Long> {

    BpmEntityTypeRelation findByOrgIdAndProjectIdAndWbsEntityTypeAndRelatedWbsEntityTypeAndStatus(Long orgId, Long projectId, String wbsEntityType,
                                                                                                  String relatedEntityType, EntityStatus status);

    List<BpmEntityTypeRelation> findByOrgIdAndProjectIdAndWbsEntityTypeAndStatus(Long orgId, Long projectId, String wbsEntityType, EntityStatus active);

    List<BpmEntityTypeRelation> findByProjectIdAndWbsEntityTypeAndStatus(Long projectId, String entityType, EntityStatus active);

}
