package com.ose.tasks.vo.bpm;

public enum BpmsProcessNameEnum {

    ////* 此代码跟工作流设定相关联，禁止删除跟修改 *////
    // GateWay
    PMI("PMI", "材质分析"),
    NDT("NDT", "探伤"),
    PWHT("PWHT", "焊后热处理"),
    HD("HD", "硬度测试"),
    WELD("WELD", "焊接"),
    FITUP("FITUP", "组对"),
    BEVEL_GROUND("BEVEL_GROUND", "弯管组"),
    BEVEL("BEVEL", "弯管"),
    CUTTING("CUTTING", "下料"),
    DRAWING_PARTIAL_UPDATE("DRAWING_PARTIAL_UPDATE", "设计局部升版"),
    DRAWING_INTEGRAL_UPDATE("DRAWING_INTEGRAL_UPDATE", "整体升版"),
    ENGINEERING("ENGINEERING", "设计出图"),
    CHANGE_LEAD_BY_DRAWING("CHANGE_LEAD_BY_DRAWING", "变更-设计变更"),
    CHANGE_LEAD_BY_CONSTRUCTION("CHANGE_LEAD_BY_CONSTRUCTION", "变更-建造变更"),
    PUNCHLIST("PUNCHLIST", "质量控制-遗留问题"),
    SPOOL_INSTALL("SPOOL_INSTALL", "单管现场安装"),
    AIR_TIGHTNESS("AIR_TIGHTNESS", "系统密性"),
    SURFACE_TREATMENT("SURFACE_TREATMENT", "表面处理"),
    PAINTING("PAINTING", "涂装"),
    MATERIAL_ISSUE("MATERIAL_ISSUE", "材料出库"),
    MATERIAL_RECEIVE("MATERIAL_RECEIVE", "材料入库"),
    MATERIAL_PROCUREMENT("MATERIAL_PROCUREMENT", "采购"),
    SPOOL_TRANSPORT("SPOOL_TRANSPORT", "单管配送"),
    PIPE_PIECE_TRANSPORT("PIPE_PIECE_TRANSPORT", "管段配送"),
    ENGINEERING_MTO("ENGINEERING_MTO", "设校审MTO"),
    STRENGTH_TEST("PRESSURE_TEST", "强度测试"),
    SPOOL_RELEASE("SPOOL_RELEASE", "预制单管放行"),
    PRE_PAINTING_HANDOVER("PRE_PAINTING_HANDOVER", "涂装前交接"),
    POST_PAINTING_HANDOVER("POST_PAINTING_HANDOVER", "涂装后交接"),
    DRAWING_REDMARK("DRAWING-REDMARK", "现场标记图"),
    BOM_ISSUE("BOM_ISSUE", "现场标记图"),
    INSULATION_INSTALL("INSULATION_INSTALL", "绝热安装"),
    AIR_BLOW("AIR_BLOW", "吹扫"),
    CLADDING_INSTALL("CLADDING_INSTALL", "外层安装"),
    LINE_CHECK("LINE_CHECK", "管线核查"),
    BOX_PACK("BOX_PACK", " 打包工作");

    private String type;
    private String displayName;

    private BpmsProcessNameEnum(String type, String displayName) {
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
