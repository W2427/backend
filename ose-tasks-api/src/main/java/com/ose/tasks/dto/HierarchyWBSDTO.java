package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目WBS层级结构数据传输对象。
 */
public class HierarchyWBSDTO extends BaseDTO {

    private static final long serialVersionUID = -7111370955161651120L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "parentId")
    private String parentId;

    @Schema(description = "level")
    private String level;

    @Schema(description = "content")
    private String content;

    @Schema(description = "is Edit")
    private Boolean isEdit;
    @Schema(description = "is Added")
    private Boolean isAdd;

    @Schema(description = "is Delete")
    private Boolean isDelete;

    @Schema(description = "is Regular")
    private Boolean isRegular;

    @Schema(description = "detail")
    private List<String> detail;

    /**
     * id: 2,
     * level: '3',
     * content: 'TASK_STAGE',
     * isAdd: false,
     * isRegular: true,
     * isEdit: false,
     */

    @Schema(description = "层级节点列表")
    private List<HierarchyWBSDTO> children = new ArrayList<>();

    @Schema(description = "层级维度 STRUCTURE")
    private String hierarchyType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty("isEdit")
    public Boolean getEdit() {
        return isEdit;
    }

    public void setEdit(Boolean edit) {
        isEdit = edit;
    }

    public List<HierarchyWBSDTO> getChildren() {
        return children;
    }

    public void setChildren(List<HierarchyWBSDTO> children) {
        this.children = children;
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void addChild(HierarchyWBSDTO child) {

        if (child == null) {
            return;
        }

        if (children == null) {
            children = new ArrayList<>();
        }

        children.add(child);
    }

    @JsonProperty("isAdd")
    public Boolean getAdd() {
        return isAdd;
    }

    public void setAdd(Boolean add) {
        isAdd = add;
    }

    @JsonProperty("isRegular")
    public Boolean getRegular() {
        return isRegular;
    }

    public void setRegular(Boolean regular) {
        isRegular = regular;
    }

    public List<String> getDetail() {
        return detail;
    }

    public void setDetail(List<String> detail) {
        this.detail = detail;
    }

    @JsonProperty("isDelete")
    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }
}
