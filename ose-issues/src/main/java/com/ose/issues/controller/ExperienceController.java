package com.ose.issues.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.issues.api.ExperienceAPI;
import com.ose.issues.domain.model.service.ExperienceInterface;
import com.ose.issues.domain.model.service.IssueImportInterface;
import com.ose.issues.dto.*;
import com.ose.issues.entity.Experience;
import com.ose.issues.entity.ExperienceMail;
import com.ose.issues.entity.IssueImportTemplate;
import com.ose.issues.dto.ExperienceMailListDTO;
import com.ose.issues.vo.IssueType;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import static com.ose.constant.HttpResponseHeaders.DATA_REVISION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

@RestController
@Tag(name = "经验教训")
public class ExperienceController extends BaseController implements ExperienceAPI {

    private final ExperienceInterface experienceService;
    private final IssueImportInterface issueImportInterface;

    @Autowired
    public ExperienceController(
        ExperienceInterface experienceService,
        IssueImportInterface issueImportInterface
    ) {
        this.experienceService = experienceService;
        this.issueImportInterface = issueImportInterface;
    }

    @Override
    @Operation(description = "创建经验教训")
    @ResponseStatus(CREATED)
    @WithPrivilege
    public JsonObjectResponseBody<Experience> create(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Valid ExperienceCreateDTO experienceCreateDTO
    ) {
        return new JsonObjectResponseBody<>(
            experienceService.create(
                getContext().getOperator().getId(),
                orgId,
                projectId,
                experienceCreateDTO
            )
        );
    }

    @Override
    @Operation(description = "更新经验教训信息")
    @WithPrivilege
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "经验教训 ID") Long experienceId,
        @RequestBody @Valid ExperienceUpdateDTO experienceUpdateDTO
    ) {

        Experience experience = experienceService.update(
            getContext().getOperator().getId(),
            orgId,
            projectId,
            experienceId,
            experienceUpdateDTO
        );

        getResponse().setHeader(DATA_REVISION, "" + experience.getVersion());

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "删除经验教训")
    @WithPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "经验教训ID") Long experienceId
    ) {

        experienceService.delete(
            getContext().getOperator().getId(),
            orgId,
            projectId,
            experienceId
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "获取经验教训列表")
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<Experience> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        IssueCriteriaDTO experienceCriteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            getContext(),
            experienceService.search(
                orgId,
                projectId,
                experienceCriteriaDTO,
                pageDTO.toPageable()
            )
        );
    }

    @Override
    @Operation(description = "获取经验教训详情")
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<Experience> get(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "经验教训ID") Long experienceId
    ) {
        return new JsonObjectResponseBody<>(
            getContext(),
            experienceService.get(projectId, experienceId)
        );
    }

    @Override
    @Operation(description = "取得遗留问题导入文件")
    @WithPrivilege
    public JsonObjectResponseBody<IssueImportTemplate> getImportFile(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId
    ) {

        IssueImportTemplate template = issueImportInterface
            .getImportFile(projectId, IssueType.EXPERIENCE);

        if (template == null) {
            template = issueImportInterface.generateImportTemplate(
                getContext().getOperator().getId(),
                orgId, projectId, IssueType.EXPERIENCE
            );
        }

        return new JsonObjectResponseBody<>(template);
    }

    @Override
    @Operation(description = "导入经验教训（尚未实现）")
    @WithPrivilege
    @ResponseStatus(NOT_IMPLEMENTED)
    public JsonResponseBody importExperiences(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Valid IssueImportDTO issueImportDTO
    ) {
        return new JsonResponseBody();
    }


    @Override
    @Operation(description = "更新经验教训状态")
    @WithPrivilege
    public JsonResponseBody operate(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "经验教训 ID") Long experienceId,
        @RequestBody @Valid ExperienceStatusDTO experienceStatusDTO
    ) {

        Experience experience = experienceService.operate(
            getContext().getOperator().getId(),
            orgId,
            projectId,
            experienceId,
            experienceStatusDTO
        );

        getResponse().setHeader(DATA_REVISION, "" + experience.getVersion());

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "发送经验教训邮件")
    @WithPrivilege
    public JsonResponseBody mail(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "经验教训 ID") Long experienceId,
        @RequestBody @Valid ExperienceMailDTO experienceMailDTO
    ) {

        ExperienceMail experienceMail = experienceService.mail(
            getContext().getOperator().getId(),
            orgId,
            projectId,
            experienceId,
            experienceMailDTO
        );
        return new JsonResponseBody();

    }

    @Override
    @Operation(description = "经验教训邮件列表")
    @WithPrivilege
    public JsonListResponseBody getList(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        Integer size,
        Integer no
    ) {

        PageDTO pageDTO = new PageDTO();
        PageDTO.Page page = new PageDTO.Page();
        page.setSize(size);
        page.setNo(no);
        pageDTO.setPage(page);
        Page<ExperienceMailListDTO> experienceMailList = experienceService.getList(orgId, projectId, pageDTO);

        for (ExperienceMailListDTO dto : experienceMailList) {
            dto.setExperience(experienceService.get(projectId, dto.getExperienceId()));
        }
        return new JsonListResponseBody<>(
            getContext(),
            experienceMailList
        );
    }

    @Override
    @Operation(description = "获取经验教训详情列表")
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<Experience> getExperienceList(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "经验教训ID") Long experienceParentId
    ) {
        return new JsonListResponseBody<>(
            getContext(),
            experienceService.getExperienceList(orgId, projectId, experienceParentId)
        );
    }

}
