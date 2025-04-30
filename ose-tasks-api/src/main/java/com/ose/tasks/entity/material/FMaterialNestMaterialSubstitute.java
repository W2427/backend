package com.ose.tasks.entity.material;

import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

@Entity
@Table(name = "mat_f_material_nest_material_substitute")
public class FMaterialNestMaterialSubstitute extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "套料清单id")
    private Long fmnId;

    @Schema(description = "被代材料id")
    private Long parentId;

    private String tagNumber;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Column(length = 500)
    private String shortDesc;

    @Schema(description = "是否是原始材料")
    private Boolean original;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public Long getFmnId() {
        return fmnId;
    }

    public void setFmnId(Long fmnId) {
        this.fmnId = fmnId;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public Boolean getOriginal() {
        return original;
    }

    public void setOriginal(Boolean original) {
        this.original = original;
    }

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }

    public String getNpsText() {
        return npsText;
    }

    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }
}
