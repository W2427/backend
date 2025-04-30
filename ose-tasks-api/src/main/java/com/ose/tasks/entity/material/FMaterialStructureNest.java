package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

import com.ose.tasks.vo.material.FMaterialStructureNestType;

/**
 * 结构套料方案表
 */
@Entity
@Table(name = "mat_f_material_structure_nest")
public class FMaterialStructureNest extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 5400103065337747536L;

    @Schema(description = "组织id")
    private Long orgId;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "结构套料方案名称")
    private String name;

    @Schema(description = "结构套料方案编号")
    private String no;

    @Schema(description = "备注")
    private String memo;

    @Schema(description = "方案类型")
    @Enumerated(EnumType.STRING)
    private FMaterialStructureNestType type;

    @Schema(description = "工序id")
    private Long processId;

    @Schema(description = "工序阶段id")
    private Long stageId;

    @Schema(description = "实体类型id")
    private Long entitySubTypeId;

    @Schema(description = "实体类型大类id")
    private Long entityTypeId;

    @Schema(description = "流程实例id")
    private Long actInstId;

    @Schema(description = "修改状态")
    private Boolean modifyState;

    @Schema(description = "流程启动状态")
    private Boolean activityStartStatus;

    @Schema(description = "保存流程状态")
    private Boolean saveActivityStatus;

    @Schema(description = "保存流程状态")
    private Boolean finished;

    @Schema(description = "流水号")
    private Integer seqNumber;

    @Schema(description = "领料单号")
    private String materialRequisitionsCode;

    @Schema(description = "领料单ID,spmFahId")
    private String materialRequisitionsId;

    @Schema(description = "公司ID")
    private String companyId;

    @Schema(description = "公司业务代码")
    private String companyCode;

    @Schema(description = "材料出库id")
    private Long fmirId;

    @Schema(description = "材料出库代码")
    private String fmirCode;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public FMaterialStructureNestType getType() {
        return type;
    }

    public void setType(FMaterialStructureNestType type) {
        this.type = type;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public Long getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(Long entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public Boolean getModifyState() {
        return modifyState;
    }

    public void setModifyState(Boolean modifyState) {
        this.modifyState = modifyState;
    }

    public Boolean getActivityStartStatus() {
        return activityStartStatus;
    }

    public void setActivityStartStatus(Boolean activityStartStatus) {
        this.activityStartStatus = activityStartStatus;
    }

    public Boolean getSaveActivityStatus() {
        return saveActivityStatus;
    }

    public void setSaveActivityStatus(Boolean saveActivityStatus) {
        this.saveActivityStatus = saveActivityStatus;
    }

    public String getMaterialRequisitionsCode() {
        return materialRequisitionsCode;
    }

    public void setMaterialRequisitionsCode(String materialRequisitionsCode) {
        this.materialRequisitionsCode = materialRequisitionsCode;
    }

    public String getMaterialRequisitionsId() {
        return materialRequisitionsId;
    }

    public void setMaterialRequisitionsId(String materialRequisitionsId) {
        this.materialRequisitionsId = materialRequisitionsId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Long getFmirId() {
        return fmirId;
    }

    public void setFmirId(Long fmirId) {
        this.fmirId = fmirId;
    }

    public String getFmirCode() {
        return fmirCode;
    }

    public void setFmirCode(String fmirCode) {
        this.fmirCode = fmirCode;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
