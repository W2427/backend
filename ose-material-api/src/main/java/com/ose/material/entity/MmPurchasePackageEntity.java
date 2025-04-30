package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


/**
 * 采购包
 */
@Entity
@Table(name = "mm_purchase_package")
public class MmPurchasePackageEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5297146938088702042L;

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

    @Schema(description = "实体子类型")
    @Column
    private Long entitySubTypeId;

    @Schema(description = "大类名称")
    @Column
    private String groupName;

    @Schema(description = "大类编码")
    @Column
    private String groupCode;

    @Schema(description = "中类名称")
    @Column
    private String middleName;

    @Schema(description = "中类编码")
    @Column
    private String middleCode;

    @Schema(description = "小类名称")
    @Column
    private String partName;

    @Schema(description = "小类编码")
    @Column
    private String partCode;

    @Schema(description = "采购包编号")
    @Column
    private String pwpsNumber;

    @Schema(description = "技术协议（文件）")
    @Column
    private String technicalAgreementFileName;

    @Schema(description = "技术协议（文件ID）")
    @Column
    private String technicalAgreementFileId;

    @Schema(description = "多个附件（文件IDS）")
    @Column
    private String attachmentsFileIds;

    @Schema(description = "多个附件（文件）")
    @Column
    private String attachmentsFileNames;

    @Schema(description = "保密协议（文件ID）")
    @Column
    private String confidentialityAgreementFileId;

    @Schema(description = "保密协议（文件）")
    @Column
    private String confidentialityAgreementFilename;

    @Schema(description = "清单附件（文件ID）")
    @Column
    private String inventoryFileId;

    @Schema(description = "清单附件（文件）")
    @Column
    private String inventoryFilename;

    @Schema(description = "商务标（文件）")
    @Column
    private String commercialBidFileName;

    @Schema(description = "商务标（文件ID）")
    @Column
    private String commercialBidFileId;

    @Schema(description = "锁定状态")
    @Column
    private Boolean locked = false;

    public String getAttachmentsFileNames() {
        return attachmentsFileNames;
    }

    public void setAttachmentsFileNames(String attachmentsFileNames) {
        this.attachmentsFileNames = attachmentsFileNames;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleCode() {
        return middleCode;
    }

    public void setMiddleCode(String middleCode) {
        this.middleCode = middleCode;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public String getPwpsNumber() {
        return pwpsNumber;
    }

    public void setPwpsNumber(String pwpsNumber) {
        this.pwpsNumber = pwpsNumber;
    }

    public String getTechnicalAgreementFileName() {
        return technicalAgreementFileName;
    }

    public void setTechnicalAgreementFileName(String technicalAgreementFileName) {
        this.technicalAgreementFileName = technicalAgreementFileName;
    }

    public String getTechnicalAgreementFileId() {
        return technicalAgreementFileId;
    }

    public void setTechnicalAgreementFileId(String technicalAgreementFileId) {
        this.technicalAgreementFileId = technicalAgreementFileId;
    }

    public String getAttachmentsFileIds() {
        return attachmentsFileIds;
    }

    public void setAttachmentsFileIds(String attachmentsFileIds) {
        this.attachmentsFileIds = attachmentsFileIds;
    }

    public String getConfidentialityAgreementFileId() {
        return confidentialityAgreementFileId;
    }

    public void setConfidentialityAgreementFileId(String confidentialityAgreementFileId) {
        this.confidentialityAgreementFileId = confidentialityAgreementFileId;
    }

    public String getConfidentialityAgreementFilename() {
        return confidentialityAgreementFilename;
    }

    public void setConfidentialityAgreementFilename(String confidentialityAgreementFilename) {
        this.confidentialityAgreementFilename = confidentialityAgreementFilename;
    }

    public String getInventoryFileId() {
        return inventoryFileId;
    }

    public void setInventoryFileId(String inventoryFileId) {
        this.inventoryFileId = inventoryFileId;
    }

    public String getInventoryFilename() {
        return inventoryFilename;
    }

    public void setInventoryFilename(String inventoryFilename) {
        this.inventoryFilename = inventoryFilename;
    }

    public String getCommercialBidFileName() {
        return commercialBidFileName;
    }

    public void setCommercialBidFileName(String commercialBidFileName) {
        this.commercialBidFileName = commercialBidFileName;
    }

    public String getCommercialBidFileId() {
        return commercialBidFileId;
    }

    public void setCommercialBidFileId(String commercialBidFileId) {
        this.commercialBidFileId = commercialBidFileId;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }
}
