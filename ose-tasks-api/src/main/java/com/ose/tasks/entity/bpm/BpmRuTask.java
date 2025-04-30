package com.ose.tasks.entity.bpm;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseEntity;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.vo.BpmTaskType;
import com.ose.tasks.vo.bpm.ExInspApplyStatus;
import com.ose.vo.InspectResult;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.collections.MapUtils;


/**
 * The persistent class for the act_ru_task database table.
 */
@Entity
@Table(name = "bpm_ru_task"
    ,
    indexes = {
        @Index(columnList = "act_inst_id,assignee_"),
        @Index(columnList = "act_inst_id,taskType,applyStatus,assignee_"),
        @Index(columnList = "task_def_key_"),
        @Index(columnList = "category_"),
    })
public class BpmRuTask extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "assignee_")
    private String assignee;

    @Column(name = "category_")
    private String category;

    @Column(name = "create_time_")
    private Timestamp createTime;

    @Column(name = "delegation_")
    private String delegation;

    @Column(name = "description_")
    private String description;

    @Column(name = "name_")
    private String name;

    @Column(name = "owner_")
    private String owner;

    @Column(name = "parent_task_id_")
    private Long parentTaskId;

    @Column(name = "suspension_state_")
    private int suspensionState;

    @Column(name = "task_def_key_")
    private String taskDefKey;

    @Column(name = "tenant_id_", length = 20)
    private String tenantId;

    @Column(name = "act_inst_id")
    private Long actInstId;

    @Column(name = "documents", columnDefinition = "text")
    private String documents;

    @Column
    @Enumerated(EnumType.STRING)
    private ExInspApplyStatus applyStatus;

    @Schema(description = " 会签标识")
    @Column
    private boolean signFlag = false;

    @Schema(description = "报告 附件")
    @Column(columnDefinition = "text")
    private String reports;

    @Schema(description = "任务类型")
    @Column
    private String taskType;

    @Schema(description = "处理状态")
    @Column
    private Boolean isHandling;

    @Schema(description = "报验结果")
    @Column
    @Enumerated(EnumType.STRING)
    private InspectResult inspectResult;

    @Schema(description = "外检处理的 网关 指令")
    @Column
    private String currentCommand;

    @Schema(description = "任务序列号") //actInstId + taskDefKey + seq唯一确定一个ruTask
    @Column
    private int seq = 0;

    public BpmRuTask() {
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getDelegation() {
        return delegation;
    }

    public void setDelegation(String delegation) {
        this.delegation = delegation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public int getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(int suspensionState) {
        this.suspensionState = suspensionState;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public List<ActReportDTO> getJsonDocuments() {
        if (documents != null && !"".equals(documents)) {
            return StringUtils.decode(documents, new TypeReference<List<ActReportDTO>>() {
            });
        } else {
            return new ArrayList<ActReportDTO>();
        }
    }

    public void setJsonDocuments(List<ActReportDTO> documents) {
        if (documents != null) {
            this.documents = StringUtils.toJSON(documents);
        }
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public ExInspApplyStatus getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(ExInspApplyStatus applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getReports() {
        return reports;
    }

    public void setReports(String report) {
        this.reports = report;
    }

    @JsonIgnore
    public void setJsonReports(List<ActReportDTO> reports) {
        if (reports != null) {
            this.reports = StringUtils.toJSON(reports);
        }
    }

    @JsonIgnore
    public void addJsonReports(ActReportDTO report) {
        if (report != null) {
            List<ActReportDTO> reportList = getJsonReportsReadOnly();
            for (int i = reportList.size() - 1; i >= 0; i--) {
                if (reportList.get(i).getReportQrCode().equals(report.getReportQrCode())) {
                    reportList.remove(i);
                }
            }
            reportList.add(report);
            this.reports = StringUtils.toJSON(reportList);
        }
    }

    @JsonProperty(value = "reports", access = JsonProperty.Access.READ_ONLY)
    public List<ActReportDTO> getJsonReportsReadOnly() {
        if (reports != null && !"".equals(reports)) {
            return StringUtils.decode(reports, new TypeReference<List<ActReportDTO>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    public boolean isSignFlag() {
        return signFlag;
    }

    public void setSignFlag(boolean signFlag) {
        this.signFlag = signFlag;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        try {
            BpmTaskType.valueOf(taskType);
            this.taskType = taskType;
        } catch (Exception e) {
            this.taskType = null;
        }
    }

    public Boolean getHandling() {
        return isHandling;
    }

    public void setHandling(Boolean handling) {
        this.isHandling = handling;
    }

    public InspectResult getInspectResult() {
        return inspectResult;
    }

    public void setInspectResult(InspectResult inspectResult) {
        this.inspectResult = inspectResult;
    }

    public String getCurrentCommand() {
        return currentCommand;
    }

    public void setCurrentCommand(String currentCommand) {
        this.currentCommand = currentCommand;
    }

    @JsonProperty(value = "currentCommand", access = JsonProperty.Access.READ_ONLY)
    public List<TaskCommandDTO> getJsonCurrentCommand() {
        if (currentCommand != null && !"".equals(currentCommand)) {
            return StringUtils.decode(currentCommand, new TypeReference<List<TaskCommandDTO>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    public Map<String, Object> getMapCurrentCommand() {
        if (currentCommand != null && !"".equals(currentCommand)) {
            List<TaskCommandDTO> commands = StringUtils.decode(currentCommand, new TypeReference<List<TaskCommandDTO>>() {
            });
            Map<String, Object> commandMap = new HashMap<>();
            commands.forEach(command -> {
                commandMap.put(command.getKey(), command.getCondition());
            });
            return commandMap;
        } else {
            return new HashMap<>();
        }
    }


    @JsonIgnore
    public void setJsonCurrentCommand(List<TaskCommandDTO> currentCommand) {
        if (currentCommand != null) {
            this.currentCommand = StringUtils.toJSON(currentCommand);
        }
    }


    @JsonIgnore
    public void setJsonCurrentCommand(Map<String, Object> currentCommand) {
        if (!MapUtils.isEmpty(currentCommand)) {
            List<TaskCommandDTO> commandDTOS = new ArrayList<>();
            for (Map.Entry<String, Object> command : currentCommand.entrySet()) {
                TaskCommandDTO taskCommandDTO = new TaskCommandDTO();
                taskCommandDTO.setKey(command.getKey());
                taskCommandDTO.setCondition(command.getValue());
                commandDTOS.add(taskCommandDTO);
            }
            this.currentCommand = StringUtils.toJSON(commandDTOS);
        }
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
