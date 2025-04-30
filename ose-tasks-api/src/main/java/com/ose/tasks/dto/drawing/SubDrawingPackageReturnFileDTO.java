package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 生产设计图纸清单导入数据传输对象。
 */
public class SubDrawingPackageReturnFileDTO extends BaseDTO {

    private static final long serialVersionUID = 3088893574043870018L;

    @Schema(description = "文件id")
    private String fileId;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "文件path")
    private String filePath;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
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
}
