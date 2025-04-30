package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import java.util.Date;

/**
 * 公司级供货商创建DTO
 */
public class MmCompanyVendorCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -7121822781784922443L;

    @Schema(description = "供货商编码")
    public String supplierCode;

    @Schema(description = "供货商名称")
    public String supplierName;

    @Schema(description = "企业分类")
    public String businessType;

    @Schema(description = "供应商名录")
    public String enterpriseList;

    @Schema(description = "等级")
    public String grade;

    @Schema(description = "准入状态")
    public String approveStatus;

    @Schema(description = "合规状态")
    public String complianceStatus ;

    @Schema(description = "联系人")
    public String contact;

    @Schema(description = "职位")
    public String title;

    @Schema(description = "公务联系方式")
    public String officeTelephone;

    @Schema(description = "联系方式")
    public String mobile;

    @Schema(description = "邮箱")
    public String email;

    @Schema(description = "地址")
    public String address;

    @Schema(description = "供应商网站")
    public String companyWebsite;

    @Schema(description = "输入日期")
    public Date inputDate;

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

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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
