package com.ose.tasks.util.wbs;

import com.ose.tasks.dto.BpmnGatewayDTO;
import com.ose.tasks.dto.BpmnSequenceNodeDTO;
import com.ose.tasks.entity.BpmnTaskRelation;
import com.ose.tasks.vo.BpmnGatewayType;
import com.ose.util.CollectionUtils;
import com.ose.util.SpElUtils;
import com.ose.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Activiti 工作流演算器。
 */
public class WorkflowEvaluator extends WorkflowResolver {

    // 临时元素序列号
    private static long serialNo = System.nanoTime();

    // 使用运行时条件的网关的名称
    private static String RUNTIME_GATEWAY_NAME = "RUNTIME_GATEWAY";

    // 临时元素序列号最大值
    private static final long SERIAL_NO_MAX = 1000000000L;





    /**
     * 构造方法。
     *
     * @param funcPart      功能块
     * @param variables     工作流执行参数
     */
    public WorkflowEvaluator(
        String funcPart,
        Map<String, Object> variables
    ) {
        super(funcPart, variables);
    }



    /**
     * 演算（计划生成）。
     *
     * @param bpmnTaskRelation  需要演算的 流程节点关系DTO
     * @param variables         实体对应的流程变量 WELD_JOINT -> weldEntityMap
     * @return 是否可以启动目标任务节点
     */
    public static Boolean evaluate(
        final BpmnTaskRelation bpmnTaskRelation,
        final Map<String, Object> variables
    ) {
        return evaluate(bpmnTaskRelation, variables, false);
    }

    /**
     * 演算。
     *
     * @param bpmnTaskRelation  需要演算的 流程节点关系DTO
     * @param variables         实体对应的流程变量 WELD_JOINT -> weldEntityMap
     * @param execution         是否为实际执行
     * @param predeccesor       当前节点 stage/process/type/subtype
     * @return 是否可以启动目标任务节点
     */
    public static Boolean evaluate(
        final BpmnTaskRelation bpmnTaskRelation,
        final Map<String, Object> variables,
        final Boolean execution,
        final String predeccesor
    ) {

        if(StringUtils.isEmpty(predeccesor)) {
            return evaluate(bpmnTaskRelation, variables, execution);
        }
        List<BpmnSequenceNodeDTO> predecessorNodes = bpmnTaskRelation.getJsonPredecessorNodes();

        if(CollectionUtils.isEmpty(predecessorNodes)){
            return !execution;
        }

        for(BpmnSequenceNodeDTO predecessorNode : predecessorNodes) {
            if(!predeccesor.equalsIgnoreCase(predecessorNode.getCategory())) {
                continue;
            }
            //如果网关为空，则可以到达，返回 true
            if(CollectionUtils.isEmpty(predecessorNode.getGateways())) {
                return true;
            } else {
                Boolean passFlag = true;
                for(BpmnGatewayDTO gateway : predecessorNode.getGateways()) {
                    //如果是并行网关,网关没有条件 ，通过
                    if(gateway.getGatewayType().equalsIgnoreCase(BpmnGatewayType.PARALLEL.name()) || //忽略并行网关
                        StringUtils.isEmpty(gateway.getGateWayCondition()) ||   //如果条件为空 忽略
                        (!execution && gateway.getGateWayName().equalsIgnoreCase(RUNTIME_GATEWAY_NAME)) //|| //生成计划时忽略 RUNTIME网关
//                        (execution  && !gateway.getGateWayName().equalsIgnoreCase(RUNTIME_GATEWAY_NAME)) //执行计划时忽略非RUNTIME网关
                    ) {
                        continue;
                    }
                    String condition = gateway.getGateWayCondition();
                    //如果条件满足
                    if(!SpElUtils.readExpr(condition,variables)) {
                        passFlag = false;
                        break;
                    }

                }
                if(passFlag) {
                    return true;
                }
            }
        }
        return false;



    }

    /**
     * 演算。
     *
     * @param bpmnTaskRelation  需要演算的 流程节点关系DTO
     * @param variables         实体对应的流程变量 WELD_JOINT -> weldEntityMap
     * @param execution         是否为实际执行
     * @return 是否可以启动目标任务节点
     */
    public static Boolean evaluate(
        final BpmnTaskRelation bpmnTaskRelation,
        final Map<String, Object> variables,
        final Boolean execution
    ) {
        //计划运行时
        List<BpmnSequenceNodeDTO> predecessorNodes = bpmnTaskRelation.getJsonPredecessorNodes();

        if(CollectionUtils.isEmpty(predecessorNodes)){
            return !execution;
        }

        //将 stage/process/entityType/entitySubType1:entitySubType2的数据展开
//        predecessorNodes = BPMNUtils.expensionNodes(bpmnTaskRelation.getJsonPredecessorNodes());

        for(BpmnSequenceNodeDTO predecessorNode : predecessorNodes) {
            //如果网关为空，则可以到达，返回 true
            if(CollectionUtils.isEmpty(predecessorNode.getGateways())) {
                return true;
            } else {
                Boolean passFlag = true;
                for(BpmnGatewayDTO gateway : predecessorNode.getGateways()) {
                    //如果是并行网关,网关没有条件 ，通过
                    if(gateway.getGatewayType().equalsIgnoreCase(BpmnGatewayType.PARALLEL.name()) || //忽略并行网关
                        StringUtils.isEmpty(gateway.getGateWayCondition()) ||   //如果条件为空 忽略
                        (!execution && gateway.getGateWayName().equalsIgnoreCase(RUNTIME_GATEWAY_NAME)) //|| //生成计划时忽略 RUNTIME网关
//                        (execution  && !gateway.getGateWayName().equalsIgnoreCase(RUNTIME_GATEWAY_NAME)) //执行计划时忽略非RUNTIME网关
                        ) {
                        continue;
                    }
                    String condition = gateway.getGateWayCondition();
                    //如果条件满足
                    if(!SpElUtils.readExpr(condition,variables)) {
                        passFlag = false;
                        break;
                    }

                }
                if(passFlag) {
                    return true;
                }
            }
        }
        return false;

    }


}
