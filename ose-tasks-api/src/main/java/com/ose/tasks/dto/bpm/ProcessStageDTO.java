package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

/**
 * 工序阶段 数据传输对象
 */
public class ProcessStageDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    // 工序名称
    private String nameCn;

    // 工序名称-英文
    private String nameEn;

    // 排序
    private short orderNo = 0;

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

    public short getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(short orderNo) {
        this.orderNo = orderNo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}
