package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmMaterialCodeEntity;
import org.springframework.data.domain.Page;

public interface MmMaterialCodeRepositoryCustom {

    /**
     * 查询任务列表
     *
     * @param id 查询条件
     * @return
     */
    Page<MmMaterialCodeEntity> search(String id);

}
