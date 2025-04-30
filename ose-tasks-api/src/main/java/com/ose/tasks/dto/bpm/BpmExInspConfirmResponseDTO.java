package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 传输对象
 */
public class BpmExInspConfirmResponseDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "批量执行是否成功")
    private Boolean successful = true;

    private List<ConfirmResponseDTO> responseDetails;

    public class ConfirmResponseDTO {
        @Schema(description = "报告号")
        private String reportNo;

        @Schema(description = "执行结果信息")
        private String msg;

        public String getReportNo() {
            return reportNo;
        }

        public void setReportNo(String reportNo) {
            this.reportNo = reportNo;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }


    public Boolean getSuccessful() {
        return successful;
    }


    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }


    public List<ConfirmResponseDTO> getResponseDetails() {
        return responseDetails;
    }


    public void setResponseDetails(List<ConfirmResponseDTO> responseDetails) {
        this.responseDetails = responseDetails;
    }
}
