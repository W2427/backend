package com.ose.tasks.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;


public class SuggestionEditDTO extends PageDTO {
    private static final long serialVersionUID = -4527532389498934307L;
    private String summary;

    private String description;

    @Schema(description = "临时文件名")
    private String fileName;

    private String priority;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
