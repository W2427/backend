package com.ose.report.dto.subreport;

import com.ose.report.entity.Template;

import java.util.Map;

/**
 * 子报表数据模型（静态模板）
 */
public class TemplateSubReport extends SubReport {

    /**
     * 模板名称
     */
    private Template template;

    /**
     * Mapping数据
     */
    private Map<String, Object> mappingData;

    /**
     * 默认构造器
     */
    public TemplateSubReport() {
    }

    /**
     * 参数构造器
     *
     * @param template    模板
     * @param mappingData Mapping数据
     */
    public TemplateSubReport(Template template, Map<String, Object> mappingData) {
        this.template = template;
        this.mappingData = mappingData;
    }

    /**
     * Gets the value of template.
     *
     * @return the value of template
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * Sets the template.
     *
     * <p>You can use getTemplate() to get the value of template</p>
     *
     * @param template template
     */
    public void setTemplate(Template template) {
        this.template = template;
    }

    /**
     * Gets the value of mappingData.
     *
     * @return the value of mappingData
     */
    public Map<String, Object> getMappingData() {
        return mappingData;
    }

    /**
     * Sets the mappingData.
     *
     * <p>You can use getMappingData() to get the value of mappingData</p>
     *
     * @param mappingData mappingData
     */
    public void setMappingData(Map<String, Object> mappingData) {
        this.mappingData = mappingData;
    }

    public void setTemplateFile(String templateFile) {
        this.template.setTemplateFile(templateFile);
    }

}
