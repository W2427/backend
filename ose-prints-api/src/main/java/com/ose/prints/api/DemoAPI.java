package com.ose.prints.api;

import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 二维码打印接口。
 */
public interface DemoAPI {

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/processes"
    )
    JsonResponseBody getList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

}
