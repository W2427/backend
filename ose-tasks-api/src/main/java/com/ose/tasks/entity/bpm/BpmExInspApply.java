package com.ose.tasks.entity.bpm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.dto.BaseDTO;
import com.ose.util.StringUtils;
import com.ose.vo.InspectParty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Entity
@Table(name = "view_external_inspection_apply")
public class BpmExInspApply extends BaseDTO {

    private static final long serialVersionUID = 7409483222999461504L;

    private Long orgId;

    private Long projectId;

    private String entityNo;

    @Id
    private Long actInstId;

    private String assignee;

    private Date createTime;

    private String processStageName;

    private String processNameEn;

    private String entityCategoryNameEn;

    private Long entitySubTypeId;

    private Long taskId;

    private Boolean isHandling;

    private String entityModuleName;

    @Column(length = 100)
    private String inspectParties;

    private String oldReportNo;

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

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getProcessStageName() {
        return processStageName;
    }

    public void setProcessStageName(String processStageName) {
        this.processStageName = processStageName;
    }

    public String getEntitySubTypeNameEn() {
        return entityCategoryNameEn;
    }

    public void setEntitySubTypeNameEn(String entityCategoryNameEn) {
        this.entityCategoryNameEn = entityCategoryNameEn;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getEntityModuleName() {
        return entityModuleName;
    }

    public void setEntityModuleName(String entityModuleName) {
        this.entityModuleName = entityModuleName;
    }

    public String getProcessNameEn() {
        return processNameEn;
    }

    public void setProcessNameEn(String processNameEn) {
        this.processNameEn = processNameEn;
    }

    @JsonProperty(value = "inspectParties", access = JsonProperty.Access.READ_ONLY)
    public List<InspectParty> getJsonInspectParties() {
        if (inspectParties != null && !"".equals(inspectParties)) {
            return StringUtils.decode(inspectParties, new TypeReference<List<InspectParty>>() {
            });
        } else {
            return new ArrayList<InspectParty>() {{
                add(InspectParty.OWNER);
            }};
        }
    }

    @JsonSetter
    public void setInspectParties(List<InspectParty> inspectParties) {
        if (inspectParties != null) {
            this.inspectParties = StringUtils.toJSON(inspectParties);
        } else {
            this.inspectParties = StringUtils.toJSON(InspectParty.OWNER);
        }
    }

    public String getInspectParties() {
        return inspectParties;
    }

    public void setInspectParties(String inspectParties) {
        this.inspectParties = inspectParties;
    }

    public Boolean getHandling() {
        return isHandling;
    }

    public void setHandling(Boolean handling) {
        this.isHandling = handling;
    }

    public String getOldReportNo() {
        return oldReportNo;
    }

    public void setOldReportNo(String oldReportNo) {
        this.oldReportNo = oldReportNo;
    }
}
