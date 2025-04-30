package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 结构套料零件表
 */
@Entity
@Table(name = "mat_f_material_structure_nest_drawing")
public class FMaterialStructureNestDrawing extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 1428924904169325808L;

    @Schema(description = "组织id->使用中")
    private Long orgId;

    @Schema(description = "项目id->使用中")
    private Long projectId;

    @Schema(description = "套料方案id->使用中")
    private Long fMaterialStructureNestId;

    @Schema(description = "WP1编号")
    private String wp01No;

    @Schema(description = "WP1 ID")
    private Long wp01Id;

    @Schema(description = "WP2编号")
    private String wp02No;

    @Schema(description = "WP2 ID")
    private Long wp02Id;

    @Schema(description = "WP3编号")
    private String wp03No;

    @Schema(description = "WP3 ID")
    private Long wp03Id;

    @Schema(description = "WP4编号")
    private String wp04No;

    @Schema(description = "WP4 ID")
    private Long wp04Id;

    @Schema(description = "WP5编号->使用中")
    private String wp05No;

    @Schema(description = "WP5 ID->使用中")
    private Long wp05Id;

    @Schema(description = "托盘号")
    private String trayNo;

    @Schema(description = "排版名->使用中")
    private String nestingProgramNo;

    @Schema(description = "排版名ID->使用中")
    private Long nestingProgramId;

    @Schema(description = "材料二维码->使用中")
    private String materialQrCode;

    @Schema(description = "版本->使用中")
    private String rev;

    @Schema(description = "件号->使用中")
    private String pieceTagNo;

    @Schema(description = "工作人->使用中")
    private String workingPeople;

    @Schema(description = "下料时间->使用中")
    private String cuttingDate;

    @Schema(description = "材料炉批号")
    private String materialHeatNo;

    @Schema(description = "完成")
    private Boolean finish;

    @Schema(description = "描述")
    private String description;

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

    public String getWp01No() {
        return wp01No;
    }

    public void setWp01No(String wp01No) {
        this.wp01No = wp01No;
    }

    public String getWp02No() {
        return wp02No;
    }

    public void setWp02No(String wp02No) {
        this.wp02No = wp02No;
    }

    public String getWp03No() {
        return wp03No;
    }

    public void setWp03No(String wp03No) {
        this.wp03No = wp03No;
    }

    public String getWp04No() {
        return wp04No;
    }

    public void setWp04No(String wp04No) {
        this.wp04No = wp04No;
    }

    public String getWp05No() {
        return wp05No;
    }

    public void setWp05No(String wp05No) {
        this.wp05No = wp05No;
    }

    public String getMaterialQrCode() {
        return materialQrCode;
    }

    public void setMaterialQrCode(String materialQrCode) {
        this.materialQrCode = materialQrCode;
    }

    public String getMaterialHeatNo() {
        return materialHeatNo;
    }

    public void setMaterialHeatNo(String materialHeatNo) {
        this.materialHeatNo = materialHeatNo;
    }

    public String getTrayNo() {
        return trayNo;
    }

    public void setTrayNo(String trayNo) {
        this.trayNo = trayNo;
    }

    public String getNestingProgramNo() {
        return nestingProgramNo;
    }

    public void setNestingProgramNo(String nestingProgramNo) {
        this.nestingProgramNo = nestingProgramNo;
    }

    public Long getfMaterialStructureNestId() {
        return fMaterialStructureNestId;
    }

    public void setfMaterialStructureNestId(Long fMaterialStructureNestId) {
        this.fMaterialStructureNestId = fMaterialStructureNestId;
    }

    public Long getWp01Id() {
        return wp01Id;
    }

    public void setWp01Id(Long wp01Id) {
        this.wp01Id = wp01Id;
    }

    public Long getWp02Id() {
        return wp02Id;
    }

    public void setWp02Id(Long wp02Id) {
        this.wp02Id = wp02Id;
    }

    public Long getWp03Id() {
        return wp03Id;
    }

    public void setWp03Id(Long wp03Id) {
        this.wp03Id = wp03Id;
    }

    public Long getWp04Id() {
        return wp04Id;
    }

    public void setWp04Id(Long wp04Id) {
        this.wp04Id = wp04Id;
    }

    public Long getWp05Id() {
        return wp05Id;
    }

    public void setWp05Id(Long wp05Id) {
        this.wp05Id = wp05Id;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getPieceTagNo() {
        return pieceTagNo;
    }

    public void setPieceTagNo(String pieceTagNo) {
        this.pieceTagNo = pieceTagNo;
    }

    public String getWorkingPeople() {
        return workingPeople;
    }

    public void setWorkingPeople(String workingPeople) {
        this.workingPeople = workingPeople;
    }

    public String getCuttingDate() {
        return cuttingDate;
    }

    public void setCuttingDate(String cuttingDate) {
        this.cuttingDate = cuttingDate;
    }

    public Long getNestingProgramId() {
        return nestingProgramId;
    }

    public void setNestingProgramId(Long nestingProgramId) {
        this.nestingProgramId = nestingProgramId;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
