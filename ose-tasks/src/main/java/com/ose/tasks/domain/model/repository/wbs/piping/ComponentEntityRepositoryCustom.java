package com.ose.tasks.domain.model.repository.wbs.piping;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.ComponentEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.entity.ComponentEntity;
import org.springframework.data.domain.Page;

/**
 * 管件实体 CRUD 操作接口。
 */
public interface ComponentEntityRepositoryCustom {

    Page<ComponentEntity> search(
        Long orgId,
        Long projectId,
        ComponentEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO);
}
