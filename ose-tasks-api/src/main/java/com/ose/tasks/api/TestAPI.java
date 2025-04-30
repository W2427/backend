package com.ose.tasks.api;

import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 测试线程池接口。
 */
public interface TestAPI {

    /**
     * 测试线程池接口。
     */
    @RequestMapping(
        method = GET,
        value = "/test",
        consumes = ALL_VALUE
    )
    JsonResponseBody test();

    /**
     * 测试线程池接口。
     */
    @RequestMapping(
        method = GET,
        value = "/test",
        consumes = ALL_VALUE
    )
    JsonResponseBody test2();
}
