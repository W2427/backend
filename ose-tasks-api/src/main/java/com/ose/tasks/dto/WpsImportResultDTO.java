package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 生产设计图纸清单导入数据传输对象。
 */
public class WpsImportResultDTO extends BaseDTO {

    private static final long serialVersionUID = -5678238630841928268L;

    @Schema(description = "成功个数")
    private Integer successCount;

    @Schema(description = "失败个数")
    private Integer errorCount;

    @Schema(description = "错误列表")
    private List<String> errorList;

    @Schema(description = "跳过个数")
    private Integer skipCount;

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }

    public Integer getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(Integer skipCount) {
        this.skipCount = skipCount;
    }
}
