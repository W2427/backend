package com.ose.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseActivitiEntity extends BaseDTO {

    private static final long serialVersionUID = 1459281922241503045L;

    // ID 取值的进制（0~9A~Z）
    private static final int ID_RADIX = 36;

    // ID 中随机数上限（不包含）
    private static final long ID_RANDOM_MAX = 1679616;

    @SuppressWarnings("CheckStyle")
    @Schema(description = "实体 ID")
    @Id
    @Column(length = 16)
    private String id_;

    /**
     * 默认构造方法。
     */
    public BaseActivitiEntity() {
        this(generateId());
    }

    /**
     * 构造方法。
     *
     * @param id_ 实体 ID
     */
    public BaseActivitiEntity(@SuppressWarnings("CheckStyle") String id_) {
        this.setId_(id_ == null ? generateId() : id_);
    }


    public String getId_() {
        return id_;
    }

    public void setId_(@SuppressWarnings("CheckStyle") String id_) {
        this.id_ = id_;
    }

    /**
     * 生成 ID。
     *
     * @return ID
     */
    public static String generateId() {
        return CryptoUtils.uniqueId().toUpperCase();
    }


}
