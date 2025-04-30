package com.ose.tasks.dto.wbs;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * WBS 工作组更新数据传输对象。
 */
public class WBSTeamPutDTO extends BaseDTO {

    private static final long serialVersionUID = 6409413527400611802L;

    @Schema(description = "工作组 ID")
    private Long teamId;

    @Schema(description = "工作场地 ID")
    private Long workSiteId;

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getWorkSiteId() {
        return workSiteId;
    }

    public void setWorkSiteId(Long workSiteId) {
        this.workSiteId = workSiteId;
    }

}
