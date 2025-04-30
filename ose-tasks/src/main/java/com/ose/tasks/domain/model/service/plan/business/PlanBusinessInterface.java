package com.ose.tasks.domain.model.service.plan.business;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.WBSEntityPatchDTO;
import com.ose.tasks.dto.WBSEntityPostDTO;
import com.ose.tasks.dto.WBSEntryDTO;
import com.ose.tasks.dto.wbs.WBSEntryPatchDTO;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.wbs.entity.WorkflowProcessVariable;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import net.sf.mpxj.RelationType;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 计划管理服务。
 */
public interface PlanBusinessInterface {

    // 数据实体 ID 格式
    Pattern ENTITY_ID_PATTERN = Pattern.compile("[_\\-0-9]{19,20}", Pattern.CASE_INSENSITIVE);

    /**
     * 取得所有工序阶段及其工序名称信息。
     *
     * @param projectId 项目 ID
     * @return 工序阶段及其工序名称的映射表
     */
    Map<String, Set<String>> getStageProcesses(Long projectId);

    /**
     * 向工作类型的 WBS 中添加实体资源。
     *
     * @param operator         操作者信息
     * @param projectId        项目 ID
     * @param workEntry        工作条目
     * @param wbsEntityPostDTO 实体添加数据
     * @param entityIndex      实体序号
     */
    WBSEntry addEntity(
        OperatorDTO operator,
        Long projectId,
        WBSEntry workEntry,
        WBSEntityPostDTO wbsEntityPostDTO,
        int entityIndex,
        Long processId
    );

    /**
     * 设置期间。
     *
     * @param operator          操作者信息
     * @param parentEntry       上级条目信息
     * @param entityEntry       WBS 实体条目
     * @param wbsEntityPatchDTO 更新信息
     */
    void updateEntity(
        OperatorDTO operator,
        WBSEntry parentEntry,
        WBSEntry entityEntry,
        WBSEntityPatchDTO wbsEntityPatchDTO
    );

    /**
     * 更新 WBS 条目。
     *
     * @param operator         操作者信息
     * @param parentEntry      上级条目信息
     * @param entry            条目信息
     * @param wbsEntryPatchDTO 条目更新数据传输对象
     */
    void updateEntry(
        OperatorDTO operator,
        WBSEntry parentEntry,
        WBSEntry entry,
        WBSEntryPatchDTO wbsEntryPatchDTO
    );

    /**
     * 更新上级条目已完成权重之和。
     *
     * @param entry 当前条目
     */
    void updateFinishedScoreOfParents(WBSEntry entry);

    /**
     * 保存任务关系。
     *
     * @param operator      操作这信息
     * @param projectId     项目 ID
     * @param predecessorId 前置任务 GUID
     * @param successorId   后置任务 GUID
     * @param optional      是否为可选前置任务
     */
    void saveWBSEntryRelations(
        OperatorDTO operator,
        Long projectId,
        String predecessorId,
        String successorId,
        RelationType relationType,
        Boolean optional
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
     * 取得实体信息。
     *
     * @param entityType 实体类型
     * @param entityId   实体 ID
     * @param discipline 专业
     * @return 实体信息
     */
    WBSEntityBase getEntity(Long projectId, String entityType, Long entityId, String discipline);

    /**
     * 取得实体信息，并将其转为工作流参数。
     *
     * @param entityType 实体类型
     * @param entityId   实体 ID
     * @param discipline 专业
     * @return 实体工作流参数对象
     */
    WorkflowProcessVariable getWorkflowEntityVariable(Long projectId, String entityType, Long entityId, String discipline);

    /**
     * 取得实体的工序信息并生成工序-工序 ID 映射表。
     *
     * @param projectId 项目 ID
     * @return 工序-工序 ID 映射表
     */
    Map<String, Long> getProcesses(Long projectId);

    /**
     * 计划条目排序。
     *
     * @param wbsEntries 计划条目列表
     * @return 排序后的计划条目列表
     */
    List<WBSEntryDTO> sortWBS(List<WBSEntryDTO> wbsEntries);

    boolean deleteEntity(String entityType, Long entityId, String discipline, Long operatorId);

    /**
     * 取得实体信息。
     *
     * @param entityType 实体类型
     * @param entityId   实体 ID
     * @param discipline 专业
     * @return 实体信息
     */
    boolean physicalDeleteEntity(String entityType, Long entityId, String discipline);
}
