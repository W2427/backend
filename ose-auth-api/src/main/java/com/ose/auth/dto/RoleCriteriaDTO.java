package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class RoleCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = -5308702758062802742L;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "角色code")
    private String code;

    @Schema(description = "状态")
    private List<EntityStatus> status;

    @Schema(description = "是否为模板")
    private String isTemplate;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<EntityStatus> getStatus() {
        return status;
    }

    public void setStatus(List<EntityStatus> status) {
        this.status = status;
    }

    public String getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(String isTemplate) {
        this.isTemplate = isTemplate;
    }
}
