package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 数据传输对象
 */
public class MaterialBizCodeLengthDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "长度")
    private int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
