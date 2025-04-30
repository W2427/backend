package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 创建配送清单DTO
 */
public class FMaterialTransfersPostDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "领料单ID")
    private Long fmreqId;

    @Schema(description = "目的地")
    private String receiveSite;

    @Schema(description = "接收人")
    private String receivePerson;

    public Long getFmreqId() {
        return fmreqId;
    }

    public void setFmreqId(Long fmreqId) {
        this.fmreqId = fmreqId;
    }

    public String getReceiveSite() {
        return receiveSite;
    }

    public void setReceiveSite(String receiveSite) {
        this.receiveSite = receiveSite;
    }

    public String getReceivePerson() {
        return receivePerson;
    }

    public void setReceivePerson(String receivePerson) {
        this.receivePerson = receivePerson;
    }

}
