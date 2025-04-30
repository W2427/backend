package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;

/**
 * 实体批量添加工序DTO
 */
public class BatchAddRelationDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4465967982539175650L;

    private List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

}
