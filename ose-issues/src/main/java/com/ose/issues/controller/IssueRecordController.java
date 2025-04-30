package com.ose.issues.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.issues.api.IssueRecordAPI;
import com.ose.issues.domain.model.service.IssueRecordInterface;
import com.ose.issues.dto.IssueRecordCriteriaDTO;
import com.ose.issues.entity.IssueRecord;
import com.ose.response.JsonListResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "操作记录接口")
@RestController
@RequestMapping("/orgs")
public class IssueRecordController extends BaseController implements IssueRecordAPI {

    @Autowired
    IssueRecordInterface issueRecordService;

    /**
     * 获取遗留问题操作记录列表
     *
     * @param orgId                  组织ID
     * @param projectId              项目ID
     * @param issueRecordCriteriaDTO 查询条件
     * @param pageDTO                分页参数
     * @return 操作记录列表
     */
    @Operation(
        summary = "获取遗留问题操作记录列表"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/issues/{issueId}/records",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @Override
    public JsonListResponseBody<IssueRecord> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "遗留问题ID") Long issueId,
        IssueRecordCriteriaDTO issueRecordCriteriaDTO,
        PageDTO pageDTO
    ) {
        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            issueRecordService.search(
                orgId,
                projectId,
                issueId,
                issueRecordCriteriaDTO,
                pageDTO
            )
        );
    }
}
