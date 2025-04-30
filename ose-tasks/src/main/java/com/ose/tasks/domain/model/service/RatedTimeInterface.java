package com.ose.tasks.domain.model.service;

import com.ose.tasks.dto.RatedTimeCreateDTO;
import com.ose.tasks.dto.RatedTimeCriteriaDTO;
import com.ose.tasks.dto.RatedTimeImportDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.RatedTime;
import com.ose.tasks.entity.RatedTimeImportRecord;
import org.springframework.data.domain.Page;

public interface RatedTimeInterface {

    /**
     * 创建定额工时。
     *
     * @param operatorId         操作人ID
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param processStageId     工序阶段ID
     * @param processId          工序ID
     * @param entitySubTypeId   实体类型ID
     * @param ratedTimeCreateDTO 定额工时信息
     */
    void create(Long operatorId,
                Long orgId,
                Long projectId,
                Long processStageId,
                Long processId,
                Long entitySubTypeId,
                RatedTimeCreateDTO ratedTimeCreateDTO);

    /**
     * 更新定额工时。
     *
     * @param operatorId  操作人ID
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param ratedTimeId 定额工时ID
     */
    void update(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long ratedTimeId,
        RatedTimeCreateDTO ratedTimeCreateDTO);

    /**
     * 获取定额工时列表。
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param ratedTimeCriteriaDTO 筛选条件
     * @return 定额工时列表
     */
    Page<RatedTime> search(
        Long orgId,
        Long projectId,
        RatedTimeCriteriaDTO ratedTimeCriteriaDTO);

    /**
     * 删除定额工时信息。
     *
     * @param operatorId  操作人ID
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param ratedTimeId 定额工时ID
     */
    void delete(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long ratedTimeId);

    /**
     * 获取定额工时详情。
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param ratedTimeId 定额工时ID
     * @return 定额工时详情
     */
    RatedTime get(Long orgId, Long projectId, Long ratedTimeId);

    /**
     * 导入定额工时。
     *
     * @param orgId      组织ID
     * @param operatorId 操作者ID
     * @param project    项目
     */
    RatedTimeImportRecord importRatedTimeFile(
        Long operatorId,
        Long orgId,
        Project project,
        RatedTimeImportDTO ratedTimeImportDTO
    );
}
