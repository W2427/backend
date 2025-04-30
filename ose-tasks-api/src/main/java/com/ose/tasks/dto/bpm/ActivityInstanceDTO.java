package com.ose.tasks.dto.bpm;

import java.util.Date;

import com.ose.dto.BaseDTO;

import com.ose.tasks.vo.qc.NDEType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 数据传输对象
 */
public class ActivityInstanceDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    // 实体编号
    @Schema(description = "实体编号")
    private String entityNo;

    @Schema(description = "实体编号1")
    private String entityNo1;

    @Schema(description = "实体编号2")
    private String entityNo2;


    @Schema(description = "图纸名称")
    private String drawingTitle;

    // project_node entityId      (id)
    @Schema(description = "实体ID")
    private Long entityId;

    // project_node entityType    (wbsEntityType)
    @Schema(description = "实体类型")
    private String entityType;

    // 实体类型
    @Schema(description = "实体分类")
    private String entitySubType;

    // 实体类型id
    @Schema(description = "实体分类ID")
    private Long entitySubTypeId;

    @Schema(description = "担当着ID")
    private Long assignee;

    @Schema(description = "担当着名字")
    private String assigneeName;

    // 工序分类
    private String processStage;

    //NDT TYPE
    private NDEType ndtType;

    // 工序
    private String process;

    // 工序分类id
    private Long processStageId;

    // 工序id
    private Long processId;

    // 预计开始日期
    private Date planStart;

    // 预计结束日期
    private Date planEnd;

    // 预计工时
    private double planHour;

    // 备注
    private String memo;

    // 版本
    private String version;

    @Schema(description = "专业")
    private String discipline;


    @Schema(description = "Parent Task Id")
    private Long parentTaskId;

    @Schema(description = "FUNC PART 功能块")
    private String funcPart;

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public Long getAssignee() {
        return assignee;
    }

    public void setAssignee(Long assignee) {
        this.assignee = assignee;
    }

    public Date getPlanStart() {
        return planStart;
    }

    public void setPlanStart(Date planStart) {
        this.planStart = planStart;
    }

    public Date getPlanEnd() {
        return planEnd;
    }

    public void setPlanEnd(Date planEnd) {
        this.planEnd = planEnd;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getProcessStage() {
        return processStage;
    }

    public void setProcessStage(String processStage) {
        this.processStage = processStage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public Long getProcessStageId() {
        return processStageId;
    }

    public void setProcessStageId(Long processStageId) {
        this.processStageId = processStageId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public double getPlanHour() {
        return planHour;
    }

    public void setPlanHour(double planHour) {
        this.planHour = planHour;
    }

    public String getDrawingTitle() {
        return drawingTitle;
    }

    public void setDrawingTitle(String drawingTitle) {
        this.drawingTitle = drawingTitle;
    }

    public NDEType getNdtType() {
        return ndtType;
    }

    public void setNdtType(NDEType ndtType) {
        this.ndtType = ndtType;
    }

    public Long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }

    public String getEntityNo1() {
        return entityNo1;
    }

    public void setEntityNo1(String entityNo1) {
        this.entityNo1 = entityNo1;
    }

    public String getEntityNo2() {
        return entityNo2;
    }

    public void setEntityNo2(String entityNo2) {
        this.entityNo2 = entityNo2;
    }
}
