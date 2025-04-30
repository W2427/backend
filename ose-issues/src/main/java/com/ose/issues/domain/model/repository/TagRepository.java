package com.ose.issues.domain.model.repository;

import com.ose.issues.entity.Tag;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TagRepository extends PagingAndSortingWithCrudRepository<Tag, Long>, TagCustomRepository {

    /**
     * 获取标签详情
     *
     * @param tagId 标签ID
     * @return 标签详情
     */
    Tag findByIdAndDeletedIsFalse(Long tagId);
}
