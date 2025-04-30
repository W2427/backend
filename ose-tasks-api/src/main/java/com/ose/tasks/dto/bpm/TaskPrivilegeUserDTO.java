package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;


/**
 * 实体数据传输对象
 */
public class TaskPrivilegeUserDTO extends BaseDTO {

    private static final long serialVersionUID = 4182209082870764520L;

    public Long id;
    public String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
