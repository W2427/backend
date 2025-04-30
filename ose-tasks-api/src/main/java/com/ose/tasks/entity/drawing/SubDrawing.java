package com.ose.tasks.entity.drawing;

import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.vo.drawing.DrawingReviewStatus;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * 子图纸。
 */
@Entity
@Table(
    name = "sub_drawing",
    indexes = {
        @Index(columnList = "pageNo,subDrawingNo,status"),
        @Index(columnList = "orgId,projectId,status,subDrawingNo,pageNo")
    }
)
public class SubDrawing extends WBSEntityBase {

    private static final long serialVersionUID = -7837891255273680991L;

    private Long orgId;

    private Long projectId;

    @Enumerated(EnumType.STRING)
    private DrawingReviewStatus reviewStatus;

    @Column(length = 9)
    private String qrCode;

    //序号
    private Integer seq = 0;

    //图纸条目id
    private Long drawingId;

    //图纸明细id
    private Long drawingDetailId;

    //对应主图纸版本
//    @Transient
    private String drawingVersion;

    //ISO图号
    private String subNo;

    //页数
    private Integer pageCount;

    //ISO版本
    private String latestRev;

    //备注
    private String comment;

    //文件Id
    private Long fileId;

    //文件名
    private String fileName;

    //文件路径
    private String filePath;

    private Long operator;

    //图纸版本
    private String subDrawingVersion;

    //图纸编号
    private String subDrawingNo;

    //页码信息
    private Integer pageNo;

    private Boolean isIssued = false;

    private Long actInstId;

    @Schema(description = "任务Id")
    @Column
    private Long taskId;

    @Schema(description = "简码")
    @Column
    private String shortCode = ".";

    @Schema(description = "redmark标识")
    @Column(length = 16)
    @Enumerated(EnumType.STRING)
    private EntityStatus isRedMark;

    //变量值
    @Column(name = "config_data", columnDefinition = "text")
    private String configData;

    @Transient
    private List<Map<String, Long>> variables;

    @Schema(description = "专业")
    @Column
    private String discipline = "PIPING";

    private String qrCodeStatus;

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

    @Override
    public String getEntitySubType() {
        return null;
    }

    @Override
    public String getEntityBusinessType() {
        return null;
    }

    public String getSubNo() {
        return subNo;
    }

    public void setSubNo(String subNo) {
        this.subNo = subNo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

    public String getConfigData() {
        return configData;
    }

    public void setConfigData(String configData) {
        this.configData = configData;
    }

    @Transient
    public List<Map<String, Long>> getVariables() {
        return variables;
    }

    public void setVariables(List<Map<String, Long>> variables) {
        this.variables = variables;
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

    public int getPageNo() {
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

    public String getSubDrawingNo() {
        return subDrawingNo;
    }

    public void setSubDrawingNo(String subDrawingNo) {
        this.subDrawingNo = subDrawingNo;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public int getSeq() {
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

    public String getDrawingVersion() {
        return drawingVersion;
    }

    public void setDrawingVersion(String drawingVersion) {
        this.drawingVersion = drawingVersion;
    }

    public DrawingReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(DrawingReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
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

    public Boolean getIssued() {
        return isIssued;
    }

    public void setIssued(Boolean issued) {
        isIssued = issued;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    public EntityStatus getIsRedMark() {
        return isRedMark;
    }

    public void setIsRedMark(EntityStatus isRedMark) {
        this.isRedMark = isRedMark;
    }

    public String getQrCodeStatus() {
        return qrCodeStatus;
    }

    public void setQrCodeStatus(String qrCodeStatus) {
        this.qrCodeStatus = qrCodeStatus;
    }

//    @Schema(description = "图集主表")
//    @JsonProperty(value = "drawingId", access = READ_ONLY)
//    public ReferenceData getDrawingIdRef() {
//        return this.drawingId == null
//            ? null
//            : new ReferenceData(this.drawingId);
//    }
}
