package com.ose.auth.domain.model.service;

import com.ose.auth.domain.model.repository.UserRoleRepository;
import com.ose.auth.dto.AuthCheckDTO;
import com.ose.auth.dto.PrivilegeCheckDTO;
import com.ose.auth.dto.UserPrivilegeDTO;
import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.OperatorDTO;
import com.ose.exception.AccessTokenInvalidError;
import com.ose.exception.UnauthorizedError;
import com.ose.service.StringRedisService;
import com.ose.util.CryptoUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 权限服务。
 */
@Component
public class PrivilegeService extends StringRedisService implements PrivilegeInterface {

    // 用户权限数据仓库
    private final UserRoleRepository userRoleRepository;

    private final UserAgentInterface userAgentService;

    private final AccessTokenInterface accessTokenService;

    // 缓存的权限检查结果的值
    private static final int USER_PRIVILEGE_CACHE_TTL = 30;
    private static final String HAS_PRIVILEGE = "T";
    private static final String NO_PRIVILEGE = "F";

    /**
     * 生成权限检查结果 Redis Key。
     *
     * @param userId             用户 ID
     * @param orgId              组织 ID
     * @param expectedPrivileges 权限集合
     * @return 权限检查结果 Redis Key
     */
    private static String generateUserPrivilegeKey(
        final Long userId,
        final Long orgId,
        final Set<String> expectedPrivileges
    ) {
        List<String> privileges = new ArrayList<>(expectedPrivileges);
        privileges.sort(String::compareTo);
        return String.format(
            "USER:%s:ORG:%s:PRIVILEGES:%s",
            userId, orgId, CryptoUtils.md5(String.join(";", privileges)).toUpperCase()
        );
    }

    /**
     * 构造方法。
     */
    @Autowired
    public PrivilegeService(
        StringRedisTemplate stringRedisTemplate,
        UserRoleRepository userRoleRepository,
        UserAgentInterface userAgentService, AccessTokenInterface accessTokenService) {
        super(stringRedisTemplate);
        this.userRoleRepository = userRoleRepository;
        this.userAgentService = userAgentService;
        this.accessTokenService = accessTokenService;
    }

    /**
     * 检查用户是否拥有指定的权限。
     *
     * @param operator          操作者信息
     * @param privilegeCheckDTO 权限检查数据传输对象
     * @return 用户是否拥有指定的权限
     */
    @Override
    public boolean hasPrivilege(
        OperatorDTO operator,
        PrivilegeCheckDTO privilegeCheckDTO
    ) {
        // 系统用户拥有所有权限
        if (operator != null && "system".equals(operator.getType())) {
            return true;
        }

        final boolean isPrivilegeRequired = privilegeCheckDTO.isRequired() || privilegeCheckDTO.isPrivilegeRequired();

        // 若用户必须拥有指定的权限则用户必须先完成认证
        if (isPrivilegeRequired && operator == null) {
            throw new UnauthorizedError();
        }

        final Long orgId = privilegeCheckDTO.getOrgId();
        final Set<String> expectedPrivileges = privilegeCheckDTO.getPrivilegeCodes();
        expectedPrivileges.remove(UserPrivilege.NONE.toString());

        // 若无组织信息或权限非必要则结束
        if (orgId == null || !isPrivilegeRequired || expectedPrivileges.size() == 0) {
            return true;
        }

        final Long userId = operator.getId();
        final String userPrivilegeRedisKey = generateUserPrivilegeKey(userId, orgId, expectedPrivileges);
        final String cachedResult = getRedisKey(userPrivilegeRedisKey, USER_PRIVILEGE_CACHE_TTL);

        // 若存在缓存数据则使用缓存数据判断用户权限
        if (!StringUtils.isEmpty(cachedResult)) {
            return HAS_PRIVILEGE.equals(cachedResult);
        }

        final Map<Long, Set<String>> privilegeMap = getUserPrivileges(userId, orgId);
        final Set<String> actualPrivileges = privilegeMap.get(orgId);

        // 若用户拥有所有需要的权限则通过检查
        if (actualPrivileges != null
            && (actualPrivileges.contains(UserPrivilegeDTO.ALL_PRIVILEGE_VALUE)
            || actualPrivileges.containsAll(expectedPrivileges))) {
            setRedisKey(userPrivilegeRedisKey, HAS_PRIVILEGE, USER_PRIVILEGE_CACHE_TTL);
            return true;
        }

        setRedisKey(userPrivilegeRedisKey, NO_PRIVILEGE, USER_PRIVILEGE_CACHE_TTL);
        return false;
    }

