package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户查询条件数据传输对象类。
 */
public class CompanyDTO extends BaseDTO {

    private static final long serialVersionUID = -8646977023338659329L;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "登录用户名")
    private String country;
    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "父级名")
    private String parentTeamName;

    public String getParentTeamName() {
        return parentTeamName;
    }

    public void setParentTeamName(String parentTeamName) {
        this.parentTeamName = parentTeamName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
