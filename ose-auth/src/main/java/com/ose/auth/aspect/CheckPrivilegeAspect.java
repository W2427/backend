package com.ose.auth.aspect;

import com.ose.aspect.BaseAspect;
import com.ose.auth.annotation.CheckPrivilege;
import com.ose.auth.domain.model.service.AccessTokenInterface;
import com.ose.auth.domain.model.service.PrivilegeInterface;
import com.ose.auth.domain.model.service.UserAgentInterface;
import com.ose.auth.dto.PrivilegeCheckDTO;
import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.ContextDTO;
import com.ose.exception.AccessTokenInvalidError;
import com.ose.exception.NoPrivilegeError;
import com.ose.exception.UnauthorizedError;
import com.ose.util.AspectUtils;
import com.ose.util.LongUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户权限检查切面。
 */
@Aspect
@Component
public class CheckPrivilegeAspect extends BaseAspect {

    // 用户代理字符串服务
    private UserAgentInterface userAgentService;

    // 用户访问令牌服务
    private AccessTokenInterface accessTokenService;

    // 权限服务
    private PrivilegeInterface privilegeService;

    private final static Logger logger = LoggerFactory.getLogger(CheckPrivilegeAspect.class);

    /**
     * 构造方法。
     */
    @Autowired
    public CheckPrivilegeAspect(
        UserAgentInterface userAgentService,
        AccessTokenInterface accessTokenService,
        PrivilegeInterface privilegeService
    ) {
        this.userAgentService = userAgentService;
        this.accessTokenService = accessTokenService;
        this.privilegeService = privilegeService;
    }

    /**
     * 定义切入点：使用 @CheckPrivilege 注解的方法。
     */
    @Pointcut("@annotation(annotation)")
    public void controller(CheckPrivilege annotation) {
    }

    /**
     * 检查 JWT 的有效性。
     * 方法的 JWT 应通过 @RequestHeader String authorization 定义。
     * @param point 切入点信息
     * @param annotation 注解设置
     */
    @Before(value = "controller(annotation)", argNames = "point,annotation")
    public void doBefore(JoinPoint point, CheckPrivilege annotation) {

        ContextDTO context = getContext();

        // 若未设置 Authorization 请求头且授权信息必须则返回未授权错误
        if (context == null || context.getAuthorization() == null) {

            if (!annotation.required()) {
                return;
            }
            logger.error("Check Privilege no auth");
            throw new UnauthorizedError();
        }

        final String userAgent = context.getUserAgent();
        final String authorizationType = annotation.type() + " ";
        final String authorization = context.getAuthorization();

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
        context.setOperator(accessTokenService.claim(
            context.getRemoteAddr(),
            userAgent,
            userAgentId,
            accessToken
        ));

        AspectUtils.ParameterHolder parameters = AspectUtils
            .getParameters(point, annotation.orgId(), annotation.resourceId());

        Long orgId = LongUtils.parseLong(parameters.getAsString(annotation.orgId()));
        String resourceId = parameters.getAsString(annotation.resourceId());

        PrivilegeCheckDTO privilegeCheckDTO = new PrivilegeCheckDTO();
        privilegeCheckDTO.setOrgId(orgId);
        privilegeCheckDTO.setRequired(annotation.required());
        privilegeCheckDTO.setPrivileges(annotation.privilege());
        privilegeCheckDTO.setResourceType(annotation.resourceType());
        privilegeCheckDTO.setResourceId(LongUtils.parseLong(resourceId));

        if (!privilegeService.hasPrivilege(context.getOperator(), privilegeCheckDTO)) {
            logger.error("HAS NOT PRIVILEGE");
            throw new NoPrivilegeError(UserPrivilege.toNameSet(privilegeCheckDTO.getPrivileges()));
        }

    }

}
