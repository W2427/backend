package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DetailDesignDrawing;
import com.ose.vo.EntityStatus;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface DetailDesignDrawingRepository extends PagingAndSortingWithCrudRepository<DetailDesignDrawing, Long>, DetailDesignDrawingRepositoryCustom {

    /**
     * 更新导入文件。
     *
     * @param fileId
     * @param list
     */
    @Modifying
    @Transactional
    @Query("update DetailDesignDrawing l set l.importFileId =:fileId where l in(:list)")
    void updateFileIdIn(@Param("fileId") Long fileId, @Param("list") List<DetailDesignDrawing> list);

    /**
     * 通过流程id找详细设计图纸。
     *
     * @param batchTaskId
     * @return
     */
    List<DetailDesignDrawing> findByBatchTaskId(Long batchTaskId);

    /**
     * 根据文档编号查找详细设计图纸。
     *
     * @param orgId
     * @param projectId
     * @param documentNumber
     * @param active
     * @return
     */
    Optional<DetailDesignDrawing> findByOrgIdAndProjectIdAndDocumentNumberAndStatus(Long orgId, Long projectId,
                                                                                    String documentNumber, EntityStatus status);

}
