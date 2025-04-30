package com.ose.issues.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.issues.vo.IssueCategory;
import com.ose.issues.vo.IssueSource;
import com.ose.issues.vo.Priority;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(
    name = "issues",
    indexes = {
        @Index(columnList = "projectId,type,no"),
    }
)
public class Issue extends IssueBase {

    private static final long serialVersionUID = 8051420086797789185L;

    @Schema(description = "意见提出日期")
    @Column
    private Date originDate;

    @Schema(description = "计划完成日期")
    @Column
    private Date planFinishDate;

    @Schema(description = "意见状态")
    @Column
    private String punchStatus;

    @Schema(description = "意见内控状态")
    @Column
    private String internalPunchStatus;

    @Schema(description = "优先级")
    @Column
    private Priority priority = Priority.HIGH;

    @Schema(description = "提出人ID")
    @Column
    private Long originatorId;

    @Schema(description = "提出人")
    @Column
    private String originator;

    @Schema(description = "提出人全称")
    @Column
    private String originatorFullName;

    @Schema(description = "提出人公司")
    @Column
    private String originatorCompany;

    @Schema(description = "责任人ID")
    @Column
    private Long personInChargeId;

    @Schema(description = "责任人")
    @Column
    private String personInCharge;

    @Schema(description = "责任 qc")
    @Column
    private String qc;

    @Schema(description = "责任 qcId")
    @Column
    private Long qcId;


    @Schema(description = "整改人")
    @Column
    private String members;

    @Schema(description = "整改人所在部门Id")
    @Column
    private Long departmentId;

    @Schema(description = "整改人所在部门")
    @Column
    private String department;


    @Schema(description = "外检编号")
    @Column
    private String externalInspectNo;

    @Schema(description = "来源")
    @Column
    @Enumerated(EnumType.STRING)
    private IssueSource punchSource;

    @Schema(description = "意见等级")
    @Column
    @Enumerated(EnumType.STRING)
    private IssueCategory punchCategory;

    @Schema(description = "报验单")
    @Column
    private String inspectionRecord;

    @Schema(description = "工序")
    @Column
    private String process;

    @Schema(description = "实体")
    @Column
    private String entities;

    @Schema(description = "模块")
    @Column
    private String module;

    @Schema(description = "专业")
    @Column
    private String discipline;

    @Schema(description = "试压包")
    @Column
    private String pressureTestPackage;

    @Schema(description = "清洁包")
    @Column
    private String cleanPackage;

    @Schema(description = "子系统")
    @Column
    private String subSystem;

    @Schema(description = "导入批次编号")
    @Column
    private String batchNo;

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }


    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public void setMemberIDs(List<Long> members) {
        setMembers((members == null || members.size() == 0) ? "" : LongUtils.join(members));
    }

    @JsonSetter
    public void setMembers(List<ReferenceData> members) {

        List<Long> memberIDs = new ArrayList<>();

        if (members != null && !members.isEmpty()) {
            for (ReferenceData member : members) {
                memberIDs.add(member.get$ref());
            }
        }

        setMemberIDs(memberIDs);
    }

    @Schema(description = "整改人")
    @JsonProperty(value = "members")
    public List<ReferenceData> getMemberRefs() {

        if (StringUtils.isEmpty(this.members)) {
            return new ArrayList<>();
        }

        List<ReferenceData> members = new ArrayList<>();

        for (String member : this.members.split(",")) {
            members.add(new ReferenceData(LongUtils.parseLong(member)));
        }

        return members;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public String getPunchStatus() {
        return punchStatus;
    }

    public void setPunchStatus(String punchStatus) {
        this.punchStatus = punchStatus;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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

    public Date getOriginDate() {
        return originDate;
    }

    public void setOriginDate(Date originDate) {
        this.originDate = originDate;
    }

    public Date getPlanFinishDate() {
        return planFinishDate;
    }

    public void setPlanFinishDate(Date planFinishDate) {
        this.planFinishDate = planFinishDate;
    }

    public String getInternalPunchStatus() {
        return internalPunchStatus;
    }

    public void setInternalPunchStatus(String internalPunchStatus) {
        this.internalPunchStatus = internalPunchStatus;
    }

    public Long getOriginatorId() {
        return originatorId;
    }

    public void setOriginatorId(Long originatorId) {
        this.originatorId = originatorId;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getOriginatorFullName() {
        return originatorFullName;
    }

    public void setOriginatorFullName(String originatorFullName) {
        this.originatorFullName = originatorFullName;
    }

    public String getOriginatorCompany() {
        return originatorCompany;
    }

    public void setOriginatorCompany(String originatorCompany) {
        this.originatorCompany = originatorCompany;
    }

    public Long getPersonInChargeId() {
        return personInChargeId;
    }

    public void setPersonInChargeId(Long personInChargeId) {
        this.personInChargeId = personInChargeId;
    }

    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    public String getQc() {
        return qc;
    }

    public void setQc(String qc) {
        this.qc = qc;
    }

    public Long getQcId() {
        return qcId;
    }

    public void setQcId(Long qcId) {
        this.qcId = qcId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    /**
     * 取得相关联的用户 ID 的集合。
     *
     * @return 相关联的用户 ID 的集合
     */
    @Override
    public Set<Long> relatedUserIDs() {

        Set<Long> userIDs = new HashSet<>();

        if (this.personInChargeId != null && this.personInChargeId != 0L) {
            userIDs.add(this.personInChargeId);
        }
        if (this.qcId != null && this.qcId != 0L) {
            userIDs.add(this.qcId);
        }
        if (!StringUtils.isEmpty(this.members)) {
            List<Long> tmpUserIds = new ArrayList<>();
            Arrays.asList(this.members.split(",")).forEach(
                tmpUserId -> {
                    tmpUserIds.add(LongUtils.parseLong(tmpUserId));
                }
            );
            userIDs.addAll(tmpUserIds);
        }
        if (this.getCreatedBy() != null && this.getCreatedBy() != 0L) {
            userIDs.add(this.getCreatedBy());
        }
        if (this.getLastModifiedBy() != null && this.getLastModifiedBy() != 0L) {
            userIDs.add(this.getLastModifiedBy());
        }

        return userIDs;
    }

    /**
     * 取得相关联的组织 ID 的集合。
     *
     * @return 相关联的组织 ID 的集合
     */
    @Override
    public Set<Long> relatedOrgIDs() {

        Set<Long> orgIDs = new HashSet<>();

        if (departmentId == null || departmentId == 0L) {
            return orgIDs;
        }


        orgIDs.add(departmentId);

        return orgIDs;
    }

}
