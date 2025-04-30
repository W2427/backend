package com.ose.tasks.domain.model.repository.categoryrule;

import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.subTyperule.EntitySubTypeRuleCriteriaDTO;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import org.springframework.data.domain.Page;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * 实体类型设置规则 CRUD 操作接口。
 */
public class EntitySubTypeRuleRepositoryImpl extends BaseRepository implements EntitySubTypeRuleRepositoryCustom {

    /**
     * 查询实体类型设置规则。
     *
     * @param criteriaDTO 查询条件
     * @param pageDTO     分页参数
     * @return 用户查询结果分页数据
     */
    @Override
    public Page<EntitySubTypeRule> search(
        Long orgId,
        Long projectId,
        EntitySubTypeRuleCriteriaDTO criteriaDTO,
        PageDTO pageDTO) {
        SQLQueryBuilder<EntitySubTypeRule> sqlQueryBuilder = getSQLQueryBuilder(EntitySubTypeRule.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("entityType", criteriaDTO.getEntityType())
            .is("entityBusinessType", criteriaDTO.getEntityBusinessType())
            .is("deleted", false);

        if (criteriaDTO.getKeyword() != null) {
            Map<String, Map<String, String>> keywordCriteria = new IdentityHashMap<>();
            Map<String, String> operator = new IdentityHashMap<>();
            operator.put("$like", criteriaDTO.getKeyword());
            keywordCriteria.put("value1", operator);
            keywordCriteria.put("value2", operator);
            keywordCriteria.put("subType", operator);
            sqlQueryBuilder.or(keywordCriteria);
        }


        return sqlQueryBuilder
            .asc("ruleOrder")
            .paginate(pageDTO.toPageable())
            .exec()
            .page();
    }

}
