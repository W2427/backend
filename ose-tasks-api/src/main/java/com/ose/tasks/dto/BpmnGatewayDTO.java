package com.ose.tasks.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.dto.BaseDTO;
import com.ose.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * BPMN 流程节点 网关
 */
public class BpmnGatewayDTO extends BaseDTO {


    private static final long serialVersionUID = 6170159742991953475L;

    private String gateWayName;

    private String gateWayCondition;

    private String gateWayId;

    //PARALLEL, INCLUSIVE, EXCLUSIVE, EVENT
    private String gatewayType;

    private String incomingNodes;

    public BpmnGatewayDTO() {
    }

    @JsonCreator
    public BpmnGatewayDTO(@JsonProperty("incomingNodes") List<String> incomingNodes) {
        this.incomingNodes = StringUtils.toJSON(incomingNodes);
    }

    @JsonProperty(value = "incomingNodes", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonIncomingNodes() {
        if (incomingNodes != null && !"".equals(incomingNodes)) {
            return StringUtils.decode(incomingNodes, new TypeReference<List<String>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonIncomingNodes(List<String> incomingNodes) {
        if (incomingNodes != null) {
            this.incomingNodes = StringUtils.toJSON(incomingNodes);
        }
    }

    public String getGateWayName() {
        return gateWayName;
    }

    public void setGateWayName(String gateWayName) {
        this.gateWayName = gateWayName;
    }

    public String getGateWayCondition() {
        return gateWayCondition;
    }

    @JsonSetter
    public void setGateWayCondition(String gateWayCondition) {
        this.gateWayCondition = gateWayCondition;
    }

    public void setGateWayCondition(String gateWayCondition, List<String> entityTypeNames) {
        if(gateWayCondition == null) {
            this.gateWayCondition = "";
            return;
        }

        for(String entityTypeName : entityTypeNames) {
            gateWayCondition = gateWayCondition.replaceAll("([^_a-zA-Z]|^)("+entityTypeName+"\\.)([^_a-zA-Z]{0,1})", "$1#" +entityTypeName+"\\.$3");

//            gateWayCondition = gateWayCondition.replaceAll( entityTypeName + "\\.", "#" + entityTypeName + "\\.");
        }
        this.gateWayCondition = gateWayCondition;

    }


    public String getGateWayId() {
        return gateWayId;
    }

    public void setGateWayId(String gateWayId) {
        this.gateWayId = gateWayId;
    }

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }
}
