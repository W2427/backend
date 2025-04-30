package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.WMrrItemEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


/**
 * 请购单查询接口。
 */
public interface WMrrItemRepository extends PagingAndSortingWithCrudRepository<WMrrItemEntity, String>, WMrrItemRepositoryCustom {

    @Query(
        value = "SELECT n FROM WMrrItemEntity n WHERE projId = :spmProjId AND mrrNumber = :mrrNumber"
    )
    List<WMrrItemEntity> findByProjIdAndMrrNumber(@Param("spmProjId") String projId, @Param("mrrNumber") String mrrNumber);

    void deleteByProjIdAndMrrNumber(String projId, String mrrNumber);

    Optional<WMrrItemEntity> findByProjIdAndAndIdent(String projId, int ident);

}
