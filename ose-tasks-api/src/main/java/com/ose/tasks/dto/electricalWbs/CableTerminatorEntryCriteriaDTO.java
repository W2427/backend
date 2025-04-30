package com.ose.tasks.dto.electricalWbs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ose 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CableTerminatorEntryCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -7032813624943414939L;

    @Schema(description = "搜索关键字")
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
