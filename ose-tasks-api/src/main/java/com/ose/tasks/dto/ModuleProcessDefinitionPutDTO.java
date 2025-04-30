package com.ose.tasks.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

/**
 * 模块工作流定义更新数据传输对象。
 */
public class ModuleProcessDefinitionPutDTO {

    @Schema(description = "上传的定义文件的临时文件名")
    @NotBlank
    private String filename;

    @Schema(description = "大流程图名称")
    @NotBlank
    private String bpmnName;

    @Schema(description = "全局工作流程的功能块名称")
    @NotBlank
    private String funcPart;

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getBpmnName() {
        return bpmnName;
    }

    public void setBpmnName(String bpmnName) {
        this.bpmnName = bpmnName;
    }
}
