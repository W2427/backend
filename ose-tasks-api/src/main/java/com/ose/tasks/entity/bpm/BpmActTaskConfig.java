package com.ose.tasks.entity.bpm;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.bpm.BpmActTaskConfigDelegateStage;
import com.ose.tasks.vo.bpm.BpmActTaskConfigDelegateType;
import com.ose.vo.BpmTaskType;
import com.ose.tasks.vo.bpm.ProcessType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 代理类设置 实体类。
 */
@Entity
@Table(name = "bpm_act_task_config")
public class BpmActTaskConfig extends BaseBizEntity {

    private static final long serialVersionUID = -4387445149304502246L;


    //流程节点名称
    @Schema(description = "流程节点名称")
    @Column
    private String taskName;

    //流程节点DefKey
    @Schema(description = "流程节点定义的键名 UT-REPORT-UPLOAD")
    @Column
    private String taskDefKey;

    //代理类型
    @Schema(description = "代理类型，如NEXT_ASSIGNEE")
    @Enumerated(EnumType.STRING)
    @Column
    private BpmActTaskConfigDelegateType delegateType;

    //执行顺序
    @Schema(description = "执行顺序，从小到大顺序执行")
    @Column
    private int orderNo = 0;

    //代理类
    @Schema(description = "代理类全称，首字符小写")
    @Column
    private String proxy;

    //批处理标识
//    @Schema(description = "批处理标识")
//    @Column
//    private boolean batchFlag;

    @Schema(description = "公司 ID")
    @Column(nullable = false, length = 16)
    private Long companyId;

    @Schema(description = "组织 ID")
    @Column(nullable = false, length = 16)
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column(nullable = false, length = 16)
    private Long projectId;

    //代理阶段， POST 之后执行， PRE 之前执行。待增加
    @Schema(description = "代理阶段， POST 之后执行， PRE 之前执行。待增加")
    @Enumerated(EnumType.STRING)
    @Column
    private BpmActTaskConfigDelegateStage delegateStage;

    @Schema(description = "工序类型，如DELIVERY_LIST,CUT_LIST,INDIVIDUAL等")
    @Enumerated(EnumType.STRING)
    @Column
    private ProcessType processType;

    @Schema(description = "工序，工序阶段-工序")
    @Column
    private String processKey;


    @Schema(description = "工序分类，如Piping_CONSTRUCTION，Material等")
    @Column
    private String processCategory;

    @Schema(description = "任务类型")
    @Column
    private String taskType;


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

//    public boolean isBatchFlag() {
//        return batchFlag;
//    }
//
//    public void setBatchFlag(boolean batchFlag) {
//        this.batchFlag = batchFlag;
//    }

    public BpmActTaskConfigDelegateType getDelegateType() {
        return delegateType;
    }

    @JsonSetter
    public void setDelegateType(BpmActTaskConfigDelegateType delegateType) {

        this.delegateType = delegateType;
    }

    public void setDelegateType(String delegateType) {
        this.delegateType = BpmActTaskConfigDelegateType.valueOf(delegateType);
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public BpmActTaskConfigDelegateStage getDelegateStage() {

        return delegateStage;
    }

    @JsonSetter
    public void setDelegateStage(BpmActTaskConfigDelegateStage delegateStage) {

        this.delegateStage = delegateStage;
    }

    public void setDelegateStage(String delegateStage) {
        this.delegateStage = BpmActTaskConfigDelegateStage.valueOf(delegateStage);
    }

    public ProcessType getProcessType() {

        return processType;
    }

    @JsonSetter
    public void setProcessType(ProcessType processType) {

        this.processType = processType;
    }

    public void setProcessType(String processType) {

        this.processType = ProcessType.valueOf(processType);
    }

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
