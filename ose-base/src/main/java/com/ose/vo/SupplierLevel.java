package com.ose.vo;

/**
 * 数据实体状态值对象。
 */
public enum SupplierLevel implements ValueObject {

    PROJECT("项目级"),
    COMPANY("公司级");

    private String displayName;

    SupplierLevel(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
