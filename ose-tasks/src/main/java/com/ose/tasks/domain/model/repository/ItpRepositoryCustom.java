package com.ose.tasks.domain.model.repository;

import com.ose.tasks.dto.ItpCriteriaDTO;
import com.ose.tasks.entity.Itp;
import org.springframework.data.domain.Page;

public interface ItpRepositoryCustom {

    /**
     * 获取ITP列表。
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param itpCriteriaDTO 查询条件
     * @return ITP列表
     */
    Page<Itp> search(Long orgId, Long projectId, ItpCriteriaDTO itpCriteriaDTO);
}
