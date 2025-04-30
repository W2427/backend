package com.ose.tasks.dto.wbs;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class SpoolMergeDTO extends BaseDTO {

    private static final long serialVersionUID = 1127404490434502847L;

    @Schema(description = "旧实施位置类型")
    private String oldShopField;

    @Schema(description = "新实施位置类型")
    private String newShopField;

    @Schema(description = "选择的实体ID数组")
    private List<Long> selectEntityIds;

    @Schema(description = "删除的实体ID数组")
    private List<Long> deleteEntityIds;

    @Schema(description = "项目版本")
    private long version;

    public List<Long> getSelectEntityIds() {
        return selectEntityIds;
    }

    public void setSelectEntityIds(List<Long> selectEntityIds) {
        this.selectEntityIds = selectEntityIds;
    }

    public List<Long> getDeleteEntityIds() {
        return deleteEntityIds;
    }

    public void setDeleteEntityIds(List<Long> deleteEntityIds) {
        this.deleteEntityIds = deleteEntityIds;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getOldShopField() {
        return oldShopField;
    }

    public void setOldShopField(String oldShopField) {
        this.oldShopField = oldShopField;
    }

    public String getNewShopField() {
        return newShopField;
    }

    public void setNewShopField(String newShopField) {
        this.newShopField = newShopField;
    }
}
