package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;

public class TaskGatewayDTO extends BaseDTO {

    public TaskGatewayDTO(String name, String key, Object condition) {
        super();
        this.name = name;
        this.condition = condition;
        this.key = key;
    }

    public TaskGatewayDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     *
     */
    private static final long serialVersionUID = -5758427728588980301L;

    private boolean mutiSelectFlag = false;

    private String name;

    private String key;

    private Object condition;

    private List<TaskGatewayDTO> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getCondition() {
        return condition;
    }

    public void setCondition(Object condition) {
        this.condition = condition;
    }

    public boolean isMutiSelectFlag() {
        return mutiSelectFlag;
    }

    public void setMutiSelectFlag(boolean mutiSelectFlag) {
        this.mutiSelectFlag = mutiSelectFlag;
    }

    public List<TaskGatewayDTO> getItems() {
        return items;
    }

    public void setItems(List<TaskGatewayDTO> items) {
        this.items = items;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
