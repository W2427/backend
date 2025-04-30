package com.ose.tasks.vo;

import com.ose.vo.ValueObject;


public enum BpmnGatewayType implements ValueObject {

    EXCLUSIVE("EXCLUSIVE"),
    INCLUSIVE("INCLUSIVE"),
    PARALLEL("PARALLEL"),
    EVENT("EVENT");

    private String displayName;

    BpmnGatewayType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
