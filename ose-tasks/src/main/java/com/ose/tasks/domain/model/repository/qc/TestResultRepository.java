package com.ose.tasks.domain.model.repository.qc;

import com.ose.tasks.entity.qc.BaseConstructionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * 建造实施记录数据仓库。
 */
@NoRepositoryBean
public interface TestResultRepository<T extends BaseConstructionLog> extends CrudRepository<T, Long> {
    /**
     * 获取 列表信息。
     *
     * @param entityId      实体 ID
     * @param processNameEn 工作流阶段
     * @return ISO列表信息
     */
    List<T> findByProcessNameEnAndEntityId(String processNameEn, Long entityId);

    /**
     * 获取 列表信息。
     *
     * @param entityId      实体 ID
     * @param processNameEn 工序名称
     * @param pageable      分页参数
     * @return ISO列表信息
     */
    Page<T> findByProcessNameEnAndEntityIdIs(String processNameEn, Long entityId, Pageable pageable);
}
