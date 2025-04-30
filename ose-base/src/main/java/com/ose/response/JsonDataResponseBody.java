package com.ose.response;

import com.ose.dto.ContextDTO;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 对象响应数据。
 */
public abstract class JsonDataResponseBody extends JsonResponseBody {

    @Schema(description = "相关链接")
    private Map<String, String> links = null;

    @Schema(description = "引用数据")
    private Map<Long, Object> included = null;

    /**
     * 构造方法。
     */
    public JsonDataResponseBody() {
    }

    /**
     * 构造方法。
     */
    public JsonDataResponseBody(ContextDTO context) {
        super(context);
    }

    /**
     * 取得相关链接。
     *
     * @return 相关链接
     */
    public Map<String, String> getLinks() {
        return links;
    }

    /**
     * 设置相关链接。
     *
     * @param name        链接名
     * @param requestURI  请求路径
     * @param queryParams 查询参数对象
     * @return 响应数据实例
     */
    public JsonDataResponseBody setLink(
        String name,
        String requestURI,
        Map<String, String[]> queryParams
    ) {
        return setLink(name, setQueryParams(requestURI, queryParams));
    }

    /**
     * 设置相关链接。
     *
     * @param name 链接名
     * @param link 链接
     * @return 响应数据实例
     */
    public JsonDataResponseBody setLink(String name, String link) {

        if (links == null) {
            links = new HashMap<>();
        }

        links.put(name, link);

        return this;
    }

    /**
     * 取得引用数据。
     *
     * @return 引用数据
     */
    public Map<Long, Object> getIncluded() {
        return included == null ? new HashMap<>() : included;
    }

    /**
     * 设置引用数据。
     *
     * @param included 引用数据
     */
    public JsonDataResponseBody setIncluded(Map<Long, Object> included) {

        if (included != null && included.keySet().size() > 0) {
            this.included = included;
        } else {
            this.included = null;
        }

        return this;
    }

    /**
     * 添加引用数据。
     *
     * @param source 引用数据
     */
    public JsonDataResponseBody addIncluded(Map<Long, Object> source) {

        if (source != null && source.keySet().size() > 0) {

            Iterator<Long> keys = source.keySet().iterator();
            Long key;
            Map<Long, Object> included = this.getIncluded();

            while (keys.hasNext()) {
                key = keys.next();
                included.put(key, source.get(key));
            }

            this.setIncluded(included);
        }

        return this;
    }

    /**
     * 设置 Query 参数。
     *
     * @param requestURI  请求路径
     * @param queryParams 查询参数对象
     * @return 链接
     */
    private static String setQueryParams(
        String requestURI,
        Map<String, String[]> queryParams
    ) {

        List<String> keyValuePairs = new ArrayList<>();

        for (HashMap.Entry<String, String[]> entry : queryParams.entrySet()) {

            String key = entry.getKey();

            for (String value : entry.getValue()) {
                keyValuePairs.add(
                    StringUtils.encodeURIComponent(key)
                        + "=" + StringUtils.encodeURIComponent(value)
                );
            }

        }

        return requestURI + "?" + String.join("&", keyValuePairs);
    }

}
