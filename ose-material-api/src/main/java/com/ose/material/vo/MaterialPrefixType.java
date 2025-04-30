package com.ose.material.vo;

import com.ose.vo.ValueObject;

/**
 * 材料前缀。
 */
public enum MaterialPrefixType implements ValueObject {


    MATERIAL_REQUISITION("MMR", "请购单"),
    MATERIAL_RFQ("RFQ", "BOM单"),
    MATERIAL_PO("MPO", "采购单"),
    MATERIAL_SHIPPING("MSH", "发货单"),
    MATERIAL_CONTRACT("MC", "合同"),
    MATERIAL_RECEIVE("MRR", "入库放行单"),
    MATERIAL_RELEASE_NOTE("MRN", "材料放行单"),
    MATERIAL_ISSUE("MIS", "材料盘点"),
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
    MATERIAL_NESTING_STRUCTURE_CUTTIND("MNSTC", "材料结构套料下料"),
    MATERIAL_WARE_HOUSE("MWH", "仓库"),
    MATERIAL_TAG_NUMBER("MTN", "材料编码");

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
