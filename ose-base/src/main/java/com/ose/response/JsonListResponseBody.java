package com.ose.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.dto.BaseDTO;
import com.ose.dto.ContextDTO;
import com.ose.entity.BaseEntity;
import com.ose.service.EntityInterface;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 列表响应数据。
 *
 * @param <T> 返回数据的类型
 */
@JsonPropertyOrder({
    "success",
    "accessToken",
    "meta",
    "links",
    "data",
    "included",
    "error"
})
public class JsonListResponseBody<T extends BaseDTO> extends JsonDataResponseBody {

    @Schema(description = "查询元数据")
    private Meta meta = new Meta();

    @Schema(description = "查询结果")
    private List<T> data = null;

    /**
     * 构造方法。
     */
    public JsonListResponseBody() {
    }

    /**
     * 构造方法。
     */
    public JsonListResponseBody(Page<T> page) {
        this(null, page);
    }

    /**
     * 构造方法。
     */
    public JsonListResponseBody(ContextDTO context, Page<T> page) {

        super(context);

        if (page == null) {
            this
                .setMeta(new Meta())
                .setData(new ArrayList<>());
        } else {
            this
                .setMeta(new Meta(page))
                .setData(page.getContent());
        }

        setPaginationLinks();
    }

    /**
     * 构造方法。
     */
    public JsonListResponseBody(List<T> list) {
        this(null, list);
    }

    /**
     * 构造方法。
     */
    public JsonListResponseBody(ContextDTO context, List<T> list) {
        super(context);
        this.setData(list == null ? new ArrayList<>() : list);
    }

    /**
     * 设置分页链接。
     */
    private void setPaginationLinks() {

        HttpServletRequest request = getRequest();

        if (request == null) {
            return;
        }

        int pages = getMeta().getPages();
        int pageNo = getMeta().getPageNo();
        String requestURI = request.getRequestURI();
        Map<String, String[]> queryParams = request.getParameterMap();

        // 首页链接
        if (pages > 0) {
            setPaginationLink("first", requestURI, queryParams, 1);
        }

        // 上一页链接
        if (pageNo > 1) {
            setPaginationLink("previous", requestURI, queryParams, pageNo - 1);
        }

        // 当前页链接
        if (pageNo <= pages) {
            setPaginationLink("self", requestURI, queryParams, pageNo);
        }

        // 下一页链接
        if (pageNo < pages) {
            setPaginationLink("next", requestURI, queryParams, pageNo + 1);
        }

        // 尾页链接
        if (pages > 0) {
            setPaginationLink("last", requestURI, queryParams, pages);
        }

    }

    /**
     * 设置分页链接。
     *
     * @param name        链接名
     * @param requestURI  请求 URI
     * @param queryParams Query 参数
     * @param pageNo      分页号
     */
    public void setPaginationLink(
        String name,
        String requestURI,
        Map<String, String[]> queryParams,
        Integer pageNo
    ) {

        Map<String, String[]> newQueryParams = new HashMap<>(queryParams);

        newQueryParams.put("page.no", new String[]{pageNo.toString()});

        newQueryParams.put("page.size", new String[]{
            this.getMeta().getPageSize().toString()
        });

        setLink(name, requestURI, newQueryParams);
    }

    /**
     * 取得查询元数据。
     *
     * @return 查询元数据
     */
    public Meta getMeta() {
        return this.meta;
    }

    /**
     * 设置查询元数据。
     *
     * @param meta 查询元数据
     * @return 响应数据实例
     */
    public JsonListResponseBody<T> setMeta(Meta meta) {
        this.meta = meta;
        return this;
    }

    /**
     * 取得返回数据。
     *
     * @return 返回数据
     */
    public List<T> getData() {
        return data;
    }

    /**
     * 设置返回数据。
     *
     * @param data 返回数据
     * @return 响应数据实例
     */
    public JsonListResponseBody<T> setData(List<T> data) {
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
    public JsonListResponseBody<T> setIncluded(Map<Long, Object> included) {
        super.setIncluded(included);
        return this;
    }

    /**
     * 设置引用数据。
     *
     * @param entityInterface 引用数据查询接口
     * @return 响应数据实例
     */
    public JsonListResponseBody<T> setIncluded(
        EntityInterface entityInterface
    ) {

        List<T> data = getData();

        if (data == null
            || data.size() == 0
            || !(data.get(0) instanceof BaseEntity)) {
            return this;
        }

        setIncluded(entityInterface.setIncluded(getIncluded(), data));

        return this;
    }

    /**
     * 添加引用数据。
     *
     * @param source 引用数据
     */
    public JsonListResponseBody<T> addIncluded(Map<Long, Object> source) {
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
    public JsonListResponseBody<T> setAccessToken(String accessToken) {
        super.setAccessToken(accessToken);
        return this;
    }

    /**
     * 元数据。
     */
    @JsonPropertyOrder({
        "count",
        "pages",
        "pageNo",
        "pageSize",
        "isFirstPage",
        "hasPreviousPage",
        "hasNextPage",
        "isLastPage"
    })
    public static class Meta {

        @Schema(description = "数据总数")
        private long count = 0;

        @Schema(description = "数据总页数")
        private int pages = 0;

        @Schema(description = "当前页号")
        private Integer pageNo = 1;

        @Schema(description = "分页大小")
        private Integer pageSize = 1;

        @Schema(description = "是否为第一页")
        private boolean isFirstPage = true;

        @Schema(description = "是否存在上一页")
        private boolean hasPreviousPage = false;

        @Schema(description = "是否存在下一页")
        private boolean hasNextPage = false;

        @Schema(description = "是否为最后一页")
        private boolean isLastPage = true;

        /**
         * 默认构造方法。
         */
        public Meta() {
        }

        /**
         * 构造方法。
         *
         * @param page 查询结果分页数据
         */
        public Meta(Page page) {
            this.count = page.getTotalElements();
            this.pages = page.getTotalPages();
            this.pageNo = page.getNumber() + 1;
            this.pageSize = page.getSize();
            this.isFirstPage = page.isFirst();
            this.hasPreviousPage = !this.isFirstPage;
            this.isLastPage = page.isLast();
            this.hasNextPage = !this.isLastPage;
        }

        /**
         * 取得数据总件数。
         *
         * @return 数据总件数
         */
        public long getCount() {
            return count;
        }

        /**
         * 设置数据总件数。
         *
         * @param count 数据总件数
         */
        public void setCount(long count) {
            this.count = count;
        }

        /**
         * 取得数据总页数。
         *
         * @return 数据总页数
         */
        public int getPages() {
            return pages;
        }

        /**
         * 设置数据总页数。
         *
         * @param pages 数据总页数
         */
        public void setPages(int pages) {
            this.pages = pages;
        }

        /**
         * 取得当前页号。
         *
         * @return 当前页号
         */
        public Integer getPageNo() {
            return pageNo;
        }

        /**
         * 取得分页大小。
         *
         * @return 分页大小
         */
        public Integer getPageSize() {
            return pageSize;
        }

        /**
         * 是否为第一页。
         *
         * @return 是否为第一页
         */
        @JsonProperty("isFirstPage")
        public boolean isFirstPage() {
            return isFirstPage;
        }

        /**
         * 是否存在上一页。
         *
         * @return 是否存在上一页
         */
        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        /**
         * 是否存在下一页。
         *
         * @return 是否存在下一页
         */
        public boolean isHasNextPage() {
            return hasNextPage;
        }

        /**
         * 是否为最后一页。
         *
         * @return 是否为最后一页
         */
        @JsonProperty("isLastPage")
        public boolean isLastPage() {
            return isLastPage;
        }

    }

}
