package com.ose.report.dto;

import com.ose.dto.BaseDTO;

/**
 * 检查项 数据传输对象（基础项目）
 */
public class BaseChecklistItemDTO extends BaseDTO {

    private static final long serialVersionUID = 5913274819087646745L;

    // 序号
    private String itemNo;

    // 描述
    private String description;

    /**
     * 构造方法。
     */
    public BaseChecklistItemDTO() {
    }

    /**
     * 构造方法。
     */
    public BaseChecklistItemDTO(String itemNo, String description) {
        this.itemNo = itemNo;
        this.description = description;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ChecklistItemDTO{itemNo='"
            + itemNo
            + "', description='"
            + description
            + "}";
    }
}
