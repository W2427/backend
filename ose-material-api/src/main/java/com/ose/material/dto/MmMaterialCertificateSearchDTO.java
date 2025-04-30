package com.ose.material.dto;
import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 */
public class MmMaterialCertificateSearchDTO extends PageDTO {

    private static final long serialVersionUID = -1047170922851880436L;

    @Schema(description = "搜索关键字")
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
