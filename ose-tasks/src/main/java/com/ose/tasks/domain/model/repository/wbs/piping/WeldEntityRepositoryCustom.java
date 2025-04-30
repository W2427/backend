package com.ose.tasks.domain.model.repository.wbs.piping;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.wbs.WeldEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.entity.WeldEntity;
import org.springframework.data.domain.Page;

/**
 * 管件实体 CRUD 操作接口。
 */
public interface WeldEntityRepositoryCustom {

    Page<WeldEntity> search(
        Long orgId,
        Long projectId,
        WeldEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO);
}
