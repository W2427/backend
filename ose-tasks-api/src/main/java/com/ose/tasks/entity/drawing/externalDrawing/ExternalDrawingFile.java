package com.ose.tasks.entity.drawing.externalDrawing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.drawing.DocScope;
import com.ose.tasks.vo.drawing.DocType;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 输入图纸历史表
 */
@Entity
@Table(name = "external_drawing_file")
public class ExternalDrawingFile extends BaseBizEntity {

    private static final long serialVersionUID = -3333525439469140573L;
    /**
     *
     */

    @Schema(description = "ORG ID 组织ID")
    @Column
    private Long orgId;

    @Schema(description = "PROJECT ID 项目ID")
    @Column
    private Long projectId;

    @Schema(description = "图纸二维码")
    @Column
    private String qrCode;

    //图纸条目id
    @Schema(description = "图纸ID")
    @Column
    private Long historyDrawingId;

    @Schema(description = "图纸ID")
    @Column
    private Long drawingId;

    //文件id
    @Schema(description = "文件ID")
    @Column
    private Long fileId;

    //文件名
    @Schema(description = "文件名")
    @Column
    private String fileName;

    @Schema(description = "文件种类")
    @Column
    private String fileType;

    //
    @Schema(description = "文件路径")
    @Column
    private String filePath;

    //文件页数
    @Schema(description = "ORG ID 组织ID")
    @Column
    private String filePageCount;

    //
    @Schema(description = "备注")
    @Column
    private String memo;

    //
    @Schema(description = "版本号")
    @Column
    private String version;

    @Schema(description = "操作者ID")
    @Column
    private Long operator;

    @Schema(description = "MD5码")
    @Column
    private String md5Value;

    @Schema(description = "图类型，包括PDF，NATIVE，ATTACHMENT，CLASS_COMMENT，OWNER_COMMENT")
    @Enumerated(EnumType.STRING)
    private DocType docType;

    @Transient
    private String dwgNo;

    @Transient
    private String rev;

    @Transient
    private DocScope docScope;

    @Transient
    private String dwgTitle;

    @Transient
    private String discipline;

    @Column
    private Integer pageCount;

    public ExternalDrawingFile(ExternalDrawing drawing){
        this.setCreatedAt();
        this.historyDrawingId = (drawing.getId());
        this.setOrgId(drawing.getOrgId());
        this.setProjectId(drawing.getProjectId());
        this.setStatus(EntityStatus.ACTIVE);
    }

    public ExternalDrawingFile(){

    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

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

    public String getFilePageCount() {
        return filePageCount;
    }

    public void setFilePageCount(String filePageCount) {
        this.filePageCount = filePageCount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    /**
     * 取得操作人引用信息。
     *
     * @return 操作人引用信息
     */
    @JsonProperty(value = "operator", access = READ_ONLY)
    public ReferenceData getOperatorRef() {
        return this.operator == null
            ? null
            : new ReferenceData(this.operator);
    }

    @Override
    public Set<Long> relatedUserIDs() {

        Set<Long> userIDs = new HashSet<>();

        if (this.getOperator() != null && this.getOperator() != 0L) {
            userIDs.add(this.getOperator());
        }

        return userIDs;
    }

    public Long getHistoryDrawingId() {
        return historyDrawingId;
    }

    public void setHistoryDrawingId(Long historyDrawingId) {
        this.historyDrawingId = historyDrawingId;
    }

    public DocType getDocType() {
        return docType;
    }

    public void setDocType(DocType docType) {
        this.docType = docType;
    }

    public String getMd5Value() {
        return md5Value;
    }

    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value;
    }

    public String getDwgNo() {
        return dwgNo;
    }

    public void setDwgNo(String dwgNo) {
        this.dwgNo = dwgNo;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getDwgTitle() {
        return dwgTitle;
    }

    public void setDwgTitle(String dwgTitle) {
        this.dwgTitle = dwgTitle;
    }

    public DocScope getDocScope() {
        return docScope;
    }

    public void setDocScope(DocScope docScope) {
        this.docScope = docScope;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }
}
