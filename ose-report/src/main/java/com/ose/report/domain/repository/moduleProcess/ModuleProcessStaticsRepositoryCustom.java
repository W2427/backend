package com.ose.report.domain.repository.moduleProcess;

import com.ose.report.dto.moduleProcess.ModuleProcessModuleAssembledStaticItemDTO;

import java.util.Date;
import java.util.List;

public interface ModuleProcessStaticsRepositoryCustom {
    List<ModuleProcessModuleAssembledStaticItemDTO> findByOrgIdAndProjectIdAndShipmentNoModuleAssembled(
        Long orgId,
        Long projectId,
        String shipmentNo,
        List<String> moduleNos,
        Date archiveDate
    );

    List<ModuleProcessModuleAssembledStaticItemDTO> findByOrgIdAndProjectIdAndShipmentNoModuleAssembledAll(
        Long orgId,
        Long projectId,
        String shipmentNo,
        Date archiveDate
    );
}
