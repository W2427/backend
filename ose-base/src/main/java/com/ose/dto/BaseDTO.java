package com.ose.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.util.LongUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据传输对象基类。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseDTO implements Serializable {

    private static final long serialVersionUID = -5237260617616981718L;

    /**
     * 取得相关联的用户 ID 的集合。
     *
     * @return 相关联的用户 ID 的集合
     */
    public Set<Long> relatedUserIDs() {
        return new HashSet<>();
    }

    /**
     * 取得相关联的组织 ID 的集合。
     *
     * @return 相关联的组织 ID 的集合
     */
    public Set<Long> relatedOrgIDs() {
        return new HashSet<>();
    }

    /**
     * 引用数据。
     */
    public static class ReferenceData {

        @Schema(description = "引用目标数据实体 ID")
        private Long $ref;

        /**
         * 构造方法。
         */
        public ReferenceData() {
        }

        /**
         * 构造方法。
         *
         * @param referenceId 引用目标数据实体 ID
         */
        public ReferenceData(Long referenceId) {
            this.$ref = referenceId;
        }

        public ReferenceData(String referenceId) { this.$ref = LongUtils.parseLong(referenceId); }

        /**
         * 取得引用目标数据实体 ID。
         *
         * @return 引用目标数据实体 ID
         */
        public Long get$ref() {
            return $ref;
        }

    }

}
