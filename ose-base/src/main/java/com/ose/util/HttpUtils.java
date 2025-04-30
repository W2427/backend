package com.ose.util;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.*;

/**
 * HTTP 客户端工具。
 */
public class HttpUtils {

    private static final String USER_AGENT_STRING = "OSE_BPM/1.0";
    private static final String CHARSET = "UTF-8";
    private static final MediaType APPLICATION_JSON_UTF8;
    private static final HttpHeaders HEADERS_GET = new HttpHeaders();
    private static final HttpHeaders HEADERS_JSON = new HttpHeaders();
    private static final HttpHeaders HEADERS_URLENCODED = new HttpHeaders();
    private static final RestTemplate restTemplate = new RestTemplate();

    // 初始化请求头
    static {

        Map<String, String> contentTypeParams = new HashMap<>();

        contentTypeParams.put("charset", CHARSET);

        APPLICATION_JSON_UTF8 = new MediaType(
            APPLICATION_JSON,
            contentTypeParams
        );

        HEADERS_GET.add(USER_AGENT, USER_AGENT_STRING);
        HEADERS_GET.add(ACCEPT, ALL_VALUE);

        HEADERS_JSON.add(USER_AGENT, USER_AGENT_STRING);
        HEADERS_JSON.setContentType(APPLICATION_JSON_UTF8);
        HEADERS_JSON.add(ACCEPT, ALL_VALUE);

        HEADERS_URLENCODED.add(USER_AGENT, USER_AGENT_STRING);
        HEADERS_URLENCODED.add(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE);
        HEADERS_URLENCODED.add(ACCEPT, ALL_VALUE);

    }

    /**
     * 设置 URL。
     *
     * @param url         URL
     * @param queryParams 查询参数对象
     * @return 设置参数后的 URL
     */
    private static String setURI(String url, Object queryParams) {

        if (queryParams == null) {
            return url;
        }

        URI uri;

        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            return url;
        }

        int port = uri.getPort();
        String path = uri.getPath();
        String query = uri.getQuery();
        String hash = uri.getFragment();

        query = (StringUtils.isEmpty(query) ? "" : (query + "&"))
            + StringUtils.toURLEncoded(queryParams);

