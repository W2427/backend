package com.ose.auth.aspect;

import com.ose.aspect.BaseAspect;
import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.dto.BatchGetDTO;
import com.ose.auth.entity.UserBasic;
import com.ose.dto.BaseDTO;
import com.ose.response.JsonDataResponseBody;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 设置关联用户信息。
 */
@Aspect
@Component
public class SetUserInfoAspect extends BaseAspect {

    // 权限检查接口
    private UserFeignAPI userFeignAPI;

    /**
     * 构造方法。
     */
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SetUserInfoAspect(UserFeignAPI userFeignAPI) {
        this.userFeignAPI = userFeignAPI;
    }

    /**
     * 定义切入点：使用 @SetUserInfo 注解的方法。
     */
    @Pointcut("@annotation(annotation)")
    public void controller(SetUserInfo annotation) {
    }

    /**
     * 设置用户信息。
     *
     * @param point      切入点信息
     * @param annotation 注解设置
     */
    @Around(value = "controller(annotation)", argNames = "point,annotation")
    public Object doAfterReturning(
        ProceedingJoinPoint point,
        SetUserInfo annotation
    ) throws Throwable {

        Object result = point.proceed();

        if (!(result instanceof JsonDataResponseBody)) {
            return result;
        }

        Set<Long> userIDs = new HashSet<>();
        BaseDTO entity;

        if (result instanceof JsonListResponseBody) {

            JsonListResponseBody responseBody = (JsonListResponseBody) result;
            List list = responseBody.getData();

            for (Object item : list) {
                if (item instanceof BaseDTO) {
                    entity = (BaseDTO) item;
                    userIDs.addAll(entity.relatedUserIDs());
                }
            }

        } else if (result instanceof JsonObjectResponseBody) {

            JsonObjectResponseBody responseBody = (JsonObjectResponseBody) result;

            if (responseBody.getData() != null) {
                entity = responseBody.getData();
                userIDs.addAll(entity.relatedUserIDs());
            }

        }

        if (userIDs.size() > 0) {

            JsonListResponseBody<UserBasic> responseBody
                = userFeignAPI.batchGet(new BatchGetDTO(userIDs));

            Map<Long, Object> users = new HashMap<>();

            for (UserBasic user : responseBody.getData()) {
                users.put(user.getId(), user);
            }

            ((JsonDataResponseBody) result).addIncluded(users);
        }

        return result;
    }

}
