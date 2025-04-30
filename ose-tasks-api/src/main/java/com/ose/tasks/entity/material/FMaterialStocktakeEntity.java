package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.material.FItemStatusType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 材料盘点实体类。
 */
@Entity
@Table(name = "mat_f_material_stocktake")
public class FMaterialStocktakeEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "fmst_code")
    private String fmstCode;

    @Column(name = "seq_number")
    private Integer seqNumber;

    // issue, recv
    @Column(name = "item_status")
    @Enumerated(EnumType.STRING)
    private FItemStatusType itemStatus;

    @Column(name = "reln_id")
    private Long relnId;

    @Column(name = "short_desc")
    private String shortDesc;

    @Column(name = "is_saved")
    private boolean isSaved;

    // 流程id
    @Column(name = "act_inst_id")
    private Long actInstId;

    @Transient
    private String taskId;

    @Transient
    private String taskDefKey;

    @Column(name = "stocktake_result")
    private String stocktakeResult;

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

    public String getFmstCode() {
        return fmstCode;
    }

    public void setFmstCode(String fmstCode) {
        this.fmstCode = fmstCode;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public Long getRelnId() {
        return relnId;
    }

    public void setRelnId(Long relnId) {
        this.relnId = relnId;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public FItemStatusType getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(FItemStatusType itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getStocktakeResult() {
        return stocktakeResult;
    }

    public void setStocktakeResult(String stocktakeResult) {
        this.stocktakeResult = stocktakeResult;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    /**
     * 取得创建者引用信息。
     *
     * @return 放行单引用信息
     */
    @Schema(description = "放行单信息")
    @JsonProperty(value = "relnId", access = READ_ONLY)
    public ReferenceData getRelnIdRef() {
        return this.relnId == null
            ? null
            : new ReferenceData(this.relnId);
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }
}
