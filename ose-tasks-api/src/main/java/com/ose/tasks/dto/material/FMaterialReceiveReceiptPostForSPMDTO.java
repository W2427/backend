package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 入库清单DTO
 */
public class FMaterialReceiveReceiptPostForSPMDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "开箱检验单ID")
    private Long obirId;

    @Schema(description = "是否反写spm")
    private Boolean spmFlag;

    public Long getObirId() {
        return obirId;
    }

    public void setObirId(Long obirId) {
        this.obirId = obirId;
    }

    public Boolean getSpmFlag() {
        return spmFlag;
    }

    public void setSpmFlag(Boolean spmFlag) {
        this.spmFlag = spmFlag;
    }
}
