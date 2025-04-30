package com.ose.tasks.dto.material;

import java.util.List;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO
 */
public class MaterialImportFileResultDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "成功个数")
    private Integer successCount;

    @Schema(description = "失败个数")
    private Integer errorCount;

    @Schema(description = "错误列表")
    private List<ErrorInfoDTO> errorList;

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

    public List<ErrorInfoDTO> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<ErrorInfoDTO> errorList) {
        this.errorList = errorList;
    }

    public class ErrorInfoDTO {

        @Schema(description = "行号")
        private Integer rowNo;

        @Schema(description = "错误信息")
        private String errorInfo;

        public ErrorInfoDTO(Integer rowNo, String errorInfo) {
            super();
            this.rowNo = rowNo;
            this.errorInfo = errorInfo;
        }

        public Integer getRowNo() {
            return rowNo;
        }

        public void setRowNo(Integer rowNo) {
            this.rowNo = rowNo;
        }

        public String getErrorInfo() {
            return errorInfo;
        }

        public void setErrorInfo(String errorInfo) {
            this.errorInfo = errorInfo;
        }

    }
}


