package com.ose.prints.dto;

import java.util.List;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 上传文件的附加信息。
 */
public class SpoolQrCodeDTO extends BaseDTO {


    private static final long serialVersionUID = 8194574818310660466L;

    @Schema(description = "材料编码")
    private List<SpoolQrCodeDetailDTO> datas;

    /**
     * 构造方法。
     */
    public SpoolQrCodeDTO() {
    }

    public List<SpoolQrCodeDetailDTO> getDatas() {
        return datas;
    }

    public void setDatas(List<SpoolQrCodeDetailDTO> datas) {
        this.datas = datas;
    }

}
