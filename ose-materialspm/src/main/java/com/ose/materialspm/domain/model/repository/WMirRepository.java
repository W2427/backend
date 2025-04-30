package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.WMirEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 临时出库单
 */
public interface WMirRepository extends PagingAndSortingWithCrudRepository<WMirEntity, String> {

    @Query(
        value = "SELECT n FROM WMirEntity n WHERE projId = :spmProjId AND mirNumber = :mirNumber"
    )
    List<WMirEntity> findByProjIdAndMirNumber(@Param("spmProjId") String projId, @Param("mirNumber") String mirNumber);

    void deleteByProjIdAndMirNumber(String projId, String mirNumber);

}
