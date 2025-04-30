package com.ose.material.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.vo.DisciplineCode;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 材料批处理任务数据实体。
 */
@Entity
@Table(name = "mm_import_batch_task")
public class MmImportBatchTask extends MmImportBatchTaskBase {

    private static final long serialVersionUID = -1158241852601375308L;

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

    @Schema(description = "备注")
    @Lob
    @Column(length = 4096)
    private String remarks;

    @Schema(description = "专业")
    @Enumerated(EnumType.STRING)
    private DisciplineCode discipline;

    public MmImportBatchTask() {
        super();
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public DisciplineCode getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineCode discipline) {
        this.discipline = discipline;
    }
}
