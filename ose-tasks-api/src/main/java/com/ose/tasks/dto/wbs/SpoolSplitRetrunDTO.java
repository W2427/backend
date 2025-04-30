package com.ose.tasks.dto.wbs;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

public class SpoolSplitRetrunDTO extends BaseDTO {
    private static final long serialVersionUID = 5742456947331611205L;

    @Schema(description = "操作数量")
    private Integer count;

    @Schema(description = "失败个数")
    private Integer failCount;

    @Schema(description = "成功个数")
    private Integer successCount;

    @Schema(description = "需要移动的实体列表")
    private List<SpoolSplitEntityDTO> entities = new ArrayList<>();


    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<SpoolSplitEntityDTO> getEntities() {
        return entities;
    }

    public void setEntities(List<SpoolSplitEntityDTO> entities) {
        this.entities = entities;
    }
}
