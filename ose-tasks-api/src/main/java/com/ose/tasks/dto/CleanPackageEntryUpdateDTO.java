package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.setting.CleanMethodType;
import com.ose.vo.unit.PressureUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * 清洁包实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CleanPackageEntryUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = -7649103238237268861L;

    @Schema(description = "版本号")
    private String revision;

    @Schema(description = "清洁压力")
    private Double cleanPressure;

    @Schema(description = "清洁压力单位")
    @Enumerated(EnumType.STRING)
    private PressureUnit cleanPressureUnit;

    @Schema(description = "清洁介质")
    private String cleanMedium;

    @Schema(description = "清洁包文件号")
    private String clpDrawingNo;

    @Schema(description = "清洁方式")
    @Enumerated(EnumType.STRING)
    private CleanMethodType cleanMethod;

    @Schema(description = "备注")
    private String remarks;

    @JsonCreator
    public CleanPackageEntryUpdateDTO() {
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Double getCleanPressure() {
        return cleanPressure;
    }

    public void setCleanPressure(Double cleanPressure) {
        this.cleanPressure = cleanPressure;
    }

    public PressureUnit getCleanPressureUnit() {
        return cleanPressureUnit;
    }

    public void setCleanPressureUnit(PressureUnit cleanPressureUnit) {
        this.cleanPressureUnit = cleanPressureUnit;
    }

    public String getCleanMedium() {
        return cleanMedium;
    }

    public void setCleanMedium(String cleanMedium) {
        this.cleanMedium = cleanMedium;
    }

    public String getClpDrawingNo() {
        return clpDrawingNo;
    }

    public void setClpDrawingNo(String clpDrawingNo) {
        this.clpDrawingNo = clpDrawingNo;
    }

    public CleanMethodType getCleanMethod() {
        return cleanMethod;
    }

    public void setCleanMethod(CleanMethodType cleanMethod) {
        this.cleanMethod = cleanMethod;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
