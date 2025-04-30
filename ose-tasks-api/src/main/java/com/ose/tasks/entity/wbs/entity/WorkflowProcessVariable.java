package com.ose.tasks.entity.wbs.entity;

/**
 * 模块工作流参数接口。
 */
public interface WorkflowProcessVariable {

    /**
     * 取得工作流参数名。
     *
     * @return 工作流参数名
     */
    String getVariableName();

    /**
     * 设置工作流参数字段。
     */
    default void setVariableFields() {
    }

    /**
     * 返回名称
     *
     * @return
     */
    String getName();

}
