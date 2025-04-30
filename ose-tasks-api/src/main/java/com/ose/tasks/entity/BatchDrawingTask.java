package com.ose.tasks.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmEntityDocsMaterials;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.DrawingHistory;
import com.ose.tasks.entity.drawing.SubDrawing;
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
@Table(name = "batch_tasks_drawing")
public class BatchDrawingTask extends BatchTaskBase {

    private static final long serialVersionUID = -5417450740038468048L;

    @Schema(description = "图纸 ID")
    @Column
    private Long drawingId;

    @Schema(description = "图纸 编号")
    @Column
    private String drawingNo;

    @Schema(description = "图纸流程key")
    @Column
    private String taskDefKey;

    @Schema(description = "图纸流程节点名称")
    @Column
    private String taskDefKeyName;

    @Schema(description = "工作流流程实例")
    @Column(columnDefinition = "text")
    private String actInst;

    @Schema(description = "执行的任务 bpm.act_ru_task")
    @Column(columnDefinition = "text")
    private String ruTask;

    @Schema(description = "下个执行的任务 bpm.act_ru_task list")
    @Column(columnDefinition = "text")
    private String nextRuTask;

    @Schema(description = "主图纸")
    @Column(columnDefinition = "text")
    private String drawing;

    @Schema(description = "图纸详情")
    @Column(columnDefinition = "text")
    private String drawingDetail;

    @Schema(description = "图纸历史")
    @Column(columnDefinition = "text")
    private String drawingHistory;

    @Schema(description = "子图纸")
    @Column(columnDefinition = "text")
    private String subDrawing;

    @Schema(description = "任务流程文档")
    @Column(columnDefinition = "text")
    private String docsMaterials;

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

    public BatchDrawingTask() {
        super();
    }

    public BatchDrawingTask(
        OperatorDTO operator,
        Project project,
        BatchTaskCode batchTaskCode
    ) {
        super(operator, project, batchTaskCode);
    }

    @JsonCreator
    public BatchDrawingTask(@JsonProperty("subDrawing") List<SubDrawing> subDrawing,
                            @JsonProperty("nextActRuTask") List<BpmRuTask> nextRuTask) {
        this.subDrawing = StringUtils.toJSON(subDrawing);
        this.nextRuTask = StringUtils.toJSON(nextRuTask);
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

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getTaskDefKeyName() {
        return taskDefKeyName;
    }

    public void setTaskDefKeyName(String taskDefKeyName) {
        this.taskDefKeyName = taskDefKeyName;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
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

    public String getRuTask() {
        return ruTask;
    }

    public void setRuTask(String ruTask) {
        this.ruTask = ruTask;
    }

    @JsonProperty(value = "actRuTask", access = JsonProperty.Access.READ_ONLY)
    public BpmRuTask getJsonRuTask() {
        if (ruTask != null && !"".equals(ruTask)) {
            return StringUtils.decode(ruTask, new TypeReference<BpmRuTask>() {
            });
        } else {
            return new BpmRuTask();
        }
    }

    @JsonIgnore
    public void setJsonRuTask(BpmRuTask ruTask) {
        if (ruTask != null) {
            this.ruTask = StringUtils.toJSON(ruTask);
        }
    }

    public String getDrawing() {
        return drawing;
    }

    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }

    @JsonProperty(value = "drawing", access = JsonProperty.Access.READ_ONLY)
    public Drawing getJsonDrawing() {
        if (drawing != null && !"".equals(drawing)) {
            return StringUtils.decode(drawing, new TypeReference<Drawing>() {
            });
        } else {
            return new Drawing();
        }
    }

    @JsonIgnore
    public void setJsonDrawing(Drawing drawing) {
        if (drawing != null) {
            this.drawing = StringUtils.toJSON(drawing);
        }
    }

    public String getDrawingDetail() {
        return drawingDetail;
    }

    public void setDrawingDetail(String drawingDetail) {
        this.drawingDetail = drawingDetail;
    }

    @JsonProperty(value = "drawingDetail", access = JsonProperty.Access.READ_ONLY)
    public DrawingDetail getJsonDrawingDetail() {
        if (drawingDetail != null && !"".equals(drawingDetail)) {
            return StringUtils.decode(drawingDetail, new TypeReference<DrawingDetail>() {
            });
        } else {
            return new DrawingDetail();
        }
    }

    @JsonIgnore
    public void setJsonDrawingDetail(DrawingDetail drawingDetail) {
        if (drawingDetail != null) {
            this.drawingDetail = StringUtils.toJSON(drawingDetail);
        }
    }

    public String getDrawingHistory() {
        return drawingHistory;
    }

    public void setDrawingHistory(String drawingHistory) {
        this.drawingHistory = drawingHistory;
    }

    @JsonProperty(value = "drawingHistory", access = JsonProperty.Access.READ_ONLY)
    public DrawingHistory getJsonDrawingHistory() {
        if (drawingHistory != null && !"".equals(drawingHistory)) {
            return StringUtils.decode(drawingHistory, new TypeReference<DrawingHistory>() {
            });
        } else {
            return new DrawingHistory();
        }
    }

    @JsonIgnore
    public void setJsonDrawingHistory(DrawingHistory drawingHistory) {
        if (drawingHistory != null) {
            this.drawingHistory = StringUtils.toJSON(drawingHistory);
        }
    }

    public String getSubDrawing() {
        return subDrawing;
    }

    public void setSubDrawing(String subDrawing) {
        this.subDrawing = subDrawing;
    }

    @JsonProperty(value = "subDrawing", access = JsonProperty.Access.READ_ONLY)
    public List<SubDrawing> getJsonSubDrawing() {
        if (subDrawing != null && !"".equals(subDrawing)) {
            return StringUtils.decode(subDrawing, new TypeReference<List<SubDrawing>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonSubDrawing(List<SubDrawing> subDrawing) {
        if (subDrawing != null) {
            this.subDrawing = StringUtils.toJSON(subDrawing);
        }
    }

    public String getDocsMaterials() {
        return docsMaterials;
    }

    public void setDocsMaterials(String docsMaterials) {
        this.docsMaterials = docsMaterials;
    }

    @JsonProperty(value = "docsMaterials", access = JsonProperty.Access.READ_ONLY)
    public BpmEntityDocsMaterials getJsonDocsMaterials() {
        if (docsMaterials != null && !"".equals(docsMaterials)) {
            return StringUtils.decode(docsMaterials, new TypeReference<BpmEntityDocsMaterials>() {
            });
        } else {
            return new BpmEntityDocsMaterials();
        }
    }

    @JsonIgnore
    public void setJsonDocsMaterials(BpmEntityDocsMaterials docsMaterials) {
        if (docsMaterials != null) {
            this.docsMaterials = StringUtils.toJSON(docsMaterials);
        }
    }

    public String getNextRuTask() {
        return nextRuTask;
    }

    public void setNextRuTask(String nextActRuTask) {
        this.nextRuTask = nextRuTask;
    }

    @JsonProperty(value = "nextActRuTask", access = JsonProperty.Access.READ_ONLY)
    public List<BpmRuTask> getJsonNextRuTask() {
        if (nextRuTask != null && !"".equals(nextRuTask)) {
            return StringUtils.decode(nextRuTask, new TypeReference<List<BpmRuTask>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonNextRuTask(List<BpmRuTask> nextRuTask) {
        if (nextRuTask != null) {
            this.nextRuTask = StringUtils.toJSON(nextRuTask);
        }
    }

}
