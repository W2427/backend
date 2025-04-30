package com.ose.tasks.dto.rapairData;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 修改项目数据接口。
 */
public class RepairHeatDTO extends BaseDTO {

    private static final long serialVersionUID = -4816158120384513560L;

    @Schema(description = "材料二维码")
    private String qrCode;

    @Schema(description = "新炉号")
    private String heatNo;

    @Schema(description = "新批号")
    private String batchNo;

    @Schema(description = "工序名称")
    private String processNameEn;

    @Schema(description = "下一个工序名称")
    private String nextProcessNameEn;

    @Schema(description = "工序阶段 名称")
    private String processStageNameEn;

    @Schema(description = "专业")
    private String discipline;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getProcessNameEn() {
        return processNameEn;
    }

    public void setProcessNameEn(String processNameEn) {
        this.processNameEn = processNameEn;
    }

    public String getProcessStageNameEn() {
        return processStageNameEn;
    }

    public void setProcessStageNameEn(String processStageNameEn) {
        this.processStageNameEn = processStageNameEn;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getNextProcessNameEn() {
        return nextProcessNameEn;
    }

    public void setNextProcessNameEn(String nextProcessNameEn) {
        this.nextProcessNameEn = nextProcessNameEn;
    }
}
