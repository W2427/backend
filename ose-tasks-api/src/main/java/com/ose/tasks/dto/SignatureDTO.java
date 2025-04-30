package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 二维码创建表单。
 */
public class SignatureDTO extends BaseDTO {

    private static final long serialVersionUID = 3306151730639645725L;

    @Schema(description = "上传临时文件名")
    private String fileName;

    @Schema(description = "用户 ID")
    private Long userId;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
