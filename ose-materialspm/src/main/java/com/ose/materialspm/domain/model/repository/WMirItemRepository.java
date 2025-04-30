package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.WMirItemEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


/**
 * 临时出库单详情
 */
public interface WMirItemRepository extends PagingAndSortingWithCrudRepository<WMirItemEntity, String> {

    @Query(
        value = "SELECT n FROM WMirItemEntity n WHERE projId = :spmProjId AND mirNumber = :mirNumber"
    )
    List<WMirItemEntity> findByProjIdAndMirNumber(@Param("spmProjId") String projId, @Param("mirNumber") String mirNumber);

    Optional<WMirItemEntity> findByProjIdAndIdent(String projId, int ident);

    void deleteByProjIdAndMirNumber(String projId, String mirNumber);
}
