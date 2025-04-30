package com.ose.tasks.domain.model.repository.bpm;
import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.bpm.DeliveryCriteriaDTO;
import com.ose.tasks.dto.bpm.DeliveryEntityCriteriaDTO;
import com.ose.tasks.dto.material.EntityQrCodeCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmDelivery;
import com.ose.tasks.entity.bpm.BpmDeliveryEntity;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务查询。
 */
public class BpmDeliveryEntityRepositoryImpl extends BaseRepository implements BpmDeliveryEntityRepositoryCustom {

    @Override
    public Page<BpmDeliveryEntity> getEntityList(Long orgId, Long projectId, DeliveryEntityCriteriaDTO criteriaDTO, Pageable pageable) {

        SQLQueryBuilder<BpmDeliveryEntity> builder = getSQLQueryBuilder(BpmDeliveryEntity.class)
            .is("projectId", projectId)
            .is("status", EntityStatus.ACTIVE)
            .isNull("deliveryId");

        if (criteriaDTO.getEntityNo() != null
            && !criteriaDTO.getEntityNo().equals("")) {
            builder.like("entityNo", criteriaDTO.getEntityNo());
        }

        if (criteriaDTO.getEntityCategoryId() != null
            && !criteriaDTO.getEntityCategoryId().equals(0L)) {
            builder.is("entityCategory.id", criteriaDTO.getEntityCategoryId());
        }

        if (criteriaDTO.getProcessId() != null
            && !criteriaDTO.getProcessId().equals(0L)) {
            builder.is("process.id", criteriaDTO.getProcessId());
        }

        if (criteriaDTO.getModuleProjectNodeId() != null
            && !criteriaDTO.getModuleProjectNodeId().equals(0L)) {
            builder.is("entityModuleProjectNodeId", criteriaDTO.getModuleProjectNodeId());
        }

        if (criteriaDTO.getDiscipline() != null) {
            builder.is("discipline", criteriaDTO.getDiscipline());
        }

        if (criteriaDTO.getTaskPackageName() != null
            && !criteriaDTO.getTaskPackageName().equals("")) {
            builder.like("taskPackageName", criteriaDTO.getTaskPackageName());
        }

        if (criteriaDTO.getPaintingCode() != null
            && !criteriaDTO.getPaintingCode().equals("")) {
            builder.is("paintingCode", criteriaDTO.getPaintingCode());
        }

        if (criteriaDTO.getPressureTestPackage() != null
            && !criteriaDTO.getPressureTestPackage().equals("")) {
            builder.is("pressureTestPackage", criteriaDTO.getPressureTestPackage());
        }

        return builder.paginate(pageable).exec().page();
    }

    @Override
    public Page<BpmDelivery> getDeliveryList(Long orgId, Long projectId, Pageable pageable, DeliveryCriteriaDTO criteriaDTO, List<Long> deliveryIds) {

        SQLQueryBuilder<BpmDelivery> builder = getSQLQueryBuilder(BpmDelivery.class)
            .is("orgId", orgId)
            .is("projectId", projectId);

        if (deliveryIds != null) {
            if (deliveryIds.isEmpty()) {
                deliveryIds.add(0L);
            }
            builder.in("id", deliveryIds);
        }
        if (criteriaDTO.getDiscipline() != null) {
            builder.is("discipline", criteriaDTO.getDiscipline());
        }

        if (criteriaDTO.getFinishState() != null) {

            if (criteriaDTO.getFinishState().equals("FINISHED")) {
                builder.is("finishState", ActInstFinishState.FINISHED);
            } else {
                builder.is("finishState", ActInstFinishState.NOT_FINISHED);
            }
        }

        if (criteriaDTO.getKeyword() != null
            && !"".equals(criteriaDTO.getKeyword())) {
            Map<String, Map<String, Object>> keywordCriteria = new IdentityHashMap<>();
            Map<String, Object> operator = new IdentityHashMap<>();
            operator.put("$like", criteriaDTO.getKeyword());
            keywordCriteria.put(new String("no"), operator);
            keywordCriteria.put(new String("name"), operator);
            builder.orObj(keywordCriteria);
        }

        return builder.paginate(pageable).exec().page();
    }

    @Override
    public List<BpmDeliveryEntity> getDeliveryEntities(Long orgId, Long projectId, Long deliveryId) {

        SQLQueryBuilder<BpmDeliveryEntity> builder = getSQLQueryBuilder(BpmDeliveryEntity.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("deliveryId", deliveryId);

        builder.sort(new Sort.Order(Sort.Direction.DESC, "executeNgFlag"));
        builder.sort(new Sort.Order(Sort.Direction.ASC, "lastModifiedAt"));

        return builder.limit(Integer.MAX_VALUE).exec().list();
    }

    @Override
    public Page<BpmDeliveryEntity> search(Long orgId, Long projectId, EntityQrCodeCriteriaDTO criteriaDTO) {


        SQLQueryBuilder<BpmDeliveryEntity> builder = getSQLQueryBuilder(BpmDeliveryEntity.class)
            .is("orgId", orgId)
            .is("projectId", projectId);


        if (criteriaDTO.getPrintFlg() != null && !criteriaDTO.getPrintFlg().equals(false)) {
            builder.is("printFlg", criteriaDTO.getPrintFlg());
        }
        if (criteriaDTO.getEntityType() != null) {
            builder.is("entityType", criteriaDTO.getEntityType());
        }

        builder.isNotNull("deliveryId");

        if (criteriaDTO.getKeyword() != null
            && !criteriaDTO.getKeyword().equals("")) {
            Map<String, Map<String, String>> keywordCriteria = new IdentityHashMap<>();
            Map<String, String> operator = new IdentityHashMap<>();
            operator.put("$like", criteriaDTO.getKeyword());
            keywordCriteria.put("entityNo", operator);
            keywordCriteria.put("deliveryNo", operator);
            builder.or(keywordCriteria);
        }

        builder.sort(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        return builder.paginate(criteriaDTO.toPageable()).exec().page();
    }

    @Override
    public Page<BpmDeliveryEntity> getDeliveryEntitiesPage(
        Long orgId,
        Long projectId,
        Long bpmDeliveryEntityId,
        PageDTO pageDTO) {


        SQLQueryBuilder<BpmDeliveryEntity> builder = getSQLQueryBuilder(BpmDeliveryEntity.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("deliveryId", bpmDeliveryEntityId);











        builder.sort(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        return builder.paginate(pageDTO.toPageable()).exec().page();
    }

}
