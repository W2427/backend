package com.ose.tasks.dto.wbs;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

public class SpoolSplitDTO extends SpoolEntryInsertDTO {

    @Schema(description = "需要移动的实体列表")
    private List<SpoolSplitEntityDTO> entities = new ArrayList<>();

    @Schema(description = "项目版本")
    private long version;

    @Schema(description = "新实施位置类型")
    private String newShopField;

    @Schema(description = "新单管实体id")
    private Long newSpoolId;

    @Schema(description = "保留单管实体id")
    private Long saveSpoolId;

    @Schema(description = "单管id")
    private Long spoolId;

    public List<SpoolSplitEntityDTO> getEntities() {
        return entities;
    }

    public void setEntities(List<SpoolSplitEntityDTO> entities) {
        this.entities = entities;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getNewShopField() {
        return newShopField;
    }

    public void setNewShopField(String newShopField) {
        this.newShopField = newShopField;
    }

    public Long getNewSpoolId() {
        return newSpoolId;
    }

    public void setNewSpoolId(Long newSpoolId) {
        this.newSpoolId = newSpoolId;
    }

    public Long getSaveSpoolId() {
        return saveSpoolId;
    }

    public void setSaveSpoolId(Long saveSpoolId) {
        this.saveSpoolId = saveSpoolId;
    }

    public Long getSpoolId() {
        return spoolId;
    }

    public void setSpoolId(Long spoolId) {
        this.spoolId = spoolId;
    }
}
