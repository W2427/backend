package com.ose.tasks.domain.model.repository.wps;

import com.ose.tasks.dto.wps.WeldWelderRelationSearchDTO;
import com.ose.tasks.entity.wps.WeldWelderRelation;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 项目 CRUD 操作接口。
 */
@Transactional
public interface WeldWelderRelationRepositoryCustom {

    /**
     * 查询批处理任务。
     *
     * @param orgId                       组织 ID
     * @param projectId                   项目 ID
     * @param weldWelderRelationSearchDTO 查询条件
     * @return 批处理任务查询结果分页数据
     */
    Page<WeldWelderRelation> search(
        final Long orgId,
        final Long projectId,
        final WeldWelderRelationSearchDTO weldWelderRelationSearchDTO
    );

}
