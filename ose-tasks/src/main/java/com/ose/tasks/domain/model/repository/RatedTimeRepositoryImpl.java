package com.ose.tasks.domain.model.repository;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.RatedTimeCriteriaDTO;
import com.ose.tasks.entity.RatedTime;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;

public class RatedTimeRepositoryImpl extends BaseRepository implements RatedTimeRepositoryCustom {

    /**
     * 获取定额工时列表。
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param ratedTimeCriteriaDTO 查询条件
     * @return 定额工时列表
     */
    @Override
    public Page<RatedTime> search(Long orgId, Long projectId, RatedTimeCriteriaDTO ratedTimeCriteriaDTO) {

        SQLQueryBuilder<RatedTime> sqlQueryBuilder = getSQLQueryBuilder(RatedTime.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("status", EntityStatus.ACTIVE);


        if (ratedTimeCriteriaDTO.getProcessStageId() != null) {
            sqlQueryBuilder.is("processStageId", ratedTimeCriteriaDTO.getProcessStageId());
        }

        if (ratedTimeCriteriaDTO.getProcessId() != null) {
            sqlQueryBuilder.is("processId", ratedTimeCriteriaDTO.getProcessId());
        }

        if (ratedTimeCriteriaDTO.getEntitySubTypeId() != null) {
            sqlQueryBuilder.is("entitySubTypeId", ratedTimeCriteriaDTO.getEntitySubTypeId());
        }

        if (ratedTimeCriteriaDTO.getMaterial() != null) {
            sqlQueryBuilder.is("material", ratedTimeCriteriaDTO.getMaterial());
        }
        if (ratedTimeCriteriaDTO.getCleaningMedium() != null) {
            sqlQueryBuilder.is("cleaningMedium", ratedTimeCriteriaDTO.getCleaningMedium());
        }

        if (ratedTimeCriteriaDTO.getMaxPipeThickness() != null) {
            sqlQueryBuilder.lte("minPipeThickness", Double.parseDouble(ratedTimeCriteriaDTO.getMaxPipeThickness()));
        }
        if (ratedTimeCriteriaDTO.getMinPipeThickness() != null) {
            sqlQueryBuilder.gte("maxPipeThickness", Double.parseDouble(ratedTimeCriteriaDTO.getMinPipeThickness()));
        }

        if (ratedTimeCriteriaDTO.getMaxTestPressure() != null) {
            sqlQueryBuilder.lte("minTestPressure", Double.parseDouble(ratedTimeCriteriaDTO.getMaxTestPressure()));
        }
        if (ratedTimeCriteriaDTO.getMinTestPressure() != null) {
            sqlQueryBuilder.gte("maxTestPressure", Double.parseDouble(ratedTimeCriteriaDTO.getMinTestPressure()));
        }
        if (ratedTimeCriteriaDTO.getMedium() != null) {
            sqlQueryBuilder.is("medium", ratedTimeCriteriaDTO.getMedium());
        }
        if (ratedTimeCriteriaDTO.getPipeDiameter() != null) {
            sqlQueryBuilder.is("pipeDiameter", ratedTimeCriteriaDTO.getPipeDiameter());
        }
        if (ratedTimeCriteriaDTO.getUnit() != null) {
            sqlQueryBuilder.is("unit", ratedTimeCriteriaDTO.getUnit());
        }

        if (ratedTimeCriteriaDTO.getHourNorm() != null) {
            sqlQueryBuilder.lte("hourNorm", Double.parseDouble(ratedTimeCriteriaDTO.getHourNorm()));
        }

        return sqlQueryBuilder
            .paginate(ratedTimeCriteriaDTO.toPageable())
            .exec()
            .page();
    }
}
