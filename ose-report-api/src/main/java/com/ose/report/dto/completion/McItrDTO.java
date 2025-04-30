package com.ose.report.dto.completion;

import com.ose.report.dto.BaseItrListReportDTO;

import java.util.List;
import java.util.Map;

public class McItrDTO extends BaseItrListReportDTO {
    private static final long serialVersionUID = -1327942956513275759L;
    private String projectName;
    private String title;
    private String serialNo;
    private String reportNo;
    private Long templateId;
    private String templateFilePath;
    private List itemLists;
    private Map<String, Object> parameters;


    public McItrDTO() {

    }

    public void setItems(List itemLists) {
        this.itemLists = itemLists;
    }

    @Override
    public List getItems() {
        return itemLists;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    @Override
    public String getSerialNo() {
        return serialNo;
    }

    @Override
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getTemplateFilePath() {
        return templateFilePath;
    }

    public void setTemplateFilePath(String templateFilePath) {
        this.templateFilePath = templateFilePath;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
}
