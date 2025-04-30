package com.ose.entity;

import com.ose.dto.BaseDTO;
import com.ose.util.CryptoUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * 数据实体基类。
 */
@MappedSuperclass
public abstract class BaseEntity extends BaseDTO {

    private static final long serialVersionUID = 1459281922241503045L;

    @Schema(description = "实体 ID")
    @Id
    @Column
    private Long id;

    /**
     * 默认构造方法。
     */
    public BaseEntity() {
        this(generateId());
    }

    /**
     * 构造方法。
     *
     * @param id 实体 ID
     */
    public BaseEntity(Long id) {
        this.setId(id == null ? generateId() : id);
    }

    /**
     * 取得数据实体 ID。
     *
     * @return 数据实体 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置数据实体 ID。
     *
     * @param id 数据实体 ID
     */
    public void setId(Long id) {
        this.id = id;
    }

//    public static String generateId() {
//        return CryptoUtils.uniqueId().toUpperCase();
//    }

    /**
     * 生成 bigint/long ID。
     *
     * @return ID
     */
    public static Long generateId() {
        return CryptoUtils.uniqueDecId();
    }
}
