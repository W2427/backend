package com.ose.tasks.domain.model.repository.bpm;

import com.ose.tasks.entity.bpm.BpmDeliveryEntity;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.vo.DisciplineCode;
import com.ose.vo.EntityStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

@Transactional
public interface BpmDeliveryEntityRepository extends PagingAndSortingRepository<BpmDeliveryEntity, Long>, BpmDeliveryEntityRepositoryCustom {

    @Modifying
    @Transactional
    @Query("update BpmDeliveryEntity t set t.deliveryId =:deliveryId where t.id in(:deliveryEntityIds)")
    void updateDeliverIdIn(@Param("deliveryId") Long deliveryId, @Param("deliveryEntityIds") List<Long> deliveryEntityIds);

    @Modifying
    @Transactional
    @Query("update BpmDeliveryEntity t set t.deliveryNo =:deliveryNo where t.id in(:deliveryEntityIds)")
    void updateDeliverNoIn(@Param("deliveryNo") String deliveryNo, @Param("deliveryEntityIds") List<Long> deliveryEntityIds);

    BpmDeliveryEntity findByEntityIdAndDeliveryIdIsNull(Long entityId);

    @Query("SELECT distinct m.process FROM BpmDeliveryEntity m WHERE m.orgId = :orgId and m.projectId = :projectId and m.discipline = :discipline and m.status = 'ACTIVE' and m.deliveryId is null")
    List<BpmProcess> findProcessByOrgIdAndProjectId(@Param("orgId") Long orgId, @Param("projectId") Long projectId, @Param("discipline") DisciplineCode discipline);

