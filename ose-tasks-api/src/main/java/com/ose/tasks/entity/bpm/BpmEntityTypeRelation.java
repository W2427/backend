package com.ose.tasks.entity.bpm;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "bpm_entity_type_relation",
indexes = {
    @Index(columnList = "orgId,projectId,wbsEntityType,relatedWbsEntityType,status")
})
public class BpmEntityTypeRelation extends BaseVersionedBizEntity {


    private static final long serialVersionUID = -849597146074882529L;
    @Schema(description = "组织id")
    @Column
    private Long orgId;

    @Schema(description = "项目id")
    @Column
    private Long projectId;

    @Schema(description = "实体类型")
    @Column
    private String wbsEntityType;

    @Schema(description = "关联的实体类型")
    @Column
    private String relatedWbsEntityType;

    @Schema(description = "用于实现关系的代理")
    @Column
    private String relationDelegate;

    @Schema(description = "关系类型")
    @Column
    private String relationType;

    @Schema(description = "备注")
    @Column
    private String memo;

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

    public String getEntityType() {
        return wbsEntityType;
    }

    public void setWbsEntityType(String wbsEntityType) {
        this.wbsEntityType = wbsEntityType;
    }

    public String getRelatedWbsEntityType() {
        return relatedWbsEntityType;
    }

    public void setRelatedWbsEntityType(String relatedWbsEntityType) {
        this.relatedWbsEntityType = relatedWbsEntityType;
    }

    public String getRelationDelegate() {
        return relationDelegate;
    }

    public void setRelationDelegate(String relationDelegate) {
        this.relationDelegate = relationDelegate;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
