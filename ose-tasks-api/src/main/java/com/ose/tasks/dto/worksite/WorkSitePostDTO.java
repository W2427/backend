package com.ose.tasks.dto.worksite;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 工作场地更新数据传输对象。
 */
public class WorkSitePostDTO extends WorkSiteSortDTO {

    private static final long serialVersionUID = 4065293760252197031L;

    @Schema(description = "场地名称")
    @Size(max = 255)
    @NotNull
    @NotEmpty
    private String name;

    @Schema(description = "场地地址")
    @Size(max = 255)
    private String address;

    @Schema(description = "场地备注")
    @Size(max = 255)
    private String remarks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
