package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DetailDesignDrawingDetail;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface DetailDesignDrawingDetailRepository extends PagingAndSortingWithCrudRepository<DetailDesignDrawingDetail, Long> {

    /**
     * 通过当前使用版本查找子图纸列表。
     *
     * @param id
     * @param activeRevision
     * @return
     */
    Optional<DetailDesignDrawingDetail> findByDetailDesignListIdAndActiveRevision(Long id, String activeRevision);

    /**
     * 查询生产设计图纸下的的子图纸明细。
     *
     * @param id
     * @return
     */
    List<DetailDesignDrawingDetail> findByDetailDesignListId(Long id);

}
