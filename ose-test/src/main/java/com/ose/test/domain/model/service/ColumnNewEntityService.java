package com.ose.test.domain.model.service;

import com.ose.test.domain.model.repository.ColumnEntityRepository;
import com.ose.test.entity.ColumnEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * 业务代码服务。
 */
@Component
public class ColumnNewEntityService implements ColumnNewEntityInterface {

    // 业务代码数据仓库
    private ColumnEntityRepository columnEntityRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public ColumnNewEntityService(ColumnEntityRepository columnEntityRepository) {
        this.columnEntityRepository = columnEntityRepository;
    }

    /**
     * 取得业务代码列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param type      业务代码类型
     * @return 业务代码列表
     */
    @Override
    public Page<ColumnEntity> list(
        Long orgId,
        Long projectId,
        String type,
        Pageable pageable
    ) {
        return null;
    }

}