    @Query("SELECT distinct m.entitySubType FROM BpmDeliveryEntity m WHERE m.orgId = :orgId and m.projectId = :projectId and m.status = 'ACTIVE' and m.deliveryId is null")
    List<BpmEntitySubType> findEntityCategoryByOrgIdAndProjectId(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    BpmDeliveryEntity findByProjectIdAndEntityIdAndProcessAndDeliveryIdIsNull(Long projectId, Long entityId, BpmProcess bpmProcess);

    BpmDeliveryEntity findByOrgIdAndProjectIdAndEntityIdAndStatus(Long orgId, Long projectId, Long entityId, EntityStatus status);

    List<BpmDeliveryEntity> findByOrgIdAndProjectIdAndEntityId(Long orgId, Long projectId, Long entityId);

    List<BpmDeliveryEntity> findByProjectIdAndEntityIdAndProcess(Long projectId, Long entityId, BpmProcess bpmProcess);

    List<BpmDeliveryEntity> findByDeliveryId(Long deliveryId);

    List<BpmDeliveryEntity> findByOrgIdAndProjectIdAndDeliveryIdIsNullAndTaskPackageIdIsNullAndStatus(Long orgId, Long projectId, EntityStatus entityStatus);

    BpmDeliveryEntity findByOrgIdAndProjectIdAndDeliveryIdAndQrCodeAndStatus(Long orgId, Long projectId, Long deliveryId, String qrCode, EntityStatus status);

    @Query("SELECT m FROM BpmDeliveryEntity m WHERE m.projectId = :projectId and m.process.id = :processId and m.discipline = :discipline and m.status = 'ACTIVE' and m.deliveryId is null")
    List<BpmDeliveryEntity> findModuleByProjectIdAndProcess(@Param("projectId") Long projectId, @Param("processId") Long processId, @Param("discipline") DisciplineCode discipline);

    @Query("SELECT m FROM BpmDeliveryEntity m WHERE m.projectId = :projectId and m.process.id = :processId and m.entityModuleProjectNodeId = :entityModuleProjectNodeId and m.discipline = :discipline and m.status = 'ACTIVE' and m.deliveryId is null")
    List<BpmDeliveryEntity> findModuleByProjectIdAndProcessAndModule(@Param("projectId") Long projectId, @Param("processId") Long processId, @Param("entityModuleProjectNodeId") Long entityModuleProjectNodeId, @Param("discipline") DisciplineCode discipline);

    List<BpmDeliveryEntity> findByProjectIdAndEntityIdAndProcessAndStatus(Long projectId, Long entityId,
                                                                          BpmProcess bpmProcess, EntityStatus status);

    @Query("SELECT distinct m.deliveryId FROM BpmDeliveryEntity m WHERE m.projectId = :projectId and m.status = 'ACTIVE' and m.process.id = :processId and m.entityNo like :entityNo")
    List<Long> findDeliveryIdByEntityNoLikeAndProcess(@Param("projectId") Long projectId, @Param("processId") Long processId, @Param("entityNo") String entityNo);

    @Query("SELECT distinct m.deliveryId FROM BpmDeliveryEntity m WHERE m.projectId = :projectId and m.status = 'ACTIVE' and m.entityNo like :entityNo")
    List<Long> findDeliveryIdByEntityNoLike(@Param("projectId") Long projectId, @Param("entityNo") String entityNo);

    @Query("SELECT distinct m.deliveryId FROM BpmDeliveryEntity m WHERE m.projectId = :projectId and m.status = 'ACTIVE' and m.process.id = :processId")
    List<Long> findDeliveryIdByProcess(@Param("projectId") Long projectId, @Param("processId") Long processId);

    @Modifying
    @Transactional
    @Query("update BpmDeliveryEntity t set t.status = :status WHERE t.projectId = :projectId and t.entityId = :entityId and t.status = 'ACTIVE'")
    void updateDeliveryEntityStatus(@Param("status") EntityStatus status, @Param("projectId") Long projectId, @Param("entityId") Long entityId);

    @Modifying
    @Transactional
    @Query("update BpmDeliveryEntity t set t.printFlag = false where t.id in(:deliveryEntityIds)")
    void updatePrintFlagIn(@Param("deliveryEntityIds") List<Long> deliveryEntityIds);

    List<BpmDeliveryEntity> findByEntityIdAndDeliveryIdIsNotNull(Long pipePieceId);

    @Modifying
    @Transactional
    @Query("update BpmDeliveryEntity t set t.executeNgFlag = false where t.id in(:deliveryEntityIds)")
    void updateExecuteNgFlagFalseIn(@Param("deliveryEntityIds") List<Long> deliveryEntityIds);


    @Modifying
    @Transactional
    @Query("update BpmDeliveryEntity t set t.executeNgFlag = false, t.deliveryId = null, t.deliveryNo = null where t.id = :deliveryEntityId")
    void updateExecuteNgFlagFalse(@Param("deliveryEntityId") Long deliveryEntityId);

    /**
     * 查找配送清单下部分实体信息。
     *
     * @param orgId      组织id
     * @param projectId  项目id
     * @param deliveryId 下料id
     * @param status     状态
     * @return
     */
    @Query(value = "SELECT c.entityId FROM BpmDeliveryEntity c WHERE c.orgId = :orgId AND c.projectId = :projectId AND c.deliveryId = :deliveryId AND c.status = :status")
    Set<Long> findIdsByOrgIdAndProjectIdAndDeliveryIdAndStatus(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("deliveryId") Long deliveryId,
        @Param("status") EntityStatus status);

    @Modifying
    @Transactional
    @Query("update BpmDeliveryEntity t set t.printFlag = true where t.entityId in(:deliveryEntityIds)")
    void updatePrintFlagInEntityIds(@Param("deliveryEntityIds") List<Long> deliveryEntityIds);

    List<BpmDeliveryEntity> findByOrgIdAndProjectIdAndDeliveryIdAndStatus(Long orgId, Long projectId, Long entityId, EntityStatus status);

    List<BpmDeliveryEntity> findByOrgIdAndProjectIdAndHeatNo(Long orgId, Long projectId, String heatNo);

}
