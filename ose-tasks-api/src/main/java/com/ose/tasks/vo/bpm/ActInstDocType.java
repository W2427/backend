package com.ose.tasks.vo.bpm;

import com.ose.vo.ValueObject;


public enum ActInstDocType implements ValueObject {

    UPLOAD_FILE("上传文件"),
    DESIGN_DRAWING("图纸"),
    EXTERNAL_INSPECTION("外检报告"),
    EXTERNAL_INSPECTION_RESIGN("外检重签报告"),
    CHECK_LIST("校验清单"),
    TRANSMITTAL_REPORT("传送单"),
    DESIGN_CHANGE_REVIEW_FORM("设计变更评审单"),
    VARIATION_ORDER_REQUEST_FORM("签字版设计变更评审单"),
    JOB_CHANGE_ORDER_FORM("作业变更指令单"),
    NESTING_FILE("套料清单"),
    DELIVERY_LIST("配送清单");

    private String displayName;

    ActInstDocType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
