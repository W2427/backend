package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.ViewMxjReqs;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


/**
 * 请购单查询接口。
 */
@Transactional
public interface ReqListRepository extends PagingAndSortingRepository<ViewMxjReqs, String>, ReqListRepositoryCustom {

    @Query(value = "SELECT n FROM ViewMxjReqs n WHERE projectId =:spmProjId AND id =:reqId "
    )
    ViewMxjReqs findByProjectIdAndId(@Param("spmProjId") String spmProjId, @Param("reqId") String reqId);

}
