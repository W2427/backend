package com.ose.tasks.entity;

import com.ose.entity.BaseVersionedBizEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "label")
public class Label extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 8812305841411160870L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
