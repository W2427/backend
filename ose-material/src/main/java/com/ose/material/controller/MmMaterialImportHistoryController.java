package com.ose.material.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.material.api.MmMaterialImportHistoryAPI;
import com.ose.material.domain.model.service.MmMaterialImportHistoryInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.MmImportBatchTask;
import com.ose.response.JsonListResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "材料导入历史")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/material-import")
public class MmMaterialImportHistoryController extends BaseController implements MmMaterialImportHistoryAPI {

    /**
     * 材料导入历史接口服务
     */
    private final MmMaterialImportHistoryInterface mmMaterialImportHistoryService;

    /**
     * 构造方法
     *
     * @param mmMaterialImportHistoryService 材料导入历史服务
     */
    @Autowired
    public MmMaterialImportHistoryController(MmMaterialImportHistoryInterface mmMaterialImportHistoryService) {
        this.mmMaterialImportHistoryService = mmMaterialImportHistoryService;
    }


    @Override
    @Operation(
        summary = "导入记录",
        description = "导入记录。"
    )
    @RequestMapping(
        method = GET,
        value = "/{entityId}"
    )
    @SetUserInfo
    @WithPrivilege
    public JsonListResponseBody<MmImportBatchTask> search(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable("entityId") Long entityId,
        MmMaterialImportHistorySearchDTO mmMaterialImportHistorySearchDTO
    ) {
        return new JsonListResponseBody<>(mmMaterialImportHistoryService.search(orgId, projectId, entityId, mmMaterialImportHistorySearchDTO));
    }
}
