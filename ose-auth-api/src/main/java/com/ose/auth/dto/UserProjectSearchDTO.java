package com.ose.auth.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户查询条件数据传输对象类。
 */
public class UserProjectSearchDTO extends PageDTO {

    private static final long serialVersionUID = -1704099273475537939L;

    @Schema(description = "姓名")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
