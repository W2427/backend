package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.material.FMaterialPrepareItemEntity;
import com.ose.tasks.entity.material.FMaterialPrepareNodeEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 材料准备单节点，详情查询，更新DTO
 */
public class FMaterialPreparePostDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "节点名称")
    private FMaterialPrepareNodeEntity fMaterialPrepareNodeEntity;

    @Schema(description = "节点详情")
    private List<FMaterialPrepareItemEntity> fMaterialPrepareItemEntityList;

    public FMaterialPrepareNodeEntity getfMaterialPrepareNodeEntity() {
        return fMaterialPrepareNodeEntity;
    }

    public void setfMaterialPrepareNodeEntity(FMaterialPrepareNodeEntity fMaterialPrepareNodeEntity) {
        this.fMaterialPrepareNodeEntity = fMaterialPrepareNodeEntity;
    }

    public List<FMaterialPrepareItemEntity> getfMaterialPrepareItemEntityList() {
        return fMaterialPrepareItemEntityList;
    }

    public void setfMaterialPrepareItemEntityList(List<FMaterialPrepareItemEntity> fMaterialPrepareItemEntityList) {
        this.fMaterialPrepareItemEntityList = fMaterialPrepareItemEntityList;
    }

}
