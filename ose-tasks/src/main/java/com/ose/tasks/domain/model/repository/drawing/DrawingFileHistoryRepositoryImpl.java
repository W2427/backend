package com.ose.tasks.domain.model.repository.drawing;

import com.ose.auth.vo.OrgType;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.*;
import com.ose.tasks.dto.drawing.DrawingFileHistorySearchDTO;
import com.ose.tasks.entity.drawing.DrawingFileHistory;
import com.ose.tasks.entity.drawing.DrawingRecord;
import com.ose.util.BeanUtils;
import com.ose.vo.DrawingFileType;
import com.ose.vo.DrawingRecordStatus;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DrawingFileHistoryRepositoryImpl extends BaseRepository implements DrawingFileHistoryRepositoryCustom {

    @Override
    public Page<DrawingFileHistory> search(
        Long orgId,
        Long projectId,
        Long drawingDetailId,
        DrawingFileHistorySearchDTO criteriaDTO
    ) {
        return getSQLQueryBuilder(DrawingFileHistory.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("drawingDetailId", drawingDetailId)
            .is("drawingFileId", criteriaDTO.getDrawingFileId())
            .is("procInstId", criteriaDTO.getProcInstId())
            .is("drawingFileType", criteriaDTO.getDrawingFileType() != null
                ? DrawingFileType.valueOf(criteriaDTO.getDrawingFileType())
                : null)
            .is("deleted", false)
            .paginate(criteriaDTO.toPageable())
            .exec()
            .page();
    }
}
