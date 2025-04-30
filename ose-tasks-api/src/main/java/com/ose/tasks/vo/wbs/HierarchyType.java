package com.ose.tasks.vo.wbs;


import com.ose.vo.ValueObject;

/**
 * 层级节点类型。
 */
public enum HierarchyType implements ValueObject {

    PIPING("PIPING"),
    ELECTRICAL("ELECTRICAL"),
    CABLE_PULLING_PACKAGE("CABLE_PULLING_PACKAGE"),
//    AREA("AREA"),
//    LAYER_PACKAGE("LAYER_PACKAGE", "层/包"),
    PRESSURE_TEST_PACKAGE("PRESSURE_TEST_PACKAGE", "试压包"),
    CLEAN_PACKAGE("CLEAN_PACKAGE", "清洁包"),
//    SECTOR("SECTOR"),
//    SUB_SECTOR("SUB_SECTOR"),
//    SYSTEM("SYSTEM"),
    SUB_SYSTEM("SUB_SYSTEM", "子系统"),
    NOT_VALID("NOT_VALID", "无效的"),
//    ISO("ISO", "区域"),
    STRUCTURE("STRUCTURE", "结构"),
    GENERAL("GENERAL", "通用");

    private String displayName;
    private String description;

    HierarchyType(String displayName) {
        this(displayName, null);
    }

    HierarchyType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return description;
    }

}
