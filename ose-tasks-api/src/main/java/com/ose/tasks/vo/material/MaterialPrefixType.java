package com.ose.tasks.vo.material;

import com.ose.vo.ValueObject;

/**
 * 材料前缀。
 */
public enum MaterialPrefixType implements ValueObject {

    MATERIAL_RELEASE_NOTE("MRN", "材料放行单"),
    MATERIAL_STOCKTAKE("MST", "材料盘点"),
    MATERIAL_OPEN_BOX_INSPECTION_REPORT("MOBIR", "材料开箱检验"),
    MATERIAL_RECEIVE_RECEIPT("MRR", "材料入库"),
    MATERIAL_PREPARE("MP", "材料准备"),
    MATERIAL_SURPLUS_RECEIPTS("MSR", "余料领料单"),
    MATERIAL_ISSUE_RECEIPT("MIR", "材料出库"),
    MATERIAL_TRANSFER("MT", "材料配送"),
    MATERIAL_RETURN("MR", "材料退库"),
    MATERIAL_JACKING("MJ", "材料套料"),
    MATERIAL_SURPLUS_JACKING("MSJ", "余料套料"),
    MATERIAL_NESTING_STRUCTURE("MNST", "材料结构套料"),
    PART_OUT("PART_OUT", "外协导入"),
    MATERIAL_NESTING_STRUCTURE_CUTTIND("MNSTC", "材料结构套料下料");

    private String code;
    String displayName;

    MaterialPrefixType(String code, String displayName) {
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
