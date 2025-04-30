package com.ose.auth.api;

import com.ose.auth.vo.BizCodeType;
import com.ose.dto.BizCodeDTO;
import com.ose.response.JsonListResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 业务代码接口。
 */
public interface BizCodeAPI {

    /**
     * 取得业务代码列表。
     *
     * @return 业务代码列表
     */
    @RequestMapping(
        method = GET,
        value = "/biz-codes",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BizCodeDTO> list(
        @RequestParam("type") BizCodeType type
    );

}
