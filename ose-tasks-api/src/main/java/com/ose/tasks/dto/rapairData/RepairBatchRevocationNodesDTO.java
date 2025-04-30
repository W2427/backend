package com.ose.tasks.dto.rapairData;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 修改项目数据接口。
 */
public class RepairBatchRevocationNodesDTO extends BaseDTO {

    private static final long serialVersionUID = 7537633537478155941L;

    @Schema(description = "撤回多个任务节点")
    private List<RepairBatchRevocationNodeDTO> repairBatchRevocationNodeDTOs ;

    public List<RepairBatchRevocationNodeDTO> getRepairBatchRevocationNodeDTOs() {
        return repairBatchRevocationNodeDTOs;
    }

    public void setRepairBatchRevocationNodeDTOs(List<RepairBatchRevocationNodeDTO> repairBatchRevocationNodeDTOs) {
        this.repairBatchRevocationNodeDTOs = repairBatchRevocationNodeDTOs;
    }
}
