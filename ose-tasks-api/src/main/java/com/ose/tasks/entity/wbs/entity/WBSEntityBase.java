package com.ose.tasks.entity.wbs.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.entity.Project;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * WBS 实体数据实体。
 */
@MappedSuperclass
public abstract class WBSEntityBase extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -8205679620993060014L;

    @Schema(description = "公司 ID")
    @Column(nullable = false)
    private Long companyId;

    @Schema(description = "组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "节点编号")
    @Column(nullable = false)
    private String no;

    @Transient
    private String parentNo;

    @Schema(description = "节点表示名")
    @Column(nullable = false, length = 64)
    private String displayName = null;

    @Schema(description = "实体类型")
    @Column
    private String entityType;

    @Schema(description = "实体子类型")
    @Column
    private String entitySubType;


    @Schema(description = "实体专业")
    @Column
    private String discipline;

    @Schema(description = "用于批量删除标记")
    @Lob
    @Column(length = 4096)
    private String remarks;

    @Schema(description = "备注")
    @Lob
    @Column(length = 4096)
    private String remarks2;

    @Schema(description = "是否已被取消")
    @Column(nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean cancelled = false;

    public WBSEntityBase() {
        super();
    }

    /**
     * 构造方法。
     *
     * @param project 项目
     */
    public WBSEntityBase(Project project) {

        this();

        if (project != null) {
            setProject(project);
        }

    }

    /**
     * 设定所属项目信息。
     *
     * @param project 项目
     */
    public void setProject(Project project) {
        setCompanyId(project.getCompanyId());
        setOrgId(project.getOrgId());
        setProjectId(project.getId());
    }

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

    public String getNo() {
        return no;
    }

    /**
     * 设定实体号。
     *
     * @param no 实体号
     */
    public void setNo(String no) {

        this.no = no;

        if (this.displayName == null) {
            this.displayName = this.no;
        }

    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * 设定表示名。
     *
     * @param displayName 表示名
     */
    public void setDisplayName(String displayName) {

        this.displayName = displayName;

        if (this.no == null) {
            this.no = this.displayName;
        }

    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public abstract String getEntitySubType();

    public abstract String getEntityBusinessType();

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
        if (cancelled && !getDeleted()) {
            setCancelledAt();
        }
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    public String getParentNo() {
        return parentNo;
    }

    public void setParentNo(String parentNo) {
        this.parentNo = parentNo;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
