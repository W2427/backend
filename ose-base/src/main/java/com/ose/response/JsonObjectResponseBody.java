package com.ose.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.dto.BaseDTO;
import com.ose.dto.ContextDTO;
import com.ose.service.EntityInterface;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * 对象响应数据。
 *
 * @param <T> 返回数据的类型
 */
@JsonPropertyOrder({
    "success",
    "accessToken",
    "links",
    "data",
    "included",
    "error"
})
public class JsonObjectResponseBody<T extends BaseDTO> extends JsonDataResponseBody {

    @Schema(description = "返回数据")
    private T data = null;

    /**
     * 构造方法。
     */
    public JsonObjectResponseBody() {
    }

    /**
     * 构造方法。
     */
    public JsonObjectResponseBody(T data) {
        this(null, data);
    }

    /**
     * 构造方法。
     */
    public JsonObjectResponseBody(ContextDTO context, T data) {
        super(context);
        this.setData(data);
    }

    /**
     * 取得返回数据。
     *
     * @return 返回数据
     */
    public T getData() {
        return data;
    }

    /**
     * 设置返回数据。
     *
     * @param data 返回数据
     * @return 响应数据实例
     */
    public JsonObjectResponseBody<T> setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 设置引用数据。
     *
     * @param included 引用数据
     * @return 响应数据实例
     */
    @JsonSetter
    public JsonObjectResponseBody<T> setIncluded(Map<Long, Object> included) {
        super.setIncluded(included);
        return this;
    }

    /**
     * 设置引用数据。
     *
     * @param entityInterface 引用数据查询接口
     * @return 响应数据实例
     */
    public JsonObjectResponseBody<T> setIncluded(
        EntityInterface entityInterface
    ) {
        setIncluded(entityInterface.setIncluded(getIncluded(), getData()));
        return this;
    }

    /**
     * 添加引用数据。
     *
     * @param source 引用数据
     */
    public JsonObjectResponseBody<T> addIncluded(Map<Long, Object> source) {
        super.addIncluded(source);
        return this;
    }

    /**
     * 设置访问令牌。
     *
     * @param accessToken 访问令牌
     * @return 响应数据实例
     */
    @Override
    public JsonObjectResponseBody<T> setAccessToken(String accessToken) {
        super.setAccessToken(accessToken);
        return this;
    }

}
