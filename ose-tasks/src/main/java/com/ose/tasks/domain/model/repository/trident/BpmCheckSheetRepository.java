package com.ose.tasks.domain.model.repository.trident;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.trident.CheckSheet;
import com.ose.tasks.vo.trident.CheckSheetTridentTbl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * BPM CHECK SHEET CRUD 操作接口。
 */
public interface BpmCheckSheetRepository extends PagingAndSortingWithCrudRepository<CheckSheet, Long> {

    List<CheckSheet> findByProjectId(Long projectId);


    Page<CheckSheet> findByProjectId(Long projectId, Pageable pageable);

    @Query("SELECT cs FROM CheckSheet cs WHERE cs.projectId = :projectId AND cs.checkSheetTridentTbl = :cst AND cs.isInTrident = :inTrident")
    Set<CheckSheet> findByProjectIdAndCheckSheetTridentTblAndIsInTrident(@Param("projectId") Long projectId,
                                                                         @Param("cst") CheckSheetTridentTbl cst,
                                                                         @Param("inTrident") boolean inTrident);

    @Modifying
    @Transactional
    @Query("UPDATE CheckSheet cs SET cs.checkSheetId = :checkSheetId, cs.isInTrident = TRUE " +
        "WHERE cs.projectId = :projectId AND cs.no = :code " +
        "AND cs.checkSheetTridentTbl = com.ose.tasks.vo.trident.CheckSheetTridentTbl.A_CHECK_SHEET")
    void updateCheckSheetTidForSheetA(@Param("projectId") Long projectId, @Param("code") String code,
                             @Param("checkSheetId") Integer checkSheetId);

    @Modifying
    @Transactional
    @Query("UPDATE CheckSheet cs SET cs.checkSheetId = :checkSheetId, cs.isInTrident = TRUE " +
        "WHERE cs.projectId = :projectId AND cs.no = :code " +
        "AND cs.checkSheetTridentTbl = com.ose.tasks.vo.trident.CheckSheetTridentTbl.A_CHECK_SHEET")
    void updateCheckSheetTidForSheetB(@Param("projectId") Long projectId, @Param("code") String code,
                                      @Param("checkSheetId") Integer checkSheetId);

    @Modifying
    @Transactional
    @Query("UPDATE CheckSheet cs SET cs.isInTrident = :inTrident WHERE cs.projectId = :projectId AND cs.no = :code")
    void updateTridentStatus(@Param("projectId") Long projectId, @Param("code") String code, @Param("inTrident") boolean inTrident);

    Set<CheckSheet> findByProjectIdAndCheckSheetTridentTbl(Long projectId, CheckSheetTridentTbl aCheckSheet);

    Page<CheckSheet> findByProjectIdAndNo(Long projectId, String no,  Pageable pagable);

    Page<CheckSheet> findByProjectIdAndNoIsLike(Long projectId, String no,  Pageable pagable);

    CheckSheet findByProjectIdAndNo(Long projectId, String no);

    List<CheckSheet> findByProjectIdAndDisciplineInOrderByNo(Long projectId, Set<String> disciplines);
}
