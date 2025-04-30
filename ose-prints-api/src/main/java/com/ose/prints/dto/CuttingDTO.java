package com.ose.prints.dto;

import java.util.List;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 上传文件的附加信息。
 */
public class CuttingDTO extends BaseDTO {


    private static final long serialVersionUID = 8194574818310660466L;

    @Schema(description = "详细")
    private List<CuttingDetailDTO> datas;

    public List<CuttingDetailDTO> getDatas() {
        return datas;
    }

    public void setDatas(List<CuttingDetailDTO> datas) {
        this.datas = datas;
    }

}
