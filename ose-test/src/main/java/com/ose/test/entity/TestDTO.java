package com.ose.test.entity;

import com.ose.dto.BaseDTO;

import java.util.List;

public class TestDTO extends BaseDTO {
    private static final long serialVersionUID = 6548871254774803255L;
    private Long test;

    private List<Long> ids;


    public Long getTest() {
        return test;
    }

    public void setTest(Long test) {
        this.test = test;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
