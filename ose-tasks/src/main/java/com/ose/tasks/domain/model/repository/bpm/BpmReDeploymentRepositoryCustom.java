package com.ose.tasks.domain.model.repository.bpm;

import org.springframework.data.domain.Page;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.ActivitiModelCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmReDeployment;

/**
 * 用户查询接口。
 */
public interface BpmReDeploymentRepositoryCustom {


    Page<BpmReDeployment> list(Long orgId, Long projectId, ActivitiModelCriteriaDTO criteriaDTO, PageDTO page);

    BpmReDeployment findLastVerisonByProcessId(Long orgId, Long projectId, Long processId);

}
