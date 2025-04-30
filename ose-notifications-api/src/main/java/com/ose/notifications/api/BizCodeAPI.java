package com.ose.notifications.api;

import com.ose.notifications.dto.BizCodeDTO;
import com.ose.notifications.vo.BizCodeType;
import com.ose.response.JsonListResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        value = "/biz-code-types/{bizCodeType}/biz-codes",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BizCodeDTO> list(
        @PathVariable("bizCodeType") BizCodeType bizCodeType
    );

}
