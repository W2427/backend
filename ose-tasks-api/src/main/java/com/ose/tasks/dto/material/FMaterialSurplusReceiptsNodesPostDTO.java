package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 添加材料准备单list DTO
 */
public class FMaterialSurplusReceiptsNodesPostDTO extends BaseDTO {

    private static final long serialVersionUID = -8963310500277937327L;

    @Schema(description = "节点信息列表")
    private List<FMaterialSurplusReceiptsNodesItemPostDTO> nodes;

    public List<FMaterialSurplusReceiptsNodesItemPostDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<FMaterialSurplusReceiptsNodesItemPostDTO> nodes) {
        this.nodes = nodes;
    }
}
