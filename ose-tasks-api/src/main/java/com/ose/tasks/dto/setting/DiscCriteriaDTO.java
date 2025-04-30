package com.ose.tasks.dto.setting;

import com.ose.dto.PageDTO;

public class DiscCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = 5196996351139892619L;
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
