package com.ose.tasks.domain.model.repository.wbs.piping;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.wbs.ISOEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.entity.ISOEntity;
import org.springframework.data.domain.Page;

/**
 * 管件实体 CRUD 操作接口。
 */
public interface ISOEntityRepositoryCustom {

    Page<ISOEntity> search(
        Long orgId,
        Long projectId,
        ISOEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO);
}
