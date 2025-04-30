package com.ose.vo;


/**
 * 二维码前缀定义。
 */
public enum QrcodePrefixType implements ValueObject {

    DRAWING("D", "图纸二维码"),
    ENTITY("E", "实体二维码"),
    MERTIAL("ME", "材料二维码"),
    WAREHOUSE_LOCATION("W", "库位二维码"),
    MATERTIAL("M", "材料二维码"),
    MATERTIAL_RECEIVE_GOODS("MRG", "材料准备明细二维码"),
    MATERTIAL_RECEIVE_TYPE("MRT", "材料准备明细二维码"),
    CHECKLIST("C", "检查单二维码"),
    EXTERNAL_CONTROL_CHECKLIST("A", "检查申请单二维码"),
    EXTERNAL_CONTROL_REPORT_APPLICATION("L", "检查申请单封面二维码"),
    ENTITY_CUTTING("E", "材料切割二维码"),
    ISO("PII", "管线二维码"),
    LINE("LI", "全管线二维码"),
    SPOOL("PIS", "材料切割二维码"),
    COMPONENT("PIC", "管附件二维码"),
    PIPE_PIECE("PP", "下料段二维码"),
    WELD("WD", "焊口二维码"),
    SAFETY_EQ("SA", "安全设备二维码"),
    OUT_FITTING("STO", "铁舾件"),
    EIT_EQ("EIE", "电气设备"),
    MECH_EQ_PACKAGE("MP", "MECH设备包"),
    EL_EQ_PACKAGE("EP", "EL设备包"),
    IN_EQ_PACKAGE("IP", "IN设备包"),
    TE_EQ_PACKAGE("TEP", "TE设备包"),
    HVAC_EQ_PACKAGE("HP", "HVAC设备包"),
    EIT_COMPONENT("EIC", "电舾件"),
    CABLE("CAB", "电缆"),
    MECHANICAL_EQUIPMENT("MEE", "机械设备"),
    MECH_EQ_COMPONENT("MEC", "机械设备基座"),
    EXTERNAL_CONTROL_REPORT("B", "报告单二维码"),
    WP01("W01", "WP01二维码"),
    WP02("W02", "WP02二维码"),
    WP03("W03", "WP03二维码"),
    WP04("W04", "WP04二维码"),
    WP05("W05", "WP05二维码"),
    STRUCTURE_WELD("SW", "结构焊口二维码"),
    DUCT("U", "DUCT"),
    HVAC_COMPONENT("V", "DUCT");


    private String code;
    private String displayName;

    QrcodePrefixType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    // 根据key获取枚举
    public static QrcodePrefixType getEnumByType(String code) {
        for (QrcodePrefixType temp : QrcodePrefixType.values()) {
            if (temp.getCode().equals(code)) {
                return temp;
            }
        }
        return null;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

}
