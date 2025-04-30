package com.ose.tasks.vo.qc;

import com.ose.vo.ValueObject;

/**
 * 项目条目类型。
 */
public enum ReportStatus implements ValueObject {

    INIT("申请初始生成"),
    EMAIL_SENT("邮件已发送"),
    UPLOADED("报告已回传"),
    DONE("报告已处理"),
    CANCEL("申请被废止"),
    RUNNING("正在处理"),

    SKIP_UPLOAD("报告跳过上传"),
    PENDING("报告需补传"),
    COMMENTS("有意见报告"),

    REHANDLE_INIT("再处理报告已生成"),
    REHANDLE_MID("再处理报告待回传"),
    REHANDLE_UPLOADED("再处理报告已回传"),
    REHANDLE_SKIP_UPLOAD("再处理报告跳过上传"),
    REHANDLE_DONE("再处理报告已处理"),
    CONFIRMED("NDT报告确认");

    private String displayName;

    ReportStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
