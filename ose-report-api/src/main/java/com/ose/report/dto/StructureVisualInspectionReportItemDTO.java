package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class StructureVisualInspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 2347911879675140801L;

    @Schema(name = "构件号")
    private String tagNo;

    @Schema(name = "")
    private String drawingNo;

    @Schema(name = "焊口号")
    private String jointNo;

    @Schema(name = "壁厚1")
    private String thickness1;

    @Schema(name = "壁厚2")
    private String thickness2;

    @Schema(name = "焊口类型")
    private String weldType;

    @Schema(name = "焊工号1")
    private String welderNo1;

    @Schema(name = "焊工号2")
    private String welderNo2;

    @Schema(name = "WPS")
    private String wps;

    @Schema(name = "焊口长度")
    private String weldLength;

    @Schema(name = "焊接日期")
    private String weldDate;

    @Schema(name = "")
    private String result1;

    @Schema(name = "")
    private String result2;

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
        return tagNo;
    }

    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
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

    public String getThickness1() {
        return thickness1;
    }

    public void setThickness1(String thickness1) {
        this.thickness1 = thickness1;
    }

    public String getThickness2() {
        return thickness2;
    }

    public void setThickness2(String thickness2) {
        this.thickness2 = thickness2;
    }

    public String getWeldType() {
        return weldType;
    }

    public void setWeldType(String weldType) {
        this.weldType = weldType;
    }

    public String getWelderNo1() {
        return welderNo1;
    }

    public void setWelderNo1(String welderNo1) {
        this.welderNo1 = welderNo1;
    }

    public String getWelderNo2() {
        return welderNo2;
    }

    public void setWelderNo2(String welderNo2) {
        this.welderNo2 = welderNo2;
    }

    public String getWps() {
        return wps;
    }

    public void setWps(String wps) {
        this.wps = wps;
    }

    public String getWeldLength() {
        return weldLength;
    }

    public void setWeldLength(String weldLength) {
        this.weldLength = weldLength;
    }

    public String getWeldDate() {
        return weldDate;
    }

    public void setWeldDate(String weldDate) {
        this.weldDate = weldDate;
    }

    public String getResult1() {
        return result1;
    }

    public void setResult1(String result1) {
        this.result1 = result1;
    }

    public String getResult2() {
        return result2;
    }

    public void setResult2(String result2) {
        this.result2 = result2;
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
