package com.ose.materialspm.domain.model.service;

import java.util.List;

import com.ose.materialspm.entity.Demo;

/**
 * Demoservice接口
 */
public interface DemoInterface {

    /**
     * 获取全部实体类型
     *
     * @param orgId     组织id
     * @param projectId 项目id
     */
    List<Demo> getByName(String name);

}
