package com.ose.tasks.entity.taskpackage;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.vo.drawing.DrawingType;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 任务包-图纸关系实体。
 */
@Entity
@Table(
    name = "task_package_drawing_relation"
)
public class TaskPackageDrawingRelation extends BaseBizEntity {

    private static final long serialVersionUID = -3251960208848020451L;

    @Schema(description = "所属组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "所属项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "任务包 ID")
    @Column(nullable = false)
    private Long taskPackageId;

    @Schema(description = "图纸类型")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DrawingType drawingType;

    @Schema(description = "图纸 ID")
    @Column(nullable = false)
    private Long drawingId;

    @Schema(description = "图纸编号")
    @Column(nullable = false)
    private String drawingNo;

    @Schema(description = "子图纸 ID")
    @Column(length = 16)
    private Long subDrawingId;

    @Schema(description = "子图纸编号")
    @Column
    private String subDrawingNo;

    @Schema(description = "图纸页码")
    @Column
    private Integer pageNo;

    @Schema(description = "子图纸文件 ID")
    @Transient
    private Long subDrawingFileId;

    @Schema(description = "子图纸版本")
    @Transient
    private String subDrawingVersion;

    public TaskPackageDrawingRelation() {
    }

    private TaskPackageDrawingRelation(TaskPackage taskPackage) {
        setOrgId(taskPackage.getOrgId());
        setProjectId(taskPackage.getProjectId());
        setTaskPackageId(taskPackage.getId());
        setStatus(EntityStatus.ACTIVE);
    }

    public TaskPackageDrawingRelation(TaskPackage taskPackage, Drawing drawing) {
        this(taskPackage);
        setDrawingType(DrawingType.DRAWING);
        setDrawingId(drawing.getId());
        setDrawingNo(drawing.getDwgNo());
    }

    public TaskPackageDrawingRelation(TaskPackage taskPackage, Drawing drawing, SubDrawing subDrawing) {
        this(taskPackage, drawing);
        setDrawingType(DrawingType.SUB_DRAWING);
        setSubDrawingId(subDrawing.getId());
        setSubDrawingNo(subDrawing.getSubDrawingNo());
        setPageNo(subDrawing.getPageNo());
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

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public DrawingType getDrawingType() {
        return drawingType;
    }

    public void setDrawingType(DrawingType drawingType) {
        this.drawingType = drawingType;
    }

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public Long getSubDrawingId() {
        return subDrawingId;
    }

    public void setSubDrawingId(Long subDrawingId) {
        this.subDrawingId = subDrawingId;
    }

    public String getSubDrawingNo() {
        return subDrawingNo;
    }

    public void setSubDrawingNo(String subDrawingNo) {
        this.subDrawingNo = subDrawingNo;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Long getSubDrawingFileId() {
        return subDrawingFileId;
    }

    public void setSubDrawingFileId(Long subDrawingFileId) {
        this.subDrawingFileId = subDrawingFileId;
    }

    public String getSubDrawingVersion() {
        return subDrawingVersion;
    }

    public void setSubDrawingVersion(String subDrawingVersion) {
        this.subDrawingVersion = subDrawingVersion;
    }
}
