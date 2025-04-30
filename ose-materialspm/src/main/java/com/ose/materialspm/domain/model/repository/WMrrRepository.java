package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.WMrrEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * 请购单查询接口。
 */
public interface WMrrRepository extends PagingAndSortingWithCrudRepository<WMrrEntity, String>, WMrrRepositoryCustom {

    @Query(
        value = "SELECT n FROM WMrrEntity n WHERE projId = :spmProjId AND mrrNumber = :mrrNumber"
    )
    List<WMrrEntity> findByProjIdAndMrrNumber(@Param("spmProjId") String projId, @Param("mrrNumber") String mrrNumber);

    void deleteByProjIdAndMrrNumber(String projId, String mrrNumber);

}
