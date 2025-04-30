package com.ose.tasks.vo.bpm;

/**
 * 挂起状态。
 */
public enum ExInspApplyHandleType {

    UPLOAD_REPORT("上传外检报告"),
    UPLOAD_REPORT_CONFIRM("确认上传的报告文件"),
    UPLOAD_REPORT_SKIP("跳过上传"),
    EXTERNAL_INSPECTION_APPLY("外检申请"),
    EXTERNAL_INSPECTION_EMAIL("外检邮件发送"),
    EXTERNAL_INSPECTION_CONFIRM("外检确认"),
    EXTERNAL_INSPECTION_RE_HANDLE("外检再处理"),
    SECOND_UPLOAD_REPORT("二次上传报告"),
    IMPORT_WELD_WELDER("导入焊工焊口信息"),
    ;

    private String displayName;

    ExInspApplyHandleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
