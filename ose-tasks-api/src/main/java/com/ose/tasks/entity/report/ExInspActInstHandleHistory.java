package com.ose.tasks.entity.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.bpm.ExInspApplyHandleType;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 外检处理历史记录
 */
@Entity
@Table(name = "bpm_exinsp_actinst_handle_history",
    indexes = {
        @Index(columnList = "orgId,projectId,exInspScheduleNo,runningStatus"),
        @Index(columnList = "orgId,projectId,type,runningStatus"),
    })
public class ExInspActInstHandleHistory extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -2285463355029901014L;

    @Schema(description = "ORG ID")
    @Column
    private Long orgId;

    @Schema(description = "PROJECT ID")
    @Column
    private Long projectId;

    @Schema(description = "外检申请 编号")
    @Column
    private String exInspScheduleNo;

    @Schema(description = "报告编号")
    @Column(columnDefinition = "TEXT")
    private String reportNo;

    @Schema(description = "流水编号")
    @Column(columnDefinition = "TEXT")
    private String seriesNo;

    @Schema(description = "外检申请 ID")
    @Column
    private Long exInspScheduleId;

    @Schema(description = "流程 proc_inst_ids")
    @Column(columnDefinition = "TEXT")
    private String actInstIds;

    @Schema(description = "实体编号")
    @Column(columnDefinition = "TEXT")
    private String entityNos;

    @Schema(description = "数据运行状态")
    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private EntityStatus runningStatus;

    @Schema(description = "备注")
    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Schema(description = "错误信息")
    @Column(columnDefinition = "TEXT")
    private String errors;

    @Schema(description = "导入文件ID")
    @Column
    private Long fileId;

    @Schema(description = "外检处理类型")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private ExInspApplyHandleType type;

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

    public String getExInspScheduleNo() {
        return exInspScheduleNo;
    }

    public void setExInspScheduleNo(String exInspScheduleNo) {
        this.exInspScheduleNo = exInspScheduleNo;
    }

    public Long getExInspScheduleId() {
        return exInspScheduleId;
    }

    public void setExInspScheduleId(Long exInspScheduleId) {
        this.exInspScheduleId = exInspScheduleId;
    }

    public String getActInstIds() {
        return actInstIds;
    }

    public void setActInstIds(String actInstIds) {
        this.actInstIds = actInstIds;
    }

    @JsonProperty(value = "actInstIds", access = JsonProperty.Access.READ_ONLY)
    public List<Long> getJsonActInstIds() {
        if (StringUtils.isEmpty(actInstIds)) {
            return new ArrayList<>();
        }

        return StringUtils.decode(actInstIds, new TypeReference<List<Long>>() {
        });

    }

    @JsonIgnore
    public void setJsonActInstIds(List<Long> actInstIds) {
        if (actInstIds != null) {
            this.actInstIds = StringUtils.toJSON(actInstIds);
        }
    }

    public EntityStatus getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(EntityStatus runningStatus) {
        this.runningStatus = runningStatus;
    }

    public String getEntityNos() {
        return entityNos;
    }

    public void setEntityNos(String entityNos) {
        this.entityNos = entityNos;
    }

    @JsonProperty(value = "entityNos", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonEntityNos() {
        if (StringUtils.isEmpty(entityNos)) {
            return new ArrayList<>();
        }

        return StringUtils.decode(entityNos, new TypeReference<List<String>>() {
        });

    }

    @JsonIgnore
    public void setJsonEntityNos(List<String> entityNos) {
        if (entityNos != null) {
            this.entityNos = StringUtils.toJSON(entityNos);
        }
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ExInspApplyHandleType getType() {
        return type;
    }

    public void setType(ExInspApplyHandleType type) {
        this.type = type;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
