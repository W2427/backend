package com.ose.auth.annotation;

import com.ose.auth.vo.UserPrivilege;
import com.ose.auth.vo.ResourceType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * REST 控制器权限检查注解。
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPrivilege {

    @AliasFor("required")
    boolean value() default true;

    // 客户端是否必须设置授权信息（Authorization 请求头）
    @AliasFor("value")
    boolean required() default true;

    // 授权信息类型（Authorization 请求头的 type 部分）
    String type() default "Bearer";

    // 组织 ID 在 REST 路径参数中的参数名
    String orgId() default "orgId";

    // 资源类型
    ResourceType resourceType() default ResourceType.NONE;

    // 资源 ID 在 REST 路径参数中的参数名
    String resourceId() default "";

    // 所需权限
    UserPrivilege[] privilege() default UserPrivilege.NONE;

}
