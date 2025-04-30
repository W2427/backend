package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.tasks.entity.wbs.entry.WBSEntryBase;
import com.ose.util.BeanUtils;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 项目 WBS 扁平结构数据聚合传输对象，在时间维度上的展示。
 * 本周情况，下周情况。本月情况，下月情况
 * 工序阶段  工序
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WBSEntryGroupPlainDTO extends BaseDTO {


    private static final long serialVersionUID = -3796553836118304195L;

    @Schema(description = "子条目权重总和")
    private double totalScore;

    @Schema(description = "已完成的子条目权重总和")
    private double finishedScore;

    @Schema(description = "实际工时")
    private Double actualDuration;

    @Schema(description = "任务完成百分比")
    private boolean finishedPercent;

    @Schema(description = "工作组 ID")
    @JsonIgnore
    private Long teamId;

    @Schema(description = "工作场地名称")
    private String workSiteName;

    @Schema(description = "模块类型")
    private String moduleType;

    @Schema(description = "模块编号")
    @JsonProperty(value = "moduleNo")
    private String sector;

    @Schema(description = "专业")
    private String discipline;

    @Schema(description = "工序阶段")
    private String stage;

    @Schema(description = "工序")
    private String process;

    @Schema(description = "材料匹配率")
    private Double bomMatchPercent;

    @Schema(description = "物量 工作量")
    private Integer workLoad;

    @Schema(description = "图纸发放状态百分比")
    private Boolean dwgIssuedPercent;

    public WBSEntryGroupPlainDTO() {
        super();
    }

    public WBSEntryGroupPlainDTO(WBSEntry entry) {
        this();
        BeanUtils.copyProperties(entry, this);
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public double getFinishedScore() {
        return finishedScore;
    }

    public void setFinishedScore(double finishedScore) {
        this.finishedScore = finishedScore;
    }

    @JsonProperty("progress")
    public double getProgress() {
        return totalScore == 0.0 ? 0.0 : (Math.round(finishedScore * 10000 / totalScore) / 100.0);
    }

    public Double getActualDuration() {
        return actualDuration;
    }

    public void setActualDuration(Double actualDuration) {
        this.actualDuration = actualDuration;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getWorkSiteName() {
        return workSiteName;
    }

    public void setWorkSiteName(String workSiteName) {
        this.workSiteName = workSiteName;
    }

    @Schema(description = "工作组信息")
    @JsonProperty(value = "team", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getTeamRef() {
        return teamId == null ? null : new ReferenceData(teamId);
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public boolean isDisciplineSupported() {
        return discipline != null && WBSEntryBase.SUPPORTED_FUNC_PARTS.contains(discipline);
    }

    public boolean isWbsValid() {
        return !StringUtils.isEmpty(sector) && !StringUtils.isEmpty(stage) && !StringUtils.isEmpty(process);
    }

    public Double getBomMatchPercent() {
        return bomMatchPercent;
    }

    public void setBomMatchPercent(Double bomMatchPercent) {
        this.bomMatchPercent = bomMatchPercent;
    }

    public Integer getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(Integer workLoad) {
        this.workLoad = workLoad;
    }

    public boolean isFinishedPercent() {
        return finishedPercent;
    }

    public void setFinishedPercent(boolean finishedPercent) {
        this.finishedPercent = finishedPercent;
    }

    public Boolean getDwgIssuedPercent() {
        return dwgIssuedPercent;
    }

    public void setDwgIssuedPercent(Boolean dwgIssuedPercent) {
        this.dwgIssuedPercent = dwgIssuedPercent;
    }
}
