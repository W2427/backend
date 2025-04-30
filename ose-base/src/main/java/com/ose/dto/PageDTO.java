package com.ose.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 查询分页设置。
 */
public class PageDTO extends BaseDTO {

    private static final long serialVersionUID = 381872807416613759L;

    @Schema(description = "分页设置")
    private Page page;

    @Schema(description = "排序字段，格式为字段名加升降序，如 <code>id:desc</code>")
    private String[] sort;

    @Schema(description = "是否取得所有数据（默认：否）")
    private Boolean fetchAll = false;

    /**
     * 构造方法。
     */
    public PageDTO() {
        this(new Page());
    }

    /**
     * 构造方法。
     *
     * @param no   页号
     * @param size 分页大小
     */
    public PageDTO(Integer no, Integer size) {
        this(new Page(no, size));
    }

    /**
     * 构造方法。
     *
     * @param page 分页信息
     */
    public PageDTO(Page page) {
        this.setPage(page);
    }

    /**
     * 取得分页设置。
     *
     * @return 分页设置
     */
    public Page getPage() {
        return this.page;
    }

    /**
     * 设置分页信息。
     *
     * @param page 分页信息
     */
    public void setPage(Page page) {
        this.page = page;
    }

    /**
     * 取得排序字段。
     *
     * @return 排序字段
     */
    public String[] getSort() {
        return this.sort;
    }

    /**
     * 设置排序字段。
     *
     * @param sort 排序字段
     */
    public void setSort(String[] sort) {
        this.sort = sort;
    }

    /**
     * 取得是否取得所有数据的标记。
     *
     * @return 是否取得所有数据
     */
    public Boolean getFetchAll() {
        return fetchAll;
    }

    /**
     * 设置是否取得所有数据的标记。
     *
     * @param fetchAll 是否取得所有数据
     */
    public void setFetchAll(Boolean fetchAll) {
        this.fetchAll = fetchAll;
    }

    /**
     * 生成排序对象。
     *
     * @param sortOrders 字段排序列表
     * @return 排序对象
     */
    private static Sort toSort(String[] sortOrders) {

        if (sortOrders == null || sortOrders.length == 0) {
            return Sort.by(new Sort.Order(Sort.Direction.DESC, "id"));
        }

        Sort.Order[] orders = new Sort.Order[sortOrders.length];

        String sortOrder;
        String[] kvp;
        Sort.Direction direction;

        for (int i = 0; i < sortOrders.length; i++) {

            sortOrder = sortOrders[i];

            kvp = (sortOrder + ":").split(":");

            direction = "DESC".equals(kvp[1].toUpperCase())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

            orders[i] = new Sort.Order(direction, kvp[0]);
        }

        return Sort.by(orders);
    }

    /**
     * 将分页参数转为 Pageable 实例。
     *
     * @return Pageable 实例
     */
    public Pageable toPageable() {

        if (fetchAll) {
            return null;
        }

        return PageRequest.of(page.no - 1, page.size, toSort(sort));
    }

    /**
     * 分页信息。
     */
    public static class Page {

        private static final Integer DEFAULT_PAGE_NO = 1;
        private static final Integer DEFAULT_PAGE_SIZE = 20;

        // 页号
        @Schema(description = "页号（从 1 开始）")
        private int no;

        // 分页大小
        @Schema(description = "分页大小（取值范围：1~100；默认：20）")
        private int size;

        /**
         * 构造方法。
         */
        public Page() {
            this(DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);
        }

        /**
         * 构造方法。
         *
         * @param no   页号
         * @param size 分页大小
         */
        public Page(Integer no, Integer size) {
            this.setNo(no == null ? DEFAULT_PAGE_NO : no);
            this.setSize(size == null ? DEFAULT_PAGE_SIZE : size);
        }

        /**
         * 取得页号。
         *
         * @return 页号
         */
        public int getNo() {
            return no;
        }

        /**
         * 设置页号。
         *
         * @param no 页号
         */
        public void setNo(int no) {
            this.no = Math.max(1, no);
        }

        /**
         * 取得分页大小。
         *
         * @return 分页大小
         */
        public int getSize() {
            return size;
        }

        /**
         * 设置分页大小。
         *
         * @param size 分页大小
         */
        public void setSize(int size) {
            this.size = Math.min(Math.max(1, size), 36655);
        }

    }

}
