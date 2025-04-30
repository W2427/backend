package com.ose.material.vo;

import com.ose.vo.ValueObject;

/**
 * 材料组织类型
 */
public enum MaterialOrganizationType implements ValueObject {
    COMPANY("公司", "公司"),
    PROJECT("项目", "项目");

    private String displayName;

    private String description;

    MaterialOrganizationType(String displayName, String description) {
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
