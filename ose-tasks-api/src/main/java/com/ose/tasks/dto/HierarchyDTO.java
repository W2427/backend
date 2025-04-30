package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目层级结构数据传输对象。
 */
public class HierarchyDTO<T extends HierarchyNodePutDTO> extends BaseDTO {

    private static final long serialVersionUID = -7111370955161651120L;

    @Schema(description = "项目更新版本号")
    private long version;

    @Schema(description = "层级节点列表")
    private List<T> children = new ArrayList<>();

    @Schema(description = "层级维度 STRUCTURE")
    private String hierarchyType;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public void addChild(T child) {
        children.add(child);
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }
}
