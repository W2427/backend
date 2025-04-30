package com.ose.material.dto;
import com.ose.material.entity.MmReleaseReceiveDetailEntity;
import com.ose.material.entity.MmReleaseReceiveDetailQrCodeEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 入库单详情查询列表
 */
public class MmReleaseReceiveDetailSearchDetailDTO extends MmReleaseReceiveDetailEntity {

    private static final long serialVersionUID = -3115709573736204494L;

    @Schema(description = "请购与发货单详情关系")
    public List<MmReleaseReceiveDetailQrCodeEntity> mmReleaseReceiveDetailQrCodeEntities;

    public List<MmReleaseReceiveDetailQrCodeEntity> getMmReleaseReceiveDetailQrCodeEntities() {
        return mmReleaseReceiveDetailQrCodeEntities;
    }

    public void setMmReleaseReceiveDetailQrCodeEntities(List<MmReleaseReceiveDetailQrCodeEntity> mmReleaseReceiveDetailQrCodeEntities) {
        this.mmReleaseReceiveDetailQrCodeEntities = mmReleaseReceiveDetailQrCodeEntities;
    }
}
