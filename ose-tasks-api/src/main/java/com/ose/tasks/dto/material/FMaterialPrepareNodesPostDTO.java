package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 添加材料准备单list DTO
 */
public class FMaterialPrepareNodesPostDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "节点名称列表")
    private List<String> nodeCodes;

    public List<String> getNodeCodes() {
        return nodeCodes;
    }

    public void setNodeCodes(List<String> nodeCodes) {
        this.nodeCodes = nodeCodes;
    }
}
