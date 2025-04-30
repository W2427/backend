package com.ose.tasks.api.material;

import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.FMaterialNestParameterPostDTO;
import com.ose.tasks.entity.material.FMaterialNestParameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

public interface FMaterialNestParameterAPI {

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialNestParameter> getParameters(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateParameters(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody FMaterialNestParameterPostDTO postDTO
    );
}
