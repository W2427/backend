package com.ose.tasks.entity.bpm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.vo.material.NestGateWay;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 下料单实体
 */
@Entity
@Table(name = "bpm_cutting_entity",
    indexes = {
        @Index(columnList = "pipePieceEntityId, projectId"),
        @Index(columnList = "projectId,pipePieceEntityId")
    })
public class BpmCuttingEntity extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -8635813266138087045L;

    //组织id
    private Long orgId;

    //项目id
    private Long projectId;

    //下料单id
    private Long cuttingId;

    //实体编号
    private Long isoEntityId;

    //实体编号
    private String isoEntityNo;

    //实体编号
    private Long spoolEntityId;

    //实体编号
    private String spoolEntityNo;

    //实体id
    @JsonIgnore
    private Long pipePieceEntityId;

    //实体编号
    private String pipePieceEntityNo;

    //材料描述
    private String materialCode;

    @Schema(description = "材料编码")
    private String tagNumber;

    //材质
    private String nps;

    //材料准备单id
    private Long matPrepareId;

    //材料准备单编号
    private String matPrepareCode;

    @Schema(description = "余料领料单号")
    private String matSurplusReceiptsNo;

    @Schema(description = "余料领料单id")
    private Long matSurplusReceiptsId;

    @Schema(description = "套料状态")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private NestGateWay nestGateWay = NestGateWay.NONE;

    //材料出库单id
    private Long matIssueId;

    //材料出库单编号
    private String matIssueCode;

    //实体模块名称
    private String entityModuleName;

    //实体模块ID
    private Long entityModuleProjectNodeId;

    private String taskPackageName;

    private Long taskPackageId;

    private boolean cuttingflag = false;

    private String memo;

    @Schema(description = "是否套料")
    private Boolean isNested;

    @Transient
    private String lengthText;

    @Column(name = "nesting_file", columnDefinition = "text")
    private String nestingFile;

    @Transient
    private boolean printEnableFlag = true;

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

    public Long getCuttingId() {
        return cuttingId;
    }

    public void setCuttingId(Long cuttingId) {
        this.cuttingId = cuttingId;
    }

    public Long getIsoEntityId() {
        return isoEntityId;
    }

    public void setIsoEntityId(Long isoEntityId) {
        this.isoEntityId = isoEntityId;
    }

    public String getIsoEntityNo() {
        return isoEntityNo;
    }

    public void setIsoEntityNo(String isoEntityNo) {
        this.isoEntityNo = isoEntityNo;
    }

    public Long getSpoolEntityId() {
        return spoolEntityId;
    }

    public void setSpoolEntityId(Long spoolEntityId) {
        this.spoolEntityId = spoolEntityId;
    }

    public String getSpoolEntityNo() {
        return spoolEntityNo;
    }

    public void setSpoolEntityNo(String spoolEntityNo) {
        this.spoolEntityNo = spoolEntityNo;
    }

    public Long getPipePieceEntityId() {
        return pipePieceEntityId;
    }

    public void setPipePieceEntityId(Long pipePieceEntityId) {
        this.pipePieceEntityId = pipePieceEntityId;
    }

    @JsonSetter
    public void setPipePieceEntityId(ReferenceData pipePieceEntityRef) {
        this.pipePieceEntityId = pipePieceEntityRef.get$ref();
    }

    @Schema(description = "实体二维码打印信息 ID")
    @JsonSetter
    @JsonProperty(value = "pipePieceEntityId")
    public ReferenceData getPipePieceEntityRef() {
        return new ReferenceData(this.pipePieceEntityId);
    }

    public String getPipePieceEntityNo() {
        return pipePieceEntityNo;
    }

    public void setPipePieceEntityNo(String pipePieceEntityNo) {
        this.pipePieceEntityNo = pipePieceEntityNo;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getNps() {
        return nps;
    }

    public void setNps(String nps) {
        this.nps = nps;
    }

    public Long getMatPrepareId() {
        return matPrepareId;
    }

    public void setMatPrepareId(Long matPrepareId) {
        this.matPrepareId = matPrepareId;
    }

    public String getMatPrepareCode() {
        return matPrepareCode;
    }

    public void setMatPrepareCode(String matPrepareCode) {
        this.matPrepareCode = matPrepareCode;
    }

    public Long getMatIssueId() {
        return matIssueId;
    }

    public void setMatIssueId(Long matIssueId) {
        this.matIssueId = matIssueId;
    }

    public String getMatIssueCode() {
        return matIssueCode;
    }

    public void setMatIssueCode(String matIssueCode) {
        this.matIssueCode = matIssueCode;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean isCuttingflag() {
        return cuttingflag;
    }

    public void setCuttingflag(boolean cuttingflag) {
        this.cuttingflag = cuttingflag;
    }

//    @Schema(description = "实体二维码打印信息")
//    @JsonProperty(value = "pipePieceEntityId")
//    public ReferenceData getPipePieceEntityIdRef() {
//        return this.pipePieceEntityId == null
//            ? null
//            : new ReferenceData(this.pipePieceEntityId);
//    }

    public String getEntityModuleName() {
        return entityModuleName;
    }

    public void setEntityModuleName(String entityModuleName) {
        this.entityModuleName = entityModuleName;
    }

    public Long getEntityModuleProjectNodeId() {
        return entityModuleProjectNodeId;
    }

    public void setEntityModuleProjectNodeId(Long entityModuleProjectNodeId) {
        this.entityModuleProjectNodeId = entityModuleProjectNodeId;
    }

    public String getNestingFile() {
        return nestingFile;
    }

    public void setNestingFile(String nestingFile) {
        this.nestingFile = nestingFile;
    }

    @JsonIgnore
    public void setJsonReports(ActReportDTO report) {
        if (report != null) {
            this.nestingFile = StringUtils.toJSON(report);
        }
    }

    @JsonProperty(value = "nestingFile", access = JsonProperty.Access.READ_ONLY)
    public ActReportDTO getJsonNestingFileReadOnly() {
        if (nestingFile != null && !"".equals(nestingFile)) {
            return StringUtils.decode(nestingFile, new TypeReference<ActReportDTO>() {
            });
        } else {
            return new ActReportDTO();
        }
    }

    public String getTaskPackageName() {
        return taskPackageName;
    }

    public void setTaskPackageName(String taskPackageName) {
        this.taskPackageName = taskPackageName;
    }

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public String getLengthText() {
        return lengthText;
    }

    public void setLengthText(String lengthText) {
        this.lengthText = lengthText;
    }

    public boolean isPrintEnableFlag() {
        return printEnableFlag;
    }

    public void setPrintEnableFlag(boolean printEnableFlag) {
        this.printEnableFlag = printEnableFlag;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public Boolean getNested() {
        return isNested;
    }

    public void setNested(Boolean nested) {
        isNested = nested;
    }

    public String getMatSurplusReceiptsNo() {
        return matSurplusReceiptsNo;
    }

    public void setMatSurplusReceiptsNo(String matSurplusReceiptsNo) {
        this.matSurplusReceiptsNo = matSurplusReceiptsNo;
    }

    public Long getMatSurplusReceiptsId() {
        return matSurplusReceiptsId;
    }

    public void setMatSurplusReceiptsId(Long matSurplusReceiptsId) {
        this.matSurplusReceiptsId = matSurplusReceiptsId;
    }

    public NestGateWay getNestGateWay() {
        return nestGateWay;
    }

    public void setNestGateWay(NestGateWay nestGateWay) {
        this.nestGateWay = nestGateWay;
    }
}
