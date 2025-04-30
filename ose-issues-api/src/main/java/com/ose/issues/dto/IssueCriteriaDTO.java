package com.ose.issues.dto;

import com.ose.issues.vo.IssueSource;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class IssueCriteriaDTO extends ExperienceCriteriaDTO {

    private static final long serialVersionUID = 4314674487765455709L;

    @Schema(description = "问题 ID 列表")
    private List<Long> issueIDs;

    @Schema(description = "参与者 ID")
    private Long participantId;

    @Schema(description = "责任人ID")
    private Long leaderId;

    @Schema(description = "完成日开始时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date finishedStartTime;

    @Schema(description = "完成结束时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date finishedEndTime;

    @Schema(description = "问题来源")
    private List<IssueSource> source;

    @Schema(description = "状态")
    private List<EntityStatus> status;

    @Schema(description = "责任人ID")
    private Long memberId;

    @Schema(description = "部门ID")
    private Long departmentId;

    @Schema(description = "模块")
    private List<String> modules;


    @Schema(description = "子系统")
    private List<String> subSystems;

    public List<Long> getIssueIDs() {
        return issueIDs;
    }

    public void setIssueIDs(List<Long> issueIDs) {
        this.issueIDs = issueIDs;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public Date getFinishedStartTime() {
        return finishedStartTime;
    }

    public void setFinishedStartTime(Date finishedStartTime) {
        this.finishedStartTime = finishedStartTime;
    }

    public Date getFinishedEndTime() {
        return finishedEndTime;
    }

    public void setFinishedEndTime(Date finishedEndTime) {
        this.finishedEndTime = finishedEndTime;
    }

    public List<EntityStatus> getStatus() {
        return status;
    }

    public void setStatus(List<EntityStatus> status) {
        this.status = status;
    }

    public List<IssueSource> getSource() {
        return source;
    }

    public void setSource(List<IssueSource> source) {
        this.source = source;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    public List<String> getSubSystems() {
        return subSystems;
    }

    public void setSubSystems(List<String> subSystems) {
        this.subSystems = subSystems;
    }
}
