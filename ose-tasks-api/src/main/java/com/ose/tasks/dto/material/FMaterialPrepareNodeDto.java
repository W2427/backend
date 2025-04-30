package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 材料准备单DTO
 */
public class FMaterialPrepareNodeDto extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "节点名称")
    private String nodeCode;

    @Schema(description = "节点的实体ID列表")
    private List<String> entityIdList;

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public List<String> getEntityIdList() {
        return entityIdList;
    }

    public void setEntityIdList(List<String> entityIdList) {
        this.entityIdList = entityIdList;
    }
}
