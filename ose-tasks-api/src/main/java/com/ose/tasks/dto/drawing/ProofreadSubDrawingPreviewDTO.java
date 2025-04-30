package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;

/**
 * 图纸校审任务信息
 */
public class ProofreadSubDrawingPreviewDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    private String subDrawingBase;

    public String getSubDrawingBase() {
        return subDrawingBase;
    }

    public void setSubDrawingBase(String subDrawingBase) {
        this.subDrawingBase = subDrawingBase;
    }
}
