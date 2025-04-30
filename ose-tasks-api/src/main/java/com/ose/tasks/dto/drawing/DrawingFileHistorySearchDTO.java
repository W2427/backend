package com.ose.tasks.dto.drawing;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 图纸文件历史 数据传输对象
 */
public class DrawingFileHistorySearchDTO extends PageDTO {

    private static final long serialVersionUID = -1739330183972690454L;
    @Schema(description = "搜索关键字")
    private String keyword;

    @Schema(description = "搜索关键字")
    private Long drawingFileId;

    @Schema(description = "搜索关键字")
    private Long procInstId;

    @Schema(description = "搜索关键字")
    private String drawingFileType;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getDrawingFileId() {
        return drawingFileId;
    }

    public void setDrawingFileId(Long drawingFileId) {
        this.drawingFileId = drawingFileId;
    }

    public Long getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(Long procInstId) {
        this.procInstId = procInstId;
    }

    public String getDrawingFileType() {
        return drawingFileType;
    }

    public void setDrawingFileType(String drawingFileType) {
        this.drawingFileType = drawingFileType;
    }
}
