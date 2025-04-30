package com.ose.auth.domain.model.service;

import com.ose.auth.dto.AuthCheckDTO;
import com.ose.auth.dto.PrivilegeCheckDTO;
import com.ose.auth.dto.UserPrivilegeDTO;
import com.ose.dto.OperatorDTO;

import java.util.List;

/**
 * 权限服务接口。
 */
public interface PrivilegeInterface {

    /**
     * 检查用户是否拥有指定的权限。
     *
     * @param operator          操作者信息
     * @param privilegeCheckDTO 权限检查数据传输对象
     * @return 用户是否拥有指定的权限
     */
    boolean hasPrivilege(
        OperatorDTO operator,
        PrivilegeCheckDTO privilegeCheckDTO
    );

    /**
     * 取得当前用户在指定组织所属顶级组织及所属顶级组织的所有下级组织所用有的所有权限，并根据作用域（组织）分组。
     *
     * @param operator 用户信息
     * @param orgId    组织 ID
     * @return 用户权限列表
     */
    List<UserPrivilegeDTO> getUserPrivileges(OperatorDTO operator, Long orgId);

    /**
     * 取得当前用户在指定组织及其所有下级组织所用有的所有权限。
     *
     * @param operator 用户信息
     * @param orgId    组织 ID
     * @return 用户权限列表
     */
    UserPrivilegeDTO getUserAvailablePrivileges(
        OperatorDTO operator,
        Long orgId
    );

    boolean hasAuth(AuthCheckDTO authCheckDTO);
}
