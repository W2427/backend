package com.ose.materialspm.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.ose.materialspm.api.MWareHouseFeignAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.materialspm.domain.model.service.MWareHouseInterface;
import com.ose.materialspm.entity.MWareHouseEntity;
import com.ose.response.JsonListResponseBody;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.Date;

@Tag(name = "SPM仓库查询接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public class MWareHouseController extends BaseController implements MWareHouseFeignAPI {

    private final MWareHouseInterface mWareHouseService;

    /**
     * 构造方法
     *
     * @param mWareHouseService 仓库查询服务
     */
    @Autowired
    public MWareHouseController(MWareHouseInterface mWareHouseService) {
        this.mWareHouseService = mWareHouseService;
    }

    @Override
    @Operation(
        summary = "查询仓库列表",
        description = "查询仓库列表"
    )
    @RequestMapping(
        method = GET,
        value = "/spm-projects/{projId}/m-ware-houses",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<MWareHouseEntity> getMWareHouse(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projId") String projId) {

        System.out.println("进入接口时间："+ new Date());
        return new JsonListResponseBody<>(
            getContext(),
            mWareHouseService.getMWareHouse(projId)
        );
    }

    @Override
    @Operation(
        summary = "查找SPM过量库",
        description = "查找SPM过量库"
    )
    @RequestMapping(
        method = GET,
        value = "/m-overdose-ware-houses",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<MWareHouseEntity> getOverdoseWareHouses(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestParam @Parameter(description = "companyId") Integer companyId) {

        return new JsonListResponseBody<>(
            getContext(),
            mWareHouseService.getOverdoseWareHouses(companyId)
        );
    }
}
