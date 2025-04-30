package com.ose.feign;

import com.ose.constant.HttpRequestAttributes;
import com.ose.util.RemoteAddressUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Feign HTTP 请求拦截器。
 */
@Component
public class RequestHeadersInterceptor implements RequestInterceptor {

    /**
     * 发送请求前设置 HTTP 请求头。
     * 将客户端提交的请求的部分头信息设置到新的请求的头中。
     *
     * @param template 请求模板对象
     */
    @Override
    public void apply(RequestTemplate template) {

        ServletRequestAttributes attributes = (ServletRequestAttributes)
            RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return;
        }

        Collection<String> contentTypes = template.headers().get(CONTENT_TYPE);
        String contentType = ALL_VALUE;

        if (contentTypes != null && contentTypes.size() > 0) {
            contentType = contentTypes.iterator().next();
        }

        if (!contentType.matches("^multipart/form-data;.+$")) {
            template.header(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE);
        }

        //add wildcard * log ftjftj
//        if(contentType != null && contentType.contains("*")) {
//            System.out.println(contentType + "  ftjftj ");
//        }

        template.header(ACCEPT, APPLICATION_JSON_VALUE);

        HttpServletRequest request = attributes.getRequest();

        template
            .header(ORIGIN, request.getHeader(ORIGIN))
            .header(USER_AGENT, request.getHeader(USER_AGENT))
            .header(
                AUTHORIZATION,
                request.getAttribute(AUTHORIZATION) != null
                    ? ("" + request.getAttribute(AUTHORIZATION))
                    : request.getHeader(AUTHORIZATION)
            )
            .header(HttpRequestAttributes.REAL_IP, RemoteAddressUtils.getRemoteAddr(request))
            .header(HttpRequestAttributes.FORWARDED_FOR, request.getHeader(HttpRequestAttributes.FORWARDED_FOR))
            .header(ACCEPT_LANGUAGE, request.getHeader(ACCEPT_LANGUAGE))
            .header(HttpRequestAttributes.FEIGN_CLIENT, "TRUE");
    }

}
