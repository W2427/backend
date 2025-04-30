package com.ose.tasks.domain.model.service;

import com.ose.tasks.dto.RatedTimeCriterionCriteriaDTO;
import com.ose.tasks.dto.RatedTimeCriterionDTO;
import com.ose.tasks.entity.RatedTimeCriterion;
import org.springframework.data.domain.Page;

public interface RatedTimeCriterionInterface {

    /**
     * 创建定额工时查询条件。
     *
     * @param operatorId            操作人ID
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param processStageId        工序阶段ID
     * @param processId             工序ID
     * @param entitySubTypeId      实体类型ID
     * @param ratedTimeCriterionDTO 创建信息
     */
    void create(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long processStageId,
        Long processId,
        Long entitySubTypeId,
        RatedTimeCriterionDTO ratedTimeCriterionDTO);

    /**
     * 更新定额工时查询信息。
     *
     * @param operatorId            操作人ID
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param ratedTimeCriterionId  定额工时查询条件ID
     * @param ratedTimeCriterionDTO 更新信息
     */
    void update(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long ratedTimeCriterionId,
        RatedTimeCriterionDTO ratedTimeCriterionDTO
    );

    /**
     * 获取定额工时查询条件列表。
     *
     * @param orgId                         组织ID
     * @param projectId                     项目ID
     * @param ratedTimeCriterionCriteriaDTO 查询条件
     * @return 定额工时查询条件列表
     */
    Page<RatedTimeCriterion> search(
        Long orgId,
        Long projectId,
        RatedTimeCriterionCriteriaDTO ratedTimeCriterionCriteriaDTO);

    /**
     * 获取定额工时查询条件详情。
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param ratedTimeCriterionId 定额工时查询条件ID
     * @return 定额工时查询条件详情
     */
    RatedTimeCriterion get(Long orgId, Long projectId, Long ratedTimeCriterionId);

    /**
     * 删除定额工时信息。
     *
     * @param operatorId           操作人ID
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param ratedTimeCriterionId 定额工时查询条件ID
     */
    void delete(Long operatorId, Long orgId, Long projectId, Long ratedTimeCriterionId);
}
