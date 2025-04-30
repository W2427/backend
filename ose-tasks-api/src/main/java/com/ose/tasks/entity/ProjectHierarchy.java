package com.ose.tasks.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.dto.BaseDTO;
import com.ose.util.CryptoUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 项目相关信息实体
 * PROJECT ID
 * PROJECT INFO KEY
 * PROJECT INFO VALUE

 */
@Entity
@Table(name = "project_hierarchy")
public class ProjectHierarchy extends BaseDTO {


    private static final long serialVersionUID = 3497125585025308781L;
    @Schema(description = "实体 ID")
    @Id
    @Column
    private Long id;

    @Schema(description = "项目 ID")
    @Column(nullable = false)
    private Long projectId;        //项目ID

    @Schema(description = "所属组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "parentId")
    @Column
    private Long parentId;

    @Schema(description = "level")
    @Column
    private String hierarchyLevel;

    @Schema(description = "content")
    @Column
    private String content;

    @Schema(description = "is Edit")
    @Column
    private Boolean isEdit;
    @Schema(description = "is Added")
    @Column
    private Boolean isAdd;

    @Schema(description = "is Delete")
    @Column
    private Boolean isDelete;

    @Schema(description = "is Regular")
    @Column
    private Boolean isRegular;

    @Schema(description = "detail")
    @Column
    private String detail;

    @Schema(description = "层级维度")
    @Column
    private String hierarchyType;


    /**
     * 默认构造方法。
     */
    public ProjectHierarchy() {
        this(generateId());
    }

    /**
     * 构造方法。
     *
     * @param id 实体 ID
     */
    public ProjectHierarchy(Long id) {
        this.setId(id == null ? generateId() : id);
    }

    /**
     * 取得数据实体 ID。
     *
     * @return 数据实体 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置数据实体 ID。
     *
     * @param id 数据实体 ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 生成 bigint/long ID。
     *
     * @return ID
     */
    public static Long generateId() {
        return CryptoUtils.uniqueDecId();
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getHierarchyLevel() {
        return hierarchyLevel;
    }

    public void setHierarchyLevel(String hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
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

    @JsonProperty("isAdd")
    public Boolean getAdd() {
        return isAdd;
    }

    public void setAdd(Boolean add) {
        isAdd = add;
    }

    @JsonProperty("isDelete")
    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    @JsonProperty("isRegular")
    public Boolean getRegular() {
        return isRegular;
    }

    public void setRegular(Boolean regular) {
        isRegular = regular;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }
}
