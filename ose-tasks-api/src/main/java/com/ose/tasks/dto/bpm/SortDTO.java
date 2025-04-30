package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

/**
 * entity排序数据传输对象
 */
public class SortDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    // 工序id or 工序分类id
    private Long id;

    // 排序序号
    private int orderNo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }


}
