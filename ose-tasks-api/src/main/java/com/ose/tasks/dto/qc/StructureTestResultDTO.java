package com.ose.tasks.dto.qc;

import io.swagger.v3.oas.annotations.media.Schema;

public class StructureTestResultDTO extends TestResultDTO {


    private static final long serialVersionUID = 2556809104974661184L;

    @Schema(description = "材料1 炉批号")
    private String heatNo1;

    @Schema(description = "材料2 炉批号")
    private String heatNo2;

    @Schema(description = "材料1")
    private String material1;

    @Schema(description = "材料2")
    private String material2;

    @Schema(description = "焊后热处理编号")
    private String pwhtCode;

    @Schema(description = "焊工 IDs,逗号分隔")
    private String executors;

    public String getHeatNo1() {
        return heatNo1;
    }

    public void setHeatNo1(String heatNo1) {
        this.heatNo1 = heatNo1;
    }

    public String getHeatNo2() {
        return heatNo2;
    }

    public void setHeatNo2(String heatNo2) {
        this.heatNo2 = heatNo2;
    }

    public String getMaterial1() {
        return material1;
    }

    public void setMaterial1(String material1) {
        this.material1 = material1;
    }

    public String getMaterial2() {
        return material2;
    }

    public void setMaterial2(String material2) {
        this.material2 = material2;
    }

    public String getPwhtCode() {
        return pwhtCode;
    }

    public void setPwhtCode(String pwhtCode) {
        this.pwhtCode = pwhtCode;
    }

    public String getExecutors() {
        return executors;
    }

    public void setExecutors(String executors) {
        this.executors = executors;
    }
}
