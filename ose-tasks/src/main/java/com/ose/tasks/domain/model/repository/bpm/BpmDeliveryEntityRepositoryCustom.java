package com.ose.tasks.domain.model.repository.bpm;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.DeliveryCriteriaDTO;
import com.ose.tasks.dto.bpm.DeliveryEntityCriteriaDTO;
import com.ose.tasks.dto.material.EntityQrCodeCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmDelivery;
import com.ose.tasks.entity.bpm.BpmDeliveryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BpmDeliveryEntityRepositoryCustom {

    /**
     * 查询交接单实体
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param criteriaDTO
     * @param pageable
     * @return
     */
    Page<BpmDeliveryEntity> getEntityList(Long orgId, Long projectId, DeliveryEntityCriteriaDTO criteriaDTO, Pageable pageable);

    /**
     * 查询交接单列表
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param pageable
     * @param criteriaDTO
     * @param deliveryIds
     * @return
     */
    Page<BpmDelivery> getDeliveryList(Long orgId, Long projectId, Pageable pageable, DeliveryCriteriaDTO criteriaDTO, List<Long> deliveryIds);

    /**
     * 查询交接单实体
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    List<BpmDeliveryEntity> getDeliveryEntities(Long orgId, Long projectId, Long deliveryId);


    Page<BpmDeliveryEntity> search(Long orgId, Long projectId, EntityQrCodeCriteriaDTO criteriaDTO);

    Page<BpmDeliveryEntity> getDeliveryEntitiesPage(
        Long orgId,
        Long projectId,
        Long bpmDeliveryEntityId,
        PageDTO pageDTO);
}
