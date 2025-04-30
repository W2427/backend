package com.ose.tasks.vo.wbs;


/**
 * WBS 条目运行状态。
 */
public enum WBSEntryRunningStatus {
    PENDING(0),   // 待启动
    RUNNING(1),   // 执行中
    SUSPENDED(2), // 已挂起
    APPROVED(3),  // 正常结束
    REJECTED(4),  // 结果驳回
    IGNORED(5),   // 已忽略
    ISNULL(6);    // NULL

    private int code;

    WBSEntryRunningStatus(int code) {
        this.code = code;
    }

}
