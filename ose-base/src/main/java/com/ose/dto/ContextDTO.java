package com.ose.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.exception.AccessTokenExpiredError;
import com.ose.exception.AccessTokenInvalidError;
import com.ose.util.RemoteAddressUtils;
import com.ose.util.StringUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static com.ose.constant.HttpRequestAttributes.FEIGN_CLIENT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.USER_AGENT;

/**
 * 请求上下文对象。
 */
public class ContextDTO implements Serializable {

    private static final long serialVersionUID = -7530347688214078329L;

    @Schema(description = "HTTP 请求实例")
    private HttpServletRequest request;

    @Schema(description = "HTTP 响应实例")
    private HttpServletResponse response;

    @Schema(description = "客户端用户代理字符串（取自 User-Agent 请求头）")
    private String userAgent;

    @Schema(description = "客户端远程 IP 地址")
    private String remoteAddr;

    @Schema(description = "授权信息（取自 Authorization 请求头）")
    private String authorization;

    @Schema(description = "授权用户信息")
    private OperatorDTO operator;

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Schema(description = "Request Method")
    private String requestMethod;

    @Schema(description = "访问令牌")
    private String accessToken;

    @Schema(description = "是否为 Eureka Feign 客户端发送的请求")
    private boolean feignClient = false;

    @Schema(description = "是否已完成初始化")
    private boolean initialized = false;

    @Schema(description = "是否 已经设置过 context")
    private boolean isContextSet = false;

    /**
     * 默认构造方法。
     */
    public ContextDTO() {
    }

    /**
     * HTTP 服务构造方法。
     *
     * @param request  HTTP 请求实例
     * @param response HTTP 响应实例
     */
    public ContextDTO(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        this.init(request, response);
    }

    /**
     * SOAP 构造方法。
     *
     * @param userAgent     客户端用户代理字符串
     * @param remoteAddr    客户端远程 IP 地址
     * @param authorization 授权信息
     */
    public ContextDTO(
        String userAgent,
        String remoteAddr,
        String authorization
    ) {
        this.userAgent = userAgent;
        this.remoteAddr = remoteAddr;
        this.authorization = authorization;
    }

    /**
     * 初始化上下文对象。
     *
     * @param request  HTTP 请求实例
     * @param response HTTP 响应实例
     */
    public void init(
        HttpServletRequest request,
        HttpServletResponse response
    ) {

        this.request = request;
        this.response = response;

        if (request != null) {

            this.userAgent = request.getHeader(USER_AGENT);
            this.remoteAddr = RemoteAddressUtils.getRemoteAddr(request);
            this.feignClient = !StringUtils.isEmpty(request.getHeader(FEIGN_CLIENT));

            String[] values = request.getParameterMap().get("access-token");

            if (values != null && values.length > 0) {
                this.authorization = this.claimTemporaryAccessToken(values[values.length - 1]);
                request.setAttribute(AUTHORIZATION, this.authorization);
            } else {
                this.authorization = request.getHeader(AUTHORIZATION);
            }

        }

        this.initialized = true;
    }

    /**
     * 取得 HTTP 请求实例。
     *
     * @return HTTP 请求实例
     */
    @JsonIgnore
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * 取得 HTTP 响应实例。
     *
     * @return HTTP 响应实例
     */
    @JsonIgnore
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * 取得客户端用户代理字符串。
     *
     * @return 用户代理字符串
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * 取得客户端远程 IP 地址。
     *
     * @return 远程 IP 地址
     */
    public String getRemoteAddr() {
        return remoteAddr;
    }

    /**
     * 取得授权信息。
     *
     * @return 授权信息
     */
    public String getAuthorization() {
        return authorization;
    }

    /**
     * 取得授权用户信息。
     *
     * @return 授权用户信息
     */
    public OperatorDTO getOperator() {
        return operator;
    }

    /**
     * 设置授权用户信息。
     *
     * @param operator 授权用户信息
     */
    public void setOperator(OperatorDTO operator) {

        this.operator = operator;

        if (operator != null) {
            this.accessToken = operator.getRenewedAccessToken();
        }

    }

    /**
     * 取得用户访问令牌。
     *
     * @return 用户访问令牌
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * 是否为 Feign 客户端发送的请求。
     *
     * @return 是否为 Feign 客户端发送的请求
     */
    public boolean isFeignClient() {
        return feignClient;
    }

    /**
     * 是否已完成初始化。
     *
     * @return 是否已完成初始化
     */
    public boolean isInitialized() {
        return initialized;
    }

    public boolean isContextSet() {
        return isContextSet;
    }

    public void setContextSet(boolean contextSet) {
        isContextSet = contextSet;
    }

    /**
     * 生成临时访问令牌。
     *
     * @param expiresAt 有效截止时间
     * @return 临时访问令牌
     */
    public String generateTemporaryAccessToken(Date expiresAt) {

        // 生成JWT的时间
//        long expMillis = System.currentTimeMillis() + ttlMillis;
//        Date exp = new Date(expMillis);
//        String secretKey = Base64.getEncoder().encodeToString(this.getUserAgent().getBytes(StandardCharsets.UTF_8));
//        String secretKey = this.getUserAgent().getBytes(StandardCharsets.UTF_8);

        //生成 HMAC 密钥，根据提供的字节数组长度选择适当的 HMAC 算法，并返回相应的 SecretKey 对象。
        SecretKey key = Keys.hmacShaKeyFor(this.getUserAgent().getBytes(StandardCharsets.UTF_8));

        // 设置jwt的body
//        JwtBuilder builder = Jwts.builder()
//            // 设置签名使用的签名算法和签名使用的秘钥
//            .signWith(key)
//            // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
//            .claims(claims)
//            // 设置过期时间
//            .expiration(exp);
//        return builder.compact();

        return io.jsonwebtoken.Jwts
            .builder()
            .setSubject(this.getAuthorization())
            .signWith(key
            )
            .setExpiration(expiresAt)
            .compact();

    }

    /**
     * 校验临时访问令牌。
     *
     * @param temporaryAccessToken 临时访问令牌
     * @return 访问令牌
     */
    public String claimTemporaryAccessToken(String temporaryAccessToken) {
        try {
            //生成 HMAC 密钥，根据提供的字节数组长度选择适当的 HMAC 算法，并返回相应的 SecretKey 对象。
            SecretKey key = Keys.hmacShaKeyFor(this.getUserAgent().getBytes(StandardCharsets.UTF_8));

            // 得到DefaultJwtParser
            JwtParser jwtParser = Jwts.parser()
                // 设置签名的秘钥
                .verifyWith(key)
                .build();
            Jws<Claims> jws = jwtParser.parseSignedClaims(temporaryAccessToken);
            Claims claims = jws.getPayload();
            return claims.getSubject();

//            return io.jsonwebtoken.Jwts
//                .parser()
//                .setSigningKey(Base64.getEncoder().encodeToString(this.getUserAgent().getBytes(StandardCharsets.UTF_8)))
//                .build()
//                .parseClaimsJws(temporaryAccessToken)
//                .getBody()
//                .getSubject();

        } catch (final ExpiredJwtException e) {
            throw new AccessTokenExpiredError();
        } catch (final JwtException e) {
            throw new AccessTokenInvalidError();
        }
    }

    //Base64.getEncoder().encodeToString(this.getUserAgent().getBytes(StandardCharsets.UTF_8))
}
