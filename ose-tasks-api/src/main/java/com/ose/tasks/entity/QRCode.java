package com.ose.tasks.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseEntity;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.vo.QRCodeTargetType;
import com.ose.util.CryptoUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 二维码代码数据实体。
 */
@Entity
@Table(
    name = "qrcode",
    indexes = {
        @Index(
            name = "code",
            columnList = "code,deletedAt",
            unique = true
        ),
        @Index(
            name = "project_target",
            columnList = "projectId,targetId,deletedAt",
            unique = true
        )
    }
)
public class QRCode extends BaseEntity {

    private static final long serialVersionUID = 4813150910571052122L;

    @Schema(description = "项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "二维码代码")
    @Column(length = 16, nullable = false)
    private String code;

    @Schema(description = "目标类型")
    @Column(length = 64, nullable = false)
    @Enumerated(EnumType.STRING)
    private QRCodeTargetType targetType;

    @Schema(description = "目标 ID")
    @Column(nullable = false)
    private Long targetId;

    @Schema(description = "创建时间")
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date createdAt;

    @Schema(description = "创建者")
    @Column(nullable = false)
    private Long createdBy;

    @Schema(description = "是否已删除")
    @Column
    private Boolean deleted;

    @Schema(description = "删除时间")
    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date deletedAt;

    @Schema(description = "删除者")
    @Column
    private Long deletedBy;

    @Schema(description = "目标数据实体")
    @Transient
    private BaseEntity target;

    /**
     * 构造方法。
     */
    public QRCode() {
        this.code = CryptoUtils.shortUniqueId();
        this.createdAt = new Date();
        this.deleted = false;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public QRCodeTargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(QRCodeTargetType targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Long getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }

    public BaseEntity getTarget() {
        return target;
    }

    public void setTarget(BaseEntity target) {

        if (target instanceof WBSEntityBase) {
            setProjectId(((WBSEntityBase) target).getProjectId());
        }

        this.target = target;
    }

    /**
     * 取得创建者引用信息。
     *
     * @return 创建者引用信息
     */
    @Schema(description = "创建者信息")
    @JsonProperty(value = "createdBy", access = READ_ONLY)
    public ReferenceData getCreatedByRef() {
        return this.createdBy == null
            ? null
            : new ReferenceData(this.createdBy);
    }

    /**
     * 取得删除者引用信息。
     *
     * @return 删除者引用信息
     */
    @Schema(description = "删除者信息")
    @JsonProperty(value = "deletedBy", access = READ_ONLY)
    public ReferenceData getDeletedByRef() {
        return this.deletedBy == null
            ? null
            : new ReferenceData(this.deletedBy);
    }

}
