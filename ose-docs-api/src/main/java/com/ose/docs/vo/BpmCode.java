package com.ose.docs.vo;

/**
 * BpmCode 常量。
 */
public enum BpmCode {

    COMAPANY_ID_L("COMPANY_ID", 100L);

    private String name;

    private Long value;

    BpmCode(String name, Long value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public Long getValue() {
        return this.value;
    }

}
