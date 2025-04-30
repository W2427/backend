package com.ose.tasks.dto.bpm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 数据传输对象
 */
public class ExInspApplyCriteriaDTO extends BaseBatchTaskCriteriaDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "工序(多选时用,分隔)")
    private String process;

    @Schema(description = "工序阶段")
    private String processStage;

    @Schema(description = "工序区分")
    private String processStart;

    @Schema(description = "模块名称(多选时用,分隔)")
    private String entityModuleNames;

    @Schema(description = "实体类型(多选时用,分隔)")
    private String entityCategoryNameCns;

    @Schema(description = "实体号")
    private String entityNo;

    @Schema(description = "初始报告号")
    private String oldReportNos;

    @Schema(description = "业主")
    private boolean owner = false;

    @Schema(description = "第三方")
    private boolean third = false;

    @Schema(description = "其他")
    private boolean other = false;

    @Schema(description = "是否分页，默认true")
    private boolean pageable = true;

    @Schema(description = "assignees 执行人")
    private List<Long> assignees;

    @Schema(description = "生成报告类型")
    private GenerateReportType genType;


    @Schema(description = "开始时间")
    private String createDateFrom;

    @Schema(description = "结束时间")
    private String createDateUntil;

    @JsonIgnore
    @Schema(hidden = true)
    private Date createDateFromTime;

    @JsonIgnore
    @Schema(hidden = true)
    private Date createDateUntilTime;

    public enum GenerateReportType {
        OTHER("OTHER"),
        REPAIR("REPAIR"),
        PENALTY("PENALTY");

        private String name;

        GenerateReportType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public String getCreateDateFrom() {
        return createDateFrom;
    }

    public void setCreateDateFrom(String createDateFrom) {
        this.createDateFrom = createDateFrom;
        this.createDateFromTime = parseDate(createDateFrom);
    }

    public String getCreateDateUntil() {
        return createDateUntil;
    }

    public void setCreateDateUntil(String createDateUntil) {
        this.createDateUntil = createDateUntil;
        this.createDateUntilTime = parseDate(createDateUntil);
    }

    public Date getCreateDateFromTime() {
        return createDateFromTime;
    }

    public void setCreateDateFromTime(Date createDateFromTime) {
        this.createDateFromTime = createDateFromTime;
    }

    public Date getCreateDateUntilTime() {
        return createDateUntilTime;
    }

    public void setCreateDateUntilTime(Date createDateUntilTime) {
        this.createDateUntilTime = createDateUntilTime;
    }

    private Date parseDate(String dateStr) {
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return f.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public GenerateReportType getGenType() {
        return genType;
    }

    public void setGenType(GenerateReportType genType) {
        this.genType = genType;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public boolean isThird() {
        return third;
    }

    public void setThird(boolean third) {
        this.third = third;
    }

    public boolean isOther() {
        return other;
    }

    public void setOther(boolean other) {
        this.other = other;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getEntitySubTypeNameCns() {
        return entityCategoryNameCns;
    }

    public void setEntitySubTypeNameCns(String entityCategoryNameCns) {
        this.entityCategoryNameCns = entityCategoryNameCns;
    }

    public String getEntityModuleNames() {
        return entityModuleNames;
    }

    public void setEntityModuleNames(String entityModuleNames) {
        this.entityModuleNames = entityModuleNames;
    }

    public List<Long> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<Long> assignees) {
        this.assignees = assignees;
    }

    public String getProcessStage() {
        return processStage;
    }

    public void setProcessStage(String processStage) {
        this.processStage = processStage;
    }

    public String getProcessStart() {
        return processStart;
    }

    public void setProcessStart(String processStart) {
        this.processStart = processStart;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public boolean isPageable() {
        return pageable;
    }

    public void setPageable(boolean pageable) {
        this.pageable = pageable;
    }

    public String getOldReportNos() {
        return oldReportNos;
    }

    public void setOldReportNos(String oldReportNos) {
        this.oldReportNos = oldReportNos;
    }
}
