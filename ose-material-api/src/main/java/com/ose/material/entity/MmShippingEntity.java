package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.vo.MaterialReceiveType;
import com.ose.vo.ShippingStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 发货单。
 */
@Entity
@Table(name = "mm_shipping",
    indexes = {
        @Index(columnList = "orgId,projectId,deleted")
    })
public class MmShippingEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 3892732218139583215L;

    @Schema(description = "组织ID")
    @Column
    public Long orgId;

    @Schema(description = "项目ID")
    @Column
    public Long projectId;

    @Schema(description = "公司ID")
    @Column
    public Long companyId;

    @Schema(description = "发货单编号")
    @Column
    private String no;

    @Schema(description = "流水号")
    private Integer seqNumber;

    @Schema(description = "发货单名称")
    @Column
    private String name;

    @Schema(description = "合同编号")
    @Column
    private String contractNo;

    @Schema(description = "备注")
    @Column
    private String remarks;

    @Schema(description = "发货单状态")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;

    @Schema(description = "文件ID")
    @Column
    private Long fileId;

    @Schema(description = "文件名")
    @Column
    private String fileName;

    @Schema(description = "文件path")
    @Column
    private String filePath;

    @Schema(description = "发货单类型")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private MaterialReceiveType type;

    @Schema(description ="请购单ID")
    private Long mmPurchasePackageId;

    @Schema(description ="请购单编号")
    private String mmPurchasePackageNo;

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

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ShippingStatus getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(ShippingStatus shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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
}
