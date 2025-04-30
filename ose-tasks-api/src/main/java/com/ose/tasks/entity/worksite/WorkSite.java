package com.ose.tasks.entity.worksite;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 工作场地数据实体。
 */
@Entity
@Table(name = "work_site")
public class WorkSite extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 8385561162241151157L;

    @Schema(description = "公司 ID")
    @Column(nullable = false)
    private Long companyId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "上级场地 ID")
    @Column
    private Long parentId;

    @Schema(description = "场地路径")
    @Column
    private String path;

    @Schema(description = "场地层级深度")
    @Column
    private Integer depth = 0;

    @Schema(description = "场地名称")
    @Column(nullable = false)
    private String name;

    @Schema(description = "地址")
    @Column
    private String address;

    @Schema(description = "备注")
    @Column
    private String remarks;

    @Schema(description = "排序号")
    @Column
    private Integer sort = 0;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}
