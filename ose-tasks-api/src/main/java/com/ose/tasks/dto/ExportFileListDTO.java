package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ExportFileListDTO extends BaseDTO {

    private static final long serialVersionUID = 5127019539493393875L;

    @Schema(description = "单个文件对象")
    private List<ExportFileDTO> fileList;

    public List<ExportFileDTO> getFileList() {
        return fileList;
    }

    public void setFileList(List<ExportFileDTO> fileList) {
        this.fileList = fileList;
    }
}
