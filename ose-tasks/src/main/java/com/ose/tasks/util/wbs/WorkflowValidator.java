package com.ose.tasks.util.wbs;

import com.ose.exception.ValidationError;
import com.ose.tasks.dto.process.EntityProcessRelationsDTO;
import org.activiti.bpmn.model.*;

import java.util.*;

/*==============================================================================
  工作流定义中元素类的继承关系：
    BaseElement
      └─ FlowElement
           ├─ FlowNode
           │    ├─ Event
           │    │    ├─ StartEvent
           │    │    └─ EndEvent
           │    ├─ Gateway
           │    │    ├─ ParallelGateway
           │    │    ├─ InclusiveGateway
           │    │    └─ ExclusiveGateway
           │    └─ Activity
           │         └─ Task
           │              └─ UserTask
           └─ SequenceFlow
==============================================================================*/

/**
 * Activiti 工作流校验器。
 */
public class WorkflowValidator extends WorkflowResolver {


    /**
     * 构造方法。
     *
     * @param funcPart    功能块
     */
    public WorkflowValidator(
        String funcPart,
        Map<String, Object> variables
    ) {

        super(
            funcPart,
            variables);
    }

    /**
     * 校验流程节点有效性。
     * 流程节点必须拥有入方向和出方向的连接。
     *
     * @param node   流程节点信息
     * @param errors 错误列表
     */
    private static void validateFlowNode(FlowNode node, List<ValidationError> errors) {

        List<SequenceFlow> incomingFlows = node.getIncomingFlows();
        List<SequenceFlow> outgoingFlows = node.getOutgoingFlows();
        String taskName = node.getName();

        // 除开始节点必须有入方向流程
        if (!(node instanceof StartEvent) && (incomingFlows == null || incomingFlows.size() == 0)) {
            errors.add(new ValidationError(String.format("任务【%s】无入方向连接", taskName)));
        }

        // 除结束节点必须有出方向流程
        if (!(node instanceof EndEvent) && (outgoingFlows == null || outgoingFlows.size() == 0)) {
            errors.add(new ValidationError(String.format("任务【%s】无出方向连接", taskName)));
        }

    }

    /**
     * 校验任务节点的有效性。
     * 任务节点的 Category 属性必须能够唯一标识该节点，且必须符合以下格式：
     * 工序阶段/工序/实体类型[/实体子类型1[:实体子类型2[:...]]]
     * 例如：
     * FABRICATION/下料/PIPE_PIECE
     * FABRICATION/焊接/WELD/SBW
     * 并且工序阶段的工序、实体类型（的子类型）必须已定义，且已为实体类型指定了相应的工序。
     *
     * @param task                      任务节点信息
     * @param entityProcessRelationsDTO 工序-实体类型映射表
     * @param categoryMap               任务节点分类值使用记录
     * @param errors                    错误列表
     */
    private static void validateTaskElement(
        UserTask task,
        EntityProcessRelationsDTO entityProcessRelationsDTO,
        Map<String, String> categoryMap,
        List<ValidationError> errors
    ) {

        String taskName = task.getName();

        TaskCategoryResolver taskCategory = new TaskCategoryResolver(task.getCategory());

        if (taskCategory.getValid() == null) {
            errors.add(new ValidationError(String.format(
                "必须指定任务【%s】的分类",
                taskName
            )));
            return;
        }

        if (!taskCategory.getValid()) {
            errors.add(new ValidationError(String.format(
                "任务【%s】的分类值【%s】格式不正确",
                taskName, task.getCategory()
            )));
            return;
        }

        /* 工序有效性检查 START */

        String stage = taskCategory.getStage();
        String process = taskCategory.getProcess();
        boolean isProcessValid = false;

        if (!entityProcessRelationsDTO.doesProcessStageExist(stage)) {
            errors.add(new ValidationError(String.format(
                "任务【%s】的工序阶段【%s】无效",
                taskName, stage
            )));
        } else if (!entityProcessRelationsDTO.doesProcessExist(stage, process)) {
            errors.add(new ValidationError(String.format(
                "任务【%s】的【%s】阶段的工序【%s】无效",
                taskName, stage, process
            )));
        } else {
            isProcessValid = true;
        }

        /* 工序有效性检查 END */

        /* 实体类型有效性检查 START */

        String entityType = taskCategory.getEntityType();
        Set<String> subTypes = taskCategory.getEntitySubTypes();
        boolean isEntityTypeValid = false;

        if (!entityProcessRelationsDTO.doesEntityTypeExist(entityType)) {
            errors.add(new ValidationError(String.format(
                "任务【%s】的实体类型【%s】无效",
                taskName, entityType
            )));
        } else if (!entityProcessRelationsDTO.doEntitySubTypesExist(entityType, subTypes)) {
            errors.add(new ValidationError(String.format(
                "任务【%s】的实体类型【%s】的子类型【%s】无效",
                taskName, entityType, String.join(":", subTypes)
            )));
        } else {
            isEntityTypeValid = true;
        }

        /* 实体类型有效性检查 END */

        // 检查是否已为实体类型指定相应的工序
        if (isProcessValid
            && isEntityTypeValid
            && !entityProcessRelationsDTO
            .doesProcessEntityRelationsExist(stage, process, entityType, subTypes)) {
            errors.add(new ValidationError(String.format(
                "任务【%s】的分类值【%s】描述的实体-工序关系无效",
                taskName, task.getCategory()
            )));
        }

        /* Category 属性唯一性检查 START */

        String previousTaskName;

        if (subTypes.size() > 0) {

            for (String entitySubType : subTypes) {

                previousTaskName = categoryMap.put(
                    String.join("/", stage, process, entityType, entitySubType),
                    taskName
                );

                if (previousTaskName != null) {
                    errors.add(new ValidationError(String.format(
                        "任务【%s】的分类值【%s】已被任务【%s】使用",
                        taskName, task.getCategory(), previousTaskName
                    )));
                }

            }

        } else {

            previousTaskName = categoryMap.put(
                String.join("/", stage, process, entityType),
                taskName
            );

            if (previousTaskName != null) {
                errors.add(new ValidationError(String.format(
                    "任务【%s】的分类值【%s】已被任务【%s】使用",
                    taskName, task.getCategory(), previousTaskName
                )));
            }

        }

        /* Category 属性唯一性检查 END */

    }

    /**
     * 校验工作流定义格式。
     *
     * @param entityProcessRelationsDTO         工序-实体类型映射表
     * @param bpmnModel                         Bpmn流程部署文件
     * @return
     */
    public static void validate(EntityProcessRelationsDTO entityProcessRelationsDTO,
                                      BpmnModel bpmnModel) {

        Map<String, String> categoryMap = new HashMap<>();
        List<ValidationError> errors = new ArrayList<>();

        for (FlowElement element : bpmnModel.getMainProcess().getFlowElements()) {

            // 校验流程节点有效性
            if (element instanceof FlowNode) {
                validateFlowNode((FlowNode) element, errors);
            }

            // 校验任务节点有效性
            if (element instanceof UserTask) {
                validateTaskElement(
                    (UserTask) element,
                    entityProcessRelationsDTO,
                    categoryMap,
                    errors
                );
            }

        }

        if (errors.size() > 0) {
            throw new ValidationError(errors);
        }

    }

}
