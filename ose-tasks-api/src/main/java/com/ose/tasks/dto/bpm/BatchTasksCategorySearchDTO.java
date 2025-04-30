package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 数据传输对象
 */
public class BatchTasksCategorySearchDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "流程id集合")
    private List<Long> actInstIds;

    public List<Long> getActInstIds() {
        return actInstIds;
    }

    public void setActInstIds(List<Long> actInstIds) {
        this.actInstIds = actInstIds;
    }

}
