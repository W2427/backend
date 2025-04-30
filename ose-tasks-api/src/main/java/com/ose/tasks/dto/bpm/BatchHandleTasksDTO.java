package com.ose.tasks.dto.bpm;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ose.dto.BaseDTO;

/**
 * 实体管理 数据传输对象
 */
public class BatchHandleTasksDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    //任务id,123456
    private List<String> ids;

    // 实际工时
    private int costHour;

    //附件
    private List<String> attachFiles;

    //备注
    private String comment;

    //附件-图片
    private List<String> pictures;

    //操作命令
    private Map<String, Map<String, Object>> commands;

    private String reportId;

    private String reportName;

    private Long reportFileId;

    private String reportSerial;

    private List<Map<String, Object>> variables;

    //外检时间
    private Date externalInspectionTime;

    //内检时间
    private Date internalInspectionTime;

    private Map<String, String> externalStatus;

    public int getCostHour() {
        return costHour;
    }

    public void setCostHour(int costHour) {
        this.costHour = costHour;
    }

    public List<String> getAttachFiles() {
        return attachFiles;
    }

    public void setAttachFiles(List<String> attachFiles) {
        this.attachFiles = attachFiles;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Long getReportFileId() {
        return reportFileId;
    }

    public void setReportFileId(Long reportFileId) {
        this.reportFileId = reportFileId;
    }

    public String getReportSerial() {
        return reportSerial;
    }

    public void setReportSerial(String reportSerial) {
        this.reportSerial = reportSerial;
    }

    public List<Map<String, Object>> getVariables() {
        return variables;
    }

    public void setVariables(List<Map<String, Object>> variables) {
        this.variables = variables;
    }

    public Date getExternalInspectionTime() {
        return externalInspectionTime;
    }

    public void setExternalInspectionTime(Date externalInspectionTime) {
        this.externalInspectionTime = externalInspectionTime;
    }

    public Date getInternalInspectionTime() {
        return internalInspectionTime;
    }

    public void setInternalInspectionTime(Date internalInspectionTime) {
        this.internalInspectionTime = internalInspectionTime;
    }

    public Map<String, String> getExternalStatus() {
        return externalStatus;
    }

    public void setExternalStatus(Map<String, String> externalStatus) {
        this.externalStatus = externalStatus;
    }

    public Map<String, Map<String, Object>> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, Map<String, Object>> commands) {
        this.commands = commands;
    }

}
