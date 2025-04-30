package com.ose.tasks.entity.material;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.material.NestGateWay;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 材料准备单节点表
 */
@Entity
@Table(name = "mat_f_material_prepare_node")
public class FMaterialPrepareNodeEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "材料准备单节点单号")
    private String mpnCode;

    @Schema(description = "准备单ID")
    private Long mpId;

    @Schema(description = "节点ID")
    private Long projectNodeId;

    @Schema(description = "节点NO")
    private String projectNodeNo;

    @Schema(description = "节点类型")
    private String projectNodeType;

    @Schema(description = "网关")
    private String gateway;

    @Schema(description = "套料状态")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private NestGateWay nestGateWay =NestGateWay.NONE;

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

    public String getMpnCode() {
        return mpnCode;
    }

    public void setMpnCode(String mpnCode) {
        this.mpnCode = mpnCode;
    }

    public Long getMpId() {
        return mpId;
    }

    public void setMpId(Long mpId) {
        this.mpId = mpId;
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

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public NestGateWay getNestGateWay() {
        return nestGateWay;
    }

    public void setNestGateWay(NestGateWay nestGateWay) {
        this.nestGateWay = nestGateWay;
    }
}
