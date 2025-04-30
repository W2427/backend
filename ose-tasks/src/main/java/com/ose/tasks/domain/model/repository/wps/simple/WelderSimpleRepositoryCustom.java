package com.ose.tasks.domain.model.repository.wps.simple;

import com.ose.tasks.dto.wps.simple.WelderSimpleSearchDTO;
import com.ose.tasks.entity.wps.simple.WelderSimplified;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;

/**
 * 焊工(简化） CRUD 操作接口。
 */
public interface WelderSimpleRepositoryCustom {

    /**
     * 焊工列表。
     *
     * @param orgId
     * @param projectId
     * @param status
     * @param wpsSimpleSearchDTO
     * @return
     */
    Page<WelderSimplified> findList(Long orgId, Long projectId, EntityStatus status, WelderSimpleSearchDTO wpsSimpleSearchDTO);
}
