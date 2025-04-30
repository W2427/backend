package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.ViewMxjValidPohEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


/**
 * 请购单查询接口。
 */
@Transactional
public interface PohRepository extends PagingAndSortingRepository<ViewMxjValidPohEntity, String>, PohRepositoryCustom {

    @Query(value = "SELECT n FROM ViewMxjValidPohEntity n WHERE projId =:spmProjId AND id =:pohId "
    )
    ViewMxjValidPohEntity findByProjIdAndId(@Param("spmProjId") String spmProjId, @Param("pohId") String pohId);

}
