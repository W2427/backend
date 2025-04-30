package com.ose.auth.domain.model.service;

import com.ose.auth.dto.*;
import com.ose.auth.entity.UserBasic;
import com.ose.auth.entity.UserProfile;
import com.ose.dto.PageDTO;
import com.ose.service.EntityInterface;
import org.springframework.data.domain.Page;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户接口。
 */
public interface UserInterface extends EntityInterface {

    /**
     * 检查登录用户名是否可用。
     *
     * @param username 登录用户名
     * @return 登录用户名是否可用
     */
    Boolean isUsernameAvailable(String username);

    /**
     * 检查手机号码是否可用。
     *
     * @param mobile 手机号码
     * @return 手机号码是否可用
     */
    Boolean isMobileAvailable(String mobile);

    /**
     * 检查电子邮箱地址是否可用。
     *
     * @param email 电子邮箱地址
     * @return 电子邮箱地址是否可用
     */
    Boolean isEmailAvailable(String email);

    /**
     * 创建用户。
     *
     * @param operatorId 操作者 ID
     * @param userDTO    用户数据
     */
    UserProfile create(Long operatorId, UserDTO userDTO);

    /**
     * 创建用户。
     *
     * @param operatorId 操作者 ID
     * @param userId     用户 ID
     * @param userDTO    用户数据
     */
    UserProfile create(Long operatorId, Long userId, UserDTO userDTO);

    /**
     * 鉴定用户登录凭证。
     *
     * @param credentials 用户登录凭证
     * @return 用户信息
     */
    UserProfile authenticate(CredentialDTO credentials);

    /**
     * 鉴定用户登录凭证不验证密码。
     *
     * @param credentials 用户登录凭证
     * @return 用户信息
     */
    UserProfile authenticateNotValidatePassword(CredentialDTO credentials);

    /**
     * 查询用户信息。
     *
     * @param criteriaDTO 查询条件
     * @param pagination  分页参数
     * @return 用户分页数据
     */
    Page<UserProfile> search(UserCriteriaDTO criteriaDTO, PageDTO pagination);

    /**
     * 取得用户信息列表。
     *
     * @param entityIDs 用户数据实体 ID 列表
     * @return 用户信息列表
     */
    List<UserBasic> getByEntityIDs(Set<Long> entityIDs);

    /**
     * 根据用户权限取得用户信息列表。
     *
     * @param orgId          组织 ID
     * @param teamPrivileges 工作组-权限映射表
     * @return 用户信息列表
     */
    List<UserBasic> getByPrivileges(Long orgId, Map<Long, Set<String>> teamPrivileges);

    /**
     * 取得用户信息。
     *
     * @param account 用户账号（ID、登录用户名、电子邮箱地址或手机号码）
     * @return 用户信息
     */
    UserProfile get(String account);

    /**
     * 取得系统用户 ID。
     *
     * @return 系统用户 ID
     */
    Long getSystemUserId();

    /**
     * 停用用户账号。
     *
     * @param operatorId 操作者 ID
     * @param userId     用户 ID
     * @param version    用户信息版本
     * @return 操作是否成功
     */
    boolean disable(Long operatorId, Long userId, long version);

    /**
     * 启用用户账号。
     *
     * @param operatorId 操作者 ID
     * @param userId     用户 ID
     * @param version    用户信息版本
     * @return 操作是否成功
     */
    boolean enable(Long operatorId, Long userId, long version);

    /**
     * 删除用户账号。
     *
     * @param operatorId 操作者 ID
     * @param userId     用户 ID
     * @param version    用户信息版本
     * @return 操作是否成功
     */
    boolean delete(Long operatorId, Long userId, long version);

    /**
     * 更新登录密码。
     *
     * @param account  账号
     * @param password 新密码
     */
    void updatePassword(String account, String password);

    /**
     * 更新用户信息。
     *
     * @param operatorId     操作人ID
     * @param userId         操作用户ID
     * @param userProfileDTO 更新数据
     */
    void updateProfile(Long operatorId, Long userId, UserProfileDTO userProfileDTO);

    /**
     * 获取所有组织中的成员列表。
     *
     * @param organizationId    组织ID
     * @param queryOrgMemberDTO 查询参数
     * @param pageDTO           分页参数
     * @return 用户列表
     */
    Page<UserProfile> getOrgMembers(Long organizationId, QueryOrgMemberDTO queryOrgMemberDTO, PageDTO pageDTO);

    /**
     * 获取单个组织中的成员列表。
     *
     * @param organizationId    组织ID
     * @param queryOrgMemberDTO 查询参数
     * @param pageDTO           分页参数
     * @return 用户列表
     */
    Page<UserProfile> getSingleOrgMembers(Long organizationId, QueryOrgMemberDTO queryOrgMemberDTO, PageDTO pageDTO);

    /**
     * 获取角色成员列表。
     *
     * @param roleId             角色ID
     * @param queryRoleMemberDTO 查询条件
     * @return 用户列表
     */
    Page<UserProfile> getRoleMembers(Long roleId, QueryRoleMemberDTO queryRoleMemberDTO);

    /**
     * 根据用户名获取用户信息。
     *
     * @param userNames 用户名称
     * @return 用户信息
     */
    List<UserProfile> getByUsername(List<String> userNames);

    /**
     * 根据用户名获取用户信息。
     *
     * @param userName 用户名称
     * @return 用户信息
     */
    UserProfile getByUsername(String userName);

    /**
     * 根据用户名获取用户信息。
     *
     * @param username 用户名称
     * @return 用户信息
     */
    List<UserProfile> getByUsername(Long orgId, String username);

    List<UserProfile> getByOrgId(Long orgId);

    List<UserProfile> getByName(Long orgId, String name);

    AuthCheckDTO getOrganizationByUserId(Long userId);

    /**
     * 查询用户信息。
     *
     * @return 用户分页数据
     */
    List<UserProfile> searchList();

    File saveDownloadFile(UserCriteriaDTO criteriaDTO,
                          Long operatorId);

    /**
     * 查询用户所在项目信息。
     *
     * @param userId
     * @param criteriaDTO 查询条件
     * @return 用户分页数据
     */
    Page<UserProjectDTO> searchProjects(Long userId, UserProjectSearchDTO criteriaDTO);

    List<UserProfile> getProjectOrgUsersByUserId(Long userId, String orgType);
}
