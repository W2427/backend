package com.ose.material.dto;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 批处理执行结果。
 */
public class MmMaterialCertificateDTO extends BaseDTO {

    private static final long serialVersionUID = -1047170922851880436L;

    @Schema(description = "证书编号")
    private String no;

    @Schema(description = "证书文件地址")
    private String path;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
