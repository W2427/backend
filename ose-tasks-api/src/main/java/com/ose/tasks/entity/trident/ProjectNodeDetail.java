package com.ose.tasks.entity.trident;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.ose.constant.JsonFormatPattern;
import com.ose.entity.BaseEntity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

/**
     * @author : http://www.chiner.pro
     * @date : 2022-1-3
     * @desc :  */
@Entity
@Table(name="project_node_detail",
    indexes = {
        @Index(columnList = "projectId,entityId"),
        @Index(columnList = "projectId,no"),
        @Index(columnList = "projectId,code"),
        @Index(columnList = "entityId"),
        @Index(columnList = "no"),
})
public class ProjectNodeDetail extends BaseEntity {
    private static final long serialVersionUID = 4834664085021282692L;
    /**  */
    @Column
    private Long projectId;
    /**  */
    @Column
    private Long entityId;
    /**  */
    @Column
    private String no;

    @Column
    private String code;

    /**  */
    @Column
    private String entityType;
    /**  */
    @Column
    private String entitySubType;
    /**  */
    @Column
    private String description;
    /**  */
    @Column
    private String pkg;
    @Column
    private Long packageId;
    @Column
    private Long subSystemId;
    @Column
    private Long subSystemEntityId;
    /**  */
    @Column
    private String subSystem;
    /**  */
    @Column
    private String system;
    @Column
    private Long systemId;
    /**  */
    @Column
    private String subSector;
    @Column
    private Long subSectorId;
    /**  */
    @Column
    private String sector;
    @Column
    private Long sectorId;
    @Column
    private String fGroup;
    /**  */
    @Column
    private Boolean initStatus;

    @Transient
    private String inCharge;

    @Transient
    private List<TagNoInfo> tagNoInfos;

    @Transient
    private List<TagNoUserRelation> tagNoUserRelations;

    @Transient
    private String memo;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    private Date initDate;
    /**  */
    @Column
    private Boolean startStatus;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    private Date startDate;
    /**  */
    @Column
    private Boolean submitStatus;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    private Date submitDate;

    /**  */
    @Column
    private Boolean pulledStatus;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    private Date pulledDate;

    /**  */
    @Column
    private Boolean commCheckOK = false;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    private Date commCheckDate;

    @Column
    private String commCheckStatus;

    /**  */
    @Column(name = "t1_status")
    private Boolean t1Status;

    @Column(name = "t1_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    private Date t1Date;


    /**  */
    @Column(name = "t2_status")
    private Boolean t2Status;

    @Column(name = "t2_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    private Date t2Date;

    /**  */
    @Column
    private Boolean signedStatus;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    private Date signedDate;
    /**  */
    @Column
    private Boolean closedStatus;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    private Date closedDate;

    @Column
    private Boolean isInSkid;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public Long getSubSystemId() {
        return subSystemId;
    }

    public void setSubSystemId(Long subSystemId) {
        this.subSystemId = subSystemId;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getSubSector() {
        return subSector;
    }

    public void setSubSector(String subSector) {
        this.subSector = subSector;
    }

    public Long getSubSectorId() {
        return subSectorId;
    }

    public void setSubSectorId(Long subSectorId) {
        this.subSectorId = subSectorId;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Long getSectorId() {
        return sectorId;
    }

    public void setSectorId(Long sectorId) {
        this.sectorId = sectorId;
    }

    public Boolean getInitStatus() {
        return initStatus;
    }

    public void setInitStatus(Boolean initStatus) {
        this.initStatus = initStatus;
    }

    public Boolean getStartStatus() {
        return startStatus;
    }

    public void setStartStatus(Boolean startStatus) {
        this.startStatus = startStatus;
    }

    public Boolean getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(Boolean submitStatus) {
        this.submitStatus = submitStatus;
    }

    public Boolean getSignedStatus() {
        return signedStatus;
    }

    public void setSignedStatus(Boolean signedStatus) {
        this.signedStatus = signedStatus;
    }

    public Boolean getClosedStatus() {
        return closedStatus;
    }

    public void setClosedStatus(Boolean closedStatus) {
        this.closedStatus = closedStatus;
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public Date getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(Date signedDate) {
        this.signedDate = signedDate;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public Long getSubSystemEntityId() {
        return subSystemEntityId;
    }

    public void setSubSystemEntityId(Long subSystemEntityId) {
        this.subSystemEntityId = subSystemEntityId;
    }

    public String getfGroup() {
        return fGroup;
    }

    public void setfGroup(String fGroup) {
        this.fGroup = fGroup;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<TagNoInfo> getTagNoInfos() {
        return tagNoInfos;
    }

    public void setTagNoInfos(List<TagNoInfo> tagNoInfos) {
        this.tagNoInfos = tagNoInfos;
    }

    public Boolean getPulledStatus() {
        return pulledStatus;
    }

    public void setPulledStatus(Boolean pulledStatus) {
        this.pulledStatus = pulledStatus;
    }

    public Date getPulledDate() {
        return pulledDate;
    }

    public void setPulledDate(Date pulledDate) {
        this.pulledDate = pulledDate;
    }

    public Boolean getT1Status() {
        return t1Status;
    }

    public void setT1Status(Boolean t1Status) {
        this.t1Status = t1Status;
    }

    public Date getT1Date() {
        return t1Date;
    }

    public void setT1Date(Date t1Date) {
        this.t1Date = t1Date;
    }

    public Boolean getT2Status() {
        return t2Status;
    }

    public void setT2Status(Boolean t2Status) {
        this.t2Status = t2Status;
    }

    public Date getT2Date() {
        return t2Date;
    }

    public void setT2Date(Date t2Date) {
        this.t2Date = t2Date;
    }

    public List<TagNoUserRelation> getTagNoUserRelations() {
        return tagNoUserRelations;
    }

    public void setTagNoUserRelations(List<TagNoUserRelation> tagNoUserRelations) {
        this.tagNoUserRelations = tagNoUserRelations;
    }

    public String getInCharge() {
        return inCharge;
    }

    public void setInCharge(String inCharge) {
        this.inCharge = inCharge;
    }

    public Boolean getCommCheckOK() {
        return commCheckOK;
    }

    public void setCommCheckOK(Boolean commCheckOK) {
        this.commCheckOK = commCheckOK;
    }

    public String getCommCheckStatus() {
        return commCheckStatus;
    }

    public void setCommCheckStatus(String commCheckStatus) {
        this.commCheckStatus = commCheckStatus;
    }

    public Date getCommCheckDate() {
        return commCheckDate;
    }

    public void setCommCheckDate(Date commCheckDate) {
        this.commCheckDate = commCheckDate;
    }

    public Boolean getInSkid() {
        return isInSkid;
    }

    public void setInSkid(Boolean inSkid) {
        isInSkid = inSkid;
    }
}
