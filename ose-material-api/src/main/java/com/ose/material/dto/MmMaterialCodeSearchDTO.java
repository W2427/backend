package com.ose.material.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * BOM查询DTO
 */
public class MmMaterialCodeSearchDTO extends PageDTO {

    private static final long serialVersionUID = 28546176887555903L;

    @Schema(description = "搜索关键字")
    public String keyword;

    @Schema(description = "添加状态")
    private String addStatus;

    private Long orgId;
    private Long projectId;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getAddStatus() {
        return addStatus;
    }

    public void setAddStatus(String addStatus) {
        this.addStatus = addStatus;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
