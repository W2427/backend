package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class IssueImportDTO extends BaseDTO {

    private static final long serialVersionUID = 3694673369519436093L;

    @Schema(description = "临时文件名")
    @NotNull
    @NotEmpty
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
