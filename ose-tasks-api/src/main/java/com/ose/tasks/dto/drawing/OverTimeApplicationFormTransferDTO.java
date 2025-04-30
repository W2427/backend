package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 传输对象
 */
public class OverTimeApplicationFormTransferDTO extends BaseDTO {

    private static final long serialVersionUID = 1925304121571311581L;
    @Schema(description = "名字")
    private String name;
    @Schema(description = "名字")
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
