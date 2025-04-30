package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 材料准备单表
 */
@Entity
@Table(name = "mat_f_material_prepare")
public class FMaterialPrepareEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "project_id")
    private Long projectId;

    // 材料准备单单号
    @Column(name = "mp_code")
    private String mpCode;

    @Column(name = "seq_number")
    private Integer seqNumber;

    // 材料准备单的简单描述
    @Column(name = "mp_desc")
    private String mpDesc;

    @Column(name = "process_id")
    private Long processId;

    @Column(name = "act_inst_id")
    private Long actInstId;

    @Column(name = "is_finished")
    private Boolean isFinished =false;

    @Schema(description = "SPM是否已经出库")
    @Column(name = "spm_saved_flg")
    private Boolean spmSavedFlg =false;

    @Schema(description = "SPM 领料单ID")
    @Column(name = "spm_fah_id")
    private String spmFahId;

    @Schema(description = "SPM 领料单")
    @Column(name = "spm_fah_code")
    private String spmFahCode;

    @Schema(description = "SPM 运行编号")
    @Column(name = "spm_run_number")
    private String spmRunNumber;

    @Schema(description = "出库清单ID")
    @Column(name = "fmir_id")
    private Long fmirId;

    @Schema(description = "套料清单")
    private String fmnrCode;

    @Schema(description = "套料清单ID")
    private Long fmnrId;

    @Schema(description = "出库清单")
    @Column(name = "fmir_code")
    private String fmirCode;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "company_code")
    private String companyCode;

    @Schema(description = "任务包ID")
    private Long taskPackageId;

    @Schema(description = "任务包类型")
    private String taskPackageCategoryEntityType;

    @Schema(description = "网关")
    private String gateway;

    @Schema(description = "网关名称")
    private String gatewayName;

    @Schema(description = "套料执行FLG，true：执行套料，false：不需要套料")
    private boolean nestExecFlg = false;

    @Transient
    private String taskId;

    @Transient
    private String taskDefKey;

    @Column(name = "nesting_file", columnDefinition = "text")
    private String nestingFile;

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
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

    public String getMpCode() {
        return mpCode;
    }

    public void setMpCode(String mpCode) {
        this.mpCode = mpCode;
    }

    public String getMpDesc() {
        return mpDesc;
    }

    public void setMpDesc(String mpDesc) {
        this.mpDesc = mpDesc;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public Boolean isFinished() {
        return isFinished;
    }

    public void setFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getSpmFahId() {
        return spmFahId;
    }

    public void setSpmFahId(String spmFahId) {
        this.spmFahId = spmFahId;
    }

    public String getSpmFahCode() {
        return spmFahCode;
    }

    public void setSpmFahCode(String spmFahCode) {
        this.spmFahCode = spmFahCode;
    }

    public String getSpmRunNumber() {
        return spmRunNumber;
    }

    public void setSpmRunNumber(String spmRunNumber) {
        this.spmRunNumber = spmRunNumber;
    }

    public Long getFmirId() {
        return fmirId;
    }

    public void setFmirId(Long fmirId) {
        this.fmirId = fmirId;
    }

    public String getFmirCode() {
        return fmirCode;
    }

    public void setFmirCode(String fmirCode) {
        this.fmirCode = fmirCode;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getNestingFile() {
        return nestingFile;
    }

    public void setNestingFile(String nestingFile) {
        this.nestingFile = nestingFile;
    }

    public String getFmnrCode() {
        return fmnrCode;
    }

    public void setFmnrCode(String fmnrCode) {
        this.fmnrCode = fmnrCode;
    }

    public Long getFmnrId() {
        return fmnrId;
    }

    public void setFmnrId(Long fmnrId) {
        this.fmnrId = fmnrId;
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

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public boolean isNestExecFlg() {
        return nestExecFlg;
    }

    public void setNestExecFlg(boolean nestExecFlg) {
        this.nestExecFlg = nestExecFlg;
    }

    @Schema(description = "任务包")
    @JsonProperty(value = "taskPackageId", access = READ_ONLY)
    public ReferenceData getTaskPackageIdRef() {
        return this.taskPackageId == null
            ? null
            : new ReferenceData(this.taskPackageId);
    }

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public String getTaskPackageCategoryEntityType() {
        return taskPackageCategoryEntityType;
    }

    public void setTaskPackageCategoryEntityType(String taskPackageCategoryEntityType) {
        this.taskPackageCategoryEntityType = taskPackageCategoryEntityType;
    }

    public Boolean getSpmSavedFlg() {
        return spmSavedFlg;
    }

    public void setSpmSavedFlg(Boolean spmSavedFlg) {
        this.spmSavedFlg = spmSavedFlg;
    }
}

