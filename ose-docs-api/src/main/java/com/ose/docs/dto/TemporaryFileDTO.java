package com.ose.docs.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 临时文件信息。
 */
public class TemporaryFileDTO extends BaseDTO {

    private static final long serialVersionUID = -4059288973826028452L;

    @Schema(description = "临时文件名")
    private String name;

    @Schema(description = "临时文件名")
    private String realName;

    public TemporaryFileDTO() {
    }

    public TemporaryFileDTO(String name) {
        this();
        setName(name);
    }
    public TemporaryFileDTO(String name, String realName) {
        this();
        setName(name);
        setRealName(realName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
