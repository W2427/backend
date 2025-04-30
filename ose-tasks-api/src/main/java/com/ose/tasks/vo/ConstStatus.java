package com.ose.tasks.vo;

import com.ose.util.StringUtils;
import com.ose.vo.ValueObject;

/**
 * 项目条目类型。
 */
public enum ConstStatus implements ValueObject {

    INIT(0, "未开始"),
    START(10, "已开始"),
    PULLED(20, "已敷设"),
    INSTALLED(21, "已安装"),
    T1(22, "T1接线"),
    T2(24, "T2接线"),
    SUBMIT(20, "已提交"),
    COMMCHECK(25, "已提交"),
    SIGNED(30, "已签字"),
    CLOSED(40, "已关闭 完成");

    private String displayName;

    private Integer index;

    ConstStatus(Integer index, String displayName) {
        this.index = index;
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public Integer getIndexName() {
        return index;
    }


    public static ConstStatus getByDisplayName(String displayName) {

        if (StringUtils.isEmpty(displayName)) {
            return null;
        }

        for (ConstStatus type : ConstStatus.values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }

}
