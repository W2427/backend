package com.ose.tasks.domain.model.service.bpm;

import java.util.List;

import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import org.springframework.data.domain.Page;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.EntityTypeDTO;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmEntityType;

/**
 * service接口
 */
public interface EntityTypeInterface {

    /**
     * 创建实体类型分类
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param typeDTO   传入实体类型分类对象
     * @return 实体类型分类对象
     */
    BpmEntityType create(Long orgId, Long projectId, String type, EntityTypeDTO typeDTO);

    /**
     * 查询实体类型分类列表
     *
     * @param page      查询条件
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return Page<BpmEntityType>
     */
    Page<BpmEntityType> getList(PageDTO page, Long projectId, Long orgId, String type);

    /**
     * 查询可配置实体类型分类
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return Page<BpmEntityType>
     */
    List<BpmEntityType> getFixedLevelList(Long orgId, Long projectId, String type, EntityTypeDTO entityTypeDTO);

    /**
     * 删除指定的实体类型分类
     *
     * @param id        实体类型分类ID
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return boolean
     */
    boolean delete(Long id, Long projectId, Long orgId, String type);

    /**
     * 获取实体类型分类详细信息
     *
     * @param id
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return
     */
    BpmEntityType detail(Long id, Long projectId, Long orgId, String type);

    /**
     * 通过名称获取实体类型分类详细信息
     *
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return BpmEntityType
     */
    BpmEntityType getCategoryTypeByName(Long projectId,
                                        Long orgId,
                                        String type,
                                        String name);

    /**
     * 修改实体类型分类信息
     *
     * @param id        实体类型分类id
     * @param typeDTO   实体类型分类对象
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return BpmEntityType
     */
    BpmEntityType modify(Long id,
                         EntityTypeDTO typeDTO,
                         Long projectId,
                         Long orgId,
                         String type);

    /**
     * 根据实体类型分类id查询实体类型列表
     *
     * @param id 实体类型分类id
     * @return List<BpmEntitySubType>
     */
    List<BpmEntitySubType> getEntityCategoriesByTypeId(Long projectId, Long id);



    BpmEntityType getBpmEntityType(Long projectId, String entityType);



    void setBpmEntityType(BpmEntityType bpmEntityType);

    List<BpmEntityType> getListForProcessReport(Long processId, Long projectId, Long orgId);

    WBSEntityBaseRepository getRepository(Long projectId, String entityType);

    BaseWBSEntityInterface getEntityInterface(Long projectId, String entityType);
}
