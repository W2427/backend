package com.ose.tasks.entity.drawing;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 图纸历史表
 */
@Entity
@Table(name = "drawing_history")
public class DrawingHistory extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -7837891255273680991L;

    @Schema(description = "ORG ID 组织ID")
    @Column
    private Long orgId;

    @Column
    private String version;

    @Schema(description = "PROJECT ID 项目ID")
    @Column
    private Long projectId;

    @Schema(description = "图纸二维码")
    @Column
    private String qrCode;

    //图纸条目id
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
    private String verison;

    @Schema(description = "操作者ID")
    @Column
    private Long operator;

//    @Schema(description = "是否发图标记")
//    @Column
//    private Boolean issueFlag = false;

    //数字化测试新增字段
    @Column(name = "vp_id")
    private Long vpId ;

    @Column(name = "vp")
    private String vp ;

    @Column(name = "sh_id")
    private Long shId ;

    @Column(name = "sh")
    private String sh ;


    @Column()
    private Long leadEngineerId ;

    @Column()
    private String leadEngineer ;


    @Column()
    private Long engineerId ;

    @Column(name = "engineer")
    private String engineer ;


    @Column()
    private Double workHour = 0.0 ;

    public DrawingHistory(Drawing drawing){
        this.setCreatedAt();
        this.drawingId = (drawing.getId());
        this.setOrgId(drawing.getOrgId());
        this.setProjectId(drawing.getProjectId());
        this.setStatus(EntityStatus.ACTIVE);
    }

    public DrawingHistory(){

    }

    public Long getVpId() {
        return vpId;
    }

    public void setVpId(Long vpId) {
        this.vpId = vpId;
    }

    public String getVp() {
        return vp;
    }

    public void setVp(String vp) {
        this.vp = vp;
    }

    public Long getShId() {
        return shId;
    }

    public void setShId(Long shId) {
        this.shId = shId;
    }

    public String getSh() {
        return sh;
    }

    public void setSh(String sh) {
        this.sh = sh;
    }

    public Long getLeadEngineerId() {
        return leadEngineerId;
    }

    public void setLeadEngineerId(Long leadEngineerId) {
        this.leadEngineerId = leadEngineerId;
    }

    public String getLeadEngineer() {
        return leadEngineer;
    }

    public void setLeadEngineer(String leadEngineer) {
        this.leadEngineer = leadEngineer;
    }

    public Long getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(Long engineerId) {
        this.engineerId = engineerId;
    }

    public String getEngineer() {
        return engineer;
    }

    public void setEngineer(String engineer) {
        this.engineer = engineer;
    }

    public Double getWorkHour() {
        return workHour;
    }

    public void setWorkHour(Double workHour) {
        this.workHour = workHour;
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

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
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

    public String getVerison() {
        return verison;
    }

    public void setVerison(String verison) {
        this.verison = verison;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
