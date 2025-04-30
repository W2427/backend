package com.ose.tasks.controller.material;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.api.material.FMaterialStructureNestProgramAPI;
import com.ose.tasks.domain.model.service.material.FMaterialStructureNestProgramInterface;
import com.ose.tasks.entity.material.FMaterialStructureNestProgram;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Api(description = "结构套料排版接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class FMaterialStructureNestProgramController extends BaseController implements FMaterialStructureNestProgramAPI {

    private final FMaterialStructureNestProgramInterface fMaterialStructureNestProgramService;

    /**
     * 构造方法
     */
    @Autowired
    public FMaterialStructureNestProgramController(
        FMaterialStructureNestProgramInterface fMaterialStructureNestProgramService
    ) {
        this.fMaterialStructureNestProgramService = fMaterialStructureNestProgramService;
    }

    /**
     * 结构套料排版接口列表。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param pageDTO   分页参数
     * @return
     */
    @RequestMapping(method = GET, value = "material-structure-nest/{fMaterialStructureNestId}/program")
    @ApiOperation(value = "结构套料排版接口列表", notes = "结构套料排版接口列表。")
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<FMaterialStructureNestProgram> search(@PathVariable @ApiParam("orgId") Long orgId,
                                                                      @PathVariable @ApiParam("项目Id") Long projectId,
                                                                      @PathVariable @ApiParam("结构套料方案id") Long fMaterialStructureNestId,
                                                                      PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            fMaterialStructureNestProgramService.search(
                orgId,
                projectId,
                fMaterialStructureNestId,
                pageDTO
            )
        ).setIncluded(fMaterialStructureNestProgramService);
    }

}
