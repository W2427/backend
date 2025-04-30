package com.ose.tasks.domain.model.service.moduleProcess;

import com.ose.report.api.ModuleProcessStaticsAPI;
import com.ose.tasks.domain.model.repository.HierarchyRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class ModuleProcessStaticsService implements ModuleProcessStaticsInterface {

    private final ModuleProcessStaticsAPI moduleProcessStaticsAPI;
    private final HierarchyRepository herarchyRepository;
    private final BpmProcessRepository bpmProcessRepository;

    @Autowired
    public ModuleProcessStaticsService(
        ModuleProcessStaticsAPI moduleProcessStaticsAPI,
        HierarchyRepository herarchyRepository,
        BpmProcessRepository bpmProcessRepository
    ) {
        this.moduleProcessStaticsAPI = moduleProcessStaticsAPI;
        this.herarchyRepository = herarchyRepository;
        this.bpmProcessRepository = bpmProcessRepository;
    }

    @Override
    public void createStructure(
        Long orgId,
        Long projectId
    ) {


        System.out.println("创建模块批次数据完成 " + " - > " + new Date());
    }

    @Override
    public void createPiping(
        Long orgId,
        Long projectId,
        Long sOrgId,
        Long sProjectId
    ) {

        System.out.println("创建模块批次数据完成 " + " - > " + new Date());
    }

    @Override
    public void createElectrical(
        Long orgId,
        Long projectId
    ) {

        System.out.println("创建模块批次数据完成 " + " - > " + new Date());
    }
}
