package com.ose.tasks.entity.bpm;

import com.ose.dto.BaseDTO;


public class TaskCommandDTO extends BaseDTO {

    private static final long serialVersionUID = 2314873118211289766L;
    /**
     *
     */

    private String key;

    private Object condition;

    public Object getCondition() {
        return condition;
    }

    public void setCondition(Object condition) {
        this.condition = condition;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
