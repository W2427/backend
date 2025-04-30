package com.ose.tasks.domain.model.service.bpm;

import com.ose.exception.BusinessError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.bpm.BpmEntityTypeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntityTypeRelationRepository;
import com.ose.tasks.dto.bpm.EntityTypeRelationDTO;
import com.ose.tasks.entity.bpm.BpmEntityType;
import com.ose.tasks.entity.bpm.BpmEntityTypeRelation;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.RedisKey;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EntityTypeRelationService extends StringRedisService implements EntityTypeRelationInterface {

    private final BpmEntityTypeRelationRepository entityTypeRelationRepository;

    private final BpmEntityTypeRepository entityCategoryTypeRepository;



    /**
     * 构造方法
     */
    @Autowired
    public EntityTypeRelationService(StringRedisTemplate stringRedisTemplate,
                                     BpmEntityTypeRelationRepository entityTypeRelationRepository,
                                     BpmEntityTypeRepository entityCategoryTypeRepository) {
        super(stringRedisTemplate);
        this.entityTypeRelationRepository = entityTypeRelationRepository;
        this.entityCategoryTypeRepository = entityCategoryTypeRepository;
    }

    /**
     * 创建实体类型分类
     */
    @Override
    public BpmEntityTypeRelation create(Long orgId, Long projectId, Long entityTypeId, EntityTypeRelationDTO entityTypeRelationDTO, Long operatorId) {
        // TODO 实体大类型暂时放开增删改
        String wbsEntityType = entityTypeRelationDTO.getEntityType();
        String relatedEntityType = entityTypeRelationDTO.getRelatedWbsEntityType();
        BpmEntityTypeRelation bpmEntityTypeRelation = entityTypeRelationRepository.findByOrgIdAndProjectIdAndWbsEntityTypeAndRelatedWbsEntityTypeAndStatus(
            orgId,
            projectId,
            wbsEntityType,
            relatedEntityType,
            EntityStatus.ACTIVE
        );

        if(bpmEntityTypeRelation != null) {
            throw new BusinessError("RELATION IS EXIST");
        }
        bpmEntityTypeRelation = BeanUtils.copyProperties(entityTypeRelationDTO, new BpmEntityTypeRelation());
        bpmEntityTypeRelation.setProjectId(projectId);
        bpmEntityTypeRelation.setOrgId(orgId);
        bpmEntityTypeRelation.setCreatedAt();
        bpmEntityTypeRelation.setStatus(EntityStatus.ACTIVE);
        bpmEntityTypeRelation.setCreatedBy(operatorId);
        bpmEntityTypeRelation.setLastModifiedBy(operatorId);
        bpmEntityTypeRelation.setLastModifiedAt();
        return entityTypeRelationRepository.save(bpmEntityTypeRelation);
    }

    /**
     * 查询实体类型分类列表
     */
    @Override
    public List<BpmEntityTypeRelation> getList(Long projectId, Long orgId, Long entityTypeId) {
        BpmEntityType entityType = entityCategoryTypeRepository.findById(entityTypeId).orElse(null);
        if(entityType == null) {
            throw new BusinessError("There is no entityType " + entityTypeId);
        }
        String wbsEntityType = entityType.getNameEn();

        return entityTypeRelationRepository.findByOrgIdAndProjectIdAndWbsEntityTypeAndStatus(
            orgId,
            projectId,
            wbsEntityType,
            EntityStatus.ACTIVE
        );
    }

    /**
     * 删除指定的实体类型分类
     */
    @Override
    public boolean delete(Long id, Long projectId, Long orgId, Long entityTypeId) {
        BpmEntityTypeRelation entityTypeRelation = entityTypeRelationRepository.findById(id).orElse(null);
        if(entityTypeRelation == null) {
            throw new BusinessError("THERE IS NO EntityType Relation as per id " + id);
        }
        entityTypeRelation.setStatus(EntityStatus.DELETED);
        entityTypeRelationRepository.save(entityTypeRelation);
        return true;
    }



    /**
     * 修改实体类型分类信息
     */
    @Override
    public BpmEntityTypeRelation modify(Long id,
                                        EntityTypeRelationDTO entityTypeRelationDTO,
                                        Long projectId,
                                        Long orgId,
                                        Long entityTypeId) {

        BpmEntityTypeRelation entityTypeRelation = entityTypeRelationRepository.findById(id).orElse(null);
        if (entityTypeRelation == null) { return null; }

        entityTypeRelation = BeanUtils.copyProperties(entityTypeRelationDTO, entityTypeRelation);

        entityTypeRelation.setLastModifiedAt();
        return entityTypeRelationRepository.save(entityTypeRelation);

    }


    @Override
    public Map<String, String> getEntityTypeRelation(Long projectId, String entityType) {
        String entityTypeKey = String.format(RedisKey.ENTITY_TYPE_RELATION.getDisplayName(), projectId.toString(), entityType);
        Map<Object, Object> entityTypeRelationMap = hgetAll(entityTypeKey);
        Map<String, String> entityTypeRMap = new HashMap<>();

        if(MapUtils.isEmpty(entityTypeRelationMap)) {
            List<BpmEntityTypeRelation> entityTypeRelations =
                entityTypeRelationRepository.findByProjectIdAndWbsEntityTypeAndStatus(projectId, entityType, EntityStatus.ACTIVE);
            entityTypeRelations.forEach(entityTypeRelation ->{
                hset(entityTypeKey, entityTypeRelation.getRelatedWbsEntityType(), entityTypeRelation.getRelationDelegate());
                entityTypeRMap.put(entityTypeRelation.getRelatedWbsEntityType(), entityTypeRelation.getRelationDelegate());
            });
        } else {
            entityTypeRelationMap.forEach((k,v) -> {
                String value = (String)v;
                if("null".equalsIgnoreCase(value)) { value = null;}
                entityTypeRMap.put((String)k, value);
            });
        }
        return entityTypeRMap;
    }



    @Override
    public void setEntityTypeRelations(List<BpmEntityTypeRelation> bpmEntityTypeRelations) {
        if(CollectionUtils.isEmpty(bpmEntityTypeRelations)) return;
        Long projectId = bpmEntityTypeRelations.get(0).getProjectId();
        String entityType = bpmEntityTypeRelations.get(0).getRelatedWbsEntityType();
        String entityTypeRelationKey = String.format(RedisKey.ENTITY_TYPE_RELATION.getDisplayName(), projectId.toString(), entityType);

        bpmEntityTypeRelations.forEach(entityTypeRelation ->{
            hset(entityTypeRelationKey, entityTypeRelation.getRelatedWbsEntityType(), entityTypeRelation.getRelationDelegate());
        });
    }

}
