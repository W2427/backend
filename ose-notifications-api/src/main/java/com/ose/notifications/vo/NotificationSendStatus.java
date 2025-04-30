package com.ose.notifications.vo;

/**
 * 通知发送状态。
 */
public enum NotificationSendStatus {

    // 等待发送
    PENDING,

    // 发送中
    SENDING,

    // 发送失败
    FAILED,

    // 发送成功
    SUCCESSFUL

}
