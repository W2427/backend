package com.ose.auth.entity;

import com.ose.auth.vo.OrgType;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(
    name = "organizations",
    indexes = {
        @Index(columnList = "deleted,depth"),
        @Index(columnList = "deleted,parentId"),
        @Index(columnList = "deleted"),
        @Index(columnList = "deleted,path")
    }
)
public class Organization extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 5752860302483007502L;

    @Schema(description = "公司 ID")
    @Column(nullable = false, length = 16)
    private Long companyId;

    @Schema(description = "名称")
    @Column(nullable = false, length = 128)
    private String name;

    @Schema(description = "编号")
    @Column
    private String no;

    @Schema(description = "组织类型")
    @Column(nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private OrgType type;

    @Schema(description = "上级组织 ID")
    @Column(length = 64)
    private Long parentId;

    @Schema(description = "层级深度")
    @Column
    private int depth = 0;

    @Schema(description = "层级路径")
    @Column(length = 1020)
    private String path;

    @Schema(description = "排序顺序")
    @Column
    private int sort = 0;

    @Schema(description = "下级组织")
    @Column(columnDefinition = "text")
    private String children;

    @Transient
    private boolean isDefault;

    @Schema(description = "是否为IDC")
    @Column
    private Boolean idc;

    @Schema(description = "代办任务数量")
    private Integer todoTaskCount;

    @Transient
    private List<UserBasic> members;

    @Transient
    private Long projectId;

    public Organization() {
        this(null);
    }

    public Organization(Long id) {
        super(id);
    }


    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public OrgType getType() {
        return type;
    }

    public void setType(OrgType type) {
        this.type = type;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public List<UserBasic> getMembers() {
        return members;
    }

    public void setMembers(List<UserBasic> members) {
        this.members = members;
    }

    public Boolean getIdc() {
        return idc;
    }

    public void setIdc(Boolean idc) {
        this.idc = idc;
    }

    public Integer getTodoTaskCount() {
        return todoTaskCount;
    }

    public void setTodoTaskCount(Integer todoTaskCount) {
        this.todoTaskCount = todoTaskCount;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
