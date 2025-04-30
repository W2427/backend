package com.ose.tasks.dto.worksite;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 工作场地更新数据传输对象。
 */
public abstract class WorkSiteSortDTO extends BaseDTO {

    private static final long serialVersionUID = 3487939038298824132L;

    @Schema(description = "下一个场地 ID（用于排序，默认排在最后）")
    private Long nextWorkSiteId = null;

    public Long getNextWorkSiteId() {
        return nextWorkSiteId;
    }

    public void setNextWorkSiteId(Long nextWorkSiteId) {
        this.nextWorkSiteId = nextWorkSiteId;
    }

}
