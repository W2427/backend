package com.ose.tasks.entity.bpm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.entity.Itp;
import com.ose.tasks.entity.report.ReportConfig;
import com.ose.tasks.vo.bpm.ProcessType;
import com.ose.tasks.vo.drawing.RuleType;
import com.ose.tasks.vo.qc.ITPType;
import com.ose.tasks.vo.qc.InspectType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

/**
 * 工序实体类。
 */
@Entity
@Table(
    name = "bpm_process_version_rule"
)
public class BpmProcessVersionRule extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    // 项目 ID
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // 组织 ID
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    // 组织 ID
    @Column(name = "process_id", nullable = false)
    private Long processId;

    // 工序名称
    @Column(name = "rule_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RuleType ruleType;

    @Schema(description = "固定前缀")
    @Column
    private String fixedPrefix;

    @Schema(description = "固定字符")
    @Column
    private String fixedCharacter;

    @Schema(description = "起始字符")
    @Column
    private String startPoint;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public String getFixedPrefix() {
        return fixedPrefix;
    }

    public void setFixedPrefix(String fixedPrefix) {
        this.fixedPrefix = fixedPrefix;
    }

    public String getFixedCharacter() {
        return fixedCharacter;
    }

    public void setFixedCharacter(String fixedCharacter) {
        this.fixedCharacter = fixedCharacter;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }
}
