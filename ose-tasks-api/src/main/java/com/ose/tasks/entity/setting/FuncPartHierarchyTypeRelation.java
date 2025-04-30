package com.ose.tasks.entity.setting;

import com.ose.entity.BaseBizEntity;
import org.springframework.data.annotation.Id;

import jakarta.persistence.*;

/**
 * 功能分块和层级类型关系实体类。
 */
@Entity
@Table(
    name = "func_part_hierarchy_type_relation",
    indexes = {
        @Index(columnList = "project_id, func_part_id,hierarchy_type_id"),
        @Index(columnList = "project_id, hierarchy_type_id, func_part_id,")
    }
)
public class FuncPartHierarchyTypeRelation extends BaseBizEntity {

    private static final long serialVersionUID = -7272699622963270472L;
    // 项目 ID
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // 组织 ID
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    // func part id
    @Id
    @ManyToOne
    @JoinColumn(name = "func_part_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private FuncPart funcPart;

    // 层级类型 ID
    @Id
    @ManyToOne
    @JoinColumn(name = "hierarchy_type_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private HierarchyType hierarchyType;

    // 排序
    @Column(name = "order_no", length = 11, columnDefinition = "int default 0")
    private int orderNo = 0;

    // 备注
    @Column(length = 500)
    private String memo;

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

    public FuncPart getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(FuncPart funcPart) {
        this.funcPart = funcPart;
    }

    public HierarchyType getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(HierarchyType hierarchyType) {
        this.hierarchyType = hierarchyType;
    }
}
