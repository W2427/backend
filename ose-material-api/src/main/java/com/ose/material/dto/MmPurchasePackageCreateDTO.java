package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 采购包创建DTO
 */
public class MmPurchasePackageCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 6731960322014456916L;

    @Schema(description = "实体类型")
    private Long entitySubTypeId;

    @Schema(description = "大类名称")
    private String groupName;

    @Schema(description = "大类编码")
    private String groupCode;

    @Schema(description = "中类名称")
    private String middleName;

    @Schema(description = "中类编码")
    private String middleCode;

    @Schema(description = "小类名称")
    private String partName;

    @Schema(description = "小类编码")
    private String partCode;

    @Schema(description = "采购包编号")
    private String pwpsNumber;

    @Schema(description = "技术协议（文件）")
    private String technicalAgreementFileName;

    @Schema(description = "技术协议（文件ID）")
    private String technicalAgreementFileId;

    @Schema(description = "多个附件（文件IDS）")
    private String attachmentsFileIds;

    @Schema(description = "多个附件（文件）")
    private String attachmentsFileNames;

    @Schema(description = "保密协议（文件ID）")
    private String confidentialityAgreementFileId;

    @Schema(description = "保密协议（文件）")
    private String confidentialityAgreementFilename;

    @Schema(description = "清单附件（文件ID）")
    private String inventoryFileId;

    @Schema(description = "清单附件（文件）")
    private String inventoryFilename;

    @Schema(description = "商务标（文件）")
    private String commercialBidFileName;

    @Schema(description = "商务标（文件ID）")
    private String commercialBidFileId;

    @Schema(description = "锁定状态")
    private Boolean locked;

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public String getAttachmentsFileNames() {
        return attachmentsFileNames;
    }

    public void setAttachmentsFileNames(String attachmentsFileNames) {
        this.attachmentsFileNames = attachmentsFileNames;
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
