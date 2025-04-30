package com.ose.tasks.dto.bizcode;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 业务代码创建数据传输对象。
 */
public class BizCodeTypeDTO extends BizCodePatchDTO {

    private static final long serialVersionUID = -1158138505889603547L;

    @Schema(description = "公司 ID")
    private Long companyId;

    @Schema(description = "组织 ID")
    private Long orgId;

    @Schema(description = "项目 ID")
    private Long projectId;

    @Schema(description = "业务代码分类")
    private String type;

    @Schema(description = "业务代码分类名称")
    private String typeName;

    public Long getCompanyId() {
        return companyId;
    }

    public BizCodeTypeDTO() {
    }

    public BizCodeTypeDTO(Long companyId, Long orgId, Long projectId, String type, String typeName) {
        this.companyId = companyId;
        this.orgId = orgId;
        this.projectId = projectId;
        this.type = type;
        this.typeName = typeName;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
