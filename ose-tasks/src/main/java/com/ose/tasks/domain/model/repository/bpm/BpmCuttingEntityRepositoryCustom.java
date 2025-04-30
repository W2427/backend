package com.ose.tasks.domain.model.repository.bpm;

import com.ose.tasks.dto.bpm.CuttingEntityCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmCutting;
import com.ose.tasks.entity.bpm.BpmCuttingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BpmCuttingEntityRepositoryCustom {

    /**
     * 查询下料实体
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param criteriaDTO
     * @param pageable
     * @return
     */
    Page<BpmCuttingEntity> getEntityList(Long orgId, Long projectId, CuttingEntityCriteriaDTO criteriaDTO, Pageable pageable);

    /**
     * 查询下料单列表
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param pageable
     * @param keyword
     * @param cuttingIds
     * @return
     */
    Page<BpmCutting> getCuttingList(Long orgId, Long projectId, Pageable pageable, String keyword, List<Long> cuttingIds);

    /**
     * 查询指定下料单对应全部实体
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param cuttingId
     * @param keyword
     * @return
     */
    List<BpmCuttingEntity> getCuttingEntities(Long orgId, Long projectId, Long cuttingId, String keyword);


}
