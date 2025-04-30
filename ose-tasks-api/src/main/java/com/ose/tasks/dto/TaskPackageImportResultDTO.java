package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class TaskPackageImportResultDTO extends BaseDTO {

    private static final long serialVersionUID = 416368325318697791L;

    @Schema(description = "错误历史")
    private List<String> result;

    @Schema(description = "跳过数量")
    private int skipCount;

    @Schema(description = "失败数量")
    private int errorCount;

    @Schema(description = "成功数量")
    private int successCount;

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    public int getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(int skipCount) {
        this.skipCount = skipCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
}
