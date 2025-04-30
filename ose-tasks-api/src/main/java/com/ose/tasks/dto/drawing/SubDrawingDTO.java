package com.ose.tasks.dto.drawing;

import java.util.List;
import java.util.Map;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 传输对象
 */
public class SubDrawingDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    /*    @Schema(description = "ISO图号")
        private String subNo;
        */
    @Schema(description = "页数")
    private Integer pageCount;

    @Schema(description = "图纸详情id")
    private Long drawingDetailId;

    @Schema(description = "序号")
    private Integer seq = 0;

    @Schema(description = "备注")
    private String comment;

    @Schema(description = "图纸版本")
    private String subDrawingVersion;

    @Schema(description = "图纸编号")
    private String subDrawingNo;

    @Schema(description = "页码")
    private Integer pageNo;

    @Schema(description = "临时文件名")
    private String fileName;

    @Schema(description = "流程id")
    private Long actInstId;

    @Schema(description = "简码")
    private String shortCode;

    private List<Map<String, String>> variables;

   /* public String getSubNo() {
        return subNo;
    }

    public void setSubNo(String subNo) {
        this.subNo = subNo;
    }*/

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<Map<String, String>> getVariables() {
        return variables;
    }

    public void setVariables(List<Map<String, String>> variables) {
        this.variables = variables;
    }

    public String getSubDrawingVersion() {
        return subDrawingVersion;
    }

    public void setSubDrawingVersion(String subDrawingVersion) {
        this.subDrawingVersion = subDrawingVersion;
    }

    public String getSubDrawingNo() {
        return subDrawingNo;
    }

    public void setSubDrawingNo(String subDrawingNo) {
        this.subDrawingNo = subDrawingNo;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Long getDrawingDetailId() {
        return drawingDetailId;
    }

    public void setDrawingDetailId(Long drawingDetailId) {
        this.drawingDetailId = drawingDetailId;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
}
