package com.ose.materialspm.domain.model.repository;

import org.springframework.data.domain.Page;

import com.ose.materialspm.entity.DemoEntity;

public interface DemoRepositoryCustom {

    /**
     * 查询任务列表
     *
     * @param id 查询条件
     * @return
     */
    Page<DemoEntity> search(String id);

}
