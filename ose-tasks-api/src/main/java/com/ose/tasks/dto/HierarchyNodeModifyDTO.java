package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 项目条目信息更新数据传输对象。
 */
public class HierarchyNodeModifyDTO extends BaseDTO {

    private static final long serialVersionUID = 1059686330952786768L;

    @Schema(description = "上级条目 ID")
    private Long parentId;

    @Schema(description = "实体类型")
    private String entityType;

    @Schema(description = "节点编号")
    private String no;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "前一个条目的 ID（排在首位时设置为<code>first</code>）")
    private Long previousEntryId;

    // 以下三个字段只有在模块的修改或添加时后台才会处理
    @Schema(description = "模块类型")
    private String moduleType;

    @Schema(description = "批次编号")
    private String batchNo;

    @Schema(description = "是否打包")
    private boolean pack = false;

    @Schema(description = "层级维度，PIPE STRUCTURE等")
    private String hierarchyType;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getPreviousEntryId() {
        return previousEntryId;
    }

    public void setPreviousEntryId(Long previousEntryId) {
        this.previousEntryId = previousEntryId;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public boolean isPack() {
        return pack;
    }

    public void setPack(boolean pack) {
        this.pack = pack;
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }
}
