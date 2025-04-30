package com.ose.tasks.domain.model.repository.drawing;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.drawing.DrawingCriteriaDTO;
import com.ose.tasks.entity.drawing.externalDrawing.DrawingAmendment;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


/**
 * 图纸 CRUD 操作接口。
 */
@Transactional
public interface DrawingAmendmentRepositoryCustom {
    Page<DrawingAmendment> getList(Long orgId, Long projectId, PageDTO page, DrawingCriteriaDTO criteriaDTO);

}
