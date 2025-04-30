package com.ose.issues.domain.model.service;

import com.ose.dto.PageDTO;
import com.ose.issues.domain.model.repository.TagRepository;
import com.ose.issues.dto.TagCreateDTO;
import com.ose.issues.dto.TagCriteriaDTO;
import com.ose.issues.entity.Tag;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class TagService implements TagInterface {

    private TagRepository tagRepository;

    @Autowired
    public TagService(
        TagRepository tagRepository
    ) {

        this.tagRepository = tagRepository;
    }

    /**
     * 创建标签。
     *
     * @param operatorId   操作人ID
     * @param projectId    项目ID
     * @param tagCreateDTO 创建信息
     */
    @Override
    public void create(Long operatorId, Long projectId, TagCreateDTO tagCreateDTO) {

        Tag tag = BeanUtils.copyProperties(tagCreateDTO, new Tag());

        tag.setProjectId(projectId);
        tag.setCreatedAt();
        tag.setCreatedBy(operatorId);
        tag.setLastModifiedAt();
        tag.setLastModifiedBy(operatorId);
        tag.setStatus(EntityStatus.ACTIVE);

        tagRepository.save(tag);
    }

    /**
     * 获取标签列表。
     *
     * @param projectId      项目ID
     * @param pageDTO        分页参数
     * @param tagCriteriaDTO 查询参数
     * @return 标签列表
     */
    @Override
    public Page<Tag> search(Long projectId, PageDTO pageDTO, TagCriteriaDTO tagCriteriaDTO) {
        return tagRepository.search(projectId, pageDTO, tagCriteriaDTO);
    }

    /**
     * 获取标签详情。
     *
     * @param projectId 项目ID
     * @param tagId     标签ID
     * @return 标签详情
     */
    @Override
    public Tag get(Long projectId, Long tagId) {
        return tagRepository.findByIdAndDeletedIsFalse(tagId);
    }
}
