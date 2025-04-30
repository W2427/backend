package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.entity.wbs.entry.WBSEntityProportion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * WBS 实体条目抽检比例视图接口。
 */
public interface WBSEntityProportionRepository extends CrudRepository<WBSEntityProportion, Long> {

    /**
     * 取得项目的四级计划抽检比例设置列表。
     *
     * @param projectId 项目 ID
     * @return 抽检比例设置列表
     */
    List<WBSEntityProportion> findByProjectId(Long projectId);

}
