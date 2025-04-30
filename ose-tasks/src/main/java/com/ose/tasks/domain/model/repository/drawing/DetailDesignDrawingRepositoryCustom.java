package com.ose.tasks.domain.model.repository.drawing;

import com.ose.tasks.dto.drawing.DetailDesignDrawingCriteriaDTO;
import com.ose.tasks.entity.drawing.DetailDesignDrawing;
import com.ose.tasks.entity.drawing.DetailDesignDrawingDetail;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface DetailDesignDrawingRepositoryCustom {

    /**
     * 详细设计图纸列表。
     *
     * @param orgId       组织id
     * @param projectId   项目id
     * @param pageable    分页参数
     * @param criteriaDTO 查询参数
     * @return
     */
    Page<DetailDesignDrawing> search(Long orgId, Long projectId, Pageable pageable,
                                     DetailDesignDrawingCriteriaDTO criteriaDTO);

    /**
     * 详细设计图纸详细列表。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param id        详细设计图纸id
     * @return
     */
    List<DetailDesignDrawingDetail> searchDetail(Long orgId, Long projectId, Long id);

}
