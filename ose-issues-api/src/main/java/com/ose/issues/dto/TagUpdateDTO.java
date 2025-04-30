package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class TagUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = 1916906287407066608L;

    @Schema(description = "文本名称")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
