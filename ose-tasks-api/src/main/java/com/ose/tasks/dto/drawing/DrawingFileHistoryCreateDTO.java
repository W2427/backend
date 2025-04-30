package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 图纸文件历史创建 数据传输对象
 */
public class DrawingFileHistoryCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 1840641055697118897L;

    @Schema(description = "图纸任务流程ID")
    private Long procInstId;

    @Schema(description = "图纸任务完成历史ID")
    private Long taskId;

    @Schema(description = "图纸文件ID")
    private Long fileId;

    @Schema(description = "图纸文件名")
    private String fileName;

    @Schema(description = "图纸文件存放路径")
    private String filePath;

    @Schema(description = "图纸版本")
    private String rev;

    @Schema(description = "上传人")
    private String user;

    public Long getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(Long procInstId) {
        this.procInstId = procInstId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
