package com.ose.tasks.domain.model.repository.p6;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.p6.Projwbs;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 托盘下实体操作接口。
 */
@Transactional
public interface ProjwbsRepository extends PagingAndSortingWithCrudRepository<Projwbs, Integer> {


    @Query("SELECT MAX(pw.wbsId) FROM Projwbs pw")
    Integer getMaxId();

    Projwbs findByProjIdAndWbsShortName(Integer projId, String no);

    Projwbs findByWbsShortNameAndParentWbsIdIsNull(String wbsCode);

    Set<Projwbs> findByProjIdAndParentWbsIdIn(Integer projId, Set<Integer> wbsIds);


    Projwbs findByWbsShortName(String p6WbsCode);

    Projwbs findByProjIdAndParentWbsIdInAndWbsShortName(Integer projId, Set<Integer> wbsIds, String shortName);
}
