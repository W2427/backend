package com.ose.tasks.dto.wbs;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 启动对应工序四级计划及任务。
 */
public class WBSEntryPlainStartDTO extends BaseDTO {

    private static final long serialVersionUID = 2990001764626535898L;

    @Schema(description = "工序id数组")
    private List<Long> processIds ;

    private Long version;

    public List<Long> getProcessIds() {
        return processIds;
    }

    public void setProcessIds(List<Long> processIds) {
        this.processIds = processIds;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
