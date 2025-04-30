package com.ose.test.controller;

import com.ose.controller.BaseController;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "Demo")
@RestController
public class DemoController extends BaseController {

    @Operation(description = "DEMO")
    @RequestMapping(
        method = GET,
        value = "/demo"
    )
    public JsonResponseBody getList(
    ) {
        System.out.println("OK");
        return new JsonResponseBody();
    }

}
