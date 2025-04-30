package com.ose.issues.api;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.IssueRecordCriteriaDTO;
import com.ose.issues.entity.IssueRecord;
import com.ose.response.JsonListResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface IssueRecordAPI {


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
    JsonListResponseBody<IssueRecord> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "遗留问题ID") Long issueId,
        IssueRecordCriteriaDTO issueRecordCriteriaDTO,
        PageDTO pageDTO
    );
}
