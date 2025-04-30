package com.ose.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;
import java.util.List;

public class UserNameCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = 73682920729004862L;

    @Schema(description = "用户名")
    private List<String> userNames;

    @JsonCreator
    public UserNameCriteriaDTO() {
    }

    public UserNameCriteriaDTO(String username) {
        this.userNames = Arrays.asList(username.split(","));
    }

    public List<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }

}
