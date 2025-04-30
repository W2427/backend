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
 * 专业实体类。
 */
@Entity
@Table(
    name = "func_part",
    indexes = {
        @Index(columnList = "project_id, name_en")
    }
)
public class FuncPart extends BaseEntity {


    private static final long serialVersionUID = -1324944234197488208L;
    // 项目 ID
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // 组织 ID
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    // 工序名称
    @Column(name = "name_cn", nullable = false, length = 128)
    @NotNull(message = "Func Part's name is required")
    private String nameCn;

    // 工序名称-英文
    @Column(name = "name_en", nullable = false)
    private String nameEn;

    // 排序
    @Column(name = "order_no", length = 11, columnDefinition = "int default 0")
    private int orderNo = 0;

    // 备注
    @Column(length = 500)
    private String memo;

    @Column(columnDefinition = "TEXT", length = 2000)
    private String hierarchyTypes;

    public FuncPart() {
    }

    @JsonCreator
    public FuncPart(@JsonProperty("hierarchyTypes") List<HierarchyType> hierarchyTypes) {
        this.hierarchyTypes = StringUtils.toJSON(hierarchyTypes);
    }


    @JsonIgnore
    public void setJsonHierarchyTypes(List<HierarchyType> hierarchyTypes) {
        if (hierarchyTypes != null) {
            this.hierarchyTypes = StringUtils.toJSON(hierarchyTypes);
        }
    }

    @JsonProperty(value = "hierarchyTypes", access = JsonProperty.Access.READ_ONLY)
    public List<HierarchyType> getJsonHierarchyTypes() {
        if (hierarchyTypes != null && !"".equals(hierarchyTypes)) {
            return StringUtils.decode(hierarchyTypes, new TypeReference<List<HierarchyType>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    public String getHierarchyTypes() {
        return hierarchyTypes;
    }

    public void setHierarchyTypes(String hierarchyTypes) {
        this.hierarchyTypes = hierarchyTypes;
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
}
