package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;

/**
 * 传输对象
 */
public class TeamLeaderDO extends BaseDTO {

    private static final long serialVersionUID = -3907609959237350019L;
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
