package com.ose.tasks.dto.wps;


import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class WelderTypeDTO extends BaseDTO {


    private static final long serialVersionUID = -8400306385096393548L;
    @Schema(description = "焊工焊接位置 打底 填充 盖面")
    private String weldProcess;

    @Schema(description = "焊工号 逗号 分割")
    private String welders;

    public String getWeldProcess() {
        return weldProcess;
    }

    public void setWeldProcess(String weldProcess) {
        this.weldProcess = weldProcess;
    }

    public String getWelders() {
        return welders;
    }

    public void setWelders(String welders) {
        this.welders = welders;
    }
}
