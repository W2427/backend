package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IssueRecordCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = -7186402168755662323L;

    @Schema(description = "查询开始时间")
    private String startTime;

    @Schema(description = "查询结束时间")
    private String endTime;

    public Date getStartTime() {

        Date startTime = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (this.startTime == null) {
            return null;
        }

        try {
            startTime = formatter.parse(this.startTime);
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        }

        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {

        Date endTime = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (this.endTime == null) {
            return null;
        }

        try {
            endTime = formatter.parse(this.endTime);
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        }

        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
