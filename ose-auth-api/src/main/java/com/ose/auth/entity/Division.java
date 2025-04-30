package com.ose.auth.entity;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 用户资料数据实体类。
 */
@Entity
@Table(name = "division")
public class Division extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 3496932788404923146L;
    @Schema(description = "名称")
    @Column
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
