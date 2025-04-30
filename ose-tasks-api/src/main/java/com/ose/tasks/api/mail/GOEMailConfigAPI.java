package com.ose.tasks.api.mail;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.mail.MailCreateDTO;
import com.ose.tasks.dto.taskpackage.TaskPackageCategoryCriteriaDTO;
import com.ose.tasks.entity.bpm.GOEMailConfig;
import com.ose.tasks.entity.taskpackage.TaskPackageCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

public interface GOEMailConfigAPI {
    @Operation(
        summary = "查询邮件配置",
        description = "查询邮件配置"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/goe-mail-config/{actInstId}/{taskDefKey}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<GOEMailConfig> searchList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("actInstId") Long actInstId,
        @PathVariable("taskDefKey") String taskDefKey
    );
}
