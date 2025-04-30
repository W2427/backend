package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

/**
 * 标签 数据传输对象
 */
public class LabelDTO extends BaseDTO {

    private static final long serialVersionUID = 2244411712346626204L;
    /**
     *
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
