package com.ose.tasks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 批处理任务数据实体。
 */
@Entity
@Table(name = "batch_construct_task")
public class BatchConstructTask extends BatchTaskBase {

    private static final long serialVersionUID = -5417450740038468048L;

    @Schema(description = "图纸 ID")
    @Column
    private Long entityId;

    @Schema(description = "图纸 编号")
    @Column
    private String entityNo;

    @Schema(description = "工作流流程实例")
    @Column(columnDefinition = "text")
    private String actInst;

    // 运行日志
    @Transient
    @JsonIgnore
    private List<String> logMessages = new ArrayList<>();

    @Schema(description = "运行日志")
    @Lob
    @Column(length = 4096)
    private String log;

    // 错误消息
    @Transient
    @JsonIgnore
    private List<String> errorMessages = new ArrayList<>();

    @Schema(description = "错误日志")
    @Lob
    @Column(length = 4096)
    private String errorLog;

    public BatchConstructTask() {
        super();
    }

    public BatchConstructTask(
        OperatorDTO operator,
        Project project,
        BatchTaskCode batchTaskCode
    ) {
        super(operator, project, batchTaskCode);
    }

    @Override
    public void setResult(BatchResultDTO result) {

        if (result == null) {
            return;
        }

        super.setResult(result);
        setLog(result.getLog());
    }

    public String getLog() {
        return log;
    }

    @JsonSetter
    public void setLog(String log) {
        setLog(new ArrayList<>(
            Arrays.asList(log.split("[\r\n]+"))
        ));
    }

    public void setLog(List<String> logMessages) {
        this.logMessages = logMessages;
        log = String.join("\r\n", logMessages);
    }

    public void addLog(String logMessage) {
        logMessages.add(logMessage);
        setLog(logMessages);
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        setErrorLog(new ArrayList<>(
            Arrays.asList(errorLog.split("[\r\n]+"))
        ));
    }

    public void setErrorLog(List<String> errorMessages) {
        this.errorMessages = errorMessages;
        errorLog = String.join("\r\n", errorMessages);
    }

    public void addError(String errorMessage) {
        errorMessages.add(errorMessage);
        setErrorLog(errorMessages);
    }

    public String getActInst() {
        return actInst;
    }

    public void setActInst(String actInst) {
        this.actInst = actInst;
    }

    @JsonProperty(value = "actInst", access = JsonProperty.Access.READ_ONLY)
    public BpmActivityInstanceBase getJsonActInst() {
        if (actInst != null && !"".equals(actInst)) {
            return StringUtils.decode(actInst, new TypeReference<BpmActivityInstanceBase>() {
            });
        } else {
            return new BpmActivityInstanceBase();
        }
    }

    @JsonIgnore
    public void setJsonActInst(BpmActivityInstanceBase actInst) {
        if (actInst != null) {
            this.actInst = StringUtils.toJSON(actInst);
        }
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public List<String> getLogMessages() {
        return logMessages;
    }

    public void setLogMessages(List<String> logMessages) {
        this.logMessages = logMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
