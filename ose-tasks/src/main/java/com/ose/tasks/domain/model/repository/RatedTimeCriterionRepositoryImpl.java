package com.ose.tasks.domain.model.repository;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.RatedTimeCriterionCriteriaDTO;
import com.ose.tasks.entity.RatedTimeCriterion;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;

public class RatedTimeCriterionRepositoryImpl extends BaseRepository implements RatedTimeCriterionRepositoryCustom {

    /**
     * 获取定额任务查询条件列表。
     *
     * @param orgId                         组织ID
     * @param projectId                     项目ID
     * @param ratedTimeCriterionCriteriaDTO 查询条件
     * @return 定额任务查询列表
     */
    @Override
    public Page<RatedTimeCriterion> search(
        Long orgId,
        Long projectId,
        RatedTimeCriterionCriteriaDTO ratedTimeCriterionCriteriaDTO) {

        SQLQueryBuilder<RatedTimeCriterion> sqlQueryBuilder = getSQLQueryBuilder(RatedTimeCriterion.class);

        sqlQueryBuilder.is("orgId", orgId).is("projectId", projectId).is("status", EntityStatus.ACTIVE);

        if (ratedTimeCriterionCriteriaDTO.getProcessStageId() != null) {
            sqlQueryBuilder.is("processStageId", ratedTimeCriterionCriteriaDTO.getProcessStageId());
        }
        if (ratedTimeCriterionCriteriaDTO.getProcessId() != null) {
            sqlQueryBuilder.is("processId", ratedTimeCriterionCriteriaDTO.getProcessId());
        }
        if (ratedTimeCriterionCriteriaDTO.getEntitySubTypeId() != null) {
            sqlQueryBuilder.is("entitySubTypeId", ratedTimeCriterionCriteriaDTO.getEntitySubTypeId());
        }

        return sqlQueryBuilder
            .paginate(ratedTimeCriterionCriteriaDTO.toPageable())
            .exec()
            .page();
    }
}
