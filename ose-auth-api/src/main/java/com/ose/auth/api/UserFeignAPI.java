package com.ose.auth.api;

import com.ose.auth.dto.*;
import com.ose.auth.entity.Organization;
import com.ose.auth.entity.UserBasic;
import com.ose.auth.entity.UserProfile;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 用户信息接口。
 */
@FeignClient(name = "ose-auth", contextId = "userFeign")
public interface UserFeignAPI {

    /**
     * 创建用户账号。
     *
     * @param userDTO 用户信息
     * @return 用户信息
     */
    @RequestMapping(
        method = POST,
        value = "/users",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<UserProfile> create(
        @RequestBody UserDTO userDTO
    );

    /**
     * 取得用户信息。
     *
     * @param userId 用户 ID
     * @return 用户信息
     */
    @RequestMapping(
        method = GET,
        value = "/users/{userId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<UserProfile> get(
        @PathVariable("userId") Long userId
    );

    /**
     * 取得用户信息(跳过验证)。
     *
     * @param userId 用户 ID
     * @return 用户信息
     */
    @RequestMapping(
        method = GET,
        value = "/users/{userId}/single",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<UserProfile> getSingle(
        @PathVariable("userId") Long userId
    );

    /**
     * 修改密码。
     */
    @RequestMapping(
        method = POST,
        value = "/users/{userId}/update-password",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updatePassword(
        @PathVariable("userId") Long userId,
        @RequestBody UserPasswordUpdateDTO userPasswordUpdateDTO
    );

    /**
     * 管理员修改密码。
     */
    @RequestMapping(
        method = POST,
        value = "/users/{userId}/admin/update-password",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updatePasswordByAdmin(
        @PathVariable("userId") Long userId,
        @RequestBody UserPasswordUpdateDTO userPasswordUpdateDTO
    );

    /**
     * 获取电子邮件或短信验证码。
     */
    @RequestMapping(
        method = POST,
        value = "/users/{account}/verifications",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody sendVerificationCode(
        @PathVariable("account") String account,
        @RequestBody UserAccountVerificationSendDTO userAccountVerificationSendDTO
    );

    /**
     * 获取电子邮件或短信验证码。
     */
    @RequestMapping(
        method = POST,
        value = "/users/{account}/reset-password",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody resetPassword(
        @PathVariable("account") String account,
        @RequestBody UserPasswordResetDTO userPasswordResetDTO
    );

    /**
     * 停用用户账号。
     *
     * @param userId  用户 ID
     * @param version 用户数据版本
     */
    @RequestMapping(
        method = POST,
        value = "/users/{userId}/disable",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody disable(
        @PathVariable("userId") Long userId,
        @RequestParam("version") long version
    );

    /**
     * 启用用户账号。
     *
     * @param userId  用户 ID
     * @param version 用户数据版本
     */
    @RequestMapping(
        method = POST,
        value = "/users/{userId}/enable",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody enable(
        @PathVariable("userId") Long userId,
        @RequestParam("version") long version
    );

    /**
     * 删除用户信息。
     *
     * @param userId  用户 ID
     * @param version 用户数据版本
     */
    @RequestMapping(
        method = DELETE,
        value = "/users/{userId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("userId") Long userId,
        @RequestParam("version") long version
    );

    /**
     * 更新用户信息。
     *
     * @param userId         用户ID
     * @param userProfileDTO 更新信息
     */
    @RequestMapping(
        method = PATCH,
        value = "/users/{userId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateProfile(
        @PathVariable("userId") Long userId,
        @RequestBody UserProfileDTO userProfileDTO
    );

    /**
     * 批量获取用户信息。
     *
     * @return 用户列表
     */
    @RequestMapping(
        method = POST,
        value = "/users/batch-get",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserBasic> batchGet(
        @RequestBody BatchGetDTO batchGetDTO
    );


    /**
     * 批量获取用户信息。
     *
     * @return 用户列表
     */
    @RequestMapping(
        method = GET,
        value = "/users/batch-get/{orgId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserProfile> batchGetByOrgId(
        @PathVariable("orgId") Long orgId

    );

    /**
     * 获取用户的顶层项目组织列表。
     *
     * @param userId 用户ID
     * @return 项目组织列表
     */
    @GetMapping(
        value = "/users/{userId}/top-project-orgs",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Organization> getTopProjectOrgs(
        @PathVariable("userId") Long userId
    );

    /**
     * 取得工作组中拥有特定权限的成员列表。
     *
     * @param orgId          组织 ID
     * @param teamPrivileges 工作组-权限映射表
     * @return 成员信息
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/members-with-privileges",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserBasic> getByPrivileges(
        @PathVariable("orgId") Long orgId,
        @RequestBody TeamPrivilegeListDTO teamPrivileges
    );

    /**
     * 根据用户名获取用户信息（遗留问题导入）。
     *
     * @param userNameCriteriaDTO 用户名信息
     * @return 用户信息
     */
    @PostMapping(
        value = "/users/get-by-username",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserProfile> getUserByUsername(
        @RequestBody UserNameCriteriaDTO userNameCriteriaDTO
    );

    /**
     * 根据用户名获取用户信息（遗留问题导入）。
     *
     * @return 用户信息
     */
    @PostMapping(
        value = "/users/{orgId}/get-by-username/{username}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserProfile> getUserByUsername(
        @PathVariable("orgId") Long orgId,
        @PathVariable("username") String username
    );

    /**
     * 根据用户名获取用户信息（遗留问题导入）。
     *
     * @return 用户信息
     */
    @PostMapping(
        value = "/users/{orgId}/get-by-name/{name}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserProfile> getUserByName(
        @PathVariable("orgId") Long orgId,
        @PathVariable("name") String name
    );

    /**
     * 根据用户名获取用户信息（system）。
     *
     * @return 用户信息
     */
    @PostMapping(
        value = "/users/{username}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<UserProfile> getUserByUsername(
        @PathVariable("username") String username
    );

    @RequestMapping(
        method = GET,
        value = "/users/{userId}/list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<AuthCheckDTO> getOrganizationByUserId(@PathVariable("userId") Long userId);

    /**
     * 查询用户。
     *
     * @return 用户信息列表
     */
    @RequestMapping(
        method = GET,
        value = "/users/list",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserProfile> searchList();

    /**
     * 查询在项目组织上是leader下的用户。
     *
     * @return 用户信息列表
     */
    @RequestMapping(
        method = GET,
        value = "/users/{userId}/org/{orgType}/list",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserProfile> searchProjectOrgUserList(
        @PathVariable("userId") Long userId,
        @PathVariable("orgType") String orgType
    );


    /**
     * 获取
     *
     * @param userId
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "/users/{userId}/root/{orgType}/list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Organization> getRootOrgListByUserId(
        @PathVariable("userId") Long userId,
        @PathVariable("orgType") String orgType
    );
}
