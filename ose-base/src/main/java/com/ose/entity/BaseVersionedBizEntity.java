package com.ose.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 版本化业务数据实体基类。
 */
@MappedSuperclass
public abstract class BaseVersionedBizEntity extends BaseBizEntity {

    private static final long serialVersionUID = -6873798593211435589L;

    @Column
    @JsonIgnore
    private Long createdBy;

    @Column
    @JsonIgnore
    private Long lastModifiedBy;

    @Schema(description = "是否已被删除")
    @Column(nullable = false)
    private Boolean deleted = false;

    @Column
    @JsonIgnore
    private Long deletedBy;

    @Schema(description = "删除时间")
    @Column
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date deletedAt;

    @Schema(description = "更新版本（手动乐观锁）")
    @Column(nullable = false)
    private Long version;

    /**
     * 默认构造方法。
     */
    public BaseVersionedBizEntity() {
        this(null);
    }

    /**
     * 构造方法。
     *
     * @param id 数据实体 ID
     */
    public BaseVersionedBizEntity(Long id) {
        super(id);
        this.version = this.getLastModifiedAt().getTime();
    }

    /**
     * 取得创建者 ID。
     *
     * @return 创建者 ID
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * 设置创建者 ID。
     *
     * @param createdBy 创建者 ID
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * 设置创建者 ID。
     *
     * @param createdBy 创建者信息应用
     */
    @JsonSetter
    public void setCreatedBy(ReferenceData createdBy) {
        this.createdBy = createdBy.get$ref();
    }

    /**
     * 取得最后更新者 ID。
     *
     * @return 最后更新者 ID
     */
    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * 设置最后更新者 ID。
     *
     * @param lastModifiedBy 最后更新者 ID
     */
    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * 设置最后更新者 ID。
     *
     * @param lastModifiedBy 最后更新者应用
     */
    @JsonSetter
    public void setLastModifiedBy(ReferenceData lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy.get$ref();
    }

    /**
     * 设置最后更新时间，更新更新版本。
     */
    @Override
    public void setLastModifiedAt() {
        this.setLastModifiedAt(new Date());
    }

    /**
     * 设置最后更新时间，更新更新版本。
     *
     * @param lastModifiedAt 最后更新时间
     */
    @Override
    @JsonSetter
    public void setLastModifiedAt(Date lastModifiedAt) {

        super.setLastModifiedAt(lastModifiedAt);

        if (lastModifiedAt == null) {
            this.version = null;
        } else {
            this.version = lastModifiedAt.getTime();
        }

    }

    /**
     * 取得是否已被删除的标记。
     *
     * @return 是否已被删除
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * 设置是否已被删除的标记。
     *
     * @param deleted 是否已被删除
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * 取得删除者 ID。
     *
     * @return 删除者 ID
     */
    public Long getDeletedBy() {
        return deletedBy;
    }

    /**
     * 设置删除者 ID。
     *
     * @param deletedBy 删除者 ID
     */
    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }

    /**
     * 设置删除者 ID。
     *
     * @param deletedBy 删除者应用
     */
    @JsonSetter
    public void setDeletedBy(ReferenceData deletedBy) {
        this.deletedBy = deletedBy.get$ref();
    }

    /**
     * 取得删除时间。
     *
     * @return 删除时间
     */
    public Date getDeletedAt() {
        return deletedAt;
    }

    /**
     * 设置删除时间。
     */
    public void setDeletedAt() {
        this.setDeletedAt(new Date());
    }

    /**
     * 设置删除时间。
     */
    public void setCancelledAt() {
        this.deletedAt = new Date();
        this.version = deletedAt.getTime();
    }

    /**
     * 设置删除时间。
     *
     * @param deletedAt 删除时间
     */
    @JsonSetter
    public void setDeletedAt(Date deletedAt) {

        if (deletedAt == null) {
            return;
        }

        this.deleted = true;
        this.deletedAt = deletedAt;
        this.version = deletedAt.getTime();
    }

    /**
     * 取得更新版本。
     *
     * @return 更新版本
     */
    public Long getVersion() {
        return version;
    }

    /**
     * 设置更新版本。
     *
     * @param version 更新版本
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * 取得创建者引用信息。
     *
     * @return 创建者引用信息
     */
    @Schema(description = "创建者信息")
    @JsonProperty(value = "createdBy")
    public ReferenceData getCreatedByRef() {
        return this.createdBy == null
            ? null
            : new ReferenceData(this.createdBy);
    }

    /**
     * 取得最后更新者引用信息。
     *
     * @return 最后更新者引用
     */
    @Schema(description = "最后更新者信息")
    @JsonProperty(value = "lastModifiedBy")
    public ReferenceData getLastModifiedByRef() {
        return this.lastModifiedBy == null
            ? null
            : new ReferenceData(this.lastModifiedBy);
    }

    /**
     * 取得删除者引用信息。
     *
     * @return 删除者引用信息
     */
    @Schema(description = "删除者信息")
    @JsonProperty(value = "deletedBy")
    public ReferenceData getDeletedByRef() {
        return this.deletedBy == null
            ? null
            : new ReferenceData(this.deletedBy);
    }

    /**
     * 取得相关联的用户 ID 的集合。
     *
     * @return 相关联的用户 ID 的集合
     */
    @Override
    public Set<Long> relatedUserIDs() {

        Set<Long> userIDs = new HashSet<>();

        if (this.getCreatedBy() != null && this.getCreatedBy() != 0L) {
            userIDs.add(this.getCreatedBy());
        }

        if (this.getLastModifiedBy() != null && this.getLastModifiedBy() != 0L) {
            userIDs.add(this.getLastModifiedBy());
        }

        if (this.getDeletedBy() != null && this.getDeletedBy() != 0L) {
            userIDs.add(this.getDeletedBy());
        }

        return userIDs;
    }

}
