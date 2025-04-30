package com.ose.tasks.domain.model.repository;

import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.DocumentUploadHistorySearchDTO;
import com.ose.tasks.dto.bpm.ExInspReportCriteriaDTO;
import com.ose.tasks.dto.bpm.ExInspViewCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingCoordinateCriteriaDTO;
import com.ose.tasks.entity.DocumentHistory;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.vo.qc.InspectType;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.tasks.vo.qc.ReportSubType;
import com.ose.tasks.vo.qc.ReportType;
import com.ose.util.BeanUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class DrawingCoordinateRepositoryImpl extends BaseRepository implements DrawingCoordinateRepositoryCustom {
    @Override
    public Page<DrawingCoordinate> search(Long orgId, Long projectId, DrawingCoordinateCriteriaDTO criteriaDTO) {
        SQLQueryBuilder<DrawingCoordinate> builder = getSQLQueryBuilder(DrawingCoordinate.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("drawingCoordinateType", criteriaDTO.getDrawingCoordinateType())
            .is("status", EntityStatus.ACTIVE);

        if (!StringUtils.isEmpty(criteriaDTO.getName())) {
            builder.like("name", criteriaDTO.getName());
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
