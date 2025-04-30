package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmMaterialCodeTypeEntity;
import org.springframework.data.domain.Page;

public interface MmMaterialCodeTypeProjectRepositoryCustom {

    /**
     * 查询任务列表
     *
     * @param id 查询条件
     * @return
     */
    Page<MmMaterialCodeTypeEntity> search(String id);

}
