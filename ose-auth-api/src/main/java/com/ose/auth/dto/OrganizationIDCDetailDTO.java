package com.ose.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

public class OrganizationIDCDetailDTO extends BaseDTO {

    private static final long serialVersionUID = -6400883053000407400L;
    @Schema(description = "IDC部门ID")
    private Long departmentId;

    @Schema(description = "IDC部门名称")
    private String departmentName;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "上级条目 ID")
    private Long parentId;

    @Schema(description = "是否是叶子结点")
    @JsonProperty("isLeaf")
    private Boolean leaf = true;

    @Schema(description = "层级节点列表")
    private List<OrganizationIDCDetailDTO> children = null;

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public List<OrganizationIDCDetailDTO> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizationIDCDetailDTO> children) {
        this.children = children;
    }

    public void addChild(OrganizationIDCDetailDTO child) {

        if (child == null) {
            return;
        }

        if (children == null) {
            children = new ArrayList<>();
        }

        children.add(child);
    }
}
