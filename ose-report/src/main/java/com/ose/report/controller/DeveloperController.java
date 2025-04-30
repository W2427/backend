package com.ose.report.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@ApiIgnore
@RestController
@RequestMapping(value = "/developer")
public class DeveloperController {

    private static final String CONTENT_TYPE = "text/html;charset=UTF-8";

    private static final Configuration configuration
        = new Configuration(new Version("2.3.28"));

    private static Template template;

    static {

        configuration.setClassForTemplateLoading(
            DeveloperController.class,
            "/templates"
        );

        configuration.setDefaultEncoding("UTF-8");

        try {
            template = configuration.getTemplate("developer.ftl");
        } catch (IOException e) {
            template = null;
        }

    }

    /**
     * 渲染 Markdown 为 HTML。
     *
     * @param markdown Markdown 文件名
     */
    @RequestMapping(method = GET, value = "/{markdown}")
    public void renderMarkdown(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable(name = "markdown") String markdown
    ) {

        try {

            Map<String, String> params = new HashMap<>();

            String strMarkdown = new String(
                IOUtils.toByteArray(
                    (new ClassPathResource("/developer/" + markdown))
                        .getInputStream()
                )
            );

            params.put("markdown", strMarkdown);

            StringWriter out = new StringWriter();

            template.process(params, out);

            response.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
            response.getWriter().write(out.getBuffer().toString());

        } catch (Exception e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }

    }

}
