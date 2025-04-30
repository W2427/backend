package com.ose.tasks.dto.wbs;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * WBS 条目打包查询条件数据传输对象。
 */
public class WBSBundleCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -3713270812073356087L;

    @Schema(description = "实体类型")
    private String entityType;

    @Schema(description = "实体子类型")
    private String entitySubType;

    @Schema(description = "工序阶段")
    private String stage;

    @Schema(description = "工序")
    private String process;

    @Schema(description = "工作场地 ID")
    private Long workSiteId;

    @Schema(description = "工作组 ID")
    private Long teamId;

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Long getWorkSiteId() {
        return workSiteId;
    }

    public void setWorkSiteId(Long workSiteId) {
        this.workSiteId = workSiteId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

}
