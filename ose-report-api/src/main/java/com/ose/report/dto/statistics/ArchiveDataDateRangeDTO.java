package com.ose.report.dto.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 归档对象数据期间数据传输对象。
 */
public class ArchiveDataDateRangeDTO extends BaseDTO {

    private static final long serialVersionUID = -1036253082536160861L;

    private static final Pattern PERIOD_PATTERN = Pattern.compile("^(\\d+)((-(\\d+)(-(\\d+))?)|(/(\\d+)))$");

    @Schema(description = "起始时间")
    private String dateFrom;

    @JsonIgnore
    @Schema(hidden = true)
    private ArchiveTimeDTO dateFromTime;

    @Schema(description = "截止时间")
    private String dateUntil;

    @JsonIgnore
    @Schema(hidden = true)
    private ArchiveTimeDTO dateUntilTime;

    private static ArchiveTimeDTO toArchiveTimeDTO(String date, int fromUntil) {

        Matcher m = PERIOD_PATTERN.matcher(date);

        if (!m.matches()) {
            return null;
        }

        return new ArchiveTimeDTO(
            m.group(1) == null ? 0 : Integer.parseInt(m.group(1)),
            m.group(4) == null ? 0 : Integer.parseInt(m.group(4)),
            m.group(6) == null ? 0 : Integer.parseInt(m.group(6)),
            m.group(8) == null ? 0 : Integer.parseInt(m.group(8)),
            fromUntil
        );
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
        this.dateFromTime = toArchiveTimeDTO(dateFrom, ArchiveTimeDTO.FROM);
    }

    public ArchiveTimeDTO getDateFromTime() {
        return dateFromTime;
    }

    public String getDateUntil() {
        return dateUntil;
    }

    public void setDateUntil(String dateUntil) {
        this.dateUntil = dateUntil;
        this.dateUntilTime = toArchiveTimeDTO(dateUntil, ArchiveTimeDTO.UNTIL);
    }

    public ArchiveTimeDTO getDateUntilTime() {
        return dateUntilTime;
    }

    public void setDateUntilTime(ArchiveTimeDTO dateUntilTime) {
        this.dateUntilTime = dateUntilTime;
    }
}
