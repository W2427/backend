package com.ose.tasks.domain.model.repository.drawing;


import java.util.IdentityHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.drawing.DesignChangeCriteriaDTO;
import com.ose.tasks.entity.ConstructionChangeRegister;
import com.ose.vo.EntityStatus;

/**
 * 用户查询。
 */
public class ConstructionChangeRegisterRepositoryImpl extends BaseRepository implements ConstructionChangeRegisterRepositoryCustom {

    @Override
    public Page<ConstructionChangeRegister> searchConstructionChangeRegisterList(Long orgId, Long projectId,
                                                                                 Pageable pageable, DesignChangeCriteriaDTO criteriaDTO) {
        SQLQueryBuilder<ConstructionChangeRegister> builder = getSQLQueryBuilder(ConstructionChangeRegister.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("status", EntityStatus.ACTIVE);

        if (criteriaDTO.getChangeType() != null) {
            builder.is("changeType", criteriaDTO.getChangeType());
        }

        if (criteriaDTO.getKeyword() != null
            && !criteriaDTO.getKeyword().equals("")) {
            Map<String, Map<String, String>> keywordCriteria = new IdentityHashMap<>();
            Map<String, String> operator = new IdentityHashMap<>();
            operator.put("$like", criteriaDTO.getKeyword());
            keywordCriteria.put("modelName", operator);
            keywordCriteria.put("originatedBy", operator);
            keywordCriteria.put("actions", operator);
            keywordCriteria.put("registerNo", operator);
            builder.or(keywordCriteria);
        }

        return builder.paginate(pageable)
            .exec()
            .page();
    }

}
