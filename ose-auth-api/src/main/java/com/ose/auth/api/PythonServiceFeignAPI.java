package com.ose.auth.api;

import com.ose.auth.dto.PythonQueryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(
    name = "ose-auth",
//    url = "${feign.client.python-service.url}"
    url = "http://127.0.0.1:8900"
)
public interface PythonServiceFeignAPI {

    @PostMapping(
        value = "/overview",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    Object overview(@RequestBody PythonQueryDTO queryDTO);
}
