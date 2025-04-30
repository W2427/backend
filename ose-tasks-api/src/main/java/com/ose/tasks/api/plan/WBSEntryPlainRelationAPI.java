package com.ose.tasks.api.plan;

import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface WBSEntryPlainRelationAPI {

    /**
     * 重新生成关系。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     */
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/regenerate-wbs-entry-plain-relation",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );


}
