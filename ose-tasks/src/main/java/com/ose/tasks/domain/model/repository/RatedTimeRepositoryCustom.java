package com.ose.tasks.domain.model.repository;

import com.ose.tasks.dto.RatedTimeCriteriaDTO;
import com.ose.tasks.entity.RatedTime;
import org.springframework.data.domain.Page;

public interface RatedTimeRepositoryCustom {

    /**
     * 获取额定工时列表。
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param ratedTimeCriteriaDTO 查询条件
     * @return 额定工时列表
     */
    Page<RatedTime> search(Long orgId, Long projectId, RatedTimeCriteriaDTO ratedTimeCriteriaDTO);
}
