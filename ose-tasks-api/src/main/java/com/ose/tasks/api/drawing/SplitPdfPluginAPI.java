package com.ose.tasks.api.drawing;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.drawing.DrawingCriteriaPageDTO;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.entity.drawing.SplitPDFHistory;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 。
 *
 * @auth DengMing
 * @date 2021/8/3 11:41
 */

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface SplitPdfPluginAPI {

    @RequestMapping(
        method = GET,
        value = "split-pdf-plugin"
    )
    @Operation(description = "获取子图纸拆分历史")
    JsonListResponseBody<SplitPDFHistory> search(@PathVariable("orgId") Long orgId,
                                                 @PathVariable("projectId") Long projectId,
                                                 @RequestBody DrawingCriteriaPageDTO criteriaDTO);

    @RequestMapping(
        method = POST,
        value = "split-pdf-plugin"
    )
    @Operation(description = "拆分上传图纸")
     JsonResponseBody splitPDF(@PathVariable("orgId") Long orgId,
                               @PathVariable("projectId") Long projectId,
                               @RequestBody DrawingUploadDTO uploadDTO
    );

    @RequestMapping(
        method = POST,
        value = "split-pdf-plugin/download-zip"
    )
    @Operation(description = "按条件下载子图纸zip包")
    JsonResponseBody startZip(@PathVariable("orgId") Long orgId,
                              @PathVariable("projectId") Long projectId,
                              @RequestBody DrawingCriteriaPageDTO criteriaDTO
    );
}
