package com.ose.tasks.vo;

import com.ose.util.StringUtils;
import com.ose.vo.ValueObject;

/**
 * 项目条目类型。
 */
public enum ConstructionChangeType implements ValueObject {

    DWG_MAT("图纸修改(涉及材料)"),
    DWG("图纸修改(不涉及材料)"),
    REWORK("建造返工");

    private String displayName;

    ConstructionChangeType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public static ConstructionChangeType getByDisplayName(String displayName) {

        if (StringUtils.isEmpty(displayName)) {
            return null;
        }

        for (ConstructionChangeType type : ConstructionChangeType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }

}
