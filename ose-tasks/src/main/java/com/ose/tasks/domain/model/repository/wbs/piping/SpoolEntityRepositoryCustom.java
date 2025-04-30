package com.ose.tasks.domain.model.repository.wbs.piping;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.wbs.SpoolEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.entity.SpoolEntity;
import org.springframework.data.domain.Page;

/**
 * 管件实体 CRUD 操作接口。
 */
public interface SpoolEntityRepositoryCustom {

    Page<SpoolEntity> search(
        Long orgId,
        Long projectId,
        SpoolEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO);
}
