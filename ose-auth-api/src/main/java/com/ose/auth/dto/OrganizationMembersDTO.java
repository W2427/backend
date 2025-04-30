package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class OrganizationMembersDTO extends BaseDTO {

    private static final long serialVersionUID = -76342157860246358L;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "用户手机号")
    private String mobile;

    @Schema(description = "部门名称")
    private List<String> organizations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<String> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<String> organizations) {
        this.organizations = organizations;
    }
}
