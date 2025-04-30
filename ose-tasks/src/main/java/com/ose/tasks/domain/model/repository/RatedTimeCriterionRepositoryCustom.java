package com.ose.tasks.domain.model.repository;

import com.ose.tasks.dto.RatedTimeCriterionCriteriaDTO;
import com.ose.tasks.entity.RatedTimeCriterion;
import org.springframework.data.domain.Page;

public interface RatedTimeCriterionRepositoryCustom {

    /**
     * 获取定额任务查询条件列表。
     *
     * @param orgId                         组织ID
     * @param projectId                     项目ID
     * @param ratedTimeCriterionCriteriaDTO 查询条件
     * @return 定额任务查询列表
     */
    Page<RatedTimeCriterion> search(
        Long orgId,
        Long projectId,
        RatedTimeCriterionCriteriaDTO ratedTimeCriterionCriteriaDTO);
}
