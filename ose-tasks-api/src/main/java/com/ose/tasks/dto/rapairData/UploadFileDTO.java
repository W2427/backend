package com.ose.tasks.dto.rapairData;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 修改项目数据接口。
 */
public class UploadFileDTO extends BaseDTO {

    private static final long serialVersionUID = 8136639584439208871L;

    @Schema(description = "")
    private List<String> fileId;

    public List<String> getFileId() {
        return fileId;
    }

    public void setFileId(List<String> fileId) {
        this.fileId = fileId;
    }
}
