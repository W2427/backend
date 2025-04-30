package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目 WBS 层级结构数据传输对象。
 */
public class WBSHierarchyDTO extends BaseDTO {

    private static final long serialVersionUID = -1308415573905571364L;

    @Schema(description = "项目更新版本号")
    private long version;

    @Schema(description = "层级节点列表")
    private List<WBSEntryDTO> children = new ArrayList<>();

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public List<WBSEntryDTO> getChildren() {
        return children;
    }

    public void setChildren(List<WBSEntryDTO> children) {
        this.children = children;
    }

    public void addChild(WBSEntryDTO child) {
        children.add(child);
    }

}
