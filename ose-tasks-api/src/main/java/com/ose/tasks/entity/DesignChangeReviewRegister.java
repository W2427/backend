package com.ose.tasks.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseBizEntity;
import com.ose.util.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "modification_review_register")
public class DesignChangeReviewRegister extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    private Long orgId;

    private Long projectId;

    @Transient
    private boolean hasTask;

    @Schema(description = "传送单号")
    private String transferNo;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "问题提出时间")
    private Date raisedDate;

    @Schema(description = "设计变更审批单号")
    private String vorNo;

    @Schema(description = "修改发生根源")
    private String originatedBy;

    @Schema(description = "草稿版评审单文件")
    private Long changeReviewFileId;

    @Schema(description = "草稿版评审单文件名")
    private String changeReviewFileName;

    @Schema(description = "可编辑版评审单文件id")
    private Long editChangeReviewFileId;

    @Schema(description = "可编辑版评审单文件名")
    private String editChangeReviewFileName;

    @Schema(description = "签字版评审单文件id")
    private Long vorFileId;

    @Schema(description = "签字版评审单文件名")
    private String vorFileName;

    @Schema(description = "作业变更指令单文件id")
    private Long jobChanegFileId;

    @Schema(description = "作业变更指令单文件名")
    private String jobChanegFileName;

    @Schema(description = "设计变更评审单审核标识")
    private boolean changeReviewFlag;

    @Column(columnDefinition = "text")
    private String dwgActInstIds;

    @JsonProperty(value = "jsonDwgActInstIds", access = JsonProperty.Access.READ_ONLY)
    public List<Long> getJsonDwgActInstIdsReadOnly() {
        if (dwgActInstIds != null && !"".equals(dwgActInstIds)) {
            return StringUtils.decode(dwgActInstIds, new TypeReference<List<Long>>() {
            });
        } else {
            return null;
        }
    }

    @JsonIgnore
    public void setJsonDwgActInstIds(List<Long> dwgActInstIds) {
        if (dwgActInstIds != null) {
            this.dwgActInstIds = StringUtils.toJSON(dwgActInstIds);
        }
    }

    public String getTransferNo() {
        return transferNo;
    }

    public void setTransferNo(String transferNo) {
        this.transferNo = transferNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getRaisedDate() {
        return raisedDate;
    }

    public void setRaisedDate(Date raisedDate) {
        this.raisedDate = raisedDate;
    }

    public String getVorNo() {
        return vorNo;
    }

    public void setVorNo(String vorNo) {
        this.vorNo = vorNo;
    }

    public String getOriginatedBy() {
        return originatedBy;
    }

    public void setOriginatedBy(String originatedBy) {
        this.originatedBy = originatedBy;
    }

    public Long getChangeReviewFileId() {
        return changeReviewFileId;
    }

    public void setChangeReviewFileId(Long changeReviewFileId) {
        this.changeReviewFileId = changeReviewFileId;
    }

    public String getChangeReviewFileName() {
        return changeReviewFileName;
    }

    public void setChangeReviewFileName(String changeReviewFileName) {
        this.changeReviewFileName = changeReviewFileName;
    }

    public Long getVorFileId() {
        return vorFileId;
    }

    public void setVorFileId(Long vorFileId) {
        this.vorFileId = vorFileId;
    }

    public String getVorFileName() {
        return vorFileName;
    }

    public void setVorFileName(String vorFileName) {
        this.vorFileName = vorFileName;
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

    public boolean isHasTask() {
        return hasTask;
    }

    public void setHasTask(boolean hasTask) {
        this.hasTask = hasTask;
    }

    public String getDwgActInstIds() {
        return dwgActInstIds;
    }

    public void setDwgActInstIds(String dwgActInstIds) {
        this.dwgActInstIds = dwgActInstIds;
    }

    public boolean isChangeReviewFlag() {
        return changeReviewFlag;
    }

    public void setChangeReviewFlag(boolean changeReviewFlag) {
        this.changeReviewFlag = changeReviewFlag;
    }

    public Long getJobChanegFileId() {
        return jobChanegFileId;
    }

    public void setJobChanegFileId(Long jobChanegFileId) {
        this.jobChanegFileId = jobChanegFileId;
    }

    public String getJobChanegFileName() {
        return jobChanegFileName;
    }

    public void setJobChanegFileName(String jobChanegFileName) {
        this.jobChanegFileName = jobChanegFileName;
    }

    public Long getEditChangeReviewFileId() {
        return editChangeReviewFileId;
    }

    public void setEditChangeReviewFileId(Long editChangeReviewFileId) {
        this.editChangeReviewFileId = editChangeReviewFileId;
    }

    public String getEditChangeReviewFileName() {
        return editChangeReviewFileName;
    }

    public void setEditChangeReviewFileName(String editChangeReviewFileName) {
        this.editChangeReviewFileName = editChangeReviewFileName;
    }

}
