package com.ose.issues.domain.model.repository;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.TagCriteriaDTO;
import com.ose.issues.entity.Tag;
import org.springframework.data.domain.Page;

public interface TagCustomRepository {

    /**
     * 获取标签列表
     *
     * @param projectId      项目ID
     * @param tagCriteriaDTO 查询信息
     * @return 标签列表
     */
    Page<Tag> search(Long projectId, PageDTO pageDTO, TagCriteriaDTO tagCriteriaDTO);
}
