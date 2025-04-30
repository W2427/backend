package com.ose.tasks.domain.model.service.moduleProcess;

public interface ModuleProcessStaticsInterface {

    void createStructure(
        Long orgId,
        Long projectId
    );

    void createPiping(
        Long orgId,
        Long projectId,
        Long sOrgId,
        Long sProjectId
    );

    void createElectrical(
        Long orgId,
        Long projectId
    );

}
