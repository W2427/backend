package com.ose.docs.domain.model.repository.project;

import com.ose.docs.entity.project.PlanES;
import org.springframework.stereotype.Component;

/**
 * 项目计划导入文件信息数据仓库。
 */
@Component
public interface PlanESRepository extends ProjectBaseESRepository<PlanES> {
}
