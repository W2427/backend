package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class WeightsUpdateDTO extends BaseDTO {

    @Schema(description = "CUTTING权重")
    private Integer cuttingWeights;

    @Schema(description = "FIT-UP权重")
    private Integer fitUpWeights;

    @Schema(description = "WELDING权重")
    private Integer weldingWeights;

    @Schema(description = "NDT权重")
    private Integer ndtWeights;

    @Schema(description = "RELEASE权重")
    private Integer releaseWeights;

    public Integer getCuttingWeights() {
        return cuttingWeights;
    }

    public void setCuttingWeights(Integer cuttingWeights) {
        this.cuttingWeights = cuttingWeights;
    }

    public Integer getFitUpWeights() {
        return fitUpWeights;
    }

    public void setFitUpWeights(Integer fitUpWeights) {
        this.fitUpWeights = fitUpWeights;
    }

    public Integer getWeldingWeights() {
        return weldingWeights;
    }

    public void setWeldingWeights(Integer weldingWeights) {
        this.weldingWeights = weldingWeights;
    }

    public Integer getNdtWeights() {
        return ndtWeights;
    }

    public void setNdtWeights(Integer ndtWeights) {
        this.ndtWeights = ndtWeights;
    }

    public Integer getReleaseWeights() {
        return releaseWeights;
    }

    public void setReleaseWeights(Integer releaseWeights) {
        this.releaseWeights = releaseWeights;
    }
}