        return uri.getScheme()
            + "://" + uri.getHost()
            + (port < 0 ? "" : (":" + port))
            + (StringUtils.isEmpty(path) ? "/" : path)
            + (StringUtils.isEmpty(query) ? "?" : query)
            + (StringUtils.isEmpty(hash) ? "" : ("#" + hash));

    }

    /**
     * 发送 GET 请求。
     *
     * @param <T>          范型
     * @param uri          请求 URI
     * @param responseType 响应数据类型
     * @return HTTP 响应实例
     */
    public static <T> ResponseEntity<T> get(
        String uri,
        Class<T> responseType
    ) {
        return get(uri, null, responseType, null);
    }

    /**
     * 发送 GET 请求。
     *
     * @param <T>           范型
     * @param uri           请求 URI
     * @param pathVariables 路径参数
     * @param responseType  响应数据类型
     * @return HTTP 响应实例
     */
    public static <T> ResponseEntity<T> get(
        String uri,
        Map<String, ?> pathVariables,
        Class<T> responseType
    ) {
        return get(uri, pathVariables, null, responseType);
    }

    /**
     * 发送 GET 请求。
     *
     * @param <T>          范型
     * @param uri          请求 URI
     * @param queryParams  查询参数
     * @param responseType 响应数据类型
     * @return HTTP 响应实例
     */
    public static <T> ResponseEntity<T> get(
        String uri,
        Class<T> responseType,
        Object queryParams
    ) {
        return get(uri, null, responseType, queryParams);
    }

    /**
     * 发送 GET 请求。
     *
     * @param <T>           范型
     * @param uri           请求 URI
     * @param pathVariables 路径参数
     * @param queryParams   查询参数
     * @param responseType  响应数据类型
     * @return HTTP 响应实例
     */
    public static <T> ResponseEntity<T> get(
        String uri,
        Map<String, ?> pathVariables,
        Class<T> responseType,
        Object queryParams
    ) {
        return send(
            GET, uri, pathVariables,
            HEADERS_GET,
            null,
            responseType,
            queryParams
        );
    }

    /**
     * 发送 POST 请求（Content-Type: application/json）。
     *
     * @param <T>          范型
     * @param uri          请求 URI
     * @param body         请求数据
     * @param responseType 响应数据类型
     * @return HTTP 响应实例
     */
    public static <T> ResponseEntity<T> postJSON(
        String uri,
        Object body,
        Class<T> responseType
    ) {
        return postJSON(uri, null, body, responseType, null);
    }

    /**
     * 发送 POST 请求（Content-Type: application/json）。
     *
     * @param <T>          范型
     * @param uri          请求 URI
     * @param body         请求数据
     * @param responseType 响应数据类型
     * @param queryParams  查询参数
     * @return HTTP 响应实例
     */
    public static <T> ResponseEntity<T> postJSON(
        String uri,
        Object body,
        Class<T> responseType,
        Object queryParams
    ) {
        return postJSON(uri, null, body, responseType, queryParams);
    }

    /**
     * 发送 POST 请求（Content-Type: application/json）。
     *
     * @param <T>           范型
     * @param uri           请求 URI
     * @param pathVariables 路径参数
     * @param body          请求数据
     * @param responseType  响应数据类型
     * @return HTTP 响应实例
     */
    public static <T> ResponseEntity<T> postJSON(
        String uri,
        Map<String, ?> pathVariables,
        Object body,
        Class<T> responseType
    ) {
        return postJSON(uri, pathVariables, body, responseType, null);
    }

    /**
     * 发送 POST 请求（Content-Type: application/json）。
     *
     * @param <T>           范型
     * @param uri           请求 URI
     * @param pathVariables 路径参数
     * @param body          请求数据
     * @param responseType  响应数据类型
     * @return HTTP 响应实例
     */
    public static <T> ResponseEntity<T> postJSON(
        String uri,
        Map<String, ?> pathVariables,
        Object body,
        Class<T> responseType,
        Object queryParams
    ) {
        return post(
            uri, pathVariables,
            HEADERS_JSON,
            StringUtils.toJSON(body),
            responseType,
            queryParams
        );
    }

    /**
     * 发送 POST 请求（Content-Type: application/x-www-form-urlencoded）。
     *
     * @param <T>          范型
     * @param uri          请求 URI
     * @param body         请求数据
     * @param responseType 响应数据类型
     * @return HTTP 响应实例
     */
    public static <T> ResponseEntity<T> postURLEncoded(
        String uri,
        Object body,
        Class<T> responseType
    ) {
        return postURLEncoded(uri, null, body, responseType, null);
    }

    /**
     * 发送 POST 请求（Content-Type: application/x-www-form-urlencoded）。
     *
     * @param <T>          范型
     * @param uri          请求 URI
     * @param body         请求数据
     * @param responseType 响应数据类型
     * @param queryParams  查询参数
     * @return HTTP 响应实例
     */
    public static <T> ResponseEntity<T> postURLEncoded(
        String uri,
        Object body,
        Class<T> responseType,
        Object queryParams
    ) {
        return postURLEncoded(uri, null, body, responseType, queryParams);
    }

    /**
     * 发送 POST 请求（Content-Type: application/x-www-form-urlencoded）。
     *
     * @param <T>           范型
     * @param uri           请求 URI
     * @param pathVariables 路径参数
     * @param body          请求数据
     * @param responseType  响应数据类型
     * @return HTTP 响应实例
     */
    public static <T> ResponseEntity<T> postURLEncoded(
        String uri,
        Map<String, ?> pathVariables,
        Object body,
        Class<T> responseType
    ) {
        return postURLEncoded(uri, pathVariables, body, responseType, null);
    }

    /**
     * 发送 POST 请求（Content-Type: application/x-www-form-urlencoded）。
     *
     * @param <T>           范型
     * @param uri           请求 URI
     * @param pathVariables 路径参数
     * @param body          请求数据
     * @param responseType  响应数据类型
     * @return HTTP 响应实例
     */
    public static <T> ResponseEntity<T> postURLEncoded(
        String uri,
        Map<String, ?> pathVariables,
        Object body,
        Class<T> responseType,
        Object queryParams
    ) {
        return post(
            uri, pathVariables,
            HEADERS_URLENCODED,
            StringUtils.toURLEncoded(body),
            responseType,
            queryParams
        );
    }

    /**
     * 发送 POST 请求。
     *
     * @param <T>           范型
     * @param uri           请求 URI
     * @param pathVariables 路径参数
     * @param headers       请求头
     * @param body          请求数据
     * @param responseType  响应数据类型
     * @return HTTP 响应实例
     */
    public static <T> ResponseEntity<T> post(
        String uri,
        Map<String, ?> pathVariables,
        HttpHeaders headers,
        String body,
        Class<T> responseType,
        Object queryParams
    ) {
        return send(
            POST, uri, pathVariables,
            headers,
            body,
            responseType,
            queryParams
        );
    }

    /**
     * 发送 HTTP 请求。
     *
     * @param <T>           范型
     * @param method        请求方法
     * @param uri           请求 URI
     * @param pathVariables 路径参数
     * @param headers       请求头
     * @param body          发送数据
     * @param responseType  响应数据类型
     * @return HTTP 响应实例
     */
    public static <T> ResponseEntity<T> send(
        HttpMethod method,
        String uri,
        Map<String, ?> pathVariables,
        HttpHeaders headers,
        String body,
        Class<T> responseType,
        Object queryParams
    ) {
        return restTemplate.exchange(
            setURI(uri, queryParams),
            method,
            body == null
                ? new HttpEntity<>(headers)
                : new HttpEntity<>(body, headers),
            responseType,
            pathVariables == null
                ? new HashMap<>()
                : pathVariables
        );
    }

}
