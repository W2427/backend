package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.drawing.DrawingProjectDeptIDCRelationSearchDTO;
import com.ose.tasks.entity.drawing.DrawingProjectDeptIDCRelation;
import org.springframework.data.domain.*;

import java.util.List;

public class DrawingProjectDeptIDCRelationRepositoryImpl extends BaseRepository implements DrawingProjectDeptIDCRelationRepositoryCustom {

    @Override
    public List<DrawingProjectDeptIDCRelation> search(
        Long orgId,
        Long projectId,
        DrawingProjectDeptIDCRelationSearchDTO dto
    ) {

        SQLQueryBuilder<DrawingProjectDeptIDCRelation> relationSQLQueryBuilder = getSQLQueryBuilder(DrawingProjectDeptIDCRelation.class)
            .is("projectId", projectId);

        relationSQLQueryBuilder.is("deleted", false);


        if (dto.getDrawingId() != null) {
            relationSQLQueryBuilder.is("drawingId", dto.getDrawingId());
        }
        if (dto.getDepartmentId() != null) {
            relationSQLQueryBuilder.is("departmentId", dto.getDepartmentId());
        }
        if (dto.getDiscipline() != null) {
            relationSQLQueryBuilder.is("discipline", dto.getDiscipline());
        }

        Pageable newPageable = null;


        return relationSQLQueryBuilder
            .paginate(newPageable)
            .exec()
            .page().getContent();
    }
}
