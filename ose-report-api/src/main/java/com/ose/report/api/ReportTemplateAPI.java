package com.ose.report.api;

import com.ose.dto.PageDTO;
import com.ose.report.dto.TemplateDTO;
import com.ose.report.entity.Template;
import com.ose.report.vo.Domain;
import com.ose.report.vo.Position;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 报表模板接口
 */
@RequestMapping(value = "/templates")
public interface ReportTemplateAPI {

    /**
     * 创建报表模板
     *
     * @param templateDTO 报表模板信息
     * @return 报表模板信息
     */
    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<Template> create(
        @RequestBody TemplateDTO templateDTO
    );

    /**
     * 查询模板（根据模板分类和显示位置）
     *
     * @param domain   模板分类
     * @param position 模板显示位置
     * @return 报表模板信息
     */
    @RequestMapping(method = GET)
    @ResponseStatus(OK)
    JsonListResponseBody<Template> search(
        @RequestParam(name = "domain") Domain domain,
        @RequestParam(name = "position") Position position,
        PageDTO page
    );
}
