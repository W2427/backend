package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.vo.SupplierLevel;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 供货商
 */
@Entity
@Table(name = "mm_vendor")
public class MmVendorEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 2203676961070986447L;

    @Schema(description = "组织ID")
    @Column
    public Long orgId;

    @Schema(description = "项目ID")
    @Column
    public Long projectId;

    @Schema(description = "公司ID")
    @Column
    public Long companyId;

    @Schema(description = "序号")
    @Column
    private Integer seqNumber;

    @Schema(description = "供货商编码")
    @Column
    public String supplierCode;

    @Schema(description = "供货商名称")
    @Column
    public String supplierName;

    @Schema(description = "供应商名录")
    @Column
    public String enterpriseList;

    @Schema(description = "等级")
    @Column
    public String grade;

    @Schema(description = "供货商级别")
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    public SupplierLevel supplierLevel;

    @Schema(description = "准入状态")
    @Column
    public String approveStatus;

    @Schema(description = "合格状态")
    @Column
    public String complianceStatus ;

    @Schema(description = "公司级供货商ID")
    @Column
    public Long companyVendorId;

    @Schema(description = "联系人")
    @Column
    public String contact;

    @Schema(description = "职位")
    @Column
    public String title;

    @Schema(description = "公务联系方式")
    @Column
    public String officeTelephone;

    @Schema(description = "联系方式")
    @Column
    public String mobile;

    @Schema(description = "邮箱")
    @Column
    public String email;

    @Schema(description = "地址")
    @Column
    public String address;

    @Schema(description = "供应商网站")
    @Column
    public String companyWebsite;

    @Schema(description = "输入日期")
    @Column
    public Date inputDate;

    @Schema(description = "企业类型")
    @Column
    public String businessType;

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

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getEnterpriseList() {
        return enterpriseList;
    }

    public void setEnterpriseList(String enterpriseList) {
        this.enterpriseList = enterpriseList;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getComplianceStatus() {
        return complianceStatus;
    }

    public void setComplianceStatus(String complianceStatus) {
        this.complianceStatus = complianceStatus;
    }

    public SupplierLevel getSupplierLevel() {
        return supplierLevel;
    }

    public void setSupplierLevel(SupplierLevel supplierLevel) {
        this.supplierLevel = supplierLevel;
    }

    public Long getCompanyVendorId() {
        return companyVendorId;
    }

    public void setCompanyVendorId(Long companyVendorId) {
        this.companyVendorId = companyVendorId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOfficeTelephone() {
        return officeTelephone;
    }

    public void setOfficeTelephone(String officeTelephone) {
        this.officeTelephone = officeTelephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }
}
