package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.drawing.Drawing;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author: DaiZeFeng
 * @date: 2023/2/24
 */
public class ManHourDTO extends BaseDTO {
    private static final long serialVersionUID = -1410097829515964536L;

    @Schema(description = "组织Id")
    private Long orgId;

    @Schema(description = "项目Id")
    private Long projectId;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "图纸/文件编号和标题")
    private String documentTitle;

    @Schema(description = "图纸/文件编号和标题")
    private String drawingNo;

    @Schema(description = "版本")
    private String rev;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "任务")
    private String task;

    @Schema(description = "流程")
    private String activity;

    @Schema(description = "分配人")
    private String assignedBy;

    @Schema(description = "阶段")
    private String stage;

    @Schema(description = "专业")
    private String discipline;

    @Schema(description = "阶段Id")
    private Long stageId;

    @Schema(description = "固定大小为8，周一到周日+合计")
    private List<String> manHourTitleList;

    @Schema(description = "周一数据")
    private List<Double> monDatas;

    @Schema(description = "周二数据")
    private List<Double> tueDatas;

    @Schema(description = "周三数据")
    private List<Double> wedDatas;

    @Schema(description = "周四数据")
    private List<Double> thuDatas;

    @Schema(description = "周五数据")
    private List<Double> friDatas;

    @Schema(description = "周六数据")
    private List<Double> satDatas;

    @Schema(description = "周日数据")
    private List<Double> sunDatas;

    @Schema(description = "合计数据")
    private List<Double> sumDatas;

    @Schema(description = "汇总正常工时数据")
    private List<Double> gatherManDatas;

    @Schema(description = "汇总加班工时数据")
    private List<Double> gatherOutDatas;

    @Schema(description = "源数据")
    private List<ManHourDTO> datas;

    @Schema(description = "文件标题列表")
    private List<Drawing> titles;

    @Schema(description = "阶段列表")
    private List<String> stages;

    @Schema(description = "当前年")
    private Integer year;

    @Schema(description = "有值的年")
    private List<String> years;

    @Schema(description = "有值的周")
    private List<String> weeks;


    @Schema(description = "当前周")
    private Integer week;

    public List<String> getYears() {
        return years;
    }

    public void setYears(List<String> years) {
        this.years = years;
    }

    public List<String> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<String> weeks) {
        this.weeks = weeks;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public List<String> getManHourTitleList() {
        return manHourTitleList;
    }

    public void setManHourTitleList(List<String> manHourTitleList) {
        this.manHourTitleList = manHourTitleList;
    }

    public List<Double> getMonDatas() {
        return monDatas;
    }

    public void setMonDatas(List<Double> monDatas) {
        this.monDatas = monDatas;
    }

    public List<Double> getTueDatas() {
        return tueDatas;
    }

    public void setTueDatas(List<Double> tueDatas) {
        this.tueDatas = tueDatas;
    }

    public List<Double> getWedDatas() {
        return wedDatas;
    }

    public void setWedDatas(List<Double> wedDatas) {
        this.wedDatas = wedDatas;
    }

    public List<Double> getThuDatas() {
        return thuDatas;
    }

    public void setThuDatas(List<Double> thuDatas) {
        this.thuDatas = thuDatas;
    }

    public List<Double> getFriDatas() {
        return friDatas;
    }

    public void setFriDatas(List<Double> friDatas) {
        this.friDatas = friDatas;
    }

    public List<Double> getSatDatas() {
        return satDatas;
    }

    public void setSatDatas(List<Double> satDatas) {
        this.satDatas = satDatas;
    }

    public List<Double> getSunDatas() {
        return sunDatas;
    }

    public void setSunDatas(List<Double> sunDatas) {
        this.sunDatas = sunDatas;
    }

    public List<Double> getSumDatas() {
        return sumDatas;
    }

    public void setSumDatas(List<Double> sumDatas) {
        this.sumDatas = sumDatas;
    }

    public List<Double> getGatherManDatas() {
        return gatherManDatas;
    }

    public void setGatherManDatas(List<Double> gatherManDatas) {
        this.gatherManDatas = gatherManDatas;
    }

    public List<Double> getGatherOutDatas() {
        return gatherOutDatas;
    }

    public void setGatherOutDatas(List<Double> gatherOutDatas) {
        this.gatherOutDatas = gatherOutDatas;
    }

    public List<ManHourDTO> getDatas() {
        return datas;
    }

    public void setDatas(List<ManHourDTO> datas) {
        this.datas = datas;
    }

    public List<Drawing> getTitles() {
        return titles;
    }

    public void setTitles(List<Drawing> titles) {
        this.titles = titles;
    }

    public List<String> getStages() {
        return stages;
    }

    public void setStages(List<String> stages) {
        this.stages = stages;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
