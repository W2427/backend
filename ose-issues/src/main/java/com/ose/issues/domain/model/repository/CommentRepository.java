package com.ose.issues.domain.model.repository;

import com.ose.issues.entity.IssueComment;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommentRepository extends PagingAndSortingWithCrudRepository<IssueComment, Long> {

    /**
     * 获取问题的评论列表
     *
     * @param issueId 问题ID
     * @return 评论列表
     */
    Page<IssueComment> findAllByIssueIdAndDeletedIsFalse(Long issueId, Pageable pageable);

    /**
     * 获取评论信息
     *
     * @param commentId 评论ID
     * @return 评论信息
     */
    IssueComment findByIdAndDeletedIsFalse(Long commentId);
}
