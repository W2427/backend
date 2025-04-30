package com.ose.auth.domain.model.service;

import com.alibaba.excel.EasyExcel;
import com.google.common.base.Joiner;
import com.ose.auth.vo.OrgType;
import com.ose.exception.*;
import com.ose.util.*;
import com.ose.auth.domain.model.repository.*;
import com.ose.auth.dto.*;
import com.ose.auth.entity.*;
import com.ose.dto.BaseDTO;
import com.ose.dto.PageDTO;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.service.StringRedisService;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务。
 */
@Component
public class UserService extends StringRedisService implements UserInterface {

    // 密码加密迭代次数
    private static final int PASSWORD_LOG_ROUNDS = 12;

    // 系统用户 ID Redis Key
    private static final String SYSTEM_USER_ID_KEY = "SYSTEM-USER-ID";

    // 用户信息操作仓库
    private final UserRepository userRepository;

    // 用户资料操作仓库
    private final UserProfileRepository userProfileRepository;

    // 用户基本信息操作仓库
    private final UserBasicRepository userBasicRepository;
    private UserOrganizationRepository userOrganizationRepository;
    private UserRoleRepository userRoleRepository;
    private OrganizationRepository organizationRepository;
    private RoleRepository roleRepository;

    /**
     * 使用 BCrypt 算法对密码加密。
     *
     * @param password 原始密码
     * @return 加密后的密码
     */
    private static String encryptPassword(String password) {
        return BCrypt.hashpw(
            CryptoUtils.md5(password),
            BCrypt.gensalt(PASSWORD_LOG_ROUNDS)
        );
    }

    /**
     * 校验密码。
     *
     * @param credentials 用户登录凭证
     * @param user        用户数据实体
     * @return 密码是否相符
     */
    private static boolean validatePassword(
        CredentialDTO credentials,
        User user
    ) {
        return BCrypt.checkpw(
            CryptoUtils.md5(credentials.getPassword()),
            user.getPassword()
        ) || BCrypt.checkpw(
            credentials.getPassword(),
            user.getPassword()
        );
    }

