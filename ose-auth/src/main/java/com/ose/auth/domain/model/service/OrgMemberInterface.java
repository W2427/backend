package com.ose.auth.domain.model.service;

import com.ose.auth.dto.QueryOrgMemberDTO;
import com.ose.auth.entity.UserOrganization;
import com.ose.auth.vo.OrgType;
import com.ose.service.EntityInterface;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 组织用户接口。
 */
public interface OrgMemberInterface extends EntityInterface {

    /**
     * 获取组织成员。
     *
     * @param organizationId 组织ID
     * @return 成员列表
     */
    Page<UserOrganization> members(Long organizationId, QueryOrgMemberDTO queryOrgMemberDTO);

    /**
     * 获取项目组织下用户的部门列表。
     *
     * @param organizationId 组织ID
     * @param userId         用户ID
     * @param type           组织类型
     * @return 用户部门列表
     */
    List<UserOrganization> getDepartments(Long organizationId, Long userId, OrgType type);

    /**
     * 用户切换项目组织。
     *
     * @param userId         用户ID
     * @param organizationId 组织ID
     */
    void switchProjectOrg(Long userId, Long organizationId);

}
