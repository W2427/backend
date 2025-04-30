package com.ose.service;

import com.ose.dto.BaseDTO;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据实体接口。
 */
public interface EntityInterface {

    /**
     * 设置返回结果的引用数据。
     *
     * @param <T>      数据实体范型
     * @param included 引用数据映射表
     * @param page     查询结果分页数据
     * @return 引用数据映射表
     */
    default <T extends BaseDTO> Map<Long, Object> setIncluded(
        Map<Long, Object> included,
        Page<T> page
    ) {

        return setIncluded(
            included,
            page == null ? new ArrayList<>() : page.getContent()
        );

    }

    /**
     * 设置返回结果的引用数据。
     *
     * @param <T>      数据实体范型
     * @param included 引用数据映射表
     * @param entity   实体数据
     * @return 引用数据映射表
     */
    default <T extends BaseDTO> Map<Long, Object> setIncluded(
        Map<Long, Object> included,
        T entity
    ) {

        List<T> entities = new ArrayList<>();

        entities.add(entity);

        return setIncluded(
            included,
            entities
        );

    }

    /**
     * 设置返回结果的引用数据。
     *
     * @param <T>      数据实体范型
     * @param included 引用数据映射表
     * @param entities 查询结果列表
     * @return 引用数据映射表
     */
    default <T extends BaseDTO> Map<Long, Object> setIncluded(
        Map<Long, Object> included,
        List<T> entities
    ) {
        return included;
    }

}
