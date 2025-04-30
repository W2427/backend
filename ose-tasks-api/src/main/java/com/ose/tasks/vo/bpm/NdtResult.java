package com.ose.tasks.vo.bpm;

import com.ose.vo.ValueObject;

/**
 * NDT 子类型执行结果 RT UT MT PT 类型。
 */
public enum NdtResult implements ValueObject {

    REJECT("驳回"),
    RE_SAMPLE("重新抽取"),
    RESAMPLE("重新抽取"),
    REPAIR("返修"),
    NDT_OK("合格"),
    NDT_NG("不合格"),
    SKIP_NDT("跳过NDT"),
    ACCEPT("接受");

    private String displayName;

    NdtResult(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
