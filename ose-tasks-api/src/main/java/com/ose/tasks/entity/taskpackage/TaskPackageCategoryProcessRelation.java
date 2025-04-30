package com.ose.tasks.entity.taskpackage;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.entity.bpm.BpmActivityNodePrivilege;
import com.ose.tasks.entity.bpm.BpmProcess;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.util.List;

/**
 * 任务包类型-实体工序关系实体。
 */
@Entity
@Table(
    name = "task_package_category_process_relation"
)
public class TaskPackageCategoryProcessRelation extends BaseBizEntity {

    private static final long serialVersionUID = 8747066578241757267L;

    @Schema(description = "所属组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "所属项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "任务包分类 ID")
    @Column(nullable = false)
    private Long categoryId;

    @Schema(description = "工序")
    @ManyToOne
    @JoinColumn(name = "processId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private BpmProcess process;

    @OneToMany
    @JoinColumns({
        @JoinColumn(updatable = false, insertable = false, name = "process_id", referencedColumnName = "processId")
    })
    @Where(clause = "status = 'ACTIVE'")
    private List<BpmActivityNodePrivilege> privileges;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setEntitySubTypeId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BpmProcess getProcess() {
        return process;
    }

    public void setProcess(BpmProcess process) {
        this.process = process;
    }

    public List<BpmActivityNodePrivilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<BpmActivityNodePrivilege> privileges) {
        this.privileges = privileges;
    }
}
