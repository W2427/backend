package com.ose.auth.aspect;

import com.ose.aspect.BaseAspect;
import com.ose.dto.ContextDTO;
import com.ose.exception.TooManyRequestsError;
import com.ose.util.CryptoUtils;
import com.ose.util.RemoteAddressUtils;
import com.ose.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

/**
 * 重复请求提交检查切面。
 */
@Aspect
@Component
public class DuplicateSubmitAspect extends BaseAspect {

    private static final int REQUEST_BODY_MAX_SIZE = 262144;
    private static final long REQUEST_KEY_TTL = 3L;
    private final StringRedisTemplate redisTemplate;

    /**
     * 构造方法。
     */
    @Autowired
    public DuplicateSubmitAspect(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 定义切入点：RestController 客户端请求处理方法。
     */
    @Pointcut(
        "execution(public com.ose.response.* com.ose..*.controller..*.*(..))"
            + " && !@annotation(org.springframework.web.bind.annotation.ExceptionHandler)"
    )
    public void controller() {
    }

    /**
     * 检查是否为重复的非幂等请求。
     */
    @Before(value = "controller()", argNames = "point")
    public void doBefore(JoinPoint point) {

        final HttpServletRequest request = getRequest();

        if (request == null) {
            return;
        }

        final String requestMethod = request.getMethod().toUpperCase();
        final String remoteAddr = RemoteAddressUtils.getRemoteAddr(request);
        final String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 若为幂等请求，或客户端位于同一局域网，或用户未提供认证信息，则不进行检查
        if (!("POST".equals(requestMethod) || "DELETE".equals(requestMethod))
            || (!RemoteAddressUtils.isLoopback(remoteAddr) && RemoteAddressUtils.isPrivate(remoteAddr))
            || accessToken == null) {
            return;
        }

        final ContextDTO context = getContext();
//        final StringRedisConnection redisConn = redisTemplate.execute(
//            (RedisCallback<StringRedisConnection>) connection -> (StringRedisConnection) connection
//        );

        // 若为服务之间的调用，或无法取得 Redis 连接，则不进行检查
        if (context.isFeignClient() /*|| redisConn == null*/) {
            return;
        }

        Parameter[] parameters = ((MethodSignature) point.getSignature()).getMethod().getParameters();
        Parameter parameter;
        Object[] values = point.getArgs();
        Object value;
        String requestBodyHash = "";

        // 当请求数据大于指定的上限时，判断内容类型及大小的一致性
        if (request.getContentLength() > REQUEST_BODY_MAX_SIZE) {
            requestBodyHash = request.getContentType() + "\r\n" + request.getContentLength();
            // 否则判断内容的一致性
        } else {
            for (int i = 0; i < parameters.length; i++) {
                parameter = parameters[i];
                if (parameter.getDeclaredAnnotation(RequestBody.class) != null) {
                    value = values[i];
                    requestBodyHash = CryptoUtils
                        .md5(StringUtils.toJSON(value))
                        .toUpperCase();
                    break;
                }
            }
        }

        // 生成请求数据在 Redis 缓存中的键
        final String requestKey = "REQUEST_KEY:"
            + CryptoUtils
            .md5(
                remoteAddr
                    + "\r\n" + request.getHeader(HttpHeaders.USER_AGENT)
                    + "\r\n" + request.getRequestURL()
                    + "\r\n" + accessToken
            )
            .toUpperCase()
            + ":" + requestBodyHash;

        redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            StringRedisConnection redisConn = (StringRedisConnection) connection;

            if(redisConn == null) {
                return false;
            }
            // 若指定的期间内客户端提交过相同的请求则返回 429 错误
            if (redisConn.ttl(requestKey) > 0) {
                redisConn.expire(requestKey, REQUEST_KEY_TTL);
                throw new TooManyRequestsError();
            }

            // 记录客户端请求
            redisConn.set(requestKey, "" + System.currentTimeMillis());
            redisConn.expire(requestKey, REQUEST_KEY_TTL);
            return true;
        });
    }

}
