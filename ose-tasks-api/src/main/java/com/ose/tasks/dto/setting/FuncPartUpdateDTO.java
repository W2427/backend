package com.ose.tasks.dto.setting;

import com.ose.dto.BaseDTO;

public class FuncPartUpdateDTO extends BaseDTO {


    private static final long serialVersionUID = -880916887737892104L;
    // 工序名称
    private String nameCn;

    // 工序名称-英文
    private String nameEn;

    // 排序
    private int orderNo = 0;

    // 备注
    private String memo;

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

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
