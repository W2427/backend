package com.ose.tasks.domain.model.service.plan;

import com.ose.dto.OperatorDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.WBSEntityPostDTO;
import com.ose.tasks.dto.wbs.WBSEntryPatchDTO;
import com.ose.tasks.entity.process.ProcessEntity;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * 计划管理服务接口。
 */
public interface PlanInterface extends EntityInterface {

    /**
     * 查询实体信息。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param stageName   工序阶段名称
     * @param processName 工序名称
     * @param entityType  实体类型
     * @param pageable    分页参数
     * @return 实体信息分页数据
     */
    Page<ProcessEntity> searchEntities(
        Long orgId,
        Long projectId,
        String stageName,
        String processName,
        String entityType,
        Pageable pageable
    );

    /**
     * 向工作类型的 WBS 中添加实体资源。
     *
     * @param operator         操作者信息
     * @param orgId            组织 ID
     * @param projectId        项目 ID
     * @param workEntryId      工作条目 ID
     * @param wbsEntityPostDTO 实体添加数据
     * @return WBS 条目
     */
    WBSEntry addEntity(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long workEntryId,
        WBSEntityPostDTO wbsEntityPostDTO
    );

    /**
     * 更新 WBS 条目设置。
     *
     * @param operator         操作者信息
     * @param orgId            组织 ID
     * @param projectId        项目 ID
     * @param entryId          条目 ID
     * @param wbsEntryPatchDTO 条目更新数据
     * @return WBS 条目
     */
    WBSEntry updateEntry(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long entryId,
        WBSEntryPatchDTO wbsEntryPatchDTO
    );

    /**
     * 删除实体条目。
     *
     * @param operator   操作者信息
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param wbsEntryId 条目 ID
     * @return WBS 更新版本号
     */
    Long deleteEntry(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long wbsEntryId
    );

    /**
     * 设置 WBS 条目的权重。
     *
     * @param operator   操作者信息
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     * @param score      权重
     */
    void setScore(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        Long wbsEntryId,
        Double score
    );

    /**
     * 根据关联实体的状态更新 WBS 的状态。
     *
     * @param projectId  项目 ID
     * @param entityType 实体类型
     * @param entityId   实体 ID
     * @param isDeletable   是否可删除
     */
    void updateStatusOfWBSOfDeletedEntity(Long projectId, String entityType, Long entityId, Long operatorId, Boolean isDeletable);

    /**
     * 设置计划手动进度
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param wbsEntryId
     * @param manualProgress
     */
    void updateWBSEntryManualProgress(Long orgId, Long projectId, Long wbsEntryId, String manualProgress);


    /**
     * 更新所有需要更新 的三级计划上的 实体
     */
    void setParentEntityIDs(Long projectId, Set<Long> workLoadWbsIds);
}
