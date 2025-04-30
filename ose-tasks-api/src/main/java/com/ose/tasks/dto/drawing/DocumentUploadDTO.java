package com.ose.tasks.dto.drawing;

import com.ose.tasks.dto.bpm.BaseBatchTaskCriteriaDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 传输对象
 */
public class DocumentUploadDTO extends BaseBatchTaskCriteriaDTO {

    private static final long serialVersionUID = 7205942242258223571L;
    @Schema(description = "备注")
    private String comment;

    @Schema(description = "标签")
    private List<String> label;

    @Schema(description = "临时文件名")
    private String fileName;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
