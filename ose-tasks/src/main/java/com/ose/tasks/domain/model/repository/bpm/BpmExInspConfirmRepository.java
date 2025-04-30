package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;
import java.util.Optional;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ose.tasks.entity.bpm.BpmExInspConfirm;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;

@Transactional
public interface BpmExInspConfirmRepository extends PagingAndSortingWithCrudRepository<BpmExInspConfirm, Long> {


    Page<BpmExInspConfirm> findByProjectIdAndUploadHistoryIdOrderByCreatedAtAsc(
        Long projectId,
        Long uploadHistoryId,
        Pageable toPageable
    );

    @Query(value = "SELECT SUM(1) AS totalCount, " +
        "SUM(CASE status WHEN 'ACTIVE' THEN 1 ELSE 0 END) AS unConfirmedCount " +
        "FROM " +
        "bpm_external_inspection_confirm AS beic " +
        "WHERE upload_history_id = :exInspUploadHistoryId AND org_id = :orgId AND project_id =:projectId",
        nativeQuery = true)
    Tuple findCounts(@Param("orgId") Long orgId,
                     @Param("projectId") Long projectId,
                     @Param("exInspUploadHistoryId") Long exInspUploadHistoryId);


    @Query("UPDATE BpmExInspConfirm n SET n.status = :status " +
        "WHERE n.orgId = :orgId AND n.projectId = :projectId AND n.qrcode = :qrCode AND n.status = com.ose.vo.EntityStatus.ACTIVE ")
    @Transactional
    @Modifying
    void updateRestStatus(@Param("orgId") Long orgId,
                          @Param("projectId") Long projectId,
                          @Param("qrCode") String qrCode,
                          @Param("status") EntityStatus status);

    Optional<BpmExInspConfirm> findByIdAndStatusIn(Long exInspConfirmId, List<EntityStatus> status);

    List<BpmExInspConfirm> findByOrgIdAndProjectIdAndQrcode(Long orgId, Long projectId, String qrcode);


    BpmExInspConfirm findFirstByOrgIdAndSeriesNoOrderByCreatedAtDesc(Long orgId, String seriesNo);

    BpmExInspConfirm findFirstByOrgIdAndSeriesNo(Long orgId, String seriesNo);
}
