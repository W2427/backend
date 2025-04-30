package com.ose.tasks.vo.qc;

import com.ose.vo.ValueObject;

public enum ProcessCategory implements ValueObject {

    // 焊口工序类型
    VT("外观检查"),
    PMI("材质分析"),
    NDT("无损探伤"),
    PWHT("焊后热处理"),
    HD("硬度测试"),
    WELD("焊接"),
    FITUP("组对"),
    BEVEL_GROUND("坡口打磨"),
    TOUCHUP("补漆"),

    // 管段工序类型
    CUTTING("下料"),
    BEND("弯管"),
    BEVEL("坡口打磨"),

    // 单管工序类型
    STRENGTH_TEST("强度试验"),
    TRANSPORT("配送"),
    PAINTING("涂装"),
    SURFACE_TREATMENT("表面处理"),
    SPOOL_INSTALL("安装"),
    PACK("打包工作"),
    RELEASE("打包放行"),

    // 管线（ISO）工序类型
    LINE_CHECK("管道核查"),
    BLIND_FABRICATE("盲板预制"),
    BLIND_INSTALL("盲板安装"),
    PRESSURE_TEST("试压"),
    BLIND_REMOVE("盲板拆除"),
    BLOW("吹扫"),
    WATER_FLUSH("清洁管道串水"),
    OIL_FLUSH("清洁管道串油"),
    SHIELD_FABRICATE("保温预制"),
    FILLING_INSTALL("保温安装"),
    SHIELD_INSTALL("保温外层安装");

    // ISO 工序类型

    private String displayName;

    ProcessCategory(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
