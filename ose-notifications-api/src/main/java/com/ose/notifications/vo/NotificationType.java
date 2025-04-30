package com.ose.notifications.vo;

import com.ose.vo.ValueObject;

/**
 * 通知分类。
 */
public enum NotificationType implements ValueObject {

    // 材料业务通知
    MATERIAL_PURCHASE_REQUISITION_STATE_CHANGE("材料请购单状态变化通知"),
    MATERIAL_CONTRACT_STATE_CHANGE("材料合同变化通知"),
    MATERIAL_DELIVERY_WORKFLOW_FINISH("材料配送工作流完成通知"),
    MATERIAL_STOCKING_WORKFLOW_FINISH("材料入库工作流完成通知"),

    // 图纸业务工作流
    DRAWING_CONFIGURATION_DETAILS("图纸配置详细信息"),
    DRAWING_REVIEW_WORKFLOW_STATE_CHANGE("图纸审核工作流状态变化通知"),
    DRAWING_MODIFYING_WORKFLOW_STATE_CHANGE("图纸变更工作流状态变化通知"),

    // 问题业务通知
    ISSUE_STATUS_CHANGE("问题状态变更通知"),
    ISSUE_RESOLVER_CHANGE("问题负责人更新通知"),
    ISSUE_CONTENT_UPDATE("问题内容更新通知"),

    // 建造业务通知
    CONSTRUCTION_EXTERNAL_INSPECTION("建造外检报验"),
    CONSTRUCTION_INTERNAL_INSPECTION("建造内检报验"),

    // 报表业务通知
    REPORT_DAILY("日报"),
    REPORT_WEEKLY("周报"),

    // 系统
    SYSTEM_SERVICE_ACTIVITY("系统服务动态"),
    SYSTEM_TUTORIAL("系统使用帮助"),
    SYSTEM_UPGRADE_INSTRUCTIONS("系统升级说明"),

    // 消息
    MESSAGE_INTERNAL("站内信"),

    WORKING_HOURS("工时信息"),

    ATTENDANCE_LOCKED("考勤锁定信息");

    private String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
