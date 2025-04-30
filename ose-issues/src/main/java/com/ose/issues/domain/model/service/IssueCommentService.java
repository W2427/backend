package com.ose.issues.domain.model.service;

import com.ose.dto.PageDTO;
import com.ose.exception.NotFoundError;
import com.ose.exception.UnauthorizedError;
import com.ose.issues.domain.model.repository.CommentRepository;
import com.ose.issues.domain.model.repository.IssueRepository;
import com.ose.issues.dto.IssueCommentCreateDTO;
import com.ose.issues.dto.IssueCommentUpdateDTO;
import com.ose.issues.entity.IssueComment;
import com.ose.issues.entity.Issue;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class IssueCommentService implements IssueCommentInterface {

    private CommentRepository commentRepository;
    private IssueRepository issueRepository;

    public IssueCommentService(
        CommentRepository commentRepository,
        IssueRepository issueRepository
    ) {
        this.commentRepository = commentRepository;
        this.issueRepository = issueRepository;
    }

    /**
     * 创建遗留问题评论
     *
     * @param operatorId       操作人ID
     * @param issueId          问题ID
     * @param commentCreateDTO 评论创建信息
     */
    @Override
    public void create(Long operatorId, Long issueId, IssueCommentCreateDTO commentCreateDTO) {

        Issue issue = issueRepository.findByIdAndDeletedIsFalse(issueId);

        if (issue == null) {
            throw new NotFoundError("issue is not found");
        }

        Date now = new Date();
        IssueComment comment = BeanUtils.copyProperties(commentCreateDTO, new IssueComment());

        comment.setIssueId(issueId);

        comment.setComment(commentCreateDTO.getComment());
        comment.setCreatedAt(now);
        comment.setCreatedBy(operatorId);
        comment.setLastModifiedAt(now);
        comment.setLastModifiedBy(operatorId);
        comment.setStatus(EntityStatus.ACTIVE);
        commentRepository.save(comment);
    }

    /**
     * 获取评论列表
     *
     * @param issueId 问题ID
     * @param pageDTO 分页参数
     * @return 问题回复列表
     */
    @Override
    public Page<IssueComment> search(Long issueId, PageDTO pageDTO) {

        return commentRepository.findAllByIssueIdAndDeletedIsFalse(issueId, pageDTO.toPageable());
    }

    /**
     * 更新评论信息
     *
     * @param operatorId       操作人ID
     * @param commentId        评论ID
     * @param commentUpdateDTO 更新信息
     */
    @Override
    public void update(Long operatorId, Long commentId, IssueCommentUpdateDTO commentUpdateDTO) {

        // 评价信息
        IssueComment comment = commentRepository.findByIdAndDeletedIsFalse(commentId);

        if (comment == null) {
            throw new NotFoundError("comment is not found");
        }

        Issue issue = issueRepository.findByIdAndDeletedIsFalse(comment.getIssueId());

        if (issue == null) {
            throw new NotFoundError("issue is not found");
        }
        // 只有问题负责人有操作权限
        if (!issue.getPersonInChargeId().equals(operatorId)) {
            throw new UnauthorizedError();
        }

        if (commentUpdateDTO.getComment() == null) {
            return;
        }

        comment.setComment(commentUpdateDTO.getComment());
        comment.setLastModifiedBy(operatorId);
        comment.setLastModifiedAt(new Date());
        commentRepository.save(comment);
    }
}
