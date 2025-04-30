package com.ose.tasks.dto.wbs;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * WBS 条目打包创建数据传输对象。
 */
public class WBSBundleCreateDTO extends WBSBundleUpdateDTO {

    private static final long serialVersionUID = 7225483401310834355L;

    @Schema(description = "实体类型")
    @NotNull
    private String entityType;

    @Schema(description = "实体子类型")
    @NotBlank
    @Size(max = 32)
    private String entitySubType;

    @Schema(description = "工序阶段")
    @NotBlank
    @Size(max = 64)
    private String stage;

    @Schema(description = "工序")
    @NotBlank
    @Size(max = 64)
    private String process;

    @Schema(description = "四级计划 ID 列表（实体类型及工序必须与计划包的实体类型及工序同）")
    private List<Long> wbsEntryIDs = new ArrayList<>();

    @Override
    public String getEntityType() {
        return entityType;
    }

    @Override
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    @Override
    public String getEntitySubType() {
        return entitySubType;
    }

    @Override
    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    @Override
    public String getStage() {
        return stage;
    }

    @Override
    public void setStage(String stage) {
        this.stage = stage;
    }

    @Override
    public String getProcess() {
        return process;
    }

    @Override
    public void setProcess(String process) {
        this.process = process;
    }

    public List<Long> getWbsEntryIDs() {
        return wbsEntryIDs;
    }

    public void setWbsEntryIDs(List<Long> wbsEntryIDs) {
        this.wbsEntryIDs = wbsEntryIDs;
    }
}
