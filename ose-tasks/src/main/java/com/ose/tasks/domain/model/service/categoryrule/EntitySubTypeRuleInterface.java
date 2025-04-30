package com.ose.tasks.domain.model.service.categoryrule;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.subTyperule.EntitySubTypeRuleCriteriaDTO;
import com.ose.tasks.dto.subTyperule.EntitySubTypeRuleInsertDTO;
import com.ose.tasks.dto.subTyperule.EntitySubTypeRuleUpdateDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import org.springframework.data.domain.Page;

/**
 * WBS 实体类型设置规则 服务接口。
 *
 * @param 实体范型
 */
public interface EntitySubTypeRuleInterface {

    /**
     * 查询 实体类型设置规则。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件DTO
     * @param pageDTO     分页参数
     * @return 实体类型设置规则
     */
    Page<EntitySubTypeRule> search(
        Long orgId,
        Long projectId,
        EntitySubTypeRuleCriteriaDTO criteriaDTO,
        PageDTO pageDTO);

    /**
     * 取得 实体类型设置规则。
     *
     * @param orgId                组织 ID
     * @param projectId            项目 ID
     * @param entityCategoryRuleId 实体类型设置规则 ID
     * @return WBS 实体详细信息
     */
    EntitySubTypeRule get(Long orgId, Long projectId, Long entityCategoryRuleId);

    /**
     * 删除 实体类型设置规则。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param ruleId    实体类型设置规则 ID
     */
    void delete(OperatorDTO operator, Long orgId, Long projectId, Long ruleId);

    /**
     * 插入 实体类型设置规则。
     *
     * @param operator 操作者信息
     * @param project  项目
     * @param dto      实体类型设置规则
     */
    void insert(OperatorDTO operator, Project project, EntitySubTypeRuleInsertDTO dto);

    /**
     * 更新 实体类型设置规则。
     *
     * @param operator 操作者信息
     * @param project  项目
     * @param ruleId   要更新的实体类型规则ID
     * @param dto      实体类型设置规则
     */
    EntitySubTypeRule update(OperatorDTO operator, Project project, Long ruleId, EntitySubTypeRuleUpdateDTO dto);

    /**
     * 判断 实体类型设置规则是否存在。
     *
     * @param entityType 实体大分类
     * @param projectId    项目 ID
     * @return 存在:true; 不存在:false
     */
    boolean existsByCategoryAndCategoryType(
        String category,
        String entityType,
        Long projectId);

}
