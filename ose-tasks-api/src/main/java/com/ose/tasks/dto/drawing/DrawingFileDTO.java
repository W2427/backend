package com.ose.tasks.dto.drawing;

import java.util.List;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.drawing.SubDrawing;

/**
 * 实体管理 数据传输对象
 */
public class DrawingFileDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    private Long fileId;

    private String fileName;

    private String filePath;

    private String qrCode;

    private List<SubDrawing> subDrawingList;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }


    public List<SubDrawing> getSubDrawingList() {
        return subDrawingList;
    }

    public void setSubDrawingList(List<SubDrawing> subDrawingList) {
        this.subDrawingList = subDrawingList;
    }
}
