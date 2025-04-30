package com.ose.tasks.controller.material;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.material.StructureEntityQrCodeAPI;
import com.ose.tasks.domain.model.service.material.StructureEntityQrCodeInterface;
import com.ose.tasks.dto.material.StructureEntityQrCodeCriteriaDTO;
import com.ose.tasks.entity.material.StructureEntityQrCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Api(description = "结构实体二维码查询接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/structure-qrcode")
public class StructureEntityQrCodeReportController extends BaseController implements StructureEntityQrCodeAPI {

    private final StructureEntityQrCodeInterface structureEntityQrCodeService;

    /**
     * 构造方法
     */
    @Autowired
    public StructureEntityQrCodeReportController(
        StructureEntityQrCodeInterface structureEntityQrCodeService) {
        this.structureEntityQrCodeService = structureEntityQrCodeService;
    }


    @Override
    @ApiOperation(
        value = "结构实体二维码查询接口",
        notes = "结构实体二维码查询接口"
    )
    @RequestMapping(
        method = GET,
        value = ""
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<StructureEntityQrCode> search(
        @PathVariable @ApiParam("orgId") Long orgId,
        @PathVariable @ApiParam("projectId") Long projectId,
        StructureEntityQrCodeCriteriaDTO criteriaDTO) {
        return new JsonListResponseBody<>(
            getContext(),
            structureEntityQrCodeService.search(orgId, projectId, criteriaDTO)
        );
    }

    @Override
    @ApiOperation(value = "更新结构两件二维码旧数据", notes = "更新结构两件二维码旧数据。")
    @WithPrivilege
    @RequestMapping(method = POST, value = "generate")
    @ResponseStatus(CREATED)
    public JsonResponseBody create(@PathVariable @ApiParam("组织id") Long orgId,
                                   @PathVariable @ApiParam("项目id") Long projectId) {
        ContextDTO context = getContext();
        structureEntityQrCodeService.generate(orgId, projectId, context);
        return new JsonResponseBody();

    }

}
