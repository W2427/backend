package com.ose.report.controller;

import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.report.api.ReportTemplateAPI;
import com.ose.report.domain.service.ReportTemplateInterface;
import com.ose.report.domain.service.impl.ReportTemplateService;
import com.ose.report.dto.TemplateDTO;
import com.ose.report.entity.Template;
import com.ose.report.vo.Domain;
import com.ose.report.vo.Position;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "报表模板接口")
@RestController
@RequestMapping(value = "/templates")
public class ReportTemplateController extends BaseController implements ReportTemplateAPI {

    /**
     * 报表模板服务
     */
    private final ReportTemplateInterface reportTemplateService;

    /**
     * 构造方法
     *
     * @param reportTemplateService 报表模板服务
     */
    @Autowired
    public ReportTemplateController(ReportTemplateService reportTemplateService) {
        this.reportTemplateService = reportTemplateService;
    }

    /**
     * 创建报表模板
     *
     * @param templateDTO 报表模板信息
     * @return 报表模板信息
     */
    @Override
    @Operation(
        summary = "创建报表模板",
        description = "根据模板信息，创建报表模板。"
    )
    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<Template> create(
        @RequestBody @Parameter(description = "报表模板信息") TemplateDTO templateDTO) {

        return new JsonObjectResponseBody<>(
            getContext(),
            reportTemplateService.create(templateDTO)
        );
    }

    /**
     * 查询模板（根据模板分类和显示位置）
     *
     * @param domain   模板分类
     * @param position 模板显示位置
     * @return 报表模板信息
     */
    @Override
    @Operation(
        summary = "查询报表模板",
        description = "根据模板分类和显示位置，查询报表模板。"
    )
    @RequestMapping(method = GET)
    @ResponseStatus(OK)
    public JsonListResponseBody<Template> search(
        @RequestParam(name = "domain") @Parameter(description = "模板分类") Domain domain,
        @RequestParam(name = "position") @Parameter(description = "模板显示位置") Position position,
        PageDTO page) {

        return new JsonListResponseBody<>(
            getContext(),
            reportTemplateService.search(domain, position, page)
        );
    }
}
