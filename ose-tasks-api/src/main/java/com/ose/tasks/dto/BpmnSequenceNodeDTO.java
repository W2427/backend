package com.ose.tasks.dto;


import java.util.List;
import java.util.Set;

/**
 * BPMN 流程节点 包括 网关 + 节点
 * Smaple 排他网关 + EL条件 + 下一个 UserTask
 */
public class BpmnSequenceNodeDTO extends BpmnBaseDTO {


    private static final long serialVersionUID = 7713070638708909743L;

    private List<BpmnGatewayDTO> gateways;

    //对于 运行中判断的网关 RUNTIME
    private Boolean optional = false;

    private Set<String> relatedNodeIds;

    public List<BpmnGatewayDTO> getGateways() {
        return gateways;
    }

    public void setGateways(List<BpmnGatewayDTO> gateways) {
        this.gateways = gateways;
    }

    public Boolean getOptional() {
        return optional;
    }

    public void setOptional(Boolean optional) {
        this.optional = optional;
    }

    public Set<String> getRelatedNodeIds() {
        return relatedNodeIds;
    }

    public void setRelatedNodeIds(Set<String> relatedNodeIds) {
        this.relatedNodeIds = relatedNodeIds;
    }
}
