package com.ose.tasks.controller.bpm.drawing;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.drawing.SplitPdfPluginAPI;
import com.ose.tasks.domain.model.service.drawing.SplitPDFHistoryInterface;
import com.ose.tasks.dto.drawing.DrawingCriteriaPageDTO;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.entity.drawing.SplitPDFHistory;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 拆分PDF历史接口。
 *
 * @auth DengMing
 * @date 2021/8/3 11:46
 */
@Tag(name = "拆分PDF历史接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class SplitPdfHistoryController extends BaseController implements SplitPdfPluginAPI {

    private final SplitPDFHistoryInterface splitPDFHistoryService;

    @Autowired
    public SplitPdfHistoryController(SplitPDFHistoryInterface splitPDFHistoryService) {
        this.splitPDFHistoryService = splitPDFHistoryService;
    }

    @RequestMapping(
        method = GET,
        value = "split-pdf-plugin"
    )
    @WithPrivilege
    @Override
    public JsonListResponseBody<SplitPDFHistory> search(@PathVariable("orgId") Long orgId,
                                                        @PathVariable("projectId") Long projectId,
                                                        DrawingCriteriaPageDTO criteriaDTO) {
        return new JsonListResponseBody<>(
            splitPDFHistoryService.search(orgId, projectId, criteriaDTO)
        );
    }


    @RequestMapping(
        method = POST,
        value = "split-pdf-plugin"
    )
    @Operation(description = "拆分图纸插件")
    @WithPrivilege
    @Override
    public JsonResponseBody splitPDF(@PathVariable("orgId") Long orgId,
                                     @PathVariable("projectId") Long projectId,
                                     @RequestBody DrawingUploadDTO uploadDTO
    ){
        splitPDFHistoryService.splitPDF(orgId,projectId,uploadDTO,getContext());
        return new JsonResponseBody();
    }

    @RequestMapping(
        method = POST,
        value = "split-pdf-plugin/download-zip"
    )
    @Operation(description = "按条件下载子图纸zip包")
    @WithPrivilege
    @Override
    public JsonResponseBody startZip(@PathVariable("orgId") Long orgId,
                                     @PathVariable("projectId") Long projectId,
                                     @RequestBody DrawingCriteriaPageDTO criteriaDTO) {
        splitPDFHistoryService.splitPdfZip(orgId, projectId, criteriaDTO, getContext());
        return new JsonResponseBody();
    }
}
