package com.ose.materialspm.api;


import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ftjftj
 * SPM BOM 节点 匹配 百分比 FeignAPI
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface SpmMatchLnNodeAPI extends SpmMatchLnNodeFeignAPI {


}
