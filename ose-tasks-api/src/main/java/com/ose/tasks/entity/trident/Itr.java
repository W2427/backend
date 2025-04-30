package com.ose.tasks.entity.trident;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.trident.CheckSheetStage;
import com.ose.tasks.vo.trident.CheckSheetTridentTbl;
import com.ose.tasks.vo.trident.CheckSheetType;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;


@Entity
@Table ( name ="itr",
indexes = {
    @Index(columnList = "entityId")
})
public class Itr extends BaseBizEntity {


    private static final long serialVersionUID = 4429806370382676753L;
    public Itr() {

    }

    public Itr(Long id,
               String entityNo,
               String process,
               String entitySubType, String entityType) {
        super();
        super.setId(id);
        this.entityNo = entityNo;
        this.process = process;
        this.entitySubType = entitySubType;
        this.entityType = entityType;
    }

    @Column(name = "trident_check_sheet_id" )
	private Integer tridentCheckSheetId;

   	@Column(name = "trident_completed_date" )
	private Date tridentCompletedDate;

    @Column(name = "trident_tag_id" )
    private Integer tridentTagId;

    @Column(name = "version" )
    private Long version;

    @Column(name = "trident_itr_id" )
    private Integer tridentItrId;

   	@Column(name = "trident_file_ref")
	private String tridentFileRef;

    @Column(name = "trident_proj_phase_id" )
    private Integer tridentProjPhaseId;

    @Column(name = "trident_is_complete" )
    private Boolean tridentIsComplete;

    @Column(name = "trident_print_date" )
    private Date tridentPrintDate;

    @Column(name = "trident_signed_date" )
    private Date tridentSignedDate;

   	@Column
    private Long projectId;

   	@Column
    private Long fileId;

   	@Column
    private String itrCheckTemplate;

   	@Column
    private Long entityId;

   	@Column
    private String entityNo;

   	@Column
    private Long inChargePerson;

    @Column
    @Enumerated(EnumType.STRING)
    private CheckSheetStage checkSheetStage;

    @Column
    @Enumerated(EnumType.STRING)
    private CheckSheetTridentTbl checkSheetTridentTbl;

   	@Column
    @Enumerated(EnumType.STRING)
    private CheckSheetType checkSheetType;

    @Column
    private Boolean isInTrident;

    @Transient
    private String process;

    @Transient
    private String stage;

    @Column
    private Long processId;

    @Column
    private Long entitySubTypeId;

    @Transient
    private String entitySubType;

    @Transient
    private String entityType;

    @Schema(description = "完成状态")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private EntityStatus runningStatus; //INIT 初始化，PRINTED 已打印，SINGED 已签署， CLOSED 已关闭

    public Integer getTridentCheckSheetId() {
        return tridentCheckSheetId;
    }

    public void setTridentCheckSheetId(Integer tridentCheckSheetId) {
        this.tridentCheckSheetId = tridentCheckSheetId;
    }

    public Date getTridentCompletedDate() {
        return tridentCompletedDate;
    }

    public void setTridentCompletedDate(Date tridentCompletedDate) {
        this.tridentCompletedDate = tridentCompletedDate;
    }

    public Integer getTridentTagId() {
        return tridentTagId;
    }

    public void setTridentTagId(Integer tridentTagId) {
        this.tridentTagId = tridentTagId;
    }

    public String getTridentFileRef() {
        return tridentFileRef;
    }

    public void setTridentFileRef(String tridentFileRef) {
        this.tridentFileRef = tridentFileRef;
    }

    public Integer getTridentProjPhaseId() {
        return tridentProjPhaseId;
    }

    public void setTridentProjPhaseId(Integer tridentProjPhaseId) {
        this.tridentProjPhaseId = tridentProjPhaseId;
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

    public String getItrCheckTemplate() {
        return itrCheckTemplate;
    }

    public void setItrCheckTemplate(String itrCheckTemplate) {
        this.itrCheckTemplate = itrCheckTemplate;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public Long getInChargePerson() {
        return inChargePerson;
    }

    public void setInChargePerson(Long inChargePerson) {
        this.inChargePerson = inChargePerson;
    }

    public CheckSheetStage getCheckSheetStage() {
        return checkSheetStage;
    }

    public void setCheckSheetStage(CheckSheetStage checkSheetStage) {
        this.checkSheetStage = checkSheetStage;
    }

    public CheckSheetType getCheckSheetType() {
        return checkSheetType;
    }

    public void setCheckSheetType(CheckSheetType checkSheetType) {
        this.checkSheetType = checkSheetType;
    }

    public Boolean getInTrident() {
        return isInTrident;
    }

    public void setInTrident(Boolean inTrident) {
        isInTrident = inTrident;
    }

    public Integer getTridentItrId() {
        return tridentItrId;
    }

    public void setTridentItrId(Integer tridentItrId) {
        this.tridentItrId = tridentItrId;
    }

    public CheckSheetTridentTbl getCheckSheetTridentTbl() {
        return checkSheetTridentTbl;
    }

    public void setCheckSheetTridentTbl(CheckSheetTridentTbl checkSheetTridentTbl) {
        this.checkSheetTridentTbl = checkSheetTridentTbl;
    }

    public EntityStatus getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(EntityStatus runningStatus) {
        this.runningStatus = runningStatus;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Boolean getTridentIsComplete() {
        return tridentIsComplete;
    }

    public void setTridentIsComplet(Boolean tridentIsComplete) {
        this.tridentIsComplete = tridentIsComplete;
    }

    public Date getTridentPrintDate() {
        return tridentPrintDate;
    }

    public void setTridentPrintDate(Date tridentPrintDate) {
        this.tridentPrintDate = tridentPrintDate;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setTridentIsComplete(Boolean tridentIsComplete) {
        this.tridentIsComplete = tridentIsComplete;
    }

    public Date getTridentSignedDate() {
        return tridentSignedDate;
    }

    public void setTridentSignedDate(Date tridentSignedDate) {
        this.tridentSignedDate = tridentSignedDate;
    }
}
