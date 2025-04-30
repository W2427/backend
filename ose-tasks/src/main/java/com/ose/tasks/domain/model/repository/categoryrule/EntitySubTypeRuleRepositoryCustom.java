package com.ose.tasks.domain.model.repository.categoryrule;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.subTyperule.EntitySubTypeRuleCriteriaDTO;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import org.springframework.data.domain.Page;

/**
 * 实体类型设置规则 CRUD 操作接口。
 */
public interface EntitySubTypeRuleRepositoryCustom {

    /**
     * 查询实体类型匹配规则。
     *
     * @param criteriaDTO 查询条件
     * @param pageDTO     分页参数
     * @return 用户查询结果分页数据
     */
    Page<EntitySubTypeRule> search(
        Long orgId,
        Long projectId,
        EntitySubTypeRuleCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    );


}
