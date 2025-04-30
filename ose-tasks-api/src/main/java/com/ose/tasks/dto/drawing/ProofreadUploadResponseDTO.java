package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 传输对象
 */
public class ProofreadUploadResponseDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "批量上传是否成功")
    private Boolean successful;

    private List<ConfirmResponseDTO> responseDetails;

    public class ConfirmResponseDTO {
        @Schema(description = "子图纸编号")
        private String subDrawingNo;

        @Schema(description = "执行结果信息")
        private String msg;

        public ConfirmResponseDTO() {
        }

        public ConfirmResponseDTO(String subDrawingNo, String msg) {
            this.subDrawingNo = subDrawingNo;
            this.msg = msg;
        }

        public String getSubDrawingNo() {
            return subDrawingNo;
        }

        public void setSubDrawingNo(String subDrawingNo) {
            this.subDrawingNo = subDrawingNo;
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
