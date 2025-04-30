package com.ose.issues.domain.model.service;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.IssueCommentCreateDTO;
import com.ose.issues.dto.IssueCommentUpdateDTO;
import com.ose.issues.entity.IssueComment;
import org.springframework.data.domain.Page;

public interface IssueCommentInterface {

    /**
     * 创建问题评论。
     *
     * @param operatorId       操作人ID
     * @param issueId          问题ID
     * @param commentCreateDTO 创建信息
     */
    void create(Long operatorId, Long issueId, IssueCommentCreateDTO commentCreateDTO);

    /**
     * 获取评论列表。
     *
     * @param issueId 问题ID
     * @param pageDTO 分页参数
     * @return 问题列表
     */
    Page<IssueComment> search(Long issueId, PageDTO pageDTO);

    /**
     * 更新评价信息。
     *
     * @param operatorId       操作人ID
     * @param commentId        评论人ID
     * @param commentUpdateDTO 更新信息
     */
    void update(Long operatorId, Long commentId, IssueCommentUpdateDTO commentUpdateDTO);
}
