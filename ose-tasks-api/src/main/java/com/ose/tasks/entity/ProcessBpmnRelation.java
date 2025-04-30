package com.ose.tasks.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ose.tasks.dto.BpmnBaseDTO;
import com.ose.tasks.dto.BpmnSequenceNodeDTO;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "process_bpmn_relation",
indexes = {
    @Index(columnList = "projectId,processBpmnDeployId"),
    @Index(columnList = "projectId,processId,bpmnVersion")
})
public class ProcessBpmnRelation extends BpmnBaseDTO {


    private static final long serialVersionUID = 3899900829018341581L;
    @Column
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    @Column
    private Boolean deleted;

    @Column
    private Long orgId;

    @Column
    private Long projectId;

    //流程 对应的 工序 Id, bpm_re_deployment id
    @Column
    private Long processBpmnDeployId;

    @Column
    private Long processId;

    @Column
    private int bpmnVersion;

    @Column(columnDefinition = "TEXT", length = 65535)
    private String predecessorNodes;

    @Column(columnDefinition = "TEXT", length = 65535)
    private String successorNodes;

    @Column(columnDefinition = "TEXT", length = 65535)
    private String preEntityTypeRelationMap;


    @Column(columnDefinition = "TEXT", length = 65535)
    private String formValueMap;

    @Column(columnDefinition = "TEXT", length = 65535)
    private String taskGateWays;

    public ProcessBpmnRelation() {
    }

    public ProcessBpmnRelation(Long processBpmnDeployId) {
        super();
        this.processBpmnDeployId = processBpmnDeployId;
    }

    @JsonCreator
    public ProcessBpmnRelation(@JsonProperty("successorNodes") List<BpmnSequenceNodeDTO> successorNodes,
                               @JsonProperty("predecessorNodes") List<BpmnSequenceNodeDTO> predecessorNodes) {
        this.successorNodes = StringUtils.toJSON(successorNodes);
        this.predecessorNodes = StringUtils.toJSON(predecessorNodes);
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getPredecessorNodes() {
        return predecessorNodes;
    }

    public void setPredecessorNodes(String predecessorNodes) {
        this.predecessorNodes = predecessorNodes;
    }

    public String getSuccessorNodes() {
        return successorNodes;
    }

    public void setSuccessorNodes(String successorNodes) {
        this.successorNodes = successorNodes;
    }

    @JsonProperty(value = "predecessorNodes", access = JsonProperty.Access.READ_ONLY)
    public List<BpmnSequenceNodeDTO> getJsonPredecessorNodes() {
        if (predecessorNodes != null && !"".equals(predecessorNodes)) {
            return StringUtils.decode(predecessorNodes, new TypeReference<List<BpmnSequenceNodeDTO>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonPredecessorNodes(List<BpmnSequenceNodeDTO> predecessorNodes) {
        if (predecessorNodes != null) {
            this.predecessorNodes = StringUtils.toJSON(predecessorNodes);
        }
    }


    @JsonProperty(value = "successorNodes", access = JsonProperty.Access.READ_ONLY)
    public List<BpmnSequenceNodeDTO> getJsonSuccessorNodes() {
        if (successorNodes != null && !"".equals(successorNodes)) {
            return StringUtils.decode(successorNodes, new TypeReference<List<BpmnSequenceNodeDTO>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonSuccessorNodes(List<BpmnSequenceNodeDTO> successorNodes) {
        if (successorNodes != null) {
            this.successorNodes = StringUtils.toJSON(successorNodes);
        }
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getProcessBpmnDeployId() {
        return processBpmnDeployId;
    }

    public void setProcessBpmnDeployId(Long processBpmnDeployId) {
        this.processBpmnDeployId = processBpmnDeployId;
    }

    public String getPreEntityTypeRelationMap() {
        return preEntityTypeRelationMap;
    }

    public void setPreEntityTypeRelationMap(String preEntityTypeRelationMap) {
        this.preEntityTypeRelationMap = preEntityTypeRelationMap;
    }

    public Map<String, List<String>> extractPreEntityTypeRelationMap(String preEntityTypeRelationMap) {
        ObjectMapper mapper = new ObjectMapper(); //转换器
        Map m = null;
        try {
            m = mapper.readValue(preEntityTypeRelationMap, Map.class);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return m;
    }

    public String strPreEntityTypeRelationMap(Map<String, Set<String>> preEntityTypeRelationMap) {
        ObjectMapper mapper = new ObjectMapper(); //转换器
        String mapStr = null;
        try {
            mapStr = mapper.writeValueAsString(preEntityTypeRelationMap);
            this.preEntityTypeRelationMap = mapStr;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return mapStr;
    }


    public String getFormValueMap() {
        return formValueMap;
    }

    public void setFormValueMap(String formValueMap) {
        this.formValueMap = formValueMap;
    }

    public Map<String, String> extractFormValueMap(String formValueMap) {
        ObjectMapper mapper = new ObjectMapper(); //转换器
        Map m = null;
        try {
            m = mapper.readValue(formValueMap, Map.class);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return m;
    }

    public String strFormValueMap(Map<String, String> formValueMap) {
        ObjectMapper mapper = new ObjectMapper(); //转换器
        String mapStr = null;
        try {
            mapStr = mapper.writeValueAsString(formValueMap);
            this.formValueMap = mapStr;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return mapStr;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public int getBpmnVersion() {
        return bpmnVersion;
    }

    public void setBpmnVersion(int bpmnVersion) {
        this.bpmnVersion = bpmnVersion;
    }

    public String getTaskGateWays() {
        return taskGateWays;
    }

    public void setTaskGateWays(String taskGateWays) {
        this.taskGateWays = taskGateWays;
    }
}
