package com.ose.tasks.entity.setting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseEntity;
import com.ose.util.StringUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 层级类型实体类。
 */
@Entity
@Table(
    name = "hierarchy_type",
    indexes = {
        @Index(columnList = "project_id, name_en")
    }
)
public class HierarchyType extends BaseEntity {

    private static final long serialVersionUID = -7272699622963270472L;
    // 项目 ID
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // 组织 ID
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    // 工序名称
    @Column(name = "name_cn", nullable = false, length = 128)
    @NotNull(message = "Hierarchy Type's name is required")
    private String nameCn;

    // 工序名称-英文
    @Column(name = "name_en", nullable = false)
    private String nameEn;

    // 排序
    @Column(name = "order_no", length = 11, columnDefinition = "int default 0")
    private int orderNo = 0;

    @Column
    private String entityTypes;

    private Long parentId;
    // 备注
    @Column(length = 500)
    private String memo;

    public HierarchyType() {
    }

    @JsonCreator
    public HierarchyType(@JsonProperty("entityTypes") List<String> entityTypes) {
        this.entityTypes = StringUtils.toJSON(entityTypes);
    }

    @JsonIgnore
    public void setJsonEntityTypes(List<String> entityTypes) {
        if (entityTypes != null) {
            this.entityTypes = StringUtils.toJSON(entityTypes);
        }
    }

    @JsonProperty(value = "entityTypes", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonEntityTypes() {
        if (entityTypes != null && !"".equals(entityTypes)) {
            return StringUtils.decode(entityTypes, new TypeReference<List<String>>() {
            });
        } else {
            return new ArrayList<>();
        }
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

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(String entityTypes) {
        this.entityTypes = entityTypes;
    }
}
