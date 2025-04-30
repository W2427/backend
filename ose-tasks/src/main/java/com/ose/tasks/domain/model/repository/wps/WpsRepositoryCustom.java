package com.ose.tasks.domain.model.repository.wps;

import com.ose.tasks.dto.wps.WpsCriteriaDTO;
import com.ose.tasks.entity.wps.Wps;
import org.springframework.data.domain.Page;

public interface WpsRepositoryCustom {

    /**
     * 获取wps列表。
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param wpsCriteriaDTO 查询条件
     * @return wps列表
     */
    Page<Wps> search(Long orgId, Long projectId, WpsCriteriaDTO wpsCriteriaDTO);

}
