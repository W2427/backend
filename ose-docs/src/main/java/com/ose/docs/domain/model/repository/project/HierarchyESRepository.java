package com.ose.docs.domain.model.repository.project;

import com.ose.docs.entity.project.HierarchyES;
import org.springframework.stereotype.Component;

/**
 * 项目层级结构导入文件信息数据仓库。
 */
@Component
public interface HierarchyESRepository extends ProjectBaseESRepository<HierarchyES> {
}
