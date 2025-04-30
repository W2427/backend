package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

/**
 * 任务查询条件数据传输对象类。
 */
public class DrawingAppointDTO extends BaseDTO {

    private static final long serialVersionUID = -1355336456498907047L;

    private Long id;

    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
