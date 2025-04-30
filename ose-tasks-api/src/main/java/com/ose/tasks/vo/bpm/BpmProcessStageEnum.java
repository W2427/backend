package com.ose.tasks.vo.bpm;

public enum BpmProcessStageEnum {

    ////* 此代码跟工作流设定相关联，禁止删除跟修改 *////
    FABRICATION("FABRICATION", "预制阶段"),
    CUTTING("CUTTING", "下料阶段"),
    INSTALLATION("INSTALLATION", "安装阶段"),
    PRESSURE_TEST("PRESSURE_TEST", "试压阶段");

    private String type;
    private String displayName;

    private BpmProcessStageEnum(String type, String displayName) {
        this.type = type;
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
