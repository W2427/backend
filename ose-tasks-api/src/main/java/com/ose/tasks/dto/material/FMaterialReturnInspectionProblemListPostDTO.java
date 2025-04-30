package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 创建质量问题列表DTO
 */
public class FMaterialReturnInspectionProblemListPostDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "二维码 + 数量的列表 + 备注")
    private List<FMaterialReturnInspectionProblemPostDTO> qrCodeAndCntList;

    public List<FMaterialReturnInspectionProblemPostDTO> getQrCodeAndCntList() {
        return qrCodeAndCntList;
    }

    public void setQrCodeAndCntList(List<FMaterialReturnInspectionProblemPostDTO> qrCodeAndCntList) {
        this.qrCodeAndCntList = qrCodeAndCntList;
    }
}
