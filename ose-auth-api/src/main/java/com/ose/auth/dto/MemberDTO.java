package com.ose.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemberDTO extends BaseDTO {

    private static final long serialVersionUID = -7473460596415937558L;

    @Schema(description = "姓名")
    private String userName;

    @Schema(description = "手机好吗")
    private String userMobile;

    private String orgNames;

    public MemberDTO(String userMobile, String userName, String orgNames) {
        this.orgNames = orgNames;
        this.userMobile = userMobile;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getOrgNames() {
        return orgNames;
    }

    public void setOrgNames(String orgNames) {
        this.orgNames = orgNames;
    }

    /**
     * 将组织名称转为列表。
     *
     * @return 组织名称列表
     */
    @Schema(description = "组织名")
    @JsonProperty(value = "orgNames", access = JsonProperty.Access.READ_ONLY)
    public List<String> formatOrgNames() {

        if (this.orgNames == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(this.orgNames.split(","));
    }
}
