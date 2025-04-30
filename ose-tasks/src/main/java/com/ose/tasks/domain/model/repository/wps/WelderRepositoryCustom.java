package com.ose.tasks.domain.model.repository.wps;

import com.ose.tasks.dto.wps.WelderCriteriaDTO;
import com.ose.tasks.entity.wps.Welder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WelderRepositoryCustom {

    /**
     * 获取焊工列表。
     *
     * @param orgId             组织ID
     * @param projectId         项目ID
     * @param welderCriteriaDTO 查询条件
     * @param pageable          分页参数
     * @return 焊工列表
     */
    Page<Welder> search(Long orgId, Long projectId, WelderCriteriaDTO welderCriteriaDTO, Pageable pageable);

    /**
     * 获取焊工列表。
     *
     * @param orgId             组织ID
     * @param projectId         项目ID
     * @param welderCriteriaDTO 查询条件
     * @return 焊工列表
     */
    Page<Welder> search(Long orgId, Long projectId, WelderCriteriaDTO welderCriteriaDTO);

    /**
     * 获取焊工全部列表。
     *
     * @param orgId             组织ID
     * @param projectId         项目ID
     * @param welderCriteriaDTO 查询条件
     * @return 焊工全部列表
     */
    List<Welder> getAll(Long orgId, Long projectId, WelderCriteriaDTO welderCriteriaDTO);
}
