package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class AddOrganizationMemberDTO extends BaseDTO {

    private static final long serialVersionUID = 4789553382761976024L;

    @Schema(description = "用户 ID")
    private Long memberId;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
