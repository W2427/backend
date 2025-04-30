package com.ose.test.api;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 查询接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface ReturnReceiptAPI extends ReturnReceiptFeignAPI {

}
