package com.ose.tasks.api.material;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.*;
import com.ose.tasks.entity.material.*;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface FMaterialNestAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody createNestReceipt(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody FMaterialNestReceiptDTO dto
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialNestReceipt> nestReceiptList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO pageDTO,
        FMaterialNestReceiptCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialNestReceipt> getNestReceipt(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId
    );

    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateNestReceipt(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "套料单ID") Long fmnrId,
        @RequestBody FMaterialNestPatchDTO fMaterialNestPatchDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/nest-cut-totals",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialNestCutTotal> getNestCutTotals(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId,
        PageDTO pageDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/nest-cut-totals/{fmnCutTotalId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialNestCutTotal> getNestCutTotal(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId,
        @PathVariable("fmnCutTotalId") Long fmnCutTotalId
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/nest-cut-totals/{fmnCutTotalId}/items",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialNestCutItem> getNestCutItems(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId,
        @PathVariable("fmnCutTotalId") Long fmnCutTotalId,
        PageDTO pageDTO
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/nest-cut-totals/{fmnCutTotalId}/nesting",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody nesting(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId,
        @PathVariable("fmnCutTotalId") Long fmnCutTotalId
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/nest-materials",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialNestMaterialTotal> nestMaterials(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId,
        PageDTO pageDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/nest-materials/{fmnmId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialNestMaterialTotal> nestMaterial(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId,
        @PathVariable("fmnmId") Long fmnmId
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/nest-materials/{fmnmId}/items",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialNestMaterialItem> nestMaterialItems(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId,
        @PathVariable("fmnmId") Long fmnmId,
        PageDTO pageDTO
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/nest-parameter",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody nestParameter(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId,
        @RequestBody FMaterialNestParameterPostDTO postDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/nest-materials/{fmnmId}/items/{fmnmItemId}/cut-items",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialNestCutItem> nestMaterialItemCutItems(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId,
        @PathVariable("fmnmId") Long fmnmId,
        @PathVariable("fmnmId") Long fmnmItemId,
        PageDTO pageDTO
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/nesting",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody nesting(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId
    );

    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/nesting",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody removeNestResult(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/material-locked",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody materialLocked(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/substitute-materials",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialNestMaterialSubstitute> searchSubstituteMaterials(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId,
        FMaterialNestMaterialSubstituteCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/substitute-materials",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody addSubstituteMaterials(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId,
        @RequestBody FMaterialNestMaterialSubstituteDTO dto
    );

    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/substitute-materials/{id}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delSubstituteMaterials(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId,
        @PathVariable("id") Long id
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nest-receipts/{fmnrId}/substitute-materials/{id}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialNestMaterialSubstitute> substituteMaterials(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fmnrId") Long fmnrId,
        @PathVariable("id") Long id
    );
}
