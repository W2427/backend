package com.ose.material.vo;

import com.ose.vo.ValueObject;

/**
 * 材料导入类型。
 */
public enum MaterialImportType implements ValueObject {

    RFQ("设备物资单", "设备物资单"),
    BOM("物料单", "物料单"),
    SHIPPING_DETAIL("发货单详情", "发货单详情"),
    CONTRACT("合同", "合同"),
    RECEIVE_RELEASE("入库放行单", "入库放行单"),
    RECEIVE_RELEASE_RECEIVE("入库放行单入库", "入库放行单入库"),
    MATERIAL_RECEIVE_NOTE_DETAIL("入库单详情", "入库单详情"),
    HEAT_BATCH_NO("炉批号", "炉批号"),
    MATERIAL_ISSUE("出库单", "出库单"),
    MATERIAL_ISSUE_DETAIL("出库单明细", "出库单明细"),
    PURCHASE_PACKAGE_ITEM("采购包明细", "采购包明细"),
    ;

    private String displayName;

    private String description;

    MaterialImportType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

}
