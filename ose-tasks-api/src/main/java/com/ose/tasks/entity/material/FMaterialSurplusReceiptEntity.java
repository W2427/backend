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
 * 余料领料单实体类。
 */
@Entity
@Table(name = "mat_f_material_surplus_receipt")
public class FMaterialSurplusReceiptEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 6486387223723326666L;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "project_id")
    private Long projectId;

    // 材料准备单单号
    @Column(name = "no")
    private String no;

    @Column(name = "seq_number")
    private Integer seqNumber;

    // 材料准备单的简单描述
    @Column(name = "memo")
    private String memo;

    @Column(name = "process_id")
    private Long processId;

    @Column(name = "act_inst_id")
    private Long actInstId;

    @Column(name = "is_finished")
    private boolean isFinished;

    @Schema(description = "套料清单")
    private String fMaterialSurplusNestNo;

    @Schema(description = "套料清单ID")
    private Long fMaterialSurplusNestId;

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

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getfMaterialSurplusNestNo() {
        return fMaterialSurplusNestNo;
    }

    public void setfMaterialSurplusNestNo(String fMaterialSurplusNestNo) {
        this.fMaterialSurplusNestNo = fMaterialSurplusNestNo;
    }

    public Long getfMaterialSurplusNestId() {
        return fMaterialSurplusNestId;
    }

    public void setfMaterialSurplusNestId(Long fMaterialSurplusNestId) {
        this.fMaterialSurplusNestId = fMaterialSurplusNestId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
