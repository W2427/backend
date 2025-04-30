package com.ose.tasks.domain.model.repository.wbs;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import org.springframework.data.domain.Page;

/**
 * 管线实体 CRUD 操作接口。
 */
public interface WBSEntityCustomRepository<T extends WBSEntityBase, S extends WBSEntryCriteriaBaseDTO> {

    /**
     * 查询实体。
     *
     * @param criteriaDTO 查询条件
     * @param pageDTO     分页参数
     * @return 用户查询结果分页数据
     */
    Page<T> search(
        Long orgId,
        Long projectId,
        S criteriaDTO,
        PageDTO pageDTO,
        Class type
    );

}
