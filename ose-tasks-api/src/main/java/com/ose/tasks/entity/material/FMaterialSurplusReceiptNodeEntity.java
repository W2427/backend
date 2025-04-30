package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 余料领料单实体类。
 */
@Entity
@Table(name = "mat_f_material_surplus_receipt_node")
public class FMaterialSurplusReceiptNodeEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -4269136826657473201L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "余料领料单号")
    private String fMaterialSurplusReceiptNo;

    @Schema(description = "余料领料单ID")
    private Long fMaterialSurplusReceiptId;

    @Schema(description = "节点ID")
    private Long projectNodeId;

    @Schema(description = "节点NO")
    private String projectNodeNo;

    @Schema(description = "节点类型")
    private String projectNodeType;

    @Schema(description = "网关")
    private String gateway;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

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

    public String getfMaterialSurplusReceiptNo() {
        return fMaterialSurplusReceiptNo;
    }

    public void setfMaterialSurplusReceiptNo(String fMaterialSurplusReceiptNo) {
        this.fMaterialSurplusReceiptNo = fMaterialSurplusReceiptNo;
    }

    public Long getfMaterialSurplusReceiptId() {
        return fMaterialSurplusReceiptId;
    }

    public void setfMaterialSurplusReceiptId(Long fMaterialSurplusReceiptId) {
        this.fMaterialSurplusReceiptId = fMaterialSurplusReceiptId;
    }

    public Long getProjectNodeId() {
        return projectNodeId;
    }

    public void setProjectNodeId(Long projectNodeId) {
        this.projectNodeId = projectNodeId;
    }

    public String getProjectNodeNo() {
        return projectNodeNo;
    }

    public void setProjectNodeNo(String projectNodeNo) {
        this.projectNodeNo = projectNodeNo;
    }

    public String getProjectNodeType() {
        return projectNodeType;
    }

    public void setProjectNodeType(String projectNodeType) {
        this.projectNodeType = projectNodeType;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
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

    public LengthUnit getNpsUnit() {
        return npsUnit;
    }

    public void setNpsUnit(LengthUnit npsUnit) {
        this.npsUnit = npsUnit;
    }
}
