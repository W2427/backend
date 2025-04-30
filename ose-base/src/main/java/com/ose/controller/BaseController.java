package com.ose.controller;

import com.ose.exception.*;
import com.ose.exception.*;
import com.ose.response.JsonResponseBody;
import com.ose.response.JsonResponseError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 控制器基类。
 */
@RestControllerAdvice
public class BaseController extends HttpContext {

    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 用户授权错误处理。
     * 用户授权错误包括：未登录、登录凭证不正确、访问令牌过期、访问令牌无效等。
     *
     * @param error 错误信息
     * @return 返回结果
     */
    @ExceptionHandler(value = UnauthorizedError.class)
    public JsonResponseBody unauthorizedErrorHandler(UnauthorizedError error) {
        return baseErrorHandler(error);
    }

    /**
     * 访问拒绝错误处理。
     * 访问拒绝错误包括：请求头无效、权限不足等。
     *
     * @param error 错误信息
     * @return 返回结果
     */
    @ExceptionHandler(value = AccessDeniedError.class)
    public JsonResponseBody accessDeniedErrorHandler(AccessDeniedError error) {
        return baseErrorHandler(error);
    }

    /**
     * 资源不存在错误处理。
     *
     * @param error 错误信息
     * @return 返回结果
     */
    @ExceptionHandler(value = NotFoundError.class)
    public JsonResponseBody notFoundErrorHandler(NotFoundError error) {
        return baseErrorHandler(error);
    }

    /**
     * 表单验证错误处理。
     *
     * @param error 错误信息
     * @return 返回结果
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    private JsonResponseBody methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException error) {
        return baseErrorHandler(new ValidationError(error));
    }

    /**
     * 错误处理。
     *
     * @param error 错误信息
     * @return 返回结果
     */
    @ExceptionHandler(value = BaseError.class)
    private JsonResponseBody baseErrorHandler(BaseError error) {
        return (new JsonResponseBody(getContext())).setError(error);
    }

    /**
     * HTTP 响应错误。
     *
     * @param error 错误信息
     * @return 返回结果
     */
    @ExceptionHandler(value = JsonResponseError.class)
    public JsonResponseBody jsonResponseErrorHandler(JsonResponseError error) {
        return (new JsonResponseBody(getContext())).setError(error);
    }

    /**
     * 异常处理。
     *
     * @param error 错误信息
     * @return 返回结果
     */
    @ExceptionHandler(value = Exception.class)
    private JsonResponseBody exceptionHandler(Exception error) {
        error.printStackTrace(System.out);
        logger.error("BaseController throw exception ", error);
        return (new JsonResponseBody(getContext())).setError(error);
    }

}
