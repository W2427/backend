package com.ose.report.dto.statistics;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 对象数据期间列表及聚合值列表。
 */
public class ArchiveDataPeriodsDTO extends BaseDTO {

    private static final long serialVersionUID = -6511871917327688409L;

    @Schema(description = "对象数据期间列表")
    private List<ArchiveTimeDTO> periods;

    @Schema(description = "聚合值列表")
    private List<ArchiveDataGroupKeyDTO> keys;

    public ArchiveDataPeriodsDTO() {
    }

    public ArchiveDataPeriodsDTO(
        List<ArchiveTimeDTO> periods,
        List<ArchiveDataGroupKeyDTO> keys
    ) {
        setPeriods(periods);
        setKeys(keys);
    }

    public List<ArchiveTimeDTO> getPeriods() {
        return periods;
    }

    public void setPeriods(List<ArchiveTimeDTO> periods) {
        this.periods = periods;
    }

    public List<ArchiveDataGroupKeyDTO> getKeys() {
        return keys;
    }

    public void setKeys(List<ArchiveDataGroupKeyDTO> keys) {
        this.keys = keys;
    }

}
