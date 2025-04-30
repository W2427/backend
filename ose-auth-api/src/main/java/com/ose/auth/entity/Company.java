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
@Table(name = "company")
public class Company extends BaseVersionedBizEntity {


    private static final long serialVersionUID = -7305912474503500753L;
    @Schema(description = "名称")
    @Column
    private String name;
    @Schema(description = "国家")
    @Column
    private String country;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
