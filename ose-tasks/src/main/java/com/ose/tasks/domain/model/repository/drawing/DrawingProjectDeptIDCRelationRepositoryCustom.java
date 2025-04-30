package com.ose.tasks.domain.model.repository.drawing;

import com.ose.tasks.dto.drawing.DrawingProjectDeptIDCRelationSearchDTO;
import com.ose.tasks.entity.drawing.DrawingProjectDeptIDCRelation;

import java.util.List;

/**
 * @author: ChenQiang
 * @date: 2024/11/6
 */
public interface DrawingProjectDeptIDCRelationRepositoryCustom {

    List<DrawingProjectDeptIDCRelation> search(
        Long orgId,
        Long projectId,
        DrawingProjectDeptIDCRelationSearchDTO dto
    );
}
