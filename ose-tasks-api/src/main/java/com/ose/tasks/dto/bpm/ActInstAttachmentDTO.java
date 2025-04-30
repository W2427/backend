package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;

/**
 * 待办任务条件 数据传输对象
 */
public class ActInstAttachmentDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = -34948993806220141L;

    private List<String> temporaryNames;

    public List<String> getTemporaryNames() {
        return temporaryNames;
    }

    public void setTemporaryNames(List<String> temporaryNames) {
        this.temporaryNames = temporaryNames;
    }


}
