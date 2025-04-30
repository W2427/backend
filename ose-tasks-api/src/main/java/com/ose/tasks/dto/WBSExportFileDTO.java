package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * WBS 导出文件数据传输对象。
 */
public class WBSExportFileDTO extends BaseDTO {

    private static final long serialVersionUID = -6248762176127055595L;

    @Schema(description = "文件 ID")
    private Long fileId;

    public WBSExportFileDTO() {
    }

    public WBSExportFileDTO(Long fileId) {
        setFileId(fileId);
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

}
