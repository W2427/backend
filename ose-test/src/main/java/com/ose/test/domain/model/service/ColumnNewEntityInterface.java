package com.ose.test.domain.model.service;

import com.ose.test.entity.ColumnEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * 业务代码接口。
 */
public interface ColumnNewEntityInterface {

    /**
     * 取得业务代码列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param type      业务代码类型
     * @return 业务代码列表
     */
    Page<ColumnEntity> list(
        Long orgId,
        Long projectId,
        String type,
        Pageable pageable
    );



}
