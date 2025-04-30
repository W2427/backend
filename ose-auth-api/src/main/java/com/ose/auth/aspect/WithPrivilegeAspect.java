package com.ose.auth.aspect;

import com.ose.aspect.BaseAspect;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.auth.api.PrivilegeFeignAPI;
import com.ose.auth.dto.PrivilegeCheckDTO;
import com.ose.dto.ContextDTO;
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
public class WithPrivilegeAspect extends BaseAspect {

    // 权限检查接口
    private PrivilegeFeignAPI privilegeAPI;

    private final static Logger logger = LoggerFactory.getLogger(WithPrivilegeAspect.class);


    /**
     * 构造方法。
     */
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public WithPrivilegeAspect(PrivilegeFeignAPI privilegeAPI) {
        this.privilegeAPI = privilegeAPI;
    }

    /**
     * 定义切入点：使用 @WithPrivilege 注解的方法。
     */
    @Pointcut("@annotation(annotation)")
    public void controller(WithPrivilege annotation) {
    }

    /**
     * 检查用户权限。
     *
     * @param point      切入点信息
     * @param annotation 注解设置
     */
    @Before(value = "controller(annotation)", argNames = "point,annotation")
    public void doBefore(JoinPoint point, WithPrivilege annotation) {

        // 取得上下文对象
        ContextDTO context = getContext();

        // 若未设置 Authorization 请求头且授权信息必须则返回未授权错误
        if (context == null || context.getAuthorization() == null) {

            if (!annotation.required()) {
                return;
            }
            logger.error("NOT AUTHORIZED");
            throw new UnauthorizedError();
        }

        AspectUtils.ParameterHolder parameters = AspectUtils
            .getParameters(point, annotation.orgId(), annotation.resourceId());

        Long orgId = LongUtils.parseLong(parameters.getAsString(annotation.orgId()));
        Long resourceId = LongUtils.parseLong(parameters.getAsString(annotation.resourceId()));

        // 根据权限配置检查登录用户权限，并在检查成功时将用户信息设置到上下文对象中
        context.setOperator(
            privilegeAPI
                .check(new PrivilegeCheckDTO(annotation, orgId, resourceId))
                .getData()
        );

    }

}
