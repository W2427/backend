package com.ose.issues.dto;

import com.ose.issues.vo.IssueCategory;
import com.ose.issues.vo.IssueSource;
import com.ose.issues.vo.Priority;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class IssueCreateDTO extends ExperienceCreateDTO {

    private static final long serialVersionUID = 7232021579638052807L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "编号")
    private String no;

    @Schema(description = "编号")
    private String opinionNo;

    @Schema(description = "优先级")
    private Priority priority;

    @Schema(description = "预计完成日期")
    private Date planFinishTime;

    @Schema(description = "提出人")
    private Long owner;

    @Schema(description = "负责人")
    private Long leader;

    @Schema(description = "整改人")
    private List<Long> members;

    @Schema(description = "整改人所在部门")
    private Long department;

    @Schema(description = "外检编号")
    private String externalInspectNo;

    @Schema(description = "来源")
    private IssueSource source;

    @Schema(description = "意见等级")
    private IssueCategory level;

    @Schema(description = "报验单")
    private String inspectionRecord;

    @Schema(description = "工序")
    private String process;

    @Schema(description = "实体")
    private String entities;

    @Schema(description = "区域")
    private String area;

    @Schema(description = "层")
    private String layer;

    @Schema(description = "试压包")
    private String pressureTestPackage;

    @Schema(description = "清洁包")
    private String cleanPackage;

    @Schema(description = "子系统")
    private String subSystem;

    @Schema(description = "状态")
    private EntityStatus status;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Date getPlanFinishTime() {
        return planFinishTime;
    }

    public void setPlanFinishTime(Date planFinishTime) {
        this.planFinishTime = planFinishTime;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public Long getLeader() {
        return leader;
    }

    public void setLeader(Long leaderId) {
        this.leader = leader;
    }

    public List<Long> getMembers() {
        return members;
    }

    public void setMembers(List<Long> members) {
        this.members = members;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public String getExternalInspectNo() {
        return externalInspectNo;
    }

    public void setExternalInspectNo(String externalInspectNo) {
        this.externalInspectNo = externalInspectNo;
    }

    public IssueSource getSource() {
        return source;
    }

    public void setSource(IssueSource source) {
        this.source = source;
    }

    public IssueCategory getLevel() {
        return level;
    }

    public void setLevel(IssueCategory level) {
        this.level = level;
    }

    public String getInspectionRecord() {
        return inspectionRecord;
    }

    public void setInspectionRecord(String inspectionRecord) {
        this.inspectionRecord = inspectionRecord;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getEntities() {
        return entities;
    }

    public void setEntities(String entities) {
        this.entities = entities;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getPressureTestPackage() {
        return pressureTestPackage;
    }

    public void setPressureTestPackage(String pressureTestPackage) {
        this.pressureTestPackage = pressureTestPackage;
    }

    public String getCleanPackage() {
        return cleanPackage;
    }

    public void setCleanPackage(String cleanPackage) {
        this.cleanPackage = cleanPackage;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOpinionNo() {
        return opinionNo;
    }

    public void setOpinionNo(String opinionNo) {
        this.opinionNo = opinionNo;
    }

}
