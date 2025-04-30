package com.ose.tasks.vo.bpm;

import com.ose.vo.ValueObject;

/**
 * 工作流配置代理阶段，PRE POST。
 */
public enum BpmActTaskConfigDelegateStage implements ValueObject {

    PREPARE("PREPARE", "数据准备类型，为处理页面预加载的数据"),
    PRE("PRE", "前置处理"),
    POST("POST", "后置处理"),
    COMPLETE("COMPLETE", "完成处理"),
    BATCH_PREPARE("BATCH_PREPARE", "批处理数据准备类型，为处理页面预加载的数据"),
    BATCH_PRE("BATCH_PRE", "批处理前置处理"),
    BATCH_POST("BATCH_POST", "批处理后置处理"),
    BATCH_COMPLETE("BATCH_COMPLETE", "批处理完成处理"),
    PRE_CREATE("PRE_CREATE", "预创建"),
    CREATE("CREATE", "创建"),
    SUSPEND("SUSPEND", "暂停"),
    UN_SUSPEND("UN_SUSPEND", "取消暂停"),
    POST_CREATE("POST_CREATE", "创建后"),
    REVOCATION("REVOCATION", "撤回"),
    PRE_BATCH_REVOCATION("PRE_BATCH_REVOCATION", "前置批量撤回"),
    POST_BATCH_REVOCATION("BATCH_REVOCATION", "后置批量撤回");

    private String code;
    private String displayName;

    BpmActTaskConfigDelegateStage(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

}
