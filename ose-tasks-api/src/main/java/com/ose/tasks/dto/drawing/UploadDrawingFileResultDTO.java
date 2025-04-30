package com.ose.tasks.dto.drawing;

import java.util.List;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 生产设计图纸清单导入数据传输对象。
 */
public class UploadDrawingFileResultDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "成功个数")
    private Integer successCount;

    @Schema(description = "失败个数")
    private Integer errorCount;

    @Schema(description = "修复图纸错误列表")
    private List<ErrorObject> errorListForRepairing;

    @Schema(description = "错误列表")
    private List<String> errorList;

    public static class ErrorObject {
        public String name;
        public String content;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

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

    public List<ErrorObject> getErrorListForRepairing() {
        return errorListForRepairing;
    }

    public void setErrorListForRepairing(List<ErrorObject> errorListForRepairing) {
        this.errorListForRepairing = errorListForRepairing;
    }
}
