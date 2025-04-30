package com.ose.tasks.dto.rapairData;

import com.ose.dto.BaseDTO;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.List;

/**
 * 修改材料信息。
 */
public class RepairMaterialDTO extends BaseDTO {

    private static final long serialVersionUID = -7187095797677233761L;

    @Schema(description = "炉号")
    private String heatNo;

    @Schema(description = "批号")
    private String batchNo;

    @Schema(description = "新NPS")
    private String nps;

    @Schema(description = "NPS 单位")
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

    @Schema(description = "放行单id")
    private Long releaseNoteId;

    @Schema(description = "放行单详情id")
    private Long releaseNoteDetailId;

    @Schema(description = "放行单详情明细id")
    private Long releaseNoteDetailItemId;

    private List<String> entityNos;

    @Schema(description = "备注")
    private String remark;

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

    public String getNps() {
        return nps;
    }

    public void setNps(String nps) {
        this.nps = nps;
    }

    public LengthUnit getNpsUnit() {
        return npsUnit;
    }

    public void setNpsUnit(LengthUnit npsUnit) {
        this.npsUnit = npsUnit;
    }

    public Long getReleaseNoteId() {
        return releaseNoteId;
    }

    public void setReleaseNoteId(Long releaseNoteId) {
        this.releaseNoteId = releaseNoteId;
    }

    public Long getReleaseNoteDetailId() {
        return releaseNoteDetailId;
    }

    public void setReleaseNoteDetailId(Long releaseNoteDetailId) {
        this.releaseNoteDetailId = releaseNoteDetailId;
    }

    public Long getReleaseNoteDetailItemId() {
        return releaseNoteDetailItemId;
    }

    public void setReleaseNoteDetailItemId(Long releaseNoteDetailItemId) {
        this.releaseNoteDetailItemId = releaseNoteDetailItemId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getEntityNos() {
        return entityNos;
    }

    public void setEntityNos(List<String> entityNos) {
        this.entityNos = entityNos;
    }
}
