package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 子系统实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubSystemEntryUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = -3255252687113223676L;

    @Schema(description = "是否执行气密程序")
    private Boolean airTightness;

    @Schema(description = "是否执行完工检查")
    private Boolean checkMC;

    @Schema(description = "备注")
    private String remarks;

    @JsonCreator
    public SubSystemEntryUpdateDTO() {
    }

    public Boolean getAirTightness() {
        return airTightness;
    }

    public void setAirTightness(Boolean airTightness) {
        this.airTightness = airTightness;
    }

    public Boolean getCheckMC() {
        return checkMC;
    }

    public void setCheckMC(Boolean checkMC) {
        this.checkMC = checkMC;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
