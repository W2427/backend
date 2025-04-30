package com.ose.aspect;

import com.ose.annotation.InternalAccessOnly;
import com.ose.exception.AccessDeniedError;
import com.ose.util.RemoteAddressUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 客户端来源限制切面。
 */
@Aspect
@Component
public class InternalAccessOnlyAspect extends BaseAspect {

    /**
     * 定义切入点：使用 @InternalAccessOnly 注解的方法。
     */
    @Pointcut("@annotation(annotation)")
    public void controller(InternalAccessOnly annotation) {
    }

    /**
     * 检查客户端 ID 地址。
     *
     * @param point      切入点信息
     * @param annotation 注解设置
     */
    @Before(value = "controller(annotation)", argNames = "point,annotation")
    public void doBefore(JoinPoint point, InternalAccessOnly annotation) {

        HttpServletRequest request = getRequest();

        if (request == null
            || RemoteAddressUtils.isPrivate(request.getRemoteAddr())
            ) {
            return;
        }

        // TODO 白名单

        throw new AccessDeniedError();
    }

}
