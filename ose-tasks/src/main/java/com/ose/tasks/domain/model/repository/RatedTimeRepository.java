package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.RatedTime;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RatedTimeRepository extends PagingAndSortingWithCrudRepository<RatedTime, Long>, RatedTimeRepositoryCustom {

    /**
     * 根据定额工时ID获取定额工时信息。
     *
     * @param ratedTimeId 定额工时ID
     * @return 定额工时信息
     */
    RatedTime findByIdAndDeletedIsFalse(Long ratedTimeId);

    /**
     * 获取全部定额工时信息。
     *
     * @param ratedTimeCriterionId 定额工时查询字段ID
     * @return 定额工时查询条件列表
     */
    List<RatedTime> findByRatedTimeCriterionIdAndDeletedIsFalse(Long ratedTimeCriterionId);
}
