package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 入库材料二维码查询DTO
 */
public class MmReleaseReceiveBatchUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = 2864527109289394407L;
    @Schema(description = "二维码")
    public List<MmReleaseReceiveBatchUpdateDetailDTO> mmReleaseReceiveBatchUpdateDetailDTOList;

    @Schema(description = "在检验中")
    public Boolean inExternalQuality;

    public List<MmReleaseReceiveBatchUpdateDetailDTO> getMmReleaseReceiveBatchUpdateDetailDTOList() {
        return mmReleaseReceiveBatchUpdateDetailDTOList;
    }

    public void setMmReleaseReceiveBatchUpdateDetailDTOList(List<MmReleaseReceiveBatchUpdateDetailDTO> mmReleaseReceiveBatchUpdateDetailDTOList) {
        this.mmReleaseReceiveBatchUpdateDetailDTOList = mmReleaseReceiveBatchUpdateDetailDTOList;
    }

    public Boolean getInExternalQuality() {
        return inExternalQuality;
    }

    public void setInExternalQuality(Boolean inExternalQuality) {
        this.inExternalQuality = inExternalQuality;
    }
}
