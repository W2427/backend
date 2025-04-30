package com.ose.tasks.dto.taskpackage;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

/**
 * 任务包分类实体 ID 集合数据传输对象。
 */
public class TaskPackageCategoryEntityDTO extends BaseDTO {

    private static final long serialVersionUID = -1883786290085758299L;

    @Schema(description = "实体 ID 集合")
    private Set<Long> entityIDs;

    public TaskPackageCategoryEntityDTO() {
    }

    public TaskPackageCategoryEntityDTO(Set<Long> entityIDs) {
        setEntityIDs(entityIDs);
    }

    public Set<Long> getEntityIDs() {
        return entityIDs;
    }

    public void setEntityIDs(Set<Long> entityIDs) {
        this.entityIDs = entityIDs;
    }
}
