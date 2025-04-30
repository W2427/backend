package com.ose.tasks.domain.model.repository.material;

import com.ose.tasks.entity.material.FMaterialStructureNest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FMaterialStructureNestRepositoryCustom {

    /**
     * 结构套料方案列表。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param pageable  分页参数
     * @return
     */
    Page<FMaterialStructureNest> list(Long orgId, Long projectId, Pageable pageable);

}
