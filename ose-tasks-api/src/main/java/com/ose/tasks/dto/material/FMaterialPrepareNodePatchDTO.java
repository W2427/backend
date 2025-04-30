package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.material.FMaterialPrepareItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 材料准备单节点，详情查询，更新DTO
 */
public class FMaterialPrepareNodePatchDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;


    @Schema(description = "节点详情")
    private List<FMaterialPrepareItemEntity> fMaterialPrepareItemEntityList;

    public List<FMaterialPrepareItemEntity> getfMaterialPrepareItemEntityList() {
        return fMaterialPrepareItemEntityList;
    }

    public void setfMaterialPrepareItemEntityList(List<FMaterialPrepareItemEntity> fMaterialPrepareItemEntityList) {
        this.fMaterialPrepareItemEntityList = fMaterialPrepareItemEntityList;
    }

}
