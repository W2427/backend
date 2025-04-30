package com.ose.material.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.controller.BaseController;
import com.ose.material.api.MmSurplusMaterialAPI;
import com.ose.material.domain.model.service.MmSurplusMaterialInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.MmSurplusMaterialEntity;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "查找余料库")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/surplus-material")
public class MmSurplusMaterialController extends BaseController implements MmSurplusMaterialAPI {

    /**
     * 余料库接口服务
     */
    private final MmSurplusMaterialInterface mmSurplusMaterialService;


    /**
     * 构造方法
     */
    @Autowired
    public MmSurplusMaterialController(MmSurplusMaterialInterface mmSurplusMaterialService) {
        this.mmSurplusMaterialService = mmSurplusMaterialService;
    }

    /**
     * 查找余料库
     *
     * @return 查找余料库
     */
    @Override
    @Operation(
        summary = "查找余料库",
        description = "查找余料库。"
    )
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    @SetUserInfo
    public JsonListResponseBody<MmSurplusMaterialEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmSurplusMaterialSearchDTO mmSurplusMaterialSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            mmSurplusMaterialService.search(
                orgId,
                projectId,
                mmSurplusMaterialSearchDTO
            )
        );
    }

    /**
     * 创建余料
     *
     * @return 创建余料
     */
    @Override
    @Operation(
        summary = "创建余料",
        description = "创建余料。"
    )
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(OK)
    @SetUserInfo
    public JsonObjectResponseBody<MmSurplusMaterialEntity> create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody MmSurplusMaterialCreateDTO mmSurplusMaterialCreateDTO) {

        return new JsonObjectResponseBody<>(
            getContext(),
            mmSurplusMaterialService.create(
                orgId,
                projectId,
                mmSurplusMaterialCreateDTO,
                getContext()
            )
        );
    }

    /**
     * 更新余料
     *
     * @return 更新余料
     */
    @Override
    @Operation(
        summary = "更新余料",
        description = "更新余料。"
    )
    @RequestMapping(
        method = POST,
        value = "/{surplusMaterialId}"
    )
    @ResponseStatus(OK)
    @SetUserInfo
    public JsonObjectResponseBody<MmSurplusMaterialEntity> update(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "余料单ID") Long surplusMaterialId,
        @RequestBody MmSurplusMaterialCreateDTO mmSurplusMaterialCreateDTO) {

        return new JsonObjectResponseBody<>(
            getContext(),
            mmSurplusMaterialService.update(
                orgId,
                projectId,
                surplusMaterialId,
                mmSurplusMaterialCreateDTO,
                getContext()
            )
        );
    }

    /**
     * task服务调用，查找余料信息
     *
     * @return 创建余料
     */
    @Override
    @Operation(
        summary = "task服务调用，查找余料信息",
        description = "task服务调用，查找余料信息。"
    )
    @RequestMapping(
        method = POST,
        value = "/information"
    )
    @ResponseStatus(OK)
    @SetUserInfo
    public JsonObjectResponseBody<MmSurplusMaterialEntity> searchSurplusMaterial(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody MmSurplusMaterialSearchDTO mmSurplusMaterialSearchDTO) {

        return new JsonObjectResponseBody<>(
            getContext(),
            mmSurplusMaterialService.searchSurplusMaterial(
                orgId,
                projectId,
                mmSurplusMaterialSearchDTO
            )
        );
    }
}

