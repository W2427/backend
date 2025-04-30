package com.ose.test.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.test.entity.Post;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 业务代码数据仓库。
 */
public interface PostRepository extends PagingAndSortingWithCrudRepository<Post, Long> {

}
