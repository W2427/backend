package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 添加材料准备单list DTO
 */
public class FMaterialPrepareNodesPostForNewDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "节点信息列表")
    private List<FMaterialPrepareNodesPostForNewItemDTO> nodes;

    public List<FMaterialPrepareNodesPostForNewItemDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<FMaterialPrepareNodesPostForNewItemDTO> nodes) {
        this.nodes = nodes;
    }
}
