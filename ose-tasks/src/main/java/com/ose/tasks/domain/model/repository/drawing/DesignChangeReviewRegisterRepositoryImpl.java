package com.ose.tasks.domain.model.repository.drawing;


import java.util.IdentityHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.drawing.DesignChangeCriteriaDTO;
import com.ose.tasks.entity.DesignChangeReviewRegister;
import com.ose.vo.EntityStatus;

/**
 * 用户查询。
 */
public class DesignChangeReviewRegisterRepositoryImpl extends BaseRepository implements DesignChangeReviewRegisterRepositoryCustom {

    @Override
    public Page<DesignChangeReviewRegister> searchDesignChangeReviewRegisterList(Long orgId, Long projectId,
                                                                                 Pageable pageable, DesignChangeCriteriaDTO criteriaDTO) {
        SQLQueryBuilder<DesignChangeReviewRegister> builder = getSQLQueryBuilder(DesignChangeReviewRegister.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("status", EntityStatus.ACTIVE);

        if (criteriaDTO.getKeyword() != null
            && !criteriaDTO.getKeyword().equals("")) {
            Map<String, Map<String, String>> keywordCriteria = new IdentityHashMap<>();
            Map<String, String> operator = new IdentityHashMap<>();
            operator.put("$like", criteriaDTO.getKeyword());
            keywordCriteria.put("transferNo", operator);
            keywordCriteria.put("title", operator);
            keywordCriteria.put("vorNo", operator);
            keywordCriteria.put("originatedBy", operator);
            builder.or(keywordCriteria);
        }

        return builder.paginate(pageable)
            .exec()
            .page();

    }

}
