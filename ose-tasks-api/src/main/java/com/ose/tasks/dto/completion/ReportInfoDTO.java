package com.ose.tasks.dto.completion;

import com.ose.dto.BaseDTO;

public class ReportInfoDTO extends BaseDTO {

    private String barCode;

    private String fileType;//XLS/DOC/PDF/HTM

    private String pdfFilePath;

    private Long pdfFileId;


    private String xlsFilePath;

    private Long xlsFileId;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getPdfFilePath() {
        return pdfFilePath;
    }

    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }

    public Long getPdfFileId() {
        return pdfFileId;
    }

    public void setPdfFileId(Long pdfFileId) {
        this.pdfFileId = pdfFileId;
    }

    public String getXlsFilePath() {
        return xlsFilePath;
    }

    public void setXlsFilePath(String xlsFilePath) {
        this.xlsFilePath = xlsFilePath;
    }

    public Long getXlsFileId() {
        return xlsFileId;
    }

    public void setXlsFileId(Long xlsFileId) {
        this.xlsFileId = xlsFileId;
    }
}
