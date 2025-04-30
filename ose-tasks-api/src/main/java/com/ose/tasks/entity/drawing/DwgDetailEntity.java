package com.ose.tasks.entity.drawing;

public class DwgDetailEntity {
    //图纸编号
    private String dwgNo;
    //版本
    private String dwgRev;

    private String eigenNo;
    // 文件Id
    private String fileId;
    // 文件名
    private String fileName;
    //文件存储路径
    private String ftpFilePath;
    //页码
    private String pageNo;
    // 幅面
    private String pageSize;
    // 日期
    private String rgstDt;
    // 用户Id
    private String rgstUserId;

    public String getDwgNo() {
        return dwgNo;
    }

    public void setDwgNo(String dwgNo) {
        this.dwgNo = dwgNo;
    }

    public String getDwgRev() {
        return dwgRev;
    }

    public void setDwgRev(String dwgRev) {
        this.dwgRev = dwgRev;
    }

    public String getEigenNo() {
        return eigenNo;
    }

    public void setEigenNo(String eigenNo) {
        this.eigenNo = eigenNo;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFtpFilePath() {
        return ftpFilePath;
    }

    public void setFtpFilePath(String ftpFilePath) {
        this.ftpFilePath = ftpFilePath;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getRgstDt() {
        return rgstDt;
    }

    public void setRgstDt(String rgstDt) {
        this.rgstDt = rgstDt;
    }

    public String getRgstUserId() {
        return rgstUserId;
    }

    public void setRgstUserId(String rgstUserId) {
        this.rgstUserId = rgstUserId;
    }
}
