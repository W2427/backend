package com.ose.tasks.domain.model.repository.trident;

import com.ose.tasks.entity.trident.TagNoInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagNoInfoRepository extends PagingAndSortingRepository<TagNoInfo, Long> {
    List<TagNoInfo> findByProjectIdAndEntityId(Long projectId, Long entityId);

    List<TagNoInfo> findByProjectIdAndEntityIdAndType(Long projectId, Long entityId, String type);

    List<TagNoInfo> findByProjectIdAndEntityIdAndOperatorId(Long projectId, Long entityId, Long id);

    @Query("SELECT tni FROM ProjectNodeDetail pnd JOIN TagNoInfo tni ON tni.projectId = pnd.projectId AND pnd.entityId = tni.entityId " +
        "   WHERE pnd.projectId = :projectId AND pnd.subSystemEntityId = :subSystemId AND pnd.code = :code ORDER BY tni.id")
    List<TagNoInfo> findByProjectIdAndSubSystemIdAndCode(@Param("projectId") Long projectId,
                                                   @Param("subSystemId") Long subSystemId,
                                                   @Param("code") String disc);

    @Query("SELECT tni FROM TagNoInfo tni JOIN HierarchyNodeRelation hnr ON hnr.projectId = tni.projectId AND tni.entityId = hnr.entityId " +
        "   WHERE hnr.projectId = :projectId AND hnr.ancestorEntityId = :subSystemId AND tni.discCode = :code")
    List<TagNoInfo> findLastByProjectIdAndSubSystemIdAndCode(@Param("projectId") Long projectId,
                                                             @Param("subSystemId") Long subSystemId,
                                                             @Param("code") String discipline);
}
