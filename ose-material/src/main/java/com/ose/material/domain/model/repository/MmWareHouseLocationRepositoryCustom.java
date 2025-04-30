package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmWareHouseLocationSearchDTO;
import com.ose.material.entity.MmWareHouseLocationEntity;
import org.springframework.data.domain.Page;

public interface MmWareHouseLocationRepositoryCustom {

    /**
     * 查询任务列表
     *
     * @param orgId
     * @param projectId
     * @param mmWareHouseLocationSearchDTO 查询条件
     * @return
     */
    Page<MmWareHouseLocationEntity> search(
        Long orgId,
        Long projectId,
        MmWareHouseLocationSearchDTO mmWareHouseLocationSearchDTO
    );

}
