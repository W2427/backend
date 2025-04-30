package com.ose.auth.aspect;

import com.ose.annotation.WithSuperPrivilege;
import com.ose.aspect.BaseAspect;
import com.ose.auth.api.PrivilegeFeignAPI;
import com.ose.auth.dto.PrivilegeCheckDTO;
import com.ose.dto.ContextDTO;
import com.ose.exception.NoPrivilegeError;
import com.ose.exception.UnauthorizedError;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户权限检查切面。
 */
@Aspect
@Component
public class WithSuperPrivilegeAspect extends BaseAspect {

    // 权限检查接口
    private PrivilegeFeignAPI privilegeAPI;

    /**
     * 构造方法。
     */
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public WithSuperPrivilegeAspect(PrivilegeFeignAPI privilegeAPI) {
        this.privilegeAPI = privilegeAPI;
    }

    /**
     * 定义切入点：使用 @WithSuperPrivilege 注解的方法。
     */
    @Pointcut("@annotation(annotation)")
    public void controller(WithSuperPrivilege annotation) {
    }

    /**
     * 检查用户权限。
     *
     * @param annotation 注解设置
     */
    @Before(value = "controller(annotation)", argNames = "annotation")
    public void doBefore(WithSuperPrivilege annotation) {

        ContextDTO context = getContext();

        if (context == null || context.getAuthorization() == null) {
            throw new UnauthorizedError();
        }

        PrivilegeCheckDTO dto = new PrivilegeCheckDTO();
        dto.setRequired(true);
        dto.setType("Bearer");

        if (!"system".equals(privilegeAPI.check(dto).getData().getType())) {
            throw new NoPrivilegeError();
        }

    }

}
