package com.ose.tasks.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.issues.dto.ExperienceMailDTO;
import com.ose.issues.dto.ExperienceMailListDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.ExperienceMailAPI;
import com.ose.tasks.domain.model.service.ExperienceMailInterface;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@Tag(name = "经验教训")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class ExperienceMailController extends BaseController implements ExperienceMailAPI {


    private ExperienceMailInterface experienceMailService;

    /**
     * 构造方法。
     */
    @Autowired
    public ExperienceMailController(
        ExperienceMailInterface experienceMailService) {
        this.experienceMailService = experienceMailService;
    }

    @RequestMapping(method = POST, value = "experiences/{experienceId}/experience-mail-send")
    @Operation(summary = "发送经验教训邮件", description = "发送经验教训邮件")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody mail(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "经验教训 ID") Long experienceId,
        @RequestBody @Valid ExperienceMailDTO experienceMailDTO
    ) {
        experienceMailService.mail(
            getContext().getOperator().getId(),
            orgId,
            projectId,
            experienceId,
            experienceMailDTO
        );
        return new JsonResponseBody();
    }

    @RequestMapping(method = GET, value = "experiences/mail")
    @Operation(summary = "经验教训邮件列表", description = "经验教训邮件列表")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<ExperienceMailListDTO> getList(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<ExperienceMailListDTO>(
            getContext(),
            experienceMailService.getList(orgId, projectId, pageDTO)
        );
    }
}
