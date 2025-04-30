package com.ose.tasks.domain.model.service.repairData;


import com.ose.dto.OperatorDTO;
import com.ose.report.dto.ExternalInspectionDTO;
import com.ose.tasks.dto.rapairData.RepairHeatDTO;

public interface PatchConstructionLogInterface {


    void patchConstructionLog(Long orgId, Long projectId, OperatorDTO operator);

    void patchPipingUnFinishedConstructionLog(Long orgId, Long projectId, OperatorDTO operator);

    void patchStructureUnFinishedConstructionLog(Long orgId, Long projectId, RepairHeatDTO repairHeatDTO, OperatorDTO operator);

    void repairHeatNo(Long orgId, Long projectId, OperatorDTO operator);

    void repairExternalInspection(Long orgId, Long projectId, ExternalInspectionDTO dto, OperatorDTO operator);

    void repairPipeExternalInspection(Long orgId, Long projectId, ExternalInspectionDTO dto, OperatorDTO operator);

    void repairFlawLength(Long orgId, Long projectId, OperatorDTO operator);

    void repairWeldRepairCount(Long orgId, Long projectId, OperatorDTO operator);
}
