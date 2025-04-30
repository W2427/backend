package com.ose.tasks.dto.wbs;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * WBS 条目打包实体添加/删除数据传输对象。
 */
public class WBSBundleEntriesDTO extends BaseDTO {

    private static final long serialVersionUID = 7469337240162774475L;

    @Schema(description = "四级计划 ID 列表")
    @NotNull
    @Size(min = 1)
    private List<Long> wbsEntryIDs = new ArrayList<>();

    public List<Long> getWbsEntryIDs() {
        return wbsEntryIDs;
    }

    public void setWbsEntryIDs(List<Long> wbsEntryIDs) {
        this.wbsEntryIDs = wbsEntryIDs;
    }

}
