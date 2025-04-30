package com.ose.materialspm.domain.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ose.materialspm.entity.MvMxjListNodesEntity;


/**
 * BOM CRUD 操作接口。
 */
@Transactional
public interface MvMxjListNodesRepository extends PagingAndSortingRepository<MvMxjListNodesEntity, String> {

    @Query(value = "SELECT n FROM MvMxjListNodesEntity n WHERE projId =:projId AND nvl(parentLnId, 0) = :parentLnId")
    List<MvMxjListNodesEntity> findByProjIdAndParentLnId(@Param("projId") String spmProjId, @Param("parentLnId") String parentLnId);

    @Query(value = "SELECT n FROM MvMxjListNodesEntity n WHERE projId =:projId")
    List<MvMxjListNodesEntity> findByProjId(@Param("projId") String spmProjId);
}
