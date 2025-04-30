package com.ose.tasks.domain.model.repository.bpm;

import com.ose.tasks.entity.bpm.BpmActivityInstanceReport;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BpmActivityInstanceReportRepository extends PagingAndSortingRepository<BpmActivityInstanceReport, Long> {


    @Query("SELECT b FROM BpmActivityInstanceReport b WHERE b.projectId = :projectId AND b.actInstId = :actInstId")
    BpmActivityInstanceReport findActInstByActInstId(@Param("projectId") Long projectId,
                                                      @Param("actInstId") Long actInstId);

    List<BpmActivityInstanceReport> findByProjectIdAndActInstIdInAndStatus(Long projectId, String[] actInstIds, EntityStatus status);
}

