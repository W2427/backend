package com.ose.tasks.entity;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;


/**
 * 项目节点数据实体。
 */
@Entity
@Table(
    name = "project_node",
    indexes = {
        @Index(columnList = "projectId,entityType,deleted"),
        @Index(columnList = "projectId,no,deleted"),
        @Index(columnList = "projectId,entityId,deleted"),
        @Index(columnList = "projectId,entitySubType,deleted"),


    }
)
public class ProjectNode extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -2443370936411449782L;

    @Schema(description = "所属公司 ID")
    @Column(nullable = false)
    private Long companyId;

    @Schema(description = "所属组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "取消状态")
    @Column(columnDefinition = "bit default 0")
    private Boolean isCancelled = false;

    @Schema(description = "实体类型")
    @Column(length = 32)
    private String entityType;

    @Schema(description = "实体子类型")
    @Column(length = 64)
    private String entitySubType;

    @Schema(description = "管件实体业务类型")
    @Column(length = 64)
    private String entityBusinessType;

    @Schema(description = "实体 ID")
    @Column
    private Long entityId;

    @Schema(description = "节点编号")
    @Column(nullable = false)
    private String no;

    @Schema(description = "节点编号")
    private String no1;

    @Schema(description = "节点编号")
    private String no2;

    @Schema(description = "节点表示名")
    @Column(nullable = false, length = 64)
    private String displayName = null;

    @Schema(description = "模块类型")
    @Column
    private String moduleType;

    @Schema(description = "批次编号")
    @Column
    private String batchNo;

    @Schema(description = "是否打包")
    @Column(columnDefinition = "BIT(1) DEFAULT 0")
    private boolean pack = false;

    @Schema(description = "备注")
    @Column
    private String remarks;

    @Schema(description = "是否可以删除")
    @Column(columnDefinition = "bit default 1")
    private Boolean isDeletable = true;

    @Schema(description = "图纸页码")
    @Column
    private Integer dwgShtNo;

    @Schema(description = "工作量 物量")
    @Column
    private Double workLoad;


    @Schema(description = "描述")
    @Column
    private String description;

    @Schema(description = "图纸类型")
    @Column
    private String drawingType;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Schema(description = "Discipline 专业")
    @Column(name = "discipline", length = 100, nullable = true)
    private String discipline;

    @Schema(description = "图纸编号")
    @Column
    private String dwgNo;

    @Schema(description = "二维码")
    @Column
    private String qrCode;

    @Schema(description = "是否是固定层级")
    private Boolean isFixedLevel = true;

    public ProjectNode() {
        super();
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getEntityBusinessType() {
        return entityBusinessType;
    }

    public void setEntityBusinessType(String entityBusinessType) {
        this.entityBusinessType = entityBusinessType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @JsonSetter
    public void setNo(String no) {

        this.no = no;

        if (this.displayName == null) {
            this.displayName = this.no;
        }

    }

    public void setNo(String no, String parentNo) {

        setDisplayName(no);
        setNo(no);

    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {

        this.displayName = displayName;

        if (this.no == null) {
            this.no = this.displayName;
        }

    }

    public String getNo() {
        return no;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public boolean isPack() {
        return pack;
    }

    public void setPack(boolean pack) {
        this.pack = pack;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getDwgShtNo() {
        return dwgShtNo;
    }

    public void setDwgShtNo(Integer dwgShtNo) {
        this.dwgShtNo = dwgShtNo;
    }

    public Double getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(Double workLoad) {
        this.workLoad = workLoad;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getDwgNo() {
        return dwgNo;
    }

    public void setDwgNo(String dwgNo) {
        this.dwgNo = dwgNo;
    }

    public Boolean getCancelled() {
        return isCancelled;
    }

    public void setCancelled(Boolean cancelled) {
        isCancelled = cancelled;
    }

    public Boolean getDeletable() {
        return isDeletable;
    }

    public void setDeletable(Boolean deletable) {
        isDeletable = deletable;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getDrawingType() {
        return drawingType;
    }

    public void setDrawingType(String drawingType) {
        this.drawingType = drawingType;
    }

    public String getNo1() {
        return no1;
    }

    public void setNo1(String no1) {
        this.no1 = no1;
    }

    public String getNo2() {
        return no2;
    }

    public void setNo2(String no2) {
        this.no2 = no2;
    }

    public Boolean getFixedLevel() {
        return isFixedLevel;
    }

    public void setFixedLevel(Boolean fixedLevel) {
        isFixedLevel = fixedLevel;
    }
}
