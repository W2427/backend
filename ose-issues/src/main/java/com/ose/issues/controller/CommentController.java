package com.ose.issues.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.issues.api.CommentAPI;
import com.ose.issues.domain.model.service.IssueCommentInterface;
import com.ose.issues.dto.IssueCommentCreateDTO;
import com.ose.issues.dto.IssueCommentUpdateDTO;
import com.ose.issues.entity.IssueComment;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "问题评论")
public class CommentController extends BaseController implements CommentAPI {

    private IssueCommentInterface commentService;

    @Autowired
    public CommentController(
        IssueCommentInterface commentService
    ) {
        this.commentService = commentService;
    }

    @Override
    @Operation(description = "创建问题评论")
    @WithPrivilege
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "问题 ID") Long issueId,
        @RequestBody IssueCommentCreateDTO commentCreateDTO
    ) {

        commentService.create(
            getContext().getOperator().getId(),
            issueId,
            commentCreateDTO
        );

        return new JsonResponseBody();
    }


    @Override
    @Operation(description = "获取评论列表")
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<IssueComment> search(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "问题 ID") Long issueId,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            getContext(),
            commentService.search(issueId, pageDTO)
        );
    }

    @Override
    @Operation(description = "更新评论信息")
    @WithPrivilege
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "问题 ID") Long issueId,
        @PathVariable @Parameter(description = "评论 ID") Long commentId,
        @RequestBody IssueCommentUpdateDTO commentUpdateDTO
    ) {

        commentService.update(
            getContext().getOperator().getId(),
            commentId,
            commentUpdateDTO
        );

        return new JsonResponseBody();
    }

}
