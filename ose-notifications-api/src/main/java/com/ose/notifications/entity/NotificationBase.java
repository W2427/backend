package com.ose.notifications.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ose.constant.JsonFormatPattern;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 通知记录数据实体。
 */
@MappedSuperclass
public class NotificationBase extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -4522290928337320130L;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "通知时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    @Column
    private Date sentAt;

    @Schema(description = "标题")
    @Column(length = 1020)
    private String title;

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

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
