package com.ose.auth.vo;

import com.ose.vo.ValueObject;

/**
 * 业务代码类型值对象。
 */
public enum BizCodeType {

    ACCOUNT_TYPE(AccountType.values()),
    ORGANIZATION_TYPE(OrgType.values()),
    USER_PRIVILEGE(UserPrivilege.values()),
    RESOURCE_TYPE(ResourceType.values()),
    VERIFICATION_PURPOSE(VerificationPurpose.values());

    private ValueObject[] valueObjects;

    BizCodeType(ValueObject[] valueObjects) {
        this.valueObjects = valueObjects;
    }

    public ValueObject[] getValueObjects() {
        return valueObjects;
    }

}
