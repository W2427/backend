package com.ose.auth.controller;

import com.ose.auth.api.BizCodeAPI;
import com.ose.auth.vo.BizCodeType;
import com.ose.controller.BaseController;
import com.ose.dto.BizCodeDTO;
import com.ose.response.JsonListResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "业务代码参照表")
@RestController
public class BizCodeController extends BaseController implements BizCodeAPI {

    @Override
    @Operation(description = "取得业务代码列表")
    @RequestMapping(
        method = GET,
        value = "/biz-codes",
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<BizCodeDTO> list(
        @RequestParam @Parameter(description = "业务代码类型") BizCodeType type
    ) {
        return new JsonListResponseBody<>(
            BizCodeDTO.list(type.getValueObjects())
        );
    }

}
