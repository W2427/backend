package com.ose.auth.controller;

import com.ose.annotation.InternalAccessOnly;
import com.ose.auth.annotation.CheckPrivilege;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.auth.api.PythonServiceFeignAPI;
import com.ose.auth.api.UserAPI;
import com.ose.auth.domain.model.service.*;
import com.ose.auth.dto.*;
import com.ose.auth.entity.Organization;
import com.ose.auth.entity.UserBasic;
import com.ose.auth.entity.UserPosition;
import com.ose.auth.entity.UserProfile;
import com.ose.auth.vo.VerificationPurpose;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.AccessDeniedError;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.util.RegExpUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "用户接口")
@RestController
public class UserController extends BaseController implements UserAPI {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    // 用户操作服务
    private final UserInterface userService;

    // 图形验证码操作服务
    private final CaptchaInterface captchaInterface;

    // 验证码操作服务
    private final VerificationInterface verificationService;
    private final OrganizationInterface organizationService;
    private final OrgMemberInterface orgMemberService;
    private final UserPositionInterface userPositionInterface;
    private final PythonServiceFeignAPI pythonServiceFeignAPI;

    /**
     * 构造方法。
     *
     * @param userService         用户操作服务
     * @param captchaInterface    图形验证码操作服务
     * @param verificationService 验证码操作服务
     */
    @Autowired
    public UserController(
        UserInterface userService,
        CaptchaInterface captchaInterface,
        VerificationInterface verificationService,
        OrganizationInterface organizationService,
        OrgMemberInterface orgMemberService, UserPositionInterface userPositionInterface,
        PythonServiceFeignAPI pythonServiceFeignAPI
    ) {
        this.userService = userService;
        this.captchaInterface = captchaInterface;
        this.verificationService = verificationService;
        this.organizationService = organizationService;
        this.orgMemberService = orgMemberService;
        this.userPositionInterface = userPositionInterface;
        this.pythonServiceFeignAPI = pythonServiceFeignAPI;
    }

