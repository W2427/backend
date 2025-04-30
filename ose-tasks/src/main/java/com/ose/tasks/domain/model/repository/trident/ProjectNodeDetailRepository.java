package com.ose.tasks.domain.model.repository.trident;

import com.ose.tasks.entity.trident.ProjectNodeDetail;
import com.ose.tasks.entity.trident.ProjectNodeInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;
import java.util.List;

/**
 * BPM ITR CRUD 操作接口。
 */
public interface ProjectNodeDetailRepository extends ProjectNodeDetailRepositoryCustom, PagingAndSortingRepository<ProjectNodeDetail, Long> {


    @Query("SELECT pni FROM ProjectNodeInfo pni LEFT JOIN ProjectNodeDetail pnd ON pni.entityId = pnd.entityId" +
        " WHERE pnd.id IS NULL")
    List<ProjectNodeInfo> getAddedPnds();

    @Query("SELECT pni FROM ProjectNodeInfo pni JOIN ProjectNodeDetail pnd ON pni.entityId = pnd.entityId" +
        " WHERE pnd.closedStatus <> pni.closedStatus OR pnd.submitStatus <> pni.submitStatus " +
        " OR pnd.startStatus <> pni.startStatus OR pnd.signedStatus <> pni.signedStatus")
    List<ProjectNodeInfo> getUpdatedPnds();

    @Query("SELECT pnd FROM ProjectNodeDetail pnd LEFT JOIN ProjectNodeInfo pni ON pni.projectId = pnd.projectId AND pni.entityId = pnd.entityId" +
        " WHERE pni.entityId IS NULL")
    List<ProjectNodeDetail> getDeletedPnds();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM project_node_detail WHERE project_id = :projectId AND entity_id = :entityId",nativeQuery = true)
    void deletedByEntityId(@Param("projectId") Long projectId, @Param("entityId") Long entityId);

    @Query(value = "SELECT pnd FROM ProjectNodeDetail pnd WHERE pnd.entityId = :entityId")
    ProjectNodeDetail findByEntityId(@Param("entityId") Long entityId);



    @Query("SELECT pni FROM ProjectNodeInfo pni JOIN ProjectNodeDetail pnd ON pni.entityId = pnd.entityId" +
        " WHERE pnd.subSystemId <> pni.subSystemId OR pnd.packageId <> pni.packageId " +
        " OR pnd.subSystemEntityId <> pni.subSystemEntityId OR pnd.code <> pni.code OR pnd.fGroup <> pni.fGroup " +
        " OR pni.entityType <> pnd.entityType OR pni.entitySubType <> pnd.entitySubType")
    List<ProjectNodeInfo> getUpdatedSubSystemPnds();


    ProjectNodeDetail findByProjectIdAndEntityId(Long projectId, Long id);

    List<ProjectNodeDetail> findByProjectIdAndSubSystemEntityId(Long projectId, Long subSystemId);

    List<ProjectNodeDetail> findByProjectIdAndSubSystemEntityIdAndCode(Long projectId, Long subSystemId, String tagCode);

    @Query(value = "SELECT COUNT(0) AS total, SUM(CASE WHEN closed_status THEN 1 ELSE 0 END) AS closed FROM project_node_detail " +
        " WHERE project_id = :projectId AND sub_system_entity_id = :subSystemId ", nativeQuery = true)
    Tuple getClosedTuple(@Param("projectId") Long projectId, @Param("subSystemId") Long subSystemId);

}
