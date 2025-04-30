package com.ose.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.auth.vo.OrgType;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 用户基本信息数据传输对象类。
 */
@Entity
@Table(name = "organizations")
public class OrganizationBasic extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 8215563909547933988L;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "上级组织")
    private Long parentId;

    @Schema(description = "是否已删除")
    @JsonIgnore
    private boolean deleted = false;

    @Schema(description = "排序")
    private long sort;

    @Schema(description = "层级深度")
    private int depth;

    @Schema(description = "下级组织")
    private String children;

    @Schema(description = "组织类型")
    @Enumerated(EnumType.STRING)
    private OrgType type;

    @Schema(description = "层级路径")
    private String path;

    @Schema(description = "编号")
    private String no;

    @Schema(description = "是否为IDC")
    @Column
    private Boolean idc;

    @Schema(description = "下级组织列表")
    @Transient
    private List<OrganizationBasic> childOrgs;

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public OrgType getType() {
        return type;
    }

    public void setType(OrgType type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @JsonProperty(value = "children", access = READ_ONLY)
    public List<OrganizationBasic> getChildOrgs() {
        return childOrgs;
    }

    public void setChildOrgs(List<OrganizationBasic> childOrgs) {
        this.childOrgs = childOrgs;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Boolean getIdc() {
        return idc;
    }

    public void setIdc(Boolean idc) {
        this.idc = idc;
    }
}
