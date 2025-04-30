package com.ose.issues.domain.model.service;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.TagCreateDTO;
import com.ose.issues.dto.TagCriteriaDTO;
import com.ose.issues.entity.Tag;
import org.springframework.data.domain.Page;

public interface TagInterface {

    /**
     * 创建标签
     *
     * @param operatorId   操作人ID
     * @param projectId    项目ID
     * @param tagCreateDTO 创建信息
     */
    void create(Long operatorId, Long projectId, TagCreateDTO tagCreateDTO);

    /**
     * 获取标签列表
     *
     * @param projectId      项目ID
     * @param pageDTO        分页参数
     * @param tagCriteriaDTO 查询参数
     * @return 标签列表
     */
    Page<Tag> search(Long projectId, PageDTO pageDTO, TagCriteriaDTO tagCriteriaDTO);

    /**
     * 获取标签详情
     *
     * @param projectId 项目ID
     * @param tagId     标签ID
     * @return 标签详情
     */
    Tag get(Long projectId, Long tagId);
}
