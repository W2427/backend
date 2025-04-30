package com.ose.report.api;

import com.ose.report.dto.MaterialReturnStockDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "ose-report", contextId = "mmReturnFeign")
public interface MaterialReturnStockAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/material-return-stock-lists"
    )
    JsonObjectResponseBody<ReportHistory> generateMaterialReturnStock(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody MaterialReturnStockDTO reportDTO
    );

}
