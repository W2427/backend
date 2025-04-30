package com.ose.tasks.dto.drawing;

import com.ose.tasks.dto.bpm.BaseBatchTaskCriteriaDTO;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import com.ose.vo.DrawingFileType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 传输对象
 */
public class DrawingUploadDTO extends BaseBatchTaskCriteriaDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "备注")
    private String comment;

    @Schema(description = "图纸详情id")
    private Long drawingDetailId;

    @Schema(description = "报告流水号")
    private String seriesNo;

    private DrawingFileType fileType;

    @Schema(description = "临时文件名")
    private String fileName;

    @Schema(description = "任务id,123456 bigint 19")
    private Long taskId;

    @Schema(description = "流程ID， 3112121")
//    private Long actInstId;
    private Long procInstId;

    @Schema(description = "redmark版本")
    private String version;

    @Schema(description = "文件真实名称")
    private String realFileName;

    @Schema(description = "文件扩展名")
    private String   fileExtensionType;

    @Schema(description = "图纸条形码ID")
    private Long  drawingCoordinateId;

    @Schema(description = "是否是最新版，最新版Y，否则N")
    private String  isLatestRev;

    public String getRealFileName() {
        return realFileName;
    }

    public void setRealFileName(String realFileName) {
        this.realFileName = realFileName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getDrawingDetailId() {
        return drawingDetailId;
    }

    public void setDrawingDetailId(Long drawingDetailId) {
        this.drawingDetailId = drawingDetailId;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(Long procInstId) {
        this.procInstId = procInstId;
    }

    public DrawingFileType getFileType() {
        return fileType;
    }

    public void setFileType(DrawingFileType fileType) {
        this.fileType = fileType;
    }

    public String getFileExtensionType() {
        return fileExtensionType;
    }

    public void setFileExtensionType(String fileExtensionType) {
        this.fileExtensionType = fileExtensionType;
    }

    public Long getDrawingCoordinateId() {
        return drawingCoordinateId;
    }

    public void setDrawingCoordinateId(Long drawingCoordinateId) {
        this.drawingCoordinateId = drawingCoordinateId;
    }

    public String getIsLatestRev() {
        return isLatestRev;
    }

    public void setIsLatestRev(String isLatestRev) {
        this.isLatestRev = isLatestRev;
    }
}
