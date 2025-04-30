package com.ose.issues.dto;

import com.ose.annotation.NullableNotBlank;
import com.ose.issues.vo.IssueCategory;
import com.ose.issues.vo.IssueSource;
import com.ose.issues.vo.Priority;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Date;
import java.util.List;

public class IssueUpdateDTO extends ExperienceUpdateDTO {

    private static final long serialVersionUID = 5525504863323681950L;

    @Schema(description = "编号")
    @NullableNotBlank
    private String no;

    @Schema(description = "编号")
    private String opinionNo;

    @Schema(description = "优先级")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Schema(description = "预计完成日期")
    private Date planFinishDate;

    @Schema(description = "负责人")
    private Long personInChargeId;

    @Schema(description = "成员")
    private List<Long> members;

    @Schema(description = "整改人所在部门")
    private Long departmentId;

    @Schema(description = "外检编号")
    private String externalInspectNo;

    @Schema(description = "来源")
    private IssueSource punchSource;

    @Schema(description = "意见等级")
    private IssueCategory punchCategory;

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
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    @Override
    public String getNo() {
        return no;
    }

    @Override
    public void setNo(String no) {
        this.no = no;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Date getPlanFinishDate() {
        return planFinishDate;
    }

    public void setPlanFinishDate(Date planFinishDate) {
        this.planFinishDate = planFinishDate;
    }

    public Long getPersonInChargeId() {
        return personInChargeId;
    }

    public void setPersonInChargeId(Long personInChargeId) {
        this.personInChargeId = personInChargeId;
    }

    public List<Long> getMembers() {
        return members;
    }

    public void setMembers(List<Long> members) {
        this.members = members;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getExternalInspectNo() {
        return externalInspectNo;
    }

    public void setExternalInspectNo(String externalInspectNo) {
        this.externalInspectNo = externalInspectNo;
    }

    public IssueSource getPunchSource() {
        return punchSource;
    }

    public void setPunchSource(IssueSource punchSource) {
        this.punchSource = punchSource;
    }

    public IssueCategory getPunchCategory() {
        return punchCategory;
    }

    public void setPunchCategory(IssueCategory punchCategory) {
        this.punchCategory = punchCategory;
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

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public String getOpinionNo() {
        return opinionNo;
    }

    public void setOpinionNo(String opinionNo) {
        this.opinionNo = opinionNo;
    }

}
