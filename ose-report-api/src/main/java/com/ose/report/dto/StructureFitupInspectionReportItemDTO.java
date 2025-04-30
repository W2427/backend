package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class StructureFitupInspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 2342813509620049220L;

    @Schema(name = "构件号")
    private String TagNo;

    @Schema(name = "图纸号")
    private String drawingNo;

    @Schema(name = "焊口号")
    private String jointNo;

    @Schema(name = "焊口类型")
    private String weldType;

    @Schema(name = "零件1")
    private String part1;

    @Schema(name = "零件2")
    private String part2;

    @Schema(name = "板号1")
    private String plate1;

    @Schema(name = "板号2")
    private String plate2;

    @Schema(name = "结果")
    private String result;

    @Schema(name = "流程id")
    private Long actInstId;

    @Schema(name = "工作组")
    private String workTeam;

    @Schema(name = "工序")
    private String process;

    @Schema(name = "任务节点名称")
    private String taskDefKey;

    @Schema(name = "任务包")
    private String taskPackageName;

    @Schema(name = "发起人")
    private String ownerName;

    @Schema(name = "当前执行人")
    private String currentExecutor;

    @Schema(name = "完成状态")
    private String finishState;

    @Schema(name = "开始时间")
    private String startDate;

    public String getTagNo() {
        return TagNo;
    }

    public void setTagNo(String tagNo) {
        TagNo = tagNo;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getJointNo() {
        return jointNo;
    }

    public void setJointNo(String jointNo) {
        this.jointNo = jointNo;
    }

    public String getWeldType() {
        return weldType;
    }

    public void setWeldType(String weldType) {
        this.weldType = weldType;
    }

    public String getPart1() {
        return part1;
    }

    public void setPart1(String part1) {
        this.part1 = part1;
    }

    public String getPart2() {
        return part2;
    }

    public void setPart2(String part2) {
        this.part2 = part2;
    }

    public String getPlate1() {
        return plate1;
    }

    public void setPlate1(String plate1) {
        this.plate1 = plate1;
    }

    public String getPlate2() {
        return plate2;
    }

    public void setPlate2(String plate2) {
        this.plate2 = plate2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getWorkTeam() {
        return workTeam;
    }

    public void setWorkTeam(String workTeam) {
        this.workTeam = workTeam;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getTaskPackageName() {
        return taskPackageName;
    }

    public void setTaskPackageName(String taskPackageName) {
        this.taskPackageName = taskPackageName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCurrentExecutor() {
        return currentExecutor;
    }

    public void setCurrentExecutor(String currentExecutor) {
        this.currentExecutor = currentExecutor;
    }

    public String getFinishState() {
        return finishState;
    }

    public void setFinishState(String finishState) {
        this.finishState = finishState;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }
}
