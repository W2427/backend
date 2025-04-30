package com.ose.prints.controller;

import com.ose.controller.BaseController;
import com.ose.prints.api.DemoAPI;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Demo")
@RestController
public class DemoController extends BaseController implements DemoAPI {

    @Override
    @Operation(description = "DEMO")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/demo"
    )
    public JsonResponseBody getList(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId
    ) {
        System.out.println("OK");
        return new JsonResponseBody();
    }

}
