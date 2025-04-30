package com.ose.tasks.domain.model.repository.trident;

import com.ose.tasks.dto.trident.EntityTypeProcessItrTemplateRelationCriteriaDTO;
import com.ose.tasks.entity.trident.EntityTypeProcessItrTemplateRelation;
import org.springframework.data.domain.Page;

public interface EntityTypeProcessItrTemplateRelationRepositoryCustom {

    /**
     * 获取列表。
     *
     * @param projectId         项目ID
     * @param entityTypeProcessItrTemplateRelationCriteriaDTO 查询条件
     * @return 列表
     */
    Page<EntityTypeProcessItrTemplateRelation> search(Long projectId,
                                                      EntityTypeProcessItrTemplateRelationCriteriaDTO entityTypeProcessItrTemplateRelationCriteriaDTO);


}
