package com.ose.tasks.domain.model.repository;

import com.ose.tasks.dto.drawing.DrawingCommentCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingCommentDTO;
import com.ose.tasks.dto.drawing.DrawingCoordinateCriteriaDTO;
import com.ose.tasks.entity.drawing.DrawingComment;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import org.springframework.data.domain.Page;


public interface DrawingCommentRepositoryCustom {
    Page<DrawingComment> search(Long orgId, Long projectId, DrawingCommentCriteriaDTO criteriaDTO);

}
