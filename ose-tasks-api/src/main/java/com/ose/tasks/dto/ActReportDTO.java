package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class ActReportDTO extends BaseDTO {

    private static final long serialVersionUID = 5971440931157335574L;

    @Schema(description = "报告Id QrCode")
    private String reportQrCode;

    @Schema(description = "文件Id")
    private Long fileId;

    @Schema(description = "报告NO")
    private String reportNo;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "报告类型")
    private String reportType;

    @Schema(description = "报告 序列号 seriesNo")
    private String seriesNo;

    @Schema(description = " is Cover")
    private Boolean isCover;

    @Schema(description = "报告对应的工序类型")
    private String process;

    @Schema(description = "图集编号")
    private String drawingNo;

    @Schema(description = "图集名称")
    private String drawingName;

    public String getReportQrCode() {
        return reportQrCode;
    }

    public void setReportQrCode(String reportQrCode) {
        this.reportQrCode = reportQrCode;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Boolean getCover() {
        return isCover;
    }

    public void setCover(Boolean cover) {
        isCover = cover;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getDrawingName() {
        return drawingName;
    }

    public void setDrawingName(String drawingName) {
        this.drawingName = drawingName;
    }
}
