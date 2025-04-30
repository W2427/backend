package com.ose.test.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.SqlResultSetMapping;

import com.ose.entity.BaseEntity;

@SqlResultSetMapping(name = "ItemResults", entities = {@EntityResult(entityClass = DemoEntity.class, // 就是当前这个类的名字
    fields = {@FieldResult(name = "identCode", column = "identCode"),
        @FieldResult(name = "id", column = "id")})})

@Entity
public class DemoEntity extends BaseEntity {

    public String identCode;

    public String getIdentCode() {
        return identCode;
    }

    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }

}
