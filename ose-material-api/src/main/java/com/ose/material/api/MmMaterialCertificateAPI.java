package com.ose.material.api;

import com.ose.material.dto.MmMaterialCertificateDTO;
import com.ose.material.dto.MmMaterialCertificateFileCreateDTO;
import com.ose.material.dto.MmMaterialCertificateSearchDTO;
import com.ose.material.entity.MmMaterialCertificate;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;
public interface MmMaterialCertificateAPI {

    /**
     * 材料证书管理接口。
     */
    @RequestMapping(
        method = GET,
        value = "/orgId/{orgId}/projects/{projectId}/material-certificate",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<MmMaterialCertificate> search(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "所属项目 ID") Long projectId,
        MmMaterialCertificateSearchDTO searchDTO
    );

    /**
     * 添加材料证书管理接口。
     */
    @RequestMapping(
        method = POST,
        value = "/orgId/{orgId}/projects/{projectId}/material-certificate",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody create(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "所属项目 ID") Long projectId,
        @RequestBody MmMaterialCertificateDTO mmMaterialCertificateDTO
    );

    /**
     * 查询材料证书详情管理接口。
     */
    @RequestMapping(
        method = GET,
        value = "/orgId/{orgId}/projects/{projectId}/material-certificate/{mmMaterialCertificateId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<MmMaterialCertificate> detail(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "所属项目 ID") Long projectId,
        @PathVariable @Parameter(description = "证书 ID") Long mmMaterialCertificateId
    );

    /**
     * 删除材料证书管理接口。
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgId/{orgId}/projects/{projectId}/material-certificate/{mmMaterialCertificateId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody delete(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "所属项目 ID") Long projectId,
        @PathVariable @Parameter(description = "证书 ID") Long mmMaterialCertificateId
    );

    @RequestMapping(
        method = POST,
        value = "/orgId/{orgId}/projects/{projectId}/material-certificate/{mmMaterialCertificateId}/file",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody uploadFile(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "材料证书id") Long mmMaterialCertificateId,
        @RequestBody @Parameter(description = "材料证书信息") MmMaterialCertificateFileCreateDTO mmMaterialCertificateFileCreateDTO);

}
