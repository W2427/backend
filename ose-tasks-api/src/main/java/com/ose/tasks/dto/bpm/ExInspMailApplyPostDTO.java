package com.ose.tasks.dto.bpm;

import java.util.Date;
import java.util.List;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 数据传输对象
 */
public class ExInspMailApplyPostDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "外检处理列表")
    private List<Long> externalInspectionApplyScheduleIds;

    @Schema(description = "发件人")
    private String fromMail;

    @Schema(description = "收件人")
    private String toMail;

    @Schema(description = "转送件人")
    private String ccMail;

    @Schema(description = "主题")
    private String subject;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "时间")
    private Date time;

    public List<Long> getExternalInspectionApplyScheduleIds() {
        return externalInspectionApplyScheduleIds;
    }

    public void setExternalInspectionApplyScheduleIds(List<Long> externalInspectionApplyScheduleIds) {
        this.externalInspectionApplyScheduleIds = externalInspectionApplyScheduleIds;
    }

    public String getFromMail() {
        return fromMail;
    }

    public void setFromMail(String fromMail) {
        this.fromMail = fromMail;
    }

    public String getToMail() {
        return toMail;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
    }

    public String getCcMail() {
        return ccMail;
    }

    public void setCcMail(String ccMail) {
        this.ccMail = ccMail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