    @RequestMapping(
        method = POST,
        value = "/users/is-access-token-valid",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public Map<String, Object> validateAccessToken() {
        ContextDTO context = getContext();
        Map<String, Object> result = new HashMap<>();
        result.put("context", context);
        return result;
    }

    /**
     * 创建用户账号。
     *
     * @param userDTO 用户信息
     * @return 用户信息
     */
    @Override
    @Operation(
        summary = "创建用户",
        description = "创建用户登录账号，需要系统管理员权限。"
    )
    @RequestMapping(
        method = POST,
        value = "/users",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
//    @CheckPrivilege
    public JsonObjectResponseBody<UserProfile> create(
        @RequestBody @Parameter(description = "用户信息") UserDTO userDTO
    ) {

        ContextDTO context = getContext();

        Long operator = 1L;
        if (context.getOperator() != null) {
            operator = context.getOperator().getId();
        }

        return new JsonObjectResponseBody<>(
            context,
            userService.create(
                operator,
                userDTO
            )
        );

    }

    /**
     * 查询用户。
     *
     * @return 用户信息列表
     */
    @Override
    @Operation(description = "查询用户信息")
    @RequestMapping(
        method = GET,
        value = "/users",
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<UserProfile> search(
        UserCriteriaDTO criteria,
        PageDTO page
    ) {

        ContextDTO context = getContext();

        return (new JsonListResponseBody<>(
            context,
            userService.search(criteria, page)
        )).setIncluded(userService);

    }

    /**
     * 取得用户信息。
     *
     * @param userId 用户 ID
     * @return 用户信息
     */
    @Override
    @Operation(
        summary = "取得用户详细信息",
        description = "取得当前登录用户信息时将路径参数 <code>userId</code> 设置为 \"<code>current</code>\"。"
    )
    @RequestMapping(
        method = GET,
        value = "/users/{userId}",
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonObjectResponseBody<UserProfile> get(
        @PathVariable @Parameter(description = "用户 ID") Long userId
    ) {

        ContextDTO context = getContext();

        if (userId.equals(0L)) {
            userId = context.getOperator().getId();
        }

        UserProfile userProfile = userService.get(userId.toString());

        if (userProfile == null) {
            throw new NotFoundError();
        }

        return (new JsonObjectResponseBody<>(context, userProfile))
            .setIncluded(userService);
    }

    /**
     * 取得用户信息。
     *
     * @param userId 用户 ID
     * @return 用户信息
     */
    @Override
    @Operation(
        summary = "取得用户详细信息(不需要验证)",
        description = "取得当前登录用户信息时将路径参数 <code>userId</code> 设置为 \"<code>current</code>\"。"
    )
    @RequestMapping(
        method = GET,
        value = "/users/{userId}/single",
        produces = APPLICATION_JSON_VALUE
    )
//    @CheckPrivilege
    public JsonObjectResponseBody<UserProfile> getSingle(
        @PathVariable @Parameter(description = "用户 ID") Long userId
    ) {

        ContextDTO context = getContext();

        if (userId.equals(0L)) {
            userId = context.getOperator().getId();
        }

        UserProfile userProfile = userService.get(userId.toString());

        if (userProfile == null) {
            throw new NotFoundError();
        }

        return (new JsonObjectResponseBody<>(context, userProfile))
            .setIncluded(userService);
    }

    /**
     * 修改密码。
     */
    @Override
    @Operation(description = "修改用户登录密码")
    @RequestMapping(
        method = POST,
        value = "/users/{userId}/update-password",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody updatePassword(
        @PathVariable @Parameter(description = "用户 ID") Long userId,
        @RequestBody @Parameter(description = "用户密码修改表单")
        UserPasswordUpdateDTO userPasswordUpdateDTO
    ) {

        CredentialDTO credentials = new CredentialDTO();
        credentials.setUsername(userId.toString());
        credentials.setPassword(userPasswordUpdateDTO.getOriginalPassword());

        UserProfile user = userService.authenticate(credentials);

        userService.updatePassword(
            user.getId().toString(),
            userPasswordUpdateDTO.getPassword()
        );

        return new JsonResponseBody();
    }

    /**
     * 修改密码。
     */
    @Override
    @Operation(description = "管理员修改用户登录密码")
    @RequestMapping(
        method = POST,
        value = "/users/{userId}/admin/update-password",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody updatePasswordByAdmin(
        @PathVariable @Parameter(description = "用户 ID") Long userId,
        @RequestBody @Parameter(description = "用户密码修改表单")
        UserPasswordUpdateDTO userPasswordUpdateDTO
    ) {

        CredentialDTO credentials = new CredentialDTO();
        credentials.setUsername(userId.toString());
        credentials.setPassword(userPasswordUpdateDTO.getOriginalPassword());

        UserProfile user = userService.authenticateNotValidatePassword(credentials);

        userService.updatePassword(
            user.getId().toString(),
            userPasswordUpdateDTO.getPassword()
        );

        return new JsonResponseBody();
    }


    /**
     * 获取电子邮件或短信验证码。
     */
    @Override
    @Operation(description = "获取电子邮件或短信验证码")
    @RequestMapping(
        method = POST,
        value = "/users/{account}/verifications",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody sendVerificationCode(
        @PathVariable @Parameter(description = "用户电子邮箱地址或手机号码") String account,
        @RequestBody @Parameter(description = "验证码获取请求表单")
        UserAccountVerificationSendDTO userAccountVerificationSendDTO
    ) {

        ContextDTO context = getContext();

        // 校验图形验证码
        captchaInterface.consume(
            context.getUserAgent(),
            context.getRemoteAddr(),
            userAccountVerificationSendDTO.getCaptcha()
        );

        String emailAddress = null;
        String mobileNumber = null;

        // 账号符合电子邮箱地址格式时
        if (RegExpUtils.isEmailAddress(account)) {
            emailAddress = account;
            // 账号符合手机号码格式时
        } else if (RegExpUtils.isMobileNumber(account)) {
            mobileNumber = account;
            // 若为用户 ID 或登录用户名则取得用户信息
            // 并在注册了电子邮箱或手机号码时发送电子邮件或短信
        } else if (RegExpUtils.isDecID(account)
            || RegExpUtils.isUsername(account)) {

            UserProfile user = userService.get(account);

            if (user == null) {
                throw new NotFoundError("account not found"); // TODO 使用消息模板
            }

            if (user.getStatus() != EntityStatus.ACTIVE) {
                throw new AccessDeniedError("user account has been disabled"); // TODO 使用消息代码
            }

            emailAddress = user.getEmail();
            mobileNumber = user.getMobile();

            if (StringUtils.isEmpty(emailAddress)
                && StringUtils.isEmpty(mobileNumber)) {
                throw new BusinessError("no Email address or mobile phone"); // TODO 使用消息代码
            }

            // 账号格式不正确时返回错误
        } else {
            // TODO 返回值无效错误
        }

        // 发送电子邮件验证码
        if (!StringUtils.isEmpty(emailAddress)) {
            verificationService.sendEmailVerification(
                emailAddress,
                userAccountVerificationSendDTO.getPurpose()
            );
        }

        // 发送短信验证码
        if (!StringUtils.isEmpty(mobileNumber)) {
            verificationService.sendSMSVerification(
                mobileNumber,
                userAccountVerificationSendDTO.getPurpose()
            );
        }

        return new JsonResponseBody();
    }

    /**
     * 重置密码。
     */
    @Override
    @Operation(description = "重置用户登录密码")
    @RequestMapping(
        method = POST,
        value = "/users/{account}/reset-password",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody resetPassword(
        @PathVariable @Parameter(description = "用户电子邮箱地址或手机号码") String account,
        @RequestBody @Parameter(description = "用户密码重置表单")
        UserPasswordResetDTO userPasswordResetDTO
    ) {

        // 校验电子邮件/短信验证码
        verificationService.verifyVerification(
            account,
            userPasswordResetDTO.getVerificationCode(),
            VerificationPurpose.RESET_PASSWORD
        );

        // 更新密码
        userService.updatePassword(
            account,
            userPasswordResetDTO.getPassword()
        );

        return new JsonResponseBody();
    }

    /**
     * 停用用户账号。
     *
     * @param userId  用户 ID
     * @param version 用户数据版本
     */
    @Override
    @Operation(
        summary = "停用用户登录账号",
        description = "需要系统管理员权限。"
    )
    @RequestMapping(
        method = POST,
        value = "/users/{userId}/disable",
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonResponseBody disable(
        @PathVariable @Parameter(description = "用户 ID") Long userId,
        @RequestParam @Parameter(description = "用户数据版本") long version
    ) {

        ContextDTO context = getContext();

        userService.disable(
            context.getOperator().getId(),
            userId,
            version
        );

        return new JsonResponseBody();
    }

    /**
     * 启用用户账号。
     *
     * @param userId  用户 ID
     * @param version 用户数据版本
     */
    @Override
    @Operation(
        summary = "启用用户登录账号",
        description = "需要系统管理员权限。"
    )
    @RequestMapping(
        method = POST,
        value = "/users/{userId}/enable",
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonResponseBody enable(
        @PathVariable @Parameter(description = "用户 ID") Long userId,
        @RequestParam @Parameter(description = "用户数据版本") long version
    ) {

        ContextDTO context = getContext();

        userService.enable(
            context.getOperator().getId(),
            userId,
            version
        );

        return new JsonResponseBody();
    }

    /**
     * 删除用户信息。
     *
     * @param userId  用户 ID
     * @param version 用户数据版本
     */
    @Override
    @Operation(
        summary = "删除用户登录账号",
        description = "需要系统管理员权限。"
    )
    @RequestMapping(
        method = DELETE,
        value = "/users/{userId}",
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "用户 ID") Long userId,
        @RequestParam @Parameter(description = "用户数据版本") long version
    ) {

        ContextDTO context = getContext();

        userService.delete(
            context.getOperator().getId(),
            userId,
            version
        );

        return new JsonResponseBody();
    }

    /**
     * 更新用户信息。
     *
     * @param userId         用户ID
     * @param userProfileDTO 用户更新信息
     */
    @Override
    @Operation(
        summary = "更新用户信息",
        description = "需要拥有更新用户权限"
    )
    @RequestMapping(
        method = PATCH,
        value = "/users/{userId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonResponseBody updateProfile(
        @PathVariable @Parameter(description = "用户ID") Long userId,
        @RequestBody @Parameter(description = "更新信息") UserProfileDTO userProfileDTO
    ) {
        ContextDTO context = getContext();

        userService.updateProfile(
            context.getOperator().getId(),
            userId,
            userProfileDTO
        );
        return new JsonResponseBody();
    }

    /**
     * 用户切换项目组织。
     *
     * @param userId       用户ID
     * @param switchOrgDTO 项目组织
     */
    @Operation(
        summary = "切换项目组织列表",
        description = "需要拥有更新用户权限"
    )
    @RequestMapping(
        method = POST,
        value = "/users/{userId}/switch-org",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonResponseBody switchProjectOrg(
        @PathVariable @Parameter(description = "用户ID") Long userId,
        @RequestBody @Parameter(description = "项目组织") SwitchOrgDTO switchOrgDTO
    ) {
        orgMemberService.switchProjectOrg(userId, switchOrgDTO.getOrganizationId());
        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "批量取得用户信息（仅供其他服务调用）")
    @RequestMapping(
        method = POST,
        value = "/users/batch-get",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @InternalAccessOnly
//    @CheckPrivilege // TODO
    public JsonListResponseBody<UserBasic> batchGet(
        @RequestBody BatchGetDTO batchGetDTO
    ) {
        return new JsonListResponseBody<>(
            userService.getByEntityIDs(batchGetDTO.getEntityIDs())
        );
    }


    /**
     * 批量获取用户信息。
     *
     * @return 用户列表
     */
    @WithPrivilege
    @Override
    @RequestMapping(
        method = GET,
        value = "/users/batch-get/{orgId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<UserProfile> batchGetByOrgId(
        @PathVariable("orgId") Long orgId
    ) {
        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            userService.getByOrgId(orgId)
        );
    }

    /**
     * 获取用户的顶层项目组织列表。
     *
     * @param userId 用户ID
     * @return 项目组织列表
     */
    @Operation(
        summary = "用户顶层项目组织列表"
    )
    @GetMapping(
        value = "/users/{userId}/top-project-orgs",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    @Override
    public JsonListResponseBody<Organization> getTopProjectOrgs(
        @PathVariable @Parameter(description = "用户ID") Long userId
    ) {

        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            organizationService.getTopProjectOrgs(userId)
        );
    }

    @Operation(description = "取得工作组中拥有特定权限的成员列表")
    @Override
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/members-with-privileges",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<UserBasic> getByPrivileges(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @RequestBody @Valid TeamPrivilegeListDTO teamPrivileges
    ) {
        return new JsonListResponseBody<>(userService.getByPrivileges(orgId, teamPrivileges.toMap()));
    }

    /**
     * 根据用户名获取用户信息（遗留问题导入）。
     *
     * @param userNameCriteriaDTO 用户名信息
     * @return 用户信息
     */
    @Operation(description = "根据用户名获取用户信息")
    @PostMapping(
        value = "/users/get-by-username",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    @Override
    public JsonListResponseBody<UserProfile> getUserByUsername(
        @RequestBody UserNameCriteriaDTO userNameCriteriaDTO
    ) {
        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            userService.getByUsername(userNameCriteriaDTO.getUserNames())
        );
    }

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
    public JsonListResponseBody<UserProfile> getUserByUsername(
        @PathVariable("orgId") Long orgId,
        @PathVariable("username") String username
    ) {

        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            userService.getByUsername(orgId, username)
        );
    }

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
    public JsonListResponseBody<UserProfile> getUserByName(
        @PathVariable("orgId") Long orgId,
        @PathVariable("name") String name
    ) {

        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            userService.getByName(orgId, name)
        );
    }

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
    public JsonObjectResponseBody<UserProfile> getUserByUsername(
        @PathVariable("username") String username
    ) {

        ContextDTO context = getContext();

        return new JsonObjectResponseBody<>(
            context,
            userService.getByUsername(username)
        );
    }

    @RequestMapping(
        method = GET,
        value = "/users/{userId}/list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonObjectResponseBody<AuthCheckDTO> getOrganizationByUserId(@PathVariable("userId") Long userId) {

        return new JsonObjectResponseBody<>(userService.getOrganizationByUserId(userId));
    }


    /**
     * 查询用户。
     *
     * @return 用户信息列表
     */
    @Override
    @Operation(description = "查询用户信息")
    @RequestMapping(
        method = GET,
        value = "/users/list",
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<UserProfile> searchList() {

        ContextDTO context = getContext();

        return (new JsonListResponseBody<>(
            context,
            userService.searchList()
        )).setIncluded(userService);

    }

    /**
     * 按条件导出用户列表。
     *
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/users/download"
    )
    @Operation(description = "按条件导出用户列表")
    @WithPrivilege
    @Override
    public void downloadUsers(UserCriteriaDTO criteriaDTO) throws IOException {

        final OperatorDTO operator = getContext().getOperator();
        File excel = userService.saveDownloadFile(criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-users.xlsx\""
            );

            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("下载文件出错");
        }

        response.flushBuffer();
    }


    /**
     * 查询用户所在的项目信息。
     *
     * @return 项目列表
     */
    @Override
    @Operation(description = "查询用户所在的项目信息")
    @RequestMapping(
        method = GET,
        value = "/user/{userId}/projects",
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<UserProjectDTO> searchProjects(
        @PathVariable @Parameter(description = "用户 ID") Long userId,
        UserProjectSearchDTO criteria
    ) {

        ContextDTO context = getContext();

        return (new JsonListResponseBody<>(
            context,
            userService.searchProjects(userId, criteria)
        )).setIncluded(userService);

    }


    /**
     * 查询用户。
     *
     * @return 用户信息列表
     */
    @Override
    @Operation(description = "查询用户信息")
    @RequestMapping(
        method = GET,
        value = "/users/{userId}/org/{orgType}/list",
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<UserProfile> searchProjectOrgUserList(
        @PathVariable("userId") Long userId,
        @PathVariable("orgType") String orgType
    ) {

        return (new JsonListResponseBody<>(
            userService.getProjectOrgUsersByUserId(userId, orgType)
        ));
    }


    /**
     * 用户是否存在于组织中
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
    @Override
    @WithPrivilege
    public JsonListResponseBody<Organization> getRootOrgListByUserId(
        @PathVariable("userId") Long userId,
        @PathVariable("orgType") String orgType) {
        return new JsonListResponseBody<>(
            organizationService.getRootOrgListByUserId(userId, orgType)
        );
    }

    /**
     * 获取所有的职位列表。
     * @return 职位列表
     */
    @GetMapping(
        value = "/user-positions",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonListResponseBody<UserPosition> getAllPositions() {
        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            userPositionInterface.getAllPositions()
        );
    }

    /**
     * 统计视图
     * @return 统计结果
     */
    @WithPrivilege
    @Override
    public Object overview(@RequestBody PythonQueryDTO queryDTO) {
        return pythonServiceFeignAPI.overview(queryDTO);
    }
}
