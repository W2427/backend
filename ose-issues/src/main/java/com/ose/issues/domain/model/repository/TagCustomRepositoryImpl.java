package com.ose.issues.domain.model.repository;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.TagCriteriaDTO;
import com.ose.issues.entity.Tag;
import com.ose.repository.BaseRepository;
import org.springframework.data.domain.Page;

public class TagCustomRepositoryImpl extends BaseRepository implements TagCustomRepository {

    /**
     * 获取标签列表。
     *
     * @param projectId      项目ID
     * @param tagCriteriaDTO 查询信息
     * @return 标签列表
     */
    @Override
    public Page<Tag> search(Long projectId, PageDTO pageDTO, TagCriteriaDTO tagCriteriaDTO) {
        return getSQLQueryBuilder(Tag.class)
            .is("projectId", projectId)
            .is("targetId", tagCriteriaDTO.getTargetId())
            .is("parentId", tagCriteriaDTO.getParentId())
            .paginate(pageDTO.toPageable())
            .exec()
            .page();
    }
}
