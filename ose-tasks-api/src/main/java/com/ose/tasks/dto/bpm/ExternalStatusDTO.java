package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

/**
 * 实体管理 数据传输对象
 */
public class ExternalStatusDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
