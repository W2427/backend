package com.ose.auth.vo;

import com.ose.vo.ValueObject;

public enum OrgType implements ValueObject {

    COMPANY("公司"),
    DEPARTMENT("部门"),
    PROJECT("项目组织"),
    BU("事业部"),
    FUNCTION("职能部门"),

    IDC("IDC部门");

    private String displayName;

    OrgType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
