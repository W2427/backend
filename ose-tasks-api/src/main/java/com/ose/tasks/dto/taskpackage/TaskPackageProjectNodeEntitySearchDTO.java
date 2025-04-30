package com.ose.tasks.dto.taskpackage;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 任务包中可添加的实体查询参数。
 */
public class TaskPackageProjectNodeEntitySearchDTO extends PageDTO {

    private static final long serialVersionUID = 6153572833207692444L;

    @Schema(description = "层级专业")
    private String hierarchyType;

    @Schema(description = "实体编号")
    private String entityNo;

    @Schema(description = "指定要查找的实体大类")
    private String entityType;

    @Schema(description = "查找父级层级ids")
    private List<Long> parentHierarchyIds;

    public List<Long> getParentHierarchyIds() {
        return parentHierarchyIds;
    }

    public void setParentHierarchyIds(List<Long> parentHierarchyIds) {
        this.parentHierarchyIds = parentHierarchyIds;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }
}
