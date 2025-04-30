package com.ose.tasks.domain.model.repository.plan;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.wbs.entry.WBSEntryPlainRelation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WBSEntryPlainRelationRepository extends PagingAndSortingWithCrudRepository<WBSEntryPlainRelation, Long> {


    WBSEntryPlainRelation findByWbsEntryIdAndWbsEntryAncestorId(Long wbsEntryId, Long wbsEntryAncestorId);

    List<WBSEntryPlainRelation> findByWbsEntryId(Long wbsEntryId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM wbs_entry_plain_relation WHERE wbs_entry_id = :wbsEntryId", nativeQuery = true)
    void deleteByWbsEntryId(@Param("wbsEntryId") Long wbsEntryId);

    @Query("SELECT n FROM WBSEntryPlainRelation n WHERE n.projectId = :projectId AND (n.wbsEntryId =:wbsEntryId OR n.wbsEntryAncestorId = :wbsEntryAncestorId) ")
    List<WBSEntryPlainRelation> findByProjectIdAndWbsEntryIdOrWbsEntryAncestorId(
        @Param("projectId") Long projectId,
        @Param("wbsEntryId") Long wbsEntryId,
        @Param("wbsEntryAncestorId") Long wbsEntryAncestorId);
}
