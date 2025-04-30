package com.ose.tasks.domain.model.repository.repairdata;

import com.ose.tasks.dto.rapairData.RepairWbsListDTO;
import com.ose.tasks.dto.rapairData.RepairWbsSearchDTO;
import org.springframework.data.domain.Page;

public interface RepairDataRepositoryCustom {
    Page<RepairWbsListDTO> searchUndoWbs(
        Long orgId,
        Long projectId,
        RepairWbsSearchDTO repairWbsSearchDTO
    );

    Page<RepairWbsListDTO> searchPreWbs(
        Long orgId,
        Long projectId,
        RepairWbsSearchDTO repairWbsSearchDTO
    );
}
