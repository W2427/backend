package com.ose.materialspm.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.materialspm.api.MCompaniesAPI;
import com.ose.materialspm.domain.model.service.MCompaniesInterface;
import com.ose.materialspm.entity.MCompaniesEntity;
import com.ose.response.JsonListResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "SPM施工单位查询接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/spm-projects/{projId}")
public class MCompaniesController extends BaseController implements MCompaniesAPI {

    private final MCompaniesInterface mCompaniesService;

    /**
     * 构造方法
     *
     * @param mCompaniesService
     */
    @Autowired
    public MCompaniesController(MCompaniesInterface mCompaniesService) {
        this.mCompaniesService = mCompaniesService;
    }

    @Override
    @Operation(
        summary = "查询施工单位列表",
        description = "查询施工单位列表"
    )
    @RequestMapping(
        method = GET,
        value = "m-companies",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<MCompaniesEntity> getMCompanies(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projId") String projId) {

        return new JsonListResponseBody<>(
            getContext(),
            mCompaniesService.getMCompanies(projId)
        );
    }

}
