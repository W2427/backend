package com.ose.tasks.domain.model.repository;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.drawing.DrawingCommentCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingCoordinateCriteriaDTO;
import com.ose.tasks.entity.drawing.DrawingComment;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;

/**
 *
 */
public class DrawingCommentRepositoryImpl extends BaseRepository implements DrawingCommentRepositoryCustom {
    @Override
    public Page<DrawingComment> search(Long orgId, Long projectId, DrawingCommentCriteriaDTO criteriaDTO) {
        SQLQueryBuilder<DrawingComment> builder = getSQLQueryBuilder(DrawingComment.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .isNot("status", EntityStatus.DELETED);

        if (criteriaDTO.getDrawingId() != null) {
            builder.is("drawing.id", criteriaDTO.getDrawingId());
        }

        return builder.paginate(criteriaDTO.toPageable()).exec().page();
    }


//    @Override
//    public Page<QCReport> externalInspectionReports(Long orgId, Long projectId, PageDTO pageDTO, ExInspReportCriteriaDTO criteriaDTO) {
//        SQLQueryBuilder<QCReport> builder = getSQLQueryBuilder(QCReport.class)
//            .is("orgId", orgId)
//            .is("projectId", projectId)
//            .is("status", EntityStatus.ACTIVE);
//
//
//        if (criteriaDTO.getOperator() != null) {
//            builder.is("operator", criteriaDTO.getOperator());
//        }
//
//        if (criteriaDTO.getKeyword() != null
//            && !"".equals(criteriaDTO.getKeyword())) {
//            builder.like("seriesNo", criteriaDTO.getKeyword());
//        }
//
//        if (criteriaDTO.getState() != null) {
//            if (criteriaDTO.getState().equals("UNDONE")) {
//                builder.isNot("reportStatus", ReportStatus.DONE);
//            } else if (criteriaDTO.getState().equals("DONE")) {
//                builder.is("reportStatus", ReportStatus.DONE);
//            }
//        }
//
//        builder.sort(new Sort.Order(Sort.Direction.DESC, "createdAt"));
//
//        return builder.paginate(pageDTO.toPageable())
//            .exec().page();
//    }


}
