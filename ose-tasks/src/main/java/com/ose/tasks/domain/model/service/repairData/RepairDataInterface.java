package com.ose.tasks.domain.model.service.repairData;
import com.ose.dto.ContextDTO;
import com.ose.tasks.dto.rapairData.*;

import java.io.IOException;

public interface RepairDataInterface {

    void repairPath(
        Long orgId,
        Long projectId,
        UploadFileDTO uploadFileDTO
    ) throws IOException;


    void batchRevocationNode(
        Long orgId,
        Long projectId,
        RepairBatchRevocationNodesDTO repairBatchRevocationNodesDTO,
        ContextDTO contextDTO
    );

    void batchRepairDrawingPlanHour();
}
