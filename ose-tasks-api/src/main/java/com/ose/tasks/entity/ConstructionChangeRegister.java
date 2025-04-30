package com.ose.tasks.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.ConstructionChangeType;
import com.ose.util.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 建造变更表
 */
@Entity
@Table(name = "construction_change_register")
public class ConstructionChangeRegister extends BaseBizEntity {

    private static final long serialVersionUID = -4387445149304502246L;

    @Schema(description = "组织id")
    private Long orgId;

    @Schema(description = "项目id")
    private Long projectId;

    @Transient
    private boolean hasTask;

    @Schema(description = "变更类型")
    @Enumerated(EnumType.STRING)
    private ConstructionChangeType changeType;

    @Schema(description = "登记号")
    private String registerNo;

    @Schema(description = "模块名称")
    private String modelName;

    @Schema(description = "生成者")
    private String originatedBy;

    @Schema(description = "有效")
    private String actions;

    @Schema(description = "提出人")
    private String createBy;

    @Schema(description = "提出人id")
    private Long createById;

    @Schema(description = "审核是否通过")
    private boolean passFlag = false;

    @Schema(description = "图纸流程实力ids")
    @Column(columnDefinition = "text")
    private String dwgActInstIds;

    @JsonProperty(value = "jsonDwgActInstIds", access = JsonProperty.Access.READ_ONLY)
    public List<Long> getJsonDwgActInstIdsReadOnly() {
        if (dwgActInstIds != null && !"".equals(dwgActInstIds)) {
            return StringUtils.decode(dwgActInstIds, new TypeReference<List<Long>>() {
            });
        } else {
            return null;
        }
    }

    @JsonIgnore
    public void setJsonDwgActInstIds(List<Long> dwgActInstIds) {
        if (dwgActInstIds != null) {
            this.dwgActInstIds = StringUtils.toJSON(dwgActInstIds);
        }
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

    public boolean isHasTask() {
        return hasTask;
    }

    public void setHasTask(boolean hasTask) {
        this.hasTask = hasTask;
    }

    public ConstructionChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ConstructionChangeType changeType) {
        this.changeType = changeType;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getOriginatedBy() {
        return originatedBy;
    }

    public void setOriginatedBy(String originatedBy) {
        this.originatedBy = originatedBy;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Long getCreateById() {
        return createById;
    }

    public void setCreateById(Long createById) {
        this.createById = createById;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public boolean isPassFlag() {
        return passFlag;
    }

    public void setPassFlag(boolean passFlag) {
        this.passFlag = passFlag;
    }

}
