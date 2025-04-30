package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmExInspSchedule;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ose.tasks.entity.bpm.BpmExInspUploadHistory;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;


public interface BpmExInspUploadHistoryRepository extends PagingAndSortingWithCrudRepository<BpmExInspUploadHistory, Long>, BpmExInspUploadHistoryRepositoryCustom {

    List<BpmExInspSchedule> findByIdIn(Long[] ids);


    @Query("UPDATE BpmExInspUploadHistory n SET n.confirmed = :status WHERE n.orgId = :orgId AND n.projectId = :projectId and n.id = :historyId")
    @Transactional
    @Modifying
    void updateStatusById(@Param("orgId") Long orgId,
                          @Param("projectId") Long projectId,
                          @Param("historyId") Long historyId,
                          @Param("status") Boolean status);


    @Query(value = "SELECT beiuh.id AS historyId FROM " +
                   " bpm_external_inspection_upload_history beiuh, bpm_external_inspection_confirm beic " +
                   "WHERE  beiuh.id = beic.upload_history_id AND beiuh.confirmed = 0 " +
                   " AND beic.org_id = :orgId AND beic.project_id = :projectId AND beic.qrcode = :qrcode " +
                   "GROUP BY beiuh.id,beiuh.confirmed having sum(1) = SUM(CASE WHEN beic.STATUS = :status THEN 0 ELSE 1 END )",
    nativeQuery = true)
    List<Tuple> findUnConfirmedUpoadHistory(@Param("orgId") Long orgId,
                                            @Param("projectId") Long projectId,
                                            @Param("qrcode") String qrcode,
                                            @Param("status") String status);
}
