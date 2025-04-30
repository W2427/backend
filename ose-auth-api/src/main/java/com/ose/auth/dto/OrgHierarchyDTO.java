package com.ose.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class OrgHierarchyDTO extends SortDTO {

    private static final long serialVersionUID = -6867798702815789210L;

    @Schema(description = "组织类型")
    private String type = "DEPARTMENT";

    @Schema(description = "组织名称")
    private String name;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "深度")
    private int depth = Integer.MAX_VALUE;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
