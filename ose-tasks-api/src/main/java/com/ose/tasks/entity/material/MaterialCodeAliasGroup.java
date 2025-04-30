package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 材料代码别称与材料组别对照表 实体类。
 */
@Entity
@Table(name = "material_code_alias_group")
public class MaterialCodeAliasGroup extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 5465909630001794702L;

    @Schema(description = "公司 ID")
    @Column(nullable = false, length = 16)
    private Long companyId;

    @Schema(description = "组织 ID")
    @Column(nullable = false, length = 16)
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column(nullable = false, length = 16)
    private Long projectId;

    @Schema(description = "材料代码别称")
    @Column(nullable = false)
    private String materialCodeAlias;

    @Schema(description = "材料分组ID")
    @Column(nullable = false, length = 16)
    private Long groupId;

    @Schema(description = "材料分组代码")
    @Column(nullable = false)
    private String groupCode;

    @Schema(description = "备注")
    @Column
    private String remark;

    public Long getCompanyId() {
        return companyId;
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

    public String getMaterialCodeAlias() {
        return materialCodeAlias;
    }

    public void setMaterialCodeAlias(String materialCodeAlias) {
        this.materialCodeAlias = materialCodeAlias;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
