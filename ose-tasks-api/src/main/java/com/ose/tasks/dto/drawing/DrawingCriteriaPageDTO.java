package com.ose.tasks.dto.drawing;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 包含分页查询的图纸。
 *
 * @auth DengMing
 * @date 2021/8/4 14:25
 */
public class DrawingCriteriaPageDTO extends PageDTO {

    private static final long serialVersionUID = -483630384082634263L;

    @Schema(description = "document number")
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
