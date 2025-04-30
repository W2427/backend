package com.ose.materialspm.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.materialspm.api.MLocationAPI;
import com.ose.materialspm.domain.model.service.MLocationInterface;
import com.ose.materialspm.entity.MLocationEntity;
import com.ose.response.JsonListResponseBody;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "SPM库位查询接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/spm-projects/{projId}")
public class MLocationController extends BaseController implements MLocationAPI {

    private final MLocationInterface mLocationService;

    /**
     * 构造方法
     *
     * @param mLocationService 货位查询服务
     */
    @Autowired
    public MLocationController(MLocationInterface mLocationService) {
        this.mLocationService = mLocationService;
    }

    @Override
    @Operation(
        summary = "查询库位列表",
        description = "查询库位列表"
    )
    @RequestMapping(
        method = GET,
        value = "m-locations",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<MLocationEntity> getMLocation(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projId") String projId) {

        return new JsonListResponseBody<>(
            getContext(),
            mLocationService.getMLocation(projId)
        );
    }

}
