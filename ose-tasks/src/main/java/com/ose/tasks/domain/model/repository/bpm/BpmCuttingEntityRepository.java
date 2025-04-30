package com.ose.tasks.domain.model.repository.bpm;
import com.ose.tasks.entity.bpm.BpmCuttingEntity;
import com.ose.vo.EntityStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface BpmCuttingEntityRepository extends PagingAndSortingRepository<BpmCuttingEntity, Long>, BpmCuttingEntityRepositoryCustom {

    @Modifying
    @Transactional
    @Query("update BpmCuttingEntity t set t.cuttingId =:cuttingId where t.id in(:cuttingEntityIds)")
    void updateCuttingIdIn(@Param("cuttingId") Long cuttingId, @Param("cuttingEntityIds") List<Long> cuttingEntityIds);


    @Modifying
    @Transactional
    @Query("update BpmCuttingEntity t set t.cuttingId = null, t.cuttingflag = false where t.id = :cuttingEntityId")
    void updateCuttingEntityById(@Param("cuttingEntityId") Long cuttingEntityId);

    @Modifying
    @Transactional
    @Query("update BpmCuttingEntity t set t.cuttingId =null where t.cuttingId=:cuttingId and t.cuttingflag=:cuttingflag")
    void updateCuttingEntityByCuttingEntityId(@Param("cuttingId") Long cuttingId, @Param("cuttingflag") Boolean cuttingflag);

    @Modifying
    @Transactional
    @Query("update BpmCuttingEntity t set t.cuttingflag =:cuttingflag where t.cuttingId=:cuttingId ")
    void updateCuttingFlagByCuttingId(@Param("cuttingId") Long cuttingId, @Param("cuttingflag") Boolean cuttingflag);

    @Query("SELECT distinct m.materialCode FROM BpmCuttingEntity m WHERE m.orgId = :orgId and m.projectId = :projectId and m.status = 'ACTIVE' and m.cuttingId is null")
    List<String> getMaterialCodeList(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    @Query("SELECT distinct m.tagNumber FROM BpmCuttingEntity m WHERE m.orgId = :orgId and m.projectId = :projectId and m.status = 'ACTIVE' and m.cuttingId is null")
    List<String> getTagNumberList(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    @Query("SELECT distinct m.matIssueCode FROM BpmCuttingEntity m WHERE m.orgId = :orgId and m.projectId = :projectId and m.status = 'ACTIVE' and m.cuttingId is null")
    List<String> getMatIssueCodeList(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    @Query("SELECT distinct m.nps FROM BpmCuttingEntity m WHERE m.orgId = :orgId and m.projectId = :projectId and m.status = 'ACTIVE' and m.cuttingId is null")
    List<String> getNpsList(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    @Query("SELECT distinct m.matSurplusReceiptsNo FROM BpmCuttingEntity m WHERE m.orgId = :orgId and m.projectId = :projectId and m.status = 'ACTIVE' and m.cuttingId is null")
    List<String> getMatSurplusReceiptsNoList(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    List<BpmCuttingEntity> findByCuttingId(Long entityId);

    List<BpmCuttingEntity> findByProjectIdAndCuttingIdAndCuttingflag(Long projectId, Long bpmCuttingId, Boolean cuttingflag);

    BpmCuttingEntity findByPipePieceEntityIdAndCuttingId(Long entityId, Long bpmCuttingId);

    BpmCuttingEntity findByProjectIdAndPipePieceEntityIdAndCuttingIdIsNull(Long projectId, Long entityId);

    List<BpmCuttingEntity> findByProjectIdAndPipePieceEntityId(Long projectId, Long entityId);

    @Query("SELECT distinct m.cuttingId FROM BpmCuttingEntity m WHERE m.projectId = :projectId and m.status = 'ACTIVE' and m.pipePieceEntityNo like :entityNo")
    List<Long> findCuttingIdByEntityNoLike(@Param("projectId") Long projectId, @Param("entityNo") String entityNo);

    @Modifying
    @Transactional
    @Query("update BpmCuttingEntity t set t.status = :status WHERE t.projectId = :projectId and t.pipePieceEntityId = :entityId and t.status = 'ACTIVE'")
    void updateCuttingEntityStatus(@Param("status") EntityStatus status, @Param("projectId") Long projectId, @Param("entityId") Long entityId);

    List<BpmCuttingEntity> findByPipePieceEntityIdAndCuttingIdIsNotNull(Long pipePieceId);

    /**
     * 查找管道下料清单下部分实体信息。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param cuttingId 下料id
     * @param status    状态
     * @return
     */
    @Query(value = "SELECT c.id FROM BpmCuttingEntity c WHERE c.orgId = :orgId AND c.projectId = :projectId AND c.cuttingId = :cuttingId AND c.status = :status")
    Set<Long> findIdsByOrgIdAndProjectIdAndCuttingIdAndStatus(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("cuttingId") Long cuttingId,
        @Param("status") EntityStatus status);

    List<BpmCuttingEntity> findByOrgIdAndProjectIdAndCuttingIdInAndStatus(Long orgId, Long projectId, List<Long> cuttingIds, EntityStatus status);

    Set<BpmCuttingEntity> findByOrgIdAndProjectIdAndCuttingIdAndStatus(Long orgId, Long projectId, Long entityId, EntityStatus status);

    @Query(value = "SELECT c.pipePieceEntityId FROM BpmCuttingEntity c WHERE c.cuttingId IS NULL AND c.matIssueCode = 'dummy'")
    List<Long> findAllDummyIds();

    @Modifying
    @Transactional
    @Query(value = "UPDATE " +
        " bpm_cutting_entity bce " +
        " JOIN project_nodes pn ON pn.project_id = bce.project_id AND pn.entity_id = bce.pipe_piece_entity_id " +
        " SET pn.is_deletable = 0 " +
        " WHERE bce.project_id = :projectId AND bce.cutting_id = :cuttingId ", nativeQuery = true)
    void setDeletableStatus(@Param("projectId") Long projectId, @Param("cuttingId") Long cuttingId);
}
