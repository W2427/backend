package com.ose.tasks.dto.wps;


import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class WeldWelderRelationCreateItemDTO extends BaseDTO {

    private static final long serialVersionUID = 216956448248427978L;

    @Schema(description = "焊工 ID")
    private Long welderId;

    @Schema(description = "焊工编号")
    private String welderNo;

    @Schema(description = "焊口 ID")
    private Long weldId;

    @Schema(description = "焊口编号")
    private String weldNo;

    @Schema(description = "工序（打底A、填充B、盖面C）")
    private String process;

    public Long getWelderId() {
        return welderId;
    }

    public void setWelderId(Long welderId) {
        this.welderId = welderId;
    }

    public String getWelderNo() {
        return welderNo;
    }

    public void setWelderNo(String welderNo) {
        this.welderNo = welderNo;
    }

    public Long getWeldId() {
        return weldId;
    }

    public void setWeldId(Long weldId) {
        this.weldId = weldId;
    }

    public String getWeldNo() {
        return weldNo;
    }

    public void setWeldNo(String weldNo) {
        this.weldNo = weldNo;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
