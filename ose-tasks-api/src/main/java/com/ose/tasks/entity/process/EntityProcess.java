package com.ose.tasks.entity.process;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 实体工序视图。
 */
@Entity
@Table(name = "entity_process")
@Cacheable(false)
public class EntityProcess implements Serializable {

    private static final long serialVersionUID = -8677116775924167732L;

    @Id
    @Column
    private String id;

    @Column
    private Long orgId;

    @Column
    private Long projectId;

    @Column
    private Long stageId;

    @Column
    private String stageName;

    @Column
    private Long processId;

    @Column
    private String processName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

}
