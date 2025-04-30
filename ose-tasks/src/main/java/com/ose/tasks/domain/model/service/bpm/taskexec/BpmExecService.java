package com.ose.tasks.domain.model.service.bpm.taskexec;

import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.dto.BpmnGatewayDTO;
import com.ose.tasks.dto.BpmnSequenceNodeDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.ProcessBpmnRelation;
import com.ose.tasks.vo.BpmnGatewayType;
import com.ose.util.CollectionUtils;
import com.ose.util.SpElUtils;
import com.ose.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 重写 bpm 服务中的 任务执行基类。
 * execResult.getVariables().put("jsonDocuments",documents); //设置文档信息
 * execResult.getVariables().put("setAttachmentsFlag","NONE");//设置是否设置附件
 * execResult.getVariables().put("setComment", execResult.getTodoTaskDTO().getComment());//设置 comment的内容
 */
@Component
public class BpmExecService implements BpmExecInterface {
    private final static Logger logger = LoggerFactory.getLogger(BpmExecService.class);

    private final WBSEntryStateRepository wbsEntryStateRepository;

    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    // 上传文件的临时路径
    @Value("${application.files.protected}")
    private String protectedDir;


    /**
     * 构造方法
     *
     * @param
     * @param wbsEntryStateRepository
     * @return
     */
    @Autowired
    public BpmExecService(
        WBSEntryStateRepository wbsEntryStateRepository) {
        this.wbsEntryStateRepository = wbsEntryStateRepository;
    }


    @Override
    public ExecResultDTO complete(BpmProcTaskDTO bpmProcTaskDTO) {
        return null;
    }

    @Override
    public CreateResultDTO create(CreateResultDTO createResult) {
        return null;
    }

    @Override
    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchComplete(ContextDTO contextDTO, Map<String, Object> data, P todoBatchTaskCriteriaDTO) {
        return null;
    }

    @Override
    public RevocationDTO revocation(ContextDTO context, Long orgId, Long projectId, ActInstSuspendDTO actInstSuspendDTO) {
        return null;
    }

    @Override
    public RevocationDTO batchRevocation(ContextDTO context, Long orgId, Long projectId, ActInstSuspendDTO actInstSuspendDTO) {
        return null;
    }

    @Override
    public JsonResponseBody suspendTask(ContextDTO context, Long orgId, Long projectId, Long taskId, ActInstSuspendDTO dto) {
        return null;
    }

    /**
     * 演算。
     *
     * @param processBpmnRelation  需要演算的 流程节点关系DTO
     * @param variables         实体对应的流程变量 WELD_JOINT -> weldEntityMap
     * @return 是否可以启动目标任务节点
     */
    @Override
    public Set<NextTaskDTO> evaluate(
        final ProcessBpmnRelation processBpmnRelation,
        final Map<String, Object> variables
    ) {
        //计划运行时
        Set<NextTaskDTO> nextNodes = new HashSet<>();
        List<BpmnSequenceNodeDTO> successorNodes = processBpmnRelation.getJsonSuccessorNodes();

        if(CollectionUtils.isEmpty(successorNodes)){
            return nextNodes;
        }

        //将 stage/process/entityType/entitySubType1:entitySubType2的数据展开
//        predecessorNodes = BPMNUtils.expensionNodes(bpmnTaskRelation.getJsonPredecessorNodes());

        for(BpmnSequenceNodeDTO successorNode : successorNodes) {
            //如果网关为空，则可以到达，返回 true
            if(CollectionUtils.isEmpty(successorNode.getGateways()) &&
                successorNode.getNodeType().equalsIgnoreCase("USERTASK")){
                NextTaskDTO nextTaskNodeDTO = new NextTaskDTO();
                nextTaskNodeDTO.setCategory(successorNode.getCategory());
                nextTaskNodeDTO.setTaskDefKey(successorNode.getNodeId());
                nextTaskNodeDTO.setTaskName(successorNode.getNodeName());
                nextTaskNodeDTO.setTaskType(successorNode.getTaskType());
                nextTaskNodeDTO.setNodeType(successorNode.getNodeType());
                nextNodes.add(nextTaskNodeDTO);
            } else if(CollectionUtils.isEmpty(successorNode.getGateways())) {
                NextTaskDTO nextTaskNodeDTO = new NextTaskDTO();
                nextTaskNodeDTO.setCategory(successorNode.getCategory());
                nextTaskNodeDTO.setTaskDefKey(successorNode.getNodeId());
                nextTaskNodeDTO.setTaskName(successorNode.getNodeName());
                nextTaskNodeDTO.setTaskType(successorNode.getTaskType());
                nextTaskNodeDTO.setNodeType(successorNode.getNodeType());
                nextNodes.add(nextTaskNodeDTO);
            } else {
                Boolean isPassed = false;
                for(BpmnGatewayDTO gateway : successorNode.getGateways()) {
                    isPassed = false;
                    //如果是并行网关,网关没有条件 ，通过
                    if(gateway.getGatewayType().equalsIgnoreCase(BpmnGatewayType.PARALLEL.name())) {//忽略并行网关
                        isPassed = true;
                    }
                    String condition = gateway.getGateWayCondition();
                    //如果条件满足
                    if(SpElUtils.processReadExpr(condition,variables)) {
                        isPassed = true;
                    }

                    //如果条件满足
                    if(!isPassed) {
                        break;
                    }

                }

                if(isPassed) {
                    NextTaskDTO nextTaskNodeDTO = new NextTaskDTO();
                    nextTaskNodeDTO.setCategory(successorNode.getCategory());
                    nextTaskNodeDTO.setTaskDefKey(successorNode.getNodeId());
                    nextTaskNodeDTO.setTaskName(successorNode.getNodeName());
                    nextTaskNodeDTO.setTaskType(successorNode.getTaskType());
                    nextTaskNodeDTO.setNodeType(successorNode.getNodeType());
                    nextNodes.add(nextTaskNodeDTO);
                }
            }
        }
        if(nextNodes.isEmpty()) {
            throw new BusinessError("THERE IS NO POSSIBLE PATH FOR BPMN" + processBpmnRelation.getNodeId() + " COMMAND :" + StringUtils.toJSON(variables));
        }

        if(nextNodes.iterator().next().getNodeType().equalsIgnoreCase("ENDEVENT")) return new HashSet<>();
        return nextNodes;

    }

//    public static void main(String[] args) {
//        System.out.println("Hello World!");
//        String condition = "${RESULT==\"OK\"}";
//        Map<String, Object> variables = new HashMap<>();
//        variables.put("RESULT","OK");
//        //如果条件满足
//        SpElUtils.processReadExpr(condition,variables);
//
//    }
}
