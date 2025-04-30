package com.ose.tasks.api.material;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.ExportFileDTO;
import com.ose.tasks.dto.material.FCompanySurplusMaterialCriteriaDTO;
import com.ose.tasks.dto.material.FCompanySurplusMaterialPatchToRecieveListDTO;
import com.ose.tasks.dto.material.FCompanySurplusMaterialReceiveDTO;
import com.ose.tasks.entity.material.FCompanySurplusMaterialDetailEntity;
import com.ose.tasks.entity.material.FCompanySurplusMaterialsEntity;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 施工单位余料库接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface FCompanySurplusMaterialAPI {

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FCompanySurplusMaterialsEntity> search(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "companyId") String companyId,
        FCompanySurplusMaterialCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FCompanySurplusMaterialDetailEntity> searchDetail(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "companyId") String companyId,
        FCompanySurplusMaterialCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ExportFileDTO> exportSurplusMaterials(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "companyId") String companyId,
        FCompanySurplusMaterialCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    JsonListResponseBody<FCompanySurplusMaterialDetailEntity> getNotReceivedSurplusMaterialDetail(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "companyId") String companyId,
        FCompanySurplusMaterialCriteriaDTO criteriaDTO

    );

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    JsonResponseBody patchNotReceivedSurplusMaterialToReceive(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "companyId") String companyId,
        @RequestBody FCompanySurplusMaterialPatchToRecieveListDTO fCompanySurplusMaterialPatchToRecieveListDTO
    );

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FCompanySurplusMaterialDetailEntity> getSurplusMaterialDetail(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "companyId") String companyId,
        @PathVariable @Parameter(description = "surplusMaterialId") Long surplusMaterialId,
        PageDTO pageDTO
    );

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    JsonResponseBody surplusConfirmAdd(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @RequestBody FCompanySurplusMaterialReceiveDTO fCompanySurplusMaterialReceiveDTO
    );

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    JsonResponseBody patchReceivedNotLockedSurplusMaterialToNotReceiveLocked(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "companyId") String companyId,
        @RequestBody FCompanySurplusMaterialPatchToRecieveListDTO fCompanySurplusMaterialPatchToRecieveListDTO
    );
}
