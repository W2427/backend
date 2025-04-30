package com.ose.tasks.domain.model.repository.trident;

import com.ose.tasks.dto.bpm.HierarchyBaseDTO;
import com.ose.tasks.dto.trident.ProjectNodeDetailCriteriaDTO;
import com.ose.tasks.entity.trident.ProjectNodeDetail;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProjectNodeDetailRepositoryCustom {

    /**
     * 获取列表。
     *
     * @param projectId         项目ID
     * @param criteriaDTO 查询条件
     * @return 列表
     */
    Page<ProjectNodeDetail> search(Long projectId, ProjectNodeDetailCriteriaDTO criteriaDTO);

    List<HierarchyBaseDTO> findDistinctFGroups(Long projectId,
                                               ProjectNodeDetailCriteriaDTO subSystemIds);



    List<HierarchyBaseDTO> findDistinctSubSystems(Long projectId,
                                       ProjectNodeDetailCriteriaDTO criteriaDTO);

    List<HierarchyBaseDTO> findDistinctEntityTypes(Long projectId,
                                        ProjectNodeDetailCriteriaDTO criteriaDTO);

    List<HierarchyBaseDTO> searchInCharge(Long projectId, ProjectNodeDetailCriteriaDTO criteriaDTO);
}
