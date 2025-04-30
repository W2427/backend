package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.*;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 实体工序map CRUD 操作接口。
 */
@Transactional
public interface EntitySubTypeCoordinateRelationRepository extends PagingAndSortingWithCrudRepository<BpmEntityTypeCoordinateRelation, Long> {

    @Query("SELECT m.entitySubType FROM BpmEntityTypeCoordinateRelation m "
        + "WHERE m.drawingCoordinate.id = :id and m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<BpmEntitySubType> findEntitySubTypeByDrawingCoordinateIdAndProjectIdAndOrgId(@Param("id") Long id, @Param("projectId") Long projectId, @Param("orgId") Long orgId);

//    @Query(
//        value = "SELECT m FROM BpmEntityTypeProcessRelation m "
//            + "WHERE m.entitySubType.id = :id and m.projectId = :projectId and m.orgId = :orgId "
//            + "and m.status = 'ACTIVE' and m.process.processStage.id = :stageId order by m.process.orderNo",
//        countQuery = "select count(m) FROM BpmEntityTypeProcessRelation m "
//            + "WHERE m.entitySubType.id = :id and m.projectId = :projectId and m.orgId = :orgId "
//            + "and m.status = 'ACTIVE' and m.process.processStage.id = :stageId"
//    )
//    Page<BpmEntityTypeProcessRelation> findProcessByEntitySubTypeIdAndProjectIdAndOrgIdAndProcessStageId(@Param("id") Long id, @Param("projectId") Long projectId, @Param("orgId") Long orgId,
//                                                                                        @Param("stageId") Long stageId, Pageable criteriaDTO);

    Optional<BpmEntityTypeCoordinateRelation> findByDrawingCoordinateIdAndEntitySubTypeIdAndStatus(Long drawingCoordinateId, Long entitySubTypeId, EntityStatus active);

    List<BpmEntityTypeCoordinateRelation> findByEntitySubTypeIdAndStatus(Long id, EntityStatus active);
//
//    List<BpmEntityTypeProcessRelation> findByProcessIdAndStatus(Long id, EntityStatus status);

    @Query(
        value = "SELECT m.entitySubType FROM BpmEntityTypeCoordinateRelation m "
            + "WHERE m.drawingCoordinate.id = :id and m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'"
            + " and m.entitySubType.entityType.id = :typeId order by m.entitySubType.orderNo",
        countQuery = "select count(m) FROM BpmEntityTypeCoordinateRelation m "
            + "WHERE m.drawingCoordinate.id = :id and m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE' "
            + "and m.entitySubType.entityType.id = :typeId "
    )
    Page<BpmEntitySubType> findEntitySubTypeByDrawingCoordinateIdAndProjectIdAndOrgIdAndTypeId(@Param("id") Long id, @Param("projectId") Long projectId, @Param("orgId") Long orgId,
                                                                                      @Param("typeId") Long typeId, Pageable pageable);

    @Query(
        value = "SELECT m.entitySubType FROM BpmEntityTypeCoordinateRelation m "
            + "WHERE m.drawingCoordinate.id = :id and m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'"
            + " order by m.entitySubType.orderNo",
        countQuery = "select count(m) FROM BpmEntityTypeCoordinateRelation m "
            + "WHERE m.drawingCoordinate.id = :id and m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE' "
    )
    Page<BpmEntitySubType> findEntitySubTypeByDrawingCoordinateIdAndProjectIdAndOrgIdasPage(@Param("id") Long id, @Param("projectId") Long projectId, @Param("orgId") Long orgId,
                                                                                   Pageable pageable);

//    @Query("SELECT distinct m.process.processStage FROM BpmEntityTypeProcessRelation m "
//        + "WHERE m.entitySubType.id = :id and m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
//    List<BpmProcessStage> findProcessStageByEntitySubTypeIdAndProjectIdAndOrgId(@Param("id") Long id, @Param("projectId") Long projectId,
//                                                                                 @Param("orgId") Long orgId);
//
//    @Query("SELECT distinct m.entitySubType.entityType FROM BpmEntityTypeProcessRelation m "
//        + "WHERE m.process.id = :id and m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
//    List<BpmEntityType> findEntityTypesByProcessIdAndProjectIdAndOrgId(@Param("id") Long id, @Param("projectId") Long projectId, @Param("orgId") Long orgId);
//
//    @Query(
//        value = "SELECT m FROM BpmEntityTypeProcessRelation m "
//            + "WHERE m.entitySubType.id = :id and m.projectId = :projectId and m.orgId = :orgId "
//            + "and m.status = 'ACTIVE' order by m.process.orderNo",
//        countQuery = "select count(m) FROM BpmEntityTypeProcessRelation m "
//            + "WHERE m.entitySubType.id = :id and m.projectId = :projectId and m.orgId = :orgId "
//            + "and m.status = 'ACTIVE'"
//    )
//    Page<BpmEntityTypeProcessRelation> findProcessByEntitySubTypeIdAndProjectIdAndOrgId(@Param("id") Long id,
//                                                                       @Param("projectId") Long projectId,
//                                                                       @Param("orgId") Long orgId,
//                                                                       Pageable pageable);
//
//    @Transactional
//    @Modifying
//    @Query(value = "DELETE FROM bpm_entity_type_process_relation WHERE org_id = :orgId", nativeQuery = true)
//    void deleteByOrgId(@Param("orgId") Long orgId);
//
//
//    @Query(value = "select distinct m " +
//        "from BpmEntityTypeProcessRelation m where m.projectId = :projectId and m.status = 'ACTIVE' AND m.funcPart is not null")
//    Set<BpmEntityTypeProcessRelation> findProcessFuncParts(@Param("projectId") Long projectId);

}
