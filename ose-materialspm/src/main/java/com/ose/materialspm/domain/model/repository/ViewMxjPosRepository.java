package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.ViewMxjPosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * BOM CRUD 操作接口。
 */
@Transactional
public interface ViewMxjPosRepository extends PagingAndSortingRepository<ViewMxjPosEntity, String> {

    @Query(value = "SELECT n FROM ViewMxjPosEntity n WHERE projId =:spmProjId AND bompath LIKE :bompath%"
    )
    Page<ViewMxjPosEntity> findByProjIdAndBompath(@Param("spmProjId") String spmProjId, @Param("bompath") String bompath, Pageable pageable);

    @Query(value = "SELECT n FROM ViewMxjPosEntity n WHERE projId =:spmProjId AND bompath LIKE :bompath%"
    )
    List<ViewMxjPosEntity> findByProjIdAndBompath(@Param("spmProjId") String spmProjId, @Param("bompath") String bompath);

    @Query(value = "SELECT n FROM ViewMxjPosEntity n WHERE projId =:spmProjId"
    )
    List<ViewMxjPosEntity> findByProjId(@Param("spmProjId") String spmProjId);
}
