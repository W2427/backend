package com.ose.test.entity;

import com.ose.dto.BaseDTO;

import java.util.List;

public class TestEntity extends BaseDTO {

    private Long id;

    private List<Test01Entity> ids;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Test01Entity> getIds() {
        return ids;
    }

    public void setIds(List<Test01Entity> ids) {
        this.ids = ids;
    }
}
