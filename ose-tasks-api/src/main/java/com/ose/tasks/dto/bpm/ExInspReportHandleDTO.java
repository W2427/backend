package com.ose.tasks.dto.bpm;

import java.util.List;
import java.util.Map;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.ose.dto.BaseDTO;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.tasks.vo.SuspensionState;

import com.ose.vo.InspectResult;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 外检报告上传
 */
public class ExInspReportHandleDTO extends BaseDTO {

    private static final long serialVersionUID = -34948993806220141L;

    @Schema(description = "外检协调id")
    private Long scheduleId;

    @Schema(description = "外检编号")
    private String reportSeriesNo;

    // 工作流实例id
    @Schema(description = "工作流实例id(task端)")
    private Long bpmActivityInstanceId;

    // 工作流实例id
    @Schema(description = "工作流实例id(bpm端)")
    private Long actInstId;

    // 工作流实例id
    @Schema(description = "工作任务id")
    private Long taskId;

    // 实体编号
    @Schema(description = "实体编号")
    private String entityNo;

    // 实体Id
    @Schema(description = "实体Id")
    private Long entityId;

    // 实体类型分类
    @Schema(description = "实体类型分类")
    private String entityType;

    // 实体类型分类
    @Schema(description = "实体类型")
    private String entitySubType;

    // 报告号
    @Schema(description = "报告号")
    private String reportNo;

    // 报告名称
    @Schema(description = "报告名称")
    private String reportName;

    // 挂起状态
    @Schema(description = "挂起状态")
    @Enumerated(EnumType.STRING)
    private SuspensionState suspensionState;

    // 完成状态
    @Schema(description = "完成状态")
    @Enumerated(EnumType.STRING)
    private ActInstFinishState finishState;

    // 工序分类
    @Schema(description = "工序阶段")
    private String processStage;

    // 工序
    @Schema(description = "工序")
    private String process;

    @Schema(description = "报告")
    private ActReportDTO report;

    @Schema(description = " 节点命令,前台返给后台")
    private Map<String, Object> command;

    @Schema(description = " gateway 后台返给前台")
    private List<TaskGatewayDTO> gateway;

    @Schema(description = "Inspect Result 检验结果")
    private InspectResult inspectResult;

    @Schema(description = "检验报告子类型，当前用于区分报告子类型 管道入库，阀门入库等")
    private String reportSubType;

    @Schema(description = "生成的报告类型，初始外检报告；重新合成外检报告；非报检报告")
    private String reportType;

    public Long getBpmActivityInstanceId() {
        return bpmActivityInstanceId;
    }

    public void setBpmActivityInstanceId(Long bpmActivityInstanceId) {
        this.bpmActivityInstanceId = bpmActivityInstanceId;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getProcessStage() {
        return processStage;
    }

    public void setProcessStage(String processStage) {
        this.processStage = processStage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public SuspensionState getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(SuspensionState suspensionState) {
        this.suspensionState = suspensionState;
    }

    public ActInstFinishState getFinishState() {
        return finishState;
    }

    public void setFinishState(ActInstFinishState finishState) {
        this.finishState = finishState;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public ActReportDTO getReport() {
        return report;
    }

    public void setReport(ActReportDTO report) {
        this.report = report;
    }


    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getReportSeriesNo() {
        return reportSeriesNo;
    }

    public void setReportSeriesNo(String reportSeriesNo) {
        this.reportSeriesNo = reportSeriesNo;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Map<String, Object> getCommand() {
        return command;
    }

    public void setCommand(Map<String, Object> command) {
        this.command = command;
    }

    public List<TaskGatewayDTO> getGateway() {
        return gateway;
    }

    public void setGateway(List<TaskGatewayDTO> gateway) {
        this.gateway = gateway;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public InspectResult getInspectResult() {
        return inspectResult;
    }

    public void setInspectResult(InspectResult inspectResult) {
        this.inspectResult = inspectResult;
    }

    public String getReportSubType() {
        return reportSubType;
    }

    public void setReportSubType(String reportSubType) {
        this.reportSubType = reportSubType;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
}
