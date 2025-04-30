package com.ose.tasks.domain.model.service.bpm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.bpm.BpmEntitySubTypeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntityTypeRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.bpm.EntityTypeDTO;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmEntityType;
import com.ose.tasks.util.SpringUtils;
import com.ose.tasks.vo.bpm.CategoryRuleType;
import com.ose.util.BeanUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EntityTypeService extends StringRedisService implements EntityTypeInterface {

    private final BpmEntityTypeRepository entityTypeRepository;

    private final BpmEntitySubTypeRepository entitySubTypeRepository;


    /**
     * 构造方法
     */
    @Autowired
    public EntityTypeService(BpmEntityTypeRepository entityTypeRepository,
                             StringRedisTemplate stringRedisTemplate,
                             BpmEntitySubTypeRepository entitySubTypeRepository) {
        super(stringRedisTemplate);

        this.entityTypeRepository = entityTypeRepository;
        this.entitySubTypeRepository = entitySubTypeRepository;
    }

    /**
     * 创建实体类型分类
     */
    @Override
    public BpmEntityType create(Long orgId, Long projectId, String type, EntityTypeDTO typeDTO) {
        BpmEntityType bpmEntityType = BeanUtils.copyProperties(typeDTO, new BpmEntityType());
        bpmEntityType.setType(type);
        bpmEntityType.setProjectId(projectId);
        bpmEntityType.setOrgId(orgId);
        bpmEntityType.setCreatedAt();
        bpmEntityType.setStatus(EntityStatus.ACTIVE);
        if(bpmEntityType.getCategoryRuleType() == null){
            bpmEntityType.setCategoryRuleType(CategoryRuleType.INCLUDE_NON);
        }

        String entityTypeKey = String.format(RedisKey.ENTITY_TYPE.getDisplayName(), projectId, bpmEntityType.getNameEn());
        String entityTypeStr = getRedisKey(entityTypeKey);
        if(StringUtils.isEmpty(entityTypeStr)) {
            entityTypeStr = StringUtils.toJSON(bpmEntityType);
            setRedisKey(entityTypeKey, entityTypeStr);
        } else {
            deleteRedisKey(entityTypeStr);
            entityTypeStr = StringUtils.toJSON(bpmEntityType);
            setRedisKey(entityTypeKey, entityTypeStr);
        }

        return entityTypeRepository.save(bpmEntityType);
    }

    /**
     * 查询实体类型分类列表
     */
    @Override
    public Page<BpmEntityType> getList(PageDTO page, Long projectId, Long orgId, String type) {
        return entityTypeRepository.findByStatusAndProjectIdAndOrgIdAndType(EntityStatus.ACTIVE,
            projectId,
            orgId,
            type,
            page.toPageable());
    }

    /**
     * 查询可配置实体类型分类
     */
    @Override
    public List<BpmEntityType> getFixedLevelList(Long orgId, Long projectId, String type, EntityTypeDTO entityTypeDTO) {
        if (entityTypeDTO.getFixedLevel().equals("true")) {
            return entityTypeRepository.findByProjectIdAndTypeAndFixedLevelAndStatus(
                projectId,
                type,
                EntityStatus.ACTIVE);
        } else {
            return entityTypeRepository.findByProjectIdAndTypeAndStatus(
                projectId,
                type,
                EntityStatus.ACTIVE);
        }
    }

    /**
     * 删除指定的实体类型分类
     */
    @Override
    public boolean delete(Long id, Long projectId, Long orgId, String type) {




        Optional<BpmEntityType> optionalType = entityTypeRepository.findById(id);
        if (optionalType.isPresent()) {
            BpmEntityType bpmEntityType = optionalType.get();
            bpmEntityType.setStatus(EntityStatus.DELETED);
            bpmEntityType.setLastModifiedAt();
            entityTypeRepository.save(bpmEntityType);
            return true;
        }
        return false;
    }

    /**
     * 查询实体类型详细信息
     */
    @Override
    public BpmEntityType detail(Long id, Long projectId, Long orgId, String type) {
        Optional<BpmEntityType> optionalType = entityTypeRepository.findById(id);
        if (optionalType.isPresent()) {
            BpmEntityType bpmEntityType = optionalType.get();
            return bpmEntityType;
        }
        return null;
    }

    /**
     * 获取实体类型分类详细信息
     *
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @param type      READONLY/BUSINESS
     * @param name      实体大类型/实体业务类型的名称
     * @return 实体类型分类对象
     */
    @Override
    public BpmEntityType getCategoryTypeByName(Long projectId,
                                               Long orgId,
                                               String type,
                                               String name) {
        return entityTypeRepository.findByNameEnAndProjectIdAndOrgIdAndTypeAndStatus(name,
            projectId,
            orgId,
            type,
            EntityStatus.ACTIVE)
            .orElse(null);
    }

    /**
     * 修改实体类型分类信息
     */
    @Override
    public BpmEntityType modify(Long id,
                                EntityTypeDTO typeDTO,
                                Long projectId,
                                Long orgId,
                                String type) {






        Optional<BpmEntityType> result = entityTypeRepository.findById(id);
        if (result.isPresent()) {
            BpmEntityType bpmEntityType = BeanUtils.copyProperties(typeDTO, result.get());
            bpmEntityType.setLastModifiedAt();
            return entityTypeRepository.save(bpmEntityType);
        }
        return null;
    }

    /**
     * 根据实体类型分类id查询实体类型列表
     */
    @Override
    public List<BpmEntitySubType> getEntityCategoriesByTypeId(Long projectId, Long id) {
        return entitySubTypeRepository.findEntityCategoriesByTypeId(projectId, id);
    }

    @Override
    public BpmEntityType getBpmEntityType(Long projectId, String entityType) {
        String entityTypeKey = String.format(RedisKey.ENTITY_TYPE.getDisplayName(), projectId.toString(), entityType);
        String entityTypeStr = getRedisKey(entityTypeKey);

        if(StringUtils.isEmpty(entityTypeStr)) {
            BpmEntityType bpmEntityType = null;
            bpmEntityType = entityTypeRepository.findByProjectIdAndNameEnAndStatus(projectId, entityType, EntityStatus.ACTIVE);
            if(bpmEntityType != null){
                entityTypeStr = StringUtils.toJSON(bpmEntityType);
                setRedisKey(entityTypeKey, entityTypeStr);
            }
            return bpmEntityType;

        } else {
            return StringUtils.decode(entityTypeStr, new TypeReference<BpmEntityType>() {});
        }
    }

    @Override
    public WBSEntityBaseRepository getRepository(Long projectId, String entityType) {
        BpmEntityType bet = getBpmEntityType(projectId, entityType);
        if(bet == null || bet.getRepositoryClazz()==null) throw new BusinessError("There is no entity type: " + entityType);
        String repositoryClazz = bet.getRepositoryClazz();

        Class clazz = null;
        try {
            clazz = Class.forName(repositoryClazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        WBSEntityBaseRepository entityRepository = (WBSEntityBaseRepository) SpringUtils.getBean(clazz);
        return entityRepository;
    }

    @Override
    public BaseWBSEntityInterface getEntityInterface(Long projectId, String entityType) {
        BpmEntityType bet = getBpmEntityType(projectId, entityType);
        if(bet == null || bet.getEntityInterfaceClazz()==null) throw new BusinessError("There is no entity interface class: " + entityType);
        String entityInterfaceClazz = bet.getEntityInterfaceClazz();

        Class clazz = null;
        try {
            clazz = Class.forName(entityInterfaceClazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        BaseWBSEntityInterface entityInterface = (BaseWBSEntityInterface) SpringUtils.getBean(clazz);
        return entityInterface;
    }

    @Override
    public void setBpmEntityType(BpmEntityType bpmEntityType) {

    }

    @Override
    public List<BpmEntityType> getListForProcessReport(Long processId, Long projectId, Long orgId) {
        return null;
    }


}
