package com.ose.tasks.dto.holiday;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 节假日清单查询传输对象。
 */
public class WorkDaySearchDTO extends PageDTO {
    private static final long serialVersionUID = 8370589535651804634L;
    @Schema(description = "关键字")
    private String keyword;
    @Schema(description = "国家")
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
