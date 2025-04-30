package com.ose.tasks.entity.drawing.externalDrawing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 修改单
 */
@Entity
@Table(name = "drawing_amendment")
public class DrawingAmendment extends BaseBizEntity {

    private static final long serialVersionUID = 8609193672285765769L;
    private Long orgId;

    private Long projectId;

    @Column(length = 9)
    private String qrCode;

    private Long batchTaskId;
    //修改单编号
    private String no;

    //图号
    private String dwgNo;

    //文件名称
    private String drawingTitle;

    //预估工时
    @Column(columnDefinition = "decimal(9,1) default 0.0")
    private Double estimatedManHours;

    private Date engineeringFinishDate;

    private Date engineeringStartDate;

    //工作版本版本
    private String latestRev;

    //最新有效版本
    private String latestApprovedRev;
    //图纸类型
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drawing_category_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BpmEntitySubType drawingCategory;

    //图纸文件id
    private Long fileId;

    //图纸文件名称
    private String fileName;

    //图纸文件路径
    private String filePath;

    //图纸文件最新版本
    private int fileLastVersion;

    //图纸文件页数
    private String filePageCount;

    private Long operator;
    //图纸封面文件id
    private Long coverId;

    //图纸封面文件Name
    private String coverName;

    //图纸封面文件路径
    private String coverPath;

    @Transient
    private String diagramResource;

    //制图人
    private String drawer;

    //制图人id
    private Long drawerId;

    private String memo;

    @Schema(description = "专业")
    @Column
    private String discipline = "PIPING";

    private String areaName;

    private String moduleName;

    @Schema(description = "工作网络")
    private String workNet;

    @Schema(description = "分段")
    private String section;

    @Schema(description = "总段")
    private String block;

    @Schema(description = "小区域")
    private String smallArea;

    @Schema(description = "设计专业")
    private String designDiscipline;

    @Schema(description = "安装/制作图号")
    private String installationDrawingNo;

    public Long getBatchTaskId() {
        return batchTaskId;
    }

    public void setBatchTaskId(Long batchTaskId) {
        this.batchTaskId = batchTaskId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDwgNo() {
        return dwgNo;
    }

    public void setDwgNo(String dwgNo) {
        this.dwgNo = dwgNo;
    }

    public String getDrawingTitle() {
        return drawingTitle;
    }

    public void setDrawingTitle(String drawingTitle) {
        this.drawingTitle = drawingTitle;
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

    public int getFileLastVersion() {
        return fileLastVersion;
    }

    public void setFileLastVersion(int fileLastVersion) {
        this.fileLastVersion = fileLastVersion;
    }

    public String getFilePageCount() {
        return filePageCount;
    }

    public void setFilePageCount(String filePageCount) {
        this.filePageCount = filePageCount;
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

    public Long getCoverId() {
        return coverId;
    }

    public void setCoverId(Long coverId) {
        this.coverId = coverId;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getCoverName() {
        return coverName;
    }

    public void setCoverName(String coverName) {
        this.coverName = coverName;
    }

    @Transient
    public String getDiagramResource() {
        return diagramResource;
    }

    public void setDiagramResource(String diagramResource) {
        this.diagramResource = diagramResource;
    }
    public BpmEntitySubType getDrawingCategory() {
        return drawingCategory;
    }

    public void setDrawingCategory(BpmEntitySubType drawingCategory) {
        this.drawingCategory = drawingCategory;
    }

    public Date getEngineeringFinishDate() {
        return engineeringFinishDate;
    }

    public void setEngineeringFinishDate(Date engineeringFinishDate) {
        this.engineeringFinishDate = engineeringFinishDate;
    }

    public Date getEngineeringStartDate() {
        return engineeringStartDate;
    }

    public void setEngineeringStartDate(Date engineeringStartDate) {
        this.engineeringStartDate = engineeringStartDate;
    }

    public String getDrawer() {
        return drawer;
    }

    public void setDrawer(String drawer) {
        this.drawer = drawer;
    }

    public Long getDrawerId() {
        return drawerId;
    }

    public void setDrawerId(Long drawerId) {
        this.drawerId = drawerId;
    }

    public Double getEstimatedManHours() {
        return estimatedManHours;
    }

    public void setEstimatedManHours(Double estimatedManHours) {
        this.estimatedManHours = estimatedManHours;
    }


    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getLatestRev() {
        return latestRev;
    }

    public void setLatestRev(String latestRev) {
        this.latestRev = latestRev;
    }
    public String getLatestApprovedRev() {
        return latestApprovedRev;
    }

    public void setLatestApprovedRev(String latestApprovedRev) {
        this.latestApprovedRev = latestApprovedRev;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getWorkNet() {
        return workNet;
    }

    public void setWorkNet(String workNet) {
        this.workNet = workNet;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getSmallArea() {
        return smallArea;
    }

    public void setSmallArea(String smallArea) {
        this.smallArea = smallArea;
    }

    public String getDesignDiscipline() {
        return designDiscipline;
    }

    public void setDesignDiscipline(String designDiscipline) {
        this.designDiscipline = designDiscipline;
    }

    public String getInstallationDrawingNo() {
        return installationDrawingNo;
    }

    public void setInstallationDrawingNo(String installationDrawingNo) {
        this.installationDrawingNo = installationDrawingNo;
    }
}
