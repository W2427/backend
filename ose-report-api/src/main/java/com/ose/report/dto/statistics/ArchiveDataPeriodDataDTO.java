package com.ose.report.dto.statistics;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ose.constant.JsonFormatPattern;
import com.ose.dto.BaseDTO;
import com.ose.report.entity.statistics.ArchiveDataBase;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

/**
 * 归档数据期间列表数据传输对象。
 */
public class ArchiveDataPeriodDataDTO extends BaseDTO {

    private static final long serialVersionUID = -6586887926411563320L;

    @Schema(description = "总和")
    private ArchiveDataBase sum;

    @Schema(description = "所选期间之前的累计值")
    private ArchiveDataBase accumulation;

    @Schema(description = "期间列表")
    private List<? extends ArchiveDataBase> periods;

    @Schema(description = "最后统计时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    Date archiveDateTime;

    public ArchiveDataPeriodDataDTO() {
    }

    public ArchiveDataPeriodDataDTO(
        ArchiveDataBase sum,
        ArchiveDataBase accumulation,
        List<? extends ArchiveDataBase> periods
    ) {
        setSum(sum);
        setAccumulation(accumulation);
        setPeriods(periods);
    }

    public ArchiveDataPeriodDataDTO(
        ArchiveDataBase sum,
        ArchiveDataBase accumulation,
        List<? extends ArchiveDataBase> periods,
        Date archiveDateTime
    ) {
        setSum(sum);
        setAccumulation(accumulation);
        setPeriods(periods);
        setArchiveDateTime(archiveDateTime);
    }

    public ArchiveDataBase getSum() {
        return sum;
    }

    public void setSum(ArchiveDataBase sum) {
        this.sum = sum;
    }

    public ArchiveDataBase getAccumulation() {
        return accumulation;
    }

    public void setAccumulation(ArchiveDataBase accumulation) {
        this.accumulation = accumulation;
    }

    public List<? extends ArchiveDataBase> getPeriods() {
        return periods;
    }

    public void setPeriods(List<? extends ArchiveDataBase> periods) {
        this.periods = periods;
    }

    public Date getArchiveDateTime() {
        return archiveDateTime;
    }

    public void setArchiveDateTime(Date archiveDateTime) {
        this.archiveDateTime = archiveDateTime;
    }
}
