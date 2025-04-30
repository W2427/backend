package com.ose.tasks.entity.qc;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * 焊口质量控制实施记录。
 */
@MappedSuperclass
public abstract class WeldingConstructionLog extends BaseConstructionLog {

    private static final long serialVersionUID = 4134546130541755715L;

    @Schema(description = "焊工 ID")
    @Column
    private Long welderId;

    @Schema(description = "外检结果")
    @Column
    private String externalInspectionResult;


    @Schema(description = "检验员ID")
    @Column
    private Long inspectorId;

    public WeldingConstructionLog() {
        super();
    }

    public Long getWelderId() {
        return welderId;
    }

    public void setWelderId(Long welderId) {
        this.welderId = welderId;
    }

    public String getExternalInspectionResult() {
        return externalInspectionResult;
    }

    public void setExternalInspectionResult(String externalInspectionResult) {
        this.externalInspectionResult = externalInspectionResult;
    }

    public Long getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(Long inspectorId) {
        this.inspectorId = inspectorId;
    }
}
