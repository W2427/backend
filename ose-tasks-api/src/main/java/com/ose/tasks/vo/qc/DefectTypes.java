package com.ose.tasks.vo.qc;

import com.ose.vo.ValueObject;

/**
 * 挂起状态。
 */
public enum DefectTypes implements ValueObject {

    CRACK("C", "裂纹"),
    LACK_OF_FUSION("L.F", "未熔合"),
    LACK_OF_PENETRATION("L.P", "未熔透"),
    SLAG_INCLUSION("S", "夹渣"),
    POROSITY("P", "气孔"),
    CLUSTERED("C.P", "密集气孔"),
    UNDERCUT("U", "咬边"),
    DEEP_POROSITY("D.P", "深孔");

    private String displayName;

    private String description;

    DefectTypes(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
