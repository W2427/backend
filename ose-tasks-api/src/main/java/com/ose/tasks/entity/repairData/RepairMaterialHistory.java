package com.ose.tasks.entity.repairData;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "repair_material_history")
public class RepairMaterialHistory extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 8506224236667698934L;

    @Schema(description = "组织id")
    private Long orgId;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "放行单id")
    private Long releaseNoteId;

    @Schema(description = "放行单编号")
    private String releaseNoteNo;

    @Schema(description = "放行单详情id")
    private Long releaseNoteDetailId;

    @Schema(description = "放行单详情明细id")
    private Long releaseNoteDetailItemId;

    @Schema(description = "旧炉号")
    private String oldHeatNo;

    @Schema(description = "旧批号")
    private String oldBatchNo;

    @Schema(description = "新炉号")
    private String newHeatNo;

    @Schema(description = "新批号")
    private String newBatchNo;

    @Schema(description = "旧炉批号id")
    private Long oldHeatNoId;

    @Schema(description = "新炉批号id")
    private Long newHeatNoId;

    @Schema(description = "新NPS")
    private String newNps;

    @Schema(description = "旧NPS")
    private String oldNps;

    @Schema(description = "备注")
    @Column(columnDefinition = "TEXT", length = 65535)
    private String remark;

    @Schema(description = "物资盘点单IDs")
    @Column(columnDefinition = "TEXT", length = 65535)
    private String fMaterialStocktakeItemEntityIds;

    @Schema(description = "材料主表IDs")
    @Column(columnDefinition = "TEXT", length = 65535)
    private String fItemEntityIds;

    @Schema(description = "结构下料程序里的实体IDs")
    @Column(columnDefinition = "TEXT", length = 65535)
    private String bpmStructureCuttingTaskProgramEntityIds;

    @Schema(description = "余料主表实体IDs")
    @Column(columnDefinition = "TEXT", length = 65535)
    private String fCompanySurplusMaterialsEntityIds;

    @Schema(description = "退库清单的材料表IDs")
    @Column(columnDefinition = "TEXT", length = 65535)
    private String fMaterialReturnReceiptItemEntityIds;

    @Schema(description = "结构实体二维码IDs")
    @Column(columnDefinition = "TEXT", length = 65535)
    private String structureEntityQrCodeIds;

    @Schema(description = "配送放行实体表IDs")
    @Column(columnDefinition = "TEXT", length = 65535)
    private String bpmDeliveryEntityIds;

    @Schema(description = "管系实体二维码IDs")
    @Column(columnDefinition = "TEXT", length = 65535)
    private String entityQrCodeEntityIds;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOldHeatNo() {
        return oldHeatNo;
    }

    public void setOldHeatNo(String oldHeatNo) {
        this.oldHeatNo = oldHeatNo;
    }

    public String getOldBatchNo() {
        return oldBatchNo;
    }

    public void setOldBatchNo(String oldBatchNo) {
        this.oldBatchNo = oldBatchNo;
    }

    public String getOldNps() {
        return oldNps;
    }

    public void setOldNps(String oldNps) {
        this.oldNps = oldNps;
    }

    public String getNewHeatNo() {
        return newHeatNo;
    }

    public void setNewHeatNo(String newHeatNo) {
        this.newHeatNo = newHeatNo;
    }

    public String getNewBatchNo() {
        return newBatchNo;
    }

    public void setNewBatchNo(String newBatchNo) {
        this.newBatchNo = newBatchNo;
    }

    public String getNewNps() {
        return newNps;
    }

    public void setNewNps(String newNps) {
        this.newNps = newNps;
    }

    public Long getReleaseNoteId() {
        return releaseNoteId;
    }

    public void setReleaseNoteId(Long releaseNoteId) {
        this.releaseNoteId = releaseNoteId;
    }

    public String getReleaseNoteNo() {
        return releaseNoteNo;
    }

    public void setReleaseNoteNo(String releaseNoteNo) {
        this.releaseNoteNo = releaseNoteNo;
    }

    public Long getReleaseNoteDetailId() {
        return releaseNoteDetailId;
    }

    public void setReleaseNoteDetailId(Long releaseNoteDetailId) {
        this.releaseNoteDetailId = releaseNoteDetailId;
    }

    public Long getReleaseNoteDetailItemId() {
        return releaseNoteDetailItemId;
    }

    public void setReleaseNoteDetailItemId(Long releaseNoteDetailItemId) {
        this.releaseNoteDetailItemId = releaseNoteDetailItemId;
    }

    public Long getOldHeatNoId() {
        return oldHeatNoId;
    }

    public void setOldHeatNoId(Long oldHeatNoId) {
        this.oldHeatNoId = oldHeatNoId;
    }

    public Long getNewHeatNoId() {
        return newHeatNoId;
    }

    public void setNewHeatNoId(Long newHeatNoId) {
        this.newHeatNoId = newHeatNoId;
    }

    public String getfMaterialStocktakeItemEntityIds() {
        return fMaterialStocktakeItemEntityIds;
    }

    public void setfMaterialStocktakeItemEntityIds(String fMaterialStocktakeItemEntityIds) {
        this.fMaterialStocktakeItemEntityIds = fMaterialStocktakeItemEntityIds;
    }

    public String getfItemEntityIds() {
        return fItemEntityIds;
    }

    public void setfItemEntityIds(String fItemEntityIds) {
        this.fItemEntityIds = fItemEntityIds;
    }

    public String getBpmStructureCuttingTaskProgramEntityIds() {
        return bpmStructureCuttingTaskProgramEntityIds;
    }

    public void setBpmStructureCuttingTaskProgramEntityIds(String bpmStructureCuttingTaskProgramEntityIds) {
        this.bpmStructureCuttingTaskProgramEntityIds = bpmStructureCuttingTaskProgramEntityIds;
    }

    public String getfCompanySurplusMaterialsEntityIds() {
        return fCompanySurplusMaterialsEntityIds;
    }

    public void setfCompanySurplusMaterialsEntityIds(String fCompanySurplusMaterialsEntityIds) {
        this.fCompanySurplusMaterialsEntityIds = fCompanySurplusMaterialsEntityIds;
    }

    public String getfMaterialReturnReceiptItemEntityIds() {
        return fMaterialReturnReceiptItemEntityIds;
    }

    public void setfMaterialReturnReceiptItemEntityIds(String fMaterialReturnReceiptItemEntityIds) {
        this.fMaterialReturnReceiptItemEntityIds = fMaterialReturnReceiptItemEntityIds;
    }

    public String getStructureEntityQrCodeIds() {
        return structureEntityQrCodeIds;
    }

    public void setStructureEntityQrCodeIds(String structureEntityQrCodeIds) {
        this.structureEntityQrCodeIds = structureEntityQrCodeIds;
    }

    public String getBpmDeliveryEntityIds() {
        return bpmDeliveryEntityIds;
    }

    public void setBpmDeliveryEntityIds(String bpmDeliveryEntityIds) {
        this.bpmDeliveryEntityIds = bpmDeliveryEntityIds;
    }

    public String getEntityQrCodeEntityIds() {
        return entityQrCodeEntityIds;
    }

    public void setEntityQrCodeEntityIds(String entityQrCodeEntityIds) {
        this.entityQrCodeEntityIds = entityQrCodeEntityIds;
    }
}
