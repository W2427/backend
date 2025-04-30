package com.ose.tasks.vo.wbs;


import com.ose.vo.ValueObject;

/**
 * 实体导入返回的状态
 */
public enum WBSImportLogStatus implements ValueObject {

    SKIP("SKIP"),
    DELETED("DELETED"),
    DONE("DONE"),
    ERROR("ERROR");

    private String code;

    WBSImportLogStatus(String code) {
        this.code = code;
    }
    /**
     * 取得代码的表示名。
     *
     * @return 代码表示名
     */
    public String getDisplayName() {
        return this.name();
    }


}
