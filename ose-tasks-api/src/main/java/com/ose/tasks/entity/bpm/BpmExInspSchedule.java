package com.ose.tasks.entity.bpm;

import java.util.*;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.vo.EntityStatus;
import com.ose.vo.InspectResult;
import com.ose.tasks.vo.bpm.ExInspScheduleCoordinateCategory;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.collections.MapUtils;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "bpm_external_inspection_schedule")
public class BpmExInspSchedule extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    @Schema(description = "ORG ID")
    @Column
    private Long orgId;

    @Schema(description = "PROJECT ID")
    @Column
    private Long projectId;

    @Schema(description = "报检名称")
    @Column
    private String seriesNos;

    @Schema(description = "附件")
    @Column(columnDefinition = "text")
    private String attachments;

    @Schema(description = "报检名称")
    @Column
    private String name;

    @Schema(description = "报验方")
    @Column(columnDefinition = "text")
    private String externalInspectionParties;

    @Schema(description = "外检时间")
    @Column(name = "external_inspection_time")
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date externalInspectionTime;

    @Schema(description = "报检人")
    @Column
    private Long operator;

    @Schema(description = "报检人姓名")
    @Column
    private String operatorName;

    @Schema(description = "报检人 email")
    @Column
    private String operatorEmail;

    @Schema(description = "状态")
    @Column
    @Enumerated(EnumType.STRING)
    private ReportStatus state;

    @Schema(description = "运行状态")
    @Column
    @Enumerated(EnumType.STRING)
    private EntityStatus runningStatus;

    @Schema(description = "备注")
    @Column
    private String comment;

    @Schema(description = "位置")
    @Column
    private String location;

    @Schema(description = "专业")
    @Column
    private String discipline;

    @Schema(description = "区分")
    @Column
    @Enumerated(EnumType.STRING)
    private ExInspScheduleCoordinateCategory coordinateCategory;

    @Schema(description = "外检结果: A;B;C")
    @Enumerated(EnumType.STRING)
    private InspectResult resultType;

    @Schema(description = "当前选择的网关")
    @Column
    private String currentCommand;

    @Schema(description = "是否单个报告")
    @Column
    private Boolean isSingleReport;

    @Schema(description = "是否发送邮件")
    @Column
    private Boolean isSendEmail;

    public InspectResult getResultType() {
        return resultType;
    }

    public void setResultType(InspectResult resultType) {
        this.resultType = resultType;
    }

    public String getExternalInspectionParties() {
        return externalInspectionParties;
    }

    public void setExternalInspectionParties(String externalInspectionParties) {
        this.externalInspectionParties = externalInspectionParties;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getExternalInspectionTime() {
        return externalInspectionTime;
    }

    public void setExternalInspectionTime(Date externalInspectionTime) {
        this.externalInspectionTime = externalInspectionTime;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public ReportStatus getState() {
        return state;
    }

    public void setState(ReportStatus state) {
        this.state = state;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public ExInspScheduleCoordinateCategory getCoordinateCategory() {
        return coordinateCategory;
    }

    public void setCoordinateCategory(ExInspScheduleCoordinateCategory coordinateCategory) {
        this.coordinateCategory = coordinateCategory;
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


    @JsonProperty(value = "attachments", access = JsonProperty.Access.READ_ONLY)
    public List<ActReportDTO> getJsonAttachments() {
        if (attachments != null && !"".equals(attachments)) {
            return StringUtils.decode(attachments, new TypeReference<List<ActReportDTO>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonAttachments(List<ActReportDTO> attachments) {
        if (attachments != null) {
            this.attachments = StringUtils.toJSON(attachments);
        }
    }

    @JsonIgnore
    public void addJsonAttachments(ActReportDTO attachment) {
        List<ActReportDTO> jsonAttachments = getJsonAttachments();
        if (attachments != null) {
            jsonAttachments.add(attachment);
            this.attachments = StringUtils.toJSON(jsonAttachments);
        }
    }


    @JsonProperty(value = "externalInspectionParties", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonExternalInspectionParties() {
        if (externalInspectionParties != null && !"".equals(externalInspectionParties)) {
            return StringUtils.decode(externalInspectionParties, new TypeReference<List<String>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonExternalInspectionParties(List<String> externalInspectionParties) {
        if (externalInspectionParties != null) {
            this.externalInspectionParties = StringUtils.toJSON(externalInspectionParties);
        }
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorEmail() {
        return operatorEmail;
    }

    public void setOperatorEmail(String operatorEmail) {
        this.operatorEmail = operatorEmail;
    }

    public String getSeriesNos() {
        return seriesNos;
    }

    public void setSeriesNos(String seriesNos) {
        this.seriesNos = seriesNos;
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

    public Boolean getSingleReport() {
        return isSingleReport;
    }

    public void setSingleReport(Boolean singleReport) {
        isSingleReport = singleReport;
    }

    public Boolean getSendEmail() {
        return isSendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        isSendEmail = sendEmail;
    }

    public EntityStatus getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(EntityStatus runningStatus) {
        this.runningStatus = runningStatus;
    }
}
