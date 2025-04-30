package com.ose.tasks.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.util.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 设计变更校验表。
 */
@Entity
@Table(name = "design_change_review_form")
public class DesignChangeReviewForm extends BaseBizEntity {

    private static final long serialVersionUID = -4387445149304502246L;

    @Schema(description = "组织id")
    private Long orgId;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "审核登记id")
    private Long reviewRegisterId;

    @Schema(description = "项目名")
    private String projectName;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "报告号")
    private String reportNo;

    @Schema(description = "设计变更提出人")
    private String raisedBy;

    @Schema(description = "设计变更提出人id")
    private Long raisedById;

    @Schema(description = "修改发生根源")
    @Column(columnDefinition = "text")
    private String originatedBy;

    @Schema(description = "修改原因描述")
    private String causeDescription;

    @Schema(description = "涉及专业")
    @Column(columnDefinition = "text")
    private String involvedDisciplines;

    @Schema(description = "行动条款")
    @Column(columnDefinition = "text")
    private String actionItem;

    @Schema(description = "图纸升级后版本")
    @Column(columnDefinition = "text")
    private String itemVersion;


    @Schema(description = "变更号")
    private String vorNo;

    @Schema(description = "设计人员工时")
    @Column(columnDefinition = "text")
    private String engineeringManhours;

    @Schema(description = "材料")
    @Column(columnDefinition = "text")
    private String materials;

    @Schema(description = "结束标识")
    @Enumerated(EnumType.STRING)
    private ActInstFinishState finishState = ActInstFinishState.NOT_FINISHED;

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

    public Long getReviewRegisterId() {
        return reviewRegisterId;
    }

    public void setReviewRegisterId(Long reviewRegisterId) {
        this.reviewRegisterId = reviewRegisterId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public Long getRaisedById() {
        return raisedById;
    }

    public void setRaisedById(Long raisedById) {
        this.raisedById = raisedById;
    }

    public String getOriginatedBy() {
        return originatedBy;
    }

    public void setOriginatedBy(String originatedBy) {
        this.originatedBy = originatedBy;
    }

    @JsonProperty(value = "jsonOriginatedBy", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonOriginatedByReadOnly() {
        if (originatedBy != null && !"".equals(originatedBy)) {
            return StringUtils.decode(originatedBy, new TypeReference<List<String>>() {
            });
        } else {
            return null;
        }
    }

    @JsonIgnore
    public void setJsonOriginatedBy(List<String> originatedBy) {
        if (originatedBy != null) {
            this.originatedBy = StringUtils.toJSON(originatedBy);
        }
    }

    public String getCauseDescription() {
        return causeDescription;
    }

    public void setCauseDescription(String causeDescription) {
        this.causeDescription = causeDescription;
    }

    public String getInvolvedDisciplines() {
        return involvedDisciplines;
    }

    public void setInvolvedDisciplines(String involvedDisciplines) {
        this.involvedDisciplines = involvedDisciplines;
    }

    @JsonProperty(value = "jsonInvolvedDisciplines", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonInvolvedDisciplinesReadOnly() {
        if (involvedDisciplines != null && !"".equals(involvedDisciplines)) {
            return StringUtils.decode(involvedDisciplines, new TypeReference<List<String>>() {
            });
        } else {
            return null;
        }
    }

    @JsonIgnore
    public void setJsonInvolvedDisciplines(List<String> involvedDisciplines) {
        if (involvedDisciplines != null) {
            this.involvedDisciplines = StringUtils.toJSON(involvedDisciplines);
        }
    }

    public String getActionItem() {
        return actionItem;
    }

    public void setActionItem(String actionItem) {
        this.actionItem = actionItem;
    }

    @JsonProperty(value = "jsonActionItem", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonActionItemReadOnly() {
        if (actionItem != null && !"".equals(actionItem)) {
            return StringUtils.decode(actionItem, new TypeReference<List<String>>() {
            });
        } else {
            return null;
        }
    }

    @JsonIgnore
    public void setJsonActionItem(List<String> actionItem) {
        if (actionItem != null) {
            this.actionItem = StringUtils.toJSON(actionItem);
        }
    }

    public String getVorNo() {
        return vorNo;
    }

    public void setVorNo(String vorNo) {
        this.vorNo = vorNo;
    }

    public String getEngineeringManhours() {
        return engineeringManhours;
    }

    public void setEngineeringManhours(String engineeringManhours) {
        this.engineeringManhours = engineeringManhours;
    }

    @JsonProperty(value = "jsonEngineeringManhours", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonEngineeringManhoursReadOnly() {
        if (engineeringManhours != null && !"".equals(engineeringManhours)) {
            return StringUtils.decode(engineeringManhours, new TypeReference<List<String>>() {
            });
        } else {
            return null;
        }
    }

    @JsonIgnore
    public void setJsonEngineeringManhours(List<String> engineeringManhours) {
        if (engineeringManhours != null) {
            this.engineeringManhours = StringUtils.toJSON(engineeringManhours);
        }
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    @JsonProperty(value = "jsonMaterials", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonMaterialsReadOnly() {
        if (materials != null && !"".equals(materials)) {
            return StringUtils.decode(materials, new TypeReference<List<String>>() {
            });
        } else {
            return null;
        }
    }

    @JsonIgnore
    public void setJsonMaterials(List<String> materials) {
        if (materials != null) {
            this.materials = StringUtils.toJSON(materials);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemVersion() {
        return itemVersion;
    }

    public void setItemVersion(String itemVersion) {
        this.itemVersion = itemVersion;
    }

    @JsonProperty(value = "jsonItemVersion", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonItemVersionReadOnly() {
        if (itemVersion != null && !"".equals(itemVersion)) {
            return StringUtils.decode(itemVersion, new TypeReference<List<String>>() {
            });
        } else {
            return null;
        }
    }

    @JsonIgnore
    public void setJsonItemVersion(List<String> itemVersion) {
        if (itemVersion != null) {
            this.itemVersion = StringUtils.toJSON(itemVersion);
        }
    }

    public ActInstFinishState getFinishState() {
        return finishState;
    }

    public void setFinishState(ActInstFinishState finishState) {
        this.finishState = finishState;
    }
}
