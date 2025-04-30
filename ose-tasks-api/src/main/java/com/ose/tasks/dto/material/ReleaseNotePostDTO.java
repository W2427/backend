package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.qc.ReportSubType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 放行单查询DTO
 */
public class ReleaseNotePostDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "SPM 计划放行单号")
    private String relnNumber;

    @Schema(description = "验收报告类型")
    private ReportSubType reportSubType;

    @Schema(description = "材料批次号")
    private String materialBatchNumber;

    @Schema(description = "材料放行量是否是0的FLAG，false: 不是0; true:是0 ")
    private boolean relnQtyZeroFlg = false;

    public ReportSubType getReportSubType() {
        return reportSubType;
    }

    public void setReportSubType(ReportSubType reportSubType) {
        this.reportSubType = reportSubType;
    }

    public String getRelnNumber() {
        return relnNumber;
    }

    public void setRelnNumber(String relnNumber) {
        this.relnNumber = relnNumber;
    }

    public String getMaterialBatchNumber() {
        return materialBatchNumber;
    }

    public void setMaterialBatchNumber(String materialBatchNumber) {
        this.materialBatchNumber = materialBatchNumber;
    }

    public boolean isRelnQtyZeroFlg() {
        return relnQtyZeroFlg;
    }

    public void setRelnQtyZeroFlg(boolean relnQtyZeroFlg) {
        this.relnQtyZeroFlg = relnQtyZeroFlg;
    }
}
