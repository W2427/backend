package com.ose.vo;

/**
 * 数据实体状态值对象。
 */
public enum EntityStatus implements ValueObject {

    INIT("未开始"),
    ACTIVE("进行中"),
    DISABLED("待审核,无效"),
    DELETED("已删除"),
    REJECTED("已驳回"),
    APPROVED("已通过"),
    PENDING("待确认"),
    FINISHED("已完成"),
    SKIPPED("已跳过"),
    CLOSED("已关闭"),
    CANCEL("已取消"),
    SUSPEND("旧版||挂起"),
    CONFIRMED("已确认");

    private String displayName;

    EntityStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
