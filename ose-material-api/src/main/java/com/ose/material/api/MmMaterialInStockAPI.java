package com.ose.material.api;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/material-stock")
public interface MmMaterialInStockAPI extends MmMaterialInStockFeignAPI{

}
