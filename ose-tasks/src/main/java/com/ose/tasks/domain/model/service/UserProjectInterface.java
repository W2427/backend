package com.ose.tasks.domain.model.service;

import com.ose.auth.entity.Organization;

import java.util.List;

/**
 * 获取用户的顶层项目组织列表。
 */
public interface UserProjectInterface {

    /**
     * 获取用户的顶层项目组织列表
     * @return
     */
    List<Organization> getProjectOrgs(
        Long userId
    );

}
