package com.ose.auth.aspect;

import com.ose.aspect.BaseAspect;
import com.ose.auth.annotation.SetOrgInfo;
import com.ose.auth.api.OrganizationFeignAPI;
import com.ose.auth.dto.BatchGetDTO;
import com.ose.auth.dto.OrganizationBasicDTO;
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
 * 设置关联组织信息。
 */
@Aspect
@Component
public class SetOrgInfoAspect extends BaseAspect {

    // 权限检查接口
    private OrganizationFeignAPI organizationFeignAPI;

    /**
     * 构造方法。
     */
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SetOrgInfoAspect(OrganizationFeignAPI organizationFeignAPI) {
        this.organizationFeignAPI = organizationFeignAPI;
    }

    /**
     * 定义切入点：使用 @SetOrgInfo 注解的方法。
     */
    @Pointcut("@annotation(annotation)")
    public void controller(SetOrgInfo annotation) {
    }

    /**
     * 设置组织信息。
     *
     * @param point      切入点信息
     * @param annotation 注解设置
     */
    @Around(value = "controller(annotation)", argNames = "point,annotation")
    public Object doAfterReturning(
        ProceedingJoinPoint point,
        SetOrgInfo annotation
    ) throws Throwable {

        Object result = point.proceed();

        if (!(result instanceof JsonDataResponseBody)) {
            return result;
        }

        Set<Long> orgIDs = new HashSet<>();
        BaseDTO entity;

        if (result instanceof JsonListResponseBody) {

            JsonListResponseBody responseBody = (JsonListResponseBody) result;
            List list = responseBody.getData();

            for (Object item : list) {
                if (item instanceof BaseDTO) {
                    entity = (BaseDTO) item;
                    orgIDs.addAll(entity.relatedOrgIDs());
                }
            }

        } else if (result instanceof JsonObjectResponseBody) {

            JsonObjectResponseBody responseBody = (JsonObjectResponseBody) result;

            if (responseBody.getData() != null) {
                entity = responseBody.getData();
                orgIDs.addAll(entity.relatedOrgIDs());
            }

        }

        if (orgIDs.size() > 0) {

            JsonListResponseBody<OrganizationBasicDTO> responseBody
                = organizationFeignAPI.batchGet(new BatchGetDTO(orgIDs));

            Map<Long, Object> orgs = new HashMap<>();

            for (OrganizationBasicDTO org : responseBody.getData()) {
                orgs.put(org.getId(), org);
            }

            ((JsonDataResponseBody) result).addIncluded(orgs);
        }

        return result;
    }

}
