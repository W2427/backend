package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.vo.EntityStatus;
import com.ose.vo.MaterialReceiveType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

/**
 * 材料入库单。
 */
@Entity
@Table(name = "mm_release_receive",
    indexes = {
        @Index(columnList = "orgId,projectId,deleted"),
        @Index(columnList = "orgId,projectId,id,status")
    })
public class MmReleaseReceiveEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -4331731646841865653L;

    @Schema(description = "组织ID")
    @Column
    public Long orgId;

    @Schema(description = "项目ID")
    @Column
    public Long projectId;

    @Schema(description = "公司ID")
    @Column
    public Long companyId;

    @Schema(description = "入库单编号")
    @Column
    private String no;

    @Schema(description = "流水号")
    @Column
    private Integer seqNumber;

    @Schema(description = "入库单名称")
    @Column
    private String name;

    @Schema(description = "备注")
    @Column
    private String remarks;

    @Schema(description = "入库状态")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private EntityStatus runningStatus;

    @Schema(description = "入库单类型")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private MaterialReceiveType type;

    @Schema(description = "采购包ID")
    @Column
    private Long mmPurchasePackageId;

    @Schema(description = "采购包编号")
    @Column
    private String mmPurchasePackageNo;

    @Schema(description = "发货单ID")
    @Column
    private Long mmShippingId;

    @Schema(description = "发货单编号")
    @Column
    private String mmShippingNumber;

    @Schema(description = "入库锁定状态")
    @Column
    private Boolean locked;

    @Schema(description = "外检中")
    @Column
    public Boolean inExternalQuality;

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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public EntityStatus getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(EntityStatus runningStatus) {
        this.runningStatus = runningStatus;
    }

    public MaterialReceiveType getType() {
        return type;
    }

    public void setType(MaterialReceiveType type) {
        this.type = type;
    }

    public Long getMmPurchasePackageId() {
        return mmPurchasePackageId;
    }

    public void setMmPurchasePackageId(Long mmPurchasePackageId) {
        this.mmPurchasePackageId = mmPurchasePackageId;
    }

    public String getMmPurchasePackageNo() {
        return mmPurchasePackageNo;
    }

    public void setMmPurchasePackageNo(String mmPurchasePackageNo) {
        this.mmPurchasePackageNo = mmPurchasePackageNo;
    }

    public Long getMmShippingId() {
        return mmShippingId;
    }

    public void setMmShippingId(Long mmShippingId) {
        this.mmShippingId = mmShippingId;
    }

    public String getMmShippingNumber() {
        return mmShippingNumber;
    }

    public void setMmShippingNumber(String mmShippingNumber) {
        this.mmShippingNumber = mmShippingNumber;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getInExternalQuality() {
        return inExternalQuality;
    }

    public void setInExternalQuality(Boolean inExternalQuality) {
        this.inExternalQuality = inExternalQuality;
    }
}
