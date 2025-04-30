package com.ose.tasks.entity.bpm;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 任务 基础表，存储基础信息
 */
@Entity
@Table(name = "bpm_activity_instance_log")
public class BpmActivityInstanceLog extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -6402335862836931507L;
    //项目id
    @Column
    private Long projectId;
    //组织id
    @Column
    private Long orgId;
    @Schema(description = "实体编号")
    @Column
    private String entityNo;

    @Column(name = "drawing_title")
    private String drawingTitle;
    @Schema(description = "工序")
    @Column(name = "process_name")
    private String process;

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

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getDrawingTitle() {
        return drawingTitle;
    }

    public void setDrawingTitle(String drawingTitle) {
        this.drawingTitle = drawingTitle;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
