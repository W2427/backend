package com.ose.tasks.dto.setting;

import com.ose.dto.PageDTO;

public class FuncPartCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -8874991008022441602L;
    // 工序名称
    private String nameCn;

    // 工序名称-英文
    private String nameEn;

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
}
