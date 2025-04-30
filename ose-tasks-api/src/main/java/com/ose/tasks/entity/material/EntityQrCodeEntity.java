package com.ose.tasks.entity.material;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 材料二维码管理表。
 */
@Entity
@Table(name = "entity_qr_code",
    indexes = {
        @Index(columnList = "org_id, project_id, qr_code"),
        @Index(columnList = "org_id, project_id, bpmCuttingId, entity_id"),
        @Index(columnList = "org_id, project_id, entity_id"),
        @Index(columnList = "entity_id")

    })
public class EntityQrCodeEntity extends BaseEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "project_id")
    private Long projectId;

    // 专业ID
    @Column(name = "entity_id")
    private Long entityId;

    @Schema(description = "实体类型")
    @Column(length = 32)

    private String entityType;

    @Column(name = "entity_no")
    private String entityNo;

    @Column(name = "qr_code")
    private String qrCode;

    @Column(name = "f_item_id")
    private Long fItemId;

    // 材料编码ID
    @Column(name = "tag_number_id")
    private int tagNumberId;

    // 炉批号
    @Column(name = "heat_no")
    private String heatNo;

    // 材料类型(一物一码，一类一码)
    @Column(name = "tag_number")
    private String tagNumber;

    private String ident;

    // 材料描述
    @Column(name = "short_desc",length = 500)
    private String shortDesc;

    @Column(name = "act_inst_id", nullable = true, length = 19)
    @JsonIgnore
    private Long actInstId;

    // 描述
    @Column(name = "material_qr_code")
    private String materialQrCode;

    // 创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    // 更新时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;

    @Column(nullable = false)
    @JsonIgnore
    private Long operatorId;

    @Column(nullable = false, length = 255)
    private String operatorName;

    @Column(nullable = true)
    private Boolean printFlg;

    // 材料规格
    @Column(name = "spec")
    private String spec;

    // 材料单位
    @Column(name = "unit_code")
    private String unitCode;

    // 库存
    @Column(name = "qty", precision = 19, scale = 3)
    private BigDecimal qty;

    //下料单id
    private Long bpmCuttingId;

    //下料单编号
    private String bpmCuttingNo;

    @Column
    private Boolean manuallyCreated = false;

    @Transient
    private String lengthText;

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

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getfItemId() {
        return fItemId;
    }

    public void setfItemId(Long fItemId) {
        this.fItemId = fItemId;
    }

    public int getTagNumberId() {
        return tagNumberId;
    }

    public void setTagNumberId(int tagNumberId) {
        this.tagNumberId = tagNumberId;
    }

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getMaterialQrCode() {
        return materialQrCode;
    }

    public void setMaterialQrCode(String materialQrCode) {
        this.materialQrCode = materialQrCode;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public Boolean getPrintFlg() {
        return printFlg;
    }

    public void setPrintFlg(Boolean printFlg) {
        if (printFlg == null) {
            printFlg = false;
        }
        this.printFlg = printFlg;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public Long getBpmCuttingId() {
        return bpmCuttingId;
    }

    public void setBpmCuttingId(Long bpmCuttingId) {
        this.bpmCuttingId = bpmCuttingId;
    }

    public String getBpmCuttingNo() {
        return bpmCuttingNo;
    }

    public void setBpmCuttingNo(String bpmCuttingNo) {
        this.bpmCuttingNo = bpmCuttingNo;
    }

    public String getLengthText() {
        return lengthText;
    }

    public void setLengthText(String lengthText) {
        this.lengthText = lengthText;
    }

    public Boolean getManuallyCreated() {
        return manuallyCreated;
    }

    public void setManuallyCreated(Boolean manuallyCreated) {
        this.manuallyCreated = manuallyCreated;
    }
}
