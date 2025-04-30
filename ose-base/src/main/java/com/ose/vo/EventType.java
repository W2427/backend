package com.ose.vo;

//事件的各种类型
public enum EventType {
    BPM_REPORT_CHECK_ERROR(0),
    BPM_OPT_LOCK_ERROR(1),
    PLAN_QUEUE(1),
    MAIL(3);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
