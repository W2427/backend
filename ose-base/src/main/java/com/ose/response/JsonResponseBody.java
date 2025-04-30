package com.ose.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.dto.ContextDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 响应数据。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(value = {"success", "accessToken", "status", "error"})
public class JsonResponseBody {

    @JsonIgnore
    private ContextDTO context = null;

    @JsonIgnore
    private HttpServletRequest request = null;

    @JsonIgnore
    private HttpServletResponse response = null;

    @Schema(description = "处理是否成功")
    private boolean success = true;

    @Schema(description = "HTTP 状态码")
    private int status = 200;

    @Schema(description = "错误信息")
    private JsonResponseError error = null;

    @Schema(description = "用户访问令牌")
    private String accessToken = null;

    /**
     * 构造方法。
     */
    public JsonResponseBody() {
    }

    /**
     * 构造方法。
     */
    public JsonResponseBody(ContextDTO context) {
        setContext(context);
    }

    /**
     * 取得请求上下文对象。
     *
     * @return 请求上下文对象
     */
    public ContextDTO getContext() {
        return context;
    }

    /**
     * 设置请求上下文对象。
     *
     * @param context 请求上下文对象
     * @return 响应数据实例
     */
    public JsonResponseBody setContext(ContextDTO context) {

        this.context = context;

        if (context != null) {
            this.request = context.getRequest();
            this.response = context.getResponse();
            this.accessToken = context.getAccessToken();
            setStatus(status);
        }

        return this;
    }

    /**
     * 取得 HTTP 请求实例。
     *
     * @return HTTP 请求实例
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * 取得 HTTP 响应实例。
     *
     * @return HTTP 响应实例
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * 取得处理是否成功。
     *
     * @return 处理是否成功
     */
    public boolean getSuccess() {
        return success;
    }

    /**
     * 设置处理是否成功。
     *
     * @param success 处理是否成功
     * @return 响应数据实例
     */
    public JsonResponseBody setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    /**
     * 取得 HTTP 状态码。
     *
     * @return HTTP 状态码
     */
    public int getStatus() {
        return status;
    }

    /**
     * 设置 HTTP 状态码。
     *
     * @param status HTTP 状态码
     * @return 响应数据实例
     */
    public JsonResponseBody setStatus(int status) {

        this.status = status;

        if (this.response != null) {
            this.response.setStatus(this.status);
        }

        return this;
    }

    /**
     * 取得错误信息。
     *
     * @return 错误信息
     */
    public JsonResponseError getError() {
        return error;
    }

    /**
     * 设置错误信息。
     *
     * @param error 错误信息
     * @return 响应数据实例
     */
    @JsonSetter
    public JsonResponseBody setError(JsonResponseError error) {
        this.error = error;
        this.setStatus(error.getStatus());
        this.setSuccess(false);
        return this;
    }

    /**
     * 设置错误信息。
     *
     * @param error 错误信息
     * @return 响应数据实例
     */
    public JsonResponseBody setError(Exception error) {

        if (error == null) {
            return this;
        }

        return this.setError(new JsonResponseError(error));
    }

    /**
     * 取得访问令牌。
     *
     * @return 访问令牌
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * 设置访问令牌。
     *
     * @param accessToken 访问令牌
     * @return 响应数据实例
     */
    public JsonResponseBody setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

}
