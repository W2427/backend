package com.ose.tasks.domain.model.repository.material;

import com.ose.repository.BaseRepository;
import com.ose.tasks.entity.material.FMaterialStructureNest;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FMaterialStructureNestRepositoryCustomImpl extends BaseRepository implements FMaterialStructureNestRepositoryCustom {

    /**
     * 查找结构套料方案列表。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param pageable  分页参数
     * @return
     */
    @Override
    public Page<FMaterialStructureNest> list(Long orgId, Long projectId, Pageable pageable) {
        SQLQueryBuilder<FMaterialStructureNest> builder = getSQLQueryBuilder(FMaterialStructureNest.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("status", EntityStatus.ACTIVE);

        return builder.paginate(pageable).exec().page();
    }
}
