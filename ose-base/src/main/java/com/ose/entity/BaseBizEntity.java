package com.ose.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.vo.EntityStatus;
import com.ose.vo.StatusConverter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;

/**
 * 业务数据实体基类。
 */
@MappedSuperclass
public abstract class BaseBizEntity extends BaseEntity {

    private static final long serialVersionUID = 1900128268725594876L;

    @Schema(description = "创建时间")
    @Column(nullable = false)
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date createdAt;

    @Schema(description = "最后更新时间")
    @Column(nullable = false)
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date lastModifiedAt;

    @Schema(description = "数据实体状态")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    @ExcelProperty(converter = StatusConverter.class)
    private EntityStatus status;

    /**
     * 默认构造方法。
     */
    public BaseBizEntity() {
        this(null);
    }

    /**
     * 构造方法。
     *
     * @param id 数据实体 ID
     */
    public BaseBizEntity(Long id) {
        super(id);
        this.setCreatedAt();
        this.setLastModifiedAt();
    }

    /**
     * 取得创建时间。
     *
     * @return 创建时间
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置创建时间。
     */
    public void setCreatedAt() {
        this.setCreatedAt(new Date());
    }

    /**
     * 设置创建时间。
     *
     * @param createdAt 创建时间
     */
    @JsonSetter
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 取得最后更新时间。
     *
     * @return 最后更新时间
     */
    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    /**
     * 设置最后更新时间。
     */
    public void setLastModifiedAt() {
        this.setLastModifiedAt(new Date());
    }

    /**
     * 设置最后更新时间。
     *
     * @param lastModifiedAt 最后更新时间
     */
    @JsonSetter
    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    /**
     * 取得状态。
     *
     * @return 状态
     */
    public EntityStatus getStatus() {
        return status;
    }

    /**
     * 设置状态。
     *
     * @param status 状态
     */
    public void setStatus(EntityStatus status) {
        this.status = status;
    }

}
