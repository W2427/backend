package com.ose.tasks.dto.performance;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.io.Serial;

public class PerformanceAppraisalListImportDTO extends BaseDTO {
    private static final long serialVersionUID = 4638499456446351821L;

    @Schema(description = "上传的导入文件的临时文件名")
    private String fileName;

    private Integer quarter;

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
