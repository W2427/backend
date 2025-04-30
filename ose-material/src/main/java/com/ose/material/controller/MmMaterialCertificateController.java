package com.ose.material.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.material.api.MmMaterialCertificateAPI;
import com.ose.material.domain.model.service.MmMaterialCertificateInterface;
import com.ose.material.dto.MmMaterialCertificateDTO;
import com.ose.material.dto.MmMaterialCertificateFileCreateDTO;
import com.ose.material.dto.MmMaterialCertificateSearchDTO;
import com.ose.material.dto.MmPurchasePackageFileCreateDTO;
import com.ose.material.entity.MmMaterialCertificate;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "材料证书管理接口")
@RestController
public class MmMaterialCertificateController extends BaseController implements MmMaterialCertificateAPI {

    // 材质管理接口
    private MmMaterialCertificateInterface mmMaterialCertificateService;

    /**
     * 构造方法。
     */
    @Autowired
    public MmMaterialCertificateController(
        MmMaterialCertificateInterface mmMaterialCertificateService) {
        this.mmMaterialCertificateService = mmMaterialCertificateService;
    }

    /**
     * 查询材料证书管理接口。
     *
     * @param orgId 所属组织 ID
     * @param projectId 所属项目 ID
     * @param searchBaseDTO   搜索查询分页参数
     * @return
     */
    @Operation(description = "查询材料证书管理接口")
    @RequestMapping(
        method = GET,
        value = "/orgId/{orgId}/projects/{projectId}/material-certificate",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonListResponseBody<MmMaterialCertificate> search(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "所属项目 ID") Long projectId,
        MmMaterialCertificateSearchDTO searchBaseDTO
    ) {
        return new JsonListResponseBody<>(
            mmMaterialCertificateService.search(
                orgId,
                projectId,
                searchBaseDTO
            )
        );
    }

    /**
     * 添加材料证书管理接口。
     *
     */
    @Operation(description = "添加材料证书管理接口")
    @RequestMapping(
        method = POST,
        value = "/orgId/{orgId}/projects/{projectId}/material-certificate",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody create(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "所属项目 ID") Long projectId,
        @RequestBody MmMaterialCertificateDTO materialCertificateDTO
    ) {
        mmMaterialCertificateService.create(orgId, projectId, materialCertificateDTO, getContext());
        return new JsonObjectResponseBody();
    }

    /**
     * 查询材料证书详情管理接口。
     *
     */
    @Operation(description = "查询材料证书详情管理接口")
    @RequestMapping(
        method = GET,
        value = "/orgId/{orgId}/projects/{projectId}/material-certificate/{materialCertificateId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<MmMaterialCertificate> detail(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "所属项目 ID") Long projectId,
        @PathVariable @Parameter(description = "证书 ID") Long materialCertificateId
    ) {
        return new JsonObjectResponseBody<>(
            mmMaterialCertificateService.detail(orgId, projectId, materialCertificateId)
        );
    }

    /**
     * 删除材料证书管理接口。
     *
     */
    @Operation(description = "删除材料证书管理接口")
    @RequestMapping(
        method = DELETE,
        value = "/orgId/{orgId}/projects/{projectId}/material-certificate/{materialCertificateId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody delete(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "所属项目 ID") Long projectId,
        @PathVariable @Parameter(description = "证书 ID") Long materialCertificateId
    ) {
        mmMaterialCertificateService.delete(orgId, projectId, materialCertificateId, getContext());
        return new JsonObjectResponseBody();
    }

    @Override
    @Operation(
        description = "创建材料证书文件"
    )
    @RequestMapping(
        method = POST,
        value = "/orgId/{orgId}/projects/{projectId}/material-certificate/{materialCertificateId}/file",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody uploadFile(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "项目id") Long materialCertificateId,
        @RequestBody @Parameter(description = "采购包信息") MmMaterialCertificateFileCreateDTO mmMaterialCertificateFileCreateDTO) {
        mmMaterialCertificateService.uploadFile(orgId, projectId, materialCertificateId, mmMaterialCertificateFileCreateDTO, getContext());
        return new JsonObjectResponseBody();
    }

}
