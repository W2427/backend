package com.ose.tasks.domain.model.service.bpm;

import java.util.List;

import com.ose.tasks.dto.bpm.ModelDeployDTO;
import com.ose.tasks.dto.bpm.ModelTaskNode;
import org.springframework.data.domain.Page;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.ActivitiModelCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmActivityCategory;
import com.ose.tasks.entity.bpm.BpmReDeployment;
import com.ose.tasks.vo.SuspensionState;

public interface ActivityModelInterface {

    /**
     * 创建流程部署模型
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param modelDeployDTO
     * @param operatorDTO
     * @return
     */
    BpmReDeployment createBpmModels(Long orgId, Long projectId, ModelDeployDTO modelDeployDTO, OperatorDTO operatorDTO);

    /**
     * 流程模型列表
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param criteriaDTO
     * @param page
     * @return
     */
    Page<BpmReDeployment> list(Long orgId, Long projectId, ActivitiModelCriteriaDTO criteriaDTO, PageDTO page);

    /**
     * 获取流程模型信息
     *
     * @param procDefId 流程定义id
     * @return
     */
    BpmReDeployment findByProcDefId(String procDefId);

    /**
     * 修改挂起状态
     *
     * @param procDefId
     * @param suspend
     * @return
     */
    boolean updateSuspendState(String procDefId, SuspensionState suspend);

    /**
     * 获取流程模型分类
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    List<BpmActivityCategory> getCategories(Long orgId, Long projectId);

    /**
     * 生成工作流的权限信息以及各任务的权限信息，为了三级计划跟四级计划的人员指定用
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param processId
     * @param taskNodes
     */
    void generateBpmActivityNodeInfo(Long orgId, Long projectId, Long processId, List<ModelTaskNode> taskNodes);

}
