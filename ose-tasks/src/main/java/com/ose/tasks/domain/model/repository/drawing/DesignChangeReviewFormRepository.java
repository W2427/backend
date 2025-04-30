package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.DesignChangeReviewForm;
import com.ose.tasks.vo.bpm.ActInstFinishState;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface DesignChangeReviewFormRepository extends PagingAndSortingWithCrudRepository<DesignChangeReviewForm, Long> {


    DesignChangeReviewForm findByReviewRegisterId(Long id);

    @Query("SELECT count(t) FROM DesignChangeReviewForm t where t.orgId = :orgId and t.projectId = :projectId")
    Long getCountByOrgIdAndProjectId(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    List<DesignChangeReviewForm> findByFinishStateAndActionItemLikeAndReviewRegisterIdNot(
        ActInstFinishState state, String action, Long id);

}
