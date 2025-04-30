package com.ose.tasks.entity.process;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 项目模块工作流定义。
 */
@Entity
@Table(name = "project_module_process_definition")
public class ModuleProcessDefinitionBasic extends ModuleProcessDefinitionBase {

    private static final long serialVersionUID = 1800347515035146050L;

}
