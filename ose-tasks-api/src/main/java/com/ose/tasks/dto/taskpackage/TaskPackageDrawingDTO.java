package com.ose.tasks.dto.taskpackage;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigInteger;

/**
 * 任务包图纸数据传输对象。
 */
public class TaskPackageDrawingDTO extends BaseDTO {

    private static final long serialVersionUID = -2755327424534295574L;

    @Schema(description = "管线编号")
    private String isoNo;

    @Schema(description = "主图纸 ID")
    private Long drawingId;

    @Schema(description = "主图纸编号")
    private String drawingNo;

    @Schema(description = "子图纸 ID")
    private Long subDrawingId;

    @Schema(description = "子图纸编号")
    private String subDrawingNo;

    @Schema(description = "页号")
    private Integer pageNo;

    @Schema(description = "子图纸版本")
    private String subDrawingVersion;

    @Schema(description = "子图纸文件 ID")
    private Long subDrawingFileId;

    public TaskPackageDrawingDTO() {
    }

    public TaskPackageDrawingDTO(Object[] columns) {

        if (!(columns != null && columns.length == 9)) {
            return;
        }

        setIsoNo((String) columns[1]);
        setDrawingId(Long.valueOf(((BigInteger) columns[2]).toString()));
        setDrawingNo((String) columns[3]);
        setSubDrawingId(Long.valueOf(((BigInteger) columns[4]).toString()));
        setSubDrawingNo((String) columns[5]);
        setPageNo((Integer) columns[6]);
        setSubDrawingVersion((String) columns[7]);
        setSubDrawingFileId(Long.valueOf(((BigInteger) columns[8]).toString()));
    }

    public String getIsoNo() {
        return isoNo;
    }

    public void setIsoNo(String isoNo) {
        this.isoNo = isoNo;
    }

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public Long getSubDrawingId() {
        return subDrawingId;
    }

    public void setSubDrawingId(Long subDrawingId) {
        this.subDrawingId = subDrawingId;
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

    public String getSubDrawingVersion() {
        return subDrawingVersion;
    }

    public void setSubDrawingVersion(String subDrawingVersion) {
        this.subDrawingVersion = subDrawingVersion;
    }

    public Long getSubDrawingFileId() {
        return subDrawingFileId;
    }

    public void setSubDrawingFileId(Long subDrawingFileId) {
        this.subDrawingFileId = subDrawingFileId;
    }
}