    /**
     * 构造方法。
     *
     * @param userRepository      用户操作仓库
     * @param stringRedisTemplate Redis 模板
     */
    @Autowired
    public UserService(
        UserRepository userRepository,
        UserProfileRepository userProfileRepository,
        UserBasicRepository userBasicRepository,
        StringRedisTemplate stringRedisTemplate,
        UserOrganizationRepository userOrganizationRepository,
        UserRoleRepository userRoleRepository,
        OrganizationRepository organizationRepository,
        RoleRepository roleRepository
    ) {
        super(stringRedisTemplate);
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.userBasicRepository = userBasicRepository;
        this.userOrganizationRepository = userOrganizationRepository;
        this.userRoleRepository = userRoleRepository;
        this.organizationRepository = organizationRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * 检查登录用户名是否可用。
     *
     * @param username 登录用户名
     * @return 登录用户名是否可用
     */
    @Override
    public Boolean isUsernameAvailable(String username) {
        return userProfileRepository
            .existsByUsernameAndDeletedIsFalse(username);
    }

    /**
     * 检查手机号码是否可用。
     *
     * @param mobile 手机号码
     * @return 手机号码是否可用
     */
    @Override
    public Boolean isMobileAvailable(String mobile) {
        return userProfileRepository
            .existsByMobileAndDeletedIsFalse(mobile);
    }

    /**
     * 检查电子邮箱地址是否可用。
     *
     * @param email 电子邮箱地址
     * @return 电子邮箱地址是否可用
     */
    @Override
    public Boolean isEmailAvailable(String email) {
        return userProfileRepository
            .existsByEmailAndDeletedIsFalse(email);
    }

    /**
     * 创建用户。
     *
     * @param operatorId 操作者 ID
     * @param userDTO    用户数据
     */
    @Override
    public UserProfile create(Long operatorId, UserDTO userDTO) {
        return create(operatorId, null, userDTO);
    }

    /**
     * 创建用户。
     *
     * @param operatorId 操作者 ID
     * @param userId     用户 ID
     * @param userDTO    用户数据
     */
    @Override
    public UserProfile create(
        Long operatorId,
        Long userId,
        UserDTO userDTO
    ) {

        User user = BeanUtils.copyProperties(userDTO, new User(userId));

        if (user.getPassword() == null) {
            user.setPassword(RandomUtils.getHexString(16));
        }

        if (user.getStatus() == null) {
            user.setStatus(EntityStatus.ACTIVE);
        } else if (EntityStatus.DISABLED.equals(user.getStatus())) {
            user.setDisabled(true);
        }

        if (user.getUsername() != null
            && userProfileRepository
            .existsByUsernameAndDeletedIsFalse(user.getUsername())) {
            throw new DuplicatedError("error.user-account.duplicate.username");
        }

        if (user.getMobile() != null
            && userProfileRepository
            .existsByMobileAndDeletedIsFalse(user.getMobile())) {
            throw new DuplicatedError("error.user-account.duplicate.user-mobile");
        }

        if (user.getEmail() != null
            && userProfileRepository
            .existsByEmailAndDeletedIsFalse(user.getEmail())) {
            throw new DuplicatedError("error.user-account.duplicate.user-email");
        }

        // 判断国籍来确定name拼接
//        String name = "";
//        if (userDTO.getNationality() != null && userDTO.getNationality()) {
//            name = userDTO.getFirstNameEn() + " " + userDTO.getLastNameEn() + " " + userDTO.getLastNameCn() + userDTO.getFirstNameCn();
//        } else {
//            name = userDTO.getFirstNameEn() + " " + userDTO.getLastNameEn();
//        }
//        user.setName(name);

        user.setPassword(encryptPassword(user.getPassword()));
        user.setCreatedBy(operatorId);
        user.setCreatedAt();
        user.setLastModifiedBy(operatorId);
        user.setLastModifiedAt();

        user.validate();

        return BeanUtils.clone(userRepository.save(user), UserProfile.class);
    }

    /**
     * 鉴定用户登录凭证。
     *
     * @param credentials 用户登录凭证
     * @return 用户信息
     */
    @Override
    public UserProfile authenticate(CredentialDTO credentials) {

        User user = null;
        String username = credentials.getUsername();

        if (credentials.getUser()) {
            user = userRepository.findFirstByDeletedIsFalse();
        } else
            // 通过手机号码登录
            if (RegExpUtils.isMobileNumber(username)) {
                user = userRepository.findByMobileAndDeletedIsFalse(username);
                // 通过电子邮箱地址登录
            } else if (RegExpUtils.isEmailAddress(username)) {
                username = username.toLowerCase();
                user = userRepository.findByEmailAndDeletedIsFalse(username);
                // 通过登录用户名登录
            } else if (RegExpUtils.isUsername(username)) {
                user = userRepository.findByUsernameAndDeletedIsFalse(username);
                // 通过用户 ID 登录
            } else if (RegExpUtils.isDecID(username)) {
                user = userRepository.findByIdAndDeletedIsFalse(LongUtils.parseLong(username));
            }

        // 检查用户登录凭证
        if (!credentials.getUser() && (user == null || !validatePassword(credentials, user))) {
            throw new AuthenticationError();
        }

        // 用户账号关闭时返回访问被拒绝错误
        if (user.getStatus() != EntityStatus.ACTIVE) {
            throw new AccessDeniedError("error.user-account.disabled");
        }

        UserProfile userProfile = new UserProfile();
        BeanUtils.copyProperties(user, userProfile);

        return userProfile;
    }

    /**
     * 鉴定用户登录凭证不验证密码。
     *
     * @param credentials 用户登录凭证
     * @return 用户信息
     */
    @Override
    public UserProfile authenticateNotValidatePassword(CredentialDTO credentials) {

        User user = null;
        String username = credentials.getUsername();

        // 通过手机号码登录
        if (RegExpUtils.isMobileNumber(username)) {
            user = userRepository.findByMobileAndDeletedIsFalse(username);
            // 通过电子邮箱地址登录
        } else if (RegExpUtils.isEmailAddress(username)) {
            username = username.toLowerCase();
            user = userRepository.findByEmailAndDeletedIsFalse(username);
            // 通过登录用户名登录
        } else if (RegExpUtils.isUsername(username)) {
            user = userRepository.findByUsernameAndDeletedIsFalse(username);
            // 通过用户 ID 登录
        } else if (RegExpUtils.isDecID(username)) {
            user = userRepository.findByIdAndDeletedIsFalse(LongUtils.parseLong(username));
        }

        // 检查用户登录凭证
        if (user == null) {
            throw new AuthenticationError();
        }

        // 用户账号关闭时返回访问被拒绝错误
        if (user.getStatus() != EntityStatus.ACTIVE) {
            throw new AccessDeniedError("error.user-account.disabled");
        }

        UserProfile userProfile = new UserProfile();
        BeanUtils.copyProperties(user, userProfile);

        return userProfile;
    }

    /**
     * 查询用户信息。
     *
     * @param criteriaDTO 查询条件
     * @param pagination  分页参数
     * @return 用户分页数据
     */
    @Override
    public Page<UserProfile> search(
        UserCriteriaDTO criteriaDTO,
        PageDTO pagination
    ) {
        return userProfileRepository.search(
            criteriaDTO,
            pagination.toPageable()
        );
    }

    /**
     * 取得用户信息列表。
     *
     * @param entityIDs 用户数据实体 ID 列表
     * @return 用户信息列表
     */
    @Override
    public List<UserBasic> getByEntityIDs(Set<Long> entityIDs) {

        if (CollectionUtils.isEmpty(entityIDs)) {
            return new ArrayList<>();
        }

        return userBasicRepository.findByIdIn(entityIDs);
    }

    /**
     * 根据用户权限取得用户信息列表。
     *
     * @param orgId          组织 ID
     * @param teamPrivileges 工作组-权限映射表
     * @return 用户信息列表
     */
    @Override
    public List<UserBasic> getByPrivileges(Long orgId, Map<Long, Set<String>> teamPrivileges) {
        return roleRepository.findUsersByPrivilege(orgId, teamPrivileges);
    }

    /**
     * 取得用户信息。
     *
     * @param account 用户账号（ID、登录用户名、电子邮箱地址或手机号码）
     * @return 用户信息
     */
    @Override
    public UserProfile get(String account) {

        // 根据用户 ID 取得用户信息
        if (RegExpUtils.isDecID(account)) {
            return userProfileRepository.findById(LongUtils.parseLong(account)).orElse(null);
            // 根据登录用户名取得用户信息
        } else if (RegExpUtils.isUsername(account)) {
            return userProfileRepository
                .findByUsernameAndDeletedIsFalse(account);
            // 根据注册电子邮箱地址取得用户信息
        } else if (RegExpUtils.isEmailAddress(account)) {
            return userProfileRepository
                .findByEmailAndDeletedIsFalse(account);
            // 根据注册手机号码取得用户信息
        } else if (RegExpUtils.isMobileNumber(account)) {
            return userProfileRepository
                .findByMobileAndDeletedIsFalse(account);
        }

        return null;
    }

    /**
     * 取得系统用户 ID。
     *
     * @return 系统用户 ID
     */
    @Override
    public Long getSystemUserId() {

        Long systemUserId = LongUtils.parseLong(getRedisKey(SYSTEM_USER_ID_KEY));

        if (!LongUtils.isEmpty(systemUserId)) {
            return systemUserId;
        }

        UserProfile systemUser = userProfileRepository.findSystemUser();

        if (systemUser == null) {
            return null;
        }

        systemUserId = systemUser.getId();

        setRedisKey(SYSTEM_USER_ID_KEY, systemUserId.toString(), 15);

        return systemUserId;
    }

    /**
     * 更新用户账号状态。
     *
     * @param operatorId 操作者 ID
     * @param userId     操作目标用户 ID
     * @param version    操作目标用户记录版本号
     * @param status     操作目标用户目标状态
     * @return 操作是否成功
     */
    private boolean changeStatus(
        Long operatorId,
        Long userId,
        long version,
        EntityStatus status
    ) {

        User user = userRepository.findById(userId).orElse(null);

        if (user != null && status.equals(user.getStatus())) {
            return true;
        }

        if (user == null || EntityStatus.DELETED.equals(user.getStatus())) {
            return false;
        }

        if (user.getVersion() != version) {
            throw new ConflictError();
        }

        if (EntityStatus.DISABLED.equals(status)) {
            user.setDisabled(true);
        }

        if (EntityStatus.DELETED.equals(status)) {
            user.setDeletedBy(operatorId);
            user.setDeletedAt();
        } else {
            user.setLastModifiedBy(operatorId);
            user.setLastModifiedAt();
        }

        user.setStatus(status);

        userRepository.save(user);

        return true;
    }

    /**
     * 停用用户账号。
     *
     * @param operatorId 操作者 ID
     * @param userId     用户 ID
     * @param version    用户信息版本
     * @return 操作是否成功
     */
    @Override
    public boolean disable(Long operatorId, Long userId, long version) {
        return changeStatus(operatorId, userId, version, EntityStatus.DISABLED);
    }

    /**
     * 启用用户账号。
     *
     * @param operatorId 操作者 ID
     * @param userId     用户 ID
     * @param version    用户信息版本
     * @return 操作是否成功
     */
    @Override
    public boolean enable(Long operatorId, Long userId, long version) {
        return changeStatus(operatorId, userId, version, EntityStatus.ACTIVE);
    }

    /**
     * 删除用户账号。
     *
     * @param operatorId 操作者 ID
     * @param userId     用户 ID
     * @param version    用户信息版本
     * @return 操作是否成功
     */
    @Override
    @Transactional
    public boolean delete(Long operatorId, Long userId, long version) {
        // 退出组织
        userOrganizationRepository.quitOrgs(operatorId, userId);
        // 退出角色
        userRoleRepository.deleteUserRoles(operatorId, userId);

        return changeStatus(operatorId, userId, version, EntityStatus.DELETED);
    }

    /**
     * 设置返回结果的引用数据。
     *
     * @param <T>      数据实体范型
     * @param included 引用数据映射表
     * @param entities 查询结果列表
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(
        Map<Long, Object> included,
        List<T> entities
    ) {

        Set<Long> userIdSet = new HashSet<>();
        BaseVersionedBizEntity versionedBizEntity;

        for (T entity : entities) {

            if (!(entity instanceof BaseVersionedBizEntity)) {
                continue;
            }

            versionedBizEntity = (BaseVersionedBizEntity) entity;

            userIdSet.add(versionedBizEntity.getCreatedBy());
            userIdSet.add(versionedBizEntity.getLastModifiedBy());
            userIdSet.add(versionedBizEntity.getDeletedBy());
        }

        List<UserBasic> users = (List<UserBasic>) userBasicRepository
            .findAllById(new ArrayList<>(userIdSet));

        for (UserBasic userBasic : users) {
            included.put(userBasic.getId(), userBasic);
        }

        return included;
    }

    /**
     * 更新登录密码。
     *
     * @param account  账号
     * @param password 新密码
     */
    @Override
    public void updatePassword(String account, String password) {

        User user = null;

        // 根据 ID 取得用户信息
        if (RegExpUtils.isDecID(account)) {
            user = userRepository.findByIdAndDeletedIsFalse(LongUtils.parseLong(account));
            // 根据登录用户名取得用户信息
        } else if (RegExpUtils.isUsername(account)) {
            user = userRepository.findByUsernameAndDeletedIsFalse(account);
            // 根据注册电子邮箱地址取得用户信息
        } else if (RegExpUtils.isEmailAddress(account)) {
            user = userRepository.findByEmailAndDeletedIsFalse(account);
            // 根据注册手机号码取得电子邮箱地址
        } else if (RegExpUtils.isMobileNumber(account)) {
            user = userRepository.findByMobileAndDeletedIsFalse(account);
        }

        if (user == null) {
            throw new NotFoundError("error.user-account.not-found");
        }

        user.setPassword(encryptPassword(password));

        userRepository.save(user);
    }

    /**
     * 更新用户信息。
     *
     * @param operatorId     操作人ID
     * @param userId         操作用户ID
     * @param userProfileDTO 更新数据
     */
    @Override
    public void updateProfile(Long operatorId, Long userId, UserProfileDTO userProfileDTO) {

        UserProfile userProfile = userProfileRepository.findByIdAndDeletedIsFalse(userId);

        if (userProfile == null) {
            throw new NotFoundError("error.user-account.not-found");
        }

//        if (userProfileDTO.getName() != null) {
//            userProfile.setName(userProfileDTO.getName());
//        }

//        if (userProfileDTO.getFirstNameEn() != null) {
//            userProfile.setFirstNameEn(userProfileDTO.getFirstNameEn());
//        }
//
//        if (userProfileDTO.getLastNameEn() != null) {
//            userProfile.setLastNameEn(userProfileDTO.getLastNameEn());
//        }
//
//        if (userProfileDTO.getFirstNameCn() != null) {
//            userProfile.setFirstNameCn(userProfileDTO.getFirstNameCn());
//        }
//
//        if (userProfileDTO.getLastNameCn() != null) {
//            userProfile.setLastNameCn(userProfileDTO.getLastNameCn());
//        }

        if (userProfileDTO.getNationality() != null) {
            userProfile.setNationality(userProfileDTO.getNationality());
        }

//        String name = "";
//        if (userProfileDTO.getNationality() != null && userProfileDTO.getNationality()) {
//            name = userProfileDTO.getFirstNameEn() + " " + userProfileDTO.getLastNameEn() + " " + userProfileDTO.getLastNameCn() + userProfileDTO.getFirstNameCn();
//        } else {
//            name = userProfileDTO.getFirstNameEn() + " " + userProfileDTO.getLastNameEn();
//            userProfile.setFirstNameCn(null);
//            userProfile.setLastNameCn(null);
//        }
//        if (userProfileDTO.getEmail() == null && userProfileDTO.getName() != null) {
//            name = userProfileDTO.getName();
//        }
        if (userProfileDTO.getName() != null) {
            userProfile.setName(userProfile.getName());
        }

        if (userProfileDTO.getGender() != null) {
            userProfile.setGender(userProfileDTO.getGender());
        }

//        if (userProfileDTO.getCompany() != null) {
//            userProfile.setCompany(userProfileDTO.getCompany());
//        } else {
//            userProfile.setCompany(null);
//        }
//
//        if (userProfileDTO.getDivision() != null) {
//            userProfile.setDivision(userProfileDTO.getDivision());
//        } else {
//            userProfile.setDivision(null);
//        }
//
//        if (userProfileDTO.getDepartment() != null) {
//            userProfile.setDepartment(userProfileDTO.getDepartment());
//        } else {
//            userProfile.setDepartment(null);
//        }
//
//        if (userProfileDTO.getTeam() != null) {
//            userProfile.setTeam(userProfileDTO.getTeam());
//        } else {
//            userProfile.setTeam(null);
//        }

        if (userProfileDTO.getNoApprovalRequired() != null) {
            userProfile.setNoApprovalRequired(userProfileDTO.getNoApprovalRequired());
        }

        if (userProfileDTO.getAutoFillHours() != null) {
            userProfile.setAutoFillHours(userProfileDTO.getAutoFillHours());
        }

        if (userProfileDTO.getOnProject() != null) {
            userProfile.setOnProject(userProfileDTO.getOnProject());
        }

        if (userProfileDTO.getTitle() != null) {
            userProfile.setTitle(userProfileDTO.getTitle());
        } else {
            userProfile.setTitle(null);
        }

        if (userProfileDTO.getDateOfEmployment() != null) {
            userProfile.setDateOfEmployment(userProfileDTO.getDateOfEmployment());
        }

        if (userProfileDTO.getDateOfTermination() != null) {
            userProfile.setDateOfTermination(userProfileDTO.getDateOfTermination());
        }

        if (userProfileDTO.getUsername() != null) {
            if (!userProfileDTO.getUsername().equals(userProfile.getUsername())
                && userProfileRepository.existsByUsernameAndDeletedIsFalse(userProfileDTO.getUsername())) {
                throw new DuplicatedError("error.user-account.duplicate.username");
            }
            userProfile.setUsername(userProfileDTO.getUsername());
        }

        if (userProfileDTO.getMobile() != null) {
            if (!userProfileDTO.getMobile().equals(userProfile.getMobile())
                && userProfileRepository.existsByMobileAndDeletedIsFalse(userProfileDTO.getMobile())) {
                throw new DuplicatedError("error.user-account.duplicate.user-mobile");
            }
            userProfile.setMobile(userProfileDTO.getMobile());
        }

        if (userProfileDTO.getEmail() != null) {
            if (!userProfileDTO.getEmail().equals(userProfile.getEmail())
                && userProfileRepository.existsByEmailAndDeletedIsFalse(userProfileDTO.getEmail())) {
                throw new DuplicatedError("error.user-account.duplicate.user-email");
            }
            userProfile.setEmail(userProfileDTO.getEmail());
        }
        if (userProfileDTO.getType() != null
            && (userProfileDTO.getType().equals("user") || userProfileDTO.getType().equals("administrator"))) {
            userProfile.setType(userProfileDTO.getType());
        }

        if (userProfileDTO.getReviewRole() != null) {
            userProfile.setReviewRole(userProfileDTO.getReviewRole());
        }

//        if (userProfileDTO.getTeamLeader() != null) {
//            userProfile.setTeamLeader(userProfileDTO.getTeamLeader());
//        }
//
//        if (userProfileDTO.getDivisionVP() != null) {
//            userProfile.setDivisionVP(userProfileDTO.getDivisionVP());
//        }
//
//        if (userProfileDTO.getCompanyGM() != null) {
//            userProfile.setCompanyGM(userProfileDTO.getCompanyGM());
//        }

        if (userProfileDTO.getReviewOtherCompany() != null && userProfileDTO.getReviewOtherCompany().size() > 0) {
            userProfile.setReviewOtherCompany(Joiner.on(",").join(userProfileDTO.getReviewOtherCompany()));
        }

        userProfile.setLastModifiedAt(new Date());
        userProfile.setLastModifiedBy(operatorId);

        userProfileRepository.save(userProfile);
    }

    /**
     * 获取所有组织成员列表。
     *
     * @param organizationId    组织ID
     * @param queryOrgMemberDTO 查询参数
     * @return 组织成员列表
     */
    @Override
    public Page<UserProfile> getOrgMembers(Long organizationId, QueryOrgMemberDTO queryOrgMemberDTO, PageDTO pageDTO) {

        Organization organization = organizationRepository.findByIdAndDeletedIsFalse(organizationId);
        if (organization == null) {
            throw new NotFoundError("organization is not found");
        }

        // 获取成员列表
        Page<UserProfile> members;
        if (!queryOrgMemberDTO.getCosign()) {
            if (queryOrgMemberDTO.getKeyword() == null) {
                members = userProfileRepository
                    .findOrgMembers(organizationId, pageDTO.toPageable());
            } else {
                members = userProfileRepository
                    .findOrgMembersByKeyword(
                        organizationId,
                        "%" + queryOrgMemberDTO.getKeyword() + "%",
                        pageDTO.toPageable()
                    );
            }
        } else {
            if (queryOrgMemberDTO.getKeyword() == null) {
                members = userProfileRepository
                    .findOrgCosignMembers(organizationId, pageDTO.toPageable());
            } else {
                members = userProfileRepository
                    .findOrgCosignMembersByKeyword(
                        organizationId,
                        "%" + queryOrgMemberDTO.getKeyword() + "%",
                        pageDTO.toPageable()
                    );
            }
        }

        // 没有数据直接返回。非项目组织或项目组织下的部门，不返回角色信息
        if (members.getContent().size() == 0) {
            return members;
        }

        // 用户ID列表
        List<Long> userIds = new ArrayList<>();
        for (UserProfile user : members.getContent()) {
            userIds.add(user.getId());
        }

        List<Long> allOrganizationIds = new ArrayList<>();

        // 全部上级组织ID
//        List<Long> organizationIds = new ArrayList<>();
//        List<Organization> allOrganization = organizationRepository.findByDeletedIsFalse();
//        for (Organization newOrganization : allOrganization) {
//            if (newOrganization.getPath() != null) {
//                if (newOrganization.getPath().startsWith("/") && newOrganization.getPath().endsWith("/") && newOrganization.getPath().length() > 1) {
//                    organizationIds = LongUtils.change2Str(
//                        newOrganization.getPath().substring(1, newOrganization.getPath().length() - 1).split("/"));
//                } else {
//                    continue;
//                }
//                organizationIds.add(newOrganization.getId());
//            } else {
//                organizationIds.add(newOrganization.getId());
//            }
//            // 将当前组织下的子组织全部加入
//            for (Long list : organizationIds) {
//                if (list.equals(organizationId)) {
//                    allOrganizationIds.addAll(organizationIds);
//                }
//            }
//        }
//
//        // 去重
//        List<Long> newAllOrganizationIds = allOrganizationIds.stream().distinct().collect(Collectors.toList());

        // 获取用户角色信息
        List<UserRole> userRoles = userRoleRepository.findOrgMembersRoles(organizationId, userIds);
//        List<UserRole> userRoles = userRoleRepository.findOrgMembersRoles(newAllOrganizationIds, userIds);

        Map<Long, List<Long>> userRoleMap = new HashMap<>();
        for (UserRole userRole : userRoles) {
            if (userRoleMap.get(userRole.getUserId()) == null) {
                List<Long> roleIDs = new ArrayList<>();
                roleIDs.add(userRole.getRoleId());
                userRoleMap.put(userRole.getUserId(), roleIDs);
            } else {
                userRoleMap.get(userRole.getUserId()).add(userRole.getRoleId());
            }
        }

        for (UserProfile member : members.getContent()) {
            if (userRoleMap.get(member.getId()) != null) {
                member.setRoleIds(userRoleMap.get(member.getId()));
            } else {
                member.setRoleIds(new ArrayList<>());
            }
        }

        return members;
    }

    /**
     * 获取单个组织成员列表。
     *
     * @param organizationId    组织ID
     * @param queryOrgMemberDTO 查询参数
     * @return 组织成员列表
     */
    @Override
    public Page<UserProfile> getSingleOrgMembers(Long organizationId, QueryOrgMemberDTO queryOrgMemberDTO, PageDTO pageDTO) {

        Organization organization = organizationRepository.findByIdAndDeletedIsFalse(organizationId);
        if (organization == null) {
            throw new NotFoundError("organization is not found");
        }

        // 获取成员列表
        Page<UserProfile> members;
        if (queryOrgMemberDTO.getKeyword() == null) {
            members = userProfileRepository
                .findOrgMembers(organizationId, pageDTO.toPageable());
        } else {
            members = userProfileRepository
                .findOrgMembersByKeyword(
                    organizationId,
                    "%" + queryOrgMemberDTO.getKeyword() + "%",
                    pageDTO.toPageable()
                );
        }

        // 没有数据直接返回。非项目组织或项目组织下的部门，不返回角色信息
        if (members.getContent().isEmpty()) {
            return members;
        }

        // 用户ID列表
        List<Long> userIds = new ArrayList<>();
        for (UserProfile user : members.getContent()) {
            userIds.add(user.getId());
        }

        // 获取用户角色信息
        List<UserRole> userRoles = userRoleRepository.findOrgMembersRoles(organizationId, userIds);

        Map<Long, List<Long>> userRoleMap = new HashMap<>();
        for (UserRole userRole : userRoles) {
            if (userRoleMap.get(userRole.getUserId()) == null) {
                List<Long> roleIDs = new ArrayList<>();
                roleIDs.add(userRole.getRoleId());
                userRoleMap.put(userRole.getUserId(), roleIDs);
            } else {
                userRoleMap.get(userRole.getUserId()).add(userRole.getRoleId());
            }
        }

        // 获取用户岗位信息
        List<OrganizationUserPosition> userPositions = userRoleRepository.findOrgMembersPositions(organizationId, userIds);

        Map<Long, List<Integer>> userPositionMap = new HashMap<>();
        for (OrganizationUserPosition userPosition : userPositions) {
            if (userPositionMap.get(userPosition.getUserId()) == null) {
                List<Integer> positionIDs = new ArrayList<>();
                positionIDs.add(userPosition.getPositionId());
                userPositionMap.put(userPosition.getUserId(), positionIDs);
            } else {
                userPositionMap.get(userPosition.getUserId()).add(userPosition.getPositionId());
            }
        }

        for (UserProfile member : members.getContent()) {
            if (userRoleMap.get(member.getId()) != null) {
                member.setRoleIds(userRoleMap.get(member.getId()));
            } else {
                member.setRoleIds(new ArrayList<>());
            }
            if (userPositionMap.get(member.getId()) != null) {
                member.setRoleIds(userRoleMap.get(member.getId()));
            } else {
                member.setRoleIds(new ArrayList<>());
            }
            // 查询对应的组织人员关系，看当前人是否为VP
            UserOrganization userOrganization = userOrganizationRepository.findByOrganizationIdAndUserIdAndDeletedIsFalse(organizationId, member.getId());
            if (userOrganization.getApplyRole() != null && userOrganization.getApplyRole()) {
                member.setApplyRole(true);
                userProfileRepository.save(member);
            } else {
                member.setApplyRole(false);
                userProfileRepository.save(member);
            }

            // 查询对应的组织人员关系，看当前人是否为IDC负责人
            if (userOrganization.getIdc() != null && userOrganization.getIdc()) {
                member.setIdc(true);
                userProfileRepository.save(member);
            } else {
                member.setIdc(false);
                userProfileRepository.save(member);
            }
        }

        return members;
    }

    /**
     * 获取角色成员列表。
     *
     * @param roleId             角色ID
     * @param queryRoleMemberDTO 查询条件
     * @return 用户列表
     */
    @Override
    public Page<UserProfile> getRoleMembers(Long roleId, QueryRoleMemberDTO queryRoleMemberDTO) {

        Role role = roleRepository.findByIdAndDeletedIsFalse(roleId);

        if (role == null) {
            throw new NotFoundError("role is not found");
        }

        Page<UserProfile> members;

        if (queryRoleMemberDTO.getKeyword() == null) {
            members = userProfileRepository.findRoleMembers(roleId, queryRoleMemberDTO.toPageable());
        } else {
            members = userProfileRepository.findRoleMembersByKeyword(
                roleId,
                queryRoleMemberDTO.getKeyword(),
                queryRoleMemberDTO.toPageable()
            );
        }

        return members;
    }

    /**
     * 根据用户名获取用户信息。
     *
     * @param userNames 用户名称
     * @return 用户信息
     */
    @Override
    public List<UserProfile> getByUsername(List<String> userNames) {

        if (userNames == null || userNames.size() == 0) {
            return new ArrayList<>();
        }

        return userProfileRepository.findByUsernameInAndDeletedIsFalse(userNames);
    }

    /**
     * 根据用户名获取用户信息。
     *
     * @param userName 用户名称
     * @return 用户信息
     */
    @Override
    public UserProfile getByUsername(String userName) {

        if (userName == null || userName.equalsIgnoreCase("")) {
            return new UserProfile();
        }

        return userProfileRepository.findByUsernameAndDeletedIsFalse(userName);
    }

    /**
     * 根据用户名获取用户信息。
     *
     * @param username 用户名称
     * @return 用户信息
     */
    @Override
    public List<UserProfile> getByUsername(Long orgId, String username) {

        if (username == null || username.equals("") || username.length() < 3) {
            return new ArrayList<>();
        }

        return userProfileRepository.findByOrgIdAndUsername(orgId, "%" + username + "%");
    }

    @Override
    public List<UserProfile> getByOrgId(Long orgId) {
        return userProfileRepository.findByOrgId(orgId);
    }

    /**
     * 根据用户名获取用户信息。
     *
     * @param name 用户名称
     * @return 用户信息
     */
    @Override
    public List<UserProfile> getByName(Long orgId, String name) {

        if (name == null || name.equals("") || name.length() < 2) {
            return new ArrayList<>();
        }

        return userProfileRepository.findByOrgIdAndName(orgId, "%" + name + "%");
    }

    public AuthCheckDTO getOrganizationByUserId(Long userId) {
        List<UserOrganization> organizations = userOrganizationRepository.findByUserIdAndDeletedIsFalse(userId);
        String s = StringUtils.toJSON(organizations);
        AuthCheckDTO result = new AuthCheckDTO();
        result.setUserAgent(s);
        return result;
    }

    /**
     * 查询用户信息。
     *
     * @return 用户分页数据
     */
    @Override
    public List<UserProfile> searchList() {
        return userProfileRepository.findByDeletedIsFalse();
    }

    @Override
    public File saveDownloadFile(UserCriteriaDTO criteriaDTO, Long operatorId) {
        String templateFilePath = "/var/www/saint-whale/backend/resources/templates/export-users.xlsx";

        String temporaryFileName = System.currentTimeMillis() + ".xlsx";
        String filePath = "/var/www/saint-whale/backend/private/upload/" + temporaryFileName;

        File excel = new File(filePath);

        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<UserProfile> userProfiles = search(
            criteriaDTO,
            pageDTO).getContent();
        EasyExcel.write(filePath, UserProfile.class).withTemplate(templateFilePath).sheet("user").doFill(userProfiles);
        return excel;
    }

    /**
     * 查询用户所在项目信息。
     *
     * @param userId
     * @param criteriaDTO 查询条件
     * @return 用户分页数据
     */
    @Override
    public Page<UserProjectDTO> searchProjects(
        Long userId,
        UserProjectSearchDTO criteriaDTO
    ) {
        return userProfileRepository.searchProject(userId, criteriaDTO);
    }

    @Override
    public List<UserProfile> getProjectOrgUsersByUserId(Long userId, String orgType) {

//        List<Organization> organizations = organizationRepository.findByUserIdAndIsApployRole(userId);
//        List<Long> ordIds = organizations.stream().map(Organization::getId).collect(Collectors.toList());

        List<UserOrganization> userOrganizations = userOrganizationRepository.findByUserIdAndOrganizationTypeAndApplyRoleIsTrueAndDeletedIsFalse(userId, OrgType.valueOf(orgType));
        List<Long> ordIds = userOrganizations.stream().map(UserOrganization::getOrganizationId).collect(Collectors.toList());

        if (!ordIds.isEmpty()){
            return userProfileRepository.findMultipleOrgMembers(ordIds);
        }else{
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(encryptPassword("admin"));
    }
}
