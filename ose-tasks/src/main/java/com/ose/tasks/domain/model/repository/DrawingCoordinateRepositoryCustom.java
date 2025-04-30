package com.ose.tasks.domain.model.repository;

import com.ose.tasks.dto.drawing.DrawingCoordinateCriteriaDTO;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import com.ose.tasks.vo.drawing.DrawingCoordinateType;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface DrawingCoordinateRepositoryCustom {
    Page<DrawingCoordinate> search(Long orgId, Long projectId, DrawingCoordinateCriteriaDTO criteriaDTO);

}
