package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 临时文件信息。
 */
public class TaskTemporaryFileDTO extends BaseDTO {

    private static final long serialVersionUID = -4059288973826028452L;

    @Schema(description = "临时文件名")
    private String name;

    public TaskTemporaryFileDTO() {
    }

    public TaskTemporaryFileDTO(String name) {
        this();
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
