package com.ose.tasks.domain.model.repository.bpm;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.bpm.ActivitiModelCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmReDeployment;
import com.ose.tasks.vo.SuspensionState;

/**
 * 用户查询。
 */
public class BpmReDeploymentRepositoryImpl extends BaseRepository implements BpmReDeploymentRepositoryCustom {

    @Override
    public Page<BpmReDeployment> list(Long orgId, Long projectId, ActivitiModelCriteriaDTO criteriaDTO,
                                      PageDTO page) {

        SQLQueryBuilder<BpmReDeployment> builder = getSQLQueryBuilder(BpmReDeployment.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("processId", criteriaDTO.getSearch());


        if (criteriaDTO.getSuspensionState() != null) {
            SuspensionState state = SuspensionState.valueOf(criteriaDTO.getSuspensionState());
            List<SuspensionState> list = new ArrayList<SuspensionState>();
            list.add(state);
            builder.in("suspensionState", list);
        }

        return builder.paginate(page.toPageable())
            .exec()
            .page();

    }

    @Override
    public BpmReDeployment findLastVerisonByProcessId(Long orgId, Long projectId, Long processId) {

        SQLQueryBuilder<BpmReDeployment> builder = getSQLQueryBuilder(BpmReDeployment.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("processId", processId)
            .is("suspensionState", SuspensionState.ACTIVE);

        builder.sort(new Sort.Order(Sort.Direction.DESC, "deployTime"));

        List<BpmReDeployment> list = builder.limit(Integer.MAX_VALUE).exec().page().getContent();

        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }


}
