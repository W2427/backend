package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

/**
 * entity排序数据传输对象
 */
public class BatchAddResponseDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    // 工序id/实体类型ID
    private Long id;

    // 添加是否成功
    private boolean result;

    // 返回信息
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
