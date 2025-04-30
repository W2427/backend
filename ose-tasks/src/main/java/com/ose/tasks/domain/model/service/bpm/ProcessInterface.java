package com.ose.tasks.domain.model.service.bpm;

import java.util.List;

import com.ose.service.EntityInterface;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.ProcessBpmnRelation;
import com.ose.tasks.entity.bpm.*;
import org.springframework.data.domain.Page;

import com.ose.tasks.vo.RelationReturnEnum;

/**
 * 工序管理service接口
 */
public interface ProcessInterface extends EntityInterface {

    /**
     * 删除工序
     *
     * @param id        工序id
     * @param projectId 项目id
     * @param orgId     组织id
     * @return 操作是否成功
     */
    boolean delete(Long id, Long projectId, Long orgId);

    /**
     * 查询工序
     *
     * @param projectId 项目id
     * @param orgId     组织id
     * @param page      分页信息
     */
    Page<BpmProcess> getList(ProcessCriteriaDTO page, Long projectId, Long orgId);

    /**
     * 创建工序
     *
     * @param processDTO 工序信息
     * @param projectId  项目id
     * @param orgId      组织id
     */
    BpmProcess create(ProcessDTO processDTO, Long projectId, Long orgId);

    /**
     * 编辑工序
     *
     * @param id             工序id
     * @param projectId      项目id
     * @param orgId          组织id
     * @return 编辑后的工序
     */
    BpmProcess modify(Long id, ProcessDTO processDTO, Long projectId, Long orgId);

    /**
     * 添加实体类型
     *
     * @param stepId    工序id
     * @param entityId  实体id
     * @param projectId 项目id
     * @param orgId     组织id
     * @return 编辑后的工序
     */
    RelationReturnEnum addEntitySubType(Long stepId, Long entityId, Long projectId, Long orgId);

    /**
     * 删除实体类型
     *
     * @param stepId    工序id
     * @param entityId  实体id
     * @param projectId 项目ID
     * @param orgId     组织id
     * @return 编辑后的工序
     */
    RelationReturnEnum deleteEntitySubType(Long stepId, Long entityId, Long projectId, Long orgId);

    /**
     * 查询工序详细信息
     *
     * @param id        工序id
     * @param orgId     组织id
     * @param projectId 项目id
     */
    BpmProcess get(Long id, Long projectId, Long orgId);

    /**
     * 取得工序详细信息。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param stageName   工序阶段名称
     * @param processName 工序名称
     * @return 工序详细信息
     */
    BpmProcess get(Long orgId, Long projectId, String stageName, String processName);

    /**
     * 获取全部实体类型
     *
     * @param orgId     组织id
     * @param projectId 项目id
     */
    List<BpmEntitySubType> getEntitySubTypeList(Long projectId, Long orgId);

    /**
     * 批量排序
     *
     * @param projectId 项目ID
     */
    boolean sort(List<SortDTO> sortDTOs, Long projectId, Long orgId);

    /**
     * 取得工序对应的实体类型列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    List<BpmEntitySubType> getEntitySubTypeByProcessId(Long id, Long projectId, Long orgId);

    /**
     * 批量添加实体类型
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @param dto
     * @return
     */
    boolean addEntityBatch(Long orgId, Long projectId, Long id, BatchAddRelationDTO dto);

    /**
     * 查询工序的实体对应关系
     *
     * @param id
     * @return
     */
    List<BpmEntityTypeProcessRelation> getRelationByEntitySubTypeId(Long id);

    /**
     * 获取工序对应的实体类型列表
     *
     * @param id
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return
     */
    Page<BpmEntitySubType> getEntitySubTypeList(Long id, Long projectId, Long orgId, ProcessEntitySubTypeCriteriaDTO criteriaDTO);

    /**
     * 获取全部工序对应的工序阶段列表
     *
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return
     */
    List<BpmProcessStage> getProcessStageList(Long projectId, Long orgId);

    /**
     * 获取工序对应的实体类型分类列表
     *
     * @param id
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return
     */
    List<BpmEntityType> getEntityTypeList(Long id, Long projectId, Long orgId);

    /**
     * 获取工序对应的工作流模型
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    BpmReDeployment findActivityModel(Long orgId, Long projectId, Long processId);

    /**
     * 获取工序对应的工序分类列表
     *
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return
     */
    List<BpmProcessCategory> getProcessCategoryList(Long projectId, Long orgId);

    /**
     * 根据英文名查询工序
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param nameEn
     * @return
     */
    List<BpmProcess> findByOrgIdAndProjectIdAndNameEn(Long orgId, Long projectId, String nameEn);

    /**
     * 根据阶段和工序CODE获取工序信息。
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param stage       阶段CODE
     * @param processCode 工序CODE
     * @return 工序信息
     */
    BpmProcess findByStageAndProcessCode(Long orgId, Long projectId, String stage, String processCode);

    /**
     * 查询工序权限列表
     *
     * @param id
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return
     */
    List<TaskPrivilegeDTO> getProcessPrivileges(Long orgId, Long projectId, Long id);

    /**
     * 设置工序权限
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @param dTO
     * @return
     */
    boolean setProcessPrivileges(Long orgId, Long projectId, Long id, TaskPrivilegeDTO dTO);

    /**
     * 获取全部 工序阶段-工序 列表
     *
     * @return 工序阶段-工序 列表
     */
    List<ProcessKeyDTO> getProcessKeys(Long orgId, Long projectId);



    BpmProcess getBpmProcess(Long projectId, String stage, String process);


    BpmProcess getBpmProcess(Long processId);


    void setBpmProcess(String discipline, String stage, String process, BpmProcess bpmProcess);

    void setBpmProcess(BpmProcess bpmProcess);

    /**
     * bpmn文件部署后 刷新内存redis中的bpmnTaskRelation
     * @param processBpmnRelations 部署的bpmn
     */
    void redeployBpmnInRedis(Long projectId,
                             Long moduleProcessDefinitionId,
                             int version,
                             List<ProcessBpmnRelation> processBpmnRelations);

    /**
     * bpmn文件部署后 取得内存redis中的bpmnTaskRelation
     * @param taskDefKey 部署的bpmn PROCESS_BPMN:ProcessId:TaskDefKey
     */
    ProcessBpmnRelation getBpmnRelation(Long projectId, Long processId,  int bpmnVersion, String taskDefKey);

    /**
     * 设置版本规则
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @param dTO
     * @return
     */
    boolean setProcessVersionRule(Long orgId, Long projectId, Long id, BpmProcessVersionRuleDTO dTO);

    /**
     * 查询工序版本规则详细信息
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param id        工序id
     */
    BpmProcessVersionRule getVersionRule(Long orgId, Long projectId, Long id);

    List<ProcessHierarchyDTO> getHierarchy(Long orgId, Long projectId);
}
