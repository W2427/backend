package com.ose.tasks.domain.model.repository.drawing;

import com.ose.tasks.dto.drawing.DrawingFileHistorySearchDTO;
import com.ose.tasks.entity.drawing.DrawingFileHistory;
import org.springframework.data.domain.Page;

import java.util.List;
public interface DrawingFileHistoryRepositoryCustom {

    Page<DrawingFileHistory> search(
        Long orgId,
        Long projectId,
        Long drawingDetailId,
        DrawingFileHistorySearchDTO criteriaDTO
    );
}
