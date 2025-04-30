package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.bpm.BpmActTaskConfigDelegateStage;
import com.ose.tasks.vo.bpm.BpmActTaskConfigDelegateType;
import com.ose.vo.BpmTaskType;
import com.ose.tasks.vo.bpm.ProcessType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class BpmActTaskConfigDTO extends BaseDTO {


    private static final long serialVersionUID = 8320093334772417666L;
    //流程节点名称
    @Schema(description = "流程节点名称")
    private String taskName;

    //流程节点DefKey
    @Schema(description = "流程节点定义的键名 UT-REPORT-UPLOAD")
    private String taskDefKey;

    //代理类型
    @Schema(description = "代理类型，如NEXT_ASSIGNEE")
    @Enumerated(EnumType.STRING)
    private BpmActTaskConfigDelegateType delegateType;

    //执行顺序
    @Schema(description = "执行顺序，从小到大顺序执行")
    private Integer orderNo = 0;

    //代理类
    @Schema(description = "代理类全称，首字符小写")
    private String proxy;

    //批处理标识
//    @Schema(description = "批处理标识")
//    private Boolean batchFlag;

    //代理阶段， POST 之后执行， PRE 之前执行。待增加
    @Schema(description = "代理阶段， POST 之后执行， PRE 之前执行。待增加")
    @Enumerated(EnumType.STRING)
    private BpmActTaskConfigDelegateStage delegateStage;

    @Schema(description = "工序类型，如DELIVERY_LIST,CUT_LIST,INDIVIDUAL等")
    @Enumerated(EnumType.STRING)
    private ProcessType processType;


    @Schema(description = "工序分类，如Piping_CONSTRUCTION，Material等")
    private String processCategory;

    @Schema(description = "任务类型")
    private String taskType;


    @Schema(description = "工序，工序阶段-工序")
    private String processKey;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public BpmActTaskConfigDelegateType getDelegateType() {
        return delegateType;
    }

    public void setDelegateType(BpmActTaskConfigDelegateType delegateType) {
        this.delegateType = delegateType;
    }

    public BpmActTaskConfigDelegateStage getDelegateStage() {
        return delegateStage;
    }

    public void setDelegateStage(BpmActTaskConfigDelegateStage delegateStage) {
        this.delegateStage = delegateStage;
    }

    public ProcessType getProcessType() {

        return processType;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

//    public Boolean getBatchFlag() {
//        return batchFlag;
//    }
//
//    public void setBatchFlag(Boolean batchFlag) {
//        this.batchFlag = batchFlag;
//    }

    public String getProcessCategory() {
        return processCategory;
    }

    public void setProcessCategory(String processCategory) {
        this.processCategory = processCategory;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
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
}
