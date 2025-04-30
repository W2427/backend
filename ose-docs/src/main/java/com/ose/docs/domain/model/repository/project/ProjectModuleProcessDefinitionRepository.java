package com.ose.docs.domain.model.repository.project;

import com.ose.docs.entity.project.ProjectModuleProcessDefinitionES;
import org.springframework.stereotype.Component;

/**
 * 项目模块流程定义文件信息数据仓库。
 */
@Component
public interface ProjectModuleProcessDefinitionRepository
    extends ProjectBaseESRepository<ProjectModuleProcessDefinitionES> {
}
