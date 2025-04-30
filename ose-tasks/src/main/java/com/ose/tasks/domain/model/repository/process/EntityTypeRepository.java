package com.ose.tasks.domain.model.repository.process;

import com.ose.tasks.entity.process.EntityType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 实体类型视图数据仓库。
 */
public interface EntityTypeRepository extends CrudRepository<EntityType, Long> {

    /**
     * 取得实体类型列表。
     *
     * @param projectId 项目 ID
     * @return 实体类型列表
     */
    List<EntityType> findByProjectId(Long projectId);

}