    /**
     * 取得当前用户在指定组织所属顶级组织及所属顶级组织的所有下级组织所用有的所有权限，并根据作用域（组织）分组。
     *
     * @param operator 用户信息
     * @param orgId    组织 ID
     * @return 用户权限列表
     */
    @Override
    public List<UserPrivilegeDTO> getUserPrivileges(
        final OperatorDTO operator,
        final Long orgId
    ) {
        final List<UserPrivilegeDTO> result = new ArrayList<>();

        getUserPrivileges(operator.getId(), orgId)
            .forEach((coveredOrgId, privileges) ->
                result.add(new UserPrivilegeDTO(coveredOrgId, privileges))
            );

        return result;
    }

    /**
     * 取得用户的组织-权限映射表。
     *
     * @param userId 用户 ID
     * @param orgId  组织 ID
     * @return 组织-权限映射表
     */
    private Map<Long, Set<String>> getUserPrivileges(
        final Long userId,
        final Long orgId
    ) {
        final Map<Long, Set<String>> privilegeMap = new HashMap<>();

        // 取得当前用户在指定组织所属顶级组织及所属顶级组织的所有下级组织所用有的所有权限
        userRoleRepository
            .findByUserIdAndOrgId(userId, orgId)
            .forEach(columns -> {

                // 设置当前组织的组织路径
                Long[] orgIDs = LongUtils.change2LongArr(((String) columns[0]).split("/"));

                // 设置当前组织的权限代码的集合
                Set<String> privileges = columns[1] == null
                    ? new HashSet<>()
                    : new HashSet<>(Arrays.asList(((String) columns[1]).split(",")));

                // 遍历每一个上级组织，继承上级组织的权限
                for (Long childOrgId : orgIDs) {

                    if (LongUtils.isEmpty(childOrgId)) {
                        continue;
                    }

                    // 将上级组织的权限添加到当前组织的权限集合中
                    privileges.addAll(privilegeMap.computeIfAbsent(childOrgId, k -> new HashSet<>()));

                    // 若权限集合中已包含【全部】权限则结束
                    if (privileges.contains(UserPrivilegeDTO.ALL_PRIVILEGE_VALUE)) {
                        break;
                    }
                }

                privileges.remove("");

                // 若权限集合中包含【全部】权限则仅保留【全部】权限
                if (privileges.contains(UserPrivilegeDTO.ALL_PRIVILEGE_VALUE)) {
                    privileges.clear();
                    privileges.add(UserPrivilegeDTO.ALL_PRIVILEGE_VALUE);
                }

                privilegeMap.put(orgIDs[orgIDs.length - 1], privileges);
            });

        return privilegeMap;
    }

    /**
     * 取得当前用户在指定组织及其所有下级组织所用有的所有权限。
     *
     * @param operator 用户信息
     * @param orgId    组织 ID
     * @return 用户权限列表
     */
    @Override
    public UserPrivilegeDTO getUserAvailablePrivileges(
        final OperatorDTO operator,
        final Long orgId
    ) {
        String privileges = "";
        if (orgId == 1533021345379520082L) {
            privileges = userRoleRepository
                .findGlobalAvailableByUserIdAndOrgId(operator.getId(), orgId);
        } else {
            privileges = userRoleRepository
                .findAvailableByUserIdAndOrgId(operator.getId(), orgId);
        }

        UserPrivilegeDTO result = new UserPrivilegeDTO();
        result.setOrgId(orgId);

        if (StringUtils.isEmpty(privileges)) {
            return result;
        }

        result.setPrivileges(privileges);
        return result;
    }

    @Override
    public boolean hasAuth(AuthCheckDTO authCheckDTO) {
        final String userAgent = authCheckDTO.getUserAgent();
        final String authorizationType = "Bearer ";
        final String authorization = authCheckDTO.getAuthorization();
        final String remoteAddr = authCheckDTO.getRemoteAddr();

        // 授权类型不匹配时返回授权信息无效错误
        if (!authorization.startsWith(authorizationType)) {
            throw new AccessTokenInvalidError();
        }

        // 取得用户代理 ID
        final Long userAgentId = userAgentService.fetchId(userAgent);

        // 取得访问令牌
        final String accessToken
            = authorization.substring(authorizationType.length());

        // 校验用户访问令牌
        accessTokenService.claim(
            remoteAddr,
            userAgent,
            userAgentId,
            accessToken
        );

        return true;

    }
}
