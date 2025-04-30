package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 结构套料排版表
 */
@Entity
@Table(name = "mat_f_material_structure_nest_program")
public class FMaterialStructureNestProgram extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -6720629481932500891L;

    @Schema(description = "组织id ->使用中")
    private Long orgId;

    @Schema(description = "项目id->使用中")
    private Long projectId;

    @Schema(description = "套料方案id->使用中")
    private Long fMaterialStructureNestId;

    @Schema(description = "排版名称->使用中")
    private String no;

    @Schema(description = "厚度")
    private Integer thickness;

    @Schema(description = "宽度")
    private Integer width;

    @Schema(description = "长度")
    private Integer length;

    @Schema(description = "材料")
    private String material;

    @Schema(description = "利用率")
    private Double utilizationRatio;

    @Schema(description = "产生余料名->使用中")
    private String remainderCreated;

    @Schema(description = "使用余料名->使用中")
    private String remainderUsed;

    @Schema(description = "材料二维码")
    private String materialQrCode;

    @Schema(description = "件号->使用中")
    private String pieceTagNo;

    @Schema(description = "材料炉批号")
    private String materialHeatNo;

    @Schema(description = "版本->使用中")
    private String rev;

    @Schema(description = "完成->使用中")
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

    public Long getfMaterialStructureNestId() {
        return fMaterialStructureNestId;
    }

    public void setfMaterialStructureNestId(Long fMaterialStructureNestId) {
        this.fMaterialStructureNestId = fMaterialStructureNestId;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
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

    public String getRemainderCreated() {
        return remainderCreated;
    }

    public void setRemainderCreated(String remainderCreated) {
        this.remainderCreated = remainderCreated;
    }

    public String getRemainderUsed() {
        return remainderUsed;
    }

    public void setRemainderUsed(String remainderUsed) {
        this.remainderUsed = remainderUsed;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getThickness() {
        return thickness;
    }

    public void setThickness(Integer thickness) {
        this.thickness = thickness;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Double getUtilizationRatio() {
        return utilizationRatio;
    }

    public void setUtilizationRatio(Double utilizationRatio) {
        this.utilizationRatio = utilizationRatio;
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
