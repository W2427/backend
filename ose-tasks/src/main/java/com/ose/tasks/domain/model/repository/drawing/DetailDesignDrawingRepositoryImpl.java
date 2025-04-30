package com.ose.tasks.domain.model.repository.drawing;


import java.util.List;

import com.ose.tasks.entity.drawing.DetailDesignDrawing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.drawing.DetailDesignDrawingCriteriaDTO;
import com.ose.tasks.entity.drawing.DetailDesignDrawingDetail;

/**
 * 用户查询。
 */
public class DetailDesignDrawingRepositoryImpl extends BaseRepository implements DetailDesignDrawingRepositoryCustom {

    /**
     * 详细设计图纸列表。
     *
     * @param orgId
     * @param projectId
     * @param pageable
     * @param criteriaDTO
     * @return
     */
    @Override
    public Page<DetailDesignDrawing> search(Long orgId, Long projectId, Pageable pageable,
                                            DetailDesignDrawingCriteriaDTO criteriaDTO) {
        SQLQueryBuilder<DetailDesignDrawing> builder = getSQLQueryBuilder(DetailDesignDrawing.class)
            .is("orgId", orgId)
            .is("projectId", projectId);

        if (criteriaDTO.getKeyword() != null) {
            builder.like("documentNumber", criteriaDTO.getKeyword());
        }
        return builder.paginate(pageable)
            .exec()
            .page();

    }

    /**
     * 详细设计图纸详情。
     *
     * @param orgId
     * @param projectId
     * @param id
     * @return
     */
    @Override
    public List<DetailDesignDrawingDetail> searchDetail(Long orgId, Long projectId, Long id) {
        SQLQueryBuilder<DetailDesignDrawingDetail> builder = getSQLQueryBuilder(DetailDesignDrawingDetail.class)
            .is("detailDesignListId", id);

        return builder.limit(Integer.MAX_VALUE).exec().list();

    }

}
