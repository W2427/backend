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
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 余料套料管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface FMaterialSurplusNestAPI {

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialSurplusNest> create(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody FMaterialSurplusNestCreateDTO dto);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialSurplusNest> search(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestParam PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialSurplusNest> detail(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msnId") Long msnId);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialSurplusNest> update(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msnId") Long msnId,
        @RequestBody FMaterialNestPatchDTO dto);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialSurplusNestSubstitute> searchSubstituteMaterials(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msnId") Long msnId,
        FMaterialNestMaterialSubstituteCriteriaDTO criteriaDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialSurplusNestSubstitute> substituteMaterialsDetail(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msnId") Long msnId,
        @PathVariable @Parameter(description = "fMaterialSurplusNestSubstituteId") Long fMaterialSurplusNestSubstituteId,
        PageDTO pageDTO);

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody addSubstituteMaterials(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msnId") Long msnId,
        @RequestBody FMaterialNestMaterialSubstituteDTO dto);

    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delSubstituteMaterials(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msnId") Long msnId,
        @PathVariable @Parameter(description = "msnId") Long id);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialSurplusNestCutTotal> getNestCutTotals(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msnId") Long msnId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialSurplusNestCutTotal> getNestCutTotalDetail(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msnId") Long msnId,
        @PathVariable @Parameter(description = "fMaterialSurplusNestCutTotalId") Long fMaterialSurplusNestCutTotalId
    );


    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialSurplusNestCutItem> getNestCutItem(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msnId") Long msnId,
        @PathVariable @Parameter(description = "fMaterialSurplusNestCutTotalId") Long fMaterialSurplusNestCutTotalId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialSurplusNestMaterialTotal> nestMaterials(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msnId") Long msnId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialSurplusNestMaterialTotal> nestMaterialsDetail(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msnId") Long msnId,
        @PathVariable @Parameter(description = "fMaterialSurplusNestMaterialTotalId") Long fMaterialSurplusNestMaterialTotalId
    );

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialSurplusNestMaterialItem> nestMaterialsItem(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msnId") Long msnId,
        @PathVariable @Parameter(description = "fMaterialSurplusNestMaterialTotalId") Long fMaterialSurplusNestMaterialTotalId,
        PageDTO pageDTO);


    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody nesting(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msnId") Long msnId);

    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody removeNestResult(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msnId") Long msnId);

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody materialLocked(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msnId") Long msnId);

    @RequestMapping(
        method = POST,
        value = "/material-surplus-nest/{fMaterialSurplusReceiptId}/surplus-nest-parameter",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody surplusNestParameter(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fMaterialSurplusReceiptId") Long fMaterialSurplusReceiptId,
        @RequestBody FMaterialNestParameterPostDTO postDTO
    );

}
